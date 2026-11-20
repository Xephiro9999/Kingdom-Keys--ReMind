package online.remind.remind.network.stc;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.ModDataRM;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record SCFinishCommandDataPacket(
        String equipped, Set<String> unlocked, Map<String, Integer> progress
) implements CustomPacketPayload {
    public static final Type<SCFinishCommandDataPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "sc_finish_command_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SCFinishCommandDataPacket> STREAM_CODEC =
            StreamCodec.of(SCFinishCommandDataPacket::encode, SCFinishCommandDataPacket::decode);

    private static void encode(RegistryFriendlyByteBuf buf, SCFinishCommandDataPacket packet) {
        buf.writeUtf(packet.equipped, 128);
        buf.writeVarInt(packet.unlocked.size());
        for (String id : packet.unlocked) buf.writeUtf(id, 128);
        buf.writeVarInt(packet.progress.size());
        packet.progress.forEach((id, count) -> {
            buf.writeUtf(id, 128);
            buf.writeVarInt(count);
        });
    }

    private static SCFinishCommandDataPacket decode(RegistryFriendlyByteBuf buf) {
        String equipped = buf.readUtf(128);
        int unlockCount = buf.readVarInt();
        if (unlockCount < 0 || unlockCount > 256) throw new IllegalArgumentException("Too many Finish Commands");
        Set<String> unlocked = new HashSet<>();
        for (int i = 0; i < unlockCount; i++) unlocked.add(buf.readUtf(128));
        int progressCount = buf.readVarInt();
        if (progressCount < 0 || progressCount > 256) throw new IllegalArgumentException("Too many Finish Command progress entries");
        Map<String, Integer> progress = new HashMap<>();
        for (int i = 0; i < progressCount; i++) progress.put(buf.readUtf(128), Math.max(0, buf.readVarInt()));
        return new SCFinishCommandDataPacket(equipped, unlocked, progress);
    }

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SCFinishCommandDataPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() == null) return;
            ModDataRM.getGlobal(context.player())
                    .applyFinishCommandSnapshot(packet.equipped, packet.unlocked, packet.progress);
        });
    }
}
