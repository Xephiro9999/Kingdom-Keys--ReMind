package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;

public class GUIHelperRM {
    public static void openPanelMenu(){
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new PanelsMenu());
    }
}
