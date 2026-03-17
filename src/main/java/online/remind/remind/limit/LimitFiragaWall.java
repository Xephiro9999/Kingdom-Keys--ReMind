package online.remind.remind.limit;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class LimitFiragaWall extends Limit {

    public LimitFiragaWall(ResourceLocation registryName, int order, Utils.OrgMember owner) {
        super(registryName, order, owner);
    }

    @Override
    public void onUse(Player player, LivingEntity livingEntity) {

    }
}
