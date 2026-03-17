package online.remind.remind.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.ArrayList;
import java.util.List;

public class magicEsuna extends Magic {

	public magicEsuna(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
		super(registryName, hasToSelect, maxLevel, gmAbility);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {
		IGlobalDataRM globalData = ModDataRM.getGlobal(player);
		WorldData worldData = WorldData.get(player.getServer());
		GlobalData globalData2 = GlobalData.get(player);

		if (globalData != null) {
			caster.swing(InteractionHand.MAIN_HAND);
			((ServerLevel) player.level()).sendParticles(ParticleTypes.SONIC_BOOM.getType(), player.getX(), player.getY() + 2.3D, player.getZ(), 5, 0D, 0D, 0D, 0D);

			List<MobEffectInstance> effectsList = new ArrayList<>();
			for (MobEffectInstance e : player.getActiveEffects()) {
				if (e.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
					effectsList.add(e);
				}
			}

			for(MobEffectInstance badEffect: effectsList){
				//TODO take a look at EffectCure and removeEffectsCuredBy
				player.removeEffect(badEffect.getEffect());
			}

			if (level == 1){ // Group Esuna
				if (worldData.getPartyFromMember(player.getUUID()) != null) {
					Party party = worldData.getPartyFromMember(player.getUUID());
					List<Party.Member> list = party.getMembers();
					if (!list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							if (player.level().getPlayerByUUID(list.get(i).getUUID()) != null && player.distanceTo(player.level().getPlayerByUUID(list.get(i).getUUID())) < ModConfigs.SERVER.partyRangeLimit.get()) {
								LivingEntity e = player.level().getPlayerByUUID(list.get(i).getUUID());
								if (e != null && Utils.isEntityInParty(party, e) && e != player) {
									List<MobEffectInstance> effectsListG = new ArrayList<>();
									for (MobEffectInstance eG : e.getActiveEffects()) {
										if (eG.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
											effectsList.add(eG);
										}
									}

									for(MobEffectInstance badEffect: effectsList){
										//TODO take a look at EffectCure and removeEffectsCuredBy
										e.removeEffect(badEffect.getEffect());
									}
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
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSoundsRM.ESUNA.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
