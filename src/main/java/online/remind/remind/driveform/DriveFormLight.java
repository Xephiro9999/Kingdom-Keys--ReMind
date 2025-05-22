package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

@Mod.EventBusSubscriber(modid = KingdomKeysReMind.MODID)
public class DriveFormLight extends DriveForm {
    public DriveFormLight(String registeryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowthAbilities) {
        super(registeryName, order, hasKeychain, baseGrowthAbilities);
        this.color = new float[] { 1F, 1F, 0.85F };
        this.skinRL = skinRL;

    }

    @Override
    public boolean displayInCommandMenu(Player player){
        return ModCapabilities.getPlayer(player).isAbilityEquipped(StringsRM.wayToLight);
    }

    @SubscribeEvent
    public static void getDarkModeXP(LivingDeathEvent event) {
        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof Monster) {
            if (event.getSource().getEntity() instanceof Player) {
                Player player = (Player) event.getSource().getEntity();
                IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
                IGlobalCapabilitiesRM formData = ModCapabilitiesRM.getGlobal(player);

                if (playerData != null && playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID+":"+ StringsRM.lightForm)) {
                    double mult = Double.parseDouble(ModConfigs.driveFormXPMultiplier.get(0).split(",")[1]);
                    playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), (int) (playerData.getDriveFormExp(playerData.getActiveDriveForm()) + (1 * mult)));
                   // formData.setDarkModeEXP(playerData.getDriveFormExp(playerData.getActiveDriveForm()));
                    PacketHandlerRM.syncGlobalToAllAround(player, formData);
                    PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
                }
            }
        }
    }
}
