package online.remind.remind.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class SlowEffect extends MobEffect {
    public SlowEffect(MobEffectCategory pCategory, int pColor){
        super(pCategory, pColor);
    }

    private static final UUID Slow = UUID.fromString("bcbcb87b-3dab-466f-bcbb-0e47e5d691aa");


    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier){
        //amplifier is being used for magic level
        AttributeInstance moveSpeed = pLivingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance attackSpeed = pLivingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        switch(pAmplifier){
            case 0:
                if (moveSpeed != null && moveSpeed.getModifier(Slow) == null) {
                    moveSpeed.addTransientModifier(new AttributeModifier(
                            Slow,
                            "slow",
                            -0.1D,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
                }
                if (attackSpeed != null && attackSpeed.getModifier(Slow) == null) {
                    attackSpeed.addTransientModifier(new AttributeModifier(
                            Slow,
                            "slow",
                            -0.1D,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
                }
                break;
            case 1:
                if (moveSpeed != null && moveSpeed.getModifier(Slow) == null) {
                    moveSpeed.addTransientModifier(new AttributeModifier(
                            Slow,
                            "slow",
                            -0.15D,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
                }
                if (attackSpeed != null && attackSpeed.getModifier(Slow) == null) {
                    attackSpeed.addTransientModifier(new AttributeModifier(
                            Slow,
                            "slow",
                            -0.15D,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
                }
                break;
            case 2:
                if (moveSpeed != null && moveSpeed.getModifier(Slow) == null) {
                    moveSpeed.addTransientModifier(new AttributeModifier(
                            Slow,
                            "slow",
                            -0.2D,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
                }
                if (attackSpeed != null && attackSpeed.getModifier(Slow) == null) {
                    attackSpeed.addTransientModifier(new AttributeModifier(
                            Slow,
                            "slow",
                            -0.2D,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
                }
                break;
        }

        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }


    public Component getDisplayName(MobEffectInstance instance) {
        int amp = instance.getAmplifier();

        switch (amp) {
            case 0:
                return Component.literal("Slow");
            case 1:
                return Component.literal("Slowra");
            case 2:
                return Component.literal("Slowga");
            case 3:
                return Component.literal("Slowja");
            default:
                return Component.literal("Slowza");
        }
    }
}