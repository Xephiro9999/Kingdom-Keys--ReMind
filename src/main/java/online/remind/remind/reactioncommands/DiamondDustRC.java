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

public class DiamondDustRC extends ReactionCommand {

	public DiamondDustRC(ResourceLocation registryName, boolean constantCheck) {
		super(registryName, constantCheck);
	}

	@Override
	public void onUse(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
		if (conditionsToAppear(player, player)) {
			PlayerData playerData = PlayerData.get(player);
			IGlobalDataRM  remindData = ModDataRM.getGlobal(player);
			DriveForm diamondDust = ModDriveForms.registry.get(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.diamondDust));
			diamondDust.initDrive(player);
			playerData.removeReactionCommand(getRegistryName().toString());
			remindData.setDiamondDust(false);
			remindData.setSituationValue(0);
			remindData.clearSituationSpells();
			PacketHandlerRM.syncGlobalToAllAround(player, remindData);

		}
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
		PlayerData playerData = PlayerData.get(player);
		IGlobalDataRM remindData = ModDataRM.getGlobal(player);
		if(playerData != null) {
			if (remindData != null){
				if (playerData.getAlignment() == Utils.OrgMember.NONE) {
					if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
						if (remindData.isDiamondDust()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
