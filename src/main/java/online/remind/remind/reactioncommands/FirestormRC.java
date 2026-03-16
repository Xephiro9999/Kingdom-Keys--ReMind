package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

public class FirestormRC extends ReactionCommand {

	public FirestormRC(ResourceLocation registryName, boolean constantCheck) {
		super(registryName, constantCheck);
	}

	@Override
	public void onUse(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
		if (conditionsToAppear(player, player)) {
			PlayerData playerData = PlayerData.get(player);
			IGlobalDataRM  remindData = ModDataRM.getGlobal(player);
			if (!playerData.getActiveDriveForm().equals(ModDriveFormsRM.FIRESTORM.get().getRegistryName().toString())) {
				DriveForm firestorm = ModDriveForms.registry.get(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.fireStorm));
				firestorm.initDrive(player);
				playerData.removeReactionCommand(getRegistryName().toString());
				remindData.setSituationValue(0);
				remindData.setStyle("");
				remindData.clearSituationSpells();
				PacketHandlerRM.syncGlobalToAllAround(player, remindData);
			} else {
				System.out.println("Finisher Code Here");

				// Leave Form
				playerData.addFP(-1000);
				remindData.setStyle("");
				remindData.setSituationValue(0);
				PacketHandlerRM.syncGlobalToAllAround(player, remindData);
			}
		}
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
		PlayerData playerData = PlayerData.get(player);
		IGlobalDataRM remindData = ModDataRM.getGlobal(player);
		if(playerData != null) {
			if (remindData != null){
				if (playerData.getAlignment() == Utils.OrgMember.NONE) {
                    // Should show the "Finisher"
                    if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
						if (remindData.getStyle().equals("FIRE")) {
							return true;
						}
					} else if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.FIRESTORM.get().getRegistryName().toString())) {
						if (remindData.getSituationValue() >= 100) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
