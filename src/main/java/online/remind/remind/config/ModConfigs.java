package online.remind.remind.config;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import online.remind.remind.KingdomKeysReMind;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(modid = KingdomKeysReMind.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModConfigs {
    private static CommonConfig COMMON;

    public static final ModConfigSpec COMMON_SPEC;

    static {
        {
            final Pair<CommonConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(CommonConfig::new);
            COMMON = specPair.getLeft();
            COMMON_SPEC = specPair.getRight();
        }
    }

    public static void bakeCommon(){
        donorKeybladeGrant = COMMON.donorKeybladeGrant.get();
        rageFormPercent = COMMON.rageFormPercent.get();
    }

    public static boolean donorKeybladeGrant;
    public static double rageFormPercent;

    @SubscribeEvent
    public static void configEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == COMMON_SPEC) {
            KingdomKeysReMind.LOGGER.info("LOAD COMMON CONFIG");
            bakeCommon();
        }
    }

}