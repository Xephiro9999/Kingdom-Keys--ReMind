package online.remind.remind.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ShowAttackNamePayload(
        String attackName
) implements CustomPacketPayload {

    public static final Type<ShowAttackNamePayload> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            "kkremind",
                            "show_attack_name"
                    )
            );

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            ShowAttackNamePayload
            > STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ShowAttackNamePayload::attackName,
            ShowAttackNamePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}