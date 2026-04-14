package online.remind.remind.capabilities;


import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.lib.StringsRM;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class GlobalDataRM implements INBTSerializable<CompoundTag> {


    private final List<String> situationSpells = new ArrayList<>();
    private int hasteTicks;
    private int hasteLevel;
    private int slowTicks;
    private int slowLevel;
    private int berserkLevel;
    private int berserkTicks;
    private int isAutoLifeActive;
    private int prestigeLvl;
    private int strBonus;
    private int magBonus;
    private int defBonus;
    private int NGPlusWarriorCount;
    private int NGPlusMysticCount;
    private int NGPlusGuardianCount;
    private int lastHpBoostBonus;
    private int lastMpBoostBonus;
    private int strPanel;
    private int magPanel;
    private int defPanel;
    private String panelChoice;
    private int panelsStatus;
    private int ngpStatus;
    private int darkModeEXP;
    private int lightFormEXP;
    private int rageFormEXP;
    private int darkModeLvl;
    private int lightFormLvl;
    private int rageFormLvl;
    private int stepTicks;
    private byte stepType;
    private int RCCooldown;
    private int SCooldown;
    private int CanCounter;
    private double situationValue;
    private String style = "";
    private int styleTicks;
    private int MPOG;
    private int riskchargeCount;
    private boolean dreamEaterSummoned = false;
    private UUID dreamEaterUUID = new UUID(0L, 0L);
    private String dreamEaterRL = KingdomKeysReMind.MODID + ":" + StringsRM.none;
    private boolean donorGiven;
    private boolean darkMode;

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag storage = new CompoundTag();
        storage.putInt("haste_ticks", this.getHasteTicks());
        storage.putInt("haste_level", this.getHasteLevel());
        storage.putInt("slow_ticks", this.getSlowTicks());
        storage.putInt("slow_level", this.getSlowLevel());
        storage.putInt("berserk_ticks", this.getBerserkTicks());
        storage.putInt("berserk_level", this.getBerserkLevel());
        storage.putInt("autolife_active", this.getAutoLifeActive());
        storage.putInt("can_counter", this.getCanCounter());

        // New Game Plus NBT
        storage.putInt("prestige_level", this.getPrestigeLvl());

        storage.putInt("NGPlus_STR_Bonus", this.getSTRBonus());
        storage.putInt("NGPlus_MAG_Bonus", this.getMAGBonus());
        storage.putInt("NGPlus_DEF_Bonus", this.getDEFBonus());

        storage.putInt("NGPlus_Warrior", this.getNGPWarriorCount());
        storage.putInt("NGPlus_Mystic", this.getNGPMysticCount());
        storage.putInt("NGPlus_Guardian", this.getNGPGuardianCount());

        storage.putInt("lastHpBoostBonus", this.getLastHpBoostBonus());
        storage.putInt("lastMpBoostBonus", this.getLastMpBoostBonus());

        storage.putInt("Panels_STR", this.getSTRPanel());
        storage.putInt("Panels_DEF", this.getDEFPanel());
        storage.putInt("Panels_MAG", this.getMAGPanel());

        storage.putInt("Panels_Enabled", this.getPanelsEnabled());
        storage.putInt("NGPlus_Enabled", this.getNGPEnabled());

        //storage.putString("Panels_Choice",this.getPanelChoice().toString());

        storage.putInt("riskcharge_count", this.getRiskchargeCount());

        storage.putDouble("situation_value", this.getSituationValue());

        storage.putString("style", this.getStyle());

        // Dream Eater
        if (dreamEaterUUID != null) {
            storage.putUUID("DreamEaterUUID", this.getDreamEaterUUID());
        }
        storage.putBoolean("dreamEaterSummoned", this.hasDreamEaterSummoned());
        storage.putString("dreamEaterRL", this.getDreamEaterRL());

        // Donor Grant
        storage.putBoolean("donor_grant", this.getDonorGiven());

        return storage;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        CompoundTag properties = nbt;
        this.setHasteTicks(properties.getInt("haste_ticks"), properties.getInt("haste_level"));
        this.setSlowTicks(properties.getInt("slow_ticks"), properties.getInt("slow_level"));
        this.setBerserkTicks(properties.getInt("berserk_ticks"), properties.getInt("berserk_level"));

        this.setPrestigeLvl(properties.getInt("prestige_level"));

        this.setSTRBonus(properties.getInt("NGPlus_STR_Bonus"));
        this.setMAGBonus(properties.getInt("NGPlus_MAG_Bonus"));
        this.setDEFBonus(properties.getInt("NGPlus_DEF_Bonus"));

        this.setNGPWarriorCount(properties.getInt("NGPlus_Warrior"));
        this.setNGPMysticCount(properties.getInt("NGPlus_Mystic"));
        this.setNGPGuardianCount(properties.getInt("NGPlus_Guardian"));

        this.setLastHpBoostBonus(properties.getInt("lastHpBoostBonus"));
        this.setLastMpBoostBonus(properties.getInt("lastMpBoostBonus"));

        this.setSTRPanel(properties.getInt("Panels_STR"));
        this.setMAGPanel(properties.getInt("Panels_MAG"));
        this.setDEFPanel(properties.getInt("Panels_DEF"));

        this.setPanelsEnabled(properties.getInt("Panels_Enabled"));
        this.setNGPEnabled(properties.getInt("NG+_Enabled"));

        this.setRiskchargeCount(properties.getInt("riskcharge_count"));

        this.setCanCounter(properties.getInt("can_counter"));

        this.setSituationValue(properties.getDouble("situation_value"));
        this.setStyle(properties.getString("style"));

        this.setDonorGiven(properties.getBoolean("donor_grant"));
        if (nbt.contains("DreamEaterUUID")) {
            this.setDreamEaterUUID(properties.getUUID("DreamEaterUUID"));
        } else {
            this.dreamEaterUUID = null;
        }
        this.setDreamEaterRL(properties.getString("dreamEaterRL"));

        // this.setPanelChoice(properties.getString("Panels_Choice"));


    }

    //Haste
    public int getHasteLevel() {
        return hasteLevel;
    }

    public void setHasteLevel(int level) {
        this.hasteLevel = level;
    }

    public int getHasteTicks() {
        return hasteTicks;
    }

    public void setHasteTicks(int i, int level) {
        hasteTicks = i;
        hasteLevel = level;
    }

    public void remHasteTicks(int ticks) {
        hasteTicks -= ticks;
    }

    //Slow
    public int getSlowLevel() {

        return slowLevel;
    }

    public void setSlowLevel(int level) {
        this.slowLevel = level;
    }

    public int getSlowTicks() {

        return slowTicks;
    }

    public void setSlowTicks(int i, int level) {
        slowTicks = i;
        slowLevel = level;
    }

    public void remSlowTicks(int ticks) {

        slowTicks -= ticks;
    }

    // Berserk

    public void setSlowCaster(String name) {

    }

    public int getBerserkLevel() {

        return berserkLevel;
    }

    public void setBerserkLevel(int level) {
        this.berserkLevel = level;
    }

    public int getBerserkTicks() {

        return berserkTicks;
    }

    public void setBerserkTicks(int i, int level) {
        berserkTicks = i;
        berserkLevel = level;

    }

    // Auto-Life

    public void remBerserkTicks(int ticks) {

        berserkTicks -= ticks;
    }

    public int setAutoLifeActive(int autoLifeActive) {
        isAutoLifeActive = autoLifeActive;
        return autoLifeActive;
    }

    public void setStepTicks(int i, byte type) {
        stepTicks = i;
        stepType = type;
    }

    public void remStepTicks(int ticks) {
        stepTicks -= ticks;
    }

    public int getStepTicks() {
        return stepTicks;
    }

    public byte getStepType() {
        return stepType;
    }

    public int getRiskchargeCount() {
        return riskchargeCount;
    }

    public void setRiskchargeCount(int i) {
        riskchargeCount = i;
    }

    public int getAutoLifeActive() {
        return isAutoLifeActive;
    }

    public void remAutoLifeActive(int use) {
        isAutoLifeActive -= use;
    }

    public int getPrestigeLvl() {
        return prestigeLvl;
    }

    public void setPrestigeLvl(int i) {
        prestigeLvl = i;
    }

    public void addPrestigeLvl(int i) {
        prestigeLvl += i;
    }

    public int getSTRBonus() {
        return strBonus;
    }

    public void setSTRBonus(int i) {
        strBonus = i;
    }

    public int getMAGBonus() {
        return magBonus;
    }

    public void setMAGBonus(int i) {
        magBonus = i;
    }

    public int getDEFBonus() {
        return defBonus;
    }

    public void setDEFBonus(int i) {
        defBonus = i;
    }

    public void addSTRBonus(int i) {
        strBonus += i;
    }

    public void addMAGBonus(int i) {
        magBonus += i;
    }

    public void addDEFBonus(int i) {
        defBonus += i;
    }

    public int getNGPWarriorCount() {
        return NGPlusWarriorCount;
    }

    public void setNGPWarriorCount(int i) {
        NGPlusWarriorCount = i;
    }

    public int getNGPMysticCount() {
        return NGPlusMysticCount;
    }

    public void setNGPMysticCount(int i) {
        NGPlusMysticCount = i;
    }

    public int getNGPGuardianCount() {
        return NGPlusGuardianCount;
    }

    public void setNGPGuardianCount(int i) {
        NGPlusGuardianCount = i;
    }

    public void addNGPWarriorCount(int i) {
        NGPlusWarriorCount += i;
    }

    public void addNGPMysticCount(int i) {
        NGPlusMysticCount += i;
    }

    public void addNGPGuardianCount(int i) {
        NGPlusGuardianCount += i;
    }

    public int getRCCooldownTicks() {
        return this.RCCooldown;
    }

    public void setRCCooldownTicks(int ticks) {
        this.RCCooldown = ticks;
    }

    public void remRCCooldownTicks(int ticks) {
        this.RCCooldown = Math.max(RCCooldown - ticks, 0);

    }

    public int getLastHpBoostBonus() {
        return lastHpBoostBonus;
    }

    public void setLastHpBoostBonus(int i) {
        lastHpBoostBonus = i;
    }

    public int getLastMpBoostBonus() {
        return lastMpBoostBonus;
    }

    public void setLastMpBoostBonus(int i) {
        lastMpBoostBonus = i;
    }

    public int getSTRPanel() {
        return strPanel;
    }

    public void setSTRPanel(int i) {
        strPanel = i;
    }

    public int getMAGPanel() {
        return magPanel;
    }

    public void setMAGPanel(int i) {
        magPanel = i;
    }

    public int getDEFPanel() {
        return defPanel;
    }

    public void setDEFPanel(int i) {
        defPanel = i;
    }

    public void addSTRPanel(int i) {
        strPanel += i;
    }

    public void addMAGPanel(int i) {
        magPanel += i;
    }

    public void addDEFPanel(int i) {
        defPanel += i;
    }

    public int getPanelsEnabled() {
        return panelsStatus;
    }

    public void setPanelsEnabled(int i) {
        panelsStatus = i;
    }

    public int getNGPEnabled() {
        return ngpStatus;
    }

    public void setNGPEnabled(int i) {
        ngpStatus = i;
    }

    public String getPanelChoice() {
        return panelChoice;
    }

    public void setPanelChoice(String choice) {
        panelChoice = choice;
    }

    public double getMPOG() {
        return MPOG;
    }

    public void setMPOG(int i) {
        this.MPOG = i;
    }

    public int getCanCounter() {
        return CanCounter;
    }

    public void setCanCounter(int i) {
        CanCounter = i;
    }

    public void remCanCounter(int use) {
        CanCounter -= use;
    }

    public boolean hasDreamEaterSummoned() {
        return dreamEaterSummoned;
    }

    public void setHasDreamEaterSummoned(boolean val) {
        this.dreamEaterSummoned = val;
    }

    public UUID getDreamEaterUUID() {
        return dreamEaterUUID;
    }

    public void setDreamEaterUUID(UUID uuid) {
        this.dreamEaterUUID = uuid;
    }

    public String getDreamEaterRL() {
        return this.dreamEaterRL;
    }

    public void setDreamEaterRL(String i) {
        dreamEaterRL = i;
    }

    public boolean getDonorGiven() {
        return donorGiven;
    }

    public void setDonorGiven(boolean i) {
        donorGiven = i;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean value) {
        this.darkMode = value;
    }

    public double getSituationValue() {
        return situationValue;
    }

    public void setSituationValue(double i) {
        this.situationValue = i;
    }

    public void remSituationValue(double i) {
        situationValue -= i;
    }

    public List<String> getSituationSpells() {
        return situationSpells;
    }

    public void addSituationSpell(String spell) {
        situationSpells.add(spell);
    }

    public void clearSituationSpells() {
        situationSpells.clear();
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }


    public int getSCooldownTicks() {
        return this.SCooldown;
    }


    public void setSCooldownTicks(int ticks) {
        this.SCooldown = ticks;
    }


    public void remSCooldownTicks(int ticks) {
        this.SCooldown = Math.max(SCooldown - ticks, 0);

    }


    public int getStyleTicks() {
        return this.styleTicks;
    }

    public void setStyleTicks(int ticks) {
        this.styleTicks = ticks;
    }

    public void remStyleTicks(int ticks) {
        this.styleTicks = Math.max(styleTicks - ticks, 0);
    }


}
