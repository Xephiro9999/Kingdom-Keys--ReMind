package online.remind.remind.entity.attacks;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.entity.ModEntitiesRM;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.registry.entries.EpicFightParticles;
import yesman.epicfight.registry.entries.EpicFightSounds;

public class ElementStrikeCollider extends ThrowableProjectile {

    private LivingEntity caster;
    private float damage;
    private int maxTicks = 10;
    private StrikeElement element = StrikeElement.FIRE;

    public ElementStrikeCollider(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setBoundingBox(new AABB(-0.5D, 0.0D, -0.5D, 0.5D, 1.5D, 0.5D));
    }

    public ElementStrikeCollider(Level level, LivingEntity caster, float damage, StrikeElement element) {
        this(ModEntitiesRM.TYPE_ELEMENT_STRIKE.get(), level);
        this.caster = caster;
        this.damage = damage;
        this.element = element;
        this.setOwner(caster);
        this.setPos(caster.getX(), caster.getY() + 0.5D, caster.getZ());
    }

    private DamageSource getStrikeDamageSource() {
        return switch (element) {
            case FIRE -> KKDamageTypes.getElementalDamage(KKDamageTypes.FIRE, this, this.getOwner());
            case BLIZZARD -> KKDamageTypes.getElementalDamage(KKDamageTypes.ICE, this, this.getOwner());
            case THUNDER -> KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHTNING, this, this.getOwner());
            case WATER -> KKDamageTypes.getElementalDamage(KKDamageTypes.WATER, this, this.getOwner());
            case AERO -> KKDamageTypes.getElementalDamage(KKDamageTypes.AIR, this, this.getOwner());
            case LIGHT -> KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHT, this, this.getOwner());
            case DARK -> KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS, this, this.getOwner());

            case BINDING, CONFUSION -> caster.damageSources().mobAttack(caster);
        };
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void tick() {
        if (caster == null || !caster.isAlive()) {
            remove(RemovalReason.KILLED);
            return;
        }

        if (this.tickCount > maxTicks) {
            remove(RemovalReason.KILLED);
            return;
        }

        this.setOwner(caster);
        this.setPos(caster.getX(), caster.getY() + 0.5D, caster.getZ());

        spawnTrailParticles();

        AABB hitBox = this.getBoundingBox().inflate(2.0D);

        for (Entity entity : level().getEntities(this, hitBox, e -> e instanceof LivingEntity && e != caster)) {
            if (!(entity instanceof LivingEntity target)) {
                continue;
            }

            if (!canHitTarget(target)) {
                continue;
            }

            hitTarget(target);
            return;
        }

        super.tick();
    }

    private boolean canHitTarget(LivingEntity target) {
        Party party = null;

        if (getOwner() != null && getOwner().getServer() != null) {
            party = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
        }

        return party == null || party.getMember(target.getUUID()) == null || party.getFriendlyFire();
    }

    private void hitTarget(LivingEntity target) {
        float finalDamage = damage * element.getDamageMultiplier();

        target.invulnerableTime = 0;
        target.hurt(getStrikeDamageSource(), finalDamage);
        target.invulnerableTime = 0;

        applyElementEffect(target);
        playHitEffects(target);

        caster.setDeltaMovement(0.0D, 0.0D, 0.0D);
        caster.swing(InteractionHand.MAIN_HAND);

        remove(RemovalReason.KILLED);
    }

    private void applyElementEffect(LivingEntity target) {
        switch (element) {
            case FIRE -> {
                target.igniteForSeconds(4.0F);
            }

            case BLIZZARD -> {
                target.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN,
                        80,
                        1
                ));
            }

            case THUNDER -> {
                target.addEffect(new MobEffectInstance(
                        MobEffects.WEAKNESS,
                        80,
                        0
                ));
            }

            case WATER -> {
                target.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN,
                        60,
                        0
                ));
                target.clearFire();
            }

            case AERO -> {
                target.setDeltaMovement(
                        target.getDeltaMovement().x,
                        0.45D,
                        target.getDeltaMovement().z
                );
                target.hurtMarked = true;
            }

            case LIGHT -> {
                target.addEffect(new MobEffectInstance(
                        MobEffects.GLOWING,
                        100,
                        0
                ));
            }

            case DARK -> {
                target.addEffect(new MobEffectInstance(
                        MobEffects.DARKNESS,
                        100,
                        0
                ));
            }

            case BINDING -> {
                target.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN,
                        100,
                        10
                ));

                target.addEffect(new MobEffectInstance(
                        MobEffects.WEAKNESS,
                        100,
                        1
                ));
            }

            case CONFUSION -> {
                target.addEffect(new MobEffectInstance(
                        ModMobEffectsRM.CONFUSE,
                        120,
                        0
                ));
            }
        }
    }

    private void spawnTrailParticles() {
        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        serverLevel.sendParticles(
                element.getParticle(),
                caster.getX(),
                caster.getY() + 1.0D,
                caster.getZ(),
                3,
                0.25D,
                0.25D,
                0.25D,
                0.03D
        );
    }

    private void playHitEffects(LivingEntity target) {
        Level level = target.level();

        level.playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                element.getSound(),
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );

        level.playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.PLAYER_ATTACK_STRONG,
                SoundSource.PLAYERS,
                0.8F,
                1.0F
        );

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    element.getParticle(),
                    target.getX(),
                    target.getY() + target.getBbHeight() * 0.5D,
                    target.getZ(),
                    25,
                    0.45D,
                    0.45D,
                    0.45D,
                    0.08D
            );

            if (element == StrikeElement.LIGHT) {
                serverLevel.sendParticles(
                        ParticleTypes.END_ROD,
                        target.getX(),
                        target.getY() + target.getBbHeight() * 0.5D,
                        target.getZ(),
                        30,
                        0.6D,
                        0.6D,
                        0.6D,
                        0.05D
                );
            }

            if (element == StrikeElement.DARK) {
                serverLevel.sendParticles(
                        ParticleTypes.SOUL,
                        target.getX(),
                        target.getY() + target.getBbHeight() * 0.5D,
                        target.getZ(),
                        35,
                        0.6D,
                        0.6D,
                        0.6D,
                        0.05D
                );
            }

            if (KingdomKeysReMind.efmLoaded) {
                EpicFightParticles.HIT_BLADE.get().spawnParticleWithArgument(
                        serverLevel,
                        HitParticleType.RANDOM_WITHIN_BOUNDING_BOX,
                        HitParticleType.ZERO,
                        target,
                        target
                );

                target.level().playSound(
                        null,
                        target.blockPosition(),
                        EpicFightSounds.BLADE_HIT.get(),
                        SoundSource.PLAYERS,
                        1.0F,
                        1.0F
                );
            }
        } else {
            level.addParticle(
                    ParticleTypes.CRIT,
                    target.getX(),
                    target.getY() + target.getBbHeight(),
                    target.getZ(),
                    0.0D,
                    0.1D,
                    0.0D
            );
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.damage = tag.getFloat("Damage");

        if (tag.contains("Element")) {
            try {
                this.element = StrikeElement.valueOf(tag.getString("Element"));
            } catch (IllegalArgumentException ignored) {
                this.element = StrikeElement.FIRE;
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Damage", this.damage);
        tag.putString("Element", this.element.name());
    }
}