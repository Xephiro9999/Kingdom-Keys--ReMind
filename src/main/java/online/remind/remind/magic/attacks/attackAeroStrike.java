package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import online.remind.remind.entity.attacks.StrikeElement;

public class attackAeroStrike extends attackElementStrike {

	public attackAeroStrike(ResourceLocation registryName, boolean hasToSelect, int tier, String gmAbility) {
		super(registryName, hasToSelect, tier, gmAbility, StrikeElement.AERO, SoundEvents.BREEZE_SHOOT);
	}
}