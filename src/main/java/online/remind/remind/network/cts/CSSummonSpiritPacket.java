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
import online.remind.remind.entity.mob.ChirithyEntity;
import online.remind.remind.network.PacketHandlerRM;

public class CSSummonSpiritPacket implements CustomPacketPayload {
    public static final Type<CSSummonSpiritPacket> TYPE = new Type(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_summon_spirit"));
    public static final StreamCodec<FriendlyByteBuf, CSSummonSpiritPacket> STREAM_CODEC = StreamCodec.of(CSSummonSpiritPacket::encode, CSSummonSpiritPacket::decode);

    public CSSummonSpiritPacket(){}

    public static void encode(FriendlyByteBuf buffer, CSSummonSpiritPacket message) {
    }

    public static CSSummonSpiritPacket decode(FriendlyByteBuf buffer) {
        CSSummonSpiritPacket msg = new CSSummonSpiritPacket();
        return msg;
    }


    public static void handle(final CSSummonSpiritPacket message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player owner = ctx.player();

            IGlobalDataRM playerData = ModDataRM.getGlobal(owner);
            if (playerData == null)

                return;

            if(!playerData.hasDreamEaterSummoned()) {
            ChirithyEntity dreamEater = new ChirithyEntity(owner.level(), owner);
            owner.level().addFreshEntity(dreamEater);
            dreamEater.setPos(owner.getX(), owner.getY() + 2, owner.getZ());
            dreamEater.getUUID();
            playerData.setHasDreamEaterSummoned(true);
            System.out.println(playerData.hasDreamEaterSummoned());
            playerData.setDreamEaterSummonedID(+1);
            PacketHandlerRM.syncGlobalToAllAround(owner, playerData);
            }
            else if (playerData.hasDreamEaterSummoned()){

                playerData.setHasDreamEaterSummoned(false);
                playerData.setDreamEaterSummonedID(-1);
                PacketHandlerRM.syncGlobalToAllAround(owner, playerData);
                System.out.println(playerData.hasDreamEaterSummoned());
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
