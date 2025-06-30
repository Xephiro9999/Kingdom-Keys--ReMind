package online.remind.remind.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

    public ForgeConfigSpec.BooleanValue donorKeybladeGrant;

    public ForgeConfigSpec.DoubleValue rageFormPercent;

    // NG+
    public ForgeConfigSpec.IntValue statCap;
    public ForgeConfigSpec.IntValue statBonus;
    public ForgeConfigSpec.IntValue hpCap;
    public ForgeConfigSpec.IntValue mpCap;




    CommonConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("General");

        donorKeybladeGrant = builder
                .comment("Enables Donators to get commissioned keyblades upon first join. True by Default.")
                .define("donorKeybladeGrant", true);

        builder.pop();
        builder.push("Form Balance");

        rageFormPercent = builder
                .comment("Changes the base chance for Rage Form's Reaction Command to appear. Setting this to 0 will disable the Reaction Command.")
                .comment("Default: 10.0.")
                .defineInRange("rageFormPercent", 10.0,0,100);

        builder.pop();
        builder.push("NG+ Balance");

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
    }
}
