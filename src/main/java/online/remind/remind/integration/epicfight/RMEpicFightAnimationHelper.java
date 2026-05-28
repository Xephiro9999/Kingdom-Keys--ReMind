package online.remind.remind.integration.epicfight;

import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKAnimations;
import online.remind.remind.KingdomKeysReMind;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class RMEpicFightAnimationHelper {

    public static void playBlitzAnimation(Player player, int chainStep) {
        if (!KingdomKeysReMind.efmLoaded) {
            return;
        }

        PlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);

        if (playerpatch == null || !playerpatch.isEpicFightMode()) {
            return;
        }

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

    public static boolean isEpicFightMode(Player player) {
        PlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);

        return playerpatch != null && playerpatch.isEpicFightMode();
    }
}