package online.remind.remind.network.stc;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.ClientOrganizationPanelSyncHandler;
import online.remind.remind.panels.PanelGrid;

public record SCOrganizationPanelSyncPacket(
        CompoundTag gridTag,
        CompoundTag ownedPanelsTag,
        int unlockedSlots
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
        buf.writeInt(packet.unlockedSlots);
    }

    private static SCOrganizationPanelSyncPacket decode(RegistryFriendlyByteBuf buf) {
        CompoundTag gridTag = buf.readNbt();
        CompoundTag ownedPanelsTag = buf.readNbt();
        int unlockedSlots = buf.readInt();

        if (gridTag == null) {
            gridTag = new CompoundTag();
        }

        if (ownedPanelsTag == null) {
            ownedPanelsTag = new CompoundTag();
        }

        return new SCOrganizationPanelSyncPacket(gridTag, ownedPanelsTag, unlockedSlots);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SCOrganizationPanelSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClientOrganizationPanelSyncHandler.handle(packet);
            }
        });
    }
}
