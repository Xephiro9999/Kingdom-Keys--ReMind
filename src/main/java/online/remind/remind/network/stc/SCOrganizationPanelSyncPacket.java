package online.remind.remind.network.stc;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.panels.PanelGrid;

public record SCOrganizationPanelSyncPacket(
        CompoundTag gridTag,
        CompoundTag ownedPanelsTag
) implements CustomPacketPayload {

    public static final Type<SCOrganizationPanelSyncPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "sc_organization_panel_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SCOrganizationPanelSyncPacket> STREAM_CODEC =
            StreamCodec.of(
                    SCOrganizationPanelSyncPacket::encode,
                    SCOrganizationPanelSyncPacket::decode
            );

    private static void encode(RegistryFriendlyByteBuf buf, SCOrganizationPanelSyncPacket packet) {
        buf.writeNbt(packet.gridTag);
        buf.writeNbt(packet.ownedPanelsTag);
    }

    private static SCOrganizationPanelSyncPacket decode(RegistryFriendlyByteBuf buf) {
        CompoundTag gridTag = buf.readNbt();
        CompoundTag ownedPanelsTag = buf.readNbt();

        if (gridTag == null) {
            gridTag = new CompoundTag();
        }

        if (ownedPanelsTag == null) {
            ownedPanelsTag = new CompoundTag();
        }

        return new SCOrganizationPanelSyncPacket(gridTag, ownedPanelsTag);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SCOrganizationPanelSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.player == null) {
                return;
            }

            GlobalDataRM globalData = ModDataRM.getGlobal(minecraft.player);

            if (globalData == null) {
                return;
            }

            globalData.setOrganizationPanelGrid(PanelGrid.load(packet.gridTag));

            globalData.getOwnedOrganizationPanels().clear();

            for (String key : packet.ownedPanelsTag.getAllKeys()) {
                globalData.getOwnedOrganizationPanels().put(key, packet.ownedPanelsTag.getInt(key));
            }
        });
    }
}
