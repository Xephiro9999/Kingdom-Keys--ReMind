package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.common.EventBusSubscriber;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.remind.remind.KingdomKeysReMind;

public class StyleThunderBolt extends DriveForm {
    public StyleThunderBolt(ResourceLocation registeryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowthAbilities) {
        super(registeryName, order, hasKeychain, baseGrowthAbilities);
        this.color = new float[]{0.0f, 1.0F, 0.0F};
        this.skinRL = skinRL;
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
