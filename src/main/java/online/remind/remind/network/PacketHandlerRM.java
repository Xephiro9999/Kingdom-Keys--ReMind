package online.remind.remind.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.network.cts.*;
import online.remind.remind.network.stc.SCSyncGlobalCapabilityToAllPacketRM;

public class PacketHandlerRM {
    private static final String PROTOCOL_VERSION = Integer.toString(1);

	private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(KingdomKeysReMind.MODID, "main_channel")).clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();

    public static void register() {
        int packetID = 0;
        System.out.println("REGISTERING PACKETS");
        //ServerToClient
		HANDLER.registerMessage(packetID++, SCSyncGlobalCapabilityToAllPacketRM.class, SCSyncGlobalCapabilityToAllPacketRM::encode, SCSyncGlobalCapabilityToAllPacketRM::decode, SCSyncGlobalCapabilityToAllPacketRM::handle);

        // ClientToServer
        HANDLER.registerMessage(packetID++, CSPrestigePacket.class, CSPrestigePacket::encode, CSPrestigePacket::decode, CSPrestigePacket::handle);
        HANDLER.registerMessage(packetID++, CSSyncAllClientDataPacketRM.class, CSSyncAllClientDataPacketRM::encode, CSSyncAllClientDataPacketRM::decode, CSSyncAllClientDataPacketRM::handle);
        HANDLER.registerMessage(packetID++, CSSetStepTicksPacket.class,CSSetStepTicksPacket::encode,CSSetStepTicksPacket::decode,CSSetStepTicksPacket::handle);
        HANDLER.registerMessage(packetID++, CSSummonSpiritPacket.class,CSSummonSpiritPacket::encode,CSSummonSpiritPacket::decode,CSSummonSpiritPacket::handle);
        HANDLER.registerMessage(packetID++, CSPanelPacket.class,CSPanelPacket::encode,CSPanelPacket::decode, CSPanelPacket::handle);
    }

        public static <MSG> void sendToServer(MSG msg) {
            HANDLER.sendToServer(msg);
        }

        public static <MSG> void sendTo(MSG msg, ServerPlayer player) {
            if (!(player instanceof FakePlayer)) {
                HANDLER.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }
        }


        public static <MSG> void sendToAllPlayers(MSG msg) {
            HANDLER.send(PacketDistributor.ALL.noArg(), msg);
        }

        public static void syncGlobalToAllAround(LivingEntity entity, IGlobalCapabilitiesRM globalData) {
            if (!entity.level().isClientSide) {
                for (Player playerFromList : entity.level().players()) {
                    sendTo(new SCSyncGlobalCapabilityToAllPacketRM(entity.getId(), (IGlobalCapabilitiesRM) globalData), (ServerPlayer) playerFromList);
                }
            }
        }
    }
