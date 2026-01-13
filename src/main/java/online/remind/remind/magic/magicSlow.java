package online.remind.remind.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;

import java.util.List;

public class magicSlow extends Magic {
	public magicSlow(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
		super(registryName, hasToSelect, maxLevel, null);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {

		float radius = 3 + (level);
		List<Entity> list = player.level().getEntities(player, player.getBoundingBox().inflate(radius, radius, radius));
		Party casterParty = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());

		if (casterParty != null && !casterParty.getFriendlyFire()) {
			for (Member m : casterParty.getMembers()) {
				list.remove(player.level().getPlayerByUUID(m.getUUID()));
			}
		}

		int particleCount = 40; // number of particles in the ring

		for (int i = 0; i < particleCount; i++) {
			double angle = 2 * Math.PI * i / particleCount;
			double xOffset = Math.cos(angle) * radius;
			double zOffset = Math.sin(angle) * radius;
			double yOffset = 1.0 + player.getRandom().nextDouble() * 0.5; // slightly above ground, around head

			((ServerLevel) player.level()).sendParticles(ParticleTypes.SOUL.getType(), player.getX() + xOffset, player.getY() + yOffset, player.getZ() + zOffset, 0, 0.02, 0,0, 1d);

			((ServerLevel) player.level()).sendParticles(ParticleTypes.EFFECT.getType(), player.getX() + xOffset, player.getY() + yOffset, player.getZ() + zOffset, 0, 0.02, 0,0, 0);

		}

		int time = (int) (PlayerData.get(caster).getMaxMP() * ((level * 0.75) + 5) + 5);
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Entity e = (Entity) list.get(i);
				if (e instanceof LivingEntity lEntity) {
					IGlobalDataRM globalData = ModDataRM.getGlobal(lEntity);
					if (globalData != null) {
						// lEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,time,
						// level + 1));
						// lEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN,time, level +
						// 1));
						lEntity.addEffect(new MobEffectInstance(ModMobEffectsRM.SLOW_RM, time, level, false, false, false));
					}
				}
			}
			player.swing(InteractionHand.MAIN_HAND);


		}
	}

	@Override
	protected void playMagicCastSound(LivingEntity player, Player caster, int level) {
		player.level().playSound(null, player.blockPosition(), ModSoundsRM.SLOW.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
