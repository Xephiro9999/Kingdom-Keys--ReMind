package online.remind.remind.integration.epicfight;

import yesman.epicfight.api.animation.AnimationClip;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.ComboAttackAnimation;
import yesman.epicfight.api.animation.types.DirectStaticAnimation;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.main.EpicFightMod;


public class AnimationsReMind {
    public static DirectStaticAnimation EMPTY_ANIMATION = new DirectStaticAnimation() {
        @Override
        public void loadAnimation() {
        }

        @Override
        public AnimationClip getAnimationClip() {
            return AnimationClip.EMPTY_CLIP;
        }
    };

    public static AnimationManager.AnimationAccessor<ComboAttackAnimation> ZANTETSUKEN;

    public static void build(AnimationManager.AnimationBuilder builder) {
        ZANTETSUKEN = builder.nextAccessor("biped/combat/tool_auto1", (accessor) ->
                new ComboAttackAnimation(0.13F, 0.05F, 0.15F, 0.3F, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED)
                        .setResourceLocation(EpicFightMod.MODID, "biped/combat/sword_auto1")
        );
    }
}
