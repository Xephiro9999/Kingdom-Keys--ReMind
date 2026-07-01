package online.remind.remind.entity.enemies;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.ModEntitiesRM;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        bus = EventBusSubscriber.Bus.GAME
)
public class CactuarDesertDaySpawner {

    private static final ResourceKey<Biome> DESERT_BIOME =
            ResourceKey.create(
                    Registries.BIOME,
                    ResourceLocation.fromNamespaceAndPath("minecraft", "desert")
            );

    private static final Map<UUID, Integer> PLAYER_SPAWN_COOLDOWNS = new HashMap<>();

    private static final int CHECK_INTERVAL_TICKS = 100;
    private static final int SPAWN_COOLDOWN_TICKS = 20 * 7;

    private static final double SPAWN_CHANCE = 0.36D;

    private static final int MIN_SPAWN_DISTANCE = 24;
    private static final int MAX_SPAWN_DISTANCE = 48;

    private static final int MAX_NEARBY_CACTUAR = 5;
    private static final double NEARBY_CHECK_RANGE = 96.0D;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        if (level.isClientSide) {
            return;
        }

        if (player.tickCount % CHECK_INTERVAL_TICKS != 0) {
            return;
        }

        tickCooldown(player);

        if (!canTrySpawn(player, level)) {
            return;
        }

        if (level.random.nextDouble() > SPAWN_CHANCE) {
            return;
        }

        if (countNearbyCactuar(player) >= MAX_NEARBY_CACTUAR) {
            return;
        }

        BlockPos spawnPos = findSpawnPos(level, player);

        if (spawnPos == null) {
            return;
        }

        CactuarEntity cactuar = ModEntitiesRM.TYPE_CACTUAR.get().create(level);

        if (cactuar == null) {
            return;
        }

        cactuar.setVariant(CactuarEntity.VARIANT_NORMAL);
        cactuar.moveTo(
                spawnPos.getX() + 0.5D,
                spawnPos.getY(),
                spawnPos.getZ() + 0.5D,
                level.random.nextFloat() * 360.0F,
                0.0F
        );

        cactuar.finalizeSpawn(
                level,
                level.getCurrentDifficultyAt(spawnPos),
                MobSpawnType.NATURAL,
                null
        );

        level.addFreshEntity(cactuar);

        PLAYER_SPAWN_COOLDOWNS.put(player.getUUID(), SPAWN_COOLDOWN_TICKS);
    }

    private static boolean canTrySpawn(ServerPlayer player, ServerLevel level) {
        if (player == null || level == null) {
            return false;
        }

        if (level.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }

        if (!level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            return false;
        }

        /*
         * This spawner is only here to cover daytime.
         * Nighttime is already handled by the biome modifier JSON.
         */
        if (!level.isDay()) {
            return false;
        }

        if (!level.getBiome(player.blockPosition()).is(DESERT_BIOME)) {
            return false;
        }

        return PLAYER_SPAWN_COOLDOWNS.getOrDefault(player.getUUID(), 0) <= 0;
    }

    private static void tickCooldown(ServerPlayer player) {
        UUID uuid = player.getUUID();

        int cooldown = PLAYER_SPAWN_COOLDOWNS.getOrDefault(uuid, 0);

        if (cooldown <= 0) {
            PLAYER_SPAWN_COOLDOWNS.remove(uuid);
            return;
        }

        cooldown -= CHECK_INTERVAL_TICKS;

        if (cooldown <= 0) {
            PLAYER_SPAWN_COOLDOWNS.remove(uuid);
        } else {
            PLAYER_SPAWN_COOLDOWNS.put(uuid, cooldown);
        }
    }

    private static int countNearbyCactuar(ServerPlayer player) {
        AABB box = player.getBoundingBox().inflate(NEARBY_CHECK_RANGE);

        return player.level().getEntitiesOfClass(
                CactuarEntity.class,
                box,
                entity -> entity.isAlive() && !entity.isJumbo()
        ).size();
    }

    private static BlockPos findSpawnPos(ServerLevel level, ServerPlayer player) {
        for (int attempts = 0; attempts < 24; attempts++) {
            double angle = level.random.nextDouble() * Math.PI * 2.0D;
            double distance = MIN_SPAWN_DISTANCE + level.random.nextDouble() * (MAX_SPAWN_DISTANCE - MIN_SPAWN_DISTANCE);

            int x = Mth.floor(player.getX() + Math.cos(angle) * distance);
            int z = Mth.floor(player.getZ() + Math.sin(angle) * distance);

            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(
                    x,
                    player.blockPosition().getY() + 12,
                    z
            );

            while (pos.getY() > level.getMinBuildHeight() + 2) {
                BlockPos below = pos.below();

                if (isValidCactuarSpawnPos(level, pos, below)) {
                    return pos.immutable();
                }

                pos.move(0, -1, 0);
            }
        }

        return null;
    }

    private static boolean isValidCactuarSpawnPos(ServerLevel level, BlockPos pos, BlockPos below) {
        if (!level.getBiome(pos).is(DESERT_BIOME)) {
            return false;
        }

        if (!level.getBlockState(pos).isAir()) {
            return false;
        }

        if (!level.getBlockState(pos.above()).isAir()) {
            return false;
        }

        return level.getBlockState(below).isSolidRender(level, below);
    }
}