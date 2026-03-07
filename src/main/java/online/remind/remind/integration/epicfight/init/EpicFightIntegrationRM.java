package online.remind.remind.integration.epicfight.init;

import net.neoforged.bus.api.IEventBus;
import yesman.epicfight.api.event.EpicFightEventHooks;

public class EpicFightIntegrationRM {

    public static void initIntegrationRM(IEventBus modEventBus) {
        EpicRMWeapons.register();
    }
}
