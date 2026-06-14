package online.remind.remind.entity.spirits;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.dreameater.DreamEater;
import online.remind.remind.dreameater.ModDreamEaters;
import online.remind.remind.entity.ModEntitiesRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

public class KomoryBatEntity extends PathfinderMob implements GeoEntity {

    public static final int VARIANT_NORMAL = 0;
    public static final int VARIANT_ORG = 1;

    private static final double SONIC_BOOM_RANGE = 14.0D;
    private static final double SONIC_BOOM_RADIUS = 1.35D;
    private static final int SONIC_BOOM_WINDUP_TICKS = 22;
    private static final int SONIC_BOOM_HIT_TICK = 11;

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(KomoryBatEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID =
            SynchedEntityData.defineId(KomoryBatEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> ATTACK_ANIM_TICKS =
            SynchedEntityData.defineId(KomoryBatEntity.class, EntityDataSerializers.INT);

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("attack");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private int attackCooldown = 0;
    private boolean didSonicBoomHit = false;
    private LivingEntity target;

    public KomoryBatEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.setNoGravity(true);
    }

    public KomoryBatEntity(Level level, Player owner) {
        this(ModEntitiesRM.TYPE_KOMORY_BAT.get(), level);
        this.setOwnerUUID(owner.getUUID());
        this.setNoGravity(true);
        this.setPersistenceRequired();
        this.applyOwnerScaling(owner);
        this.setHealth(this.getMaxHealth());
    }

    private void flyToward(Vec3 targetPos, double speed) {
        Vec3 currentPos = this.position();
        Vec3 difference = targetPos.subtract(currentPos);
        double distance = difference.length();

        if (distance < 0.15D) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.65D));
            return;
        }

        Vec3 wantedMotion = difference.normalize().scale(speed);
        Vec3 currentMotion = this.getDeltaMovement();

        Vec3 newMotion = currentMotion.scale(0.72D).add(wantedMotion.scale(0.28D));

        if (newMotion.length() > speed) {
            newMotion = newMotion.normalize().scale(speed);
        }

        this.setDeltaMovement(newMotion);
        this.hasImpulse = true;

        this.getLookControl().setLookAt(targetPos.x, targetPos.y, targetPos.z, 20.0F, 20.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 34.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.36D)
                .add(Attributes.FLYING_SPEED, 0.52D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.1D);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(false);
        return navigation;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        this.setNoGravity(true);

        if (this.level().isClientSide) {
            return;
        }

        UUID ownerId = this.getOwnerUUID();

        if (ownerId == null) {
            this.discard();
            return;
        }

        ServerPlayer owner = getOwnerPlayerFromServer(ownerId);

        // Owner logged out, changed dimension, or is no longer loaded in this level.
        if (owner == null || owner.level() != this.level()) {
            if (owner != null) {
                GlobalDataRM ownerData = ModDataRM.getGlobal(owner);

                if (ownerData != null) {
                    clearDreamEaterData(owner, ownerData);
                }
            }

            this.discard();
            return;
        }

        GlobalDataRM data = ModDataRM.getGlobal(owner);

        if (data == null) {
            this.discard();
            return;
        }

        if (owner.isDeadOrDying()) {
            clearDreamEaterData(owner, data);
            this.discard();
            return;
        }

        if (!this.isAlive() || this.isDeadOrDying()) {
            clearDreamEaterData(owner, data);
            this.discard();
            return;
        }

        if (!isSelectedDreamEaterKomoryBat(data)) {
            clearDreamEaterData(owner, data);
            this.discard();
            return;
        }

        if (this.tickCount % 40 == 0) {
            this.applyOwnerScaling(owner);
            this.updateVariantFromOwner(owner);
        }

        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        }

        if (this.getAttackAnimTicks() > 0) {
            this.setAttackAnimTicks(this.getAttackAnimTicks() - 1);

            if (!this.didSonicBoomHit && this.getAttackAnimTicks() <= SONIC_BOOM_HIT_TICK) {
                this.didSonicBoomHit = true;
                this.doSonicBoomAttack(owner);
            }
        }

        followOwner(owner);
        updateCombatTarget(owner);
        tryStartSonicBoom(owner);
    }

    @Nullable
    private ServerPlayer getOwnerPlayerFromServer(UUID ownerId) {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return null;
        }

        MinecraftServer server = serverLevel.getServer();

        if (server == null) {
            return null;
        }

        return server.getPlayerList().getPlayer(ownerId);
    }

    private void followOwner(Player owner) {
        double distSqr = this.distanceToSqr(owner);

        if (distSqr > 400.0D) {
            this.moveTo(owner.getX(), owner.getY() + 2.4D, owner.getZ(), owner.getYRot(), owner.getXRot());
            this.setDeltaMovement(Vec3.ZERO);
            this.getNavigation().stop();
            return;
        }

        if (this.getAttackAnimTicks() > 0) {
            return;
        }

        Vec3 hoverPos = owner.position().add(0.0D, 2.35D, 0.0D);

        if (distSqr > 4.0D) {
            flyToward(hoverPos, 0.22D);
        } else {
            Vec3 bob = new Vec3(
                    Math.sin(this.tickCount * 0.08D) * 0.015D,
                    Math.sin(this.tickCount * 0.12D) * 0.025D,
                    Math.cos(this.tickCount * 0.08D) * 0.015D
            );

            this.setDeltaMovement(this.getDeltaMovement().scale(0.75D).add(bob));
        }
    }

    private void updateCombatTarget(Player owner) {
        if (this.target != null) {
            if (canKomoryBatAttack(owner, this.target) && this.distanceToSqr(this.target) < 256.0D) {
                return;
            }

            this.target = null;
        }

        LivingEntity ownerAttacker = owner.getLastHurtByMob();

        if (canKomoryBatAttack(owner, ownerAttacker) && owner.distanceToSqr(ownerAttacker) < 256.0D) {
            this.target = ownerAttacker;
            return;
        }

        LivingEntity ownerTarget = owner.getLastHurtMob();

        if (canKomoryBatAttack(owner, ownerTarget) && owner.distanceToSqr(ownerTarget) < 256.0D) {
            this.target = ownerTarget;
            return;
        }

        AABB box = owner.getBoundingBox().inflate(12.0D, 6.0D, 12.0D);

        this.target = this.level().getEntitiesOfClass(
                        LivingEntity.class,
                        box,
                        entity -> canKomoryBatAttack(owner, entity)
                )
                .stream()
                .min(Comparator.comparingDouble(entity -> entity.distanceToSqr(owner)))
                .orElse(null);
    }

    private void tryStartSonicBoom(Player owner) {
        if (this.attackCooldown > 0 || this.getAttackAnimTicks() > 0) {
            return;
        }

        if (this.target == null || !canKomoryBatAttack(owner, this.target)) {
            this.target = null;
            return;
        }

        Vec3 targetPos = this.target.position().add(0.0D, this.target.getBbHeight() * 0.55D, 0.0D);
        double distSqr = this.distanceToSqr(this.target);

        // Ranged attack: only move closer if outside sonic boom range.
        if (distSqr > SONIC_BOOM_RANGE * SONIC_BOOM_RANGE) {
            flyToward(targetPos, 0.28D);
            return;
        }

        // Stop and face the target before blasting.

        this.getNavigation().stop();
        faceTargetForAttack(this.target);

        Vec3 awayFromTarget = this.position().subtract(this.target.position());

        if (awayFromTarget.lengthSqr() > 0.001D) {
            // Small recoil/rear-back motion, not a melee lunge.
            this.setDeltaMovement(awayFromTarget.normalize().scale(0.10D).add(0.0D, 0.05D, 0.0D));
        } else {
            this.setDeltaMovement(0.0D, 0.05D, 0.0D);
        }

        this.setAttackAnimTicks(SONIC_BOOM_WINDUP_TICKS);
        this.didSonicBoomHit = false;
        this.attackCooldown = 70;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.91D));
        } else {
            super.travel(travelVector);
        }
    }

    private void faceTargetForAttack(LivingEntity target) {
        if (target == null) {
            return;
        }

        Vec3 eyePos = this.position().add(0.0D, this.getBbHeight() * 0.55D, 0.0D);
        Vec3 targetPos = target.position().add(0.0D, target.getBbHeight() * 0.55D, 0.0D);

        double dx = targetPos.x - eyePos.x;
        double dy = targetPos.y - eyePos.y;
        double dz = targetPos.z - eyePos.z;

        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) (Mth.atan2(dz, dx) * (180F / Math.PI)) - 90F;
        float pitch = (float) -(Mth.atan2(dy, horizontalDistance) * (180F / Math.PI));

        // Whole body turn
        this.setYRot(yaw);
        this.yRotO = yaw;
        this.yBodyRot = yaw;
        this.yBodyRotO = yaw;

        // Head follows body
        this.yHeadRot = yaw;
        this.yHeadRotO = yaw;

        // Slight vertical aim for flying/ranged attack
        this.setXRot(Mth.clamp(pitch, -35F, 35F));
        this.xRotO = this.getXRot();

        this.getLookControl().setLookAt(target, 30.0F, 30.0F);
    }

    private void doSonicBoomAttack(Player owner) {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        if (this.target == null || !this.target.isAlive()) {
            return;
        }

        Vec3 origin = this.position().add(0.0D, this.getBbHeight() * 0.55D, 0.0D);
        Vec3 targetPoint = this.target.position().add(0.0D, this.target.getBbHeight() * 0.55D, 0.0D);

        Vec3 direction = targetPoint.subtract(origin);

        if (direction.lengthSqr() < 0.001D) {
            return;
        }

        direction = direction.normalize();

        double damage = this.getAttributeValue(Attributes.ATTACK_DAMAGE);

        Vec3 end = origin.add(direction.scale(SONIC_BOOM_RANGE));

        AABB searchBox = new AABB(origin, end).inflate(SONIC_BOOM_RADIUS + 0.75D);

        for (LivingEntity entity : this.level().getEntitiesOfClass(
                LivingEntity.class,
                searchBox,
                entity -> canKomoryBatAttack(owner, entity)
        )) {
            Vec3 entityCenter = entity.position().add(0.0D, entity.getBbHeight() * 0.5D, 0.0D);
            Vec3 fromOrigin = entityCenter.subtract(origin);

            double distanceAlongBeam = fromOrigin.dot(direction);

            if (distanceAlongBeam < 0.0D || distanceAlongBeam > SONIC_BOOM_RANGE) {
                continue;
            }

            Vec3 closestPoint = origin.add(direction.scale(distanceAlongBeam));
            double distanceFromBeam = entityCenter.distanceTo(closestPoint);

            double allowedRadius = SONIC_BOOM_RADIUS + (entity.getBbWidth() * 0.5D);

            if (distanceFromBeam > allowedRadius) {
                continue;
            }

            entity.hurt(this.damageSources().mobAttack(this), (float) damage);

            Vec3 knockback = direction.scale(0.75D).add(0.0D, 0.12D, 0.0D);
            entity.push(knockback.x, knockback.y, knockback.z);
        }

        this.level().playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SoundEvents.WARDEN_SONIC_BOOM,
                SoundSource.NEUTRAL,
                0.8F,
                1.45F
        );

        spawnSonicBoomLine(serverLevel, origin, direction);
    }

    private void spawnSonicBoomLine(ServerLevel serverLevel, Vec3 origin, Vec3 direction) {
        for (double distance = 1.0D; distance <= SONIC_BOOM_RANGE; distance += 0.9D) {
            Vec3 pos = origin.add(direction.scale(distance));

            serverLevel.sendParticles(
                    ParticleTypes.CLOUD,
                    pos.x,
                    pos.y,
                    pos.z,
                    6,
                    0.25D,
                    0.12D,
                    0.25D,
                    0.035D
            );

            if (distance == 1.0D || distance > SONIC_BOOM_RANGE - 1.2D) {
                serverLevel.sendParticles(
                        ParticleTypes.SONIC_BOOM,
                        pos.x,
                        pos.y,
                        pos.z,
                        1,
                        0.0D,
                        0.0D,
                        0.0D,
                        0.0D
                );
            }
        }
    }

    private void applyOwnerScaling(Player owner) {
        PlayerData ownerData = PlayerData.get(owner);

        if (ownerData == null) {
            return;
        }

        int level = Mth.clamp(ownerData.getLevel(), 1, 100);

        double hp = 32.7D + ((level - 1) * 0.65D);
        double strength = 8.2D + (level * 1.9D);
        double magic = 10.8D + ((level - 1) * 2.60D);
        double defense = 5.9D + (level * 0.05D);

        if (this.getAttribute(Attributes.MAX_HEALTH) != null) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(hp);
        }

        if (this.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(strength);
        }

        if (this.getAttribute(Attributes.ARMOR) != null) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(defense);
        }

        if (this.getAttribute(Attributes.FLYING_SPEED) != null) {
            this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.52D);
        }

        if (this.getHealth() > this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
    }

    private void updateVariantFromOwner(Player owner) {
        PlayerData ownerData = PlayerData.get(owner);

        if (ownerData == null) {
            return;
        }

        this.setVariant(ownerData.getAlignment() != Utils.OrgMember.NONE ? VARIANT_ORG : VARIANT_NORMAL);
    }

    private boolean isSelectedDreamEaterKomoryBat(GlobalDataRM data) {
        String dreamEaterRL = data.getDreamEaterRL();

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            return false;
        }

        DreamEater dreamEater = ModDreamEaters.registry.get(ResourceLocation.parse(dreamEaterRL));

        if (dreamEater == null) {
            return false;
        }

        return StringsRM.komoryBat.equals(dreamEater.getName());
    }

    private void clearDreamEaterData(Player owner, GlobalDataRM data) {
        data.setHasDreamEaterSummoned(false);
        data.setDreamEaterUUID(null);
        PacketHandlerRM.syncGlobalToAllAround(owner, data);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isDamageFromOwner(source)) {
            return false;
        }

        return super.hurt(source, amount);
    }

    private boolean isDamageFromOwner(DamageSource source) {
        UUID ownerId = this.getOwnerUUID();

        if (ownerId == null || source == null) {
            return false;
        }

        Entity attacker = source.getEntity();
        Entity directEntity = source.getDirectEntity();

        // Direct player melee / magic source
        if (attacker != null && ownerId.equals(attacker.getUUID())) {
            return true;
        }

        // Direct entity is the owner
        if (directEntity != null && ownerId.equals(directEntity.getUUID())) {
            return true;
        }

        // Projectile shot by owner
        if (directEntity instanceof Projectile projectile) {
            Entity projectileOwner = projectile.getOwner();

            return projectileOwner != null && ownerId.equals(projectileOwner.getUUID());
        }

        return false;
    }

    private boolean canKomoryBatAttack(Player owner, LivingEntity entity) {
        if (owner == null || entity == null) {
            return false;
        }

        if (!entity.isAlive()) {
            return false;
        }

        // Never attack itself or the owner
        if (entity == this || entity == owner) {
            return false;
        }

        // Never attack players
        if (entity instanceof Player) {
            return false;
        }

        // Never attack Dream Eaters
        if (entity instanceof BaseDreamEaterEntity) {
            return false;
        }

        if (entity instanceof MeowWowEntity) {
            return false;
        }

        if (entity instanceof KomoryBatEntity) {
            return false;
        }

        // Never attack owner's tamed mobs/pets
        if (entity instanceof TamableAnimal tameable) {
            UUID tameOwner = tameable.getOwnerUUID();

            if (tameOwner != null && tameOwner.equals(owner.getUUID())) {
                return false;
            }
        }

        // Respect vanilla ally/team logic
        if (entity.isAlliedTo(owner) || owner.isAlliedTo(entity)) {
            return false;
        }

        // Always defend the owner
        if (owner.getLastHurtByMob() == entity) {
            return true;
        }

        // Help attack what the owner attacked
        if (owner.getLastHurtMob() == entity) {
            return true;
        }

        // If a mob is actively targeting the owner, attack it
        if (entity instanceof Mob mob && mob.getTarget() == owner) {
            return true;
        }

        // Otherwise only attack hostile mobs
        return entity instanceof Enemy;
    }

    public static void removeExistingKomoryBat(ServerLevel level, UUID ownerUUID) {
        MinecraftServer server = level.getServer();

        for (ServerLevel serverLevel : server.getAllLevels()) {
            for (Entity entity : serverLevel.getAllEntities()) {
                if (entity instanceof KomoryBatEntity komoryBat) {
                    if (ownerUUID.equals(komoryBat.getOwnerUUID())) {
                        komoryBat.discard();
                    }
                }
            }
        }
    }

    @Override
    public void die(DamageSource source) {
        UUID ownerId = this.getOwnerUUID();

        if (ownerId != null) {
            Player owner = this.level().getPlayerByUUID(ownerId);

            if (owner != null) {
                GlobalDataRM data = ModDataRM.getGlobal(owner);

                if (data != null) {
                    clearDreamEaterData(owner, data);
                }
            }
        }

        super.die(source);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void updateWalkAnimation(float partialTick) {
        float f = this.getPose() == Pose.STANDING ? Math.min(partialTick * 6.0F, 1.0F) : 0.0F;
        this.walkAnimation.update(f, 0.2F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, VARIANT_NORMAL);
        builder.define(OWNER_UUID, Optional.empty());
        builder.define(ATTACK_ANIM_TICKS, 0);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    public int getAttackAnimTicks() {
        return this.entityData.get(ATTACK_ANIM_TICKS);
    }

    public void setAttackAnimTicks(int ticks) {
        this.entityData.set(ATTACK_ANIM_TICKS, ticks);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        UUID ownerId = this.getOwnerUUID();

        if (ownerId != null) {
            tag.putUUID("DreamEaterOwner", ownerId);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.hasUUID("DreamEaterOwner")) {
            this.setOwnerUUID(tag.getUUID("DreamEaterOwner"));
        }

        this.setNoGravity(true);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main_controller", 1, state -> {
            if (this.getAttackAnimTicks() > 0) {
                return state.setAndContinue(ATTACK_ANIM);
            }

            double dx = this.getX() - this.xOld;
            double dy = this.getY() - this.yOld;
            double dz = this.getZ() - this.zOld;
            double movedSqr = dx * dx + dy * dy + dz * dz;

            if (movedSqr > 0.00001D || this.getDeltaMovement().lengthSqr() > 0.00001D) {
                return state.setAndContinue(WALK_ANIM);
            }

            return state.setAndContinue(IDLE_ANIM);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}