package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;

public class CSChangeSpiritPacket implements CustomPacketPayload {
    public static final Type<CSChangeSpiritPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_change_spirit"));
    public static final StreamCodec<FriendlyByteBuf, CSChangeSpiritPacket> STREAM_CODEC = StreamCodec.of(CSChangeSpiritPacket::encode, CSChangeSpiritPacket::decode);


    private int id;
    public CSChangeSpiritPacket(){}

    public CSChangeSpiritPacket(int id){
        this.id = id;
    }

    public static void encode(FriendlyByteBuf buffer, CSChangeSpiritPacket message) {
        buffer.writeInt(message.id);
    }

    public static CSChangeSpiritPacket decode(FriendlyByteBuf buffer) {
        CSChangeSpiritPacket msg = new CSChangeSpiritPacket();
        msg.id = buffer.readInt();
        return msg;
    }

    public static void handle(final CSChangeSpiritPacket message, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();

            IGlobalDataRM globalData = ModDataRM.getGlobal(player);
            PlayerData playerData = PlayerData.get(player);

            switch(message.id){
                case 0:

                    globalData.setDreamEaterID(0);
                    PacketHandlerRM.syncGlobalToAllAround(player, globalData);
                    break;
                case 1:

                    globalData.setDreamEaterID(1);
                    PacketHandlerRM.syncGlobalToAllAround(player, globalData);
                    break;

            }
            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);

        });
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
