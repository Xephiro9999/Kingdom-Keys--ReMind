package online.remind.remind.integration.epicfight.init;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

public class EpicFightIntegrationRM {

    public static void initIntegration(IEventBus modEventBus) {
        //EpicFightUtils.setBattleMode((player) -> EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class).isBattleMode());
        modEventBus.addListener(EpicRMWeapons::register);

    }
}
