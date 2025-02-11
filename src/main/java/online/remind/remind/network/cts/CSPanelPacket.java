package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;

public class CSPanelPacket implements CustomPacketPayload {
    public static final Type<CSPanelPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_panel_packet"));
    public static final StreamCodec<FriendlyByteBuf, CSPanelPacket> STREAM_CODEC = StreamCodec.of(CSPanelPacket::encode, CSPanelPacket::decode);

    // 0 = none (default), 1 = str, 2 = mag, 3 = def...
    private int choice;
    String formName;


    private static int driveLvl;

    public CSPanelPacket(){}

    public CSPanelPacket(int choice){
        this.choice = choice;
    }

    public static void encode(FriendlyByteBuf buffer, CSPanelPacket message) {
        buffer.writeInt(message.choice);
    }

    public static CSPanelPacket decode(FriendlyByteBuf buffer) {
        CSPanelPacket msg = new CSPanelPacket();
        msg.choice = buffer.readInt();
        return msg;
    }

    public static void handle(final CSPanelPacket message, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();

            PlayerData playerData = PlayerData.get(player);
            IGlobalDataRM globalData = ModDataRM.getGlobal(player);


            //
            int level;
            int xpGain;
            int i;
            int totalBoost;
            float heartsRegained;


            //System.out.println(globalData.getPanelChoice());

            switch (message.choice) {
                case 0:
                    System.out.println("This shouldn't happen");
                    break;
                case 1:
                    playerData.addHearts(-1000 * globalData.getSTRPanel());
                    globalData.addSTRPanel(1);
                    System.out.println(globalData.getSTRPanel());
                    break;
                case 2:
                    playerData.addHearts(-1000 * globalData.getMAGPanel());
                    globalData.addMAGPanel(1);
                    break;
                case 3:
                    playerData.addHearts(-1000 * globalData.getDEFPanel());
                    globalData.addDEFPanel(1);
                    System.out.println(globalData.getDEFPanel());
                    break;
                case 4:
                    playerData.addMaxAP(2);
                    playerData.addHearts(-1000);
                    break;
                case 5:
                    level = playerData.getDriveFormLevel(Strings.Form_Valor);
                    xpGain = level * 10;

                    playerData.addDriveFormExperience(Strings.Form_Valor, player, xpGain);
                    playerData.addHearts(-5000);
                    PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
                    break;
                case 6:
                    level = playerData.getDriveFormLevel(Strings.Form_Wisdom);
                    xpGain = level * 4;

                    playerData.addDriveFormExperience(Strings.Form_Wisdom, player, xpGain);
                    playerData.addHearts(-5000);
                    PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
                    break;
                case 7:
                    level = playerData.getDriveFormLevel(Strings.Form_Limit);
                    xpGain = level * 2;

                    playerData.addDriveFormExperience(Strings.Form_Limit, player, xpGain);
                    playerData.addHearts(-5000);
                    PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
                    break;
                case 8:
                    level = playerData.getDriveFormLevel(Strings.Form_Master);
                    xpGain = level * 8;

                    playerData.addDriveFormExperience(Strings.Form_Master, player, xpGain);
                    playerData.addHearts(-5000);
                    PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
                    break;
                case 9:
                    level = playerData.getDriveFormLevel(Strings.Form_Final);
                    xpGain = level * 4;

                    playerData.addDriveFormExperience(Strings.Form_Final, player, xpGain);
                    playerData.addHearts(-5000);
                    PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
                    break;
                case 10:
                    playerData.addHearts(-10000 * playerData.getLevel());
                    xpGain = playerData.getExpNeeded(playerData.getLevel(), 0) - playerData.getExperience();
                    //System.out.println(playerData.getExpNeeded(playerData.getLevel(),0)- playerData.getExperience());
                    playerData.addExperience(player, xpGain, false, true);
                    break;
                case 11:

                    totalBoost = globalData.getDEFPanel() + globalData.getSTRPanel() + globalData.getMAGPanel();

                    heartsRegained = (totalBoost * 1000) * 0.75f;

                    globalData.setSTRPanel(0);
                    globalData.setMAGPanel(0);
                    globalData.setDEFPanel(0);

                    playerData.getStrengthStat().removeModifier("Panel");
                    playerData.getMagicStat().removeModifier("Panel");
                    playerData.getDefenseStat().removeModifier("Panel");

                    playerData.addHearts((int) heartsRegained);

                    System.out.println(totalBoost);
                    System.out.println(heartsRegained);
                    break;
            }

            PacketHandler.sendTo(new SCSyncPlayerData(player), player);
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}


