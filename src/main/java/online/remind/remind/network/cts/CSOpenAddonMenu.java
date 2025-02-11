package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenMenu;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.network.stc.SCOpenAddonMenu;

public record CSOpenAddonMenu() implements Packet {

    public static final Type<CSOpenAddonMenu> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_open_addon_menu"));
    public static final StreamCodec<FriendlyByteBuf, CSOpenAddonMenu> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {}, pBuffer -> new CSOpenAddonMenu());

    @Override
    public void handle(IPayloadContext context) {
        PlayerData playerData = PlayerData.get(context.player());
        if (playerData.getSoAState() != SoAState.COMPLETE) {
            if (context.player().level().dimension() != ModDimensions.DIVE_TO_THE_HEART) {
                PacketHandler.sendTo(new SCOpenAddonMenu(playerData.serializeNBT(context.player().level().registryAccess()), false), (ServerPlayer) context.player());
            }
        } else {
            PacketHandler.sendTo(new SCOpenAddonMenu(playerData.serializeNBT(context.player().level().registryAccess()), true), (ServerPlayer) context.player());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
