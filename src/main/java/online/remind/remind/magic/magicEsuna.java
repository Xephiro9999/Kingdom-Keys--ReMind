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
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.ArrayList;
import java.util.List;

public class magicEsuna extends Magic {

	public magicEsuna(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
		super(registryName, hasToSelect, maxLevel, null);
	}

	@Override
	public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {
		IGlobalCapabilitiesRM globalData = ModCapabilitiesRM.getGlobal(player);
		IGlobalCapabilities globalData2 = ModCapabilities.getGlobal(player);

		if (globalData != null) {
			caster.swing(InteractionHand.MAIN_HAND);
			((ServerLevel) player.level()).sendParticles(ParticleTypes.SONIC_BOOM.getType(), player.getX(), player.getY() + 2.3D, player.getZ(), 5, 0D, 0D, 0D, 0D);

			List<MobEffectInstance> effectsList = new ArrayList<>();
			for (MobEffectInstance e : player.getActiveEffects()) {
				if (e.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
					effectsList.add(e);
				}
			}

			for(MobEffectInstance badEffect: effectsList){
				player.removeEffect(badEffect.getEffect());
			}


			PacketHandler.syncToAllAround(player, globalData2);
			PacketHandlerRM.syncGlobalToAllAround(player, globalData);

		}
	}

	@Override
	protected void playMagicCastSound(Player player, Player caster, int level) {
		player.level().playSound(null, player.blockPosition(), ModSoundsRM.ESUNA.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
