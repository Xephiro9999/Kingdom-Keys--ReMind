package online.remind.remind.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class AbilityOrbItem extends Item implements ICreativeTabRM {
    String abilities;

    public AbilityOrbItem(Properties properties, String name){
        super(properties);
        this.abilities = name;
    }

    @Override
    public Tab getTab(){
        return Tab.MISC;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand){
        PlayerData playerData = PlayerData.get(player);
        Ability abilityInstance = ModAbilities.registry.get(ResourceLocation.parse(abilities));

        if(!world.isClientSide){
            if (!playerData.getPAbilitiesList().contains(abilities)){
                playerData.getPAbilitiesList().add(abilities);
                takeItem(player);
				player.displayClientMessage(Component.translatable("Permanently learned " + Utils.translateToLocal(abilityInstance.getTranslationKey())), true);
            } else {
                player.displayClientMessage(Component.translatable("You already have " + Utils.translateToLocal(abilityInstance.getTranslationKey()) + " permanently."), true);

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

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
        Ability abilityInstance = ModAbilities.registry.get(ResourceLocation.parse(abilities));
        tooltip.add(Component.translatable("Contains ability: " + Utils.translateToLocal(abilityInstance.getTranslationKey())));
    }
}
