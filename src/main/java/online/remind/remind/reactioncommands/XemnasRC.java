package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.remind.remind.ability.ModAbilitiesRM;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.entity.reactioncommand.ThornsEntity;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

public class XemnasRC extends ReactionCommand {


    public XemnasRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck, -1);
    }


    @Override
    public void onUse(Player player, LivingEntity livingEntity, LivingEntity lockOnEntity) {
        if (conditionsToAppear(player, player)) {
            PlayerData playerData = PlayerData.get(player);

            GlobalDataRM globalData = ModDataRM.getGlobal(player);
            float dmgmult = (PlayerData.get(player).getNumberOfAbilitiesEquipped(ModAbilitiesRM.DARKNESS_BOOST) * 0.2F);
            globalData.setRCCooldownTicks(60);
            playerData.setFP(playerData.getFP() - 40);

            // Fire Dual Shot

            player.swing(InteractionHand.MAIN_HAND, true);




            ThrowableProjectile thorn = new ThornsEntity(player.level(), player,lockOnEntity);
            thorn.setPos(player.getX(), player.getY()+0.75,player.getZ());
            thorn.setOwner(player);
            player.level().addFreshEntity(thorn);
            thorn.shootFromRotation(player, player.getXRot(),player.getYRot(),0,1.25F, 0);
            //player.level().playSound(null, player.blockPosition(), ModSoundsRM.DUAL_SHOT.get(), SoundSource.PLAYERS, 1F, 1F);
            // Sync Packet
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        }
    }


    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        PlayerData playerData = PlayerData.get(player);

        if (playerData != null) {
            if (player.getMainHandItem().getItem() instanceof IOrgWeapon) {
                IOrgWeapon weapon = (IOrgWeapon) player.getMainHandItem().getItem();
                /*

                if (weapon.getMember() == playerData.getAlignment() && playerData.getAlignment() == Utils.OrgMember.XEMNAS) {
                    return true;
                }
                */
            } /*else if (playerData.isAbilityEquipped(StringsRM.Regen)){
                return true;
            }*/
        }
        return false;
    }
}