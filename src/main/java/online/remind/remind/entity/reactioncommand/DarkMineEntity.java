package online.remind.remind.entity.reactioncommand;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.entity.ModEntitiesRM;
import org.joml.Vector3f;

import java.util.List;

public class DarkMineEntity extends ThrowableProjectile {
    int maxTicks = 120;
    float dmg;

	LivingEntity closest = null;

    @Override
    protected double getDefaultGravity() {
        return tickCount > 8 && closest == null ? 0.25 : 0;
    }

    public DarkMineEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
    }

    public DarkMineEntity(Level world, LivingEntity player, float damage, double x, double y, double z) {
        this(world, player,  damage);
        this.setPos(x,y,z);
    }

    public DarkMineEntity(Level world) {
        super(ModEntitiesRM.TYPE_DARK_MINE.get(), world);
        this.blocksBuilding = true;
    }

    public DarkMineEntity(Level world, LivingEntity player, float damage) {
        super(ModEntitiesRM.TYPE_DARK_MINE.get(), player, world);
        this.dmg = damage;
    }

    @Override
    public void tick() {
        if (this.tickCount > maxTicks) {
            level().explode(this.getOwner(), this.blockPosition().getX(),this.blockPosition().getY() + (double)(this.getBbHeight() / 16.0F), this.blockPosition().getZ(), 3,false, Level.ExplosionInteraction.NONE);
            this.remove(RemovalReason.KILLED);
        }

        //world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
        if(tickCount > 0) {
            level().addParticle(ParticleTypes.SQUID_INK, getX(), getY() + 1, getZ(), 0, 0, 0);
            level().addAlwaysVisibleParticle(ParticleTypes.SQUID_INK, getX() + level().random.nextDouble() - 0.5D, getY()+ level().random.nextDouble() *2D, getZ() + level().random.nextDouble() - 0.5D, 0, 0, 0);
            level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.5F,0F,0.5F),1F),getX() + level().random.nextDouble() - 0.5D, getY()+ level().random.nextDouble() *2D, getZ() + level().random.nextDouble() - 0.5D, 0, 0, 0);
            level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.5F,0F,1F),1F),getX() + level().random.nextDouble() - 0.5D, getY()+ level().random.nextDouble() *2D, getZ() + level().random.nextDouble() - 0.5D, 0, 0, 0);
            level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.2F,0F,0F),1F),getX() + level().random.nextDouble() - 0.55D, getY()+ level().random.nextDouble() *2D, getZ() + level().random.nextDouble() - 0.55D, 0, 0, 0);
        } 
        if(tickCount > 8 && tickCount < 16) {
			setDeltaMovement(0, 0, 0);
        }
        if(tickCount > 16){
        	
        	WorldData worldData = WorldData.get(level().getServer());
        	if(worldData == null || getOwner() == null)
        		return;
        	
        	int radius = 4;
        	List<Entity> list = level().getEntities(this, getBoundingBox().inflate(radius));
			Party casterParty = worldData.getPartyFromMember(getOwner().getUUID());

			if(casterParty != null && !casterParty.getFriendlyFire()) {
				for(Member m : casterParty.getMembers()) {
					list.remove(level().getPlayerByUUID(m.getUUID()));
				}
			} else {
				list.remove(getOwner());
			}

			double d0 = -1.0D;

			if(closest == null) {
				for (Entity t1 : list) {
					double d1 = t1.distanceToSqr(getX(), getY(), getZ());
					if (d0 == -1.0D || d1 < d0) {
						d0 = d1;
						if (t1 instanceof LivingEntity le)
							closest = le;
					}
	
				}
			}

			if (closest == null) {
				setDeltaMovement(0, 0, 0);
			} else {
				Vec3 vec3d = new Vec3(closest.getX() - this.getX(), closest.getY() - this.getY(), closest.getZ() - this.getZ());
				double d1 = vec3d.lengthSqr();
				double d2 = 1.0D - Math.sqrt(d1) / 6;
				this.setDeltaMovement(this.getDeltaMovement().add(vec3d.normalize().scale(d2*d2*0.1F))); //d2*d2*0.1F
			}
				
			
            //level().playSound(null, this.getX(),this.getY(),this.getZ(), ModSoundsRM.DARK_MINE_ALIVE.get(), SoundSource.NEUTRAL,1F,1F);
        }

        super.tick();
    }

    @Override
    protected void onHit(HitResult rtRes) {
        if (!level().isClientSide && getOwner() != null) {
            EntityHitResult ertResult = null;
            BlockHitResult brtResult = null;

            int radius = 3;

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
                        float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) : 2;
                        Player player = (Player) this.getOwner();
                        //target.hurt(DarknessDamageSource.getDarknessDamage(this, this.getOwner()), dmg * dmgMult);
                        if(this.getOwner() instanceof Player) {
                            List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty((Player) this.getOwner(), this, radius,radius,radius);
                            for(LivingEntity e : targetList) {
                                e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS,this, this.getOwner()), dmg);
                                e.invulnerableTime = 0;
                            }
                        }
                        level().explode(this.getOwner(), this.blockPosition().getX(), this.blockPosition().getY() + (double)(this.getBbHeight() / 16.0F), this.blockPosition().getZ(), radius, false, Level.ExplosionInteraction.NONE);
                        PlayerData playerData = PlayerData.get(player);
                        playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), playerData.getDriveFormExp(playerData.getActiveDriveForm()) + 20);
                        remove(RemovalReason.KILLED);

                    }
                }
            }

            if (brtResult != null) {
                setDeltaMovement(0,0,0);
            }
        }

    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}

