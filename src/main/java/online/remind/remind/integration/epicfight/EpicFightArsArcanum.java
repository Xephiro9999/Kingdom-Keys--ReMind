package online.remind.remind.integration.epicfight;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import online.remind.remind.effect.ModMobEffectsRM;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class EpicFightArsArcanum {

    public static void play(ServerPlayer player, int hit) {
        ServerPlayerPatch patch =
                EpicFightCapabilities.getEntityPatch(
                        player,
                        ServerPlayerPatch.class
                );

        if (patch == null || !patch.isEpicFightMode()) {
            return;
        }

        player.addEffect(new MobEffectInstance(
                ModMobEffectsRM.RM_ANIMATION_LOCK,
                8,
                0,
                false,
                false,
                false
        ));

        switch (hit) {

            // ARCANUM
            case 1 -> patch.playAnimationSynchronized(
                    Animations.SWORD_DASH.get().getRealAnimation(),
                    0.0F
            );

            case 2 -> patch.playAnimationSynchronized(
                    Animations.SWORD_AUTO1.get().getRealAnimation(),
                    0.0F
            );

            case 3 -> patch.playAnimationSynchronized(
                    Animations.TACHI_AUTO1.get().getRealAnimation(),
                    0.0F
            );

            case 4 -> patch.playAnimationSynchronized(
                    Animations.SWORD_AUTO2.get().getRealAnimation(),
                    0.0F
            );

            case 5 -> patch.playAnimationSynchronized(
                    Animations.TACHI_AUTO2.get().getRealAnimation(),
                    0.0F
            );

            case 6 -> patch.playAnimationSynchronized(
                    Animations.LONGSWORD_AUTO1.get().getRealAnimation(),
                    0.0F
            );

            case 7 -> patch.playAnimationSynchronized(
                    Animations.SWORD_AUTO3.get().getRealAnimation(),
                    0.0F
            );


            // BASH
            case 8 -> patch.playAnimationSynchronized(
                    Animations.TACHI_AUTO1.get().getRealAnimation(),
                    0.0F
            );

            case 9 -> patch.playAnimationSynchronized(
                    Animations.LONGSWORD_AUTO2.get().getRealAnimation(),
                    0.0F
            );

            case 10 -> patch.playAnimationSynchronized(
                    Animations.SWORD_AUTO1.get().getRealAnimation(),
                    0.0F
            );

            case 11 -> patch.playAnimationSynchronized(
                    Animations.TACHI_AUTO2.get().getRealAnimation(),
                    0.0F
            );

            case 12 -> patch.playAnimationSynchronized(
                    Animations.LONGSWORD_AUTO3.get().getRealAnimation(),
                    0.0F
            );


            // FINISHER
            case 13 -> patch.playAnimationSynchronized(
                    Animations.GREATSWORD_AIR_SLASH.get().getRealAnimation(),
                    0.0F
            );
        }
    }
}