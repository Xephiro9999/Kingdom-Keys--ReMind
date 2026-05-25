package online.remind.remind.entity.attacks;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.remind.remind.entity.ModEntitiesRM;

import java.util.List;

public class thunderSurgeCollider extends ThrowableProjectile {

    private Player caster;
    private float damage;
    private int maxTicks = 10;
    private double radius = 1.5;
    private int hits = 0;
    private int maxHits = 3;

    public thunderSurgeCollider(EntityType<? extends ThrowableProjectile> type, Level level){
        super(type, level);
        this.noPhysics = true;
        //this.setInvisible(true);
        this.setBoundingBox(new AABB(-0.5, 0, -0.5, 0.5, 1.5, 0.5));

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    public thunderSurgeCollider(Level level, Player caster, float damage){
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

        // Ring of thunder particles
        if (tickCount > 1) {
            if (caster.level() instanceof ServerLevel serverLevel) {

                for (int i = 0; i < 8; i++) {
                    double angle = caster.tickCount * 0.3 + i * (Math.PI / 4);
                    double xOffset = Math.cos(angle) * radius;
                    double zOffset = Math.sin(angle) * radius;
                    serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                            caster.getX() + xOffset,
                            caster.getY() + 1.0,
                            caster.getZ() + zOffset,
                            1, 0, 0, 0, 0);
                }
            }
        }

        List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class,
                new AABB(getX() - radius, getY() - 1, getZ() - radius,
                        getX() + radius, getY() + 1, getZ() + radius),
                e -> e != caster && e.isAlive());

        PlayerData playerData = PlayerData.get(caster);
        if (playerData != null) {
            damage = playerData.getStrength(true) * 0.2f;
            double dmgMult = (playerData.getNumberOfAbilitiesEquipped(Strings.thunderBoost)) * 0.5f;
            damage += (damage * dmgMult);
        }

        this.setOwner(caster);
        for (LivingEntity target : entities) {
            if (target != getOwner()) {
                Party p = null;
                if (getOwner() != null) {
                    p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
                }

                if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) {
                    //getOwner().sendSystemMessage(Component.literal("Entity: " + target));
                    target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHTNING, this, this.getOwner()), damage);
                    target.invulnerableTime = 0; // allow multiple hits per tick
                }
            }

            level().addParticle(ParticleTypes.ELECTRIC_SPARK,
                    target.getX(), target.getY() + target.getBbHeight(), target.getZ(),
                    0, 0.1, 0);
        }
        super.tick();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}
    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}
}
