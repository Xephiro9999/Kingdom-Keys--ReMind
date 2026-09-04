package online.remind.remind.dreameater;

import net.minecraft.world.entity.player.Player;

public final class DreamEaterSummonCooldown {

    private static final String COOLDOWN_KEY =
            "KKReMindDreamEaterSummonCooldownEnd";

    private static final long COOLDOWN_MILLIS =
            30_000L;

    private DreamEaterSummonCooldown() {
    }


    // Cooldown control
    public static void start(Player player) {
        if (player == null || player.level().isClientSide) {
            return;
        }

        player.getPersistentData().putLong(
                COOLDOWN_KEY,
                System.currentTimeMillis() + COOLDOWN_MILLIS
        );
    }

    public static long getRemainingMillis(Player player) {
        if (player == null) {
            return 0L;
        }

        long endTime =
                player.getPersistentData().getLong(COOLDOWN_KEY);

        if (endTime <= 0L) {
            return 0L;
        }

        long remaining =
                endTime - System.currentTimeMillis();

        if (remaining <= 0L) {
            player.getPersistentData().remove(COOLDOWN_KEY);
            return 0L;
        }

        return remaining;
    }

    public static long getRemainingSeconds(Player player) {
        long remaining = getRemainingMillis(player);

        if (remaining <= 0L) {
            return 0L;
        }

        return (remaining + 999L) / 1000L;
    }

    public static boolean isOnCooldown(Player player) {
        return getRemainingMillis(player) > 0L;
    }

    public static void clear(Player player) {
        if (player == null) {
            return;
        }

        player.getPersistentData().remove(COOLDOWN_KEY);
    }
}