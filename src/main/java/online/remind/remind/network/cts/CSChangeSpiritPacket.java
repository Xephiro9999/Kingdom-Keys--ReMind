package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;

public class CSChangeSpiritPacket implements CustomPacketPayload {
    public static final Type<CSChangeSpiritPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_change_spirit"));
    public static final StreamCodec<FriendlyByteBuf, CSChangeSpiritPacket> STREAM_CODEC = StreamCodec.of(CSChangeSpiritPacket::encode, CSChangeSpiritPacket::decode);

    private String rl;
    public CSChangeSpiritPacket(){}

    public CSChangeSpiritPacket(String id){
        this.rl = id;
    }

    public static void encode(FriendlyByteBuf buffer, CSChangeSpiritPacket message) {
        buffer.writeUtf(message.rl,100);
    }

    public static CSChangeSpiritPacket decode(FriendlyByteBuf buffer) {
        CSChangeSpiritPacket msg = new CSChangeSpiritPacket();
        msg.rl = buffer.readUtf(100);
        return msg;
    }

    public static void handle(final CSChangeSpiritPacket message, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();

            IGlobalDataRM globalData = ModDataRM.getGlobal(player);

            globalData.setDreamEaterRL(message.rl);
            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        });
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
