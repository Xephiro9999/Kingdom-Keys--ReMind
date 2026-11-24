package online.remind.remind.finishcommands;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import org.joml.Vector3f;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;

import java.util.List;

/**
 * Executes normal Finish Commands on the logical server.
 * FinishRC owns Situation Gauge consumption / RC removal.
 *
 * The shared LV1 "finish" command varies by chosen starting path:
 * WARRIOR -> Ventus: flurry of fast attacks
 * GUARDIAN -> Terra: powerful jumping attack
 * MYSTIC -> Aqua: rising magical orbs and enemy launch
 */

public final class FinishCommandExecutor {
    private FinishCommandExecutor() {}

    public static void execute(ServerPlayer player, LivingEntity selectedTarget) {
        PlayerData kk = PlayerData.get(player);
        GlobalDataRM data = ModDataRM.getGlobal(player);
        if (kk == null || data == null || !player.isAlive()) return;

        String id = data.getEquippedFinishCommand();
        if (id == null || !FinishCommandRegistry.isRegistered(id)
                || !data.hasFinishCommand(id)) {
            id = "finish";
        }

        float damage = Math.max(1f,
                (kk.getMagic(true) + kk.getStrength(true)) / 2f);


        if (!isImplemented(id)) {
            FinishCommandDefinition command =
                    FinishCommandRegistry.get(id);

            String name = command != null
                    ? command.name()
                    : id;

            player.displayClientMessage(
                    Component.literal(
                            name + " is not implemented yet!"
                    ).withStyle(ChatFormatting.YELLOW),
                    true
            );
        }


        switch (id) {
            case "finish" -> executeLevelOneFinish(player, kk, damage);
            case "rising_rock_1" -> executeRisingRockOne(player, damage);
            case "air_flair_1" -> executeAirFlairOne(player, damage);
            case "magic_pulse_1" -> executeMagicPulseOne(player, damage);
            case "heat_slash_1" -> executeHeatSlashOne(player, kk, damage);
            case "gold_rush" -> executeGoldRush(player, kk, selectedTarget, damage);
            default -> executeLevelOneFinish(player, kk, damage);
        }
    }

    private static void executeLevelOneFinish(
            ServerPlayer player, PlayerData kk, float damage) {

        if (kk.getChosen() == SoAState.WARRIOR) {
            executeVentusFinish(player, damage);
        } else if (kk.getChosen() == SoAState.GUARDIAN) {
            executeTerraFinish(player, damage);
        } else if (kk.getChosen() == SoAState.MYSTIC) {
            executeAquaFinish(player, kk, damage);
        } else {
            radialStrike(player, damage, 3.0, 0.0,
                    ParticleTypes.CRIT, SoundEvents.PLAYER_ATTACK_CRIT);
        }
    }

    /**
     * Ventus: four clearly separated hits on the SAME targets.
     * The starting target selection is locked so knockback/movement can't
     * accidentally make hits 2-4 miss their targets.
     */
    private static void executeVentusFinish(ServerPlayer player, float damage) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        // Capture targets once, BEFORE the animation sequence moves anyone.
        List<LivingEntity> comboTargets = forwardTargets(player, 4.2, -0.3);
        Vec3 direction = horizontalFacing(player);
        player.push(direction.x * 0.12, 0.04, direction.z * 0.12);
        player.hurtMarked = true;

        for (int hit = 0; hit < 4; hit++) {
            final int index = hit;
            schedule(server, index * 4, () -> {
                if (!player.isAlive() || player.isRemoved()) return;

                int connected = 0;
                for (LivingEntity target : comboTargets) {
                    if (!target.isAlive() || target.isRemoved()
                            || target.distanceToSqr(player) > 8.0 * 8.0) continue;

                    // Vanilla's normal 10-tick hurt immunity would collapse
                    // these rapid swings into one registered damage event.
                    target.invulnerableTime = 0;
                    if (target.hurt(target.damageSources().playerAttack(player),
                            damage * (index == 3 ? 0.75f : 0.38f))) {
                        connected++;
                        if (index == 3) pushAway(player, target, 0.65, 0.24);
                    }
                }

                // Alternate the visible sweep from left to right.
                Vec3 forward = horizontalFacing(player);
                Vec3 right = new Vec3(-forward.z, 0, forward.x);
                double side = index % 2 == 0 ? -0.5 : 0.5;
                Vec3 center = player.position().add(forward.scale(1.5))
                        .add(right.scale(side));
                player.serverLevel().sendParticles(ParticleTypes.SWEEP_ATTACK,
                        center.x, center.y + 1.0, center.z,
                        4, 0.45, 0.23, 0.45, 0.01);
                player.serverLevel().sendParticles(ParticleTypes.CRIT,
                        center.x, center.y + 1.0, center.z,
                        index == 3 ? 28 : 12, 0.6, 0.4, 0.6, 0.06);
                play(player, index == 3 ? SoundEvents.PLAYER_ATTACK_STRONG
                        : SoundEvents.PLAYER_ATTACK_SWEEP, 1.30f + index * 0.06f);
            });
        }
    }

    /**
     * Terra: leap, aim toward the initial enemy, then commit to a plunging
     * strike. Actual sword pose needs a client animation (Epic Fight etc.).
     */
    private static void executeTerraFinish(ServerPlayer player, float damage) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        List<LivingEntity> targets = forwardTargets(player, 5.0, -0.3);
        LivingEntity focus = targets.isEmpty() ? null : targets.get(0);
        Vec3 facing = horizontalFacing(player);
        player.setDeltaMovement(facing.x * 0.36, 0.70, facing.z * 0.36);
        player.hurtMarked = true;
        play(player, SoundEvents.PLAYER_ATTACK_SWEEP, 0.78f);

        // Telegraph an aerial descent; prevent the leap from being just an
        // instantaneous AoE or a jump that never follows through.
        schedule(server, 7, () -> {
            if (!player.isAlive() || player.isRemoved()) return;
            Vec3 direction = horizontalFacing(player);
            if (focus != null && focus.isAlive()) {
                Vec3 toward = focus.position().subtract(player.position());
                Vec3 horizontal = new Vec3(toward.x, 0, toward.z);
                if (horizontal.lengthSqr() > 0.001) direction = horizontal.normalize();
            }
            player.setDeltaMovement(direction.x * 0.56, -0.65,
                    direction.z * 0.56);
            player.hurtMarked = true;
        });

        schedule(server, 11, () -> {
            if (!player.isAlive() || player.isRemoved()) return;
            Vec3 facingNow = horizontalFacing(player);
            Vec3 center = player.position().add(facingNow.scale(1.1));

            // Collision volume extends forward, including the intended target.
            List<LivingEntity> strikeTargets = validTargets(player, 4.1);
            if (focus != null && focus.isAlive() &&
                    focus.distanceToSqr(player) <= 6.5 * 6.5 &&
                    !strikeTargets.contains(focus)) strikeTargets.add(focus);

            for (LivingEntity target : strikeTargets) {
                if (target.hurt(target.damageSources().playerAttack(player),
                        damage * 1.65f)) {
                    pushAway(player, target, 0.9, 0.32);
                }
            }
            ServerLevel level = player.serverLevel();
            level.sendParticles(ParticleTypes.EXPLOSION,
                    center.x, player.getY() + 0.6, center.z,
                    3, 0.45, 0.35, 0.45, 0.0);
            level.sendParticles(ParticleTypes.CRIT,
                    center.x, player.getY() + 0.8, center.z,
                    45, 1.0, 0.5, 1.0, 0.16);
            level.sendParticles(ParticleTypes.POOF,
                    center.x, player.getY() + 0.25, center.z,
                    30, 1.0, 0.20, 1.0, 0.06);
            play(player, SoundEvents.PLAYER_ATTACK_STRONG, 0.65f);
        });
    }

    /**
     * Aqua: six large VISIBLE magical orbs rise around the caster over time.
     * Each orb is a small cluster of colored particles, not a lone end rod.
     * At the climax an AoE launches enemies vertically.
     */
    private static void executeAquaFinish(
            ServerPlayer player, PlayerData kk, float damage) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        final int orbCount = 6;
        final int duration = 16;
        for (int tick = 0; tick < duration; tick++) {
            final int frame = tick;
            schedule(server, tick, () -> {
                if (!player.isAlive() || player.isRemoved()) return;
                ServerLevel level = player.serverLevel();
                for (int orb = 0; orb < orbCount; orb++) {
                    double angle = orb * (2.0 * Math.PI / orbCount) + frame * 0.065;
                    double radius = 1.55;
                    double x = player.getX() + Math.cos(angle) * radius;
                    double y = player.getY() + 0.45 + frame * 0.17;
                    double z = player.getZ() + Math.sin(angle) * radius;

                    DustParticleOptions glow = new DustParticleOptions(
                            new Vector3f(0.40f, 0.78f, 1.0f), 1.55f);
                    // Multiple cloud points with a bright core: readable orbs.
                    level.sendParticles(glow, x, y, z,
                            9, 0.14, 0.14, 0.14, 0.0);
                    level.sendParticles(ParticleTypes.END_ROD, x, y, z,
                            2, 0.08, 0.08, 0.08, 0.0);
                }

                if (frame == 7) {
                    for (LivingEntity target : validTargets(player, 4.3)) {
                        if (target.hurt(target.damageSources().playerAttack(player),
                                damage * 1.2f)) {
                            target.setDeltaMovement(target.getDeltaMovement().x,
                                    Math.max(0.95, target.getDeltaMovement().y),
                                    target.getDeltaMovement().z);
                            target.hurtMarked = true;
                        }
                    }
                    level.sendParticles(ParticleTypes.ENCHANT,
                            player.getX(), player.getY() + 1.1, player.getZ(),
                            70, 1.8, 0.8, 1.8, 0.15);
                    play(player, SoundEvents.AMETHYST_BLOCK_CHIME, 1.05f);
                }
            });
        }
    }


    // ---------------------------------------------------------------------
    // LEVEL 2 FINISH COMMANDS
    // ---------------------------------------------------------------------

    /**
     * Rising Rock 1 (BBS): four weapon impacts and a fifth shockwave.
     * Its Level 2 version does NOT summon Rising Rock 2's boulders.
     */
    private static void executeRisingRockOne(ServerPlayer player, float damage) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        List<LivingEntity> lockedTargets = forwardTargets(player, 4.4, -0.45);
        for (int hit = 0; hit < 4; hit++) {
            final int frame = hit;
            schedule(server, frame * 4, () -> {
                if (!canContinue(player)) return;
                for (LivingEntity target : lockedTargets) {
                    if (!isComboTarget(player, target, 7.5)) continue;
                    // Remove vanilla hurt cooldown so all four blows register.
                    target.invulnerableTime = 0;
                    if (target.hurt(target.damageSources().playerAttack(player),
                            damage * 0.40f) && frame == 3) {
                        target.push(0, 0.35, 0);
                        target.hurtMarked = true;
                    }
                }

                if (frame == 2) {
                    Vec3 direction = horizontalFacing(player);
                    player.setDeltaMovement(direction.x * 0.14, 0.33, direction.z * 0.14);
                    player.hurtMarked = true;
                }

                double angle = Math.toRadians(player.getYRot() + frame * 95.0);
                player.serverLevel().sendParticles(ParticleTypes.SWEEP_ATTACK,
                        player.getX() + Math.cos(angle) * 1.0,
                        player.getY() + 0.95, player.getZ() + Math.sin(angle) * 1.0,
                        8, 0.7, 0.35, 0.7, 0.015);
                play(player, SoundEvents.PLAYER_ATTACK_SWEEP, 0.91f + frame * 0.08f);
            });
        }

        schedule(server, 16, () -> {
            if (!canContinue(player)) return;
            for (LivingEntity target : validTargets(player, 4.3)) {
                target.invulnerableTime = 0;
                if (target.hurt(target.damageSources().playerAttack(player), damage * 0.65f)) {
                    pushAway(player, target, 0.55, 0.28);
                    // BBS's 50% Stun chance. Strong Slowness is an interim
                    // approximation until a proper stun effect is connected.
                    chanceStun(player, target, 0.50f);
                }
            }
            ring(player.serverLevel(), player.position(), 3.2,
                    ParticleTypes.CRIT, 36, 0.25);
            player.serverLevel().sendParticles(ParticleTypes.POOF,
                    player.getX(), player.getY() + 0.25, player.getZ(),
                    38, 1.8, 0.2, 1.8, 0.07);
            play(player, SoundEvents.GENERIC_EXPLODE.value(), 0.72f);
        });
    }

    /**
     * Air Flair 1 (BBS): four spinning wind strikes, launching foes.
     * 25% stun chance is represented by a temporary strong Slowness effect.
     */
    private static void executeAirFlairOne(ServerPlayer player, float damage) {
        MinecraftServer server = player.getServer();
        if (server == null) return;
        List<LivingEntity> lockedTargets = validTargets(player, 4.5);

        for (int hit = 0; hit < 4; hit++) {
            final int frame = hit;
            schedule(server, frame * 4, () -> {
                if (!canContinue(player)) return;
                for (LivingEntity target : lockedTargets) {
                    if (!isComboTarget(player, target, 7.5)) continue;
                    target.invulnerableTime = 0;
                    if (!target.hurt(target.damageSources().playerAttack(player),
                            damage * 0.46f)) continue;
                    // Keep opponents airborne while the spinning combo runs.
                    double lift = frame == 3 ? 0.70 : 0.22;
                    target.push(0, lift, 0);
                    target.hurtMarked = true;
                    if (frame == 3) {
                        pushAway(player, target, 0.32, 0.14);
                        chanceStun(player, target, 0.25f);
                    }
                }
                ring(player.serverLevel(), player.position().add(0, 1.0, 0),
                        1.8 + frame * 0.25, ParticleTypes.CLOUD, 24, 0.28);
                player.serverLevel().sendParticles(ParticleTypes.SWEEP_ATTACK,
                        player.getX(), player.getY() + 1.2, player.getZ(),
                        8, 1.5, 0.4, 1.5, 0.01);
                play(player, SoundEvents.PLAYER_ATTACK_SWEEP, 1.25f + frame * 0.07f);
            });
        }
    }

    /**
     * Magic Pulse 1 (BBS): TWO spiraling magical orbs with ONE main hit.
     * Orb positions are re-rendered every server tick for visibility.
     */
    private static void executeMagicPulseOne(ServerPlayer player, float damage) {
        MinecraftServer server = player.getServer();
        if (server == null) return;
        DustParticleOptions blue = new DustParticleOptions(
                new Vector3f(0.32f, 0.75f, 1.0f), 1.7f);
        DustParticleOptions violet = new DustParticleOptions(
                new Vector3f(0.70f, 0.48f, 1.0f), 1.7f);

        for (int tick = 0; tick < 22; tick++) {
            final int frame = tick;
            schedule(server, tick, () -> {
                if (!canContinue(player)) return;
                ServerLevel level = player.serverLevel();
                for (int orb = 0; orb < 2; orb++) {
                    double a = frame * 0.32 + orb * Math.PI;
                    double radius = 1.65;
                    double x = player.getX() + Math.cos(a) * radius;
                    double y = player.getY() + 0.45 + frame * 0.135;
                    double z = player.getZ() + Math.sin(a) * radius;
                    level.sendParticles(orb == 0 ? blue : violet, x, y, z,
                            13, 0.16, 0.16, 0.16, 0.005);
                    level.sendParticles(ParticleTypes.END_ROD, x, y, z,
                            3, 0.07, 0.07, 0.07, 0.002);
                }
                if (frame == 9) {
                    for (LivingEntity target : validTargets(player, 4.2)) {
                        if (!target.hurt(target.damageSources().playerAttack(player),
                                damage * 1.35f)) continue;
                        target.push(0, 0.92, 0);
                        target.hurtMarked = true;
                        if (player.getRandom().nextFloat() < 0.25f) {
                            // Functional stand-in for BBS Confuse: enemies become
                            // slowed/weakened. A real AI-target-switch effect is future work.
                            target.addEffect(new MobEffectInstance(
                                    MobEffects.MOVEMENT_SLOWDOWN, 66, 1));
                            target.addEffect(new MobEffectInstance(
                                    MobEffects.WEAKNESS, 66, 0));
                        }
                    }
                    level.sendParticles(ParticleTypes.ENCHANT,
                            player.getX(), player.getY() + 1.2, player.getZ(),
                            65, 2.2, 0.8, 2.2, 0.14);
                    play(player, SoundEvents.AMETHYST_BLOCK_CHIME, 1.05f);
                }
            });
        }
    }

    /**
     * Heat Slash 1 (BBS): fiery two-phase spin. Ventus's version hits twice;
     * Guardian/Terra and Mystic/Aqua deal their primary hit in the second phase.
     */
    private static void executeHeatSlashOne(
            ServerPlayer player, PlayerData kk, float damage) {
        MinecraftServer server = player.getServer();
        if (server == null) return;
        boolean twoHits = kk.getChosen() == SoAState.WARRIOR;
        List<LivingEntity> lockedTargets = validTargets(player, 4.0);

        for (int phase = 0; phase < 2; phase++) {
            final int frame = phase;
            schedule(server, phase * 7, () -> {
                if (!canContinue(player)) return;
                for (LivingEntity target : lockedTargets) {
                    if (!isComboTarget(player, target, 6.0)) continue;
                    if (frame == 0 && !twoHits) continue;
                    target.invulnerableTime = 0;
                    if (target.hurt(target.damageSources().playerAttack(player),
                            damage * (twoHits ? 0.85f : 1.60f))) {
                        target.setRemainingFireTicks(60);
                        if (frame == 1) pushAway(player, target, 0.48, 0.16);
                    }
                }
                ring(player.serverLevel(), player.position().add(0, 0.95, 0),
                        2.1 + 0.3 * frame, ParticleTypes.FLAME, 32, 0.38);
                player.serverLevel().sendParticles(ParticleTypes.FLAME,
                        player.getX(), player.getY() + 0.9, player.getZ(),
                        34, 1.45, 0.55, 1.45, 0.09);
                play(player, SoundEvents.BLAZE_SHOOT, 1.10f + frame * 0.18f);
            });
        }
    }

    /**
     * Gold Rush (BBS): a single leaping blow; 75% of struck enemies pay out
     * 1-7 munny. Re:Mind credits Kingdom Keys wallet directly (pickup proxy).
     */
    private static void executeGoldRush(
            ServerPlayer player, PlayerData kk,
            LivingEntity selectedTarget, float damage) {
        MinecraftServer server = player.getServer();
        if (server == null) return;
        Vec3 direction = horizontalFacing(player);
        player.setDeltaMovement(direction.x * 0.36, 0.52, direction.z * 0.36);
        player.hurtMarked = true;
        play(player, SoundEvents.PLAYER_ATTACK_SWEEP, 1.20f);

        schedule(server, 8, () -> {
            if (!canContinue(player)) return;
            List<LivingEntity> strikeTargets = forwardTargets(player, 4.2, -0.45);
            if (selectedTarget != null && isComboTarget(player, selectedTarget, 5.0)
                    && !strikeTargets.contains(selectedTarget)
                    && validTargets(player, 5.0).contains(selectedTarget)) {
                strikeTargets = new java.util.ArrayList<>(strikeTargets);
                strikeTargets.add(selectedTarget);
            }
            int reward = 0;
            for (LivingEntity target : strikeTargets) {
                if (!target.hurt(target.damageSources().playerAttack(player),
                        damage * 1.65f)) continue;
                pushAway(player, target, 0.65, 0.22);
                if (player.getRandom().nextFloat() < 0.75f) {
                    reward += 1 + player.getRandom().nextInt(7);
                    player.serverLevel().sendParticles(ParticleTypes.HAPPY_VILLAGER,
                            target.getX(), target.getY() + 1, target.getZ(),
                            18, 0.45, 0.5, 0.45, 0.08);
                }
            }
            // Direct wallet credit deliberately avoids spawning unrelated coin
            // denominations and matches the existing Kingdom Keys munny API.
            if (reward > 0) {
                kk.setMunny(kk.getMunny() + reward);
                PacketHandler.sendTo(new SCSyncPlayerData(player), player);
            }
            ring(player.serverLevel(), player.position(), 2.4,
                    ParticleTypes.CRIT, 28, 0.4);
            play(player, SoundEvents.EXPERIENCE_ORB_PICKUP, 1.35f);
        });
    }

    private static boolean canContinue(ServerPlayer player) {
        return player.isAlive() && !player.isRemoved();
    }

    private static boolean isComboTarget(
            ServerPlayer player, LivingEntity target, double maxDistance) {
        return target != null && target.isAlive() && !target.isRemoved()
                && target.level() == player.level()
                && target.distanceToSqr(player) <= maxDistance * maxDistance;
    }

    private static void chanceStun(
            ServerPlayer player, LivingEntity target, float chance) {
        if (player.getRandom().nextFloat() < chance) {
            target.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN, 66, 5));
        }
    }

    private static void ring(ServerLevel level, Vec3 center, double radius,
                             ParticleOptions particle, int points, double y) {
        for (int i = 0; i < points; i++) {
            double a = i * Math.PI * 2 / points;
            level.sendParticles(particle,
                    center.x + radius * Math.cos(a), center.y + y,
                    center.z + radius * Math.sin(a),
                    2, 0.06, 0.07, 0.06, 0.02);
        }
    }

    private static void schedule(MinecraftServer server, int delay, Runnable action) {
        server.tell(new TickTask(server.getTickCount() + delay, action));
    }

    /** Horizontal facing vector used for forward-only melee attacks. */
    private static Vec3 horizontalFacing(ServerPlayer player) {
        Vec3 look = player.getLookAngle();
        Vec3 horizontal = new Vec3(look.x, 0, look.z);
        if (horizontal.lengthSqr() < 1.0E-6) return new Vec3(0, 0, 1);
        return horizontal.normalize();
    }

    private static List<LivingEntity> forwardTargets(
            ServerPlayer player, double radius, double minimumDot) {
        Vec3 facing = horizontalFacing(player);
        return validTargets(player, radius).stream()
                .filter(target -> {
                    Vec3 delta = target.position().subtract(player.position());
                    Vec3 horizontal = new Vec3(delta.x, 0, delta.z);
                    if (horizontal.lengthSqr() < 0.05) return true;
                    return facing.dot(horizontal.normalize()) >= minimumDot;
                })
                .toList();
    }

    private static void radialStrike(
            ServerPlayer player, float damage, double radius, double knockUp,
            ParticleOptions particle, SoundEvent sound) {

        for (LivingEntity target : validTargets(player, radius)) {
            if (!target.hurt(target.damageSources().playerAttack(player),
                    damage)) continue;
            if (knockUp > 0) {
                target.push(0, knockUp, 0);
                target.hurtMarked = true;
            }
        }

        player.serverLevel().sendParticles(particle,
                player.getX(), player.getY() + 1, player.getZ(),
                55, radius * 0.5, 0.8, radius * 0.5, 0.1);
        play(player, sound, 1f);
    }

    private static void pushAway(
            ServerPlayer player, LivingEntity target,
            double horizontal, double vertical) {
        Vec3 displacement = target.position().subtract(player.position());
        Vec3 direction = new Vec3(displacement.x, 0, displacement.z);
        if (direction.lengthSqr() < 1.0e-6) {
            direction = player.getLookAngle();
        }
        direction = new Vec3(direction.x, 0, direction.z).normalize();
        target.push(direction.x * horizontal, vertical,
                direction.z * horizontal);
        target.hurtMarked = true;
    }

    private static void play(ServerPlayer player, SoundEvent sound, float pitch) {
        player.serverLevel().playSound(null, player.blockPosition(),
                sound, SoundSource.PLAYERS, 1f, pitch);
    }

    private static List<LivingEntity> validTargets(
            ServerPlayer player, double radius) {

        Party party = WorldData.get(player.getServer())
                .getPartyFromMember(player.getUUID());

        return player.level().getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(radius),
                target -> target != player && target.isAlive()
                        && (party == null
                        || party.getMember(target.getUUID()) == null
                        || party.getFriendlyFire())
        );
    }


    public static boolean isImplemented(String id) {
        if (id == null) return false;

        return switch (id) {
            case "finish",
                 "rising_rock_1",
                 "air_flair_1",
                 "magic_pulse_1",
                 "heat_slash_1",
                 "gold_rush" -> true;

            default -> false;
        };
    }

}
