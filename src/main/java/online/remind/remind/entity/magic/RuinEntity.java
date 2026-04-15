package online.remind.remind.entity.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.entity.ModEntitiesRM;

import java.util.List;

public class RuinEntity extends ThrowableProjectile {

    // Start
    int maxTicks = 100, radius = 2;
    float dmgMult = 1;

    public RuinEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
    }

    public RuinEntity(Level world) {
        super(ModEntitiesRM.TYPE_RUIN.get(), world);
        this.blocksBuilding = true;
    }

    public RuinEntity(Level world, LivingEntity player, float dmgMult, int radius) {
        super(ModEntitiesRM.TYPE_RUIN.get(), player, world);
        this.dmgMult = dmgMult;
        this.radius = radius;
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
        if(tickCount > 0)
            level().addParticle(ParticleTypes.SQUID_INK, getX(), getY(), getZ(), 0, 0, 0);

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
                        float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) / 4F : 2;
                        /*
                        System.out.println("Spell Damage (Before Mult): "+ dmg);
                        System.out.println("Spell Damage (After Mult): "+ dmg*dmgMult);
                         */
                        //target.hurt(DarknessDamageSource.getDarknessDamage(this, this.getOwner()), dmg * dmgMult);
                        if(this.getOwner() instanceof Player) {
	                        List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty((Player) this.getOwner(), this, radius,radius,radius);
	                        for(LivingEntity e : targetList) {
                                e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS,this, this.getOwner()), dmg);
	                            e.invulnerableTime = 0;
	                        }
                        }
                        this.level().explode(this.getOwner(), this.blockPosition().getX(), this.blockPosition().getY() + (double)(this.getBbHeight() / 16.0F), this.blockPosition().getZ(), radius, false, ExplosionInteraction.NONE);

                        remove(RemovalReason.KILLED);

                    }
                }
			}

			if (brtResult != null) {
				if (this.getOwner() instanceof Player) {
					float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) / 6F : 2;
                        /*
                        System.out.println("Spell Damage - Splash (Before Mult): "+ dmg);
                        System.out.println("Spell Damage - Splash (After Mult): "+ dmg*dmgMult);
                         */
					List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty((Player) this.getOwner(), this, radius, radius, radius);
					for (LivingEntity e : targetList) {
                        e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS,this, this.getOwner()), dmg);
						e.invulnerableTime = 0;
					}
				}
                this.level().explode(this.getOwner(), this.blockPosition().getX(), this.blockPosition().getY() + (double)(this.getBbHeight() / 16.0F), this.blockPosition().getZ(), radius, false, ExplosionInteraction.NONE);

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
