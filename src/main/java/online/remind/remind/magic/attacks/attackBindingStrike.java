package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import online.remind.remind.entity.attacks.StrikeElement;

public class attackBindingStrike extends attackElementStrike {

    public attackBindingStrike(ResourceLocation registryName, boolean hasToSelect, int tier, String gmAbility) {
        super(
                registryName,
                hasToSelect,
                tier,
                gmAbility,
                StrikeElement.BINDING,
                SoundEvents.ENCHANTMENT_TABLE_USE
        );
    }
}