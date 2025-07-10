package online.remind.remind.capabilities;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import online.remind.remind.leveling.DreamEaterStat;
import org.jetbrains.annotations.UnknownNullability;

public interface IDreamEaterData extends INBTSerializable<CompoundTag> {

    // Dream Eater Stats
    int getSpiritLevel();
    void setSpiritLevel(int lvl);

    int getSpiritExperience();
    void setSpiritExperience(int exp);
    void addSpiritExperience(LivingEntity livingEntity, int exp);

    int getSpiritExperienceGiven();
    void setSpiritExperienceGiven(int exp);

    int getSpiritStrength();
    void setSpiritStrength(int str);
    void addSpiritStrength(int str);
    DreamEaterStat getSpiritStrengthStat();
    void setStrengthStat(DreamEaterStat stat);

}
