package online.remind.remind.finishcommands;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;

import java.util.List;

/** Server-side combat dispatcher for normal (non-Style) Finish Commands. */
public final class FinishCommandExecutor {
    private FinishCommandExecutor() {}

    /**
     * Executes one normal Finish Command. The caller (FinishRC) is responsible
     * for consuming the Situation Gauge and removing the Reaction Command.
     *
     * Currently implements the basic Finish and first-tier attack prototypes.
     * Other registered IDs intentionally use the basic Finish fallback until
     * their bespoke animation / timing / damage behavior is implemented.
     */
    public static void execute(ServerPlayer player, LivingEntity selectedTarget) {
        PlayerData kk = PlayerData.get(player);
        GlobalDataRM data = ModDataRM.getGlobal(player);
        if (kk == null || data == null) return;

        String id = data.getEquippedFinishCommand();
        if (id == null || !FinishCommandRegistry.isRegistered(id) || !data.hasFinishCommand(id)) {
            id = "finish";
        }

        float damage = Math.max(1f, (kk.getMagic(true) + kk.getStrength(true)) / 2f);
        switch (id) {
            case "rising_rock_1" -> radialStrike(player, damage * 1.10f, 3.0,
                    0.45, ParticleTypes.CRIT, SoundEvents.PLAYER_ATTACK_SWEEP);
            case "air_flair_1" -> radialStrike(player, damage, 3.5,
                    0.65, ParticleTypes.CLOUD, SoundEvents.PLAYER_ATTACK_SWEEP);
            case "magic_pulse_1" -> radialStrike(player, damage * 0.95f, 4.0,
                    0.12, ParticleTypes.ENCHANT, SoundEvents.AMETHYST_BLOCK_CHIME);
            case "heat_slash_1" -> radialStrike(player, damage * 1.10f, 3.0,
                    0.12, ParticleTypes.FLAME, SoundEvents.BLAZE_SHOOT);
            case "gold_rush" -> radialStrike(player, damage, 3.0,
                    0.20, ParticleTypes.CRIT, SoundEvents.EXPERIENCE_ORB_PICKUP);
            default -> basicFinish(player, kk, damage);
        }
    }

    /** Retains the original FinishRC path choice effects. */
    private static void basicFinish(ServerPlayer player, PlayerData kk, float damage) {
        List<LivingEntity> targets = validTargets(player, 3.0);
        for (LivingEntity target : targets) {
            if (!target.hurt(target.damageSources().playerAttack(player), damage)) continue;
            if (kk.getChosen() == SoAState.WARRIOR) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, 0, false, false, true));
            } else if (kk.getChosen() == SoAState.GUARDIAN) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120, 0, false, false, true));
                player.heal(damage * 0.15f);
            } else if (kk.getChosen() == SoAState.MYSTIC) {
                kk.addMP(damage * 0.15f);
            }
        }
        ServerLevel level = player.serverLevel();
        Vec3 pos = player.position().add(0, 1, 0);
        for (int i = 0; i < 16; i++) {
            double angle = i * (Math.PI * 2 / 16);
            level.sendParticles(ParticleTypes.CRIT,
                    pos.x + 2.0 * Math.cos(angle), pos.y,
                    pos.z + 2.0 * Math.sin(angle), 3,
                    0.10, 0.15, 0.10, 0.02);
        }
        level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT,
                SoundSource.PLAYERS, 1f, 1f);
    }

    /** Prototype attack variants: server-side damage, knock-up, sound, particles. */
    private static void radialStrike(ServerPlayer player, float damage, double radius,
                                     double knockUp,
                                     net.minecraft.core.particles.ParticleOptions particle,
                                     net.minecraft.sounds.SoundEvent sound) {
        for (LivingEntity target : validTargets(player, radius)) {
            if (!target.hurt(target.damageSources().playerAttack(player), damage)) continue;
            if (knockUp > 0) {
                target.push(0, knockUp, 0);
                target.hurtMarked = true;
            }
        }
        ServerLevel level = player.serverLevel();
        level.sendParticles(particle, player.getX(), player.getY() + 1,
                player.getZ(), 55, radius * 0.5, 0.8, radius * 0.5, 0.1);
        level.playSound(null, player.blockPosition(), sound, SoundSource.PLAYERS, 1f, 1f);
    }

    private static List<LivingEntity> validTargets(ServerPlayer player, double radius) {
        Party party = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());
        return player.level().getEntitiesOfClass(LivingEntity.class,
                player.getBoundingBox().inflate(radius), target ->
                        target != player && target.isAlive() &&
                                (party == null || party.getMember(target.getUUID()) == null || party.getFriendlyFire()));
    }
}
