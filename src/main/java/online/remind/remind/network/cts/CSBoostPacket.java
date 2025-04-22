package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.function.Supplier;

public class CSBoostPacket {
    // 0 = Default, 1 = NG+, 2 = Panels
    private int boost;

    public CSBoostPacket(){}

    public CSBoostPacket(int boost){
        this.boost = boost;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.boost);
    }

    public static CSBoostPacket decode(FriendlyByteBuf buffer) {
        CSBoostPacket msg = new CSBoostPacket();
        msg.boost = buffer.readInt();
        return msg;
    }

    public static void handle(final CSBoostPacket message, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer player = ctx.get().getSender();

        IGlobalCapabilitiesRM globalData = ModCapabilitiesRM.getGlobal(player);
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

        switch(message.boost){
            case 1:
                // Insert Code to Disable NG+ Boosts
                globalData.setNGPEnabled(0);
                System.out.println(globalData.getNGPEnabled());
                playerData.getStrengthStat().removeModifier("NG+ Bonus");
                playerData.getMagicStat().removeModifier("NG+ Bonus");
                playerData.getDefenseStat().removeModifier("NG+ Bonus");
                PacketHandlerRM.syncGlobalToAllAround(player, globalData);
                break;
            case 2:
                // Insert Code to Disable Org Boosts
                globalData.setPanelsEnabled(0);
                System.out.println(globalData.getPanelsEnabled());
                playerData.getStrengthStat().removeModifier("Panel");
                playerData.getMagicStat().removeModifier("Panel");
                playerData.getDefenseStat().removeModifier("Panel");
                PacketHandlerRM.syncGlobalToAllAround(player, globalData);
                break;
            case 3:
                // Insert Code to Enable NG+ Boosts
                globalData.setNGPEnabled(1);
                System.out.println(globalData.getNGPEnabled());
                playerData.getStrengthStat().addModifier("NG+ Bonus", globalData.getSTRBonus(), true, false);
                playerData.getMagicStat().addModifier("NG+ Bonus",globalData.getMAGBonus(), true, false);
                playerData.getDefenseStat().addModifier("NG+ Bonus",globalData.getDEFBonus(), true, false);
                PacketHandlerRM.syncGlobalToAllAround(player, globalData);
                break;
            case 4:
                // Insert Code to Enable Org Boosts
                globalData.setPanelsEnabled(1);
                System.out.println(globalData.getPanelsEnabled());
                playerData.getStrengthStat().addModifier("Panel", globalData.getSTRPanel(), false, false);
                playerData.getMagicStat().addModifier("Panel", globalData.getMAGPanel(), false, false);
                playerData.getDefenseStat().addModifier("Panel", globalData.getDEFPanel(), false, false);
                PacketHandlerRM.syncGlobalToAllAround(player, globalData);
                break;
        }
        PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), player);
        PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        ctx.get().setPacketHandled(true);
    }
}
