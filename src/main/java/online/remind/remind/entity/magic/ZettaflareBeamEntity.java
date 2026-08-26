package online.remind.remind.entity.magic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.remind.remind.entity.ModEntitiesRM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ZettaflareBeamEntity extends Entity {

    // ============================================================
    // BEAM SETTINGS
    // ============================================================

    /**
     * Maximum distance of Zettaflare.
     */
    private static final double BEAM_LENGTH = 48.0D;

    /**
     * Gameplay collision radius.
     *
     * 1.75 radius = roughly 3.5 blocks wide.
     */
    private static final double BEAM_RADIUS = 1.75D;

    /**
     * How long the beam remains active.
     *
     * 50 ticks = 2.5 seconds.
     */
    private static final int MAX_LIFETIME = 50;

    /**
     * How frequently the same enemy can be damaged.
     *
     * 5 ticks = 4 hits per second.
     */
    private static final int DAMAGE_INTERVAL = 5;


    // ============================================================
    // SYNCED DATA
    // ============================================================

    /**
     * Runtime entity ID of whoever is physically firing Zettaflare.
     *
     * This is synchronized so the client renderer knows whose
     * facing direction the beam should use.
     */
    private static final EntityDataAccessor<Integer> FIRING_ENTITY_ID =
            SynchedEntityData.defineId(
                    ZettaflareBeamEntity.class,
                    EntityDataSerializers.INT
            );


    // ============================================================
    // SAVED DATA
    // ============================================================

    /**
     * Persistent UUID of the entity physically firing the spell.
     */
    private UUID firingEntityUUID;

    /**
     * Player who owns the cast / should receive damage credit.
     */
    private UUID ownerUUID;

    /**
     * Damage dealt on each beam damage interval.
     */
    private float damage = 10.0F;


    // ============================================================
    // RUNTIME DATA
    // ============================================================

    /**
     * Prevents Zettaflare from damaging the same mob every single tick.
     *
     * Entity ID -> remaining cooldown.
     */
    private final Map<Integer, Integer> hitCooldowns =
            new HashMap<>();


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
        super(type, level);

        this.noPhysics = true;
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

        this.firingEntityUUID = firingEntity.getUUID();
        this.ownerUUID = owner.getUUID();
        this.damage = damage;

        /*
         * Runtime ID used by the client renderer.
         */
        this.entityData.set(
                FIRING_ENTITY_ID,
                firingEntity.getId()
        );

        /*
         * Spawn the controller at the caster's eyes.
         */
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
    }


    // ============================================================
    // TICK
    // ============================================================

    @Override
    public void tick() {
        super.tick();

        LivingEntity firingEntity = getFiringEntity();

        /*
         * Server can recover the runtime entity ID from the UUID
         * after world reloads.
         */
        if (!level().isClientSide
                && firingEntity == null
                && firingEntityUUID != null
                && level() instanceof ServerLevel serverLevel) {

            Entity found =
                    serverLevel.getEntity(firingEntityUUID);

            if (found instanceof LivingEntity living) {

                firingEntity = living;

                this.entityData.set(
                        FIRING_ENTITY_ID,
                        living.getId()
                );
            }
        }

        /*
         * The server decides whether this beam continues existing.
         *
         * Don't immediately discard client-side just because the
         * firing entity hasn't synchronized yet.
         */
        if (!level().isClientSide) {

            if (firingEntity == null || !firingEntity.isAlive()) {
                discard();
                return;
            }
        }

        /*
         * Follow the firing entity.
         *
         * Doing this client-side too keeps the visual attached
         * tightly to the player.
         */
        if (firingEntity != null) {

            setPos(
                    firingEntity.getX(),
                    firingEntity.getEyeY(),
                    firingEntity.getZ()
            );
        }

        /*
         * DAMAGE IS SERVER-SIDE ONLY.
         */
        if (!level().isClientSide
                && firingEntity != null) {

            tickDamage(firingEntity);
        }

        /*
         * End the beam.
         */
        if (tickCount >= MAX_LIFETIME) {
            discard();
        }
    }


    // ============================================================
    // DAMAGE
    // ============================================================

    private void tickDamage(
            LivingEntity firingEntity
    ) {

        /*
         * Count every target's internal Zettaflare cooldown down.
         */
        hitCooldowns.replaceAll(
                (id, ticks) ->
                        Math.max(0, ticks - 1)
        );


        // --------------------------------------------------------
        // Beam line
        // --------------------------------------------------------

        Vec3 start =
                firingEntity.getEyePosition();

        Vec3 direction =
                firingEntity.getLookAngle().normalize();

        Vec3 end =
                start.add(
                        direction.scale(BEAM_LENGTH)
                );


        // --------------------------------------------------------
        // Broad-phase collision search
        // --------------------------------------------------------

        AABB searchArea =
                new AABB(start, end)
                        .inflate(BEAM_RADIUS);


        List<LivingEntity> targets =
                level().getEntitiesOfClass(
                        LivingEntity.class,
                        searchArea,
                        entity ->
                                entity != firingEntity
                                        && entity.isAlive()
                );


        // --------------------------------------------------------
        // Check each entity against the actual beam line
        // --------------------------------------------------------

        for (LivingEntity target : targets) {

            if (!isInsideBeam(
                    target,
                    start,
                    end
            )) {
                continue;
            }

            int id =
                    target.getId();


            /*
             * This enemy was hit recently by this same beam.
             */
            if (hitCooldowns.getOrDefault(
                    id,
                    0
            ) > 0) {
                continue;
            }


            Player owner =
                    getOwnerPlayer();

            if (owner == null) {
                continue;
            }


            /*
             * Zettaflare has its own hit interval, so don't allow
             * Minecraft's normal hurt cooldown to eat beam hits.
             */
            target.invulnerableTime = 0;


            boolean damaged =
                    target.hurt(
                            target.damageSources().indirectMagic(
                                    this,
                                    owner
                            ),
                            damage
                    );


            /*
             * Only start our internal cooldown if damage actually
             * succeeded.
             */
            if (damaged) {

                hitCooldowns.put(
                        id,
                        DAMAGE_INTERVAL
                );
            }
        }
    }


    // ============================================================
    // BEAM COLLISION
    // ============================================================

    /**
     * Tests the actual distance between an entity and the Zettaflare
     * beam line.
     *
     * The giant AABB above is only used to FIND possible targets.
     * This determines whether they're really inside the beam.
     */
    private boolean isInsideBeam(
            LivingEntity target,
            Vec3 start,
            Vec3 end
    ) {

        Vec3 targetCenter =
                target.getBoundingBox().getCenter();


        Vec3 beam =
                end.subtract(start);

        double beamLengthSqr =
                beam.lengthSqr();


        if (beamLengthSqr <= 0.0001D) {
            return false;
        }


        /*
         * Find where the target projects onto the beam.
         */
        Vec3 toTarget =
                targetCenter.subtract(start);


        double t =
                toTarget.dot(beam)
                        / beamLengthSqr;


        /*
         * Clamp to the beginning/end of the beam.
         */
        t = Math.max(
                0.0D,
                Math.min(
                        1.0D,
                        t
                )
        );


        /*
         * Closest point along the beam to this entity.
         */
        Vec3 closestPoint =
                start.add(
                        beam.scale(t)
                );


        /*
         * Give larger mobs a larger effective collision radius.
         */
        double targetRadius =
                Math.max(
                        target.getBbWidth() * 0.5D,
                        target.getBbHeight() * 0.25D
                );


        double combinedRadius =
                BEAM_RADIUS
                        + targetRadius;


        return targetCenter.distanceToSqr(
                closestPoint
        ) <= combinedRadius * combinedRadius;
    }


    // ============================================================
    // FIRING ENTITY
    // ============================================================

    /**
     * Works on BOTH server and client.
     *
     * This is especially important for ZettaflareBeamRenderer.
     */
    public LivingEntity getFiringEntity() {

        int entityId =
                this.entityData.get(
                        FIRING_ENTITY_ID
                );


        if (entityId < 0) {
            return null;
        }


        Entity entity =
                level().getEntity(entityId);


        if (entity instanceof LivingEntity living) {
            return living;
        }


        return null;
    }


    // ============================================================
    // OWNER
    // ============================================================

    private Player getOwnerPlayer() {

        if (ownerUUID == null) {
            return null;
        }


        if (!(level() instanceof ServerLevel serverLevel)) {
            return null;
        }


        return serverLevel.getPlayerByUUID(
                ownerUUID
        );
    }


    // ============================================================
    // RENDERER ACCESS
    // ============================================================

    /**
     * Used by ZettaflareBeamRenderer.
     */
    public double getBeamLength() {
        return BEAM_LENGTH;
    }


    /**
     * Used by ZettaflareBeamRenderer.
     *
     * Because FIRING_ENTITY_ID is synced, the client can follow
     * the caster's actual facing direction.
     */
    public Vec3 getBeamDirection(
            float partialTick
    ) {

        LivingEntity firingEntity =
                getFiringEntity();


        if (firingEntity == null) {
            return Vec3.ZERO;
        }


        /*
         * Interpolate pitch/yaw so the beam turns smoothly instead
         * of snapping every client tick.
         */
        float yaw =
                firingEntity.yRotO
                        + (
                        firingEntity.getYRot()
                                - firingEntity.yRotO
                ) * partialTick;


        float pitch =
                firingEntity.xRotO
                        + (
                        firingEntity.getXRot()
                                - firingEntity.xRotO
                ) * partialTick;


        float yawRad =
                yaw
                        * ((float) Math.PI / 180F);


        float pitchRad =
                pitch
                        * ((float) Math.PI / 180F);


        float cosPitch =
                (float) Math.cos(pitchRad);


        return new Vec3(
                -Math.sin(yawRad) * cosPitch,
                -Math.sin(pitchRad),
                Math.cos(yawRad) * cosPitch
        ).normalize();
    }


    /**
     * Can also be useful later when rendering muzzle/impact effects.
     */
    public Vec3 getBeamStart() {

        LivingEntity firingEntity =
                getFiringEntity();


        if (firingEntity == null) {
            return position();
        }


        return firingEntity.getEyePosition();
    }


    /**
     * Useful later for Zettaflare's endpoint explosion/effect.
     */
    public Vec3 getBeamEnd() {

        Vec3 start =
                getBeamStart();


        Vec3 direction =
                getBeamDirection(1.0F);


        return start.add(
                direction.scale(BEAM_LENGTH)
        );
    }


    // ============================================================
    // NBT
    // ============================================================

    @Override
    protected void readAdditionalSaveData(
            CompoundTag tag
    ) {

        if (tag.hasUUID("FiringEntity")) {

            firingEntityUUID =
                    tag.getUUID(
                            "FiringEntity"
                    );
        }


        if (tag.hasUUID("Owner")) {

            ownerUUID =
                    tag.getUUID(
                            "Owner"
                    );
        }


        if (tag.contains("Damage")) {

            damage =
                    tag.getFloat(
                            "Damage"
                    );
        }
    }


    @Override
    protected void addAdditionalSaveData(
            CompoundTag tag
    ) {

        if (firingEntityUUID != null) {

            tag.putUUID(
                    "FiringEntity",
                    firingEntityUUID
            );
        }


        if (ownerUUID != null) {

            tag.putUUID(
                    "Owner",
                    ownerUUID
            );
        }


        tag.putFloat(
                "Damage",
                damage
        );
    }
}