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
    }

    public static boolean donorKeybladeGrant;

    @SubscribeEvent
    public static void configEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == COMMON_SPEC) {
            KingdomKeysReMind.LOGGER.info("LOAD COMMON CONFIG");
            bakeCommon();
        }
    }

}
