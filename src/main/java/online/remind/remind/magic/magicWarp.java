package online.remind.remind.magic;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.client.sound.ModSoundsRM;

public class magicWarp extends Magic {
	public magicWarp(ResourceLocation registryName, boolean hasToSelect, int tier, ResourceLocation gmAbility) {
		super(registryName, hasToSelect, gmAbility);
setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnTarget) {
		caster.sendSystemMessage(Component.literal("This magic has now been ported to Kingdom Keys, unequip it to get the new spell."));
	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSoundsRM.PLAYER_CAST.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
