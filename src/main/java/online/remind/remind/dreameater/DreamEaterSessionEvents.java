package online.remind.remind.dreameater;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.entity.spirits.CactuarSpiritEntity;
import online.remind.remind.entity.spirits.ChirithyEntity;
import online.remind.remind.entity.spirits.KomoryBatEntity;
import online.remind.remind.entity.spirits.MeowWowEntity;

import java.util.UUID;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        bus = EventBusSubscriber.Bus.GAME
)
public class DreamEaterSessionEvents {

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        cleanupDreamEaterOnLogout(player);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        /*
         * Safety cleanup in case the server crashed, player disconnected weirdly,
         * or old saved data still says a Dream Eater is summoned.
         */
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null) {
            return;
        }

        globalData.setHasDreamEaterSummoned(false);
        globalData.setDreamEaterUUID(null);

        removeAllDreamEatersForPlayer(player);
    }

    private static void cleanupDreamEaterOnLogout(ServerPlayer player) {
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData != null) {
            UUID dreamEaterUUID = globalData.getDreamEaterUUID();

            if (dreamEaterUUID != null) {
                removeEntityByUUID(player, dreamEaterUUID);
            }

            globalData.setHasDreamEaterSummoned(false);
            globalData.setDreamEaterUUID(null);
        }

        removeAllDreamEatersForPlayer(player);

        /*
         * This method needs to be added to DreamEaterAbilityLinkHelper below.
         */
        DreamEaterAbilityLinkHelper.clearAbilityLinkGrantsForLogout(player);
    }

    private static void removeEntityByUUID(ServerPlayer player, UUID uuid) {
        MinecraftServer server = player.getServer();

        if (server == null || uuid == null) {
            return;
        }

        for (ServerLevel level : server.getAllLevels()) {
            Entity entity = level.getEntity(uuid);

            if (entity != null) {
                entity.discard();
                return;
            }
        }
    }

    private static void removeAllDreamEatersForPlayer(ServerPlayer player) {
        if (player == null || player.getServer() == null) {
            return;
        }

        UUID ownerUUID = player.getUUID();

        for (ServerLevel level : player.getServer().getAllLevels()) {
            ChirithyEntity.removeExistingChirithy(level, ownerUUID);
            MeowWowEntity.removeExistingMeowWow(level, ownerUUID);
            KomoryBatEntity.removeExistingKomoryBat(level, ownerUUID);
            CactuarSpiritEntity.removeExistingCactuarSpirit(level, ownerUUID);
        }
    }
}