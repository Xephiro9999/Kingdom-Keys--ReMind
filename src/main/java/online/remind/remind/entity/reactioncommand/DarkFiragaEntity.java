package online.remind.remind.entity.reactioncommand;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.remind.remind.entity.ModEntitiesRM;
import org.joml.Vector3f;

public class DarkFiragaEntity extends ThrowableProjectile {
    int maxTicks = 10;
    float dmg;

    @Override
    protected double getDefaultGravity() {
        return 0;
    }

    public DarkFiragaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
    }

    public DarkFiragaEntity(Level world, LivingEntity player, float damage, double x, double y, double z) {
        this(world, player,  damage);
        this.setPos(x,y,z);
    }

    public DarkFiragaEntity(Level world) {
        super(ModEntitiesRM.TYPE_DARK_FIRAGA.get(), world);
        this.blocksBuilding = true;
    }

    public DarkFiragaEntity(Level world, LivingEntity player, float damage) {
        super(ModEntitiesRM.TYPE_DARK_FIRAGA.get(), player, world);
        this.dmg = damage;
    }

    @Override
    public void tick() {
        if (this.tickCount > maxTicks) {
            this.remove(RemovalReason.KILLED);
        }

        //world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
        if(tickCount > 0) {
            float radius = 0.5F;
            for (int t = 1; t < 360; t += 30) {
                for (int s = 20; s < 360; s += 30) {
                    double x = getX() + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                    double z = getZ() + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                    double y = getY() + (radius * Math.cos(Math.toRadians(t)));
                    level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 0, 0, 0);
                    //level().addAlwaysVisibleParticle(ParticleTypes.SQUID_INK, getX() + level().random.nextDouble() - 0.5D, getY()+ level().random.nextDouble() *2D, getZ() + level().random.nextDouble() - 0.5D, 0, 0, 0);
                    level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F, 1F, 1F), 1F), getX(), getY(), getZ(), 0, 0, 0);
                    level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F, 0F, 0F), 1F), x, y, z, 0, 0, 0);

                }
            }
            super.tick();
        }
    }

    @Override
    protected void onHit(HitResult rtRes) {
        if (!level().isClientSide && getOwner() != null) {
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
                Player player = (Player) this.getOwner();
                if (target != getOwner()) {
                    Party p = null;
                    if (getOwner() != null) {
                        p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
                    }
                    if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
                        float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) : 2;

                        target.hurt(damageSources().indirectMagic(this, this.getOwner()), dmg);
                        target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 160,3));
                        target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 160,3));
                        remove(RemovalReason.KILLED);
                    }
                }
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
