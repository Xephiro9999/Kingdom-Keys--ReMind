package online.remind.remind.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;

public class MusicManager {
    private static DreamEaterMenuSound currentSound;

    public static void start() {
        if (currentSound == null) {
            currentSound = new DreamEaterMenuSound(ModSoundsRM.DREAM_EATERS.get());
            Minecraft.getInstance().getSoundManager().play(currentSound);
        }
    }

    public static void stop() {
        if (currentSound != null) {
            SoundManager sm = Minecraft.getInstance().getSoundManager();
            sm.stop(currentSound);
            currentSound = null;
        }
    }

    public static boolean isPlaying() {
        return currentSound != null;
    }
}
