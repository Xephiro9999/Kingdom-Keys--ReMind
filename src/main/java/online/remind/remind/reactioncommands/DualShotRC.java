package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.ability.ModAbilitiesRM;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.entity.reactioncommand.DualShotEntity;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

public class DualShotRC extends ReactionCommand {
    public DualShotRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck, -1);
    }

    @Override
    public void onUse(Player player, LivingEntity livingEntity, LivingEntity lockOnEntity) {
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);
        float dmgmult = (PlayerData.get(player).getNumberOfAbilitiesEquipped(ModAbilitiesRM.DARKNESS_BOOST) + PlayerData.get(player).getNumberOfAbilitiesEquipped(ModAbilitiesRM.LIGHT_BOOST))  * 0.2F;
        globalData.setRCCooldownTicks(60);
        playerData.setFP(playerData.getFP() - 40);

        // Fire Dual Shot

        player.swing(InteractionHand.MAIN_HAND, true);




        ThrowableProjectile dualShot = new DualShotEntity(player.level(), player,dmgmult,lockOnEntity);
        dualShot.setPos(player.getX(), player.getY()+0.75,player.getZ());
        dualShot.setOwner(player);
        player.level().addFreshEntity(dualShot);
        dualShot.shootFromRotation(player, player.getXRot(),player.getYRot(),0,1.25F, 0);
        player.level().playSound(null, player.blockPosition(), ModSoundsRM.DUAL_SHOT.get(), SoundSource.PLAYERS, 1F, 1F);
        // Sync Packet
        PacketHandlerRM.syncGlobalToAllAround(player, globalData);


    }

    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);
        if (playerData != null) {
            if (playerData.isFormActive(ModDriveFormsRM.TWILIGHT) && globalData.getRCCooldownTicks() == 0) {
                return true;
            }
        }
        return false;
    }
}
