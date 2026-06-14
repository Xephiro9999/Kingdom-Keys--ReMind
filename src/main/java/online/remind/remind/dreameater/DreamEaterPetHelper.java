package online.remind.remind.dreameater;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DreamEaterPetHelper {

    private static final Map<UUID, Long> LAST_PET_TIME = new HashMap<>();

    private static final int PET_COOLDOWN_TICKS = 20;

    public static InteractionResult tryPetDreamEater(
            LivingEntity dreamEater,
            Player player,
            InteractionHand hand,
            UUID ownerUUID,
            String dreamEaterName
    ) {
        if (dreamEater == null || player == null) {
            return InteractionResult.PASS;
        }

        // Main hand only so it does not trigger twice.
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        // Empty hand only.
        if (!player.getItemInHand(hand).isEmpty()) {
            return InteractionResult.PASS;
        }

        if (ownerUUID == null || !ownerUUID.equals(player.getUUID())) {
            if (!dreamEater.level().isClientSide) {
                player.displayClientMessage(Component.literal("This Dream Eater is not yours."), true);
            }

            return InteractionResult.sidedSuccess(dreamEater.level().isClientSide);
        }

        if (dreamEater.level().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        long gameTime = dreamEater.level().getGameTime();
        long lastPet = LAST_PET_TIME.getOrDefault(dreamEater.getUUID(), 0L);

        if (gameTime - lastPet < PET_COOLDOWN_TICKS) {
            return InteractionResult.CONSUME;
        }

        LAST_PET_TIME.put(dreamEater.getUUID(), gameTime);

        dreamEater.level().playSound(
                null,
                dreamEater.getX(),
                dreamEater.getY(),
                dreamEater.getZ(),
                SoundEvents.CAT_PURR,
                SoundSource.NEUTRAL,
                0.7F,
                1.35F
        );

        if (dreamEater.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.HEART,
                    dreamEater.getX(),
                    dreamEater.getY() + dreamEater.getBbHeight() + 0.2D,
                    dreamEater.getZ(),
                    2,
                    0.25D,
                    0.15D,
                    0.25D,
                    0.02D
            );

            serverLevel.sendParticles(
                    ParticleTypes.HAPPY_VILLAGER,
                    dreamEater.getX(),
                    dreamEater.getY() + dreamEater.getBbHeight() * 0.65D,
                    dreamEater.getZ(),
                    6,
                    0.35D,
                    0.25D,
                    0.35D,
                    0.02D
            );
        }

        player.displayClientMessage(Component.literal("You pet " + dreamEaterName + "."), true);

        // Future Bond/Affinity hook goes here.
        // DreamEaterBondData.addBond(player, dreamEaterName, 1);

        return InteractionResult.CONSUME;
    }
}