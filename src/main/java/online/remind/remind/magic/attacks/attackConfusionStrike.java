package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import online.remind.remind.entity.attacks.StrikeElement;

public class attackConfusionStrike extends attackElementStrike {

    public attackConfusionStrike(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
        super(
                registryName,
                hasToSelect,
                maxLevel,
                gmAbility,
                StrikeElement.CONFUSION,
                SoundEvents.ILLUSIONER_CAST_SPELL
        );
    }
}