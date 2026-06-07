package online.remind.remind.client;

import net.minecraft.client.Minecraft;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.stc.SCOrganizationPanelSyncPacket;
import online.remind.remind.panels.PanelGrid;

public class ClientOrganizationPanelSyncHandler {

    public static void handle(SCOrganizationPanelSyncPacket packet) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(minecraft.player);

        if (globalData == null) {
            return;
        }

        globalData.setOrganizationPanelGrid(PanelGrid.load(packet.gridTag()));

        globalData.getOwnedOrganizationPanels().clear();

        for (String key : packet.ownedPanelsTag().getAllKeys()) {
            globalData.getOwnedOrganizationPanels().put(
                    key,
                    packet.ownedPanelsTag().getInt(key)
            );
        }

        globalData.setUnlockedOrganizationPanelSlots(packet.unlockedSlots());
    }
}