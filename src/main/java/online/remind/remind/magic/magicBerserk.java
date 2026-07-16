package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;

public class magicBerserk extends Magic {

	public magicBerserk(ResourceLocation registryName, boolean hasToSelect, int tier, ResourceLocation gmAbility) {
		super(registryName, hasToSelect, gmAbility);
setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnTarget) {

		GlobalDataRM globalData = ModDataRM.getGlobal(player);

		if (globalData != null) {
			int time = (int) (PlayerData.get(caster).getMaxMP() * ((getTier() * 0.75) + 5));
			caster.swing(InteractionHand.MAIN_HAND);
			player.addEffect(new MobEffectInstance(ModMobEffectsRM.BERSERK, time, getTier(), false, false, false));
		}
	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.BERSERK.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}