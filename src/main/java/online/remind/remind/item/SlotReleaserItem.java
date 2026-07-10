package online.remind.remind.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.panels.OrganizationPanelRewardHelper;

public class SlotReleaserItem extends Item implements ICreativeTabRM {

    public SlotReleaserItem(Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        boolean used = OrganizationPanelRewardHelper.useSlotReleaser(player);

        if (!used) {
            return InteractionResultHolder.fail(stack);
        }

        if (!player.isCreative()) {
            stack.shrink(1);
        }

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public Tab getTabRM() {
        return Tab.MISC;
    }
}