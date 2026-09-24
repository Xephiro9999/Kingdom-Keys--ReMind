package online.remind.remind.integration;

import net.minecraft.server.level.ServerPlayer;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.integration.epicfight.EpicFightArsArcanum;

public class ArsArcanumAnimationBridge {

    public static void play(ServerPlayer player, int hit) {
        if (!KingdomKeysReMind.efmLoaded) {
            return;
        }

        EpicFightArsArcanum.play(player, hit);
    }
}