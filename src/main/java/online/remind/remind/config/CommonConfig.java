package online.remind.remind.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {

    public ModConfigSpec.BooleanValue donorKeybladeGrant;
    public ModConfigSpec.DoubleValue rageFormPercent;


    CommonConfig(final ModConfigSpec.Builder builder) {
        builder.push("general");

        donorKeybladeGrant = builder
                .comment("Enables Donators to get commissioned keyblades upon first join. True by Default.")
                .define("donorKeybladeGrant", true);

        builder.pop();

        builder.push("balance");

        rageFormPercent = builder
                .comment("Changes the base chance for Rage Form's Reaction Command to appear. Setting this to 0 will disable the Reaction Command.")
                .comment("Default: 10.0.")
                .defineInRange("rageFormPercent", 10.0,0,100);

        builder.pop();
    }
}
