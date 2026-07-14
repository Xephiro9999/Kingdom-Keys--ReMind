package online.remind.remind.reactioncommands;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;

public class ZexionRC extends ReactionCommand {
    ResourceLocation magic;


    public ZexionRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck, -1);
    }

    public String getMagicName() {
        return magic.toString();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getTranslationKey() {
        GlobalDataRM globalData = ModDataRM.getGlobal(Minecraft.getInstance().player);
        int level = globalData.getLearnedMagicLevel(magic);
        Magic mag = ModMagic.registry.get(magic);
        //Maybe this will have to be re-enabled if we give access to -za magic to players without reaction commands
		/*if(level == mag.getMaxLevel()) { //If magic level is the same as the max keep it max
			level = mag.getMaxLevel();
		} else { //If magic level is not max increment it one level
			level++;
		}*/

        return "Doublecast: " + "magic." + magic.getNamespace() + "." + magic.getPath() + level+".name";
    }


    @Override
    public void onUse(Player player, LivingEntity livingEntity, LivingEntity lockOnEntity) {
            PlayerData playerData = PlayerData.get(player);
            GlobalDataRM globalData = ModDataRM.getGlobal(player);
            Magic mag = ModMagic.registry.get(magic);
            int level = globalData.getLearnedMagicLevel(magic);

            globalData.setRCCooldownTicks(60);

            player.swing(InteractionHand.MAIN_HAND, true);

            mag.onUse(player, player, lockOnEntity);
            playerData.removeReactionCommand(getRegistryName());
            //player.level().playSound(null, player.blockPosition(), ModSoundsRM.DUAL_SHOT.get(), SoundSource.PLAYERS, 1F, 1F);
            // Sync Packet
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);
    }


    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);
        if (playerData != null) {
            if (player.getMainHandItem().getItem() instanceof IOrgWeapon) {
                if (globalData.getRCCooldownTicks() == 0) {
                    IOrgWeapon weapon = (IOrgWeapon) player.getMainHandItem().getItem();
                    if (weapon.getMember() == playerData.getAlignment() && playerData.getAlignment() == Utils.OrgMember.ZEXION) {
                    return true;
                    }
                }
            }
        }
        return false;
    }
}