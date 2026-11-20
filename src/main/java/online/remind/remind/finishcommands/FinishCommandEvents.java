package online.remind.remind.finishcommands;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.finishcommands.FinishCommandDefinition.ProgressType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = KingdomKeysReMind.MODID, bus = EventBusSubscriber.Bus.GAME)
public final class FinishCommandEvents {
    private FinishCommandEvents() {}

    private static final Map<UUID, Vec3> LAST_POS = new HashMap<>();
    private static final Map<UUID, Double> STEP_FRACTIONS = new HashMap<>();
    private static final Map<UUID, Integer> LAST_MUNNY = new HashMap<>();

    @SubscribeEvent
    public static void tick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        UUID id = player.getUUID();
        Vec3 current = player.position();
        Vec3 previous = LAST_POS.put(id, current);

        // Movement-based progress. Exclude teleports and riding.
        if (previous != null && !player.isPassenger()) {
            double moved = Math.hypot(current.x - previous.x, current.z - previous.z);
            if (moved > 0.001 && moved <= 1.5 && player.onGround()) {
                double total = STEP_FRACTIONS.getOrDefault(id, 0D) + moved;
                int whole = (int) total;
                STEP_FRACTIONS.put(id, total - whole);
                if (whole > 0) FinishCommandProgression.awardProgress(player, ProgressType.STEPS, whole);
            }
        }

        // Observe increases, not decreases, in the existing Kingdom Keys munny balance.
        // Snapshotting is a fallback: instrument direct reward sites for exact amounts.
        PlayerData kk = PlayerData.get(player);
        if (kk != null) {
            int now = kk.getMunny();
            Integer previousMunny = LAST_MUNNY.put(id, now);
            if (previousMunny != null && now > previousMunny) {
                FinishCommandProgression.awardProgress(player, ProgressType.MUNNY, now - previousMunny);
            }
        }

        if (player.tickCount % 20 == 0) FinishCommandProgression.flushIfDirty(player);
    }

    @SubscribeEvent
    public static void onKill(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim instanceof ServerPlayer || victim.level().isClientSide()) return;
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();
        if (attacker instanceof ServerPlayer player) {
            FinishCommandProgression.awardProgress(player, ProgressType.ENEMY_KILLS, 1);
        }
    }

    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            LAST_POS.put(player.getUUID(), player.position());
            PlayerData kk = PlayerData.get(player);
            if (kk != null) LAST_MUNNY.put(player.getUUID(), kk.getMunny());
            FinishCommandProgression.sync(player);
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            LAST_POS.put(player.getUUID(), player.position());
            PlayerData kk = PlayerData.get(player);
            if (kk != null) LAST_MUNNY.put(player.getUUID(), kk.getMunny());
            FinishCommandProgression.sync(player);
        }
    }

    @SubscribeEvent
    public static void onLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID id = event.getEntity().getUUID();
        LAST_POS.remove(id);
        LAST_MUNNY.remove(id);
        STEP_FRACTIONS.remove(id);
        FinishCommandProgression.onLeave(event.getEntity());
    }
}
