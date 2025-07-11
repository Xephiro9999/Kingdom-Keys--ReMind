package online.remind.remind.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import online.kingdomkeys.kingdomkeys.client.gui.IPlayerDataRequester;
import online.kingdomkeys.kingdomkeys.client.gui.menu.NoChoiceMenuPopup;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.stc.SCSendPlayerDataToClient;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.client.gui.AddonMenu;
import online.remind.remind.network.cts.*;
import online.remind.remind.network.stc.SCOpenAddonMenu;
import online.remind.remind.network.stc.SCSyncGlobalCapabilityToAllPacketRM;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class PacketHandlerRM {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(KingdomKeysReMind.MODID);
        KingdomKeysReMind.LOGGER.info("REGISTERING PACKETS");
        //ServerToClient
        registrar.playToClient(SCOpenAddonMenu.TYPE, SCOpenAddonMenu.STREAM_CODEC, (payload, context) -> {
            context.enqueueWork(() -> ((SCOpenAddonMenu) payload).handle(context));
        });
        registrar.playToClient(SCSyncGlobalCapabilityToAllPacketRM.TYPE, SCSyncGlobalCapabilityToAllPacketRM.STREAM_CODEC, SCSyncGlobalCapabilityToAllPacketRM::handle);

        // ClientToServer
        registrar.playToServer(CSPrestigePacket.TYPE, CSPrestigePacket.STREAM_CODEC, CSPrestigePacket::handle);
        registrar.playToServer(CSSyncAllClientDataPacketRM.TYPE, CSSyncAllClientDataPacketRM.STREAM_CODEC, CSSyncAllClientDataPacketRM::handle);
        registrar.playToServer(CSSetStepTicksPacket.TYPE, CSSetStepTicksPacket.STREAM_CODEC, CSSetStepTicksPacket::handle);
        registrar.playToServer(CSSummonSpiritPacket.TYPE, CSSummonSpiritPacket.STREAM_CODEC, CSSummonSpiritPacket::handle);
        registrar.playToServer(CSPanelPacket.TYPE, CSPanelPacket.STREAM_CODEC, CSPanelPacket::handle);
        registrar.playToServer(CSBoostPacket.TYPE, CSBoostPacket.STREAM_CODEC, CSBoostPacket::handle);
        registrar.playToServer(CSOpenAddonMenu.TYPE,CSOpenAddonMenu.STREAM_CODEC,CSOpenAddonMenu::handle);
        registrar.playToServer(CSTakeCoins.TYPE,CSTakeCoins.STREAM_CODEC,CSTakeCoins::handle);
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


        public static void syncGlobalToAllAround(LivingEntity entity, IGlobalDataRM globalData) {
            if (!entity.level().isClientSide) {
                for (Player playerFromList : entity.level().players()) {
                    sendTo(new SCSyncGlobalCapabilityToAllPacketRM(entity.getId(), (IGlobalDataRM) globalData), (ServerPlayer) playerFromList);
                }
            }
        }
    }
