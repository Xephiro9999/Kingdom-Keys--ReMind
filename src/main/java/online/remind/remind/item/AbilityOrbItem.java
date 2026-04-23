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
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
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
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        PlayerData playerData = PlayerData.get(player);
        String abilityIdString = player.getItemInHand(hand).get(ModComponentsRM.ABILITY.get());

        if (abilityIdString == null) {
            player.displayClientMessage(Component.literal("This orb doesn't contain an ability..."), true);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }

        Ability abilityInstance = ModAbilities.registry.get(ResourceLocation.parse(abilityIdString));

        if (abilityInstance == null) {
            player.displayClientMessage(Component.literal("Invalid ability."), true);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }

        if (!world.isClientSide) {
            if (!playerData.getPAbilitiesList().contains(abilityIdString)) {
                playerData.addPAbility(abilityIdString);
                takeItem(player);
                player.displayClientMessage(Component.literal(
                        "Permanently learned " + Utils.translateToLocal(abilityInstance.getTranslationKey())
                ), true);
                PacketHandler.syncToAllAround(player, playerData);
            } else {
                player.displayClientMessage(Component.literal(
                        "You already have " + Utils.translateToLocal(abilityInstance.getTranslationKey())
                ), true);
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
        String abilityIdString = stack.get(ModComponentsRM.ABILITY.get());

        if (abilityIdString != null) {
            Ability abilityInstance = ModAbilities.registry.get(ResourceLocation.parse(abilityIdString));

            if (abilityInstance != null) {
                tooltip.add(Component.literal("Contains ability: " +
                        Utils.translateToLocal(abilityInstance.getTranslationKey())));
                return;
            }
        }

        tooltip.add(Component.literal("This orb doesn't contain an ability."));
    }
}
