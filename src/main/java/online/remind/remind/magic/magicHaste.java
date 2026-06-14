package online.remind.remind.magic;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;

import java.util.List;

public class magicHaste extends Magic {

	public magicHaste(ResourceLocation registryName, boolean hasToSelect, int tier, String gmAbility) {
		super(registryName, hasToSelect, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnTarget) {
		GlobalDataRM globalData = ModDataRM.getGlobal(player);
		WorldData worldData = WorldData.get(player.getServer());
		

		if (globalData != null) {
			int time = (int) (PlayerData.get(caster).getMaxMP() * ((getTier() * 0.75) + 5) + 5);
			caster.swing(InteractionHand.MAIN_HAND);
			caster.addEffect(new MobEffectInstance(ModMobEffectsRM.HASTE_RM, time, getTier(), false, false, false));

			// Hastega Effect
			if (getTier() == 2) {
				if (worldData.getPartyFromMember(player.getUUID()) != null) {
					Party party = worldData.getPartyFromMember(player.getUUID());
					List<Party.Member> list = party.getMembers();
					if (!list.isEmpty()) { // Haste everyone in the party within reach
						for (Party.Member member : list) {
							if (player.level().getPlayerByUUID(member.getUUID()) != null && player.distanceTo(player.level().getPlayerByUUID(member.getUUID())) < ModConfigs.SERVER.partyRangeLimit.get()) {
								LivingEntity e = player.level().getPlayerByUUID(member.getUUID());
								if (e != null && Utils.isEntityInParty(party, e) && e != player) {
									e.addEffect(new MobEffectInstance(ModMobEffectsRM.HASTE_RM, time, getTier(), false, false, false));
								}
							}
						}
					}
				}
			}
		}
	}


	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		player.level().playSound(null, player.blockPosition(), ModSoundsRM.HASTE.get(), SoundSource.PLAYERS, 1F, 1F);
	}

}