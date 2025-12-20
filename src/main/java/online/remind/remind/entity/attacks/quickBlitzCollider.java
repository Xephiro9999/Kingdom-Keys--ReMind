package online.remind.remind.entity.attacks;

import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.ModEntitiesRM;
import org.joml.Vector3f;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.registry.entries.EpicFightParticles;
import yesman.epicfight.registry.entries.EpicFightSounds;

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

        if (tickCount > 2){
            AABB hitBox = this.getBoundingBox().inflate(2.0); // easier to land
        }

        this.setPos(caster.getX(), caster.getY() + 0.5, caster.getZ());

        if (tickCount > 1) {
            if (caster.level() instanceof ServerLevel serverLevel) {

                serverLevel.sendParticles(ParticleTypes.CRIT,
                        caster.getX(),
                        caster.getY()+1,
                        caster.getZ(),
                        1, 0, 0, 0, 0);
            }
        }


        // Check Collision
        this.setOwner(caster);

        for (Entity entity : level().getEntities(this, this.getBoundingBox(), e -> e instanceof LivingEntity && e != caster)) {
            if (entity != getOwner()) {
                Party p = null;
                if (getOwner() != null) {
                    p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
                }
                LivingEntity target = (LivingEntity) entity;
                if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())){
                    target.hurt(caster.damageSources().playerAttack((caster)), damage);
                    caster.setDeltaMovement(0, 0, 0);
                    caster.swing(InteractionHand.MAIN_HAND);
                    target.invulnerableTime = 0;
                    this.remove(RemovalReason.KILLED);
                    target.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                            SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (KingdomKeysReMind.efmLoaded) {
                        EpicFightParticles.HIT_BLADE.get().spawnParticleWithArgument(((ServerLevel) target.level()), HitParticleType.RANDOM_WITHIN_BOUNDING_BOX, HitParticleType.ZERO, target, target);
                        target.level().playSound(null, target.blockPosition(), EpicFightSounds.BLADE_HIT.get(), SoundSource.PLAYERS, 1F, 1F);
                    } else {
                        level().addParticle(ParticleTypes.CRIT,
                                target.getX(), target.getY() + target.getBbHeight(), target.getZ(),
                                0, 0.1, 0);
                    }
                }
            }
        }
        super.tick();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}
    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}
}
