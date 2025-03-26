package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
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
                    DriveForm drive = ModDriveForms.registry.get().getValue(new ResourceLocation(Strings.Form_Valor));
                    if (level == 0){
                        playerData.setDriveFormLevel(Strings.Form_Valor, 1);
                        playerData.addVisibleDriveForm(Strings.Form_Valor);
                    } else {
                        while (playerData.getDriveFormLevel(Strings.Form_Valor) < level + 1) {
                            int cost = drive.getLevelUpCost(playerData.getDriveFormLevel(Strings.Form_Valor) + 1);
                            playerData.setDriveFormExp(player, Strings.Form_Valor, cost);
                        }
                    }
                    playerData.addHearts(-5000);
                    PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
                    break;
                case 6:
                    level = playerData.getDriveFormLevel(Strings.Form_Wisdom);
                    DriveForm drive1 = ModDriveForms.registry.get().getValue(new ResourceLocation(Strings.Form_Wisdom));
                    if (level == 0){
                        playerData.setDriveFormLevel(Strings.Form_Wisdom, 1);
                        playerData.addVisibleDriveForm(Strings.Form_Wisdom);
                    } else {
                        while (playerData.getDriveFormLevel(Strings.Form_Wisdom) < level + 1) {
                            int cost = drive1.getLevelUpCost(playerData.getDriveFormLevel(Strings.Form_Wisdom) + 1);
                            playerData.setDriveFormExp(player, Strings.Form_Wisdom, cost);
                        }
                    }
                    playerData.addHearts(-5000);
                    PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
                    break;
                case 7:
                    DriveForm drive2 = ModDriveForms.registry.get().getValue(new ResourceLocation(Strings.Form_Wisdom));
                    if (level == 0){
                        playerData.setDriveFormLevel(Strings.Form_Limit, 1);
                        playerData.addVisibleDriveForm(Strings.Form_Limit);
                    } else {
                        while (playerData.getDriveFormLevel(Strings.Form_Limit) < level + 1) {
                            int cost = drive2.getLevelUpCost(playerData.getDriveFormLevel(Strings.Form_Limit) + 1);
                            playerData.setDriveFormExp(player, Strings.Form_Limit, cost);
                        }
                    }
                    playerData.addHearts(-5000);
                    PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
                    break;
                case 8:
                    level = playerData.getDriveFormLevel(Strings.Form_Master);
                    DriveForm drive3 = ModDriveForms.registry.get().getValue(new ResourceLocation(Strings.Form_Master));
                    if (level == 0){
                        playerData.setDriveFormLevel(Strings.Form_Master, 1);
                        playerData.addVisibleDriveForm(Strings.Form_Master);
                    } else {
                        while (playerData.getDriveFormLevel(Strings.Form_Master) < level + 1) {
                            int cost = drive3.getLevelUpCost(playerData.getDriveFormLevel(Strings.Form_Master) + 1);
                            playerData.setDriveFormExp(player, Strings.Form_Master, cost);
                        }
                    }
                    playerData.addHearts(-5000);
                    PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
                    break;
                case 9:
                    level = playerData.getDriveFormLevel(Strings.Form_Final);
                    DriveForm drive4 = ModDriveForms.registry.get().getValue(new ResourceLocation(Strings.Form_Final));
                    if (level == 0){
                        playerData.setDriveFormLevel(Strings.Form_Final, 1);
                        playerData.addVisibleDriveForm(Strings.Form_Final);
                    } else {
                        while (playerData.getDriveFormLevel(Strings.Form_Final) < level + 1) {
                            int cost = drive4.getLevelUpCost(playerData.getDriveFormLevel(Strings.Form_Final) + 1);
                            playerData.setDriveFormExp(player, Strings.Form_Final, cost);
                        }
                    }
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


