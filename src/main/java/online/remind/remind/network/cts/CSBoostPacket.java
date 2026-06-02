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
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.panels.OrganizationPanelStatHelper;

public class CSBoostPacket implements CustomPacketPayload{
    public static final Type<CSBoostPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_boost_packet"));
    public static final StreamCodec<FriendlyByteBuf, CSBoostPacket> STREAM_CODEC = StreamCodec.of(CSBoostPacket::encode, CSBoostPacket::decode);

    // 0 = Default, 1 = NG+, 2 = Panels
    private int boost;
    public CSBoostPacket(){}

    public CSBoostPacket(int boost){
        this.boost = boost;
    }

    public static void encode(FriendlyByteBuf buffer, CSBoostPacket message) {
        buffer.writeInt(message.boost);
    }

    public static CSBoostPacket decode(FriendlyByteBuf buffer) {
        CSBoostPacket msg = new CSBoostPacket();
        msg.boost = buffer.readInt();
        return msg;
    }

    public static void handle(final CSBoostPacket message, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
        Player player = ctx.player();

        GlobalDataRM globalData = ModDataRM.getGlobal(player);
        PlayerData playerData = PlayerData.get(player);

        switch(message.boost){
            case 1:
                // Insert Code to Disable NG+ Boosts
                globalData.setNGPEnabled(0);
                playerData.getStrengthStat().removeModifier("NG+ Bonus");
                playerData.getMagicStat().removeModifier("NG+ Bonus");
                playerData.getDefenseStat().removeModifier("NG+ Bonus");
                PacketHandlerRM.syncGlobalToAllAround(player, globalData);
                break;
            case 2:
                globalData.setPanelsEnabled(0);
                OrganizationPanelStatHelper.removePanelModifiers(player);
                PacketHandlerRM.syncGlobalToAllAround(player, globalData);
                break;
            case 3:
                // Insert Code to Enable NG+ Boosts
                globalData.setNGPEnabled(1);
                playerData.getStrengthStat().addModifier("NG+ Bonus", globalData.getSTRBonus(), true, false);
                playerData.getMagicStat().addModifier("NG+ Bonus",globalData.getMAGBonus(), true, false);
                playerData.getDefenseStat().addModifier("NG+ Bonus",globalData.getDEFBonus(), true, false);
                PacketHandlerRM.syncGlobalToAllAround(player, globalData);
                break;
            case 4:
                globalData.setPanelsEnabled(1);
                OrganizationPanelStatHelper.applyPanelModifiers(player);
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
