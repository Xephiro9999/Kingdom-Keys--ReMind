package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import online.remind.remind.entity.attacks.StrikeElement;

public class attackDarkStrike extends attackElementStrike {

	public attackDarkStrike(ResourceLocation registryName, boolean hasToSelect, int tier, ResourceLocation gmAbility) {
		super(registryName, hasToSelect, tier, gmAbility, StrikeElement.DARK, SoundEvents.WITHER_SHOOT);
	}
}