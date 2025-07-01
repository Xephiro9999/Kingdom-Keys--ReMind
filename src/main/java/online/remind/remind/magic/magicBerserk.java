package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.network.PacketHandlerRM;

public class magicBerserk extends Magic {

	public magicBerserk(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
		super(registryName, hasToSelect, maxLevel, null);
	}

	@Override
	public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {

		IGlobalDataRM globalData = ModDataRM.getGlobal(player);

		if (globalData != null) {
			int time = (int) (PlayerData.get(caster).getMaxMP() * ((level * 0.75) + 5));
			caster.swing(InteractionHand.MAIN_HAND);
			player.addEffect(new MobEffectInstance(ModMobEffectsRM.BERSERK, time, level, false, false, false));
		}
	}

	@Override
	protected void playMagicCastSound(Player player, Player caster, int level) {
		player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.BERSERK.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}