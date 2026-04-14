package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.remind.remind.lib.StringsRM;

public class DriveFormRegen extends DriveForm {

    public DriveFormRegen(ResourceLocation registeryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowthAbilities) {
        super(registeryName, order, hasKeychain, baseGrowthAbilities);
        this.color = new float[]{0.25F, 0.25F, 0.25F};
        this.skinRL = skinRL;
    }

    @Override
    public boolean displayInCommandMenu(Player player){
        return PlayerData.get(player).isAbilityEquipped(StringsRM.Regen);

    }

    @Override
    public void endDrive(Player player) {
        super.endDrive(player);
        PlayerData playerData = PlayerData.get(player);
    }

}
