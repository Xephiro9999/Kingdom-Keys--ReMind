package online.remind.remind.integration.epicfight;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import online.remind.remind.effect.ModMobEffectsRM;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class EpicFightAerialSlam {

    public static void playLaunch(ServerPlayer player) {
        ServerPlayerPatch patch = EpicFightCapabilities.getEntityPatch(player, ServerPlayerPatch.class);
        if (patch == null || !patch.isEpicFightMode()) return;

        player.addEffect(new MobEffectInstance(ModMobEffectsRM.RM_ANIMATION_LOCK, 20, 0, false, false, false)); // Prevent native EFM hit damage

        patch.playAnimationSynchronized(Animations.LONGSWORD_AUTO3.get().getRealAnimation(), 0.0F); // Rushing upward slash
    }

    public static void playSlam(ServerPlayer player) {
        ServerPlayerPatch patch = EpicFightCapabilities.getEntityPatch(player, ServerPlayerPatch.class);
        if (patch == null || !patch.isEpicFightMode()) return;

        player.addEffect(new MobEffectInstance(ModMobEffectsRM.RM_ANIMATION_LOCK, 30, 0, false, false, false)); // Cover slam + aerial recovery

        patch.playAnimationSynchronized(Animations.GREATSWORD_DASH.get().getRealAnimation(), 0.0F); // Heavy downward aerial slash
    }
}