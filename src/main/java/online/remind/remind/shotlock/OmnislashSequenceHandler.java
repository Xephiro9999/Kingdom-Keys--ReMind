package online.remind.remind.shotlock;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.integration.epicfight.EpicFightOmnislash;

import java.util.*;

@EventBusSubscriber(modid = KingdomKeysReMind.MODID)
public final class OmnislashSequenceHandler {

    private static final Map<UUID, OmnislashState> ACTIVE =
            new HashMap<>();

    private static final Set<UUID> APPLYING_DAMAGE =
            new HashSet<>();

    private static final int NORMAL_HITS = 14;

    private static final float NORMAL_DAMAGE_MULTIPLIER =
            0.30F;

    private static final float FINISHER_DAMAGE_MULTIPLIER =
            1.25F;

    private static final int[] NORMAL_HIT_TICKS = {
            // tap tap tap
            10, 15, 20,

            // tap tap tap
            30, 35, 40,

            // tap tap tap
            50, 55, 60,

            // tap tap tap
            70, 75, 80,

            // tap tap
            90, 95
    };

    private static final int FINISHER_SETUP_TICK =
            105;

    private static final int FINISHER_HIT_TICK =
            120;

    private static final int END_TICK =
            134;   // 1.2 sec recovery

    private OmnislashSequenceHandler() {
    }


    // ============================================================
    // START
    // ============================================================

    public static void start(
            ServerPlayer player,
            LivingEntity target,
            float baseDamage
    ) {
        if (player == null
                || target == null
                || !target.isAlive()) {

            return;
        }

        UUID playerId =
                player.getUUID();

        /*
         * Don't start another Omnislash while one is already active.
         */
        if (ACTIVE.containsKey(playerId)) {
            return;
        }

        ACTIVE.put(
                playerId,
                new OmnislashState(
                        target.getUUID(),
                        Math.max(0.0F, baseDamage)
                )
        );

        /*
         * Your existing EntityEventsRM already prevents normal attacks while
         * RM_ANIMATION_LOCK is active.
         */
        player.addEffect(
                new MobEffectInstance(
                        ModMobEffectsRM.RM_ANIMATION_LOCK,
                        END_TICK + 10,
                        0,
                        false,
                        false,
                        false
                )
        );

        player.setDeltaMovement(
                Vec3.ZERO
        );

        player.fallDistance =
                0.0F;
    }


    // ============================================================
    // TICK
    // ============================================================

    @SubscribeEvent
    public static void onPlayerTick(
            PlayerTickEvent.Post event
    ) {
        if (!(event.getEntity()
                instanceof ServerPlayer player)) {

            return;
        }

        UUID playerId =
                player.getUUID();

        OmnislashState state =
                ACTIVE.get(playerId);

        if (state == null) {
            return;
        }

        if (!player.isAlive()) {
            cancel(player);
            return;
        }

        ServerLevel level =
                player.serverLevel();

        Entity targetEntity =
                level.getEntity(
                        state.targetId
                );

        if (!(targetEntity
                instanceof LivingEntity target)
                || !target.isAlive()) {

            cancel(player);
            return;
        }

        state.tick++;

        player.fallDistance =
                0.0F;

        player.setDeltaMovement(
                Vec3.ZERO
        );


        // --------------------------------------------------------
        // HITS 1 - 14
        // --------------------------------------------------------

        if (state.hit < NORMAL_HIT_TICKS.length) {

            int expectedTick =
                    NORMAL_HIT_TICKS[state.hit];

            if (state.tick >= expectedTick) {

                performNormalSlash(
                        player,
                        target,
                        state
                );

                state.hit++;

                return;
            }
        }


        // --------------------------------------------------------
        // FINISHER SETUP
        // --------------------------------------------------------

        if (!state.finisherPrepared
                && state.tick >= FINISHER_SETUP_TICK) {

            state.finisherPrepared =
                    true;

            prepareFinisher(
                    player,
                    target
            );

            return;
        }


        // --------------------------------------------------------
        // FINAL HIT
        // --------------------------------------------------------

        if (!state.finisherHit
                && state.tick >= FINISHER_HIT_TICK) {

            state.finisherHit =
                    true;

            performFinisher(
                    player,
                    target,
                    state
            );

            return;
        }


        // --------------------------------------------------------
        // END
        // --------------------------------------------------------

        if (state.tick >= END_TICK) {

            finish(
                    player,
                    target
            );
        }
    }


    // ============================================================
    // NORMAL SLASH
    // ============================================================

    private static void performNormalSlash(
            ServerPlayer player,
            LivingEntity target,
            OmnislashState state
    ) {

        int hit =
                state.hit;

        /*
         * Golden-angle rotation prevents Omnislash from just bouncing
         * front/back/front/back around the enemy.
         */
        double angle =
                Math.toRadians(
                        hit * 137.5D
                );

        double radius =
                1.65D;

        double verticalOffset =
                switch (hit % 4) {

                    case 0 -> 0.15D;

                    case 1 -> Math.min(
                            target.getBbHeight() * 0.40D,
                            1.1D
                    );

                    case 2 -> Math.min(
                            target.getBbHeight() * 0.20D,
                            0.6D
                    );

                    default -> Math.min(
                            target.getBbHeight() * 0.55D,
                            1.4D
                    );
                };


        Vec3 destination =
                target.position()
                        .add(
                                Math.cos(angle)
                                        * radius,
                                verticalOffset,
                                Math.sin(angle)
                                        * radius
                        );


        warpPlayer(
                player,
                target,
                destination
        );

        EpicFightOmnislash.playSlash(
                player,
                hit
        );


        float damage =
                state.baseDamage
                        * NORMAL_DAMAGE_MULTIPLIER;


        damageTarget(
                player,
                target,
                damage
        );


        slashEffects(
                player,
                target,
                hit
        );

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.CRITSLASH.get(), SoundSource.PLAYERS, 1F, 1F);

    }


    // ============================================================
    // FINISHER SETUP
    // ============================================================

    private static void prepareFinisher(
            ServerPlayer player,
            LivingEntity target
    ) {

        Vec3 direction =
                player.position()
                        .subtract(
                                target.position()
                        );

        if (direction.lengthSqr()
                < 0.001D) {

            direction =
                    target
                            .getLookAngle()
                            .scale(-1.0D);
        }

        direction =
                new Vec3(
                        direction.x,
                        0.0D,
                        direction.z
                );

        if (direction.lengthSqr()
                < 0.001D) {

            direction =
                    new Vec3(
                            0.0D,
                            0.0D,
                            -1.0D
                    );
        }

        direction =
                direction.normalize();


        Vec3 destination =
                target.position()
                        .add(
                                direction.scale(
                                        0.75D
                                )
                        )
                        .add(
                                0.0D,
                                target.getBbHeight()
                                        + 2.3D,
                                0.0D
                        );


        warpPlayer(
                player,
                target,
                destination
        );


        ServerLevel level =
                player.serverLevel();


        level.sendParticles(
                ParticleTypes.END_ROD,
                player.getX(),
                player.getY()
                        + 0.8D,
                player.getZ(),
                12,
                0.35D,
                0.6D,
                0.35D,
                0.03D
        );
    }


    // ============================================================
    // FINAL HIT
    // ============================================================

    private static void performFinisher(
            ServerPlayer player,
            LivingEntity target,
            OmnislashState state
    ) {

        player.lookAt(
                EntityAnchorArgument.Anchor.EYES,
                target.getEyePosition()
        );

        EpicFightOmnislash.playFinisher(
                player
        );


        float damage =
                state.baseDamage
                        * FINISHER_DAMAGE_MULTIPLIER;


        damageTarget(
                player,
                target,
                damage
        );


        ServerLevel level =
                player.serverLevel();


        double x =
                target.getX();

        double y =
                target.getY()
                        + target.getBbHeight()
                        * 0.5D;

        double z =
                target.getZ();


        level.sendParticles(
                ParticleTypes.SWEEP_ATTACK,
                x,
                y,
                z,
                5,
                1.0D,
                0.8D,
                1.0D,
                0.0D
        );


        level.sendParticles(
                ParticleTypes.CRIT,
                x,
                y,
                z,
                30,
                0.9D,
                0.9D,
                0.9D,
                0.25D
        );


        level.sendParticles(
                ParticleTypes.END_ROD,
                x,
                y,
                z,
                20,
                0.75D,
                0.75D,
                0.75D,
                0.10D
        );


        level.playSound(
                null,
                target.blockPosition(),
                SoundEvents.PLAYER_ATTACK_CRIT,
                SoundSource.PLAYERS,
                1.5F,
                0.7F
        );


        level.playSound(
                null,
                target.blockPosition(),
                SoundEvents.PLAYER_ATTACK_STRONG,
                SoundSource.PLAYERS,
                1.5F,
                0.65F
        );
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.CRITSLASH.get(), SoundSource.PLAYERS, 1F, 1F);

    }


    // ============================================================
    // DAMAGE
    // ============================================================

    private static void damageTarget(
            ServerPlayer player,
            LivingEntity target,
            float damage
    ) {
        if (!target.isAlive()
                || damage <= 0.0F) {

            return;
        }

        /*
         * Omnislash handles its own hit timing.
         */
        target.invulnerableTime = 0;
        target.hurtTime = 0;

        UUID playerId =
                player.getUUID();

        /*
         * Open the Omnislash damage window.
         */
        APPLYING_DAMAGE.add(
                playerId
        );

        try {

            boolean damaged =
                    target.hurt(
                            player
                                    .damageSources()
                                    .playerAttack(player),
                            damage
                    );

            System.out.println(
                    "[Omnislash] damage="
                            + damage
                            + " accepted="
                            + damaged
                            + " targetHP="
                            + target.getHealth()
            );

        } finally {

            /*
             * Immediately close it again so EFM/normal melee
             * can't sneak damage into the sequence.
             */
            APPLYING_DAMAGE.remove(
                    playerId
            );
        }
    }


    // ============================================================
    // WARP
    // ============================================================

    private static void warpPlayer(
            ServerPlayer player,
            LivingEntity target,
            Vec3 destination
    ) {

        player.teleportTo(
                destination.x,
                destination.y,
                destination.z
        );

        player.setDeltaMovement(
                Vec3.ZERO
        );

        player.fallDistance =
                0.0F;

        player.lookAt(
                EntityAnchorArgument.Anchor.EYES,
                target.getEyePosition()
        );
    }


    // ============================================================
    // SLASH FX
    // ============================================================

    private static void slashEffects(
            ServerPlayer player,
            LivingEntity target,
            int hit
    ) {

        ServerLevel level =
                player.serverLevel();


        double x =
                target.getX();

        double y =
                target.getY()
                        + target.getBbHeight()
                        * 0.5D;

        double z =
                target.getZ();


        level.sendParticles(
                ParticleTypes.SWEEP_ATTACK,
                x,
                y,
                z,
                1,
                0.3D,
                0.35D,
                0.3D,
                0.0D
        );


        level.sendParticles(
                ParticleTypes.CRIT,
                x,
                y,
                z,
                6,
                0.4D,
                0.4D,
                0.4D,
                0.12D
        );


        float pitch =
                0.85F
                        + (
                        (hit % 4)
                                * 0.08F
                );


        level.playSound(
                null,
                target.blockPosition(),
                SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.PLAYERS,
                0.85F,
                pitch
        );
    }


    // ============================================================
    // FINISH / CANCEL
    // ============================================================

    private static void finish(
            ServerPlayer player,
            LivingEntity target
    ) {

        Vec3 away =
                player.position()
                        .subtract(
                                target.position()
                        );

        away =
                new Vec3(
                        away.x,
                        0.0D,
                        away.z
                );


        if (away.lengthSqr()
                < 0.001D) {

            away =
                    target
                            .getLookAngle()
                            .scale(-1.0D);

            away =
                    new Vec3(
                            away.x,
                            0.0D,
                            away.z
                    );
        }


        if (away.lengthSqr()
                < 0.001D) {

            away =
                    new Vec3(
                            0.0D,
                            0.0D,
                            -1.0D
                    );
        }


        away =
                away.normalize();


        Vec3 landing =
                target.position()
                        .add(
                                away.scale(
                                        1.8D
                                )
                        );


        player.teleportTo(
                landing.x,
                target.getY(),
                landing.z
        );


        player.setDeltaMovement(
                Vec3.ZERO
        );

        player.fallDistance =
                0.0F;

        player.lookAt(
                EntityAnchorArgument.Anchor.EYES,
                target.getEyePosition()
        );


        ACTIVE.remove(
                player.getUUID()
        );


        player.removeEffect(
                ModMobEffectsRM.RM_ANIMATION_LOCK
        );
    }


    private static void cancel(
            ServerPlayer player
    ) {

        ACTIVE.remove(
                player.getUUID()
        );

        player.setDeltaMovement(
                Vec3.ZERO
        );

        player.fallDistance =
                0.0F;

        player.removeEffect(
                ModMobEffectsRM.RM_ANIMATION_LOCK
        );
    }


    // ============================================================
    // STATE
    // ============================================================

    private static final class OmnislashState {

        private final UUID targetId;

        private final float baseDamage;

        private int tick =
                0;

        private int hit =
                0;

        private boolean finisherPrepared =
                false;

        private boolean finisherHit =
                false;


        private OmnislashState(
                UUID targetId,
                float strength
        ) {
            this.targetId =
                    targetId;

            this.baseDamage =
                    strength;
        }
    }

    public static boolean isApplyingDamage(Player player) {
        return player != null
                && APPLYING_DAMAGE.contains(player.getUUID());
    }

}