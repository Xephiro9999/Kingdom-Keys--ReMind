package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.common.EventBusSubscriber;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.HeartEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.network.PacketHandlerRM;

public class RiskchargeReaction extends ReactionCommand {


    public RiskchargeReaction(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck);
    }





    @Override
    public void onUse(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
        if(conditionsToAppear(player,player)){
            PlayerData playerData = PlayerData.get(player);
            IGlobalDataRM globalData = ModDataRM.getGlobal(player);
            HeartEntity heart = new HeartEntity(player.level());

            player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSoundsRM.RISKCHARGE.get(), SoundSource.PLAYERS, 1F, 1F);
            player.level().addFreshEntity(heart);
            heart.setPos(player.getX(),player.getY() + 1,player.getZ());
            player.setHealth(player.getHealth()/2);
            globalData.setRCCooldownTicks(25);
            player.invulnerableTime = 2;
            playerData.getStrengthStat().addModifier("Riskcharge", 5.5, true, true);
            playerData.addFP(50);
            globalData.setRiskchargeCount(globalData.getRiskchargeCount()+1);
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);
            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
        }
    }

    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        PlayerData playerData = PlayerData.get(player);
        IGlobalDataRM globalData = ModDataRM.getGlobal(player);
        if(playerData != null){
            if(playerData.getActiveDriveForm().equals(ModDriveFormsRM.RAGE.get().getRegistryName().toString())){
                if(globalData.getRiskchargeCount() < 3 && globalData.getRCCooldownTicks() == 0){
                    return true;
                }
            }
        }
        return false;
    }
}
