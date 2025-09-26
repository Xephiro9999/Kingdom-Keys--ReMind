package online.remind.remind.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class HasteEffect extends MobEffect {
    public HasteEffect(MobEffectCategory pCategory, int pColor){
        super(pCategory, pColor);
    }

    private static final UUID HasteSpell = UUID.fromString("4db7efef-9886-49a8-b10c-ff83b8b97be2");



    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier){
        //amplifier is being used for magic level
        AttributeInstance moveSpeed = pLivingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance attackSpeed = pLivingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        switch(pAmplifier){
            case 0:
                if (moveSpeed != null && moveSpeed.getModifier(HasteSpell) == null) {
                    moveSpeed.addTransientModifier(new AttributeModifier(
                            HasteSpell,
                            "haste_speed_boost",
                            0.1D,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
                }
                if (attackSpeed != null && attackSpeed.getModifier(HasteSpell) == null) {
                    attackSpeed.addTransientModifier(new AttributeModifier(
                            HasteSpell,
                            "haste_speed_boost",
                            0.1D,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
                }
                break;
            case 1:
                if (moveSpeed != null && moveSpeed.getModifier(HasteSpell) == null) {
                    moveSpeed.addTransientModifier(new AttributeModifier(
                            HasteSpell,
                            "haste_speed_boost",
                            0.15D,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
                }
                if (attackSpeed != null && attackSpeed.getModifier(HasteSpell) == null) {
                    attackSpeed.addTransientModifier(new AttributeModifier(
                            HasteSpell,
                            "haste_speed_boost",
                            0.15D,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
                }
                break;
            case 2:
                if (moveSpeed != null && moveSpeed.getModifier(HasteSpell) == null) {
                    moveSpeed.addTransientModifier(new AttributeModifier(
                            HasteSpell,
                            "haste_speed_boost",
                            0.2D,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
                }
                if (attackSpeed != null && attackSpeed.getModifier(HasteSpell) == null) {
                    attackSpeed.addTransientModifier(new AttributeModifier(
                            HasteSpell,
                            "haste_speed_boost",
                            0.2D,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
                }
                break;
        }

        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        if (event.getEffectInstance().getEffect() == ModMobEffectsRM.HASTE_RM.get()) {
            LivingEntity entity = event.getEntity();
            AttributeInstance speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
            AttributeInstance speedA = entity.getAttribute(Attributes.ATTACK_SPEED);
            if (speedA != null && speedA.getModifier(HasteSpell) != null) {
                speedA.removeModifier(HasteSpell);
            }
            if (speed != null && speed.getModifier(HasteSpell) != null) {
                speed.removeModifier(HasteSpell);
            }
        }
    }





//    @Override
//    public void applyEffectTick(int pAmplifier, LivingEntity pLivingEntity){
//        //amplifier is being used for magic level
//
//        switch(pAmplifier){
//            case 0:
//                pLivingEntity.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier("haste_1", 0.10f, AttributeModifier.Operation.MULTIPLY_TOTAL));
//                pLivingEntity.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier("haste_1", 0.10f, AttributeModifier.Operation.MULTIPLY_TOTAL));
//                break;
//            case 1:
//                pLivingEntity.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier("haste_2", 0.15f, AttributeModifier.Operation.MULTIPLY_TOTAL));
//                pLivingEntity.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier( "haste_2", 0.15f, AttributeModifier.Operation.MULTIPLY_TOTAL));
//                break;
//            case 2:
//                pLivingEntity.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier( "haste_3", 0.20f, AttributeModifier.Operation.MULTIPLY_TOTAL));
//                pLivingEntity.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier("haste_3", 0.20f, AttributeModifier.Operation.MULTIPLY_TOTAL));
//                break;
//        }
//
//
//
//    }


    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
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
                return Component.literal("Hasteza");
        }
    }
}