package online.remind.remind.dreameater;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.entity.ModEntitiesRM;
import online.remind.remind.entity.spirits.CactuarSpiritEntity;
import online.remind.remind.entity.spirits.ChirithyEntity;
import online.remind.remind.entity.spirits.KomoryBatEntity;
import online.remind.remind.entity.spirits.MeowWowEntity;
import online.remind.remind.entity.spirits.TonberrySpiritEntity;
import online.remind.remind.lib.StringsRM;

import java.util.*;
import java.util.function.Supplier;

public final class DreamEaterInfo {

    private static final Map<String, Info> INFO_BY_KEY = new HashMap<>();

    private DreamEaterInfo() {
    }

    public record DreamEaterStats(
            float maxHP,
            float strength,
            float magic,
            float defense
    ) {
    }

    @FunctionalInterface
    public interface StatsFactory {
        DreamEaterStats create(int level, PlayerData playerData);
    }

    @FunctionalInterface
    public interface PreviewFactory {
        LivingEntity create(Level level, Player owner, PlayerData playerData);
    }

    public record Info(
            String registryId,
            String nameKey,
            String displayName,
            int previewScale,
            float expMultiplier,
            StatsFactory statsFactory,
            Supplier<List<DreamEaterLinkData.LinkEntry>> linkSupplier,
            PreviewFactory previewFactory,
            Set<String> aliases
    ) {
    }

    static {
        register(new Info(
                GlobalDataRM.DREAM_EATER_CHIRITHY,
                StringsRM.chirithy,
                "Chirithy",
                45,
                1.20F,
                DreamEaterInfo::chirithyStats,
                DreamEaterLinkData::getChirithyLinks,
                DreamEaterInfo::createChirithyPreview,
                aliases("chirithy", "dreameater_chirithy", "kkremind:chirithy")
        ));

        register(new Info(
                GlobalDataRM.DREAM_EATER_MEOW_WOW,
                StringsRM.meowWow,
                "Meow Wow",
                38,
                1.05F,
                DreamEaterInfo::meowWowStats,
                DreamEaterLinkData::getMeowWowLinks,
                DreamEaterInfo::createMeowWowPreview,
                aliases("meow_wow", "meowwow", "dreameater_meow_wow", "dreameater_meowwow", "kkremind:meow_wow", "kkremind:meowwow")
        ));

        register(new Info(
                GlobalDataRM.DREAM_EATER_KOMORY_BAT,
                StringsRM.komoryBat,
                "Komory Bat",
                44,
                1.05F,
                DreamEaterInfo::komoryBatStats,
                DreamEaterLinkData::getKomoryBatLinks,
                DreamEaterInfo::createKomoryBatPreview,
                aliases("komory_bat", "komorybat", "dreameater_komory_bat", "kkremind:komory_bat", "kkremind:komorybat")
        ));

        register(new Info(
                GlobalDataRM.DREAM_EATER_CACTUAR,
                StringsRM.cactuar,
                "Cactuar",
                44,
                1.25F,
                DreamEaterInfo::cactuarStats,
                DreamEaterLinkData::getCactuarLinks,
                DreamEaterInfo::createCactuarPreview,
                aliases("cactuar", "dreameater_cactuar", "kkremind:cactuar")
        ));

        register(new Info(
                GlobalDataRM.DREAM_EATER_TONBERRY,
                StringsRM.tonberry,
                "Tonberry",
                55,
                1.15F,
                DreamEaterInfo::tonberryStats,
                DreamEaterLinkData::getTonberryLinks,
                DreamEaterInfo::createTonberryPreview,
                aliases("tonberry", "dreameater_tonberry", "kkremind:tonberry")
        ));
    }

    private static Set<String> aliases(String... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    private static void register(Info info) {
        put(info.registryId(), info);
        put(info.nameKey(), info);

        ResourceLocation registryLocation = safeParse(info.registryId());

        if (registryLocation != null) {
            put(registryLocation.getPath(), info);
        }

        for (String alias : info.aliases()) {
            put(alias, info);

            ResourceLocation aliasLocation = safeParse(alias);

            if (aliasLocation != null) {
                put(aliasLocation.getPath(), info);
            }
        }
    }

    private static void put(String key, Info info) {
        String normalized = normalize(key);

        if (!normalized.isEmpty()) {
            INFO_BY_KEY.put(normalized, info);
        }
    }

    public static Info getInfo(DreamEater dreamEater) {
        if (dreamEater == null) {
            return null;
        }

        Info info = getInfo(dreamEater.getName());

        if (info != null) {
            return info;
        }

        if (dreamEater.getRegistryName() != null) {
            info = getInfo(dreamEater.getRegistryName().toString());

            if (info != null) {
                return info;
            }
        }

        return null;
    }

    public static Info getInfo(String dreamEaterRLOrName) {
        return INFO_BY_KEY.get(normalize(dreamEaterRLOrName));
    }

    public static boolean isKnown(DreamEater dreamEater) {
        return getInfo(dreamEater) != null;
    }

    public static boolean isKnown(String dreamEaterRLOrName) {
        return getInfo(dreamEaterRLOrName) != null;
    }

    public static String getDisplayName(DreamEater dreamEater) {
        Info info = getInfo(dreamEater);

        if (info != null) {
            return info.displayName();
        }

        if (dreamEater == null) {
            return "N/A";
        }

        String name = dreamEater.getName();

        if (name == null || name.isEmpty()) {
            return "Dream Eater";
        }

        return prettifyName(name);
    }

    public static String getDisplayName(String dreamEaterRLOrName) {
        Info info = getInfo(dreamEaterRLOrName);

        if (info != null) {
            return info.displayName();
        }

        return prettifyName(dreamEaterRLOrName);
    }

    public static int getPreviewScale(DreamEater dreamEater) {
        Info info = getInfo(dreamEater);
        return info != null ? info.previewScale() : 40;
    }

    public static float getExpMultiplier(String dreamEaterRLOrName) {
        Info info = getInfo(dreamEaterRLOrName);
        return info != null ? info.expMultiplier() : 1.0F;
    }

    public static List<DreamEaterLinkData.LinkEntry> getLinks(DreamEater dreamEater) {
        Info info = getInfo(dreamEater);

        if (info == null || info.linkSupplier() == null) {
            return List.of();
        }

        List<DreamEaterLinkData.LinkEntry> links = info.linkSupplier().get();
        return links == null ? List.of() : links;
    }

    public static List<DreamEaterLinkData.LinkEntry> getLinks(String dreamEaterRLOrName) {
        Info info = getInfo(dreamEaterRLOrName);

        if (info == null || info.linkSupplier() == null) {
            return List.of();
        }

        List<DreamEaterLinkData.LinkEntry> links = info.linkSupplier().get();
        return links == null ? List.of() : links;
    }

    public static Set<String> getAllGrantedAbilityIds() {
        Set<String> ids = new HashSet<>();

        for (Info info : new HashSet<>(INFO_BY_KEY.values())) {
            if (info.linkSupplier() == null) {
                continue;
            }

            List<DreamEaterLinkData.LinkEntry> links = info.linkSupplier().get();

            if (links == null) {
                continue;
            }

            for (DreamEaterLinkData.LinkEntry link : links) {
                if (!link.grantsPlayerAbility()) {
                    continue;
                }

                String abilityId = link.abilityId();

                if (abilityId != null && !abilityId.isEmpty()) {
                    ids.add(abilityId);
                }
            }
        }

        return ids;
    }

    public static DreamEaterStats getProjectedStats(DreamEater dreamEater, PlayerData playerData, int level) {
        Info info = getInfo(dreamEater);

        if (info != null && info.statsFactory() != null) {
            return info.statsFactory().create(level, playerData);
        }

        return fallbackStats(playerData);
    }

    public static float getProjectedMagic(DreamEater dreamEater, int level) {
        Info info = getInfo(dreamEater);

        if (info != null && info.statsFactory() != null) {
            return info.statsFactory().create(level, null).magic();
        }

        return 0.0F;
    }

    public static LivingEntity createPreviewEntity(DreamEater dreamEater, Level level, Player owner, PlayerData playerData) {
        Info info = getInfo(dreamEater);

        if (info == null || info.previewFactory() == null) {
            return null;
        }

        return info.previewFactory().create(level, owner, playerData);
    }

    private static DreamEaterStats fallbackStats(PlayerData playerData) {
        if (playerData == null) {
            return new DreamEaterStats(1.0F, 1.0F, 1.0F, 1.0F);
        }

        return new DreamEaterStats(
                (float) playerData.getMaxHP(),
                (float) playerData.getStrengthStat().getStat(),
                (float) playerData.getMagicStat().getStat(),
                (float) playerData.getDefenseStat().getStat()
        );
    }

    private static DreamEaterStats chirithyStats(int level, PlayerData playerData) {
        level = Mth.clamp(level, 1, GlobalDataRM.DREAM_EATER_MAX_LEVEL);

        float hp = 22 + (float) Math.round((level - 1) * 1.25D);

        float strength = 1F;

        if (level >= 50) {
            strength = 2F;
        }

        if (level >= 90) {
            strength = 3F;
        }

        float magic = 8 + (float) Math.round((level - 1) * 0.55D);
        float defense = 4 + (float) Math.round((level - 1) * 0.35D);

        return new DreamEaterStats(hp, strength, magic, defense);
    }

    private static DreamEaterStats meowWowStats(int level, PlayerData playerData) {
        level = Mth.clamp(level, 1, GlobalDataRM.DREAM_EATER_MAX_LEVEL);

        if (level < 3) {
            return new DreamEaterStats(36F, 8.4F, 11.1F, 6.6F);
        }

        if (level < 6) {
            return new DreamEaterStats(37F, 12F, 16F, 6F);
        }

        if (level < 8) {
            return new DreamEaterStats(46F, 15F, 20F, 8F);
        }

        if (level < 10) {
            return new DreamEaterStats(52F, 17F, 22F, 9F);
        }

        if (level < 12) {
            return new DreamEaterStats(58F, 19F, 25F, 10F);
        }

        if (level < 14) {
            return new DreamEaterStats(63F, 21F, 27F, 11F);
        }

        if (level < 16) {
            return new DreamEaterStats(69F, 23F, 30F, 12F);
        }

        if (level < 18) {
            return new DreamEaterStats(75F, 24F, 32F, 12F);
        }

        if (level < 20) {
            return new DreamEaterStats(81F, 26F, 35F, 13F);
        }

        if (level < 22) {
            return new DreamEaterStats(86F, 28F, 37F, 14F);
        }

        if (level < 24) {
            return new DreamEaterStats(92F, 30F, 40F, 15F);
        }

        if (level < 26) {
            return new DreamEaterStats(98F, 32F, 42F, 16F);
        }

        int extraLevels = level - 26;

        float hp = 104F + (extraLevels * 2.5F);
        float strength = 34F + (extraLevels * 0.50F);
        float magic = 45F + (extraLevels * 0.65F);
        float defense = 17F + (extraLevels * 0.25F);

        return new DreamEaterStats(hp, strength, magic, defense);
    }

    private static DreamEaterStats komoryBatStats(int level, PlayerData playerData) {
        level = Mth.clamp(level, 1, GlobalDataRM.DREAM_EATER_MAX_LEVEL);

        float hp;
        float strength;
        float magic;
        float defense;

        if (level <= 3) {
            hp = (float) (32.7D + ((level - 1) * 0.65D));
            strength = (float) (8.2D + ((level - 1) * 1.9D));
            magic = (float) (10.8D + ((level - 1) * 2.6D));
            defense = (float) (5.9D + ((level - 1) * 0.05D));
        } else {
            int extraLevels = level - 3;

            hp = (float) (34.0D + (extraLevels * 2.15D));
            strength = (float) (12.0D + (extraLevels * 0.42D));
            magic = (float) (16.0D + (extraLevels * 0.62D));
            defense = (float) (6.0D + (extraLevels * 0.18D));
        }

        return new DreamEaterStats(hp, strength, magic, defense);
    }

    private static DreamEaterStats cactuarStats(int level, PlayerData playerData) {
        level = Mth.clamp(level, 1, GlobalDataRM.DREAM_EATER_MAX_LEVEL);

        float hp = 26.0F + level * 0.90F;
        float strength = 4.0F + level * 0.16F;
        float magic = 5.0F + level * 0.20F;
        float defense = 3.0F + level * 0.08F;

        return new DreamEaterStats(hp, strength, magic, defense);
    }

    private static DreamEaterStats tonberryStats(int level, PlayerData playerData) {
        level = Mth.clamp(level, 1, GlobalDataRM.DREAM_EATER_MAX_LEVEL);

        float hp = 42.0F + level * 1.35F;
        float strength = 6.0F + level * 0.24F;
        float magic = 2.0F + level * 0.06F;
        float defense = 7.0F + level * 0.13F;

        return new DreamEaterStats(hp, strength, magic, defense);
    }

    private static LivingEntity createChirithyPreview(Level level, Player owner, PlayerData playerData) {
        ChirithyEntity chirithy = new ChirithyEntity(ModEntitiesRM.TYPE_CHIRITHY.get(), level);
        chirithy.setOwnerUUID(owner.getUUID());

        boolean isOrg = playerData != null && playerData.getAlignment() != Utils.OrgMember.NONE;
        chirithy.setVariant(isOrg ? 0 : 1);

        return chirithy;
    }

    private static LivingEntity createMeowWowPreview(Level level, Player owner, PlayerData playerData) {
        MeowWowEntity meowWow = new MeowWowEntity(ModEntitiesRM.TYPE_MEOW_WOW.get(), level);
        meowWow.setOwnerUUID(owner.getUUID());

        boolean isOrg = playerData != null && playerData.getAlignment() != Utils.OrgMember.NONE;
        meowWow.setVariant(isOrg ? MeowWowEntity.VARIANT_ORG : MeowWowEntity.VARIANT_NORMAL);

        return meowWow;
    }

    private static LivingEntity createKomoryBatPreview(Level level, Player owner, PlayerData playerData) {
        KomoryBatEntity komoryBat = new KomoryBatEntity(ModEntitiesRM.TYPE_KOMORY_BAT.get(), level);
        komoryBat.setOwnerUUID(owner.getUUID());

        boolean isOrg = playerData != null && playerData.getAlignment() != Utils.OrgMember.NONE;
        komoryBat.setVariant(isOrg ? KomoryBatEntity.VARIANT_ORG : KomoryBatEntity.VARIANT_NORMAL);

        return komoryBat;
    }

    private static LivingEntity createCactuarPreview(Level level, Player owner, PlayerData playerData) {
        CactuarSpiritEntity cactuar = new CactuarSpiritEntity(ModEntitiesRM.TYPE_CACTUAR_SPIRIT.get(), level);
        cactuar.setOwnerUUID(owner.getUUID());
        return cactuar;
    }

    private static LivingEntity createTonberryPreview(Level level, Player owner, PlayerData playerData) {
        TonberrySpiritEntity tonberry = new TonberrySpiritEntity(ModEntitiesRM.TYPE_TONBERRY_SPIRIT.get(), level);
        tonberry.setOwnerUUID(owner.getUUID());
        return tonberry;
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }

        String cleaned = value.trim().toLowerCase(Locale.ROOT);

        if (cleaned.isEmpty()) {
            return "";
        }

        return cleaned;
    }

    private static ResourceLocation safeParse(String value) {
        try {
            if (value == null || value.isEmpty() || !value.contains(":")) {
                return null;
            }

            return ResourceLocation.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    private static String prettifyName(String value) {
        if (value == null || value.isEmpty()) {
            return "Dream Eater";
        }

        String cleaned = value;

        if (cleaned.contains(":")) {
            cleaned = cleaned.substring(cleaned.indexOf(':') + 1);
        }

        if (cleaned.startsWith("dreameater_")) {
            cleaned = cleaned.substring("dreameater_".length());
        }

        cleaned = cleaned.replace('_', ' ').trim();

        if (cleaned.isEmpty()) {
            return "Dream Eater";
        }

        StringBuilder builder = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : cleaned.toCharArray()) {
            if (Character.isWhitespace(c)) {
                builder.append(c);
                capitalizeNext = true;
                continue;
            }

            builder.append(capitalizeNext ? Character.toUpperCase(c) : c);
            capitalizeNext = false;
        }

        return builder.toString();
    }
}