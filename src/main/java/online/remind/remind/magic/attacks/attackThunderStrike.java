package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import online.remind.remind.entity.attacks.StrikeElement;

public class attackThunderStrike extends attackElementStrike {

    public attackThunderStrike(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
        super(
                registryName,
                hasToSelect,
                maxLevel,
                gmAbility,
                StrikeElement.THUNDER,
                SoundEvents.TRIDENT_THUNDER.value()
        );
    }
}