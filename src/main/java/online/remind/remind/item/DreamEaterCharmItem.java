package online.remind.remind.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.List;

public class DreamEaterCharmItem extends Item implements ICreativeTabRM {

    private final String dreamEaterRL;
    private final String dreamEaterName;

    public DreamEaterCharmItem(Properties properties, String dreamEaterRL, String dreamEaterName) {
        super(properties);
        this.dreamEaterRL = dreamEaterRL;
        this.dreamEaterName = dreamEaterName;
    }

    @Override
    public ICreativeTabRM.Tab getTab(){
        return ICreativeTabRM.Tab.DREAMEATERS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, net.minecraft.world.entity.player.Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.fail(stack);
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(serverPlayer);

        if (globalData == null) {
            return InteractionResultHolder.fail(stack);
        }

        if (globalData.hasDreamEaterUnlocked(this.dreamEaterRL)) {
            serverPlayer.displayClientMessage(
                    Component.literal("You already unlocked " + this.dreamEaterName + "."),
                    true
            );

            return InteractionResultHolder.success(stack);
        }

        globalData.unlockDreamEater(this.dreamEaterRL);
        PacketHandlerRM.syncGlobalToAllAround(serverPlayer, globalData);

        if (!serverPlayer.isCreative()) {
            stack.shrink(1);
        }

        serverPlayer.displayClientMessage(
                Component.literal("Dream Eater unlocked: " + this.dreamEaterName + "!"),
                false
        );



        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.literal("Unlocks Dream Eater: " + this.dreamEaterName)
                .withStyle(net.minecraft.ChatFormatting.AQUA));

        tooltip.add(Component.literal("Right-click to permanently unlock this Spirit.")
                .withStyle(net.minecraft.ChatFormatting.GRAY));
    }
}