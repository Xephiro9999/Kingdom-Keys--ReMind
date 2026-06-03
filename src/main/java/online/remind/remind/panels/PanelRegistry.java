package online.remind.remind.panels;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.KingdomKeysReMind;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PanelRegistry {

    private static final Map<ResourceLocation, PanelData> PANELS = new HashMap<>();

    // ============================================================
    // Stat Panels
    // ============================================================

    public static final ResourceLocation LEVEL_UP = id("level_up");

    public static final ResourceLocation STRENGTH_UNIT = id("strength_unit");
    public static final ResourceLocation MAGIC_UNIT = id("magic_unit");
    public static final ResourceLocation DEFENSE_UNIT = id("defense_unit");
    public static final ResourceLocation AP_UNIT = id("ap_unit");

    public static final ResourceLocation STRENGTH_UNIT_L = id("strength_unit_l");
    public static final ResourceLocation MAGIC_UNIT_L = id("magic_unit_l");
    public static final ResourceLocation DEFENSE_UNIT_L = id("defense_unit_l");
    public static final ResourceLocation AP_UNIT_L = id("ap_unit_l");

    // ============================================================
    // Level Doubler Panels
    // ============================================================

    /**
     * Original/current LV Doubler.
     * Kept so old saves, shop entries, and existing references do not break.
     */

    private static boolean[][] shape(String... rows) {
        boolean[][] mask = new boolean[rows.length][];

        for (int y = 0; y < rows.length; y++) {
            mask[y] = new boolean[rows[y].length()];

            for (int x = 0; x < rows[y].length(); x++) {
                char c = rows[y].charAt(x);
                mask[y][x] = c == 'X' || c == 'x' || c == '#';
            }
        }

        return mask;
    }
    public static final ResourceLocation LEVEL_DOUBLER = id("level_doubler");

    /**
     * Days-style LV Doubler shape variants.
     *
     * Each variant has two masks:
     * - shape = solid/body cells that block placement
     * - linkArea = open cells where Level Up panels are allowed to sit
     */
    public static final ResourceLocation LEVEL_DOUBLER_L_RIGHT = id("level_doubler_l_right");
    public static final ResourceLocation LEVEL_DOUBLER_L_LEFT = id("level_doubler_l_left");
    public static final ResourceLocation LEVEL_DOUBLER_L_TOP_RIGHT = id("level_doubler_l_top_right");
    public static final ResourceLocation LEVEL_DOUBLER_L_TOP_LEFT = id("level_doubler_l_top_left");
    public static final ResourceLocation LEVEL_DOUBLER_LINE = id("level_doubler_line");

    // ============================================================
    // Link Panels
    // ============================================================

    public static final ResourceLocation POWER_LINK = id("power_link");
    public static final ResourceLocation MAGIC_LINK = id("magic_link");
    public static final ResourceLocation GUARD_LINK = id("guard_link");
    public static final ResourceLocation LEVEL_LINK = id("level_link");

    // ============================================================
    // Special Ability Panels
    // ============================================================

    public static final ResourceLocation HEARTS_POWER_PANEL = id("hearts_power_panel");
    public static final ResourceLocation ULTIMA_WEAPON_PANEL = id("ultima_weapon_panel");

    // ============================================================
    // Growth Ability Panels
    // ============================================================

    public static final ResourceLocation HIGH_JUMP_PANEL = id("high_jump_panel");
    public static final ResourceLocation DODGE_ROLL_PANEL = id("dodge_roll_panel");
    public static final ResourceLocation AERIAL_DODGE_PANEL = id("aerial_dodge_panel");
    public static final ResourceLocation QUICK_RUN_PANEL = id("quick_run_panel");
    public static final ResourceLocation GLIDE_PANEL = id("glide_panel");

    // ============================================================
    // Stackable Base KK Ability Panels
    // ============================================================

    public static final ResourceLocation COMBO_PLUS_PANEL = id("combo_plus_panel");

    public static final ResourceLocation FIRE_BOOST_PANEL = id("fire_boost_panel");
    public static final ResourceLocation BLIZZARD_BOOST_PANEL = id("blizzard_boost_panel");
    public static final ResourceLocation THUNDER_BOOST_PANEL = id("thunder_boost_panel");

    public static final ResourceLocation DRAW_PANEL = id("draw_panel");
    public static final ResourceLocation JACKPOT_PANEL = id("jackpot_panel");
    public static final ResourceLocation LUCKY_LUCKY_PANEL = id("lucky_lucky_panel");

    // ============================================================
    // Init
    // ============================================================

    public static void init() {
        registerStatPanels();
        registerLevelDoublerPanels();
        registerLinkPanels();
        registerSpecialAbilityPanels();
        registerGrowthAbilityPanels();
        registerStackableAbilityPanels();
    }

    // ============================================================
    // Registration Groups
    // ============================================================

    private static void registerStatPanels() {
        registerPanel(LEVEL_UP, PanelType.LEVEL, 1, 1, 0, 0, 0, 0, 1);

        registerPanel(STRENGTH_UNIT, PanelType.STRENGTH, 1, 1, 1, 0, 0, 0, 0);
        registerPanel(MAGIC_UNIT, PanelType.MAGIC, 1, 1, 0, 1, 0, 0, 0);
        registerPanel(DEFENSE_UNIT, PanelType.DEFENSE, 1, 1, 0, 0, 1, 0, 0);
        registerPanel(AP_UNIT, PanelType.AP, 1, 1, 0, 0, 0, 2, 0);

        registerPanel(STRENGTH_UNIT_L, PanelType.STRENGTH, 1, 2, 3, 0, 0, 0, 0);
        registerPanel(MAGIC_UNIT_L, PanelType.MAGIC, 1, 2, 0, 3, 0, 0, 0);
        registerPanel(DEFENSE_UNIT_L, PanelType.DEFENSE, 1, 2, 0, 0, 3, 0, 0);
        registerPanel(AP_UNIT_L, PanelType.AP, 1, 2, 0, 0, 0, 5, 0);
    }

    private static void registerLevelDoublerPanels() {
        /*
         * Original/current LV Doubler.
         * Kept rectangular for old saves and existing references.
         */
        registerPanel(
                LEVEL_DOUBLER,
                PanelType.LEVEL,
                2,
                1,
                0,
                0,
                0,
                0,
                0
        );

        registerShapedPanel(
                LEVEL_DOUBLER_L_RIGHT,
                PanelType.LEVEL,
                2,
                3,
                0,
                0,
                0,
                0,
                0,
                shape(
                        "X.",
                        "X.",
                        "XX"
                ),
                shape(
                        "..",
                        "X.",
                        "XX"
                )
        );

        registerShapedPanel(
                LEVEL_DOUBLER_L_LEFT,
                PanelType.LEVEL,
                2,
                3,
                0,
                0,
                0,
                0,
                0,
                shape(
                        ".X",
                        ".X",
                        "XX"
                ),
                shape(
                        "..",
                        ".X",
                        "XX"
                )
        );

        registerShapedPanel(
                LEVEL_DOUBLER_L_TOP_RIGHT,
                PanelType.LEVEL,
                2,
                3,
                0,
                0,
                0,
                0,
                0,
                shape(
                        "XX",
                        "X.",
                        "X."
                ),
                shape(
                        ".X",
                        "X.",
                        "X."
                )
        );

        registerShapedPanel(
                LEVEL_DOUBLER_L_TOP_LEFT,
                PanelType.LEVEL,
                2,
                3,
                0,
                0,
                0,
                0,
                0,
                shape(
                        "XX",
                        ".X",
                        ".X"
                ),
                shape(
                        ".X",
                        ".X",
                        ".X"
                )
        );

        registerShapedPanel(
                LEVEL_DOUBLER_LINE,
                PanelType.LEVEL,
                4,
                1,
                0,
                0,
                0,
                0,
                0,
                shape(
                        "XXXX"
                ),
                shape(
                        ".XXX"
                )
        );
    }

    private static void registerLinkPanels() {
        registerPanel(POWER_LINK, PanelType.LINK, 1, 1, 0, 0, 0, 0, 0);
        registerPanel(MAGIC_LINK, PanelType.LINK, 1, 1, 0, 0, 0, 0, 0);
        registerPanel(GUARD_LINK, PanelType.LINK, 1, 1, 0, 0, 0, 0, 0);
        registerPanel(LEVEL_LINK, PanelType.LINK, 1, 1, 0, 0, 0, 0, 0);
    }

    private static void registerSpecialAbilityPanels() {
        registerPanel(HEARTS_POWER_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 0, 0);
        registerPanel(ULTIMA_WEAPON_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 0, 0);
    }

    private static void registerGrowthAbilityPanels() {
        registerPanel(HIGH_JUMP_PANEL, PanelType.ABILITY, 3, 1, 0, 0, 0, 0, 0);
        registerPanel(DODGE_ROLL_PANEL, PanelType.ABILITY, 3, 1, 0, 0, 0, 0, 0);
        registerPanel(AERIAL_DODGE_PANEL, PanelType.ABILITY, 3, 1, 0, 0, 0, 0, 0);
        registerPanel(QUICK_RUN_PANEL, PanelType.ABILITY, 3, 1, 0, 0, 0, 0, 0);
        registerPanel(GLIDE_PANEL, PanelType.ABILITY, 3, 1, 0, 0, 0, 0, 0);
    }

    private static void registerStackableAbilityPanels() {
        registerPanel(COMBO_PLUS_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 1, 0);

        registerPanel(FIRE_BOOST_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 3, 0);
        registerPanel(BLIZZARD_BOOST_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 3, 0);
        registerPanel(THUNDER_BOOST_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 3, 0);

        registerPanel(DRAW_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 3, 0);
        registerPanel(JACKPOT_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 4, 0);
        registerPanel(LUCKY_LUCKY_PANEL, PanelType.ABILITY, 1, 1, 0, 0, 0, 5, 0);
    }

    // ============================================================
    // Helpers
    // ============================================================

    private static void registerPanel(
            ResourceLocation id,
            PanelType type,
            int width,
            int height,
            int strength,
            int magic,
            int defense,
            int ap,
            int level
    ) {
        register(new PanelData(
                id,
                type,
                width,
                height,
                strength,
                magic,
                defense,
                ap,
                level,
                null,
                null
        ));
    }

    private static void registerShapedPanel(
            ResourceLocation id,
            PanelType type,
            int width,
            int height,
            int strength,
            int magic,
            int defense,
            int ap,
            int level,
            boolean[][] shape,
            boolean[][] linkArea

    ) {
        register(new PanelData(
                id,
                type,
                width,
                height,
                strength,
                magic,
                defense,
                ap,
                level,
                shape,
                linkArea
        ));
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