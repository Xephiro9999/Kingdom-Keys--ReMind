package online.remind.remind.integration.epicfight;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKAnimations;
import online.remind.remind.effect.ModMobEffectsRM;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class RMEpicFightAnimationHelper {

    public static boolean isEpicFightMode(Player player) {
        PlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);

        return playerpatch != null && playerpatch.isEpicFightMode();
    }

    public static void playHeavyCommandAnimation(Player player, String commandId, int chainStep) {
        PlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);

        if (playerpatch == null || !playerpatch.isEpicFightMode()) {
            return;
        }

        player.addEffect(new MobEffectInstance(
                ModMobEffectsRM.RM_ANIMATION_LOCK,
                12,
                0,
                false,
                false,
                false
        ));

        switch (commandId) {
            case "blitz" -> playBlitz(playerpatch, chainStep);
            case "slot_edge" -> playSlotEdge(playerpatch, chainStep);
            case "sonic_blade" -> playSonicBlade(playerpatch, chainStep);
            case "chaos_blade" -> playChaosBlade(playerpatch, chainStep);
            case "dark_haze" -> playDarkHaze(playerpatch, chainStep);
        }
    }

    private static void playBlitz(PlayerPatch playerpatch, int chainStep) {
        float transition = switch (chainStep) {
            case 1 -> 0.08F;
            case 2 -> 0.05F;
            default -> 0.1F;
        };

        playerpatch.playAnimationSynchronized(
                KKAnimations.SORA_FINISHER1.get().getRealAnimation(),
                transition
        );
    }

    private static void playSlotEdge(PlayerPatch playerpatch, int chainStep) {
        float transition = switch (chainStep) {
            case 1 -> 0.08F;
            case 2 -> 0.05F;
            default -> 0.1F;
        };

        playerpatch.playAnimationSynchronized(
                KKAnimations.SORA_FINISHER1.get().getRealAnimation(),
                transition
        );
    }

    private static void playSonicBlade(PlayerPatch playerpatch, int chainStep) {
        float transition = switch (chainStep) {
            case 1, 2, 3, 4 -> 0.04F;
            default -> 0.06F;
        };

        playerpatch.playAnimationSynchronized(
                Animations.SWORD_DASH.get().getRealAnimation(),
                transition
        );
    }

    private static void playChaosBlade(PlayerPatch playerpatch, int chainStep) {
        float transition = switch (chainStep) {
            case 3 -> 0.03F;
            default -> 0.06F;
        };

        playerpatch.playAnimationSynchronized(
                Animations.SWORD_DASH.get().getRealAnimation(),
                transition
        );
    }

    private static void playDarkHaze(PlayerPatch playerpatch, int chainStep) {
        playerpatch.playAnimationSynchronized(
                Animations.SWORD_DASH.get().getRealAnimation(),
                0.08F
        );
    }
}