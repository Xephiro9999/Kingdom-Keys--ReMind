package online.remind.remind.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.render.mob.MeowWowRenderer;
import online.remind.remind.entity.ModEntitiesRM;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        value = Dist.CLIENT,
        bus = EventBusSubscriber.Bus.MOD
)
public class ClientModEventsRM {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                ModEntitiesRM.TYPE_MEOW_WOW.get(),
                MeowWowRenderer::new
        );
    }
}