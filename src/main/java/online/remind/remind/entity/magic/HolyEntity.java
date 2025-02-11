package online.remind.remind.entity.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.Tags;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnetEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.IKHMob;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.remind.remind.entity.ModEntitiesRM;

public class HolyEntity extends ThrowableProjectile {

	int maxTicks = 100;
	Player player;
	String caster;
	float dmgMult = 1;
	int index = 0;

	public HolyEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public HolyEntity(Level world) {
		super(ModEntitiesRM.TYPE_HOLY.get(), world);
		this.blocksBuilding = true;
	}

	public HolyEntity(Level world, Player player, int index, float dmgMult) {
		super(ModEntitiesRM.TYPE_HOLY.get(), player, world);
		this.player = player;
		this.dmgMult = dmgMult;
		this.index = index;
	}

	@Override
	protected double getDefaultGravity() {
		return 0;
	}

	double a = 0;

	@Override
	public void tick() {
		for (Player playerFromList : level().players()) {
			if (playerFromList.getDisplayName().getString().equals(getCaster())) {
				player = playerFromList;
				break;
			}
		}

		if (player == null)
			return;

		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		} else if (tickCount > 2) {
			level().addParticle(ParticleTypes.END_ROD, getX(), getY(), getZ(), 0, 0, 0);
		}
		if(!level().isClientSide) {
			shootFromRotation(player, player.getXRot(), player.getYRot() + (index*5), 0, 2F, 0);
			this.markHurt();
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

			if (ertResult != null && ertResult.getEntity() != null && ertResult.getEntity() instanceof LivingEntity) {

				LivingEntity target = (LivingEntity) ertResult.getEntity();

				if (target != getOwner()) {
					Party p = null;
					if (getOwner() != null) {
						p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
					}
					if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { // If caster is not in a party || the party doesn't have the target in it || the
																										// party has FF on
						float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) / 5.75F : 2;

						if (target.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("undead")))) {
							target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHT,this, this.getOwner()), (dmg * dmgMult)*1.15F);
							//System.out.println((dmg * dmgMult)*1.15F);
						} else if (target instanceof IKHMob ikhMob) {
							if(ikhMob.getKHMobType() == EntityHelper.MobType.HEARTLESS_PUREBLOOD || ikhMob.getKHMobType() == EntityHelper.MobType.HEARTLESS_EMBLEM){
								target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHT,this, this.getOwner()), (dmg * dmgMult)*1.15F);
								//System.out.println((dmg * dmgMult)*1.15F);
							} else {
								target.hurt(damageSources().indirectMagic(this, this.getOwner()), dmg * dmgMult);
								//System.out.println((dmg * dmgMult));
							}
						} else {
							target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHT,this, this.getOwner()), dmg * dmgMult);
							System.out.println(dmg * dmgMult);
						}
						target.invulnerableTime = 0;
					}
				}
			}
		} else { // Block (not ERTR)
			remove(RemovalReason.KILLED);
		}
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putString("caster", this.getCaster());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		this.setCaster(compound.getString("caster"));
	}

	private static final EntityDataAccessor<String> CASTER = SynchedEntityData.defineId(MagnetEntity.class, EntityDataSerializers.STRING);

	public String getCaster() {
		return caster;
	}

	public void setCaster(String name) {
		this.entityData.set(CASTER, name);
		this.caster = name;
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (key.equals(CASTER)) {
			this.caster = this.getCasterDataManager();
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(CASTER, "");
	}

	public String getCasterDataManager() {
		return this.entityData.get(CASTER);
	}
}