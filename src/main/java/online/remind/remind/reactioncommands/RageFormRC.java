package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.lib.StringsRM;

import java.util.Random;
import java.util.WeakHashMap;

public class RageFormRC extends ReactionCommand {
    public RageFormRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck, -1, 0xff6f00);
    }

    @Override
    public void onUse(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
        if (conditionsToAppear(player, player)) {
            PlayerData playerData = PlayerData.get(player);
            DriveForm rageForm = ModDriveForms.registry.get(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.rageForm));
            if (playerData.getDriveFormLevel(ModDriveFormsRM.RAGE.get().getRegistryName().toString()) == 0) {
                playerData.setDriveFormLevel(ModDriveFormsRM.RAGE.get().getRegistryName().toString(), 1);
            }
            rageForm.initDrive(player);
            playerData.removeReactionCommand(getName());
            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
        }
    }

    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        return Utils.isLowHP(player.getHealth(), player.getMaxHealth());
    }

}