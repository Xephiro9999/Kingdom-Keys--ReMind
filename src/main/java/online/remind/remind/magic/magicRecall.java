package online.remind.remind.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.sound.ModSoundsRM;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        bus = EventBusSubscriber.Bus.GAME
)
public class magicRecall extends Magic {

    private static final int RECALL_CAST_TICKS = 14;
    private static final Map<UUID, Integer> ACTIVE_RECALLS =
            new HashMap<>();


    public magicRecall(
            ResourceLocation registryName,
            boolean hasToSelect,
            int tier,
            ResourceLocation gmAbility
    ) {
        super(
                registryName,
                hasToSelect,
                gmAbility
        );

        setTier(tier);
    }


    // ============================================================
    // SPELL USE
    // ============================================================

    @Override
    public void magicUse(
            LivingEntity player,
            Player caster,
            float fullMPBlastMult,
            LivingEntity lockOnTarget
    ) {

        if (!(caster instanceof ServerPlayer serverPlayer)) {
            return;
        }


        if (ACTIVE_RECALLS.containsKey(serverPlayer.getUUID())) {
            return;
        }

        ACTIVE_RECALLS.put(
                serverPlayer.getUUID(),
                0
        );

        spawnRecallStartEffect(serverPlayer);
    }


    // ============================================================
    // RECALL TICK
    // ============================================================

    @SubscribeEvent
    public static void onServerTick(
            ServerTickEvent.Post event
    ) {

        if (ACTIVE_RECALLS.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<UUID, Integer>> iterator =
                ACTIVE_RECALLS.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry<UUID, Integer> entry =
                    iterator.next();

            ServerPlayer player =
                    event.getServer()
                            .getPlayerList()
                            .getPlayer(entry.getKey());


            if (player == null
                    || player.isRemoved()
                    || !player.isAlive()) {

                iterator.remove();
                continue;
            }

            int tick =
                    entry.getValue() + 1;

            entry.setValue(tick);

            spawnRecallTrail(
                    player,
                    tick
            );

            if (tick >= RECALL_CAST_TICKS) {

                performRecall(player);

                iterator.remove();
            }
        }
    }


    // ============================================================
    // INITIAL EFFECT
    // ============================================================

    private static void spawnRecallStartEffect(
            ServerPlayer player
    ) {

        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        double x =
                player.getX();

        double y =
                player.getY() + 0.1D;

        double z =
                player.getZ();

        /*
         * Small flash around the feet.
         */
        level.sendParticles(
                ParticleTypes.END_ROD,
                x,
                y + 0.2D,
                z,
                18,
                0.45D,
                0.08D,
                0.45D,
                0.02D
        );

        level.sendParticles(
                ParticleTypes.ENCHANT,
                x,
                y + 0.15D,
                z,
                30,
                0.75D,
                0.10D,
                0.75D,
                0.3D
        );

        spawnHorizontalRing(
                level,
                x,
                y + 0.05D,
                z,
                1.15D,
                24,
                false
        );
    }


    // ============================================================
    // SPIRAL TRAILS
    // ============================================================

    private static void spawnRecallTrail(
            ServerPlayer player,
            int tick
    ) {

        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        double progress =
                (double) tick
                        / RECALL_CAST_TICKS;


        double trailHeight =
                0.15D
                        + (
                        progress
                                * (
                                player.getBbHeight()
                                        + 1.1D
                        )
                );


        double radius =
                1.05D
                        - (
                        progress
                                * 0.55D
                );


        double baseAngle =
                tick * 0.85D;

        /*
         * Three separate light strands.
         */
        for (int strand = 0; strand < 3; strand++) {

            double angle =
                    baseAngle
                            + (
                            strand
                                    * (
                                    Math.PI
                                            * 2.0D
                                            / 3.0D
                            )
                    );

            double x =
                    player.getX()
                            + Math.cos(angle)
                            * radius;

            double y =
                    player.getY()
                            + trailHeight;

            double z =
                    player.getZ()
                            + Math.sin(angle)
                            * radius;

            level.sendParticles(
                    ParticleTypes.END_ROD,
                    x,
                    y,
                    z,
                    1,
                    0.0D,
                    0.0D,
                    0.0D,
                    0.0D
            );

            /*
             * Slightly smaller secondary strand behind the main one.
             */
            double innerAngle =
                    angle - 0.30D;

            double innerRadius =
                    radius * 0.65D;

            level.sendParticles(
                    ParticleTypes.ENCHANT,
                    player.getX()
                            + Math.cos(innerAngle)
                            * innerRadius,
                    y - 0.15D,
                    player.getZ()
                            + Math.sin(innerAngle)
                            * innerRadius,
                    1,
                    0.0D,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }

        /*
         * Add rising particles through the player's center.
         */
        if (tick % 2 == 0) {

            level.sendParticles(
                    ParticleTypes.END_ROD,
                    player.getX(),
                    player.getY()
                            + progress
                            * player.getBbHeight(),
                    player.getZ(),
                    3,
                    0.18D,
                    0.10D,
                    0.18D,
                    0.015D
            );
        }

        /*
         * Growing rings at key points during the cast.
         */
        if (tick == 4
                || tick == 8
                || tick == 11) {

            double ringHeight =
                    player.getY()
                            + (
                            progress
                                    * player.getBbHeight()
                    );

            spawnHorizontalRing(
                    level,
                    player.getX(),
                    ringHeight,
                    player.getZ(),
                    radius + 0.25D,
                    20,
                    false
            );
        }
    }


    // ============================================================
    // TELEPORT
    // ============================================================

    private static void performRecall(
            ServerPlayer player
    ) {

        if (!(player.level() instanceof ServerLevel departureLevel)) {
            return;
        }

        double oldX =
                player.getX();

        double oldY =
                player.getY();

        double oldZ =
                player.getZ();

        spawnDepartureEffect(
                departureLevel,
                oldX,
                oldY,
                oldZ,
                player.getBbHeight()
        );

        DimensionTransition respawn =
                player.findRespawnPositionAndUseSpawnBlock(
                        false,
                        DimensionTransition.DO_NOTHING
                );

        Vec3 destination =
                respawn.pos();

        player.teleportTo(
                respawn.newLevel(),
                destination.x,
                destination.y,
                destination.z,
                respawn.yRot(),
                respawn.xRot()
        );

        player.setDeltaMovement(
                Vec3.ZERO
        );

        player.fallDistance =
                0.0F;

        spawnArrivalEffect(
                respawn.newLevel(),
                player
        );
    }


    // ============================================================
    // DEPARTURE EFFECT
    // ============================================================

    private static void spawnDepartureEffect(
            ServerLevel level,
            double x,
            double y,
            double z,
            double playerHeight
    ) {

        /*
         * Final vertical "pull upward" burst.
         */
        for (int i = 0; i < 28; i++) {

            double angle =
                    (
                            Math.PI
                                    * 2.0D
                                    * i
                    )
                            / 28.0D;

            double radius =
                    0.25D
                            + (
                            i % 4
                    )
                            * 0.18D;

            double height =
                    (
                            (double) i
                                    / 28.0D
                    )
                            * (
                            playerHeight
                                    + 1.5D
                    );

            level.sendParticles(
                    ParticleTypes.END_ROD,
                    x
                            + Math.cos(angle)
                            * radius,
                    y + height,
                    z
                            + Math.sin(angle)
                            * radius,
                    1,
                    0.0D,
                    0.04D,
                    0.0D,
                    0.01D
            );
        }

        level.sendParticles(
                ParticleTypes.ENCHANT,
                x,
                y + playerHeight * 0.5D,
                z,
                45,
                0.75D,
                playerHeight * 0.45D,
                0.75D,
                0.55D
        );

        level.sendParticles(
                ParticleTypes.END_ROD,
                x,
                y + playerHeight * 0.5D,
                z,
                28,
                0.5D,
                playerHeight * 0.5D,
                0.5D,
                0.08D
        );

        spawnHorizontalRing(
                level,
                x,
                y + 0.1D,
                z,
                1.4D,
                30,
                true
        );
    }


    // ============================================================
    // ARRIVAL EFFECT
    // ============================================================

    private static void spawnArrivalEffect(
            ServerLevel level,
            ServerPlayer player
    ) {

        double x =
                player.getX();

        double y =
                player.getY();

        double z =
                player.getZ();

        /*
         * Three expanding rings around the destination.
         */
        spawnHorizontalRing(
                level,
                x,
                y + 0.05D,
                z,
                0.65D,
                18,
                true
        );

        spawnHorizontalRing(
                level,
                x,
                y + 0.08D,
                z,
                1.15D,
                24,
                true
        );

        spawnHorizontalRing(
                level,
                x,
                y + 0.12D,
                z,
                1.65D,
                30,
                false
        );

        /*
         * Light rises around the player after materialization.
         */
        level.sendParticles(
                ParticleTypes.END_ROD,
                x,
                y + 0.9D,
                z,
                32,
                0.65D,
                0.9D,
                0.65D,
                0.06D
        );

        level.sendParticles(
                ParticleTypes.ENCHANT,
                x,
                y + 0.8D,
                z,
                45,
                0.85D,
                0.7D,
                0.85D,
                0.5D
        );

        /*
         * Arrival sound.
         */
        level.playSound(
                null,
                x,
                y,
                z,
                ModSoundsRM.PLAYER_CAST.get(),
                SoundSource.PLAYERS,
                0.75F,
                1.35F
        );
    }


    // ============================================================
    // PARTICLE RING
    // ============================================================

    private static void spawnHorizontalRing(
            ServerLevel level,
            double centerX,
            double centerY,
            double centerZ,
            double radius,
            int particles,
            boolean endRod
    ) {

        for (int i = 0; i < particles; i++) {

            double angle =
                    (
                            Math.PI
                                    * 2.0D
                                    * i
                    )
                            / particles;

            double x =
                    centerX
                            + Math.cos(angle)
                            * radius;

            double z =
                    centerZ
                            + Math.sin(angle)
                            * radius;

            level.sendParticles(
                    endRod
                            ? ParticleTypes.END_ROD
                            : ParticleTypes.ENCHANT,
                    x,
                    centerY,
                    z,
                    1,
                    0.0D,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }


    // ============================================================
    // CAST SOUND
    // ============================================================

    @Override
    public void playMagicCastSound(
            LivingEntity player,
            Player caster
    ) {

        player.level().playSound(
                null,
                player.position().x(),
                player.position().y(),
                player.position().z(),
                ModSoundsRM.PLAYER_CAST.get(),
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
    }
}