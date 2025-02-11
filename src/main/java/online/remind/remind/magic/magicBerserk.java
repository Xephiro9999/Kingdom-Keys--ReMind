package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
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
			// Effect and Level Modifier
			PlayerData playerData = PlayerData.get(player);
            double strBonus = (playerData.getStrengthStat().getStat() * 0.15F) * (level + 1);
			double defDebuff = (playerData.getDefenseStat().getStat() * 0.15F) * (level + 1);
			System.out.println(strBonus);
            if (globalData.getBerserkTicks() <= 0) {

				// Future color change line below
				// Levels 0 - 2
				switch (level) {
				case 0, 1, 2:
					playerData.getStrengthStat().addModifier("berserk", strBonus, false, false);
					playerData.getDefenseStat().addModifier("berserk", -defDebuff, false, false);
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
					break;
				}
				globalData.setBerserkTicks(time, level);
				PacketHandlerRM.syncGlobalToAllAround(player, globalData);

			}
		}
	}

	@Override
	protected void playMagicCastSound(Player player, Player caster, int level) {
		player.level().playSound(null, player.blockPosition(), ModSoundsRM.BERSERK.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}