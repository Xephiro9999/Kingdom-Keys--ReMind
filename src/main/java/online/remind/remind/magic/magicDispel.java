package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
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

	public magicDispel(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
		super(registryName, hasToSelect, maxLevel, null);
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
			float radius = 16;
			List<Entity> list = player.level().getEntities(player, player.getBoundingBox().inflate(radius));
			Party casterParty = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());

			if (casterParty != null && !casterParty.getFriendlyFire()) {
				for (Party.Member m : casterParty.getMembers()) {
					list.remove(player.level().getPlayerByUUID(m.getUUID()));
				}
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
