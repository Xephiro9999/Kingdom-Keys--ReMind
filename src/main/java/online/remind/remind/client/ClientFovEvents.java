package online.remind.remind.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.effect.ModMobEffectsRM;

@EventBusSubscriber(modid = KingdomKeysReMind.MODID, value = Dist.CLIENT)
public class ClientFovEvents {

    @SubscribeEvent
    public static void onComputeFov(ComputeFovModifierEvent event) {
        LocalPlayer player = (LocalPlayer) event.getPlayer();

        if (player == null) {
            return;
        }

        MobEffectInstance haste = player.getEffect(ModMobEffectsRM.HASTE_RM);

        if (haste == null) {
            return;
        }

        double hasteBoost = switch (Math.max(0, haste.getAmplifier())) {
            case 0 -> 0.15D;
            case 1 -> 0.25D;
            case 2 -> 0.35D;
            case 3 -> 0.45D;
            default -> 0.50D;
        };

        float currentFov = event.getNewFovModifier();
        float correctedFov = (float) (currentFov / (1.0D + hasteBoost));

        event.setNewFovModifier(correctedFov);
    }
}