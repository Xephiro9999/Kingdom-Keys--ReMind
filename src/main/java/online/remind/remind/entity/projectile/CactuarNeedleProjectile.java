package online.remind.remind.entity.projectile;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import online.remind.remind.entity.enemies.CactuarEntity;
import online.remind.remind.event.CactuarNeedleKnockbackHandler;
import online.remind.remind.init.ModDamageTypes;

public class CactuarNeedleProjectile extends Arrow {

    private static final int MAX_LIFE = 12;

    public CactuarNeedleProjectile(EntityType<? extends CactuarNeedleProjectile> type, Level level) {
        super(type, level);
        this.pickup = AbstractArrow.Pickup.DISALLOWED;
        this.setNoGravity(true);
        this.setBaseDamage(0.0D);

    }

    public CactuarNeedleProjectile(EntityType<? extends CactuarNeedleProjectile> type, Level level, LivingEntity owner) {
        this(type, level);
        this.setOwner(owner);
    }

    @Override
    public void tick() {
        super.tick();

        this.pickup = AbstractArrow.Pickup.DISALLOWED;
        this.setNoGravity(true);
        this.setBaseDamage(0.0D);

        if (!this.level().isClientSide && this.tickCount > MAX_LIFE) {
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.level().isClientSide) {
            return;
        }

        Entity entity = result.getEntity();

        if (!(entity instanceof LivingEntity target)) {
            this.discard();
            return;
        }

        DamageSource source = ModDamageTypes.cactuarNeedle(
                this.level(),
                this,
                this.getOwner()
        );

        boolean noKnockbackTag =
                source.is(net.minecraft.tags.DamageTypeTags.NO_KNOCKBACK);

        Vec3 velocityBefore = target.getDeltaMovement();

        boolean damaged = target.hurt(source, 1.0F);

        Vec3 velocityAfter = target.getDeltaMovement();

        this.discard();
    }

    @Override
    protected void doKnockback(
            LivingEntity target,
            DamageSource damageSource
    ) {
        // Prevent AbstractArrow's separate knockback.
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        if (target == this.getOwner()) {
            return false;
        }

        if (target instanceof CactuarEntity) {
            return false;
        }

        return super.canHitEntity(target);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void playerTouch(Player player) {
        // No pickup.
    }
}