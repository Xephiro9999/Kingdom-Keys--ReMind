package online.remind.remind.dreameater;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.entity.spirits.CactuarSpiritEntity;
import online.remind.remind.entity.spirits.ChirithyEntity;
import online.remind.remind.entity.spirits.KomoryBatEntity;
import online.remind.remind.entity.spirits.MeowWowEntity;
import online.remind.remind.entity.spirits.TonberrySpiritEntity;
import online.remind.remind.network.PacketHandlerRM;

import java.util.UUID;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        bus = EventBusSubscriber.Bus.GAME
)
public class DreamEaterDeathHandler {


    // Dream Eater death cooldown
    @SubscribeEvent
    public static void onDreamEaterDeath(LivingDeathEvent event) {

        LivingEntity dreamEater =
                event.getEntity();

        if (!(dreamEater.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        UUID ownerUUID =
                getOwnerUUID(dreamEater);

        if (ownerUUID == null) {
            return;
        }

        ServerPlayer owner =
                serverLevel
                        .getServer()
                        .getPlayerList()
                        .getPlayer(ownerUUID);

        if (owner == null) {
            return;
        }

        GlobalDataRM globalData =
                ModDataRM.getGlobal(owner);

        if (globalData == null) {
            return;
        }

        UUID summonedUUID =
                globalData.getDreamEaterUUID();

        if (summonedUUID == null
                || !summonedUUID.equals(dreamEater.getUUID())) {
            return;
        }

        DreamEaterSummonCooldown.start(owner);

        globalData.setDreamEaterUUID(null);
        globalData.setHasDreamEaterSummoned(false);

        PacketHandlerRM.syncGlobalToAllAround(
                owner,
                globalData
        );
    }


    // Dream Eater owner lookup
    private static UUID getOwnerUUID(LivingEntity entity) {

        if (entity instanceof ChirithyEntity chirithy) {
            return chirithy.getOwnerUUID();
        }

        if (entity instanceof MeowWowEntity meowWow) {
            return meowWow.getOwnerUUID();
        }

        if (entity instanceof KomoryBatEntity komoryBat) {
            return komoryBat.getOwnerUUID();
        }

        if (entity instanceof CactuarSpiritEntity cactuar) {
            return cactuar.getOwnerUUID();
        }

        if (entity instanceof TonberrySpiritEntity tonberry) {
            return tonberry.getOwnerUUID();
        }

        return null;
    }
}