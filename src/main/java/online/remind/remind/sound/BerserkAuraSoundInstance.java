package online.remind.remind.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;

@OnlyIn(Dist.CLIENT)
public class BerserkAuraSoundInstance extends AbstractTickableSoundInstance {
    private final LivingEntity ent;

    public BerserkAuraSoundInstance(LivingEntity ent) {
        super(ModSoundsRM.BERSERK2.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.ent = ent;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
        this.x = (double)((float)ent.getX());
        this.y = (double)((float)ent.getY());
        this.z = (double)((float)ent.getZ());
    }

        public boolean canPlaySound(){
            return true;
        }

    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        if(ent.isRemoved()) {
            this.stop();
        } else {
            if (ModDataRM.getGlobal(ent) != null) {
                if(ModDataRM.getGlobal(ent).getBerserkTicks() <= 0) {
                    this.volume = 0;
                } else {
                    this.x = (double)((float)this.ent.getX());
                    this.y = (double)((float)this.ent.getY());
                    this.z = (double)((float)this.ent.getZ());
                    this.pitch = 1F;
                    this.volume = 1F;
                }
            }
        }
    }
}
