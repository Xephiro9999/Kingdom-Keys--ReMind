package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.lib.StringsRM;

public class DriveFormTwilight extends DriveForm {
    public DriveFormTwilight(ResourceLocation registeryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowthAbilities) {
        super(registeryName, order, hasKeychain, baseGrowthAbilities);
        this.color = new float[] {0.25F,0.25F,0.25F};
        this.skinRL = skinRL;
    }

    // Twilight Form EXP Gain -- If decided to let the form level

    @Override
    public boolean isSlotVisible(Player player) {
		PlayerData playerData = PlayerData.get(player);
		if (playerData != null) {
			if (playerData.isAbilityEquipped(StringsRM.roadToDawn)) {
				return true;
			} else if (playerData.getDriveFormLevel(KingdomKeysReMind.MODID + ":" + StringsRM.darkForm) == 7 && playerData.getDriveFormLevel(KingdomKeysReMind.MODID + ":" + StringsRM.lightForm) == 7) {
				if (playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.darkForm))
					if (playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.oblivionChain.get()) {
						return true;
					}

				if (playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.lightForm))
					if (playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.oathkeeperChain.get()) {
						return true;
					}

				return playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.twilight);
			}

		}
		return false;
	}

	@Override
	public boolean displayInCommandMenu(Player player) {
		PlayerData playerData = PlayerData.get(player);
		if (playerData != null) {
			if (playerData.isAbilityEquipped(StringsRM.roadToDawn)){
				return true;
			}
		}
		return false;
	}
}
