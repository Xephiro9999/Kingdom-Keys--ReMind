package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;

public class CSSyncAllClientDataPacketRM implements CustomPacketPayload {
	public static final Type<CSSyncAllClientDataPacketRM> TYPE = new Type(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_sync_all_client_data"));
	public static final StreamCodec<FriendlyByteBuf, CSSyncAllClientDataPacketRM> STREAM_CODEC = StreamCodec.of(CSSyncAllClientDataPacketRM::encode, CSSyncAllClientDataPacketRM::decode);

	public CSSyncAllClientDataPacketRM() {
		}

	public static void encode(FriendlyByteBuf buffer, CSSyncAllClientDataPacketRM message) {

	}

	public static CSSyncAllClientDataPacketRM decode(FriendlyByteBuf buffer) {
		CSSyncAllClientDataPacketRM msg = new CSSyncAllClientDataPacketRM();

		return msg;
	}

	public static void handle(final CSSyncAllClientDataPacketRM message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			Player player = ctx.player();

			IGlobalDataRM globalData = ModDataRM.getGlobal(player);
			PacketHandlerRM.syncGlobalToAllAround(player, globalData);
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}