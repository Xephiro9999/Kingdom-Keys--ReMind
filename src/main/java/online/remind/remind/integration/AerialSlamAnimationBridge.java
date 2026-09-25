package online.remind.remind.integration;

import net.minecraft.server.level.ServerPlayer;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.integration.epicfight.EpicFightAerialSlam;

public class AerialSlamAnimationBridge {

    public static void playLaunch(ServerPlayer player) {
        if (!KingdomKeysReMind.efmLoaded) return;
        EpicFightAerialSlam.playLaunch(player);
    }

    public static void playSlam(ServerPlayer player) {
        if (!KingdomKeysReMind.efmLoaded) return;
        EpicFightAerialSlam.playSlam(player);
    }
}