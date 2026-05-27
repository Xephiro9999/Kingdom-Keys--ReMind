package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import online.remind.remind.entity.attacks.StrikeElement;

public class attackLightStrike extends attackElementStrike {

    public attackLightStrike(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
        super(
                registryName,
                hasToSelect,
                maxLevel,
                gmAbility,
                StrikeElement.LIGHT,
                SoundEvents.AMETHYST_BLOCK_CHIME
        );
    }
}