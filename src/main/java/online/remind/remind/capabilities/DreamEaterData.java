package online.remind.remind.capabilities;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class DreamEaterData implements INBTSerializable<CompoundTag> {

    protected DreamEaterData() {}

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider){
        CompoundTag storage = new CompoundTag();
        storage.putInt("level", this.getSpiritLevel());
        storage.putInt("experience", this.getSpiritExperience());
    }
}
