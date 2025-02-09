package online.remind.remind.capabilities;


import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface IGlobalCapabilitiesRM extends INBTSerializable<CompoundTag> {

    //Haste
    int getHasteLevel();
    void setHasteLevel(int level);
    int getHasteTicks();
    void setHasteTicks(int i, int level);
    void remHasteTicks(int ticks);

    //Slow
    int getSlowLevel();
    void setSlowLevel(int level);
    void setSlowTicks(int i, int level);
    int getSlowTicks();
    void remSlowTicks(int ticks);
    void setSlowCaster(String name);

    //Berserk
    int getBerserkLevel();
    void setBerserkLevel(int level);
    int getBerserkTicks();
    void setBerserkTicks(int i, int level);
    void remBerserkTicks(int ticks);
    
    //AutoLife
    int setAutoLifeActive(int i);
    int getAutoLifeActive();
    void remAutoLifeActive(int use);

    // Light/Dark Step SFX Ticks
    void setStepTicks(int i, byte type);
    void remStepTicks(int ticks);
    int getStepTicks();
    byte getStepType();

    // Rage Form
    void setRiskchargeCount(int i);
    int getRiskchargeCount();

    //Prestige
    int getPrestigeLvl();
    void addPrestigeLvl(int i);
    void setPrestigeLvl(int i);

    int getSTRBonus();
    int getMAGBonus();
    int getDEFBonus();

    void setSTRBonus(int i);
    void setMAGBonus(int i);
    void setDEFBonus(int i);

    void addSTRBonus(int i);
    void addMAGBonus(int i);
    void addDEFBonus(int i);

    int getNGPWarriorCount();
    int getNGPMysticCount();
    int getNGPGuardianCount();

    void setNGPWarriorCount(int i);
    void setNGPMysticCount(int i);
    void setNGPGuardianCount(int i);

    void addNGPWarriorCount(int i);
    void addNGPMysticCount(int i);
    void addNGPGuardianCount(int i);

    void addSTRPanel(int i);
    void addMAGPanel(int i);
    void addDEFPanel(int i);


    int getSTRPanel();
    int getMAGPanel();
    int getDEFPanel();

    void setSTRPanel(int i);
    void setMAGPanel(int i);
    void setDEFPanel(int i);

    int getRCCooldownTicks();
    void setRCCooldownTicks(int ticks);
    void remRCCooldownTicks(int ticks);
    String getPanelChoice();

    void setPanelChoice(String choice);

    double getMPOG();
    void setMPOG(int i);

    int getCanCounter();
    void setCanCounter(int i);
    void remCanCounter(int use);



    // Dream Eater Stuff


    boolean hasDreamEaterSummoned();
    void setHasDreamEaterSummoned(boolean val);

    int getDreamEaterSummonedID();

    void setDreamEaterSummonedID(int i);


}
