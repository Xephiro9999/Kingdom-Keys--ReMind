package online.remind.remind.network.stc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.remind.remind.client.ClientPacketHandlerRM;

public record SCSyncGlobalDataRM(int player, CompoundTag data) implements Packet {

	public SCSyncGlobalDataRM(Player player) {
		this(player.getId(), PlayerData.get(player).serializeNBT(player.level().registryAccess()));
	}

	public static final Type<SCSyncGlobalDataRM> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_sync_global_data_auto"));

	public static final StreamCodec<FriendlyByteBuf, SCSyncGlobalDataRM> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			SCSyncGlobalDataRM::player,
			ByteBufCodecs.COMPOUND_TAG,
			SCSyncGlobalDataRM::data,
			SCSyncGlobalDataRM::new
	);

	public SCSyncGlobalDataRM(Player player, PlayerData playerData) {
		this(player.getId(), playerData.serializeNBT(player.level().registryAccess()));
	}

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandlerRM.syncCapability(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
