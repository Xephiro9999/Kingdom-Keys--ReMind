package online.remind.remind.integration.epicfight;

import net.minecraft.server.level.ServerPlayer;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKAnimations;
import online.remind.remind.KingdomKeysReMind;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public final class EpicFightOmnislash {

    private EpicFightOmnislash() {
    }


    // ============================================================
    // NORMAL OMNISLASH HITS
    // ============================================================

    public static void playSlash(
            ServerPlayer player,
            int hitIndex
    ) {
        if (player == null
                || !KingdomKeysReMind.efmLoaded) {

            return;
        }

        ServerPlayerPatch patch =
                EpicFightCapabilities.getEntityPatch(
                        player,
                        ServerPlayerPatch.class
                );

        if (patch == null
                || !patch.isEpicFightMode()) {

            return;
        }

        /*
         * Omnislash rhythm:
         *
         * slash
         * slash
         * accented slash
         *
         * repeat
         *
         * Because we're already teleporting around the enemy,
         * even repeating SWORD_AUTO3 will visually come from
         * different directions.
         */
        int beat =
                hitIndex % 3;

        switch (beat) {

            // First tap
            case 0 -> patch.playAnimationSynchronized(
                    Animations.SWORD_AUTO3
                            .get()
                            .getRealAnimation(),
                    0.0F
            );

            // Second tap
            case 1 -> patch.playAnimationSynchronized(
                    Animations.SWORD_AUTO3
                            .get()
                            .getRealAnimation(),
                    0.0F
            );

            // Third / accented tap
            case 2 -> patch.playAnimationSynchronized(
                    KKAnimations.SORA_FINISHER1
                            .get()
                            .getRealAnimation(),
                    0.05F
            );
        }
    }


    // ============================================================
    // FINAL HIT
    // ============================================================

    public static void playFinisher(
            ServerPlayer player
    ) {
        if (player == null
                || !KingdomKeysReMind.efmLoaded) {

            return;
        }

        ServerPlayerPatch patch =
                EpicFightCapabilities.getEntityPatch(
                        player,
                        ServerPlayerPatch.class
                );

        if (patch == null
                || !patch.isEpicFightMode()) {

            return;
        }

        /*
         * For now use the known Kingdom Keys finisher animation.
         *
         * Later we can replace ONLY this with a proper custom
         * Omnislash downward-finisher animation.
         */
        patch.playAnimationSynchronized(
                KKAnimations.SORA_FINISHER1
                        .get()
                        .getRealAnimation(),
                0.0F
        );
    }
}