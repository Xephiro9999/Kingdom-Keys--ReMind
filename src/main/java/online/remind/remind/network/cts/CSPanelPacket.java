package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.client.gui.PanelsMenu;
import online.remind.remind.network.PacketHandlerRM;

import java.sql.SQLOutput;
import java.util.function.Supplier;

public class CSPanelPacket {
    // 0 = none (default), 1 = str, 2 = mag, 3 = def...
    private int choice;
    String formName;


    private static int driveLvl;

    public CSPanelPacket(){}

    public CSPanelPacket(int choice){
        this.choice = choice;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.choice);
    }

    public static CSPanelPacket decode(FriendlyByteBuf buffer) {
        CSPanelPacket msg = new CSPanelPacket();
        msg.choice = buffer.readInt();
        return msg;
    }

    public static void handle(final CSPanelPacket message, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer player = ctx.get().getSender();

        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        IGlobalCapabilitiesRM globalData = ModCapabilitiesRM.getGlobal(player);


        //
        int level;
        int xpGain;
        int i;
        int totalBoost;
        float heartsRegained;


        //System.out.println(globalData.getPanelChoice());

        switch(message.choice){
            case 0:
                System.out.println("This shouldn't happen");
                break;
            case 1:
                playerData.addHearts(-1000 * globalData.getSTRPanel());
                globalData.addSTRPanel(1);
                System.out.println(globalData.getSTRPanel());
                break;
            case 2:
                playerData.addHearts(-1000 * globalData.getMAGPanel());
                globalData.addMAGPanel(1);
                break;
            case 3:
                playerData.addHearts(-1000 * globalData.getDEFPanel());
                globalData.addDEFPanel(1);
                System.out.println(globalData.getDEFPanel());
                break;
            case 4:
                playerData.addMaxAP(2);
                playerData.addHearts(-1000);
                break;
            case 5:
                level = playerData.getDriveFormLevel(Strings.Form_Valor);
                xpGain = level * 10;

                playerData.addDriveFormExperience(Strings.Form_Valor, player, xpGain);
                playerData.addHearts(-5000);
                PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(player)), (ServerPlayer) player);
                break;
            case 6:
                level = playerData.getDriveFormLevel(Strings.Form_Wisdom);
                xpGain = level * 4;

                playerData.addDriveFormExperience(Strings.Form_Wisdom, player, xpGain);
                playerData.addHearts(-5000);
                PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(player)), (ServerPlayer) player);
                break;
            case 7:
                level = playerData.getDriveFormLevel(Strings.Form_Limit);
                xpGain = level * 2;

                playerData.addDriveFormExperience(Strings.Form_Limit, player, xpGain);
                playerData.addHearts(-5000);
                PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(player)), (ServerPlayer) player);
                break;
            case 8:
                level = playerData.getDriveFormLevel(Strings.Form_Master);
                xpGain = level * 8;

                playerData.addDriveFormExperience(Strings.Form_Master, player, xpGain);
                playerData.addHearts(-5000);
                PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(player)), (ServerPlayer) player);
                break;
            case 9:
                level = playerData.getDriveFormLevel(Strings.Form_Final);
                xpGain = level * 4;

                playerData.addDriveFormExperience(Strings.Form_Final, player, xpGain);
                playerData.addHearts(-5000);
                PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(player)), (ServerPlayer) player);
                break;
            case 10:
                playerData.addHearts(-10000 * playerData.getLevel());
                xpGain = playerData.getExpNeeded(playerData.getLevel(), 0) - playerData.getExperience();
                //System.out.println(playerData.getExpNeeded(playerData.getLevel(),0)- playerData.getExperience());
                playerData.addExperience(player, xpGain, false, true);
                break;
            case 11:

                totalBoost = globalData.getDEFPanel() + globalData.getSTRPanel() + globalData.getMAGPanel();

                heartsRegained = (totalBoost * 1000) * 0.75f;

                globalData.setSTRPanel(0);
                globalData.setMAGPanel(0);
                globalData.setDEFPanel(0);

                playerData.getStrengthStat().removeModifier("Panel");
                playerData.getMagicStat().removeModifier("Panel");
                playerData.getDefenseStat().removeModifier("Panel");

                playerData.addHearts((int)heartsRegained);

                System.out.println(totalBoost);
                System.out.println(heartsRegained);
                break;
        }

        PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), player);
        PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        ctx.get().setPacketHandled(true);
    }
}


