package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.magic.attacks.*;

import java.util.function.Supplier;

public class ModMagicsRM {

    static int order = 11;

    //The Command
    public static DeferredRegister<Magic> MAGIC = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "magics"), KingdomKeysReMind.MODID);

    //Normal Spells
    public static final Supplier<Magic>
            HASTE = MAGIC.register(ResourceLocation.parse("magic_haste").getPath(), () -> new magicHaste(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_haste"), true, 3, null)),
            SLOW = MAGIC.register(ResourceLocation.parse("magic_slow").getPath(), () -> new magicSlow(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_slow"), false, 3, null)),
            HOLY = MAGIC.register(ResourceLocation.parse("magic_holy").getPath(),() -> new magicHoly(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_holy"), false, 3, null)),
            RUIN = MAGIC.register(ResourceLocation.parse("magic_ruin").getPath(), () -> new magicRuin(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_ruin"), false, 3, null)),
            BALLOON = MAGIC.register(ResourceLocation.parse("magic_balloon").getPath(), () -> new magicBalloon(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_balloon"), false, 3, null)),
            ULTIMA = MAGIC.register(ResourceLocation.parse("magic_ultima").getPath(), () -> new magicUltima(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_ultima"), false, 1, null)),
            COMET = MAGIC.register(ResourceLocation.parse("magic_comet").getPath(), () -> new magicComet( ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_comet"), false, 2, null)),
            BERSERK = MAGIC.register(ResourceLocation.parse("magic_berserk").getPath(), () -> new magicBerserk( ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "magic_berserk"), true, 3, null)),
            AUTO_LIFE = MAGIC.register(ResourceLocation.parse("magic_auto-life").getPath(),() -> new magicAutoLife( ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"magic_auto-life"), true, 1, null)),
            OSMOSE = MAGIC.register(ResourceLocation.parse("magic_osmose").getPath(),() -> new magicOsmose( ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"magic_osmose"), false, 3, null)),
            DRAIN = MAGIC.register(ResourceLocation.parse("magic_drain").getPath(),() -> new magicDrain( ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"magic_drain"), false, 3, null)),
            SILENCE = MAGIC.register(ResourceLocation.parse("magic_silence").getPath(), () -> new magicSilence(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"magic_silence"), false,3, null)),
            WARP = MAGIC.register(ResourceLocation.parse("magic_warp").getPath(), () -> new magicWarp(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"magic_warp"), false, 1, null)),
            ESUNA = MAGIC.register(ResourceLocation.parse("magic_esuna").getPath(), () -> new magicEsuna(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"magic_esuna"), true, 1, null)),
            DISPEL = MAGIC.register(ResourceLocation.parse("magic_dispel").getPath(), () -> new magicDispel(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"magic_dispel"), false, 1, null)),
            REGEN = MAGIC.register(ResourceLocation.parse("magic_regen").getPath(), () -> new magicRegen(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"magic_regen"), true, 3, null)),
            FAITH = MAGIC.register(ResourceLocation.parse("magic_faith").getPath(), () -> new magicFaith(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"magic_faith"), 1, null)),
            DEATH = MAGIC.register(ResourceLocation.parse("magic_death").getPath(), () -> new magicDeath(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"magic_death"), false, 1, null)),
            SPARK = MAGIC.register(ResourceLocation.parse("magic_spark").getPath(), () -> new magicSpark(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"magic_spark"), false, 3, null)),
            MINE_SQUARE = MAGIC.register(ResourceLocation.parse("magic_mine_square").getPath(), () -> new magicMineSquare(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"magic_mine_square"), false, 4, null)),
            MINE_SHIELD = MAGIC.register(ResourceLocation.parse("magic_mine_shield").getPath(), () -> new magicMineShield(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"magic_mine_shield"), false, 4, null)),
            CONFUSE = MAGIC.register(ResourceLocation.parse("magic_confuse").getPath(), () -> new magicConfuse(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"magic_confuse"), false, 3, null)),


    // "Spells"
            STEAL = MAGIC.register(ResourceLocation.parse("magic_steal").getPath(), () -> new magicSteal(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"magic_steal"), false, 1, null)),
        //TODO: Add BBS/DDD Attack Commands and FF Related Commands
            QUICK_BLITZ = MAGIC.register(ResourceLocation.parse("attack_quick_blitz").getPath(),() -> new attackQuickBlitz(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"attack_quick_blitz"), false, 3, null)),
            SLIDING_DASH = MAGIC.register(ResourceLocation.parse("attack_sliding_dash").getPath(),() -> new attackSlidingDash(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"attack_sliding_dash"), false, 3, null)),
            FIRE_SURGE = MAGIC.register(ResourceLocation.parse("attack_fire_surge").getPath(),() -> new attackFireSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"attack_fire_surge"), false, 3, null)),
            THUNDER_SURGE = MAGIC.register(ResourceLocation.parse("attack_thunder_surge").getPath(),() -> new attackThunderSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"attack_thunder_surge"), false, 3, null)),
            BLIZZARD_SURGE = MAGIC.register(ResourceLocation.parse("attack_blizzard_surge").getPath(),() -> new attackBlizzardSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"attack_blizzard_surge"), false, 3, null)),
            WATER_SURGE = MAGIC.register(ResourceLocation.parse("attack_water_surge").getPath(),() -> new attackWaterSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"attack_water_surge"), false, 3, null)),
            AERO_SURGE = MAGIC.register(ResourceLocation.parse("attack_aero_surge").getPath(),() -> new attackAeroSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"attack_aero_surge"), false, 3, null)),
            LIGHT_SURGE = MAGIC.register(ResourceLocation.parse("attack_light_surge").getPath(),() -> new attackLightSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"attack_light_surge"), false, 3, null)),
            DARK_SURGE = MAGIC.register(ResourceLocation.parse("attack_dark_surge").getPath(),() -> new attackDarkSurge(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"attack_dark_surge"), false, 3, null)),
            ZANTETSUKEN = MAGIC.register(ResourceLocation.parse("attack_zantetsuken").getPath(),() -> new attackZantetsuken(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"attack_zantetsuken"), false, 3, null)),
            SWIFT_STRIKE = MAGIC.register(ResourceLocation.parse("attack_swift_strike").getPath(),() -> new attackSwiftStrike(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"attack_swift_strike"), false, 1, null)); // SEPHIROTH!






    // Add more magic later...



    }






