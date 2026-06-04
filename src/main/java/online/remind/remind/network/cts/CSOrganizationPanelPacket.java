package online.remind.remind.network.cts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PanelPacketAction;
import online.remind.remind.network.stc.SCOrganizationPanelSyncPacket;
import online.remind.remind.panels.*;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.remind.remind.panels.OrganizationPanelAbilityHelper;

import java.util.Map;

public record CSOrganizationPanelPacket(
        PanelPacketAction action,
        ResourceLocation panelId,
        int x,
        int y
) implements CustomPacketPayload {

    public static final Type<CSOrganizationPanelPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_organization_panel"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CSOrganizationPanelPacket> STREAM_CODEC =
            StreamCodec.of(
                    CSOrganizationPanelPacket::encode,
                    CSOrganizationPanelPacket::decode
            );

    private static void encode(RegistryFriendlyByteBuf buf, CSOrganizationPanelPacket packet) {
        buf.writeEnum(packet.action);
        buf.writeResourceLocation(packet.panelId);
        buf.writeInt(packet.x);
        buf.writeInt(packet.y);
    }

    private static CSOrganizationPanelPacket decode(RegistryFriendlyByteBuf buf) {
        return new CSOrganizationPanelPacket(
                buf.readEnum(PanelPacketAction.class),
                buf.readResourceLocation(),
                buf.readInt(),
                buf.readInt()
        );
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CSOrganizationPanelPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }

            PlayerData playerData = PlayerData.get(player);
            GlobalDataRM globalData = ModDataRM.getGlobal(player);

            if (playerData == null || globalData == null) {
                return;
            }

            /*
             * Keep this if only Organization XIII members should use the panel grid.
             * Remove this block if you want to test with everyone.
             */
            if (playerData.getAlignment() == Utils.OrgMember.NONE) {
                player.displayClientMessage(
                        Component.literal("Only Organization XIII members can use the Panel System.")
                                .withColor(0xFF5555),
                        true
                );
                return;
            }

            switch (packet.action) {
                case PLACE -> handlePlace(player, globalData, packet.panelId, packet.x, packet.y);
                case REMOVE -> handleRemove(player, globalData, packet.x, packet.y);
                case CLEAR -> handleClear(player, globalData);
            }
        });
    }

    private static void handlePlace(ServerPlayer player, GlobalDataRM globalData, ResourceLocation panelId, int x, int y) {
        PanelData data = PanelRegistry.get(panelId);

        if (data == null) {
            player.displayClientMessage(
                    Component.literal("Unknown panel: " + panelId)
                            .withColor(0xFF5555),
                    true
            );

            syncBackToClient(player);
            return;
        }

        boolean placed = globalData.placeOrganizationPanel(panelId, x, y);

        if (!placed) {
            player.displayClientMessage(
                    Component.literal("Panel cannot be placed there, or you do not own one.")
                            .withColor(0xFF5555),
                    true
            );

            syncBackToClient(player);
            return;
        }

        player.displayClientMessage(
                Component.literal("Placed panel: " + panelId.getPath())
                        .withColor(0x55FF55),
                true
        );

        OrganizationPanelStatHelper.refreshPanelModifiersIfEnabled(player);
        syncBackToClient(player);
    }

    private static void handleRemove(ServerPlayer player, GlobalDataRM globalData, int x, int y) {
        boolean removed = globalData.removeOrganizationPanelAt(x, y);

        if (!removed) {
            player.displayClientMessage(
                    Component.literal("No panel found there.")
                            .withColor(0xFF5555),
                    true
            );

            syncBackToClient(player);
            return;
        }

        player.displayClientMessage(
                Component.literal("Removed panel.")
                        .withColor(0xFFFF55),
                true
        );

        OrganizationPanelStatHelper.refreshPanelModifiersIfEnabled(player);
        syncBackToClient(player);
    }

    private static void handleClear(ServerPlayer player, GlobalDataRM globalData) {
        PanelGrid oldGrid = globalData.getOrganizationPanelGrid();

        for (PanelSlot slot : oldGrid.getPlacedPanels()) {
            globalData.refundOwnedOrganizationPanel(slot.getPanelId());
        }

        globalData.setOrganizationPanelGrid(new PanelGrid(5, 4));

        player.displayClientMessage(
                Component.literal("Panel grid cleared.")
                        .withColor(0xFFFF55),
                true
        );

        OrganizationPanelStatHelper.refreshPanelModifiersIfEnabled(player);
        syncBackToClient(player);
    }

    private static void syncBackToClient(ServerPlayer player) {
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null) {
            return;
        }

        /*
         * Recalculate everything the panel grid grants.
         * This is important after PLACE / REMOVE / CLEAR.
         */
        OrganizationPanelStatHelper.refreshPanelModifiersIfEnabled(player);
        OrganizationPanelAbilityHelper.markPanelAbilityRefreshDirty(player);

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
}