package online.remind.remind.dreameater;

import java.util.List;

public class DreamEaterLinkData {

    public static final String TYPE_SPIRIT_SKILL = "Spirit Skill";
    public static final String TYPE_GRANTED_ABILITY = "Granted Ability";


    public static final String ITEM_BOOST = "kingdomkeys:ability_item_boost";
    public static final String MP_HASTE = "kingdomkeys:ability_mp_haste";
    //public static final String CONFUSION_BLOCK = "kkremind:ability_confusion_block";
    public static final String LEAF_BRACER = "kingdomkeys:ability_leaf_bracer";

    public static final String HP_BOOST = "kkremind:ability_hp_boost";
    public static final String MP_BOOST = "kkremind:ability_mp_boost";
    public static final String ATTACK_HASTE = "kkremind:ability_attack_haste";
    public static final String POISON_BLOCK = "kkremind:ability_poison_block";


    private static final List<LinkEntry> MEOW_WOW_LINKS = List.of(
            // Spirit skills. These are NOT granted to the player.
            new LinkEntry("meow_wow_cure", "Cure", TYPE_SPIRIT_SKILL, 1, false, ""),
            new LinkEntry("meow_wow_slow", "Slow", TYPE_SPIRIT_SKILL, 1, false, ""),
            new LinkEntry("meow_wow_balloon", "Balloon", TYPE_SPIRIT_SKILL, 1, false, ""),
            new LinkEntry("meow_wow_cura", "Cura", TYPE_SPIRIT_SKILL, 10, false, ""),
            new LinkEntry("meow_wow_balloonra", "Balloonra", TYPE_SPIRIT_SKILL, 16, false, ""),
            new LinkEntry("meow_wow_curaga", "Curaga", TYPE_SPIRIT_SKILL, 20, false, ""),
            new LinkEntry("meow_wow_balloonga", "Balloonga", TYPE_SPIRIT_SKILL, 25, false, ""),

            // Granted player abilities.
            new LinkEntry("meow_wow_item_boost", "Item Boost", TYPE_GRANTED_ABILITY, 1, true, ITEM_BOOST),
            new LinkEntry("meow_wow_hp_boost", "HP Boost", TYPE_GRANTED_ABILITY, 5, true, HP_BOOST),
            new LinkEntry("meow_wow_mp_boost", "MP Boost", TYPE_GRANTED_ABILITY, 5, true, MP_BOOST),
            new LinkEntry("meow_wow_attack_haste", "Attack Haste", TYPE_GRANTED_ABILITY, 8, true, ATTACK_HASTE),
            new LinkEntry("meow_wow_mp_haste", "MP Haste", TYPE_GRANTED_ABILITY, 12, true, MP_HASTE),
            //new LinkEntry("meow_wow_confusion_block", "Confusion Block", TYPE_GRANTED_ABILITY, 30, true, CONFUSION_BLOCK),
            new LinkEntry("meow_wow_poison_block", "Poison Block", TYPE_GRANTED_ABILITY, 35, true, POISON_BLOCK),
            new LinkEntry("meow_wow_leaf_bracer", "Leaf Bracer", TYPE_GRANTED_ABILITY, 50, true, LEAF_BRACER)
    );

    public static List<LinkEntry> getMeowWowLinks() {
        return MEOW_WOW_LINKS;
    }

    private static final List<LinkEntry> KOMORY_BAT_LINKS = List.of(
            // Spirit skills. These are NOT granted to the player.
            new LinkEntry("komory_bat_supersonics", "Supersonics", TYPE_SPIRIT_SKILL, 1, false, ""),
            new LinkEntry("komory_bat_confusing_waves", "Confusing Waves", TYPE_SPIRIT_SKILL, 1, false, ""),
            new LinkEntry("komory_bat_zero_gravity", "Zero Gravity", TYPE_SPIRIT_SKILL, 5, false, ""),
            new LinkEntry("komory_bat_drain", "Drain", TYPE_SPIRIT_SKILL, 8, false, ""),
            new LinkEntry("komory_bat_zero_gravira", "Zero Gravira", TYPE_SPIRIT_SKILL, 15, false, ""),
            new LinkEntry("komory_bat_haste", "Haste", TYPE_SPIRIT_SKILL, 20, false, ""),
            new LinkEntry("komory_bat_zero_graviga", "Zero Graviga", TYPE_SPIRIT_SKILL, 25, false, ""),


            new LinkEntry("komory_bat_magic_haste", "Magic Haste", TYPE_GRANTED_ABILITY, 12, true, MP_HASTE),
            new LinkEntry("komory_bat_magic_haste", "Magic Haste", TYPE_GRANTED_ABILITY, 24, true, MP_HASTE),
            new LinkEntry("komory_bat_attack_haste", "Attack Haste", TYPE_GRANTED_ABILITY, 10, true, ATTACK_HASTE),
            new LinkEntry("komory_bat_attack_haste", "Attack Haste", TYPE_GRANTED_ABILITY, 20, true, ATTACK_HASTE)
            //new LinkEntry("komory_bat_confusion_block", "Confusion Block", TYPE_GRANTED_ABILITY, 30, true, CONFUSION_BLOCK)
    );

    public static List<DreamEaterLinkData.LinkEntry> getKomoryBatLinks() {
        return KOMORY_BAT_LINKS;
    }

    public static boolean isUnlocked(LinkEntry entry, int level) {
        return level >= entry.unlockLevel();
    }

    public record LinkEntry(
            String id,
            String displayName,
            String type,
            int unlockLevel,
            boolean grantsPlayerAbility,
            String abilityId
    ) {
    }
}