package online.remind.remind.entity.reactioncommand;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.entity.ModEntitiesRM;
import org.joml.Vector3f;

import java.util.List;

public class DualShotEntity extends ThrowableProjectile {
    int maxTicks = 40;
    float dmgMult = 1;

    static int ticks = 0;
    static double a = 3600;
    LivingEntity lockOnEntity;
    public DualShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
    }

    public DualShotEntity(Level world) {
        super(ModEntitiesRM.TYPE_DUAL_SHOT.get(), world);
        this.blocksBuilding = true;
    }

    public DualShotEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockOnEntity) {
        super(ModEntitiesRM.TYPE_DUAL_SHOT.get(), player, world);
        this.dmgMult = dmgMult;
        this.lockOnEntity = lockOnEntity;
    }

    @Override
    protected double getDefaultGravity() {
        return 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        if (this.tickCount > maxTicks) {
            this.remove(RemovalReason.KILLED);
            a = 3600;
        }


        if(tickCount > 0) {

            // Rotate
            ticks++;
            if (a > 0) {


                double r = 0.5D;

                double cx = getX();
                double cy = getY() + 0.5;
                double cz = getZ();

                a -= 9;
                double x = cx + (r * Math.cos(Math.toRadians(a)));
                double y = cy + (r * Math.cos(Math.toRadians(a)));
                double z = cz + (r * Math.sin(Math.toRadians(a)));





                double x2 = cx + (r * Math.cos(Math.toRadians(-a)));
                double y2 = cy + -(r * Math.cos(Math.toRadians(-a)));
                double z2 = cz + (r * Math.sin(Math.toRadians(-a)));

                //Light Particles
                //level().addParticle(new DustParticleOptions(new Vector3f(1F,1F,1F), 1F), getX() + 0.5D, getY(), getZ() + 0.5D, 0, 1,0);
                level().addParticle(new DustParticleOptions(new Vector3f(1F,1F,1F), 1F), x, y, z, 0, 0,0);
                level().addParticle(new DustParticleOptions(new Vector3f(1F,1F,0.8F), 1F), x, y, z, 0, 0,0);

                //Dark Particles
                //level().addParticle(new DustParticleOptions(new Vector3f(0.25F,0F,0.45F), 1F), getX() - 0.5D, getY(), getZ() - 0.5D, 0, 1, 0);
                level().addParticle(new DustParticleOptions(new Vector3f(0.25F,0F,0.45F), 1F), x2, y2, z2, 0, 0, 0);
                level().addParticle(new DustParticleOptions(new Vector3f(0F,0F,0F), 1F), x2, y2, z2, 0, 0, 0);

            }
            super.tick();
        }
    }

    @Override
    protected void onHit (HitResult rtRes) {
        if (!level().isClientSide && getOwner() != null) {
            EntityHitResult ertResult = null;
            BlockHitResult brtResult = null;
            float radius = 4F;
            double X = getX();
            double Y = getY();
            double Z = getZ();

            for (int t = 1; t < 360; t += 20) {
                for (int s = 1; s < 360; s += 20) {
                    double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                    double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                    double y = Y + (radius * Math.cos(Math.toRadians(t)));
                    ((ServerLevel) level()).sendParticles(new DustParticleOptions(new Vector3f(0.5F, 0.5F, 0.5F), 2F), x, y + 1, z, 1, 0, 0, 0, 0);
                    ((ServerLevel) level()).sendParticles(new DustParticleOptions(new Vector3f(1F,1F,0.7F), 3F), x, y + 1, z, 1, 0, -0.25, 0, 0);
                    ((ServerLevel) level()).sendParticles(new DustParticleOptions(new Vector3f(0F,0F,0F), 2F), x, y + 1, z, 1, 0, -0.50,0 , 0);
                    ((ServerLevel) level()).sendParticles(new DustParticleOptions(new Vector3f(1F,1F,0.8F), 3F), x, y + 1, z, 1, 0, -0.75, 0, 0);
                }
            }

            WorldData worldData = WorldData.get(level().getServer());
            if (rtRes instanceof EntityHitResult) {
                ertResult = (EntityHitResult) rtRes;
            }

            if (rtRes instanceof BlockHitResult) {
                brtResult = (BlockHitResult) rtRes;
            }

            if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) ertResult.getEntity();

                if (target != getOwner()) {
                    Party p = null;
                    if (getOwner() != null) {
                        p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
                    }
                    if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
                        float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) / 1.25F : 2;
                        /*
                        System.out.println("Spell Damage (Before Mult): "+ dmg);
                        System.out.println("Spell Damage (After Mult): "+ dmg*dmgMult);
                         */
                        //target.hurt(DarknessDamageSource.getDarknessDamage(this, this.getOwner()), dmg * dmgMult);
                        if (this.getOwner() instanceof Player) {
                            List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty((Player) this.getOwner(), this, radius, radius, radius);
                            for (LivingEntity e : targetList) {
                                e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHT,this, this.getOwner()), ((dmg/2) * (dmgMult/2)));
                                e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS,this, this.getOwner()), ((dmg/2) * (dmgMult/2)));
                            }
                        }
                    }
                }
                remove(RemovalReason.KILLED);
                a = 3600;
            }
            if (brtResult != null) {
                if (this.getOwner() instanceof Player) {
                    float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) / 1.25F : 2;
                        /*
                        System.out.println("Spell Damage - Splash (Before Mult): "+ dmg);
                        System.out.println("Spell Damage - Splash (After Mult): "+ dmg*dmgMult);
                         */
                    List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty((Player) this.getOwner(), this, radius, radius, radius);
                    for (LivingEntity e : targetList) {
                        e.hurt(damageSources().indirectMagic(this, this.getOwner()), dmg * dmgMult);
                    }
                }
                remove(RemovalReason.KILLED);
                a = 3600;
            }
        }

    }

}
