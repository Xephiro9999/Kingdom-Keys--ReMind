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