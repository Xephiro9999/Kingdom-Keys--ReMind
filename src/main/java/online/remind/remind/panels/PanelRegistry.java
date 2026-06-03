package online.remind.remind.panels;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.KingdomKeysReMind;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PanelRegistry {

    private static final Map<ResourceLocation, PanelData> PANELS = new HashMap<>();

    public static final ResourceLocation LEVEL_UP =
            id("level_up");

    public static final ResourceLocation STRENGTH_UNIT =
            id("strength_unit");

    public static final ResourceLocation MAGIC_UNIT =
            id("magic_unit");

    public static final ResourceLocation DEFENSE_UNIT =
            id("defense_unit");

    public static final ResourceLocation AP_UNIT =
            id("ap_unit");

    public static final ResourceLocation STRENGTH_UNIT_L =
            id("strength_unit_l");

    public static final ResourceLocation MAGIC_UNIT_L =
            id("magic_unit_l");

    public static final ResourceLocation DEFENSE_UNIT_L =
            id("defense_unit_l");

    public static final ResourceLocation AP_UNIT_L =
            id("ap_unit_l");

    public static final ResourceLocation LEVEL_DOUBLER =
            id("level_doubler");
    public static final ResourceLocation POWER_LINK =
            id("power_link");

    public static final ResourceLocation MAGIC_LINK =
            id("magic_link");

    public static final ResourceLocation GUARD_LINK =
            id("guard_link");

    public static final ResourceLocation LEVEL_LINK =
            id("level_link");

    // KK Abilities

    public static final ResourceLocation HIGH_JUMP_PANEL = id("high_jump_panel");
    public static final ResourceLocation DODGE_ROLL_PANEL = id("dodge_roll_panel");
    public static final ResourceLocation AERIAL_DODGE_PANEL = id("aerial_dodge_panel");
    public static final ResourceLocation QUICK_RUN_PANEL = id("quick_run_panel");
    public static final ResourceLocation GLIDE_PANEL = id("glide_panel");

    public static final ResourceLocation HEARTS_POWER_PANEL = id("hearts_power_panel");

    public static final ResourceLocation ULTIMA_WEAPON_PANEL = id("ultima_weapon_panel");

    public static final ResourceLocation COMBO_PLUS_PANEL =
            id("combo_plus_panel");

    public static final ResourceLocation FIRE_BOOST_PANEL =
            id("fire_boost_panel");

    public static final ResourceLocation BLIZZARD_BOOST_PANEL =
            id("blizzard_boost_panel");

    public static final ResourceLocation THUNDER_BOOST_PANEL =
            id("thunder_boost_panel");

    public static final ResourceLocation DRAW_PANEL =
            id("draw_panel");

    public static final ResourceLocation JACKPOT_PANEL =
            id("jackpot_panel");

    public static final ResourceLocation LUCKY_LUCKY_PANEL =
            id("lucky_lucky_panel");

    public static void init() {
        register(new PanelData(
                LEVEL_UP,
                PanelType.LEVEL,
                1,
                1,
                0,
                0,
                0,
                0,
                1
        ));

        register(new PanelData(
                STRENGTH_UNIT,
                PanelType.STRENGTH,
                1,
                1,
                1,
                0,
                0,
                0,
                0
        ));

        register(new PanelData(
                MAGIC_UNIT,
                PanelType.MAGIC,
                1,
                1,
                0,
                1,
                0,
                0,
                0
        ));

        register(new PanelData(
                DEFENSE_UNIT,
                PanelType.DEFENSE,
                1,
                1,
                0,
                0,
                1,
                0,
                0
        ));

        register(new PanelData(
                AP_UNIT,
                PanelType.AP,
                1,
                1,
                0,
                0,
                0,
                2,
                0
        ));

        register(new PanelData(
                STRENGTH_UNIT_L,
                PanelType.STRENGTH,
                1,
                2,
                3,
                0,
                0,
                0,
                0
        ));

        register(new PanelData(
                MAGIC_UNIT_L,
                PanelType.MAGIC,
                1,
                2,
                0,
                3,
                0,
                0,
                0
        ));

        register(new PanelData(
                DEFENSE_UNIT_L,
                PanelType.DEFENSE,
                1,
                2,
                0,
                0,
                3,
                0,
                0
        ));

        register(new PanelData(
                AP_UNIT_L,
                PanelType.AP,
                1,
                2,
                0,
                0,
                0,
                5,
                0
        ));

        register(new PanelData(
                LEVEL_DOUBLER,
                PanelType.LEVEL,
                2,
                1,
                0,
                0,
                0,
                0,
                2
        ));

        register(new PanelData(
                POWER_LINK,
                PanelType.LINK,
                1,
                1,
                0,
                0,
                0,
                0,
                0
        ));

        register(new PanelData(
                MAGIC_LINK,
                PanelType.LINK,
                1,
                1,
                0,
                0,
                0,
                0,
                0
        ));

        register(new PanelData(
                GUARD_LINK,
                PanelType.LINK,
                1,
                1,
                0,
                0,
                0,
                0,
                0
        ));

        register(new PanelData(
                LEVEL_LINK,
                PanelType.LINK,
                1,
                1,
                0,
                0,
                0,
                0,
                0
        ));

        register(new PanelData(
                HEARTS_POWER_PANEL,
                PanelType.ABILITY,
                1,
                1,
                0,
                0,
                0,
                0,
                0
        ));
        register(new PanelData(
                ULTIMA_WEAPON_PANEL,
                PanelType.ABILITY,
                3,
                2,
                0,
                0,
                0,
                0,
                0
        ));

        // Growth
        register(new PanelData(HIGH_JUMP_PANEL, PanelType.ABILITY, 3, 1, 0, 0, 0, 0, 0));
        register(new PanelData(DODGE_ROLL_PANEL, PanelType.ABILITY, 3, 1, 0, 0, 0, 0, 0));
        register(new PanelData(AERIAL_DODGE_PANEL, PanelType.ABILITY, 3, 1, 0, 0, 0, 0, 0));
        register(new PanelData(QUICK_RUN_PANEL, PanelType.ABILITY, 3, 1, 0, 0, 0, 0, 0));
        register(new PanelData(GLIDE_PANEL, PanelType.ABILITY, 3, 1, 0, 0, 0, 0, 0));

        // Stackable
        register(new PanelData(COMBO_PLUS_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 1, 0));
        register(new PanelData(FIRE_BOOST_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 3, 0));
        register(new PanelData(BLIZZARD_BOOST_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 3, 0));
        register(new PanelData(THUNDER_BOOST_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 3, 0));
        register(new PanelData(DRAW_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 3, 0));
        register(new PanelData(JACKPOT_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 4, 0));
        register(new PanelData(LUCKY_LUCKY_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 5, 0));
    }

    private static void register(PanelData data) {
        PANELS.put(data.getId(), data);
    }

    public static PanelData get(ResourceLocation id) {
        return PANELS.get(id);
    }

    public static Collection<PanelData> getAll() {
        return PANELS.values();
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, path);
    }
}