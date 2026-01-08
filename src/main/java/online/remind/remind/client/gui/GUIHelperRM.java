package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.remind.remind.network.PacketHandlerRM;

public class GUIHelperRM {
    public static void openPanelMenu(){
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new PanelsMenu());
    }
}
