package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.reactioncommand.DarkMineEntity;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

public class DarkMineRC extends ReactionCommand {


    public DarkMineRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck, -1);
    }


    @Override
    public void onUse(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
        if (conditionsToAppear(player, player)) {
            PlayerData playerData = PlayerData.get(player);
            GlobalDataRM globalData = ModDataRM.getGlobal(player);
            float dmgmult = PlayerData.get(player).getNumberOfAbilitiesEquipped(StringsRM.darknessBoost) * 0.25F;
            globalData.setRCCooldownTicks(40);
            playerData.setFP(playerData.getFP() - 40);
            player.level().playSound(null, player.blockPosition(), ModSoundsRM.DARK_MINE.get(), SoundSource.PLAYERS, 1F, 1F);

            for (int i = -90; i <= 225; i += 45) {
                ThrowableProjectile DarkMine = new DarkMineEntity(player.level(), player, dmgmult);
                DarkMine.setPos(player.getX(),player.getY(),player.getZ());
                DarkMine.setOwner(player);
                player.level().addFreshEntity(DarkMine);
                DarkMine.shootFromRotation(player, 0, player.getYRot() + i, 0, 1.25F, 0);
            }

            PacketHandlerRM.syncGlobalToAllAround(player, globalData);
            //PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
        }
    }


    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);
        if(playerData != null){
            if(playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.darkForm) && globalData.getRCCooldownTicks() == 0){
                return true;
            }
        }
        return false;
    }
}