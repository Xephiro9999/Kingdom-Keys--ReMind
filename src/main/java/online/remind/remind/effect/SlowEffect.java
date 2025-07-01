package online.remind.remind.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import online.remind.remind.KingdomKeysReMind;

import java.util.UUID;

public class SlowEffect extends MobEffect {
    public SlowEffect(MobEffectCategory pCategory, int pColor){
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier){
        //amplifier is being used for magic level
        switch(pAmplifier){
            case 0:
                this.addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "slow_rm"), -0.10D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "slow_rm"), -0.10D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                break;
            case 1:
                this.addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "slow_rm"), -0.15D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "slow_rm"), -0.15D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                break;
            case 2:
                this.addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "slow_rm"), -0.20D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "slow_rm"), -0.20D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                break;
        }



        return super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
