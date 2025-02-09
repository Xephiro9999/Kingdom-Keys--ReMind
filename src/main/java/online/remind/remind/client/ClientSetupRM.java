package online.remind.remind.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import online.remind.remind.client.render.AutoLifeLayerRenderer;
import online.remind.remind.client.render.BerserkLayerRenderer;
import online.remind.remind.entity.ModEntitiesRM;
import online.remind.remind.handler.ClientEventsRM;
import online.remind.remind.handler.InputHandlerRM;

@EventBusSubscriber(value = Dist.CLIENT, bus=EventBusSubscriber.Bus.MOD)
public class ClientSetupRM {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ModEntitiesRM.registerRenderers(event);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ModEntitiesRM.registerLayers(event);
    }

    @SubscribeEvent
    public static void registerKeyBinding(RegisterKeyMappingsEvent event) {
        for (InputHandlerRM.Keybinds key : InputHandlerRM.Keybinds.values())
            event.register(key.getKeybind());
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        /*for (Map.Entry<EntityType<?>, EntityRenderer<?>> entry : Minecraft.getInstance().getEntityRenderDispatcher().renderers.entrySet()) {
            if (entry.getValue() instanceof LivingEntityRenderer renderer && !(entry.getValue() instanceof PlayerRenderer)) {
                renderer.addLayer(new BerserkLayerRenderer<LivingEntity>(renderer, event.getEntityModels()));
            }
        }*/
        LivingEntityRenderer<Player, PlayerModel<Player>> renderer = event.getSkin(PlayerSkin.Model.WIDE);
        renderer.addLayer(new BerserkLayerRenderer<>(renderer, event.getEntityModels()));
        renderer.addLayer(new AutoLifeLayerRenderer<>(renderer, event.getEntityModels()));

        renderer = event.getSkin(PlayerSkin.Model.SLIM);
        renderer.addLayer(new BerserkLayerRenderer<>(renderer, event.getEntityModels()));
        renderer.addLayer(new AutoLifeLayerRenderer<>(renderer, event.getEntityModels()));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
		NeoForge.EVENT_BUS.register(new ClientEventsRM());
    }


}
