package online.remind.remind.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

    public ForgeConfigSpec.BooleanValue donorKeybladeGrant;
    public ForgeConfigSpec.DoubleValue rageFormPercent;


    CommonConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");

        donorKeybladeGrant = builder
                .comment("Enables Donators to get commissioned keyblades upon first join. True by Default.")
                .define("donorKeybladeGrant", true);


        rageFormPercent = builder
                .comment("Changes the base chance for Rage Form's Reaction Command to appear. Setting this to 0 will disable the Reaction Command.")
                .comment("Default: 10.0.")
                .defineInRange("rageFormPercent", 10.0,0,100);

        builder.pop();
    }
}
