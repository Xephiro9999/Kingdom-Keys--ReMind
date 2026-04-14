package online.remind.remind.reactioncommands;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.List;

public class FinishRC extends ReactionCommand {

	public FinishRC(ResourceLocation registryName, boolean constantCheck) {
		super(registryName, constantCheck, 30 * 20);
	}

	@Override
	public void onUse(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
		if (conditionsToAppear(player, player)) {
			PlayerData playerData = PlayerData.get(player);
			GlobalDataRM  remindData = ModDataRM.getGlobal(player);

			double X = player.getX();
			double Y = player.getY();
			double Z = player.getZ();

			// Finisher Attack Code Below
			float damage = (float) (playerData.getMagic(true) + playerData.getStrength(true)) / 2; // AVG of STR + MAG
			//float dmgMult = playerData.getNumberOfAbilitiesEquipped(Strings.fireBoost) * 0.25f;
			//damage += (damage * dmgMult);

			Level level = player.level();
			ServerLevel serverLevel = (ServerLevel) player.level();

			double radius = 3;
			List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(radius));

			for (LivingEntity target : targets){
				if (target != player){
					Party p = null;
					if (player != null) {
						p = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());
					}

					if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) {
						//getOwner().sendSystemMessage(Component.literal("Entity: " + target));

						if (playerData.getChosen() == SoAState.WARRIOR) {
							target.hurt(target.damageSources().playerAttack(player), damage);
							player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,120,0,false,false,true));
						}
						if (playerData.getChosen() == SoAState.GUARDIAN) {
							target.hurt(target.damageSources().playerAttack(player), damage);
							player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,120,0,false,false,true));
							player.heal(damage * 0.15f);
						}
						if (playerData.getChosen() == SoAState.MYSTIC) {
							target.hurt(target.damageSources().playerAttack(player), damage);
							System.out.println(damage);
							playerData.addMP(damage * 0.15f);
						}
						target.invulnerableTime = 0;
					}
				}
			}

			for (int t = 1; t < 360; t += 20) {
				for (int s = 1; s < 360 ; s += 20) {
					double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double y = Y + (radius * Math.cos(Math.toRadians(t)));

					serverLevel.sendParticles(ParticleTypes.CRIT, x,y,z,2,0.05,0.05,0.05,0.01);

				}
			}

			level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1F, 1F);

			// Drain Gauge
			remindData.setStyle("NONE");
			remindData.setSituationValue(0);
			remindData.clearSituationSpells();
			playerData.removeReactionCommand(getRegistryName().toString());
			PacketHandlerRM.syncGlobalToAllAround(player, remindData);
		}
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
		return true;
	}
}
