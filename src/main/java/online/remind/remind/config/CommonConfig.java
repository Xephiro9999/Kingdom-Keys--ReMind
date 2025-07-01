package online.remind.remind.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {

    public ModConfigSpec.BooleanValue donorKeybladeGrant;

    public ModConfigSpec.DoubleValue rageFormPercent;

    // NG+
    public ModConfigSpec.BooleanValue ngpEnabled;
    public ModConfigSpec.IntValue statCap;
    public ModConfigSpec.IntValue statBonus;
    public ModConfigSpec.IntValue hpCap;
    public ModConfigSpec.IntValue mpCap;

    // Panels
    public ModConfigSpec.BooleanValue panelsEnabled;
    public ModConfigSpec.IntValue panelBonus;
    public ModConfigSpec.IntValue panelLimit;





    CommonConfig(final ModConfigSpec.Builder builder) {
        builder.push("General");

        donorKeybladeGrant = builder
                .comment("Enables Donators to get commissioned keyblades upon first join. True by Default.")
                .define("Give Donors Keyblades", true);

        builder.pop();
        builder.push("Forms");

        rageFormPercent = builder
                .comment("Changes the base chance for Rage Form's Reaction Command to appear. Setting this to 0 will disable the Reaction Command.")
                .comment("Default: 10.0.")
                .defineInRange("Base Rage Form Chance", 10.0,0,100);

        builder.pop();
        builder.push("NG+");

        ngpEnabled = builder
                .comment("Dictates if New Game + is enabled or not.")
                .comment("Default: true")
                .define("New Game + Enabled", true);
        statCap = builder
                .comment("Sets the maximum NG+ can give you stat wise. (This excludes HP and MP)")
                .comment("Default: 50")
                .defineInRange("Stat Cap:", 50, 0, 9999);
        statBonus = builder
                .comment("Sets the stat bonus per NG+ cycle.")
                .comment("Default: 1")
                .defineInRange("Stat Bonus:", 0, 0, 9999);
        hpCap = builder
                .comment("Sets the maximum HP that NG+ can give you.")
                .comment("Default: 100")
                .defineInRange("HP Cap:", 100, 0, 9999);
        mpCap = builder
                .comment("Sets the maximum MP that NG+ can give you. WARNING! Setting this too high will break the balance of certain spells!")
                .comment("Default: 100")
                .defineInRange("MP Cap:", 100, 0, 9999);
        builder.pop();
        builder.push("Panels");

        panelsEnabled = builder
                .comment("Dictates if Panels are enabled or not. NOTE: If false, you will get rid of the only way for Org members to level up forms aside from gathering the orbs.")
                .comment("Default: true")
                .define("Panels Enabled", true);
        panelBonus = builder
                .comment("Sets the stat bonus for STR, MAG, and DEF in the panels menu. Note: Setting this to 0 will disable the boost entirely, and setting this too high can/will break balance!")
                .comment("Default: 1")
                .defineInRange("Panel Stat Bonus:", 1, 0, 9999);
        panelLimit = builder
                .comment("Sets the max stats given by the Panel System. Note: High numbers can/will break balance!")
                .comment("Default: 50")
                .defineInRange("Panels Cap:", 50, 1, 9999);
    }
}
