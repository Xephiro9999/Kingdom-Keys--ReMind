package online.remind.remind.entity.attacks;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import online.remind.remind.entity.ModEntitiesRM;

public class quickBlitzCollider extends ThrowableProjectile {

    private Player caster;
    private float damage;
    private int maxTicks = 20;
    private int ticks = 0;

    public quickBlitzCollider(EntityType<? extends ThrowableProjectile> type, Level level){
        super(type, level);
        this.noPhysics = true;
        //this.setInvisible(true);
        this.setBoundingBox(new AABB(-0.5, 0, -0.5, 0.5, 1.5, 0.5));

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    public quickBlitzCollider(Level level, Player caster, float damage){
        this(ModEntitiesRM.TYPE_QUICK_BLITZ.get(),level);
        this.caster = caster;
        this.damage = damage;
        this.setPos(caster.getX(), caster.getY(), caster.getZ());
    }

    public void tick() {

        if (caster == null || !caster.isAlive()) {
            remove(RemovalReason.KILLED);
            return;
        }

        if (this.tickCount > maxTicks) {
            this.remove(RemovalReason.KILLED);
        }

        this.setPos(caster.getX(), caster.getY() + 0.5, caster.getZ());

        // Check Collision
        for (Entity entity : level().getEntities(this, this.getBoundingBox(), e -> e instanceof LivingEntity && e != caster)){
            LivingEntity target = (LivingEntity) entity;
            target.hurt(caster.damageSources().playerAttack((caster)), damage);
            caster.setDeltaMovement(0,0,0);
            caster.swing(InteractionHand.MAIN_HAND);
            this.remove(RemovalReason.KILLED);
            target.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                    SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        super.tick();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}
    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}
}
