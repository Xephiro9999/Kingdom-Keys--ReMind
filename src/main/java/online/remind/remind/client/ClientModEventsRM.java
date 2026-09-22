package online.remind.remind.client;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.gui.LimitBreakHud;
import online.remind.remind.client.render.CrossSlashEffectRenderer;
import online.remind.remind.client.render.mob.MeowWowRenderer;
import online.remind.remind.entity.ModEntitiesRM;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        value = Dist.CLIENT,
        bus = EventBusSubscriber.Bus.MOD
)
public class ClientModEventsRM {

    private static final ResourceLocation LIMIT_BREAK_BAR =
            ResourceLocation.fromNamespaceAndPath(
                    "kkremind",
                    "limit_break_bar"
            );

    @SubscribeEvent
    public static void registerGuiLayers(
            RegisterGuiLayersEvent event
    ) {
        event.registerAboveAll(
                LIMIT_BREAK_BAR,
                LimitBreakHud::render
        );
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                ModEntitiesRM.TYPE_MEOW_WOW.get(),
                MeowWowRenderer::new
        );

        event.registerEntityRenderer(
                ModEntitiesRM.CROSS_SLASH_EFFECT.get(),
                CrossSlashEffectRenderer::new
        );
    }
}