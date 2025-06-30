package online.remind.remind.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import online.remind.remind.KingdomKeysReMind;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = KingdomKeysReMind.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfigs {
    private static CommonConfig COMMON;

    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        {
            final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
            COMMON = specPair.getLeft();
            COMMON_SPEC = specPair.getRight();
        }
    }

    public static void bakeCommon(){
        donorKeybladeGrant = COMMON.donorKeybladeGrant.get();
        rageFormPercent = COMMON.rageFormPercent.get();

        // NG+ Configs
        ngpEnabled = COMMON.ngpEnabled.get();
        statCap = COMMON.statCap.get();
        hpCap = COMMON.hpCap.get();
        mpCap = COMMON.mpCap.get();
        statBonus = COMMON.statBonus.get();

        // Panels Configs
        panelsEnabled = COMMON.panelsEnabled.get();
        panelBonus = COMMON.panelBonus.get();
        panelLimit = COMMON.panelLimit.get();


    }

    public static boolean donorKeybladeGrant;

    // Forms
    public static double rageFormPercent;

    // NG+
    public static boolean ngpEnabled;
    public static int statCap;
    public static int statBonus;
    public static int hpCap;
    public static int mpCap;

    // Panels
    public static boolean panelsEnabled;
    public static int panelBonus;
    public static int panelLimit;


    @SubscribeEvent
    public static void configEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == COMMON_SPEC) {
            KingdomKeysReMind.LOGGER.info("LOAD COMMON CONFIG");
            bakeCommon();
        }
    }

}
