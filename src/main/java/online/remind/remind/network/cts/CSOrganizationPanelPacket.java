package online.remind.remind.network.cts;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PanelPacketAction;
import online.remind.remind.panels.PanelData;
import online.remind.remind.panels.PanelGrid;
import online.remind.remind.panels.PanelRegistry;

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
             * Organization XIII restriction.
             * Remove this if you want non-Org players to test it too.
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
            return;
        }

        PanelGrid grid = globalData.getOrganizationPanelGrid();

        boolean placed = grid.place(panelId, x, y);

        if (!placed) {
            player.displayClientMessage(
                    Component.literal("Panel cannot be placed there.")
                            .withColor(0xFF5555),
                    true
            );
            return;
        }

        player.displayClientMessage(
                Component.literal("Placed panel: " + panelId.getPath())
                        .withColor(0x55FF55),
                true
        );

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
            return;
        }

        player.displayClientMessage(
                Component.literal("Removed panel.")
                        .withColor(0xFFFF55),
                true
        );

        syncBackToClient(player);
    }

    private static void handleClear(ServerPlayer player, GlobalDataRM globalData) {
        globalData.setOrganizationPanelGrid(new PanelGrid(5, 4));

        player.displayClientMessage(
                Component.literal("Panel grid cleared.")
                        .withColor(0xFFFF55),
                true
        );

        syncBackToClient(player);
    }

    private static void syncBackToClient(ServerPlayer player) {
        /*
         * Put your capability sync call here if you already have one.
         * Examples might be something like:
         *
         * ModDataRM.sync(player);
         * PacketHandlerRM.syncGlobalData(player);
         * ModDataRM.syncGlobal(player);
         *
         * If you do not have sync yet, the server data will still save,
         * but the GUI may not visually update until reopened or resynced.
         */
    }
}