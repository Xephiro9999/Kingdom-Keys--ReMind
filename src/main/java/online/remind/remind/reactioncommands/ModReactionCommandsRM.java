package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
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
        RAGE_FORM = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"rage", () -> new RageFormRC(ResourceLocation.parse(StringsRM.RageRC),false)),
        DARK_FIRAGA_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"dark_firaga", () -> new DarkFiragaRC( ResourceLocation.parse(StringsRM.DarkFiragaRC),true)),
        XEMNAS_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"xemnas", () -> new XemnasRC( ResourceLocation.parse(StringsRM.XemnasRC),true)),
        ZEXION_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"zexion", () -> new XemnasRC( ResourceLocation.parse(StringsRM.ZexionRC),true)),


        FINISH_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"finish", () -> new FinishRC(ResourceLocation.parse(StringsRM.FinishRC),false)),
        FIRESTORM_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"firestorm", () -> new StyleRC(ResourceLocation.parse(StringsRM.FireStormRC), false, KingdomKeysReMind.MODID+":"+StringsRM.fireStorm)),
        DIAMOND_DUST_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"diamond_dust", () -> new StyleRC(ResourceLocation.parse(StringsRM.DiamondDustRC),false, KingdomKeysReMind.MODID+":"+StringsRM.diamondDust)),
        THUNDER_BOLT_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"thunder_bolt", () -> new StyleRC(ResourceLocation.parse(StringsRM.ThunderBoltRC),false, KingdomKeysReMind.MODID+":"+StringsRM.thunderBolt)),
        FEVER_PITCH_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"fever_pitch", () -> new StyleRC(ResourceLocation.parse(StringsRM.FeverPitchRC),false, KingdomKeysReMind.MODID+":"+StringsRM.feverPitch)),
        CRITICAL_IMPACT_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"critical_impact", () -> new StyleRC(ResourceLocation.parse(StringsRM.CriticalImpactRC),false, KingdomKeysReMind.MODID+":"+StringsRM.criticalImpact)),
        SPELLWEAVER_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"spellweaver", () -> new StyleRC(ResourceLocation.parse(StringsRM.SpellweaverRC),false, KingdomKeysReMind.MODID+":"+StringsRM.spellweaver)),



    // Commission RCs

        REGEN_RC = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"regen", () -> new RegenRC( ResourceLocation.parse(StringsRM.RegenRC),true)),


    // Reprisals

        COUNTER_HAMMER = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"counter_hammer", () -> new CounterHammerRC( ResourceLocation.parse(StringsRM.CounterHammerRC), true)),
        COUNTER_BLAST = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"counter_blast", () -> new CounterBlastRC( ResourceLocation.parse(StringsRM.CounterBlastRC), true)),
        COUNTER_RUSH = REACTION_COMMANDS.register(StringsRM.RCMA_Prefix+"counter_rush", () -> new CounterRushRC( ResourceLocation.parse(StringsRM.CounterRushRC), true));

}







