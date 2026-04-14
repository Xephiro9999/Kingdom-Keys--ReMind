package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;

public class CSSetStepTicksPacket implements CustomPacketPayload {
    public static final Type<CSSetStepTicksPacket> TYPE = new Type(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_set_step_ticks"));
    public static final StreamCodec<FriendlyByteBuf, CSSetStepTicksPacket> STREAM_CODEC = StreamCodec.of(CSSetStepTicksPacket::encode, CSSetStepTicksPacket::decode);

    private int ticks;
    private byte type;


    public CSSetStepTicksPacket(){}

    public CSSetStepTicksPacket(int ticks, byte type){
        this.ticks = ticks;
        this.type = type;
    }

    public static void encode(FriendlyByteBuf buffer, CSSetStepTicksPacket message) {
        buffer.writeInt(message.ticks);
        buffer.writeByte(message.type);
    }

    public static CSSetStepTicksPacket decode(FriendlyByteBuf buffer) {
        CSSetStepTicksPacket msg = new CSSetStepTicksPacket();
        msg.ticks = buffer.readInt();
        msg.type = buffer.readByte();
        return msg;
    }

    public static void handle(final CSSetStepTicksPacket message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            GlobalDataRM globalData = ModDataRM.getGlobal(player);
            globalData.setStepTicks(message.ticks,message.type);

            PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
