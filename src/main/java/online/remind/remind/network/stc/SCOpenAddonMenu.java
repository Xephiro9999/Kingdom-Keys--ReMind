package online.remind.remind.network.stc;

import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.network.ClientPacketHandlerRM;
import online.remind.remind.network.PacketHandlerRM;

public record SCOpenAddonMenu(CompoundTag playerData, boolean open) implements Packet {
    public static final Type<SCOpenAddonMenu> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "sc_open_addon_menu"));
    public static final StreamCodec<FriendlyByteBuf, SCOpenAddonMenu> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            SCOpenAddonMenu::playerData,
            ByteBufCodecs.BOOL,
            SCOpenAddonMenu::open,
            SCOpenAddonMenu::new
    );
    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandlerRM.openMenu(this);
        }
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
