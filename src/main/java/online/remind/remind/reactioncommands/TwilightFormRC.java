package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.common.EventBusSubscriber;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.lib.StringsRM;

public class TwilightFormRC extends ReactionCommand {

	public TwilightFormRC(ResourceLocation registryName, boolean constantCheck) {
		super(registryName, constantCheck, -1);
	}

	@Override
	public void onUse(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
		if (conditionsToAppear(player, player)) {
			DriveForm twilightForm = ModDriveForms.registry.get(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.twilight));
			twilightForm.initDrive(player);
		}
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
		PlayerData playerData = PlayerData.get(player);
		if (playerData != null && playerData.getEquippedKeychain(DriveForm.NONE) != null && playerData.getEquippedKeychain(ModDriveFormsRM.TWILIGHT.get().getRegistryName()) != null) {
			if (playerData.getDriveFormLevel(KingdomKeysReMind.MODID + ":" + StringsRM.darkForm) == 7 && playerData.getDriveFormLevel(KingdomKeysReMind.MODID + ":" + StringsRM.lightForm) == 7) {
				if (playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.darkForm))
					return playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.oblivionChain.get() && playerData.getEquippedKeychain(ModDriveFormsRM.TWILIGHT.get().getRegistryName()).getItem() == ModItems.oathkeeperChain.get();
				
				if (playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.lightForm))
					return playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.oathkeeperChain.get() && playerData.getEquippedKeychain(ModDriveFormsRM.TWILIGHT.get().getRegistryName()).getItem() == ModItems.oblivionChain.get();
			}
		}
		return false;
	}
}
