package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.KingdomKeysReMind;

public class ModMagicsRM {

    static int order = 11;

    //The Command
    public static DeferredRegister<Magic> MAGIC = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "magics"), "magicksaddon");

    //Normal Spells
    public static final RegistryObject<Magic>
            HASTE = MAGIC.register("magic_haste", () -> new magicHaste(new ResourceLocation(KingdomKeysReMind.MODID, "magic_haste"), true, 3)),
            SLOW = MAGIC.register("magic_slow", () -> new magicSlow(new ResourceLocation(KingdomKeysReMind.MODID, "magic_slow"), false, 3)),
            HOLY = MAGIC.register("magic_holy",() -> new magicHoly(new ResourceLocation(KingdomKeysReMind.MODID, "magic_holy"), false, 3, null)),
            RUIN = MAGIC.register("magic_ruin", () -> new magicRuin(new ResourceLocation(KingdomKeysReMind.MODID, "magic_ruin"), false, 3, null)),
            BALLOON = MAGIC.register("magic_balloon", () -> new magicBalloon(new ResourceLocation(KingdomKeysReMind.MODID, "magic_balloon"), false, 3)),
            ULTIMA = MAGIC.register("magic_ultima", () -> new magicUltima(new ResourceLocation(KingdomKeysReMind.MODID, "magic_ultima"), false, 1, null)),
            COMET = MAGIC.register("magic_comet", () -> new magicComet(new ResourceLocation(KingdomKeysReMind.MODID, "magic_comet"), false, 2, null)),
            BERSERK = MAGIC.register("magic_berserk", () -> new magicBerserk(new ResourceLocation(KingdomKeysReMind.MODID, "magic_berserk"), true, 3)),
            AUTO_LIFE = MAGIC.register("magic_auto-life",() -> new magicAutoLife(new ResourceLocation(KingdomKeysReMind.MODID,"magic_auto-life"), true, 1)),
            OSMOSE = MAGIC.register("magic_osmose",() -> new magicOsmose(new ResourceLocation(KingdomKeysReMind.MODID,"magic_osmose"), false, 3)),
            DRAIN = MAGIC.register("magic_drain",() -> new magicDrain(new ResourceLocation(KingdomKeysReMind.MODID,"magic_drain"), false, 3)),
            SILENCE = MAGIC.register("magic_silence", () -> new magicSilence(new ResourceLocation(KingdomKeysReMind.MODID,"magic_silence"), false,3)),
            WARP = MAGIC.register("magic_warp", () -> new magicWarp(new ResourceLocation(KingdomKeysReMind.MODID,"magic_warp"), false, 1, null)),
            ESUNA = MAGIC.register("magic_esuna", () -> new magicEsuna(new ResourceLocation(KingdomKeysReMind.MODID,"magic_esuna"), true, 1)),
            DISPEL = MAGIC.register("magic_dispel", () -> new magicDispel(new ResourceLocation(KingdomKeysReMind.MODID,"magic_dispel"), false, 1)),
            REGEN = MAGIC.register("magic_regen", () -> new magicRegen(new ResourceLocation(KingdomKeysReMind.MODID,"magic_regen"), false, 3)),
            FAITH = MAGIC.register("magic_faith", () -> new magicFaith(new ResourceLocation(KingdomKeysReMind.MODID,"magic_faith"), 1, null)),
            DEATH = MAGIC.register("magic_death", () -> new magicDeath(new ResourceLocation(KingdomKeysReMind.MODID, "magic_death"), false, 1));






    // Add more magic later...



    }





