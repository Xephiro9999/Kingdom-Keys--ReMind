package online.remind.remind.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;

import java.util.List;

public class magicConfuse extends Magic {
	public magicConfuse(ResourceLocation registryName, boolean hasToSelect, int tier, ResourceLocation gmAbility) {
		super(registryName, hasToSelect, null);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnTarget) {

		float radius = 3 + (getTier());
		List<Entity> list = player.level().getEntities(player, player.getBoundingBox().inflate(radius, radius, radius));
		Party casterParty = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());

		if (casterParty != null && !casterParty.getFriendlyFire()) {
			for (Member m : casterParty.getMembers()) {
				list.remove(player.level().getPlayerByUUID(m.getUUID()));
			}
		}
		int time = 0;
		switch (getTier()){
			case 0:
				time = 100;

			case 1:
				time = 120;

			case 2:
				time = 140;

		}
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Entity e = (Entity) list.get(i);
				if (e instanceof LivingEntity lEntity) {
					lEntity.addEffect(new MobEffectInstance(ModMobEffectsRM.CONFUSE, time, getTier(), false, false, false));
				}
			}
			player.swing(InteractionHand.MAIN_HAND);
			// Add particles below here.

			int particleCount = 40; // number of particles in the ring

			for (int i = 0; i < particleCount; i++) {
				double angle = 2 * Math.PI * i / particleCount;
				double xOffset = Math.cos(angle) * radius;
				double zOffset = Math.sin(angle) * radius;
				double yOffset = 1.0 + player.getRandom().nextDouble() * 0.5; // slightly above ground, around head

				// Pink particle
				((ServerLevel) player.level()).sendParticles(ParticleTypes.CHERRY_LEAVES.getType(), player.getX() + xOffset, player.getY() + yOffset, player.getZ() + zOffset, 0, 0.02, 0,0, 1d);

				// White particle
					((ServerLevel) player.level()).sendParticles(ParticleTypes.EFFECT.getType(), player.getX() + xOffset, player.getY() + yOffset, player.getZ() + zOffset, 0, 0.02, 0,0, 0);

			}
		}
	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		player.level().playSound(null, player.blockPosition(), ModSoundsRM.CONFUSE.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
