package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.common.EventBusSubscriber;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.remind.remind.KingdomKeysReMind;

public class StyleDiamondDust extends StyleForm {
    public StyleDiamondDust(ResourceLocation registryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowthAbilities) {
        super(registryName, order, skinRL, hasKeychain, baseGrowthAbilities);

        this.color = new float[]{0.0f, 0.75F, 1.0F};
    }
}
