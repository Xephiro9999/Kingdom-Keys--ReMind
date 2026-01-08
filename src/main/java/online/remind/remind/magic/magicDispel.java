package online.remind.remind.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.ArrayList;
import java.util.List;

public class magicDispel extends Magic {

	public magicDispel(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
		super(registryName, hasToSelect, maxLevel, gmAbility);
	}

	@Override
	public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {

		if (lockOnEntity != null) {
			IGlobalDataRM globalData = ModDataRM.getGlobal(lockOnEntity);
			GlobalData globalData2 = GlobalData.get(lockOnEntity);

			// If target is locked and magic lock on ability is on
			List<MobEffectInstance> effectsList = new ArrayList<>();
			for (MobEffectInstance e : lockOnEntity.getActiveEffects()) {
				if (e.getEffect().value().getCategory() == MobEffectCategory.BENEFICIAL) {
					effectsList.add(e);
				}

			}

			for(MobEffectInstance goodEffect: effectsList){
				//TODO take a look at EffectCure and removeEffectsCuredBy
				lockOnEntity.removeEffect(goodEffect.getEffect());
			}
		} else {
			// IDK do some area of effect or something like slow or haste
			float radius = 4;
			List<Entity> list = player.level().getEntities(player, player.getBoundingBox().inflate(radius));
			Party casterParty = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());

			if (casterParty != null && !casterParty.getFriendlyFire()) {
				for (Party.Member m : casterParty.getMembers()) {
					list.remove(player.level().getPlayerByUUID(m.getUUID()));
				}
			}

			int particleCount = 40; // number of particles in the ring

			for (int i = 0; i < particleCount; i++) {
				double angle = 2 * Math.PI * i / particleCount;
				double xOffset = Math.cos(angle) * radius;
				double zOffset = Math.sin(angle) * radius;
				double yOffset = 1.0 + player.getRandom().nextDouble() * 0.5; // slightly above ground, around head

				((ServerLevel) player.level()).sendParticles(ParticleTypes.SCULK_SOUL.getType(), player.getX() + xOffset, player.getY() + yOffset, player.getZ() + zOffset, 0, 0.02, 0,0, 1d);

				((ServerLevel) player.level()).sendParticles(ParticleTypes.SOUL_FIRE_FLAME.getType(), player.getX() + xOffset, player.getY() + yOffset, player.getZ() + zOffset, 0, 0.02, 0,0, 0);

			}

			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Entity e = list.get(i);
					if (e instanceof LivingEntity lEntity) {
						IGlobalDataRM globalData = ModDataRM.getGlobal(lEntity);
						GlobalData globalData2 = GlobalData.get(lEntity);
						lEntity.removeEffect(MobEffects.DAMAGE_BOOST);
						lEntity.removeEffect(MobEffects.MOVEMENT_SPEED);
						lEntity.removeEffect(MobEffects.DAMAGE_RESISTANCE);
						lEntity.removeEffect(MobEffects.FIRE_RESISTANCE);
						lEntity.removeEffect(ModMobEffectsRM.AUTO_LIFE);
						lEntity.removeEffect(ModMobEffectsRM.HASTE_RM);
						lEntity.removeEffect(ModMobEffectsRM.REGEN);
						lEntity.removeEffect(ModMobEffectsRM.BERSERK);
						lEntity.removeEffect(ModMobEffects.AERO);


					}
				}
			}

		}
	}

	@Override
	protected void playMagicCastSound(Player player, Player caster, int level) {
		 player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.DISPEL.get(),
		 SoundSource.PLAYERS, 1F, 1F);
	}
}
