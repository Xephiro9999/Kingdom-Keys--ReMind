package online.remind.remind.capabilities;


import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.List;
import java.util.UUID;

public interface IGlobalDataRM extends INBTSerializable<CompoundTag> {

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

    int getLastHpBoostBonus();
    int getLastMpBoostBonus();
    void setLastHpBoostBonus(int i);
    void setLastMpBoostBonus(int i);

    void addSTRPanel(int i);
    void addMAGPanel(int i);
    void addDEFPanel(int i);


    int getSTRPanel();
    int getMAGPanel();
    int getDEFPanel();

    void setSTRPanel(int i);
    void setMAGPanel(int i);
    void setDEFPanel(int i);

    void setPanelsEnabled(int i);
    void setNGPEnabled(int i);

    int getPanelsEnabled();
    int getNGPEnabled();

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

    boolean isDarkMode();
    void setDarkMode(boolean value);

    double getSituationValue();
    void setSituationValue(double i);
    void remSituationValue(double i);

    List<String> getSituationSpells();
    void addSituationSpell(String spell);
    void clearSituationSpells();

    String getStyle();
    void setStyle(String style);




    // Dream Eater Stuff
    boolean hasDreamEaterSummoned();
    void setHasDreamEaterSummoned(boolean val);

    UUID getDreamEaterUUID();
    void setDreamEaterUUID(UUID uuid);

    String getDreamEaterRL();
    void setDreamEaterRL(String s);

    // Donor Item Grant
    boolean getDonorGiven();
    void setDonorGiven(boolean i);


}
