package online.remind.remind.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.network.cts.*;
import online.remind.remind.network.stc.SCSyncGlobalCapabilityToAllPacketRM;
import online.remind.remind.network.stc.SCSyncGlobalDataRM;

@EventBusSubscriber
public class PacketHandlerRM {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(KingdomKeysReMind.MODID);
        KingdomKeysReMind.LOGGER.info("REGISTERING PACKETS");
        //ServerToClient
        registrar.playToClient(SCSyncGlobalCapabilityToAllPacketRM.TYPE, SCSyncGlobalCapabilityToAllPacketRM.STREAM_CODEC, SCSyncGlobalCapabilityToAllPacketRM::handle);
        registrar.playToClient(SCSyncGlobalDataRM.TYPE, SCSyncGlobalDataRM.STREAM_CODEC, SCSyncGlobalDataRM::handle);


        // ClientToServer
        registrar.playToServer(CSPrestigePacket.TYPE, CSPrestigePacket.STREAM_CODEC, CSPrestigePacket::handle);
        registrar.playToServer(CSSyncAllClientDataPacketRM.TYPE, CSSyncAllClientDataPacketRM.STREAM_CODEC, CSSyncAllClientDataPacketRM::handle);
        registrar.playToServer(CSSetStepTicksPacket.TYPE, CSSetStepTicksPacket.STREAM_CODEC, CSSetStepTicksPacket::handle);
        registrar.playToServer(CSSummonSpiritPacket.TYPE, CSSummonSpiritPacket.STREAM_CODEC, CSSummonSpiritPacket::handle);
        registrar.playToServer(CSPanelPacket.TYPE, CSPanelPacket.STREAM_CODEC, CSPanelPacket::handle);
        registrar.playToServer(CSBoostPacket.TYPE, CSBoostPacket.STREAM_CODEC, CSBoostPacket::handle);
        registrar.playToServer(CSTakeCoins.TYPE,CSTakeCoins.STREAM_CODEC,CSTakeCoins::handle);
        registrar.playToServer(CSChangeSpiritPacket.TYPE,CSChangeSpiritPacket.STREAM_CODEC,CSChangeSpiritPacket::handle);
    }

        public static void sendToServer(CustomPacketPayload msg) {
            PacketDistributor.sendToServer(msg);
        }

        public static void sendTo(CustomPacketPayload msg, ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, msg);
        }


        public static void sendToAllPlayers(CustomPacketPayload msg) {
            PacketDistributor.sendToAllPlayers(msg);
        }


        public static void syncGlobalToAllAround(LivingEntity entity, GlobalDataRM globalData) {
            if (!entity.level().isClientSide) {
                for (Player playerFromList : entity.level().players()) {
                    sendTo(new SCSyncGlobalCapabilityToAllPacketRM(entity.getId(), (GlobalDataRM) globalData), (ServerPlayer) playerFromList);
                }
            }
        }
    }
