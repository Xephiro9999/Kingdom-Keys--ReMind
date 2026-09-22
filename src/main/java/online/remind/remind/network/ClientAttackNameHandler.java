package online.remind.remind.network;

import online.remind.remind.client.gui.LimitBreakHud;

import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ClientAttackNameHandler {

    private ClientAttackNameHandler() {
    }

    public static void handle(
            ShowAttackNamePayload payload,
            IPayloadContext context
    ) {
        LimitBreakHud.show(
                payload.attackName()
        );
    }
}