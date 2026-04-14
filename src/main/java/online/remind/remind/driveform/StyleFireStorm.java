package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;

public class StyleFireStorm extends DriveForm {
    public StyleFireStorm(ResourceLocation registeryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowthAbilities) {
        super(registeryName, order, hasKeychain, baseGrowthAbilities);
        this.color = new float[]{1f, 0.0F, 0.0F};
        this.skinRL = skinRL;
        ModDriveFormsRM.styles.add(registeryName);
    }

    @Override
    public boolean displayInCommandMenu(Player player){
        return false;
    }

    @Override
    public void endDrive(Player player) {
        super.endDrive(player);
        PlayerData playerData = PlayerData.get(player);
    }
}
