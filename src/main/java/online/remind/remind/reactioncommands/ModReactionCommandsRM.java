package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionMagic;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.lib.StringsRM;

import java.util.function.Supplier;

public class ModReactionCommandsRM {
    public static DeferredRegister<ReactionCommand> REACTION_COMMANDS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "reactioncommands"), KingdomKeysReMind.MODID);


    public static final Supplier<ReactionCommand>
        RISKCHARGE = REACTION_COMMANDS.register(ResourceLocation.parse(StringsRM.RCMA_Prefix+"riskcharge").getPath(), () -> new RiskchargeReaction(ResourceLocation.parse(StringsRM.riskchargeRC), true)),
        RAGING_BURST = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"raging_burst", () -> new RagingBurstRC(ResourceLocation.parse(StringsRM.ragingBurst), true)),
        TEST_ORG = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"test", () -> new TestReaction( ResourceLocation.parse(StringsRM.testRC),false)),
        LIGHT_BEAM = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"light_beam", () -> new LightBeamRC( ResourceLocation.parse(StringsRM.LightBeamRC), true)),
        DARK_MINE_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"dark_mine", () -> new DarkMineRC( ResourceLocation.parse(StringsRM.DarkMineRC),true)),
        DUAL_SHOT_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"dual_shot", () -> new DualShotRC( ResourceLocation.parse(StringsRM.DualShotRC),true)),
        TWILIGHT_FORM = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"twilight", () -> new TwilightFormRC( ResourceLocation.parse(StringsRM.TwilightRC),true)),
        RAGE_FORM = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"rage", () -> new RageFormRC(ResourceLocation.parse(StringsRM.RageRC),true)),
        DARK_FIRAGA_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"dark_firaga", () -> new DarkFiragaRC( ResourceLocation.parse(StringsRM.DarkFiragaRC),true)),
        XEMNAS_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"xemnas", () -> new XemnasRC( ResourceLocation.parse(StringsRM.XemnasRC),true)),
        ZEXION_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"zexion", () -> new XemnasRC( ResourceLocation.parse(StringsRM.ZexionRC),true)),


        FINISH_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"finish", () -> new FinishRC( ResourceLocation.parse(StringsRM.FinishRC),true)),
        FIRESTORM_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"firestorm", () -> new FirestormRC( ResourceLocation.parse(StringsRM.FireStormRC),true)),
        DIAMOND_DUST_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"diamond_dust", () -> new DiamondDustRC( ResourceLocation.parse(StringsRM.DiamondDustRC),true)),
        THUNDER_BOLT_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"thunder_bolt", () -> new ThunderBoltRC( ResourceLocation.parse(StringsRM.ThunderBoltRC),true)),
        FEVER_PITCH_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"fever_pitch", () -> new FeverPitchRC( ResourceLocation.parse(StringsRM.FeverPitchRC),true)),
        CRITICAL_IMPACT_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"critical_impact", () -> new CriticalImpactRC( ResourceLocation.parse(StringsRM.CriticalImpactRC),true)),
        SPELLWEAVER_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"spellweaver", () -> new SpellweaverRC( ResourceLocation.parse(StringsRM.SpellweaverRC),true)),



    // Commission RCs

        REGEN_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"regen", () -> new RegenRC( ResourceLocation.parse(StringsRM.RegenRC),true)),


    // Reprisals

        COUNTER_HAMMER = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"counter_hammer", () -> new CounterHammerRC( ResourceLocation.parse(StringsRM.CounterHammerRC), true)),
        COUNTER_BLAST = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"counter_blast", () -> new CounterBlastRC( ResourceLocation.parse(StringsRM.CounterBlastRC), true)),
        COUNTER_RUSH = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"counter_rush", () -> new CounterRushRC( ResourceLocation.parse(StringsRM.CounterRushRC), true));

}







