package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.entity.attacks.ElementStrikeCollider;
import online.remind.remind.entity.attacks.StrikeElement;

public class attackElementStrike extends Magic {

	private final StrikeElement element;
	private final SoundEvent castSound;

	public attackElementStrike(ResourceLocation registryName, boolean hasToSelect, int tier, ResourceLocation gmAbility, StrikeElement element, SoundEvent castSound) {
		super(registryName, hasToSelect, gmAbility);
	setTier(tier);
		this.element = element;
		this.castSound = castSound;
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		PlayerData playerData = PlayerData.get(caster);

		if (playerData == null) {
			return;
		}

		float dmg = switch (getTier()) {
			case 0 -> playerData.getStrength(true) * 1F;
			case 1 -> playerData.getStrength(true) * 1.15F;
			case 2 -> playerData.getStrength(true) * 1.3F;
			default -> playerData.getStrength(true) * 1F;
		};

		caster.hurtMarked = true;
		caster.fallDistance = 0;


		ElementStrikeCollider collider = new ElementStrikeCollider(player.level(), caster, dmg, element);

		caster.level().addFreshEntity(collider);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		player.level().playSound(null, player.getX(), player.getY(), player.getZ(), castSound, SoundSource.PLAYERS, 1.0F, 1.0F);
	}
}