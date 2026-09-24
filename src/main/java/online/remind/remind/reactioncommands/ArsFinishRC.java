package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.config.ServerConfig;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormLimit;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.handler.ArsArcanumSequenceHandler;

public class ArsFinishRC extends ReactionCommand {

    public ArsFinishRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck, 20, 0xFFD700);
    }

    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        return ArsArcanumSequenceHandler.canUseFinish(player); // Only show after Bash #5
    }

    @Override
    public void onUse(Player player, LivingEntity livingEntity, LivingEntity target) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        PlayerData playerData = PlayerData.get(player);
        ArsArcanumSequenceHandler.useFinish(serverPlayer); // Perform the final hit
        if (playerData == null) {
            return;
        }
        if(playerData.isFormActive(ModDriveForms.LIMIT)){ // Giving Limit Form EXP on the final use
            float formXP = playerData.getDriveFormLevel(ModDriveForms.LIMIT.location()); //
            playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), (int) (playerData.getDriveFormExp(playerData.getActiveDriveForm()) + formXP + 1));
            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer)player);
        }

    }
}