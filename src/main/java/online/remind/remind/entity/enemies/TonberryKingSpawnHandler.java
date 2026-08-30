package online.remind.remind.entity.enemies;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.ModEntitiesRM;
import online.remind.remind.entity.spirits.CactuarSpiritEntity;
import online.remind.remind.entity.spirits.ChirithyEntity;
import online.remind.remind.entity.spirits.KomoryBatEntity;
import online.remind.remind.entity.spirits.MeowWowEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        bus = EventBusSubscriber.Bus.GAME
)
public class TonberryKingSpawnHandler {

    /*
     * Flat random chance per normal Tonberry kill.
     *
     * 0.04D = 4% chance every time a normal Tonberry dies.
     *
     * No kill counter.
     * No minimum kills.
     * No guaranteed spawn.
     */
    private static final double TONBERRY_KING_SPAWN_CHANCE = 0.05D;

    private static final double EXISTING_KING_CHECK_RANGE = 160.0D;

    @SubscribeEvent
    public static void onTonberryKilled(LivingDeathEvent event) {
        LivingEntity defeated = event.getEntity();

        if (!(defeated.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        /*
         * Tonberry King extends TonberryEntity.
         * King deaths should NOT roll another King.
         */
        if (defeated instanceof TonberryKingEntity) {
            return;
        }

        if (!(defeated instanceof TonberryEntity)) {
            return;
        }

        ServerPlayer killer = getKillerPlayer(
                event.getSource().getEntity(),
                event.getSource().getDirectEntity(),
                serverLevel
        );

        if (killer == null) {
            return;
        }

        if (hasNearbyTonberryKing(serverLevel, killer)) {
            killer.displayClientMessage(
                    Component.literal("Tonberry King's grudge is already present...")
                            .withStyle(ChatFormatting.DARK_PURPLE),
                    true
            );

            return;
        }

        /*
         * The entire spawn logic is just this roll.
         */
        if (killer.getRandom().nextDouble() >= TONBERRY_KING_SPAWN_CHANCE) {
            return;
        }

        spawnTonberryKing(serverLevel, killer);
    }

    private static boolean hasNearbyTonberryKing(ServerLevel serverLevel, ServerPlayer player) {
        AABB checkBox = player.getBoundingBox().inflate(EXISTING_KING_CHECK_RANGE);

        List<TonberryKingEntity> kings = serverLevel.getEntitiesOfClass(
                TonberryKingEntity.class,
                checkBox,
                TonberryKingEntity::isAlive
        );

        return !kings.isEmpty();
    }

    private static void spawnTonberryKing(ServerLevel serverLevel, ServerPlayer player) {
        TonberryKingEntity king = ModEntitiesRM.TYPE_TONBERRY_KING.get().create(serverLevel);

        if (king == null) {
            return;
        }

        BlockPos spawnPos = findSpawnPosition(serverLevel, player);

        king.moveTo(
                spawnPos.getX() + 0.5D,
                spawnPos.getY(),
                spawnPos.getZ() + 0.5D,
                player.getRandom().nextFloat() * 360.0F,
                0.0F
        );

        king.finalizeSpawn(
                serverLevel,
                serverLevel.getCurrentDifficultyAt(spawnPos),
                MobSpawnType.TRIGGERED,
                (SpawnGroupData) null
        );

        serverLevel.addFreshEntity(king);
        king.setTarget(player);

        announceTonberryKingSpawn(serverLevel, player, king);
    }

    private static BlockPos findSpawnPosition(ServerLevel serverLevel, ServerPlayer player) {
        RandomSource random = player.getRandom();
        BlockPos playerPos = player.blockPosition();

        for (int attempt = 0; attempt < 80; attempt++) {
            double angle = random.nextDouble() * Math.PI * 2.0D;
            double distance = 8.0D + random.nextDouble() * 18.0D;

            int x = playerPos.getX() + (int) Math.round(Math.cos(angle) * distance);
            int z = playerPos.getZ() + (int) Math.round(Math.sin(angle) * distance);

            for (int y = playerPos.getY() + 4; y >= playerPos.getY() - 8; y--) {
                BlockPos pos = new BlockPos(x, y, z);

                if (canSpawnKingAt(serverLevel, pos)) {
                    return pos;
                }
            }
        }

        return playerPos.offset(3, 0, 3);
    }

    private static boolean canSpawnKingAt(ServerLevel serverLevel, BlockPos pos) {
        if (!serverLevel.getWorldBorder().isWithinBounds(pos)) {
            return false;
        }

        if (!serverLevel.isEmptyBlock(pos)) {
            return false;
        }

        if (!serverLevel.isEmptyBlock(pos.above())) {
            return false;
        }

        BlockPos below = pos.below();
        BlockState belowState = serverLevel.getBlockState(below);

        if (!belowState.isFaceSturdy(serverLevel, below, Direction.UP)) {
            return false;
        }

        AABB kingBox = new AABB(
                pos.getX() + 0.5D - 0.75D,
                pos.getY(),
                pos.getZ() + 0.5D - 0.75D,
                pos.getX() + 0.5D + 0.75D,
                pos.getY() + 2.6D,
                pos.getZ() + 0.5D + 0.75D
        );

        return serverLevel.noCollision(kingBox);
    }

    private static void announceTonberryKingSpawn(ServerLevel serverLevel, ServerPlayer player, TonberryKingEntity king) {
        Component message = Component.literal("Tonberry King has appeared!")
                .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD);

        Component subMessage = Component.literal("The King seeks revenge for all defeated Tonberries!")
                .withStyle(ChatFormatting.LIGHT_PURPLE);

        for (ServerPlayer serverPlayer : serverLevel.players()) {
            if (serverPlayer.distanceToSqr(king) <= 96.0D * 96.0D) {
                serverPlayer.displayClientMessage(message, false);
                serverPlayer.displayClientMessage(subMessage, true);
            }
        }

        serverLevel.playSound(
                null,
                king.getX(),
                king.getY(),
                king.getZ(),
                SoundEvents.WITHER_SPAWN,
                SoundSource.HOSTILE,
                1.0F,
                0.75F
        );

        serverLevel.sendParticles(
                ParticleTypes.SOUL,
                king.getX(),
                king.getY() + 1.2D,
                king.getZ(),
                80,
                1.2D,
                1.0D,
                1.2D,
                0.08D
        );

        serverLevel.sendParticles(
                ParticleTypes.LARGE_SMOKE,
                king.getX(),
                king.getY() + 0.8D,
                king.getZ(),
                45,
                0.9D,
                0.7D,
                0.9D,
                0.04D
        );
    }

    @Nullable
    private static ServerPlayer getKillerPlayer(Entity attacker, Entity directEntity, ServerLevel serverLevel) {
        if (attacker instanceof ServerPlayer serverPlayer) {
            return serverPlayer;
        }

        if (directEntity instanceof Projectile projectile && projectile.getOwner() instanceof ServerPlayer serverPlayer) {
            return serverPlayer;
        }

        if (attacker instanceof Projectile projectile && projectile.getOwner() instanceof ServerPlayer serverPlayer) {
            return serverPlayer;
        }

        UUID ownerUUID = getDreamEaterOwnerUUID(attacker);

        if (ownerUUID != null) {
            return (ServerPlayer) serverLevel.getPlayerByUUID(ownerUUID);
        }

        ownerUUID = getDreamEaterOwnerUUID(directEntity);

        if (ownerUUID != null) {
            return (ServerPlayer) serverLevel.getPlayerByUUID(ownerUUID);
        }

        return null;
    }

    @Nullable
    private static UUID getDreamEaterOwnerUUID(Entity entity) {
        if (entity instanceof ChirithyEntity chirithy) {
            return chirithy.getOwnerUUID();
        }

        if (entity instanceof MeowWowEntity meowWow) {
            return meowWow.getOwnerUUID();
        }

        if (entity instanceof KomoryBatEntity komoryBat) {
            return komoryBat.getOwnerUUID();
        }

        if (entity instanceof CactuarSpiritEntity cactuarSpirit) {
            return cactuarSpirit.getOwnerUUID();
        }

        return null;
    }
}