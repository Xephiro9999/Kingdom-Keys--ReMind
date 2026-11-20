package online.remind.remind.network.cts;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;

import online.remind.remind.finishcommands.FinishCommandRegistry;
import online.remind.remind.finishcommands.FinishCommandProgression;

public record CSFinishCommandEquipPacket(String commandId) implements CustomPacketPayload {

    public static final Type<CSFinishCommandEquipPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID, "cs_finish_command_equip"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CSFinishCommandEquipPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> buf.writeUtf(packet.commandId(), 128),
                    buf -> new CSFinishCommandEquipPacket(buf.readUtf(128))
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CSFinishCommandEquipPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            String id = packet.commandId();
            if (!FinishCommandRegistry.isRegistered(id)) return;

            GlobalDataRM data = ModDataRM.getGlobal(player);
            if (data == null || !data.hasFinishCommand(id)) return;

            if (data.equipFinishCommand(id)) {
                FinishCommandProgression.sync(player);
            }
        });
    }
}
