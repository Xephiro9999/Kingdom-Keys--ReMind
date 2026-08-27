package online.remind.remind.entity.magic;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.TrainingDummyEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.ModEntitiesRM;

import java.util.List;

public class UltimaEntity extends ThrowableProjectile {


	// ============================================================
	// PHASES
	// ============================================================

	public static final int PHASE_FLYING = 0;

	public static final int PHASE_IMPLODING = 1;

	public static final int PHASE_EXPANDING = 2;


	// ============================================================
	// PROJECTILE SETTINGS
	// ============================================================

	/**
	 * How far Ultima can travel before detonating by itself.
	 */
	private static final double MAX_RANGE = 40.0D;


	/**
	 * Absolute failsafe.
	 */
	private static final int MAX_TOTAL_LIFETIME = 240;


	// ============================================================
	// IMPLOSION SETTINGS
	// ============================================================

	/**
	 * Duration of the inward-collapse animation.
	 *
	 * 10 ticks = 0.5 seconds.
	 */
	private static final int IMPLOSION_DURATION = 16;


	/**
	 * Size of the sphere when the implosion begins.
	 */
	private static final float IMPLOSION_START_RADIUS = 4.0F;


	/**
	 * How tiny it becomes before exploding outward.
	 */
	private static final float IMPLOSION_END_RADIUS = 0.15F;


	// ============================================================
	// EXPANSION SETTINGS
	// ============================================================

	/**
	 * Duration of the outward Ultima field.
	 *
	 * 40 ticks = 2 seconds.
	 */
	private static final int EXPANSION_DURATION = 40;


	/**
	 * Final radius of Ultima.
	 */
	private static final float MAX_EXPANSION_RADIUS = 8.0F;


	/**
	 * How frequently enemies inside Ultima take DoT.
	 *
	 * 5 ticks = 4 times per second.
	 */
	private static final int DOT_INTERVAL = 5;


	// ============================================================
	// DAMAGE SETTINGS
	// ============================================================

	/**
	 * Initial detonation.
	 *
	 * 75% of the calculated Kingdom Keys Magic Damage.
	 */
	private static final float INITIAL_DAMAGE_MULTIPLIER = 0.75F;


	/**
	 * Damage dealt on every DoT pulse.
	 */
	private static final float DOT_DAMAGE_MULTIPLIER = 0.12F;


	/**
	 * Initial burst radius.
	 *
	 * This guarantees the directly-hit target gets caught even
	 * though the projectile normally intersects its outer hitbox.
	 */
	private static final float INITIAL_DAMAGE_RADIUS = 3.0F;


	/**
	 * Preserves the basic damage cap behavior old Ultima used.
	 */
	private static final float DAMAGE_CAP = 99.0F;


	// ============================================================
	// SYNCED DATA
	// ============================================================

	private static final EntityDataAccessor<Integer> PHASE =
			SynchedEntityData.defineId(
					UltimaEntity.class,
					EntityDataSerializers.INT
			);


	// ============================================================
	// RUNTIME DATA
	// ============================================================

	private float dmgMult = 1.0F;

	private int phaseTicks = 0;


	// ============================================================
	// ORIGIN / RANGE
	// ============================================================

	private boolean originSet = false;

	private double originX;
	private double originY;
	private double originZ;


	// ============================================================
	// CONSTRUCTORS
	// ============================================================

	public UltimaEntity(
			EntityType<? extends ThrowableProjectile> type,
			Level world
	) {

		super(type, world);

		this.blocksBuilding = true;
	}


	public UltimaEntity(
			Level world
	) {

		super(
				ModEntitiesRM.TYPE_ULTIMA.get(),
				world
		);

		this.blocksBuilding = true;
	}


	public UltimaEntity(
			Level world,
			LivingEntity caster,
			float dmgMult
	) {

		super(
				ModEntitiesRM.TYPE_ULTIMA.get(),
				caster,
				world
		);

		this.blocksBuilding = true;

		this.dmgMult =
				dmgMult;
	}


	// ============================================================
	// GRAVITY
	// ============================================================

	@Override
	protected double getDefaultGravity() {

		/*
		 * Ultima flies perfectly straight.
		 */
		return 0.0D;
	}


	// ============================================================
	// TICK
	// ============================================================

	@Override
	public void tick() {


		// ========================================================
		// SERVER LOGIC
		// ========================================================

		if (!level().isClientSide) {


			// ----------------------------------------------------
			// Failsafe lifetime
			// ----------------------------------------------------

			if (tickCount > MAX_TOTAL_LIFETIME) {

				discard();

				return;
			}


			// ----------------------------------------------------
			// Owner validation
			// ----------------------------------------------------

			if (
					getOwner() == null
							|| level().getServer() == null
			) {

				discard();

				return;
			}


			// ----------------------------------------------------
			// Save projectile starting location
			// ----------------------------------------------------

			/*
			 * We DON'T do this in the constructor because
			 * magicUltima changes the projectile position after
			 * constructing it.
			 */
			if (!originSet) {

				originSet = true;

				originX = getX();
				originY = getY();
				originZ = getZ();
			}


			// ====================================================
			// PHASE LOGIC
			// ====================================================

			switch (getPhase()) {


				// =================================================
				// FLYING
				// =================================================

				case PHASE_FLYING ->

						tickFlying();


				// =================================================
				// IMPLODING
				// =================================================

				case PHASE_IMPLODING ->

						tickImplosion();


				// =================================================
				// EXPANDING
				// =================================================

				case PHASE_EXPANDING ->

						tickExpansion();
			}
		}


		/*
		 * Keep vanilla projectile networking/ticking working.
		 *
		 * Once Ultima begins imploding its movement is forced to
		 * zero, so this won't move the effect anymore.
		 */
		super.tick();
	}


	// ============================================================
	// FLYING PHASE
	// ============================================================

	private void tickFlying() {


		// --------------------------------------------------------
		// Projectile trail
		// --------------------------------------------------------

		if (level() instanceof ServerLevel serverLevel) {

			serverLevel.sendParticles(
					ParticleTypes.ENCHANT,

					getX(),
					getY(),
					getZ(),

					3,

					0.12D,
					0.12D,
					0.12D,

					0.01D
			);


			serverLevel.sendParticles(
					ParticleTypes.END_ROD,

					getX(),
					getY(),
					getZ(),

					1,

					0.02D,
					0.02D,
					0.02D,

					0.0D
			);
		}


		// --------------------------------------------------------
		// Maximum range
		// --------------------------------------------------------

		double dx =
				getX() - originX;

		double dy =
				getY() - originY;

		double dz =
				getZ() - originZ;


		double distanceSquared =
				dx * dx
						+ dy * dy
						+ dz * dz;


		if (
				distanceSquared
						>= MAX_RANGE * MAX_RANGE
		) {

			beginImplosion(
					position()
			);
		}
	}


	// ============================================================
	// BEGIN IMPLOSION
	// ============================================================

	private void beginImplosion(
			Vec3 impactPosition
	) {

		/*
		 * Prevent the same collision from restarting Ultima.
		 */
		if (
				getPhase()
						!= PHASE_FLYING
		) {

			return;
		}


		// --------------------------------------------------------
		// Lock the effect at the impact point
		// --------------------------------------------------------

		setPos(
				impactPosition.x,
				impactPosition.y,
				impactPosition.z
		);


		setDeltaMovement(
				Vec3.ZERO
		);


		/*
		 * The projectile no longer needs physical collision.
		 */
		this.noPhysics = true;


		/*
		 * Hide the actual projectile model.
		 *
		 * From this point on, the particle sphere IS Ultima.
		 */
		setInvisible(
				true
		);


		// --------------------------------------------------------
		// Change state
		// --------------------------------------------------------

		setPhase(
				PHASE_IMPLODING
		);


		phaseTicks =
				0;


		// --------------------------------------------------------
		// Implosion sound
		// --------------------------------------------------------

		playSound(
				ModSoundsRM.ULTIMA_CAST.get(),
				1.0F,
				1.0F
		);
	}


	// ============================================================
	// IMPLOSION
	// ============================================================

	private void tickImplosion() {


		/*
		 * Ultima absolutely does not move anymore.
		 */
		setDeltaMovement(
				Vec3.ZERO
		);


		phaseTicks++;


		// --------------------------------------------------------
		// Progress
		// --------------------------------------------------------

		float progress =
				Math.min(
						1.0F,

						phaseTicks
								/ (float) IMPLOSION_DURATION
				);


		/*
		 * Accelerate inward.
		 *
		 * progress² makes the collapse get increasingly violent.
		 */
		float easedProgress =
				progress * progress;


		float radius =
				lerp(
						IMPLOSION_START_RADIUS,
						IMPLOSION_END_RADIUS,
						easedProgress
				);


		// --------------------------------------------------------
		// Particle sphere
		// --------------------------------------------------------

		spawnSphere(
				radius,
				ParticleTypes.ENCHANT,
				56
		);


		/*
		 * Add some energy being dragged toward the center.
		 */
		if (
				phaseTicks % 2 == 0
						&& level() instanceof ServerLevel serverLevel
		) {

			serverLevel.sendParticles(
					ParticleTypes.END_ROD,

					getX(),
					getY(),
					getZ(),

					4,

					radius * 0.3D,
					radius * 0.3D,
					radius * 0.3D,

					0.0D
			);
		}


		// ========================================================
		// IMPLOSION COMPLETE
		// ========================================================

		if (
				phaseTicks
						>= IMPLOSION_DURATION
		) {

			beginExpansion();
		}
	}


	// ============================================================
	// BEGIN EXPANSION
	// ============================================================

	private void beginExpansion() {


		setPhase(
				PHASE_EXPANDING
		);


		phaseTicks =
				0;


		// --------------------------------------------------------
		// Explosion sound
		// --------------------------------------------------------

		playSound(
				ModSoundsRM.ULTIMA_EXPLOSION.get(),
				1.25F,
				1.0F
		);


		// --------------------------------------------------------
		// Flash
		// --------------------------------------------------------

		if (level() instanceof ServerLevel serverLevel) {

			serverLevel.sendParticles(
					ParticleTypes.FLASH,

					getX(),
					getY(),
					getZ(),

					1,

					0.0D,
					0.0D,
					0.0D,

					0.0D
			);


			serverLevel.sendParticles(
					ParticleTypes.END_ROD,

					getX(),
					getY(),
					getZ(),

					30,

					0.5D,
					0.5D,
					0.5D,

					0.15D
			);
		}


		// ========================================================
		// INITIAL DAMAGE
		// ========================================================

		dealAreaDamage(
				INITIAL_DAMAGE_RADIUS,
				INITIAL_DAMAGE_MULTIPLIER
		);
	}


	// ============================================================
	// EXPANSION
	// ============================================================

	private void tickExpansion() {


		setDeltaMovement(
				Vec3.ZERO
		);


		phaseTicks++;


		// --------------------------------------------------------
		// Expansion progress
		// --------------------------------------------------------

		float progress =
				Math.min(
						1.0F,

						phaseTicks
								/ (float) EXPANSION_DURATION
				);


		/*
		 * Ease outward quickly.
		 *
		 * This makes it explode outward aggressively instead of
		 * growing like a slow bubble.
		 */
		float easedProgress =
				1.0F
						- (
						1.0F - progress
				)
						* (
						1.0F - progress
				);


		float radius =
				MAX_EXPANSION_RADIUS
						* easedProgress;


		// --------------------------------------------------------
		// Main expanding shell
		// --------------------------------------------------------

		spawnSphere(
				radius,
				ParticleTypes.ENCHANT,
				72
		);


		// --------------------------------------------------------
		// Inner energy
		// --------------------------------------------------------

		spawnSphere(
				radius * 0.70F,
				ParticleTypes.END_ROD,
				32
		);


		// ========================================================
		// DAMAGE OVER TIME
		// ========================================================

		if (
				phaseTicks
						% DOT_INTERVAL
						== 0
		) {

			dealAreaDamage(
					radius,
					DOT_DAMAGE_MULTIPLIER
			);
		}


		// ========================================================
		// FINISHED
		// ========================================================

		if (
				phaseTicks
						>= EXPANSION_DURATION
		) {

			discard();
		}
	}


	// ============================================================
	// DAMAGE
	// ============================================================

	private void dealAreaDamage(
			float radius,
			float damageMultiplier
	) {


		if (
				getOwner() == null
						|| level().getServer() == null
		) {

			return;
		}


		// --------------------------------------------------------
		// Broad-phase entity search
		// --------------------------------------------------------

		List<Entity> entities =
				level().getEntities(
						this,

						getBoundingBox()
								.inflate(
										radius
								)
				);


		if (entities.isEmpty()) {
			return;
		}


		// --------------------------------------------------------
		// Actual spherical hit detection
		// --------------------------------------------------------

		Vec3 center =
				position();


		double radiusSquared =
				radius * radius;


		for (Entity entity : entities) {


			if (
					!(entity instanceof LivingEntity target)
			) {

				continue;
			}


			if (
					!canDamageTarget(
							target
					)
			) {

				continue;
			}


			/*
			 * Don't use only the inflated AABB.
			 *
			 * The visual is a sphere, so damage should also
			 * actually be spherical.
			 */
			Vec3 targetCenter =
					target.getBoundingBox()
							.getCenter();


			if (
					targetCenter.distanceToSqr(
							center
					)
							> radiusSquared
			) {

				continue;
			}


			// ----------------------------------------------------
			// Damage calculation
			// ----------------------------------------------------

			float damage;


			if (
					getOwner()
							instanceof Player ownerPlayer
			) {

				damage =
						DamageCalculation.getMagicDamage(
								ownerPlayer
						)
								* damageMultiplier;


				damage =
						Math.min(
								damage,
								DAMAGE_CAP
						);

			} else {

				damage =
						2.0F
								* damageMultiplier;
			}


			damage *=
					dmgMult;


			// ----------------------------------------------------
			// Allow every scheduled DoT pulse to land
			// ----------------------------------------------------

			target.invulnerableTime =
					0;


			target.hurt(
					damageSources().indirectMagic(
							this,
							getOwner()
					),

					damage
			);
		}
	}


	// ============================================================
	// VALID TARGET
	// ============================================================

	private boolean canDamageTarget(
			LivingEntity target
	) {


		if (
				target == getOwner()
						|| !target.isAlive()
		) {

			return false;
		}


		/*
		 * Keep the same general enemy rules old Ultima used.
		 */
		if (
				!Utils.isHostile(target)
						&& !(target instanceof Slime)
						&& !(target instanceof EnderMan)
						&& !(target instanceof TrainingDummyEntity)
		) {

			return false;
		}


		// --------------------------------------------------------
		// Party friendly fire
		// --------------------------------------------------------

		if (
				getOwner() != null
						&& getOwner().getServer() != null
		) {

			WorldData worldData =
					WorldData.get(
							getOwner().getServer()
					);


			if (worldData != null) {

				Party party =
						worldData.getPartyFromMember(
								getOwner().getUUID()
						);


				if (
						party != null
								&& !party.getFriendlyFire()
								&& party.getMember(
								target.getUUID()
						) != null
				) {

					return false;
				}
			}
		}


		return true;
	}


	// ============================================================
	// COLLISION
	// ============================================================

	@Override
	protected void onHit(
			HitResult result
	) {


		if (
				level().isClientSide
						|| getPhase()
						!= PHASE_FLYING
		) {

			return;
		}


		// ========================================================
		// ENTITY HIT
		// ========================================================

		if (
				result
						instanceof EntityHitResult entityHit
		) {

			Entity hitEntity =
					entityHit.getEntity();


			if (
					hitEntity
							instanceof LivingEntity living
			) {


				/*
				 * Owner and protected party members do not
				 * detonate Ultima.
				 */
				if (
						living == getOwner()
				) {

					return;
				}


				if (
						!canDamageTarget(
								living
						)
				) {

					return;
				}


				beginImplosion(
						result.getLocation()
				);

				return;
			}
		}


		// ========================================================
		// BLOCK HIT
		// ========================================================

		if (
				result
						instanceof BlockHitResult
		) {

			beginImplosion(
					result.getLocation()
			);
		}
	}


	// ============================================================
	// PARTICLE SPHERE
	// ============================================================

	private void spawnSphere(
			float radius,
			ParticleOptions particle,
			int points
	) {


		if (
				!(level() instanceof ServerLevel serverLevel)
		) {

			return;
		}


		/*
		 * Fibonacci sphere.
		 *
		 * This distributes particles much more evenly than the
		 * old nested 0-360 degree loops.
		 */
		double goldenAngle =
				Math.PI
						* (
						3.0D
								- Math.sqrt(
								5.0D
						)
				);


		for (
				int i = 0;
				i < points;
				i++
		) {


			double y =
					1.0D
							- (
							i
									/ (double) (
									points - 1
							)
					)
							* 2.0D;


			double horizontalRadius =
					Math.sqrt(
							1.0D
									- y * y
					);


			double theta =
					goldenAngle
							* i;


			double x =
					Math.cos(
							theta
					)
							* horizontalRadius;


			double z =
					Math.sin(
							theta
					)
							* horizontalRadius;


			serverLevel.sendParticles(
					particle,

					getX()
							+ x * radius,

					getY()
							+ y * radius,

					getZ()
							+ z * radius,

					1,

					0.0D,
					0.0D,
					0.0D,

					0.0D
			);
		}
	}


	// ============================================================
	// LERP
	// ============================================================

	private float lerp(
			float start,
			float end,
			float progress
	) {

		return start
				+ (
				end - start
		)
				* progress;
	}


	// ============================================================
	// PHASE ACCESS
	// ============================================================

	public int getPhase() {

		return entityData.get(
				PHASE
		);
	}


	private void setPhase(int phase) {

		this.entityData.set(
				PHASE,
				phase
		);

		this.entityData.set(
				PHASE_START_TICK,
				this.tickCount
		);
	}

	public int getPhaseStartTick() {

		return this.entityData.get(
				PHASE_START_TICK
		);
	}

	private static final EntityDataAccessor<Integer> PHASE_START_TICK =
			SynchedEntityData.defineId(
					UltimaEntity.class,
					EntityDataSerializers.INT
			);


	// ============================================================
	// SYNCED DATA
	// ============================================================

	@Override
	protected void defineSynchedData(
			SynchedEntityData.Builder builder
	) {

		builder.define(
				PHASE,
				PHASE_FLYING
		);

		builder.define(
				PHASE_START_TICK,
				0
		);
	}




	// ============================================================
	// SAVE DATA
	// ============================================================

	@Override
	public void addAdditionalSaveData(
			CompoundTag compound
	) {

		super.addAdditionalSaveData(
				compound
		);


		compound.putInt(
				"UltimaPhase",
				getPhase()
		);


		compound.putInt(
				"UltimaPhaseTicks",
				phaseTicks
		);


		compound.putFloat(
				"UltimaDamageMultiplier",
				dmgMult
		);


		compound.putBoolean(
				"UltimaOriginSet",
				originSet
		);


		if (originSet) {

			compound.putDouble(
					"UltimaOriginX",
					originX
			);

			compound.putDouble(
					"UltimaOriginY",
					originY
			);

			compound.putDouble(
					"UltimaOriginZ",
					originZ
			);
		}
	}


	// ============================================================
	// LOAD DATA
	// ============================================================

	@Override
	public void readAdditionalSaveData(
			CompoundTag compound
	) {

		super.readAdditionalSaveData(
				compound
		);


		if (
				compound.contains(
						"UltimaPhase"
				)
		) {

			setPhase(
					compound.getInt(
							"UltimaPhase"
					)
			);
		}


		phaseTicks =
				compound.getInt(
						"UltimaPhaseTicks"
				);


		if (
				compound.contains(
						"UltimaDamageMultiplier"
				)
		) {

			dmgMult =
					compound.getFloat(
							"UltimaDamageMultiplier"
					);
		}


		originSet =
				compound.getBoolean(
						"UltimaOriginSet"
				);


		if (originSet) {

			originX =
					compound.getDouble(
							"UltimaOriginX"
					);

			originY =
					compound.getDouble(
							"UltimaOriginY"
					);

			originZ =
					compound.getDouble(
							"UltimaOriginZ"
					);
		}
	}
}