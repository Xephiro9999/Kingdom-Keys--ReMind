package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;

public class GUIHelperRM {
    @OnlyIn(Dist.CLIENT)
    public static void openAddonMenu() {
        Minecraft mc = Minecraft.getInstance();
        mc.level.playSound(mc.player, mc.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
        //mc.setScreen(new AddonMenu()); TODO send packet to get player data to open menu
    }
}
