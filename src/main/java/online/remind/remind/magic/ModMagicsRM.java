package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.magic.attacks.*;

import java.util.function.Supplier;

public class ModMagicsRM {

	//The Command
	public static DeferredRegister<Magic> MAGIC = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "magics"), KingdomKeysReMind.MODID);

	//Normal Spells
	public static final Supplier<Magic>
		HASTE = MAGIC.register(ResourceLocation.parse("magic_haste").getPath(), () -> new magicHaste(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_haste"), true, 0, null)),
		HASTERA = MAGIC.register(ResourceLocation.parse("magic_hastera").getPath(), () -> new magicHaste(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_hastera"), true, 1, null)),
		HASTEGA = MAGIC.register(ResourceLocation.parse("magic_hastega").getPath(), () -> new magicHaste(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_hastega"), true, 2, null)),

		// Slow
		SLOW = MAGIC.register(ResourceLocation.parse("magic_slow").getPath(), () -> new magicSlow(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_slow"), false, 0, null)),
		SLOWRA = MAGIC.register(ResourceLocation.parse("magic_slowra").getPath(), () -> new magicSlow(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_slowra"), false, 1, null)),
		SLOWGA = MAGIC.register(ResourceLocation.parse("magic_slowga").getPath(), () -> new magicSlow(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_slowga"), false, 2, null)),

		// Holy
		HOLY = MAGIC.register(ResourceLocation.parse("magic_holy").getPath(), () -> new magicHoly(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_holy"), false, 0, null)),
		HOLYRA = MAGIC.register(ResourceLocation.parse("magic_holyra").getPath(), () -> new magicHoly(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_holyra"), false, 1, null)),
		HOLYGA = MAGIC.register(ResourceLocation.parse("magic_holyga").getPath(), () -> new magicHoly(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_holyga"), false, 2, null)),

		// Ruin
		RUIN = MAGIC.register(ResourceLocation.parse("magic_ruin").getPath(), () -> new magicRuin(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_ruin"), false, 0, null)),
		RUINRA = MAGIC.register(ResourceLocation.parse("magic_ruinra").getPath(), () -> new magicRuin(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_ruinra"), false, 1, null)),
		RUINGA = MAGIC.register(ResourceLocation.parse("magic_ruinga").getPath(), () -> new magicRuin(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_ruinga"), false, 2, null)),

		// Balloon
		BALLOON = MAGIC.register(ResourceLocation.parse("magic_balloon").getPath(), () -> new magicBalloon(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_balloon"), false, 0, null)),
		BALLOONRA = MAGIC.register(ResourceLocation.parse("magic_balloonra").getPath(), () -> new magicBalloon(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_balloonra"), false, 1, null)),
		BALLOONGA = MAGIC.register(ResourceLocation.parse("magic_balloonga").getPath(), () -> new magicBalloon(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_balloonga"), false, 2, null)),

		ULTIMA = MAGIC.register(ResourceLocation.parse("magic_ultima").getPath(), () -> new magicUltima(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_ultima"), false, 0, null)),

		COMET = MAGIC.register(ResourceLocation.parse("magic_comet").getPath(), () -> new magicComet(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_comet"), false, 0, null)),
		METEOR = MAGIC.register(ResourceLocation.parse("magic_meteor").getPath(), () -> new magicComet(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_meteor"), false, 1, null)),

		BERSERK = MAGIC.register(ResourceLocation.parse("magic_berserk").getPath(), () -> new magicBerserk(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_berserk"), true, 0, null)),
		BERSERKRA = MAGIC.register(ResourceLocation.parse("magic_berserkra").getPath(), () -> new magicBerserk(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_berserkra"), true, 1, null)),
		BERSERKGA = MAGIC.register(ResourceLocation.parse("magic_berserkga").getPath(), () -> new magicBerserk(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_berserkga"), true, 2, null)),

		AUTO_LIFE = MAGIC.register(ResourceLocation.parse("magic_auto-life").getPath(), () -> new magicAutoLife(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_auto-life"), true, 0, null)),

		OSMOSE = MAGIC.register(ResourceLocation.parse("magic_osmose").getPath(), () -> new magicOsmose(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_osmose"), false, 0, null)),
		OSMOSERA = MAGIC.register(ResourceLocation.parse("magic_osmosera").getPath(), () -> new magicOsmose(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_osmosera"), false, 1, null)),
		OSMOSEGA = MAGIC.register(ResourceLocation.parse("magic_osmosega").getPath(), () -> new magicOsmose(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_osmosega"), false, 2, null)),

		DRAIN = MAGIC.register(ResourceLocation.parse("magic_drain").getPath(), () -> new magicDrain(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_drain"), false, 0, null)),
		DRAINRA = MAGIC.register(ResourceLocation.parse("magic_drainra").getPath(), () -> new magicDrain(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_drainra"), false, 1, null)),
		DRAINGA = MAGIC.register(ResourceLocation.parse("magic_drainga").getPath(), () -> new magicDrain(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_drainga"), false, 2, null)),

		SILENCE = MAGIC.register(ResourceLocation.parse("magic_silence").getPath(), () -> new magicSilence(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_silence"), false, 0, null)),
		SILENCERA = MAGIC.register(ResourceLocation.parse("magic_silencera").getPath(), () -> new magicSilence(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_silencera"), false, 1, null)),
		SILENCEGA = MAGIC.register(ResourceLocation.parse("magic_silencega").getPath(), () -> new magicSilence(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_silencega"), false, 2, null)),

		WARP = MAGIC.register(ResourceLocation.parse("magic_warp").getPath(), () -> new magicWarp(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_warp"), false, 0, null)),

		ESUNA = MAGIC.register(ResourceLocation.parse("magic_esuna").getPath(), () -> new magicEsuna(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_esuna"), true, 0, null)),
		GROUP_ESUNA = MAGIC.register(ResourceLocation.parse("magic_group_esuna").getPath(), () -> new magicEsuna(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_group_esuna"), true, 1, null)),

		DISPEL = MAGIC.register(ResourceLocation.parse("magic_dispel").getPath(), () -> new magicDispel(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_dispel"), false, 0, null)),

		REGEN = MAGIC.register(ResourceLocation.parse("magic_regen").getPath(), () -> new magicRegen(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_regen"), true, 0, null)),
		REGENRA = MAGIC.register(ResourceLocation.parse("magic_regenra").getPath(), () -> new magicRegen(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_regenra"), true, 1, null)),
		REGENGA = MAGIC.register(ResourceLocation.parse("magic_regenga").getPath(), () -> new magicRegen(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_regenga"), true, 2, null)),

		FAITH = MAGIC.register(ResourceLocation.parse("magic_faith").getPath(), () -> new magicFaith(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_faith"), 0, null)),

		DEATH = MAGIC.register(ResourceLocation.parse("magic_death").getPath(), () -> new magicDeath(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_death"), false, 0, null)),

		// Confuse
		CONFUSE = MAGIC.register(ResourceLocation.parse("magic_confuse").getPath(), () -> new magicConfuse(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_confuse"), false, 0, null)),
		CONFUSERA = MAGIC.register(ResourceLocation.parse("magic_confusera").getPath(), () -> new magicConfuse(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_confusera"), false, 1, null)),
		CONFUSEGA = MAGIC.register(ResourceLocation.parse("magic_confusega").getPath(), () -> new magicConfuse(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_confusega"), false, 2, null)),

		STEAL = MAGIC.register(ResourceLocation.parse("magic_steal").getPath(), () -> new magicSteal(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_steal"), false, 0, null)),

		// TODO: Add BBS/DDD Attack Commands and FF Related Commands

		QUICK_BLITZ = MAGIC.register(ResourceLocation.parse("attack_quick_blitz").getPath(), () -> new attackQuickBlitz(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_quick_blitz"), false, 0, null)),

		SLIDING_DASH = MAGIC.register(ResourceLocation.parse("attack_sliding_dash").getPath(), () -> new attackSlidingDash(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_sliding_dash"), false, 0, null)),

		FIRE_SURGE = MAGIC.register(ResourceLocation.parse("attack_fire_surge").getPath(), () -> new attackFireSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_fire_surge"), false, 0, null)),
		FIRA_SURGE = MAGIC.register(ResourceLocation.parse("attack_fira_surge").getPath(), () -> new attackFireSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_fira_surge"), false, 1, null)),
		FIRAGA_SURGE = MAGIC.register(ResourceLocation.parse("attack_firaga_surge").getPath(), () -> new attackFireSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_firaga_surge"), false, 2, null)),

		THUNDER_SURGE = MAGIC.register(ResourceLocation.parse("attack_thunder_surge").getPath(), () -> new attackThunderSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_thunder_surge"), false, 0, null)),
		THUNDARA_SURGE = MAGIC.register(ResourceLocation.parse("attack_thundara_surge").getPath(), () -> new attackThunderSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_thundara_surge"), false, 1, null)),
		THUNDAGA_SURGE = MAGIC.register(ResourceLocation.parse("attack_thundaga_surge").getPath(), () -> new attackThunderSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_thundaga_surge"), false, 2, null)),

		BLIZZARD_SURGE = MAGIC.register(ResourceLocation.parse("attack_blizzard_surge").getPath(), () -> new attackBlizzardSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_blizzard_surge"), false, 0, null)),
		BLIZZARA_SURGE = MAGIC.register(ResourceLocation.parse("attack_blizzara_surge").getPath(), () -> new attackBlizzardSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_blizzara_surge"), false, 1, null)),
		BLIZZAGA_SURGE = MAGIC.register(ResourceLocation.parse("attack_blizzaga_surge").getPath(), () -> new attackBlizzardSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_blizzaga_surge"), false, 2, null)),

		WATER_SURGE = MAGIC.register(ResourceLocation.parse("attack_water_surge").getPath(), () -> new attackWaterSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_water_surge"), false, 0, null)),
		WATERRA_SURGE = MAGIC.register(ResourceLocation.parse("attack_watera_surge").getPath(), () -> new attackWaterSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_watera_surge"), false, 1, null)),
		WATERGA_SURGE = MAGIC.register(ResourceLocation.parse("attack_waterga_surge").getPath(), () -> new attackWaterSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_waterga_surge"), false, 2, null)),

		AERO_SURGE = MAGIC.register(ResourceLocation.parse("attack_aero_surge").getPath(), () -> new attackAeroSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_aero_surge"), false, 0, null)),
		AERORA_SURGE = MAGIC.register(ResourceLocation.parse("attack_aerora_surge").getPath(), () -> new attackAeroSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_aerora_surge"), false, 1, null)),
		AEROGA_SURGE = MAGIC.register(ResourceLocation.parse("attack_aeroga_surge").getPath(), () -> new attackAeroSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_aeroga_surge"), false, 2, null)),

		LIGHT_SURGE = MAGIC.register(ResourceLocation.parse("attack_light_surge").getPath(), () -> new attackLightSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_light_surge"), false, 0, null)),
		LIGHTRA_SURGE = MAGIC.register(ResourceLocation.parse("attack_lightra_surge").getPath(), () -> new attackLightSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_lightra_surge"), false, 1, null)),
		LIGHTGA_SURGE = MAGIC.register(ResourceLocation.parse("attack_lightga_surge").getPath(), () -> new attackLightSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_lightga_surge"), false, 2, null)),

		DARK_SURGE = MAGIC.register(ResourceLocation.parse("attack_dark_surge").getPath(), () -> new attackDarkSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_dark_surge"), false, 0, null)),
		DARKRA_SURGE = MAGIC.register(ResourceLocation.parse("attack_darkra_surge").getPath(), () -> new attackDarkSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_darkra_surge"), false, 1, null)),
		DARKGA_SURGE = MAGIC.register(ResourceLocation.parse("attack_darkga_surge").getPath(), () -> new attackDarkSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_darkga_surge"), false, 2, null)),

		ZANTETSUKEN = MAGIC.register(ResourceLocation.parse("attack_zantetsuken").getPath(), () -> new attackZantetsuken(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_zantetsuken"), false, 0, null)),

		FIRE_STRIKE = MAGIC.register(ResourceLocation.parse("attack_fire_strike").getPath(), () -> new attackFireStrike(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_fire_strike"), false, 0, null)),

		BLIZZARD_STRIKE = MAGIC.register(ResourceLocation.parse("attack_blizzard_strike").getPath(), () -> new attackBlizzardStrike(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_blizzard_strike"), false, 0, null)),

		THUNDER_STRIKE = MAGIC.register(ResourceLocation.parse("attack_thunder_strike").getPath(), () -> new attackThunderStrike(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_thunder_strike"), false, 0, null)),

		WATER_STRIKE = MAGIC.register(ResourceLocation.parse("attack_water_strike").getPath(), () -> new attackWaterStrike(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_water_strike"), false, 0, null)),

		AERO_STRIKE = MAGIC.register(ResourceLocation.parse("attack_aero_strike").getPath(), () -> new attackAeroStrike(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_aero_strike"), false, 0, null)),

		LIGHT_STRIKE = MAGIC.register(ResourceLocation.parse("attack_light_strike").getPath(), () -> new attackLightStrike(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_light_strike"), false, 0, null)),

		DARK_STRIKE = MAGIC.register(ResourceLocation.parse("attack_dark_strike").getPath(), () -> new attackDarkStrike(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_dark_strike"), false, 0, null)),

		BINDING_STRIKE = MAGIC.register(ResourceLocation.parse("attack_binding_strike").getPath(), () -> new attackBindingStrike(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_binding_strike"), false, 0, null)),

		CONFUSION_STRIKE = MAGIC.register(ResourceLocation.parse("attack_confusion_strike").getPath(), () -> new attackConfusionStrike(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_confusion_strike"), false, 0, null)),

		BLITZ = MAGIC.register(ResourceLocation.parse("attack_blitz").getPath(), () -> new attackBlitz(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_blitz"), false, 0, null)),

		SLOT_EDGE = MAGIC.register(ResourceLocation.parse("attack_slot_edge").getPath(), () -> new attackSlotEdge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_slot_edge"), false, 0, null)),

		SWIFT_STRIKE = MAGIC.register(ResourceLocation.parse("attack_swift_strike").getPath(), () -> new attackSwiftStrike(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "attack_swift_strike"), false, 1, null)); // SEPHIROTH!

	// Add more magic later...


}






