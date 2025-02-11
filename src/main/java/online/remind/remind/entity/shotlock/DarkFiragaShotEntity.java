package online.remind.remind.entity.shotlock;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.shotlock.BaseShotlockShotEntity;
import org.joml.Vector3f;

import java.awt.*;

public class DarkFiragaShotEntity extends BaseShotlockShotEntity {
    public DarkFiragaShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
    }

    public DarkFiragaShotEntity(Level world) {
        super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world);
        this.blocksBuilding = true;
    }

    public DarkFiragaShotEntity(Level world, LivingEntity player, Entity target, double dmg) {
        super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world, player, target, dmg);
    }

    @Override
    public void tick() {
        if (this.tickCount > getMaxTicks()) {
            this.remove(RemovalReason.KILLED);
        }

        if(tickCount > 1) {
            Color color = new Color(getColor());
            level().addParticle(new DustParticleOptions(new Vector3f(0.25f, 1f, 1f), 1F), getX(), getY(), getZ(), 1,1,1);
            level().addParticle(new DustParticleOptions(new Vector3f(0f, 0f, 0f), 1F), getX(), getY(), getZ(), 1,1,1);
            level().addAlwaysVisibleParticle(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY(), getZ(), 0, 0, 0);
            level().addParticle(ParticleTypes.SQUID_INK, getX(), getY(), getZ(), 0, 0, 0);
        }

        if(tickCount % 10 == 0) {
            updateMovement();
        }

        super.tick();
    }

    private void updateMovement() {
        if(getTarget() != null) {
            if(getTarget().isAlive()) {
                this.shoot(getTarget().getX() - this.getX(), (getTarget().getY() + (getTarget().getBbHeight() / 2.0F) - this.getBbHeight()) - getY() + 0.5, getTarget().getZ() - this.getZ(), 1, 0);
            } else {
                if(getOwner() != null)
                    this.shootFromRotation(this, getOwner().getXRot(), getOwner().getYRot(), 0, 1, 0); // Work in progress
            }
        }
    }

    @Override
    protected void onHit(HitResult rtRes) {
        super.onHit(rtRes);
        if (!level().isClientSide) {
            EntityHitResult ertResult = null;
            BlockHitResult brtResult = null;

            if (rtRes instanceof EntityHitResult) {
                ertResult = (EntityHitResult) rtRes;
            }

            if (rtRes instanceof BlockHitResult) {
                brtResult = (BlockHitResult) rtRes;
            }

            if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) ertResult.getEntity();
                if (target != getOwner()) {
                    target.hurt(target.damageSources().thrown(this, this.getOwner()), dmg);
                    target.invulnerableTime = 1;
                    target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 120, 1));
                    target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 120, 1));
                    super.remove(RemovalReason.KILLED);
                }
            }
            remove(RemovalReason.KILLED);
        }
    }
}
