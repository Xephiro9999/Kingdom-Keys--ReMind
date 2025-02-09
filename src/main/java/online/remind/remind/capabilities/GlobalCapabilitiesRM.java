package online.remind.remind.capabilities;


import net.minecraft.nbt.CompoundTag;


public class GlobalCapabilitiesRM implements IGlobalCapabilitiesRM {



    @Override
    public CompoundTag serializeNBT() {
        CompoundTag storage = new CompoundTag();
        storage.putInt("haste_ticks", this.getHasteTicks());
        storage.putInt("haste_level", this.getHasteLevel());
        storage.putInt("slow_ticks", this.getSlowTicks());
        storage.putInt("slow_level", this.getSlowLevel());
        storage.putInt("berserk_ticks",this.getBerserkTicks());
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

        storage.putInt("Panels_STR", this.getSTRPanel());
        storage.putInt("Panels_DEF", this.getDEFPanel());
        storage.putInt("Panels_MAG", this.getMAGPanel());

        //storage.putString("Panels_Choice",this.getPanelChoice().toString());

        storage.putInt("riskcharge_count", this.getRiskchargeCount());

        // Dream Eater

        storage.putBoolean("dreamEaterSummoned", this.hasDreamEaterSummoned());
        storage.putInt("dreamEaterSummonedID", this.getDreamEaterSummonedID());


        return storage;
    }

	@Override
    public void deserializeNBT(CompoundTag nbt) {
        CompoundTag properties = (CompoundTag) nbt;
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

        this.setSTRPanel(properties.getInt("Panels_STR"));
        this.setMAGPanel(properties.getInt("Panels_MAG"));
        this.setDEFPanel(properties.getInt("Panels_DEF"));

        this.setRiskchargeCount(properties.getInt("riskcharge_count"));

        this.setCanCounter(properties.getInt("can_counter"));

        this.setPanelChoice(properties.getString("Panels_Choice"));

    }

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

    private int strPanel;
    private int magPanel;
    private int defPanel;
    private String panelChoice;

    private int darkModeEXP;
    private int lightFormEXP;
    private int rageFormEXP;
    private int darkModeLvl;
    private int lightFormLvl;
    private int rageFormLvl;

    private int stepTicks;
    private byte stepType;

    private int RCCooldown;
    private int CanCounter;

    private int MPOG;

    private int riskchargeCount;

    private boolean dreamEaterSummoned = false;
    private int dreamEaterSummonedID;

    //Haste
    public int getHasteLevel() {

        return hasteLevel;
    }
    @Override
    public void setHasteLevel(int level) {

        this.hasteLevel = level;
    }
    @Override
    public int getHasteTicks() {

        return hasteTicks;
    }
    @Override
    public void setHasteTicks(int i, int level) {
            hasteTicks = i;
            hasteLevel = level;
    }
    @Override
    public void remHasteTicks(int ticks) {

        hasteTicks -= ticks;
    }


    //Slow
    public int getSlowLevel() {

        return slowLevel;
    }

    @Override
    public void setSlowLevel(int level) {
        this.slowLevel = level;
    }

    @Override
    public int getSlowTicks() {

        return slowTicks;
    }
    @Override
    public void setSlowTicks(int i, int level) {
        slowTicks = i;
        slowLevel = level;
    }
    @Override
    public void remSlowTicks(int ticks) {

        slowTicks -= ticks;
    }

    @Override
    public void setSlowCaster(String name) {

    }
    // Berserk
    @Override
    public int getBerserkLevel() {

        return berserkLevel;
    }

    @Override
    public void setBerserkLevel(int level) {
        this.berserkLevel = level;
    }

    @Override
    public int getBerserkTicks() {

        return berserkTicks;
    }

    @Override
    public void setBerserkTicks(int i, int level) {
        berserkTicks = i;
        berserkLevel = level;

    }

    @Override
    public void remBerserkTicks(int ticks) {

        berserkTicks -= ticks;
    }

    // Auto-Life
    @Override
    public int setAutoLifeActive(int autoLifeActive) {
        isAutoLifeActive = autoLifeActive;
        return autoLifeActive;
    }

    @Override
    public void setStepTicks(int i, byte type) {
        stepTicks = i;
        stepType = type;
    }

    @Override
    public void remStepTicks(int ticks) {
        stepTicks -= ticks;
    }

    @Override
    public int getStepTicks(){
        return stepTicks;
    }
    
    @Override
    public byte getStepType(){
        return stepType;
    }

    @Override
    public void setRiskchargeCount(int i) {
        riskchargeCount = i;
    }

    @Override
    public int getRiskchargeCount() {
        return riskchargeCount;
    }

    @Override
    public int getAutoLifeActive() {
        return isAutoLifeActive;
    }

    @Override
    public void remAutoLifeActive(int use) {
        isAutoLifeActive -= use;
    }

    @Override
    public int getPrestigeLvl() {
        return prestigeLvl;
    }

    @Override
    public void addPrestigeLvl(int i) {
        prestigeLvl += i;
    }

    @Override
    public void setPrestigeLvl(int i) {
        prestigeLvl = i;
    }

    @Override
    public int getSTRBonus() {
        return strBonus;
    }

    @Override
    public int getMAGBonus() {
        return magBonus;
    }

    @Override
    public int getDEFBonus() {
        return defBonus;
    }

    @Override
    public void setSTRBonus(int i) {
        strBonus = i;
    }

    @Override
    public void setMAGBonus(int i) {
        magBonus = i;
    }

    @Override
    public void setDEFBonus(int i) {
        defBonus = i;
    }

    @Override
    public void addSTRBonus(int i) {
        strBonus += i;
    }

    @Override
    public void addMAGBonus(int i) {
        magBonus += i;
    }

    @Override
    public void addDEFBonus(int i) {
        defBonus += i;
    }

    @Override
    public int getNGPWarriorCount() {
        return NGPlusWarriorCount;
    }

    @Override
    public int getNGPMysticCount() {
        return NGPlusMysticCount;
    }

    @Override
    public int getNGPGuardianCount() {
        return NGPlusGuardianCount;
    }

    @Override
    public void setNGPWarriorCount(int i) {
        NGPlusWarriorCount = i;
    }

    @Override
    public void setNGPMysticCount(int i) {
        NGPlusMysticCount = i;
    }

    @Override
    public void setNGPGuardianCount(int i) {
        NGPlusGuardianCount = i;
    }

    @Override
    public void addNGPWarriorCount(int i){
        NGPlusWarriorCount += i;
    }
    @Override
    public void addNGPMysticCount(int i){
        NGPlusMysticCount += i;
    }
    @Override
        public void addNGPGuardianCount(int i){
            NGPlusGuardianCount += i;
        }

    @Override
    public int getRCCooldownTicks() {
        return this.RCCooldown;
    }

    @Override
    public void setRCCooldownTicks(int ticks) {
        this.RCCooldown = ticks;
    }

    @Override
    public void remRCCooldownTicks(int ticks) {
        this.RCCooldown = Math.max(RCCooldown - ticks, 0);

    }


    @Override
    public int getSTRPanel() {
        return strPanel;
    }

    @Override
    public int getMAGPanel() {
        return magPanel;
    }

    @Override
    public int getDEFPanel() {
        return defPanel;
    }

    @Override
    public void setSTRPanel(int i) {
        strPanel = i;
    }

    @Override
    public void setMAGPanel(int i) {
        magPanel = i;
    }

    @Override
    public void setDEFPanel(int i) {
        defPanel = i;
    }

    @Override
    public void addSTRPanel(int i) {
        strPanel += i;
    }

    @Override
    public void addMAGPanel(int i) {
        magPanel += i;
    }

    @Override
    public void addDEFPanel(int i) {
        defPanel += i;
    }


    @Override
    public void setPanelChoice(String choice){
        panelChoice = choice;
    }
    @Override
    public String getPanelChoice(){
        return panelChoice;
    }






    @Override
    public double getMPOG() {
        return MPOG;
    }
    @Override
    public void setMPOG(int i) {
        this.MPOG = i;
    }

    @Override
    public int getCanCounter() {
        return CanCounter;
    }

    @Override
    public void setCanCounter(int i) {
        CanCounter = i;
    }

    @Override
    public void remCanCounter(int use) {
        CanCounter -= use;
    }

    @Override
    public boolean hasDreamEaterSummoned() {
        return dreamEaterSummoned;
    }

    @Override
    public void setHasDreamEaterSummoned(boolean val) {
        this.dreamEaterSummoned = val;
    }

    @Override
    public int getDreamEaterSummonedID() {

        return dreamEaterSummonedID;
    }

    @Override
    public void setDreamEaterSummonedID(int i) {

    }


}
