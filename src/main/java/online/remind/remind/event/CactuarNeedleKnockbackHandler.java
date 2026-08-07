package online.remind.remind.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import online.remind.remind.KingdomKeysReMind;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        bus = EventBusSubscriber.Bus.GAME
)
public final class CactuarNeedleKnockbackHandler {

    public static final String NO_KNOCKBACK_MARKER =
            "kkremind_cactuar_needle_no_knockback";

    private CactuarNeedleKnockbackHandler() {
    }

    @SubscribeEvent
    public static void onLivingKnockback(LivingKnockBackEvent event) {
        if (event.getEntity()
                .getPersistentData()
                .getBoolean(NO_KNOCKBACK_MARKER)) {

            event.setCanceled(true);
        }
    }
}