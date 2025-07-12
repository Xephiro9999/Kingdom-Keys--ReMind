package online.remind.remind.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface PacketRM extends CustomPacketPayload {
    void handle(IPayloadContext context);
    default PacketRM reply(IPayloadContext context) {
        return null;
    }
}
