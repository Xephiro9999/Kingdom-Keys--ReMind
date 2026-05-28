package online.remind.remind.integration;

import net.minecraft.world.entity.player.Player;
import online.remind.remind.KingdomKeysReMind;

public class RMIntegrationHooks {

    public static void playBlitzAnimation(Player player, int chainStep) {
        if (!KingdomKeysReMind.efmLoaded) {
            return;
        }

        online.remind.remind.integration.epicfight.RMEpicFightAnimationHelper.playBlitzAnimation(player, chainStep);
    }

    public static boolean isEpicFightMode(Player player) {
        if (!KingdomKeysReMind.efmLoaded) {
            return false;
        }

        return online.remind.remind.integration.epicfight.RMEpicFightAnimationHelper.isEpicFightMode(player);
    }
}