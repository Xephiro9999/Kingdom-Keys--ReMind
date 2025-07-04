package online.remind.remind.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class RMCoinItem extends Item implements IItemCategory{
    int value;
    String type;

    public RMCoinItem(Properties properties, int value, String type) {
        super(properties);
        this.value = value;
        this.type = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        if (!world.isClientSide) {
            if (playerData != null ) {
                if (!ItemStack.matches(player.getMainHandItem(), ItemStack.EMPTY) && player.getMainHandItem().getItem() == this) {
                    player.getMainHandItem().shrink(1);
                } else if (!ItemStack.matches(player.getOffhandItem(), ItemStack.EMPTY) && player.getOffhandItem().getItem() == this) {
                    player.getOffhandItem().shrink(1);
                }
                switch (type) {
                    case "munny": {
                        playerData.setMunny(playerData.getMunny() + value);
                        break;
                    }
                    case "hearts": {
                        playerData.addHearts(value);
                        break;
                    }
                }
                PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.MISC;
    }

}