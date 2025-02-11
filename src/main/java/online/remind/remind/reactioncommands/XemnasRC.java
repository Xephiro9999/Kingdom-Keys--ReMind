package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.common.EventBusSubscriber;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;

public class XemnasRC extends ReactionCommand {


    public XemnasRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck);
    }


    @Override
    public void onUse(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
        if (conditionsToAppear(player, player)) {
            PlayerData playerData = PlayerData.get(player);

        }
    }


    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        PlayerData playerData = PlayerData.get(player);

        if (playerData != null) {
            if (player.getMainHandItem().getItem() instanceof IOrgWeapon) {
                IOrgWeapon weapon = (IOrgWeapon) player.getMainHandItem().getItem();
                /*
                System.out.println(weapon);
                System.out.println(weapon.getMember());
                System.out.println(playerData.getAlignment());
                */
                if (weapon.getMember() == playerData.getAlignment() && playerData.getAlignment() == Utils.OrgMember.XEMNAS) {
                    return true;
                }
            }
        }
        return false;
    }
}