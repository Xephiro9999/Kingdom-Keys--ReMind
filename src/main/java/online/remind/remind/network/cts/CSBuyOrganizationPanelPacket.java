package online.remind.remind.network.cts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.network.stc.SCOrganizationPanelSyncPacket;
import online.remind.remind.panels.OrganizationPanelRewardHelper;
import online.remind.remind.panels.PanelData;
import online.remind.remind.panels.PanelRegistry;

import java.util.Map;

public class CSBuyOrganizationPanelPacket implements CustomPacketPayload {

    public static final Type<CSBuyOrganizationPanelPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_buy_organization_panel"));

    public static final StreamCodec<FriendlyByteBuf, CSBuyOrganizationPanelPacket> STREAM_CODEC =
            StreamCodec.of(CSBuyOrganizationPanelPacket::encode, CSBuyOrganizationPanelPacket::decode);

    private ResourceLocation panelId;
    private int amount;

    public CSBuyOrganizationPanelPacket() {
    }

    public CSBuyOrganizationPanelPacket(ResourceLocation panelId, int amount) {
        this.panelId = panelId;
        this.amount = amount;
    }

    private static void encode(FriendlyByteBuf buf, CSBuyOrganizationPanelPacket packet) {
        buf.writeResourceLocation(packet.panelId);
        buf.writeInt(packet.amount);
    }

    private static CSBuyOrganizationPanelPacket decode(FriendlyByteBuf buf) {
        return new CSBuyOrganizationPanelPacket(
                buf.readResourceLocation(),
                buf.readInt()
        );
    }

    public static void handle(final CSBuyOrganizationPanelPacket packet, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();

            if (!(player instanceof ServerPlayer serverPlayer)) {
                return;
            }

            PlayerData playerData = PlayerData.get(serverPlayer);
            GlobalDataRM globalData = ModDataRM.getGlobal(serverPlayer);

            if (playerData == null || globalData == null) {
                return;
            }

            if (packet.panelId == null || packet.amount <= 0) {
                return;
            }

            int cost = getPanelCost(packet.panelId, packet.amount);

            if (cost <= 0) {
                serverPlayer.displayClientMessage(
                        Component.literal("That panel cannot be purchased.")
                                .withColor(0xFF5555),
                        true
                );
                syncPlayerData(serverPlayer);
                return;
            }

            boolean buyingSlotReleaser = packet.panelId.equals(
                    ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "slot_releaser")
            );

            if (!buyingSlotReleaser) {
                PanelData data = PanelRegistry.get(packet.panelId);

                if (data == null) {
                    serverPlayer.displayClientMessage(
                            Component.literal("Unknown panel: " + packet.panelId)
                                    .withColor(0xFF5555),
                            true
                    );
                    syncPlayerData(serverPlayer);
                    return;
                }
            }

            if (buyingSlotReleaser
                    && globalData.getUnlockedOrganizationPanelSlots() >= GlobalDataRM.ORGANIZATION_PANEL_MAX_SLOTS) {
                serverPlayer.displayClientMessage(
                        Component.literal("Your Panel Grid is already fully expanded.")
                                .withColor(0xFF5555),
                        true
                );
                syncPlayerData(serverPlayer);
                return;
            }

            if (playerData.getHearts() < cost) {
                serverPlayer.displayClientMessage(
                        Component.literal("Not enough Hearts. Need " + cost + ".")
                                .withColor(0xFF5555),
                        true
                );
                syncPlayerData(serverPlayer);
                return;
            }

            /*
             * Charge first, then grant.
             * If grant fails, refund and sync.
             */
            playerData.addHearts(-cost);

            boolean success;

            if (buyingSlotReleaser) {
                success = OrganizationPanelRewardHelper.useSlotReleaser(serverPlayer);
            } else {
                success = OrganizationPanelRewardHelper.grantOrganizationPanel(
                        serverPlayer,
                        packet.panelId,
                        packet.amount
                );
            }

            if (!success) {
                playerData.addHearts(cost);

                serverPlayer.displayClientMessage(
                        Component.literal("Purchase failed.")
                                .withColor(0xFF5555),
                        true
                );

                PacketHandler.sendTo(new SCSyncPlayerData(serverPlayer), serverPlayer);
                PacketHandlerRM.syncGlobalToAllAround(serverPlayer, globalData);
                syncOrganizationPanelsToClient(serverPlayer, globalData);
                return;
            }

            PacketHandler.sendTo(new SCSyncPlayerData(serverPlayer), serverPlayer);
            PacketHandlerRM.syncGlobalToAllAround(serverPlayer, globalData);
            syncOrganizationPanelsToClient(serverPlayer, globalData);

            serverPlayer.displayClientMessage(
                    Component.literal("Purchased for " + cost + " Hearts.")
                            .withColor(0x55FF55),
                    true
            );
        });
    }

    private static int getPanelCost(ResourceLocation panelId, int amount) {
        String path = panelId.getPath();

        int singleCost = switch (path) {
            case "strength_unit" -> 1000;
            case "magic_unit" -> 1000;
            case "defense_unit" -> 1000;
            case "ap_unit" -> 500;
            case "sight_unit" -> 1000;
            case "level_up" -> 2000;

            case "strength_unit_l" -> 2000;
            case "magic_unit_l" -> 2000;
            case "defense_unit_l" -> 2000;
            case "ap_unit_l" -> 1000;
            case "level_doubler" -> 4000;

            case "power_link" -> 2500;
            case "magic_link" -> 2500;
            case "guard_link" -> 2500;
            case "level_link" -> 4500;

            case "level_doubler_l_right" -> 4500;
            case "level_doubler_l_left" -> 4500;
            case "level_doubler_l_top_right" -> 4500;
            case "level_doubler_l_top_left" -> 4500;
            case "level_doubler_line" -> 4500;

            case "ultima_weapon_panel" -> 50000;
            case "hearts_power_panel" -> 50000;

            case "high_jump_panel" -> 2500;
            case "dodge_roll_panel" -> 2500;
            case "aerial_dodge_panel" -> 3000;
            case "quick_run_panel" -> 3000;
            case "glide_panel" -> 4000;

            case "slot_releaser" -> 10000;

            case "combo_plus_panel" -> 2500;
            case "haste_panel" -> 2500;

            case "fire_boost_panel" -> 3000;
            case "blizzard_boost_panel" -> 3000;
            case "thunder_boost_panel" -> 3000;

            case "draw_panel" -> 2000;
            case "jackpot_panel" -> 2500;
            case "lucky_lucky_panel" -> 4000;

            default -> -1;
        };

        if (singleCost <= 0) {
            return -1;
        }

        return singleCost * amount;
    }

    private static void syncPlayerData(ServerPlayer player) {
        PacketHandler.sendTo(new SCSyncPlayerData(player), player);
    }

    private static void syncAfterBuy(ServerPlayer player, GlobalDataRM globalData) {
        syncPlayerData(player);

        PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        syncOrganizationPanelsToClient(player, globalData);
    }

    private static void syncOrganizationPanelsToClient(ServerPlayer player, GlobalDataRM globalData) {
        CompoundTag ownedPanelsTag = new CompoundTag();

        for (Map.Entry<String, Integer> entry : globalData.getOwnedOrganizationPanels().entrySet()) {
            ownedPanelsTag.putInt(entry.getKey(), entry.getValue());
        }

        PacketDistributor.sendToPlayer(
                player,
                new SCOrganizationPanelSyncPacket(
                        globalData.getOrganizationPanelGrid().save(),
                        ownedPanelsTag,
                        globalData.getUnlockedOrganizationPanelSlots()
                )
        );
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}