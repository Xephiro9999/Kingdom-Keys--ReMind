package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.network.PacketHandlerRM;

public class CSPanelPacket implements CustomPacketPayload {
    public static final Type<CSPanelPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_panel_packet"));
    public static final StreamCodec<FriendlyByteBuf, CSPanelPacket> STREAM_CODEC = StreamCodec.of(CSPanelPacket::encode, CSPanelPacket::decode);

    // 0 = none (default), 1 = str, 2 = mag, 3 = def...
    private int choice;

    public CSPanelPacket() {
    }

    public CSPanelPacket(int choice) {
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
            GlobalDataRM globalData = ModDataRM.getGlobal(player);

            int level;
            int xpGain;
            int totalBoost;
            float heartsRegained;


            //System.out.println(globalData.getPanelChoice());

            switch (message.choice) {
                case 0:
                    System.out.println("This shouldn't happen");
                    break;
                case 1:
                    PacketHandlerRM.syncGlobalToAllAround(player, globalData);
                    playerData.addHearts(-1000 * globalData.getSTRPanel() + 1);
                    globalData.addSTRPanel(ModConfigs.panelBonus);
                    if (globalData.getSTRPanel() > ModConfigs.panelLimit) {
                        playerData.getStrengthStat().addModifier("Panel", ModConfigs.panelLimit, false, false);
                    }
                    playerData.getStrengthStat().addModifier("Panel", globalData.getSTRPanel(), false, false);
                    break;
                case 2:
                    PacketHandlerRM.syncGlobalToAllAround(player, globalData);
                    playerData.addHearts(-1000 * globalData.getMAGPanel() + 1);
                    globalData.addMAGPanel(ModConfigs.panelBonus);
                    if (globalData.getMAGPanel() > ModConfigs.panelLimit) {
                        playerData.getMagicStat().addModifier("Panel", ModConfigs.panelLimit, false, false);
                    }
                    playerData.getMagicStat().addModifier("Panel", globalData.getMAGPanel(), false, false);
                    break;
                case 3:
                    PacketHandlerRM.syncGlobalToAllAround(player, globalData);
                    playerData.addHearts(-1000 * globalData.getDEFPanel() + 1);
                    globalData.addDEFPanel(ModConfigs.panelBonus);
                    if (globalData.getDEFPanel() > ModConfigs.panelLimit) {
                        playerData.getDefenseStat().addModifier("Panel", ModConfigs.panelLimit, false, false);
                    }
                    //System.out.println(globalData.getDEFPanel());
                    playerData.getDefenseStat().addModifier("Panel", globalData.getDEFPanel(), false, false);
                    break;
                case 4:
                    PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
                    playerData.addMaxAP(2);
                    playerData.addHearts(-1000);
                    break;
                case 5:
                    PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
                    level = playerData.getDriveFormLevel(ModDriveForms.VALOR.location());
                    DriveForm drive = ModDriveForms.VALOR.get();
                    if (level == 0) {
                        playerData.setDriveFormLevel(ModDriveForms.VALOR.location(), 1);
                    } else {
                        while (playerData.getDriveFormLevel(ModDriveForms.VALOR.location()) < level + 1) {
                            int cost = drive.getLevelUpCost(playerData.getDriveFormLevel(ModDriveForms.VALOR.location()) + 1);
                            playerData.setDriveFormExp(player, ModDriveForms.VALOR.location(), cost);
                        }
                    }

                    playerData.addHearts(-5000);
                    break;
                case 6:
                    PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
                    level = playerData.getDriveFormLevel(ModDriveForms.WISDOM.location());
                    DriveForm drive1 = ModDriveForms.WISDOM.get();
                    if (level == 0) {
                        playerData.setDriveFormLevel(ModDriveForms.WISDOM.location(), 1);
                    } else {
                        while (playerData.getDriveFormLevel(ModDriveForms.WISDOM.location()) < level + 1) {
                            int cost = drive1.getLevelUpCost(playerData.getDriveFormLevel(ModDriveForms.WISDOM.location()) + 1);
                            playerData.setDriveFormExp(player, ModDriveForms.WISDOM.location(), cost);
                        }
                    }
                    playerData.addHearts(-5000);
                    break;
                case 7:
                    PacketHandler.sendTo(new SCSyncPlayerData(player), player);
                    level = playerData.getDriveFormLevel(ModDriveForms.LIMIT.location());
                    DriveForm drive2 = ModDriveForms.LIMIT.get();
                    if (level == 0) {
                        playerData.setDriveFormLevel(ModDriveForms.LIMIT.location(), 1);
                    } else {
                        while (playerData.getDriveFormLevel(ModDriveForms.LIMIT.location()) < level + 1) {
                            int cost = drive2.getLevelUpCost(playerData.getDriveFormLevel(ModDriveForms.LIMIT.location()) + 1);
                            playerData.setDriveFormExp(player, ModDriveForms.LIMIT.location(), cost);
                        }
                    }

                    playerData.addHearts(-5000);
                    break;
                case 8:
                    PacketHandler.sendTo(new SCSyncPlayerData(player), player);
                    level = playerData.getDriveFormLevel(ModDriveForms.MASTER.location());
                    DriveForm drive3 = ModDriveForms.MASTER.get();
                    if (level == 0) {
                        playerData.setDriveFormLevel(ModDriveForms.MASTER.location(), 1);
                    } else {
                        while (playerData.getDriveFormLevel(ModDriveForms.MASTER.location()) < level + 1) {
                            int cost = drive3.getLevelUpCost(playerData.getDriveFormLevel(ModDriveForms.MASTER.location()) + 1);
                            playerData.setDriveFormExp(player, ModDriveForms.MASTER.location(), cost);
                        }
                    }
                    playerData.addHearts(-5000);
                    break;
                case 9:
                    PacketHandler.sendTo(new SCSyncPlayerData(player), player);
                    level = playerData.getDriveFormLevel(ModDriveForms.FINAL.location());
                    DriveForm drive4 = ModDriveForms.FINAL.get();
                    if (level == 0) {
                        playerData.setDriveFormLevel(ModDriveForms.FINAL.location(), 1);
                    } else {
                        while (playerData.getDriveFormLevel(ModDriveForms.FINAL.location()) < level + 1) {
                            int cost = drive4.getLevelUpCost(playerData.getDriveFormLevel(ModDriveForms.FINAL.location()) + 1);
                            playerData.setDriveFormExp(player, ModDriveForms.FINAL.location(), cost);
                        }
                    }
                    playerData.addHearts(-5000);
                    break;
                case 10:
                    playerData.addHearts(-1000 * playerData.getLevel());
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

                    //System.out.println(totalBoost);
                    //System.out.println(heartsRegained);
                    break;
                case 12:
                    playerData.addMaxArmors(1);
                    playerData.addHearts(-10000);
                    PacketHandler.sendTo(new SCSyncPlayerData(player), player);
                    break;
                case 13:
                    playerData.addMaxAccessories(1);
                    playerData.addHearts(-10000);
                    PacketHandler.sendTo(new SCSyncPlayerData(player), player);
                    break;
                case 14:
                    if (playerData.getHearts() >= 13000) {
                        PacketHandler.sendTo(new SCSyncPlayerData(player), player);
                        playerData.setAlignment(0);
                        playerData.addHearts(-13000);
                    } else {
                        player.sendSystemMessage(Component.literal("You do not have enough Hearts to do this."));
                        player.playSound(ModSounds.error.get());
                    }
                    break;
            }

            // Failsafe

            if (globalData.getSTRPanel() > ModConfigs.panelLimit) {
                playerData.getStrengthStat().addModifier("Panel", ModConfigs.panelLimit, false, false);
                globalData.setSTRPanel(ModConfigs.panelLimit);
            }
            if (globalData.getMAGPanel() > ModConfigs.panelLimit) {
                playerData.getMagicStat().addModifier("Panel", ModConfigs.panelLimit, false, false);
                globalData.setMAGPanel(ModConfigs.panelLimit);
            }
            if (globalData.getDEFPanel() > ModConfigs.panelLimit) {
                playerData.getDefenseStat().addModifier("Panel", ModConfigs.panelLimit, false, false);
                globalData.setDEFPanel(ModConfigs.panelLimit);
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


