package online.remind.remind.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import online.remind.remind.KingdomKeysReMind;

import java.util.UUID;

public class HasteEffect extends MobEffect {
    public HasteEffect(MobEffectCategory pCategory, int pColor){
        super(pCategory, pColor);
    }



    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier){
        //amplifier is being used for magic level
        switch(pAmplifier){
            case 0:
                this.addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "haste_rm"), 0.10D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "haste_rm"), 0.10D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                break;
            case 1:
                this.addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "haste_rm"), 0.15D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "haste_rm"), 0.15D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                break;
            case 2:
                this.addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "haste_rm"), 0.20D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "haste_rm"), 0.20D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                break;
        }



        return super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }


    public Component getDisplayName(MobEffectInstance instance) {
        int amp = instance.getAmplifier();

        switch (amp) {
            case 0:
                return Component.literal("Haste");
            case 1:
                return Component.literal("Hastera");
            case 2:
                return Component.literal("Hastega");
            case 3:
                return Component.literal("Hasteja");
            default:
                return Component.literal("Haste++");
        }
    }
}
