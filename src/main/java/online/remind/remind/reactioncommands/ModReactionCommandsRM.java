package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.remind.remind.lib.StringsRM;

public class ModReactionCommandsRM {
    public static DeferredRegister<ReactionCommand> REACTION_COMMANDS = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "reactioncommands"), "magicksaddon");

    public static final RegistryObject<ReactionCommand>
        RISKCHARGE = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"riskcharge", () -> new RiskchargeReaction(new ResourceLocation(StringsRM.riskchargeRC), true)),
        RAGING_BURST = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"raging_burst", () -> new RagingBurstRC(new ResourceLocation(StringsRM.ragingBurst), true)),
        TEST_ORG = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"test", () -> new TestReaction(new ResourceLocation(StringsRM.testRC),false)),
        LIGHT_BEAM = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"light_beam", () -> new LightBeamRC(new ResourceLocation(StringsRM.LightBeamRC), true)),
        DARK_MINE_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"dark_mine", () -> new DarkMineRC(new ResourceLocation(StringsRM.DarkMineRC),true)),
        DUAL_SHOT_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"dual_shot", () -> new DualShotRC(new ResourceLocation(StringsRM.DualShotRC),true)),
        TWILIGHT_FORM = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"twilight", () -> new TwilightFormRC(new ResourceLocation(StringsRM.TwilightRC),true)),
        RAGE_FORM = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"rage", () -> new RageFormRC(new ResourceLocation(StringsRM.RageRC),true)),
        DARK_FIRAGA_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"dark_firaga", () -> new DarkFiragaRC(new ResourceLocation(StringsRM.DarkFiragaRC),true)),
        XEMNAS_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"xemnas", () -> new XemnasRC(new ResourceLocation(StringsRM.XemnasRC),true)),

        // Reprisals

        COUNTER_HAMMER = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"counter_hammer", () -> new CounterHammerRC(new ResourceLocation(StringsRM.CounterHammerRC), true)),
        COUNTER_BLAST = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"counter_blast", () -> new CounterBlastRC(new ResourceLocation(StringsRM.CounterBlastRC), true)),
        COUNTER_RUSH = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"counter_rush", () -> new CounterRushRC(new ResourceLocation(StringsRM.CounterRushRC), true));

}







