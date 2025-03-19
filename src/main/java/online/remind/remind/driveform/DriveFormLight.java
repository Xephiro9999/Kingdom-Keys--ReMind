package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

@EventBusSubscriber(modid = KingdomKeysReMind.MODID)
public class DriveFormLight extends DriveForm {
    public DriveFormLight(String registeryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowthAbilities) {
        super(registeryName, order, hasKeychain, baseGrowthAbilities);
        this.color = new float[] { 1F, 1F, 0.85F };
        this.skinRL = skinRL;

    }

    @SubscribeEvent
    public static void getDarkModeXP(LivingDeathEvent event) {
        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof Monster) {
            if (event.getSource().getEntity() instanceof Player) {
                Player player = (Player) event.getSource().getEntity();
                PlayerData playerData = PlayerData.get(player);
                IGlobalDataRM formData = ModDataRM.getGlobal(player);

                if (playerData != null && playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID+":"+ StringsRM.lightForm)) {
                    double mult = Double.parseDouble(ModConfigs.SERVER.driveFormXPMultiplier.get().get(2).split(",")[1]);
                    //double mult = 1;
                    playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), (int) (playerData.getDriveFormExp(playerData.getActiveDriveForm()) + (1 * mult)));
                   // formData.setDarkModeEXP(playerData.getDriveFormExp(playerData.getActiveDriveForm()));
                    PacketHandlerRM.syncGlobalToAllAround(player, formData);
                    PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
                }
            }
        }
    }
}
