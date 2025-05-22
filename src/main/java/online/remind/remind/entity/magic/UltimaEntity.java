package online.remind.remind.entity.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnegaEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.ModEntitiesRM;

import java.util.List;

public class UltimaEntity extends ThrowableProjectile {
	int maxTicks = 160;
	Player player;
	String caster;
	float dmgMult = 1;
	float radius = 0.5F;

	public UltimaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public UltimaEntity(Level world) {
		super(ModEntitiesRM.TYPE_ULTIMA.get(), world);
		this.blocksBuilding = true;
	}

	public UltimaEntity(Level world, LivingEntity player, float dmgMult) {
		super(ModEntitiesRM.TYPE_ULTIMA.get(), player, world);
		this.dmgMult = dmgMult;
	}

	@Override
	protected double getDefaultGravity() {
		return 0;
	}

	double a = 0;
	public int startingTicks = -1;
	
	@Override
	public void tick() {
		if (!level().isClientSide) {
			if (this.tickCount > maxTicks) {
				this.remove(RemovalReason.KILLED);
			}

			WorldData worldData = WorldData.get(level().getServer());
			if (worldData == null || getOwner() == null) {
				this.remove(RemovalReason.KILLED);
				return;
			}

			this.hurtMarked = false;
			double X = getX();
			double Y = getY();
			double Z = getZ();
			if (tickCount <= 1) {
			} else if (tickCount < 15) { // Ultima being dropped to target
				// Start
			} else if (tickCount == 15) {
				 setPos(new Vec3(getX(), getY(+20), getZ()));
                 this.setDeltaMovement(0,-1,0);
                 this.markHurt();
			} else { // EXPLOOOOOSION!!!!! (And damage)
				if(getStartingTicks() > -1) {// Start
					float radius = (tickCount - getStartingTicks()) * 0.2f;
					//float radius = 2;
					for (int t = 1; t < 360; t += 40) {
						for (int s = 1; s < 360 ; s += 40) {
							double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
							double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
							double y = Y + (radius * Math.cos(Math.toRadians(t)));
							((ServerLevel) level()).sendParticles(ParticleTypes.ENCHANT, x, y+1, z, 1, 0,0,0, 0);
						}

					}
					if (tickCount == 40){
						playSound(ModSoundsRM.ULTIMA_CAST.get(),1F,1F);
					} if (tickCount == 140) {
						playSound(ModSoundsRM.ULTIMA_EXPLOSION.get(),0.75F,1F);
					}
					
	
					List<Entity> list = level().getEntities(getOwner(), getBoundingBox().inflate(radius));
	
					Party casterParty = worldData.getPartyFromMember(getOwner().getUUID());
	
					if (casterParty != null && !casterParty.getFriendlyFire()) {
						for (Member m : casterParty.getMembers()) {
							list.remove(level().getPlayerByUUID(m.getUUID()));
						}
					} else {
						list.remove(getOwner());
					}
	
					if (!list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							Entity e = (Entity) list.get(i);
							if (e instanceof LivingEntity) {
								if (Utils.isHostile(e) || e instanceof Slime) {
									float dmg = this.getOwner() instanceof Player ? (DamageCalculation.getMagicDamage((Player) this.getOwner()) / 4F) : 2;
									dmg = Math.min(dmg, 99);
									e.hurt(e.damageSources().indirectMagic(this, this.getOwner()), dmg * dmgMult);
								}
							}
						}
					}
				}
			}
		}
		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		if (!level().isClientSide) {
			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {
				if (tickCount > 25) {
					LivingEntity target = (LivingEntity) ertResult.getEntity();

					if (target != getOwner()) {
						Party p = null;
						if (getOwner() != null) {
							p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
						}
						if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { // If caster is not in a party || the party doesn't have the target in it || the
																											// party has FF on
							float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) * 0.2F : 2;
							target.invulnerableTime = 1;
							target.hurt(damageSources().indirectMagic(this, this.getOwner()), dmg * dmgMult);
						}
					}
				}
			}
			if (brtResult != null) {
				setDeltaMovement(0, 0, 0);
				if(startingTicks == -1)
					setStartingTicks(tickCount);

			}
		}

	}

	private static final EntityDataAccessor<Integer> STARTING_TICKS = SynchedEntityData.defineId(UltimaEntity.class, EntityDataSerializers.INT);

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.entityData.get(STARTING_TICKS) != null) {
			compound.putInt("startingTicks", this.entityData.get(STARTING_TICKS));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(STARTING_TICKS, compound.getInt("startingTicks"));
	}

	public int getStartingTicks() {
		return this.getEntityData().get(STARTING_TICKS).intValue();
	}

	public void setStartingTicks(int ticks) {
		this.entityData.set(STARTING_TICKS, ticks);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(STARTING_TICKS, -1);
	}
}
