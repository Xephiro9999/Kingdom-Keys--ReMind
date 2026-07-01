package online.remind.remind.entity;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.enemies.CactuarEntity;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        bus = EventBusSubscriber.Bus.MOD
)
public class ModSpawnPlacementsRM {

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
                ModEntitiesRM.TYPE_CACTUAR.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                CactuarEntity::checkCactuarSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }
}