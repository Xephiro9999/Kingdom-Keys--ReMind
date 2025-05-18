package online.remind.remind.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

    public ForgeConfigSpec.BooleanValue donorKeybladeGrant;

    CommonConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");

        donorKeybladeGrant = builder
                .comment("Enables Donators to get commissioned keyblades upon first join. True by Default.")
                .define("donorKeybladeGrant", true);

        builder.pop();
    }
}
