package online.remind.remind.entity.spirits;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.dreameater.DreamEaterExpHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TonberrySpiritEntity extends PathfinderMob implements GeoEntity {

    private static final int ACTION_NONE = 0;
    private static final int ACTION_STAB = 1;
    private static final int ACTION_GRUDGE = 2;
    private static final int ACTION_DEATH = 3;

    private static final int STAB_ACTION_TICKS = 26;
    private static final int STAB_DAMAGE_DELAY_TICKS = 13;

    private static final int GRUDGE_ACTION_TICKS = 46;
    private static final int GRUDGE_DAMAGE_DELAY_TICKS = 24;
    private static final int GRUDGE_COOLDOWN_TICKS = 120;

    private static final int DEATH_ACTION_TICKS = 70;

    private static final int GOLD_FEED_EXP = 15;
    private static final int EMERALD_FEED_EXP = 30;
    private static final int FEED_COOLDOWN_TICKS = 10;

    private static final double FOLLOW_START_DISTANCE = 8.0D;
    private static final double FOLLOW_STOP_DISTANCE = 4.0D;
    private static final double TELEPORT_DISTANCE = 20.0D;

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID =
            SynchedEntityData.defineId(TonberrySpiritEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> ACTION =
            SynchedEntityData.defineId(TonberrySpiritEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> ACTION_TICKS =
            SynchedEntityData.defineId(TonberrySpiritEntity.class, EntityDataSerializers.INT);

    private static final RawAnimation IDLE =
            RawAnimation.begin().thenLoop("idle");

    private static final RawAnimation WALK =
            RawAnimation.begin().thenLoop("walk");

    private static final RawAnimation STAB =
            RawAnimation.begin().thenPlay("stab");

    private static final RawAnimation GRUDGE =
            RawAnimation.begin().thenPlay("grudge");

    private static final RawAnimation DEATH =
            RawAnimation.begin().thenPlayAndHold("death");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private int statUpdateTicks = 0;
    private int feedCooldownTicks = 0;

    private int pendingStabTargetId = -1;
    private int pendingStabDamageDelay = 0;

    private int grudgeCooldownTicks = 0;
    private int pendingGrudgeTargetId = -1;
    private int pendingGrudgeDamageDelay = 0;
    private float pendingGrudgeDamage = 0.0F;

    public TonberrySpiritEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.xpReward = 0;
    }

    public TonberrySpiritEntity(Level level, Player owner) {
        this(online.remind.remind.entity.ModEntitiesRM.TYPE_TONBERRY_SPIRIT.get(), level);
        this.setOwnerUUID(owner.getUUID());
        this.moveTo(owner.getX(), owner.getY() + 0.1D, owner.getZ(), owner.getYRot(), owner.getXRot());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 44.0D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.16D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.45D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        builder.define(OWNER_UUID, Optional.empty());
        builder.define(ACTION, ACTION_NONE);
        builder.define(ACTION_TICKS, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TonberrySpiritMeleeGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.05D));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.45D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(
                this,
                Monster.class,
                10,
                true,
                false,
                this::isValidSpiritTarget
        ));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isPlayingDeathAnimation()
                || this.isPlayingStabAnimation()
                || this.isPlayingGrudgeAnimation()) {
            lockMovement();
        }

        if (!this.level().isClientSide) {
            tickAction();
            tickDelayedStab();
            tickGrudge();
            tickFeedingCooldown();
            tickStats();
            discardIfOwnerMissing();
        }
    }

    private void tickFeedingCooldown() {
        if (this.feedCooldownTicks > 0) {
            this.feedCooldownTicks--;
        }
    }

    private void tickStats() {
        if (this.statUpdateTicks > 0) {
            this.statUpdateTicks--;
            return;
        }

        this.statUpdateTicks = 40;
        applyDreamEaterStats();
    }

    private void applyDreamEaterStats() {
        int level = getDreamEaterLevel();

        double maxHP = 42.0D + level * 1.35D;
        double attack = 6.0D + level * 0.24D;
        double armor = 7.0D + level * 0.13D;
        double speed = 0.155D + Math.min(0.055D, level * 0.0006D);

        setAttributeBaseValue(Attributes.MAX_HEALTH, maxHP);
        setAttributeBaseValue(Attributes.ATTACK_DAMAGE, attack);
        setAttributeBaseValue(Attributes.ARMOR, armor);
        setAttributeBaseValue(Attributes.MOVEMENT_SPEED, speed);

        if (this.getHealth() > this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
    }

    private void setAttributeBaseValue(net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute, double value) {
        AttributeInstance instance = this.getAttribute(attribute);

        if (instance != null) {
            instance.setBaseValue(value);
        }
    }

    private int getDreamEaterLevel() {
        Player owner = getOwnerPlayer();

        if (owner == null) {
            return 1;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(owner);

        if (globalData == null) {
            return 1;
        }

        return globalData.getDreamEaterLevel(GlobalDataRM.DREAM_EATER_TONBERRY);
    }

    private void discardIfOwnerMissing() {
        if (this.tickCount < 40) {
            return;
        }

        if (getOwnerUUID() == null) {
            this.discard();
            return;
        }

        if (getOwnerPlayer() == null) {
            this.discard();
        }
    }

    private void tickAction() {
        int ticks = this.getTonberryActionTicks();

        if (ticks <= 0) {
            if (this.getTonberryAction() != ACTION_NONE && !this.isDeadOrDying()) {
                setTonberryAction(ACTION_NONE, 0);
            }

            return;
        }

        this.entityData.set(ACTION_TICKS, ticks - 1);

        if (ticks - 1 <= 0 && !this.isDeadOrDying()) {
            setTonberryAction(ACTION_NONE, 0);
        }
    }

    private void tickDelayedStab() {
        if (this.pendingStabDamageDelay <= 0) {
            return;
        }

        this.pendingStabDamageDelay--;

        if (this.pendingStabDamageDelay <= 0) {
            applyPendingStabDamage();
        }
    }

    private void tickGrudge() {
        if (this.grudgeCooldownTicks > 0) {
            this.grudgeCooldownTicks--;
        }

        if (this.pendingGrudgeDamageDelay <= 0) {
            return;
        }

        this.pendingGrudgeDamageDelay--;

        if (this.pendingGrudgeDamageDelay <= 0) {
            applyPendingGrudgeDamage();
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (this.level().isClientSide) {
            return false;
        }

        if (!(target instanceof LivingEntity livingTarget)) {
            return false;
        }

        if (!isValidSpiritTarget(livingTarget)) {
            return false;
        }

        if (this.isDeadOrDying()
                || this.isPlayingStabAnimation()
                || this.isPlayingGrudgeAnimation()
                || this.pendingStabTargetId != -1) {
            return false;
        }

        startDelayedStab(livingTarget);
        return true;
    }

    private void startDelayedStab(LivingEntity target) {
        this.pendingStabTargetId = target.getId();
        this.pendingStabDamageDelay = STAB_DAMAGE_DELAY_TICKS;

        triggerStabAnimation();

        this.level().playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP,
                this.getSoundSource(),
                0.35F,
                0.65F
        );
    }

    private void applyPendingStabDamage() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            clearPendingStab();
            return;
        }

        Entity entity = serverLevel.getEntity(this.pendingStabTargetId);

        if (!(entity instanceof LivingEntity target) || !isValidSpiritTarget(target)) {
            clearPendingStab();
            return;
        }

        if (!isTargetInStabRange(target)) {
            clearPendingStab();
            return;
        }

        target.invulnerableTime = 0;
        boolean hit = super.doHurtTarget(target);

        if (hit && this.random.nextFloat() < getGrudgeChance()) {
            tryStartSpiritGrudge(target);
        }

        serverLevel.playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.PLAYER_ATTACK_CRIT,
                this.getSoundSource(),
                0.45F,
                0.75F
        );

        clearPendingStab();
    }

    private boolean isTargetInStabRange(LivingEntity target) {
        double reach = this.getBbWidth() + target.getBbWidth() + 1.05D;
        return this.distanceToSqr(target) <= reach * reach;
    }

    private void clearPendingStab() {
        this.pendingStabTargetId = -1;
        this.pendingStabDamageDelay = 0;
    }

    private float getGrudgeChance() {
        int level = getDreamEaterLevel();
        return Math.min(0.32F, 0.12F + level * 0.002F);
    }

    private void tryStartSpiritGrudge(LivingEntity target) {
        if (this.grudgeCooldownTicks > 0) {
            return;
        }

        if (this.pendingGrudgeTargetId != -1) {
            return;
        }

        if (!isValidSpiritTarget(target)) {
            return;
        }

        this.pendingGrudgeTargetId = target.getId();
        this.pendingGrudgeDamage = calculateSpiritGrudgeDamage();
        this.pendingGrudgeDamageDelay = GRUDGE_DAMAGE_DELAY_TICKS;
        this.grudgeCooldownTicks = GRUDGE_COOLDOWN_TICKS;

        triggerGrudgeAnimation();

        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.playSound(
                    null,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    SoundEvents.WITHER_AMBIENT,
                    SoundSource.NEUTRAL,
                    0.45F,
                    0.8F
            );
        }
    }

    private float calculateSpiritGrudgeDamage() {
        int level = getDreamEaterLevel();

        /*
         * Dream Eater version does NOT use player lifetime kills.
         * Keeps it strong, but not server-breaking.
         */
        return 7.0F + level * 0.55F;
    }

    private void applyPendingGrudgeDamage() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            clearPendingGrudge();
            return;
        }

        Entity entity = serverLevel.getEntity(this.pendingGrudgeTargetId);

        if (!(entity instanceof LivingEntity target) || !isValidSpiritTarget(target)) {
            clearPendingGrudge();
            return;
        }

        target.invulnerableTime = 0;
        target.hurt(this.damageSources().mobAttack(this), this.pendingGrudgeDamage);

        serverLevel.playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.WITHER_SHOOT,
                SoundSource.NEUTRAL,
                0.45F,
                0.9F
        );

        clearPendingGrudge();
    }

    private void clearPendingGrudge() {
        this.pendingGrudgeTargetId = -1;
        this.pendingGrudgeDamage = 0.0F;
        this.pendingGrudgeDamageDelay = 0;
    }

    private boolean isValidSpiritTarget(@Nullable LivingEntity target) {
        if (target == null || !target.isAlive()) {
            return false;
        }

        if (target == this) {
            return false;
        }

        UUID ownerUUID = getOwnerUUID();

        if (ownerUUID != null && target.getUUID().equals(ownerUUID)) {
            return false;
        }

        if (target instanceof Player) {
            return false;
        }

        if (target instanceof TonberrySpiritEntity) {
            return false;
        }

        if (target instanceof ChirithyEntity) {
            return false;
        }

        if (target instanceof MeowWowEntity) {
            return false;
        }

        if (target instanceof KomoryBatEntity) {
            return false;
        }

        if (target instanceof CactuarSpiritEntity) {
            return false;
        }

        return target instanceof Monster;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        int feedExp = getFeedExp(heldStack);

        if (feedExp > 0) {
            return feedTonberry(player, heldStack, feedExp);
        }

        return super.mobInteract(player, hand);
    }

    private InteractionResult feedTonberry(Player player, ItemStack heldStack, int feedExp) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.SUCCESS;
        }

        if (!isOwner(serverPlayer)) {
            serverPlayer.displayClientMessage(
                    Component.literal("This Tonberry Spirit does not belong to you.")
                            .withStyle(ChatFormatting.RED),
                    true
            );

            return InteractionResult.CONSUME;
        }

        if (this.feedCooldownTicks > 0) {
            return InteractionResult.CONSUME;
        }

        if (!serverPlayer.getAbilities().instabuild) {
            heldStack.shrink(1);
        }

        this.feedCooldownTicks = FEED_COOLDOWN_TICKS;

        DreamEaterExpHandler.giveDreamEaterExp(
                serverPlayer,
                GlobalDataRM.DREAM_EATER_TONBERRY,
                feedExp,
                this
        );

        return InteractionResult.CONSUME;
    }

    private int getFeedExp(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }

        if (stack.is(Items.GOLD_INGOT)) {
            return GOLD_FEED_EXP;
        }

        if (stack.is(Items.EMERALD)) {
            return EMERALD_FEED_EXP;
        }

        return 0;
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    @Nullable
    public Player getOwnerPlayer() {
        UUID ownerUUID = getOwnerUUID();

        if (ownerUUID == null) {
            return null;
        }

        if (this.level() instanceof ServerLevel serverLevel) {
            return serverLevel.getPlayerByUUID(ownerUUID);
        }

        return this.level().getPlayerByUUID(ownerUUID);
    }

    private boolean isOwner(Player player) {
        UUID ownerUUID = getOwnerUUID();
        return ownerUUID != null && ownerUUID.equals(player.getUUID());
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return isValidSpiritTarget(target) && super.canAttack(target);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity attacker = source.getEntity();

        if (attacker instanceof Player player && isOwner(player)) {
            return false;
        }

        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource damageSource) {
        if (!this.level().isClientSide) {
            setTonberryAction(ACTION_DEATH, DEATH_ACTION_TICKS);
        }

        lockMovement();
        super.die(damageSource);
    }

    @Override
    protected void tickDeath() {
        this.deathTime++;

        lockMovement();

        if (!this.level().isClientSide && this.deathTime >= DEATH_ACTION_TICKS) {
            this.level().broadcastEntityEvent(this, (byte) 60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    private void lockMovement() {
        this.getNavigation().stop();
        this.setDeltaMovement(Vec3.ZERO);
        this.hasImpulse = true;
        this.hurtMarked = true;
    }

    public void triggerStabAnimation() {
        if (this.isDeadOrDying()) {
            return;
        }

        setTonberryAction(ACTION_STAB, STAB_ACTION_TICKS);
    }

    public void triggerGrudgeAnimation() {
        if (this.isDeadOrDying()) {
            return;
        }

        setTonberryAction(ACTION_GRUDGE, GRUDGE_ACTION_TICKS);
    }

    protected void setTonberryAction(int action, int ticks) {
        this.entityData.set(ACTION, action);
        this.entityData.set(ACTION_TICKS, Math.max(0, ticks));
    }

    public int getTonberryAction() {
        return this.entityData.get(ACTION);
    }

    public int getTonberryActionTicks() {
        return this.entityData.get(ACTION_TICKS);
    }

    public boolean isPlayingStabAnimation() {
        return getTonberryAction() == ACTION_STAB && getTonberryActionTicks() > 0;
    }

    public boolean isPlayingGrudgeAnimation() {
        return getTonberryAction() == ACTION_GRUDGE && getTonberryActionTicks() > 0;
    }

    public boolean isPlayingDeathAnimation() {
        return getTonberryAction() == ACTION_DEATH || this.isDeadOrDying();
    }

    private boolean shouldPlayWalkAnimation() {
        if (this.isPlayingStabAnimation()
                || this.isPlayingGrudgeAnimation()
                || this.isPlayingDeathAnimation()) {
            return false;
        }

        if (this.getNavigation() != null && !this.getNavigation().isDone()) {
            return true;
        }

        return this.getDeltaMovement().horizontalDistanceSqr() > 0.00001D;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.WITHER_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_SKELETON_DEATH;
    }

    public static void removeExistingTonberrySpirit(ServerLevel level, UUID ownerUUID) {
        if (level == null || ownerUUID == null) {
            return;
        }

        List<TonberrySpiritEntity> spirits = level.getEntitiesOfClass(
                TonberrySpiritEntity.class,
                level.getWorldBorder().getCollisionShape().bounds(),
                spirit -> ownerUUID.equals(spirit.getOwnerUUID())
        );

        for (TonberrySpiritEntity spirit : spirits) {
            spirit.discard();
        }

        for (ServerLevel serverLevel : level.getServer().getAllLevels()) {
            if (serverLevel == level) {
                continue;
            }

            List<TonberrySpiritEntity> otherSpirits = serverLevel.getEntitiesOfClass(
                    TonberrySpiritEntity.class,
                    serverLevel.getWorldBorder().getCollisionShape().bounds(),
                    spirit -> ownerUUID.equals(spirit.getOwnerUUID())
            );

            for (TonberrySpiritEntity spirit : otherSpirits) {
                spirit.discard();
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 2, state -> {
            if (this.isPlayingDeathAnimation()) {
                state.setAnimation(DEATH);
                return PlayState.CONTINUE;
            }

            if (this.isPlayingGrudgeAnimation()) {
                state.setAnimation(GRUDGE);
                return PlayState.CONTINUE;
            }

            if (this.isPlayingStabAnimation()) {
                state.setAnimation(STAB);
                return PlayState.CONTINUE;
            }

            if (this.shouldPlayWalkAnimation()) {
                state.setAnimation(WALK);
                return PlayState.CONTINUE;
            }

            state.setAnimation(IDLE);
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private static class TonberrySpiritMeleeGoal extends MeleeAttackGoal {
        private final TonberrySpiritEntity tonberry;

        public TonberrySpiritMeleeGoal(TonberrySpiritEntity tonberry, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(tonberry, speedModifier, followingTargetEvenIfNotSeen);
            this.tonberry = tonberry;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (this.canPerformAttack(target)) {
                this.resetAttackCooldown();
                this.mob.swing(this.mob.getUsedItemHand());
                this.tonberry.doHurtTarget(target);
            }
        }
    }

    private static class FollowOwnerGoal extends Goal {
        private final TonberrySpiritEntity spirit;
        private final double speedModifier;
        private Player owner;
        private int repathTicks;

        public FollowOwnerGoal(TonberrySpiritEntity spirit, double speedModifier) {
            this.spirit = spirit;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            Player ownerPlayer = this.spirit.getOwnerPlayer();

            if (ownerPlayer == null || !ownerPlayer.isAlive()) {
                return false;
            }

            if (this.spirit.distanceToSqr(ownerPlayer) < FOLLOW_START_DISTANCE * FOLLOW_START_DISTANCE) {
                return false;
            }

            this.owner = ownerPlayer;
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            if (this.owner == null || !this.owner.isAlive()) {
                return false;
            }

            if (this.spirit.getNavigation().isDone()) {
                return false;
            }

            return this.spirit.distanceToSqr(this.owner) > FOLLOW_STOP_DISTANCE * FOLLOW_STOP_DISTANCE;
        }

        @Override
        public void start() {
            this.repathTicks = 0;
        }

        @Override
        public void stop() {
            this.owner = null;
            this.spirit.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (this.owner == null) {
                return;
            }

            this.spirit.getLookControl().setLookAt(this.owner, 10.0F, this.spirit.getMaxHeadXRot());

            if (this.spirit.distanceToSqr(this.owner) > TELEPORT_DISTANCE * TELEPORT_DISTANCE) {
                teleportNearOwner();
                return;
            }

            if (--this.repathTicks <= 0) {
                this.repathTicks = 10;
                this.spirit.getNavigation().moveTo(this.owner, this.speedModifier);
            }
        }

        private void teleportNearOwner() {
            BlockPos ownerPos = this.owner.blockPosition();

            for (int i = 0; i < 16; i++) {
                int x = ownerPos.getX() + this.spirit.getRandom().nextInt(7) - 3;
                int y = ownerPos.getY() + this.spirit.getRandom().nextInt(3) - 1;
                int z = ownerPos.getZ() + this.spirit.getRandom().nextInt(7) - 3;

                BlockPos pos = new BlockPos(x, y, z);

                if (!this.spirit.level().isEmptyBlock(pos) || !this.spirit.level().isEmptyBlock(pos.above())) {
                    continue;
                }

                if (!this.spirit.level().getBlockState(pos.below()).isSolidRender(this.spirit.level(), pos.below())) {
                    continue;
                }

                this.spirit.moveTo(
                        pos.getX() + 0.5D,
                        pos.getY(),
                        pos.getZ() + 0.5D,
                        this.spirit.getYRot(),
                        this.spirit.getXRot()
                );

                this.spirit.getNavigation().stop();
                return;
            }

            this.spirit.moveTo(this.owner.getX(), this.owner.getY(), this.owner.getZ(), this.owner.getYRot(), 0.0F);
        }
    }
}