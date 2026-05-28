package online.remind.remind.integration.epicfight;

import net.minecraft.world.entity.player.Player;
import online.remind.remind.KingdomKeysReMind;

public class RMIntegrationHooks {

    public static boolean isEpicFightMode(Player player) {
        if (!KingdomKeysReMind.efmLoaded) {
            return false;
        }

        return online.remind.remind.integration.epicfight.RMEpicFightAnimationHelper.isEpicFightMode(player);
    }

    public static void playHeavyCommandAnimation(Player player, String commandId, int chainStep) {
        if (!KingdomKeysReMind.efmLoaded) {
            return;
        }

        online.remind.remind.integration.epicfight.RMEpicFightAnimationHelper.playHeavyCommandAnimation(
                player,
                commandId,
                chainStep
        );
    }
}