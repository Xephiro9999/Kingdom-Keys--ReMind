package online.remind.remind.entity.magic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.remind.remind.entity.ModEntitiesRM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class ZettaflareBeamEntity extends Entity {


    // ============================================================
    // DAMAGE SETTINGS
    // ============================================================

    /**
     * Flat damage added to each sustained pulse.
     */
    private static final float BASE_HIT_DAMAGE =
            20.0F;


    /**
     * Damage gained per point of Magic.
     */
    private static final float MAGIC_SCALING =
            2.0F;


    /**
     * Sustained pulses additionally deal 8% maximum HP.
     */
    private static final float MAX_HEALTH_DAMAGE_PERCENT =
            0.08F;


    /**
     * Multiplier for the Magic-scaled portion of the final blast.
     */
    private static final float FINAL_BLAST_MULTIPLIER =
            4.0F;


    /**
     * Final blast additionally deals 20% maximum HP.
     */
    private static final float FINAL_MAX_HEALTH_PERCENT =
            0.20F;


    // ============================================================
    // BEAM SETTINGS
    // ============================================================

    /**
     * Zettaflare range.
     */
    private static final double BEAM_LENGTH =
            52.0D;


    /**
     * Gameplay collision radius.
     *
     * 1.75 radius = approximately 3.5 blocks wide.
     */
    private static final double BEAM_RADIUS =
            1.75D;


    /**
     * 100 ticks = 5 seconds.
     */
    private static final int MAX_LIFETIME =
            100;


    /**
     * 5 ticks = 4 damage pulses per second.
     */
    private static final int DAMAGE_INTERVAL =
            5;


    // ============================================================
    // ROOT SETTINGS
    // ============================================================

    /**
     * How far the caster can drift before the server explicitly
     * snaps them back to their casting position.
     *
     * This is intentionally tiny.
     */
    private static final double ROOT_POSITION_EPSILON_SQR =
            0.000001D;


    // ============================================================
    // BEAM VARIANTS
    // ============================================================

    public static final int VARIANT_ZETTAFLARE =
            0;

    public static final int VARIANT_FINAL_FLASH =
            1;

    public static final int VARIANT_KAMEHAMEHA =
            2;


    // ============================================================
    // SYNCED DATA
    // ============================================================

    /**
     * Runtime ID of the entity physically firing the beam.
     */
    private static final EntityDataAccessor<Integer> FIRING_ENTITY_ID =
            SynchedEntityData.defineId(
                    ZettaflareBeamEntity.class,
                    EntityDataSerializers.INT
            );


    /**
     * Visual / sound variant.
     */
    private static final EntityDataAccessor<Integer> BEAM_VARIANT =
            SynchedEntityData.defineId(
                    ZettaflareBeamEntity.class,
                    EntityDataSerializers.INT
            );


    // ============================================================
    // LOCKED AIM
    // ============================================================

    /**
     * Zettaflare's firing direction is captured once when the spell
     * begins.
     *
     * It is synced because the client renderer must use the exact
     * same direction as the server damage calculations.
     */
    private static final EntityDataAccessor<Float> LOCKED_DIR_X =
            SynchedEntityData.defineId(
                    ZettaflareBeamEntity.class,
                    EntityDataSerializers.FLOAT
            );


    private static final EntityDataAccessor<Float> LOCKED_DIR_Y =
            SynchedEntityData.defineId(
                    ZettaflareBeamEntity.class,
                    EntityDataSerializers.FLOAT
            );


    private static final EntityDataAccessor<Float> LOCKED_DIR_Z =
            SynchedEntityData.defineId(
                    ZettaflareBeamEntity.class,
                    EntityDataSerializers.FLOAT
            );


    // ============================================================
    // SAVED DATA
    // ============================================================

    /**
     * Entity physically firing the beam.
     */
    private UUID firingEntityUUID;


    /**
     * Player who receives damage credit.
     */
    private UUID ownerUUID;


    /**
     * Spell damage multiplier supplied by magicZettaflare.
     */
    private float damage =
            1.0F;


    // ============================================================
    // ROOT POSITION
    // ============================================================

    /**
     * Where the caster was standing when Zettaflare began.
     *
     * The caster is kept here for the entire beam duration.
     *
     * Rotation is NOT stored here because the camera is intentionally
     * allowed to move.
     */
    private double rootX;
    private double rootY;
    private double rootZ;


    private boolean rootPositionSet =
            false;


    // ============================================================
    // RUNTIME DATA
    // ============================================================

    /**
     * Target entity ID -> remaining Zettaflare hit cooldown.
     */
    private final Map<Integer, Integer> hitCooldowns =
            new HashMap<>();


    /**
     * Prevents the finishing blast from occurring more than once.
     */
    private boolean finalBlastDone =
            false;


    // ============================================================
    // CONSTRUCTORS
    // ============================================================

    /**
     * Constructor used by EntityType.
     */
    public ZettaflareBeamEntity(
            EntityType<? extends ZettaflareBeamEntity> type,
            Level level
    ) {

        super(
                type,
                level
        );


        this.noPhysics =
                true;
    }


    /**
     * Constructor used when actually casting Zettaflare.
     */
    public ZettaflareBeamEntity(
            Level level,
            LivingEntity firingEntity,
            Player owner,
            float damage
    ) {

        this(
                ModEntitiesRM.TYPE_ZETTAFLARE_BEAM.get(),
                level
        );


        // ========================================================
        // OWNER DATA
        // ========================================================

        this.firingEntityUUID =
                firingEntity.getUUID();


        this.ownerUUID =
                owner.getUUID();


        this.damage =
                damage;


        // ========================================================
        // CLIENT FIRING ENTITY
        // ========================================================

        this.entityData.set(
                FIRING_ENTITY_ID,
                firingEntity.getId()
        );


        // ========================================================
        // LOCK AIM
        // ========================================================

        /*
         * Capture the firing direction exactly once.
         *
         * Camera movement after this point cannot redirect the beam.
         */
        lockCurrentAim(
                firingEntity
        );


        // ========================================================
        // ROOT CASTER
        // ========================================================

        /*
         * Capture the caster's current world position.
         */
        captureRootPosition(
                firingEntity
        );


        // ========================================================
        // SPAWN CONTROLLER
        // ========================================================

        setPos(
                firingEntity.getX(),
                firingEntity.getEyeY(),
                firingEntity.getZ()
        );
    }


    // ============================================================
    // ENTITY DATA
    // ============================================================

    @Override
    protected void defineSynchedData(
            SynchedEntityData.Builder builder
    ) {

        builder.define(
                FIRING_ENTITY_ID,
                -1
        );


        builder.define(
                BEAM_VARIANT,
                VARIANT_ZETTAFLARE
        );


        builder.define(
                LOCKED_DIR_X,
                0.0F
        );


        builder.define(
                LOCKED_DIR_Y,
                0.0F
        );


        builder.define(
                LOCKED_DIR_Z,
                0.0F
        );
    }


    // ============================================================
    // VARIANT
    // ============================================================

    public void setBeamVariant(
            int variant
    ) {

        this.entityData.set(
                BEAM_VARIANT,
                variant
        );
    }


    public int getBeamVariant() {

        return this.entityData.get(
                BEAM_VARIANT
        );
    }


    // ============================================================
    // CAPTURE AIM
    // ============================================================

    private void lockCurrentAim(
            LivingEntity firingEntity
    ) {

        Vec3 direction =
                firingEntity
                        .getLookAngle();


        if (
                direction.lengthSqr()
                        <= 0.000001D
        ) {

            return;
        }


        direction =
                direction.normalize();


        this.entityData.set(
                LOCKED_DIR_X,
                (float) direction.x
        );


        this.entityData.set(
                LOCKED_DIR_Y,
                (float) direction.y
        );


        this.entityData.set(
                LOCKED_DIR_Z,
                (float) direction.z
        );
    }


    // ============================================================
    // LOCKED AIM ACCESS
    // ============================================================

    public Vec3 getLockedBeamDirection() {

        Vec3 direction =
                new Vec3(

                        this.entityData.get(
                                LOCKED_DIR_X
                        ),

                        this.entityData.get(
                                LOCKED_DIR_Y
                        ),

                        this.entityData.get(
                                LOCKED_DIR_Z
                        )
                );


        if (
                direction.lengthSqr()
                        <= 0.000001D
        ) {

            return Vec3.ZERO;
        }


        return direction.normalize();
    }


    // ============================================================
    // CAPTURE ROOT POSITION
    // ============================================================

    private void captureRootPosition(
            LivingEntity firingEntity
    ) {

        rootX =
                firingEntity.getX();


        rootY =
                firingEntity.getY();


        rootZ =
                firingEntity.getZ();


        rootPositionSet =
                true;


        /*
         * Riding something would otherwise allow the vehicle to move
         * the player despite Zettaflare's root.
         */
        if (
                firingEntity.isPassenger()
        ) {

            firingEntity.stopRiding();
        }
    }


    // ============================================================
    // ROOT CASTER
    // ============================================================

    /**
     * Completely prevents player movement during Zettaflare while
     * deliberately leaving rotation alone.
     *
     * The player may freely move their camera.
     */
    private void rootCaster(
            LivingEntity firingEntity
    ) {

        if (
                !rootPositionSet
        ) {

            captureRootPosition(
                    firingEntity
            );
        }


        // ========================================================
        // REMOVE VELOCITY
        // ========================================================

        /*
         * Stops:
         *
         * walking momentum
         * jumping
         * falling
         * knockback movement
         */
        firingEntity.setDeltaMovement(
                Vec3.ZERO
        );


        /*
         * Prevent the rooted player from accumulating fall damage
         * while suspended at the casting point.
         */
        firingEntity.fallDistance =
                0.0F;


        // ========================================================
        // PLAYER ROOT
        // ========================================================

        if (
                firingEntity instanceof ServerPlayer serverPlayer
        ) {

            /*
             * Turn sprinting off while rooted.
             */
            serverPlayer.setSprinting(
                    false
            );


            double dx =
                    serverPlayer.getX()
                            - rootX;


            double dy =
                    serverPlayer.getY()
                            - rootY;


            double dz =
                    serverPlayer.getZ()
                            - rootZ;


            double distanceSqr =
                    dx * dx
                            + dy * dy
                            + dz * dz;


            /*
             * Only send an actual teleport correction if the client
             * attempted to move.
             *
             * IMPORTANT:
             *
             * We use the player's CURRENT yaw and pitch.
             *
             * This means the correction does NOT lock their camera.
             */
            if (
                    distanceSqr
                            > ROOT_POSITION_EPSILON_SQR
            ) {

                serverPlayer.connection.teleport(
                        rootX,
                        rootY,
                        rootZ,

                        serverPlayer.getYRot(),
                        serverPlayer.getXRot()
                );
            }


            return;
        }


        // ========================================================
        // NON-PLAYER FALLBACK
        // ========================================================

        /*
         * If Zettaflare is ever fired by an NPC or another
         * LivingEntity, hard-lock its position too.
         */
        firingEntity.setPos(
                rootX,
                rootY,
                rootZ
        );
    }


    // ============================================================
    // TICK
    // ============================================================

    @Override
    public void tick() {

        super.tick();


        LivingEntity firingEntity =
                getFiringEntity();


        // ========================================================
        // RECOVER FIRING ENTITY
        // ========================================================

        if (
                !level().isClientSide
                        && firingEntity == null
                        && firingEntityUUID != null
                        && level() instanceof ServerLevel serverLevel
        ) {

            Entity found =
                    serverLevel.getEntity(
                            firingEntityUUID
                    );


            if (
                    found instanceof LivingEntity living
            ) {

                firingEntity =
                        living;


                this.entityData.set(
                        FIRING_ENTITY_ID,
                        living.getId()
                );
            }
        }


        // ========================================================
        // INVALID FIRING ENTITY
        // ========================================================

        if (
                !level().isClientSide
        ) {

            if (
                    firingEntity == null
                            || !firingEntity.isAlive()
            ) {

                discard();

                return;
            }
        }


        // ========================================================
        // SAFETY AIM INITIALIZATION
        // ========================================================

        /*
         * Handles old saves / weird development summons.
         *
         * Aim is ONLY captured if no locked direction exists.
         */
        if (
                !level().isClientSide
                        && firingEntity != null
                        && getLockedBeamDirection()
                        .lengthSqr()
                        <= 0.000001D
        ) {

            lockCurrentAim(
                    firingEntity
            );
        }


        // ========================================================
        // ROOT CASTER
        // ========================================================

        if (
                !level().isClientSide
                        && firingEntity != null
        ) {

            rootCaster(
                    firingEntity
            );
        }


        // ========================================================
        // FOLLOW ROOTED CASTER
        // ========================================================

        if (
                firingEntity != null
        ) {

            setPos(
                    firingEntity.getX(),
                    firingEntity.getEyeY(),
                    firingEntity.getZ()
            );
        }


        // ========================================================
        // DAMAGE
        // ========================================================

        if (
                !level().isClientSide
                        && firingEntity != null
        ) {

            tickDamage(
                    firingEntity
            );
        }


        // ========================================================
        // FINAL BLAST
        // ========================================================

        if (
                tickCount
                        >= MAX_LIFETIME
        ) {

            if (
                    !level().isClientSide
                            && !finalBlastDone
                            && firingEntity != null
            ) {

                finalBlastDone =
                        true;


                doFinalBlast(
                        firingEntity
                );


                applyZettaflareExhaustion(
                        firingEntity
                );
            }


            /*
             * Once this entity disappears, rootCaster() stops running.
             *
             * The player is therefore automatically free to move again.
             */
            discard();
        }
    }


    // ============================================================
    // SUSTAINED BEAM DAMAGE
    // ============================================================

    private void tickDamage(
            LivingEntity firingEntity
    ) {

        // ========================================================
        // HIT COOLDOWNS
        // ========================================================

        hitCooldowns.replaceAll(
                (id, ticks) ->
                        Math.max(
                                0,
                                ticks - 1
                        )
        );


        // ========================================================
        // OWNER
        // ========================================================

        Player owner =
                getOwnerPlayer();


        if (
                owner == null
        ) {

            return;
        }


        PlayerData playerData =
                PlayerData.get(
                        owner
                );


        if (
                playerData == null
        ) {

            return;
        }


        // ========================================================
        // MAGIC
        // ========================================================

        float magicStat =
                (float) playerData
                        .getMagicStat()
                        .getStat();


        // ========================================================
        // BEAM
        // ========================================================

        Vec3 start =
                firingEntity
                        .getEyePosition();


        Vec3 direction =
                getLockedBeamDirection();


        if (
                direction.lengthSqr()
                        <= 0.000001D
        ) {

            return;
        }


        Vec3 end =
                start.add(
                        direction.scale(
                                BEAM_LENGTH
                        )
                );


        // ========================================================
        // SEARCH
        // ========================================================

        AABB searchArea =
                new AABB(
                        start,
                        end
                ).inflate(
                        BEAM_RADIUS
                );


        List<LivingEntity> targets =
                level().getEntitiesOfClass(
                        LivingEntity.class,
                        searchArea,
                        entity ->
                                entity != firingEntity
                                        && entity.isAlive()
                );


        // ========================================================
        // DAMAGE TARGETS
        // ========================================================

        for (
                LivingEntity target : targets
        ) {

            if (
                    !isInsideBeam(
                            target,
                            start,
                            end
                    )
            ) {

                continue;
            }


            int id =
                    target.getId();


            if (
                    hitCooldowns.getOrDefault(
                            id,
                            0
                    ) > 0
            ) {

                continue;
            }


            // ====================================================
            // MAGIC DAMAGE
            // ====================================================

            float magicDamage =
                    BASE_HIT_DAMAGE
                            + (
                            magicStat
                                    * MAGIC_SCALING
                    );


            float scaledDamage =
                    magicDamage
                            * damage;


            // ====================================================
            // MAX HP DAMAGE
            // ====================================================

            float percentDamage =
                    target.getMaxHealth()
                            * MAX_HEALTH_DAMAGE_PERCENT;


            float finalDamage =
                    scaledDamage
                            + percentDamage;


            // ====================================================
            // APPLY DAMAGE
            // ====================================================

            target.invulnerableTime =
                    0;


            boolean damaged =
                    target.hurt(
                            target.damageSources()
                                    .indirectMagic(
                                            this,
                                            owner
                                    ),
                            finalDamage
                    );


            if (
                    damaged
            ) {

                hitCooldowns.put(
                        id,
                        DAMAGE_INTERVAL
                );
            }
        }
    }


    // ============================================================
    // FINAL BLAST
    // ============================================================

    private void doFinalBlast(
            LivingEntity firingEntity
    ) {

        Player owner =
                getOwnerPlayer();


        if (
                owner == null
        ) {

            return;
        }


        PlayerData playerData =
                PlayerData.get(
                        owner
                );


        if (
                playerData == null
        ) {

            return;
        }


        float magicStat =
                (float) playerData
                        .getMagicStat()
                        .getStat();


        // ========================================================
        // FINAL BEAM
        // ========================================================

        Vec3 start =
                firingEntity
                        .getEyePosition();


        Vec3 direction =
                getLockedBeamDirection();


        if (
                direction.lengthSqr()
                        <= 0.000001D
        ) {

            return;
        }


        Vec3 end =
                start.add(
                        direction.scale(
                                BEAM_LENGTH
                        )
                );


        // ========================================================
        // SEARCH
        // ========================================================

        AABB searchArea =
                new AABB(
                        start,
                        end
                ).inflate(
                        BEAM_RADIUS
                                + 1.0D
                );


        List<LivingEntity> targets =
                level().getEntitiesOfClass(
                        LivingEntity.class,
                        searchArea,
                        entity ->
                                entity != firingEntity
                                        && entity.isAlive()
                );


        // ========================================================
        // FINAL DAMAGE
        // ========================================================

        for (
                LivingEntity target : targets
        ) {

            if (
                    !isInsideBeam(
                            target,
                            start,
                            end
                    )
            ) {

                continue;
            }


            float magicDamage =
                    BASE_HIT_DAMAGE
                            + (
                            magicStat
                                    * MAGIC_SCALING
                    );


            float scaledDamage =
                    magicDamage
                            * damage
                            * FINAL_BLAST_MULTIPLIER;


            float percentDamage =
                    target.getMaxHealth()
                            * FINAL_MAX_HEALTH_PERCENT;


            float finalDamage =
                    scaledDamage
                            + percentDamage;


            target.invulnerableTime =
                    0;


            target.hurt(
                    target.damageSources()
                            .indirectMagic(
                                    this,
                                    owner
                            ),
                    finalDamage
            );
        }
    }


    // ============================================================
    // ZETTAFLARE EXHAUSTION
    // ============================================================

    private void applyZettaflareExhaustion(
            LivingEntity firingEntity
    ) {

        // ========================================================
        // HP
        // ========================================================

        firingEntity.setHealth(
                1.0F
        );


        // ========================================================
        // MP
        // ========================================================

        Player owner =
                getOwnerPlayer();


        if (
                owner == null
        ) {

            return;
        }


        PlayerData playerData =
                PlayerData.get(
                        owner
                );


        if (
                playerData == null
        ) {

            return;
        }


        playerData.addMP(
                -playerData.getMP()
        );
    }


    // ============================================================
    // BEAM COLLISION
    // ============================================================

    private boolean isInsideBeam(
            LivingEntity target,
            Vec3 start,
            Vec3 end
    ) {

        Vec3 targetCenter =
                target.getBoundingBox()
                        .getCenter();


        Vec3 beam =
                end.subtract(
                        start
                );


        double beamLengthSqr =
                beam.lengthSqr();


        if (
                beamLengthSqr
                        <= 0.0001D
        ) {

            return false;
        }


        // ========================================================
        // PROJECT TARGET ONTO BEAM
        // ========================================================

        Vec3 toTarget =
                targetCenter.subtract(
                        start
                );


        double t =
                toTarget.dot(
                        beam
                )
                        / beamLengthSqr;


        t =
                Math.max(
                        0.0D,
                        Math.min(
                                1.0D,
                                t
                        )
                );


        // ========================================================
        // CLOSEST POINT
        // ========================================================

        Vec3 closestPoint =
                start.add(
                        beam.scale(
                                t
                        )
                );


        // ========================================================
        // ENTITY RADIUS
        // ========================================================

        double targetRadius =
                Math.max(

                        target.getBbWidth()
                                * 0.5D,

                        target.getBbHeight()
                                * 0.25D
                );


        double combinedRadius =
                BEAM_RADIUS
                        + targetRadius;


        return targetCenter.distanceToSqr(
                closestPoint
        ) <= combinedRadius
                * combinedRadius;
    }


    // ============================================================
    // FIRING ENTITY
    // ============================================================

    public LivingEntity getFiringEntity() {

        int entityId =
                this.entityData.get(
                        FIRING_ENTITY_ID
                );


        if (
                entityId < 0
        ) {

            return null;
        }


        Entity entity =
                level().getEntity(
                        entityId
                );


        if (
                entity instanceof LivingEntity living
        ) {

            return living;
        }


        return null;
    }


    // ============================================================
    // OWNER
    // ============================================================

    private Player getOwnerPlayer() {

        if (
                ownerUUID == null
        ) {

            return null;
        }


        if (
                !(level() instanceof ServerLevel serverLevel)
        ) {

            return null;
        }


        return serverLevel.getPlayerByUUID(
                ownerUUID
        );
    }


    // ============================================================
    // RENDERER ACCESS
    // ============================================================

    public double getBeamLength() {

        return BEAM_LENGTH;
    }


    /**
     * Renderer now receives the locked firing direction.
     *
     * partialTick remains in the signature so the current renderer
     * does not need to be changed.
     */
    public Vec3 getBeamDirection(
            float partialTick
    ) {

        return getLockedBeamDirection();
    }


    public Vec3 getBeamStart() {

        LivingEntity firingEntity =
                getFiringEntity();


        if (
                firingEntity == null
        ) {

            return position();
        }


        return firingEntity
                .getEyePosition();
    }


    public Vec3 getBeamEnd() {

        Vec3 start =
                getBeamStart();


        Vec3 direction =
                getLockedBeamDirection();


        if (
                direction.lengthSqr()
                        <= 0.000001D
        ) {

            return start;
        }


        return start.add(
                direction.scale(
                        BEAM_LENGTH
                )
        );
    }


    // ============================================================
    // READ NBT
    // ============================================================

    @Override
    protected void readAdditionalSaveData(
            CompoundTag tag
    ) {

        // ========================================================
        // FIRING ENTITY
        // ========================================================

        if (
                tag.hasUUID(
                        "FiringEntity"
                )
        ) {

            firingEntityUUID =
                    tag.getUUID(
                            "FiringEntity"
                    );
        }


        // ========================================================
        // OWNER
        // ========================================================

        if (
                tag.hasUUID(
                        "Owner"
                )
        ) {

            ownerUUID =
                    tag.getUUID(
                            "Owner"
                    );
        }


        // ========================================================
        // DAMAGE
        // ========================================================

        if (
                tag.contains(
                        "Damage"
                )
        ) {

            damage =
                    tag.getFloat(
                            "Damage"
                    );
        }


        // ========================================================
        // VARIANT
        // ========================================================

        if (
                tag.contains(
                        "BeamVariant"
                )
        ) {

            setBeamVariant(
                    tag.getInt(
                            "BeamVariant"
                    )
            );
        }


        // ========================================================
        // LOCKED DIRECTION
        // ========================================================

        if (
                tag.contains(
                        "LockedDirX"
                )
                        && tag.contains(
                        "LockedDirY"
                )
                        && tag.contains(
                        "LockedDirZ"
                )
        ) {

            Vec3 direction =
                    new Vec3(

                            tag.getFloat(
                                    "LockedDirX"
                            ),

                            tag.getFloat(
                                    "LockedDirY"
                            ),

                            tag.getFloat(
                                    "LockedDirZ"
                            )
                    );


            if (
                    direction.lengthSqr()
                            > 0.000001D
            ) {

                direction =
                        direction.normalize();


                this.entityData.set(
                        LOCKED_DIR_X,
                        (float) direction.x
                );


                this.entityData.set(
                        LOCKED_DIR_Y,
                        (float) direction.y
                );


                this.entityData.set(
                        LOCKED_DIR_Z,
                        (float) direction.z
                );
            }
        }


        // ========================================================
        // ROOT POSITION
        // ========================================================

        if (
                tag.contains(
                        "RootX"
                )
                        && tag.contains(
                        "RootY"
                )
                        && tag.contains(
                        "RootZ"
                )
        ) {

            rootX =
                    tag.getDouble(
                            "RootX"
                    );


            rootY =
                    tag.getDouble(
                            "RootY"
                    );


            rootZ =
                    tag.getDouble(
                            "RootZ"
                    );


            rootPositionSet =
                    true;
        }


        // ========================================================
        // FINAL BLAST
        // ========================================================

        if (
                tag.contains(
                        "FinalBlastDone"
                )
        ) {

            finalBlastDone =
                    tag.getBoolean(
                            "FinalBlastDone"
                    );
        }
    }


    // ============================================================
    // WRITE NBT
    // ============================================================

    @Override
    protected void addAdditionalSaveData(
            CompoundTag tag
    ) {

        // ========================================================
        // FIRING ENTITY
        // ========================================================

        if (
                firingEntityUUID != null
        ) {

            tag.putUUID(
                    "FiringEntity",
                    firingEntityUUID
            );
        }


        // ========================================================
        // OWNER
        // ========================================================

        if (
                ownerUUID != null
        ) {

            tag.putUUID(
                    "Owner",
                    ownerUUID
            );
        }


        // ========================================================
        // DAMAGE
        // ========================================================

        tag.putFloat(
                "Damage",
                damage
        );


        // ========================================================
        // VARIANT
        // ========================================================

        tag.putInt(
                "BeamVariant",
                getBeamVariant()
        );


        // ========================================================
        // AIM
        // ========================================================

        Vec3 direction =
                getLockedBeamDirection();


        tag.putFloat(
                "LockedDirX",
                (float) direction.x
        );


        tag.putFloat(
                "LockedDirY",
                (float) direction.y
        );


        tag.putFloat(
                "LockedDirZ",
                (float) direction.z
        );


        // ========================================================
        // ROOT POSITION
        // ========================================================

        if (
                rootPositionSet
        ) {

            tag.putDouble(
                    "RootX",
                    rootX
            );


            tag.putDouble(
                    "RootY",
                    rootY
            );


            tag.putDouble(
                    "RootZ",
                    rootZ
            );
        }


        // ========================================================
        // FINAL BLAST
        // ========================================================

        tag.putBoolean(
                "FinalBlastDone",
                finalBlastDone
        );
    }
}