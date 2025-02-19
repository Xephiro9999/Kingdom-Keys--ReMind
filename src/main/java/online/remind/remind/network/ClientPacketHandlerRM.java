package online.remind.remind.network;

import net.minecraft.client.Minecraft;
import online.kingdomkeys.kingdomkeys.client.gui.IPlayerDataRequester;
import online.kingdomkeys.kingdomkeys.client.gui.menu.NoChoiceMenuPopup;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSendPlayerDataToClient;
import online.remind.remind.client.gui.AddonMenu;
import online.remind.remind.network.stc.SCOpenAddonMenu;

public class ClientPacketHandlerRM {

    public static void sendPlayerDataToClient(SCSendPlayerDataToClient message) {
        if (Minecraft.getInstance().screen instanceof IPlayerDataRequester gui) {
            PlayerData data = PlayerData.get(message.playerData(), Minecraft.getInstance().player);
            gui.updatePlayerData(data);
        }
    }
    public static void openMenu(SCOpenAddonMenu message) {
        if (message.open()) {
            Minecraft.getInstance().setScreen(new AddonMenu(PlayerData.get(message.playerData(), Minecraft.getInstance().player)));
        } else {
            Minecraft.getInstance().setScreen(new NoChoiceMenuPopup());
        }
    }

}
