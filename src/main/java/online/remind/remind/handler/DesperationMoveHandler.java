package online.remind.remind.handler;

import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ViewportEvent;
import online.remind.remind.entity.attacks.swiftStrikeCollider;

@OnlyIn(Dist.CLIENT)
public class DesperationMoveHandler {

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {

        if (!swiftStrikeCollider.SWIFT_STRIKE_ACTIVE) return;

        // Dark KH2-style tint
        float darken = 0.35F;

        event.setRed(event.getRed() * darken);
        event.setGreen(event.getGreen() * darken);
        event.setBlue(event.getBlue() * darken);
    }

    @SubscribeEvent
    public static void onFogDensity(ViewportEvent.RenderFog event) {
        if (!swiftStrikeCollider.SWIFT_STRIKE_ACTIVE) return;

        event.setNearPlaneDistance(0.5F);
        event.setFarPlaneDistance(15F);
    }

}
