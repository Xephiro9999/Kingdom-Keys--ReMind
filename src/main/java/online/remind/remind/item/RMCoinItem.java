package online.remind.remind.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

public class RMCoinItem extends Item implements IItemCategory {
    int value;
    String type;

    public RMCoinItem(Properties properties, int value, String type) {
        super(properties);
        this.value = value;
        this.type = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        PlayerData playerData = PlayerData.get(player);
        if (!world.isClientSide) {
            if (playerData != null ) {

                int stack = player.getMainHandItem().getCount();

                if (player.isCrouching()){
                    if (!ItemStack.matches(player.getMainHandItem(), ItemStack.EMPTY) && player.getMainHandItem().getItem() == this) {
                        player.getMainHandItem().shrink(stack);
                    } else if (!ItemStack.matches(player.getOffhandItem(), ItemStack.EMPTY) && player.getOffhandItem().getItem() == this) {
                        player.getOffhandItem().shrink(stack);
                    }
                    switch (type) {
                        case "munny": {
                            playerData.setMunny(playerData.getMunny() + value * stack);
                            player.displayClientMessage(Component.translatable(ChatFormatting.YELLOW+"You've received " + value * stack + " Munny!"), true);
                            //player.level().playSound(player, player.blockPosition(), ModSounds.itemget.get(), SoundSource.MASTER, 1.0f, 1.0f);

                            break;
                        }
                        case "hearts": {
                            playerData.addHearts(value * stack);
                            player.displayClientMessage(Component.translatable(ChatFormatting.YELLOW+"You've received " + value * stack + " Hearts!"), true);
                            //player.level().playSound(player, player.blockPosition(), ModSounds.itemget.get(), SoundSource.MASTER, 1.0f, 1.0f);
                            break;
                        }
                    }
                } else {
                    if (!ItemStack.matches(player.getMainHandItem(), ItemStack.EMPTY) && player.getMainHandItem().getItem() == this) {
                        player.getMainHandItem().shrink(1);
                    } else if (!ItemStack.matches(player.getOffhandItem(), ItemStack.EMPTY) && player.getOffhandItem().getItem() == this) {
                        player.getOffhandItem().shrink(1);
                    }
                    switch (type) {
                        case "munny": {
                            playerData.setMunny(playerData.getMunny() + value);
                            player.displayClientMessage(Component.translatable(ChatFormatting.YELLOW+"You've received " + value + " Munny!"), true);
                            //player.level().playSound(player, player.blockPosition(), ModSounds.itemget.get(), SoundSource.MASTER, 1.0f, 1.0f);

                            break;
                        }
                        case "hearts": {
                            playerData.addHearts(value);
                            player.displayClientMessage(Component.translatable(ChatFormatting.YELLOW+"You've received " + value + " Hearts!"), true);
                            //player.level().playSound(player, player.blockPosition(), ModSounds.itemget.get(), SoundSource.MASTER, 1.0f, 1.0f);
                            break;
                        }
                    }
                }

                PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.MISC;
    }

    public int getCoinValue() {
        return value;
    }

    public String getCoinType() {
        return type;
    }
}
