package online.remind.remind.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ShotlockOrbItem extends Item {
    String shotlocks;

    public ShotlockOrbItem(Properties properties, String name){
        super(properties);
        this.shotlocks = name;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand){
        PlayerData playerData = PlayerData.get(player);
        Shotlock shotlockInstance = ModShotlocks.registry.get(ResourceLocation.parse(shotlocks));

        if(!world.isClientSide){
            if (!playerData.getShotlockList().contains(shotlocks)){
                playerData.getShotlockList().add(shotlocks);
                takeItem(player);
				player.displayClientMessage(Component.translatable("Unlocked " + Utils.translateToLocal(shotlockInstance.getTranslationKey())), true);
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    private void takeItem(Player player) {
        if (!ItemStack.matches(player.getMainHandItem(), ItemStack.EMPTY) && player.getMainHandItem().getItem() == this) {
            player.getMainHandItem().shrink(1);
        } else if (!ItemStack.matches(player.getOffhandItem(), ItemStack.EMPTY) && player.getOffhandItem().getItem() == this) {
            player.getOffhandItem().shrink(1);
        }
    }
}
