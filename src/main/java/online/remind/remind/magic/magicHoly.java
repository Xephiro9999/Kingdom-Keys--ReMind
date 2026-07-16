package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.ability.ModAbilitiesRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.magic.HolyEntity;
import online.remind.remind.lib.StringsRM;

public class magicHoly extends Magic {

	public magicHoly(ResourceLocation registryName, boolean hasToSelect, int tier, ResourceLocation gmAbility) {
		super(registryName, hasToSelect, gmAbility);
setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnTarget) {

		// IGlobalCapabilitiesMA globalData = ModCapabilitiesMA.getGlobal(player);
		float dmgMult = getDamageMult() + PlayerData.get(caster).getNumberOfAbilitiesEquipped(ModAbilitiesRM.LIGHT_BOOST) * 0.2F;
		dmgMult *= fullMPBlastMult;

		switch (getTier()) {
		case 0:
			for (int i = -1; i <= 1; i++) {
				HolyEntity holy = new HolyEntity(player.level(), caster, i, dmgMult);
				holy.setCaster(player.getDisplayName().getString());
				player.level().addFreshEntity(holy);
			}

			break;
		case 1:
			for (int i = -2; i <= 2; i++) {
				HolyEntity holy = new HolyEntity(player.level(), caster, i, dmgMult);
				holy.setCaster(player.getDisplayName().getString());
				player.level().addFreshEntity(holy);
			}

			break;
		case 2:
			for (int i = -3; i <= 3; i++) {
				HolyEntity holy = new HolyEntity(player.level(), caster, i, dmgMult);
				holy.setCaster(player.getDisplayName().getString());
				player.level().addFreshEntity(holy);
			}

			break;
		}

	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		player.level().playSound(null, player.blockPosition(), ModSoundsRM.HOLY.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
