package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
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

import java.util.List;

public class magicSlow extends Magic {
	public magicSlow(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
		super(registryName, hasToSelect, maxLevel, null);
	}

	@Override
	public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {

		float radius = 2 + (level);
		List<Entity> list = player.level().getEntities(player, player.getBoundingBox().inflate(radius, radius, radius));
		Party casterParty = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());

		if (casterParty != null && !casterParty.getFriendlyFire()) {
			for (Member m : casterParty.getMembers()) {
				list.remove(player.level().getPlayerByUUID(m.getUUID()));
			}
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
						lEntity.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "Slow"), -(0.25 + (0.25 * globalData.getSlowLevel())), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
						lEntity.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "Slow"), -(0.25 + (0.25 * globalData.getSlowLevel())), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

						globalData.setSlowTicks(time, level); // Slow Time
						globalData.setSlowCaster(player.getDisplayName().getString());
					}
				}
			}
			player.swing(InteractionHand.MAIN_HAND);
		}
	}

	@Override
	protected void playMagicCastSound(Player player, Player caster, int level) {
		player.level().playSound(null, player.blockPosition(), ModSoundsRM.SLOW.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
