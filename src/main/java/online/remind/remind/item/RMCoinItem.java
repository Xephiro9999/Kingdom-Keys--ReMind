package online.remind.remind.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

import java.util.List;
import java.util.function.Supplier;

public class RMCoinItem extends Item implements IItemCategory,ICreativeTabRM{
    private final Supplier<Integer> value;
    private final String type;

    @Override
    public Tab getTab() {
        return Tab.MISC;
    }



    public RMCoinItem(Properties properties, Supplier<Integer> value, String type) {
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
                int coinValue = value.get();

                if (player.isCrouching()){
                    if (!ItemStack.matches(player.getMainHandItem(), ItemStack.EMPTY) && player.getMainHandItem().getItem() == this) {
                        player.getMainHandItem().shrink(stack);
                    } else if (!ItemStack.matches(player.getOffhandItem(), ItemStack.EMPTY) && player.getOffhandItem().getItem() == this) {
                        player.getOffhandItem().shrink(stack);
                    }
                    switch (type) {
                        case "munny": {
                            playerData.setMunny(playerData.getMunny() + coinValue * stack);
                            player.displayClientMessage(Component.translatable(ChatFormatting.YELLOW+"You've received " + coinValue * stack + " Munny!"), true);
                            //player.level().playSound(player, player.blockPosition(), ModSounds.itemget.get(), SoundSource.MASTER, 1.0f, 1.0f);

                            break;
                        }
                        case "hearts": {
                            playerData.addHearts(coinValue * stack);
                            player.displayClientMessage(Component.translatable(ChatFormatting.YELLOW+"You've received " + coinValue * stack + " Hearts!"), true);
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
                            playerData.setMunny(playerData.getMunny() + coinValue);
                            player.displayClientMessage(Component.translatable(ChatFormatting.YELLOW+"You've received " + coinValue + " Munny!"), true);
                            //player.level().playSound(player, player.blockPosition(), ModSounds.itemget.get(), SoundSource.MASTER, 1.0f, 1.0f);

                            break;
                        }
                        case "hearts": {
                            playerData.addHearts(coinValue);
                            player.displayClientMessage(Component.translatable(ChatFormatting.YELLOW+"You've received " + coinValue + " Hearts!"), true);
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
    public void appendHoverText(ItemStack stack,
                                Item.TooltipContext context,
                                List<Component> tooltip,
                                TooltipFlag flag) {
        tooltip.add(
                Component.literal(getCoinValue() + " " + getCoinType())
                        .withStyle(ChatFormatting.YELLOW)
        );
    }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.MISC;
    }

    public int getCoinValue() {
        return value.get();
    }

    public String getCoinType() {
        return type;
    }
}
