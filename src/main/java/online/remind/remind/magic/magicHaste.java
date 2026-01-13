package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;

import java.util.List;

public class magicHaste extends Magic {

	public magicHaste(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
		super(registryName, hasToSelect, maxLevel, gmAbility);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {
		IGlobalDataRM globalData = ModDataRM.getGlobal(player);
		WorldData worldData = WorldData.get(player.getServer());
		if (globalData != null) {
			int time = (int) (PlayerData.get(caster).getMaxMP() * ((level * 0.75) + 5) + 5);
			caster.swing(InteractionHand.MAIN_HAND);
			caster.addEffect(new MobEffectInstance(ModMobEffectsRM.HASTE_RM, time, level, false, false, false));

			// Hastega Effect
			if (level == 2) {
				if (worldData.getPartyFromMember(player.getUUID()) != null) {
					Party party = worldData.getPartyFromMember(player.getUUID());
					List<Party.Member> list = party.getMembers();
					if (!list.isEmpty()) { // Haste everyone in the party within reach
						for (int i = 0; i < list.size(); i++) {
							if (player.level().getPlayerByUUID(list.get(i).getUUID()) != null && player.distanceTo(player.level().getPlayerByUUID(list.get(i).getUUID())) < ModConfigs.SERVER.partyRangeLimit.get()) {
								LivingEntity e = player.level().getPlayerByUUID(list.get(i).getUUID());
								if (e != null && Utils.isEntityInParty(party, e) && e != player) {
									e.addEffect(new MobEffectInstance(ModMobEffectsRM.HASTE_RM, time, level, false, false, false));
								}
							}
						}
					}
				}
			}
		}
	}


	@Override
	protected void playMagicCastSound(LivingEntity player, Player caster, int level) {
		player.level().playSound(null, player.blockPosition(), ModSoundsRM.HASTE.get(), SoundSource.PLAYERS, 1F, 1F);
	}

}