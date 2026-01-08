package online.remind.remind.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class DreamEaterMenuSound extends AbstractTickableSoundInstance {

    private static final float MAX_VOLUME  = 1.0f;
    private static final float FADE_SPEED = 0.02F;

    private boolean fadingIn = true;
    private boolean fadingOut = false;

    public DreamEaterMenuSound(SoundEvent sound){
        super(sound, SoundSource.MASTER, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.volume = 0.5f;
        this.pitch = 1.0f;
        this.relative = true;
        this.attenuation = Attenuation.NONE;
    }


    @Override
    public void tick() {

    }

    public void fadeOut() {
        fadingOut = true;
        fadingIn = false;
    }
}
