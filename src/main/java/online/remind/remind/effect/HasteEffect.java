package online.remind.remind.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import online.remind.remind.KingdomKeysReMind;

public class HasteEffect extends MobEffect {

    private static final ResourceLocation HASTE_ATTACK_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "haste_attack_speed");

    private static final ResourceLocation HASTE_MOVEMENT_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "haste_movement_speed");

    public HasteEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void addAttributeModifiers(AttributeMap attributeMap, int amplifier) {
        addModifier(
                attributeMap.getInstance(Attributes.ATTACK_SPEED),
                HASTE_ATTACK_SPEED_ID,
                getHasteAmount(amplifier)
        );

        addModifier(
                attributeMap.getInstance(Attributes.MOVEMENT_SPEED),
                HASTE_MOVEMENT_SPEED_ID,
                getHasteAmount(amplifier)
        );
    }

    @Override
    public void removeAttributeModifiers(AttributeMap attributeMap) {
        removeModifier(attributeMap.getInstance(Attributes.ATTACK_SPEED), HASTE_ATTACK_SPEED_ID);
        removeModifier(attributeMap.getInstance(Attributes.MOVEMENT_SPEED), HASTE_MOVEMENT_SPEED_ID);
    }

    private void addModifier(AttributeInstance instance, ResourceLocation id, double amount) {
        if (instance == null) {
            return;
        }

        instance.removeModifier(id);

        instance.addPermanentModifier(new AttributeModifier(
                id,
                amount,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        ));
    }

    private void removeModifier(AttributeInstance instance, ResourceLocation id) {
        if (instance == null) {
            return;
        }

        instance.removeModifier(id);
    }

    private double getHasteAmount(int amplifier) {
        return switch (Math.max(0, amplifier)) {
            case 0 -> 0.15D; // Haste
            case 1 -> 0.25D; // Hastera
            case 2 -> 0.35D; // Hastega
            case 3 -> 0.45D; // Hasteja
            default -> 0.50D; // Haste++
        };
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return false;
    }

    public Component getDisplayName(MobEffectInstance instance) {
        int amp = instance.getAmplifier();

        return switch (amp) {
            case 0 -> Component.literal("Haste");
            case 1 -> Component.literal("Hastera");
            case 2 -> Component.literal("Hastega");
            case 3 -> Component.literal("Hasteja");
            default -> Component.literal("Haste +");
        };
    }
}