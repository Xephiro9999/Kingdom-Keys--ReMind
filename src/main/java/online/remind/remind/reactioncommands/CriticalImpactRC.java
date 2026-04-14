package online.remind.remind.reactioncommands;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.entity.reactioncommand.CounterRushCore;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import org.joml.Vector3f;

import java.util.List;

public class CriticalImpactRC extends ReactionCommand {

	int hits = 0;
	int maxHits = 4;

	public CriticalImpactRC(ResourceLocation registryName, boolean constantCheck) {
		super(registryName, constantCheck, 30 * 20);
	}

	@Override
	public void onUse(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
		if (conditionsToAppear(player, player)) {
			PlayerData playerData = PlayerData.get(player);
			IGlobalDataRM  remindData = ModDataRM.getGlobal(player);

			double X = player.getX();
			double Y = player.getY();
			double Z = player.getZ();

			if (!playerData.getActiveDriveForm().equals(ModDriveFormsRM.CRITICAL_IMPACT.get().getRegistryName().toString())) {
				DriveForm criticalImpact = ModDriveForms.registry.get(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.criticalImpact));
				criticalImpact.initDrive(player);
				playerData.removeReactionCommand(getRegistryName().toString());
				remindData.setSituationValue(0);
				remindData.setStyleTicks(100);
				remindData.clearSituationSpells();
				remindData.setStyle("");
				PacketHandlerRM.syncGlobalToAllAround(player, remindData);
			} else {
				// Finisher Attack Code Below

				float damage = (float) playerData.getStrength(true) * 2.5f; // AVG of STR
				float dmgMult = playerData.getNumberOfAbilitiesEquipped(Strings.criticalBoost) * 0.30f;

				damage += (damage * dmgMult);

				Level level = player.level();

				ServerLevel serverLevel = (ServerLevel) player.level();

				double radius = 6;

				List<LivingEntity> targets = level.getEntitiesOfClass(
						LivingEntity.class,
						player.getBoundingBox().inflate(radius)
				);


				for (int t = 1; t < 360; t += 20) {
					for (int s = 1; s < 360 ; s += 20) {
						double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double y = Y + (radius * Math.cos(Math.toRadians(t)));

						serverLevel.sendParticles(ParticleTypes.CRIT, x,y,z,2,0.05,0.05,0.05,0.01);

					}
				}

				List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty((player), player, (float) radius, (float) radius, (float) radius);
				for (LivingEntity e : targetList) {
					for (int t = 1; t < 360; t += 20) {
						for (int s = 1; s < 360; s += 20) {
							double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
							double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
							((ServerLevel) player.level()).sendParticles(new DustParticleOptions(new Vector3f(1F,1F,1F),1F),x,player.getY() ,z,1,0,0,0,0);
							e.knockback(2, -e.getX(),-e.getZ());
							e.hurt(e.damageSources().indirectMagic(e, player), damage * dmgMult);
						}
					}
				}
				level.playSound(
						null,
						player.blockPosition(),
						SoundEvents.PLAYER_ATTACK_SWEEP,
						SoundSource.PLAYERS,
						1F,
						1F
				);

				// Leave Form
				playerData.addFP(-1000);
				remindData.setStyle("NONE");
				remindData.setSituationValue(0);

				remindData.clearSituationSpells();
				PacketHandlerRM.syncGlobalToAllAround(player, remindData);
			}
		}
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
		PlayerData playerData = PlayerData.get(player);
		IGlobalDataRM  remindData = ModDataRM.getGlobal(player);
		if(playerData != null) {
			if (remindData != null){
				//if (playerData.getAlignment() == Utils.OrgMember.NONE) {
					if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
						if (remindData.getStyle().equals("PHYSICAL") || remindData.getStyle().equals("NONE")) {
							//Keyblade Check
							if (playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.earthshakerChain.get() || playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.endsOfTheEarthChain.get()){
								return true;

							}
						}
					} else if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.CRITICAL_IMPACT.get().getRegistryName().toString())) {
						if (remindData.getSituationValue() >= 100) {
							return true;
						}
					}
				//}
			}
		}
		return false;
	}
}
