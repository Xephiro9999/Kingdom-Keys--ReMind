package online.remind.remind.entity.magic;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
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
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.ModEntitiesRM;

import java.util.List;

public class BalloonEntity extends ThrowableProjectile {
    // Start
    int maxTicks = 100;
    float dmgMult = 1;

    public BalloonEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
    }

    public BalloonEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        super(ModEntitiesRM.TYPE_BALLOON.get(), world);
    }

    public BalloonEntity(Level world) {
        super(ModEntitiesRM.TYPE_BALLOON.get(), world);
        this.blocksBuilding = true;
    }

    public BalloonEntity(Level world, LivingEntity player, float dmgMult) {
        super(ModEntitiesRM.TYPE_BALLOON.get(), player, world);
        this.dmgMult = dmgMult;
    }


    private void balloonBurst(){
        float explosionSize = 2.0F;
        this.level().explode(this, this.blockPosition().getX(), this.blockPosition().getY() + (double)(this.getBbHeight() / 1.0F), this.blockPosition().getZ(), explosionSize, false, Level.ExplosionInteraction.NONE);
    }


    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return (Packet<ClientGamePacketListener>) NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected float getGravity() {
        return 0.125F;
    }

    @Override
    public void tick() {
        if (this.tickCount > maxTicks) {
            this.remove(RemovalReason.KILLED);
        }

        //world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
        if(tickCount > 2)
            level().addParticle(ParticleTypes.GLOW_SQUID_INK, getX(), getY(), getZ(), 0, 0, 0);

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
                        p = ModCapabilities.getWorld(getOwner().level()).getPartyFromMember(getOwner().getUUID());
                    }
                    if(p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
                        float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) / 2F : 2;
                        /*
                        System.out.println("Spell Damage (Before Mult): "+ dmg);
                        System.out.println("Spell Damage (After Mult): "+ dmg*dmgMult);
                         */
                        target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.WATER,this, this.getOwner()), dmg * dmgMult);
                        target.invulnerableTime = 0;
                        playSound(ModSoundsRM.BALLOON_BOUNCE.get(),1F,1F);
                        this.remove(RemovalReason.KILLED);
                    }
                }
            }

            if (brtResult != null && rtRes.getType() == HitResult.Type.BLOCK) {

                // Bounce

                Vec3 mot = getDeltaMovement();

                double x = mot.x();
                double y = mot.y();
                double z = mot.z();

                LivingEntity target = this.tickCount > 30 ? getNearbyEntity(ModCapabilities.getWorld(level())) : null;
                if(brtResult.getDirection() == Direction.UP || brtResult.getDirection() == Direction.DOWN){
                	if(target != null) {
                		this.shoot(target.getX() - this.getX(), -y, target.getZ() - this.getZ(), 0.5f, 0);
                	} else {
                		this.setDeltaMovement(x,-y,z);
                	}
                    this.markHurt();
                } else if (brtResult.getDirection() == Direction.EAST || brtResult.getDirection() == Direction.WEST){
                    this.setDeltaMovement(-x,y,z);
                    this.markHurt();
                }else if (brtResult.getDirection() == Direction.NORTH || brtResult.getDirection() == Direction.SOUTH){
                	this.setDeltaMovement(x,y,-z);
                    this.markHurt();
                }
                playSound(ModSoundsRM.BALLOON_BOUNCE.get(),1F,1F);

            }
        }

    }

    private LivingEntity getNearbyEntity(IWorldCapabilities worldData) {
    	List<Entity> list = level().getEntities(getOwner(), getBoundingBox().inflate(3));
    	if(worldData == null)
    		return null;
		Party casterParty = worldData.getPartyFromMember(getOwner().getUUID());

		if(casterParty != null && !casterParty.getFriendlyFire()) {
			for(Member m : casterParty.getMembers()) {
				list.remove(level().getPlayerByUUID(m.getUUID()));
			}
		} else {
			list.remove(getOwner());
		}
		
		if (!list.isEmpty()) {
			for (Entity entity : list) {
				if(entity instanceof LivingEntity le) {
					return le;
				}
			}
		}
		return null;
	}

	public int getMaxTicks() {
        return maxTicks;
    }

    public void setMaxTicks(int maxTicks) {
        this.maxTicks = maxTicks;
    }



    @Override
    protected void defineSynchedData() {
        // TODO Auto-generated method stub

    }
}
