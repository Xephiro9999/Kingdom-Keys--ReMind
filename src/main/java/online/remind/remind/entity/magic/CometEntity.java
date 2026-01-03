package online.remind.remind.entity.magic;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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

public class CometEntity extends ThrowableProjectile {
        int maxTicks = 200, radius = 5;
        float dmg;
        float dmgMult = 1;
        int index = 0;
        boolean meteor;


        public CometEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
                super(type, world);
                this.blocksBuilding = true;
        }

        public CometEntity(Level world) {
                super(ModEntitiesRM.TYPE_COMET.get(), world);
                this.blocksBuilding = true;
        }

        public CometEntity(Level world, LivingEntity player, float dmgMult, int radius, int index, double x, double y, double z, boolean meteor) {
                super(ModEntitiesRM.TYPE_COMET.get(), player, world);
                this.dmgMult = dmgMult;
                this.radius = radius;
                this.index = index;
                this.meteor = meteor;
                this.setPos(x,y,z);
        }

        @Override
        protected double getDefaultGravity() {
                return 0;
        }

        @Override
        public void tick() {
                if (this.tickCount > maxTicks) {
                        this.remove(RemovalReason.KILLED);
                }

                //world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
                if(tickCount > 2)
                        level().addParticle(ParticleTypes.FLAME, getX(), getY(), getZ(), 0, 0, 0);

                if(meteor)
                        this.setDeltaMovement(this.getDeltaMovement().add(0, -0.5, 0));

                super.tick();
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

                                if (target != getOwner()) {
                                        Party p = null;
                                        if (getOwner() != null) {
                                                p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
                                        }
                                        if(p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
                                                float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) / 2F : 2;
                                                //target.hurt(DarknessDamageSource.getDarknessDamage(this, this.getOwner()), dmg * dmgMult);
                                                //System.out.println("Spell Damage (Before Mult): "+ dmg);
                                                //System.out.println("Spell Damage (After Mult): "+ dmg*dmgMult);
                                                if(this.getOwner() instanceof Player) {
                                                        List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty((Player) this.getOwner(), this, radius,radius,radius);
                                                        for(LivingEntity e : targetList) {
                                                                if (Utils.isHostile(e) || e instanceof Slime || e instanceof EnderMan) {
                                                                        e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS, this, this.getOwner()), (dmg * dmgMult)/4);
                                                                        e.invulnerableTime = 0;
                                                                }
                                                        }

                                                }
                                                if (!meteor) {
                                                        level().explode(this, this.blockPosition().getX(), this.blockPosition().getY() + (double) (this.getBbHeight() / 16.0F), this.blockPosition().getZ(), radius, false, Level.ExplosionInteraction.NONE);
                                                } else {
                                                        level().explode(this, this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ(), 0, false, Level.ExplosionInteraction.NONE);
                                                }
                                                double X = getX();
                                                double Y = getY();
                                                double Z = getZ();


                                                for (int t = 1; t < 360; t += 20) {
                                                        for (int s = 1; s < 360 ; s += 20) {
                                                                double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                                                                double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                                                                double y = Y + (radius * Math.cos(Math.toRadians(t)));
                                                                ((ServerLevel) level()).sendParticles(ParticleTypes.FLAME, x, y+1, z, 1, 0,0,0, 0);
                                                                ((ServerLevel) level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()),x,y+1 ,z,1,0,0,0,0);
                                                                ((ServerLevel) level()).sendParticles(new DustParticleOptions(new Vector3f(0.45F,0.45F,0.45f),6F),x,y+1 ,z,1,0,0,0,0);

                                                        }
                                                }
                                                remove(RemovalReason.KILLED);

                                        }
                                }
                        }

                        // Blast Zone, impact from entity hitting
                        if (brtResult != null) {
                                if (this.getOwner() instanceof Player) {
                                        radius = 5;
                                        float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) / 4F : 2;
                                        //System.out.println("Spell Damage - Splash (Before Mult): "+ dmg);
                                        //System.out.println("Spell Damage - Splash (After Mult): "+ dmg*dmgMult);
                                        List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty((Player) this.getOwner(), this, radius, radius, radius);
                                        for (LivingEntity e : targetList) {
                                                e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS,this, this.getOwner()), (dmg * dmgMult)/4);
                                                e.invulnerableTime = 0;
                                        }
                                        double X = getX();
                                        double Y = getY();
                                        double Z = getZ();


                                        for (int t = 1; t < 360; t += 20) {
                                                for (int s = 1; s < 360 ; s += 20) {
                                                        double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                                                        double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                                                        double y = Y + (radius * Math.cos(Math.toRadians(t)));
                                                        ((ServerLevel) level()).sendParticles(ParticleTypes.FLAME, x, y+1, z, 1, 0,0,0, 0);
                                                        ((ServerLevel) level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()),x,y+1 ,z,1,0,0,0,0);
                                                        ((ServerLevel) level()).sendParticles(new DustParticleOptions(new Vector3f(0.45F,0.45F,0.45f),6F),x,y+1 ,z,1,0,0,0,0);

                                                }
                                        }
                                }
                                if (!meteor) {
                                        level().explode(this, this.blockPosition().getX(), this.blockPosition().getY() + (double) (this.getBbHeight() / 16.0F), this.blockPosition().getZ(), radius, false, Level.ExplosionInteraction.NONE);
                                } else {
                                        level().explode(this, this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ(), radius, false, Level.ExplosionInteraction.NONE);
                                }
                                remove(RemovalReason.KILLED);
                        }
                }

        }

        public int getMaxTicks() {
                return maxTicks;
        }

        public void setMaxTicks(int maxTicks) {
                this.maxTicks = maxTicks;
        }



        @Override
        protected void defineSynchedData(SynchedEntityData.Builder builder) {

        }
}

