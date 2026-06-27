package online.remind.remind.dreameater;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import online.remind.remind.KingdomKeysReMind;
import org.joml.Vector3f;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = KingdomKeysReMind.MODID)
public class DreamEaterLevelUpSwirlHandler {

    private static final List<SwirlEffect> ACTIVE_SWIRLS = new LinkedList<>();

    private static final int MAX_TICKS = 60;

    private static final DustParticleOptions KH_PINK =
            new DustParticleOptions(new Vector3f(1.0F, 0.05F, 0.65F), 0.35F);

    private static final DustParticleOptions KH_RED =
            new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 0.35F);

    private static final DustParticleOptions KH_GOLD =
            new DustParticleOptions(new Vector3f(1.0F, 0.82F, 0.0F), 0.65F);

    // Fake glow/halo versions
    private static final DustParticleOptions KH_PINK_GLOW =
            new DustParticleOptions(new Vector3f(1.0F, 0.12F, 0.85F), 1.35F);

    private static final DustParticleOptions KH_RED_GLOW =
            new DustParticleOptions(new Vector3f(1.0F, 0.05F, 0.05F), 1.25F);

    private static final DustParticleOptions KH_GOLD_GLOW =
            new DustParticleOptions(new Vector3f(1.0F, 0.85F, 0.05F), 1.45F);

    public static void start(Entity entity) {
        if (entity == null || !(entity.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(
                    MobEffects.GLOWING,
                    MAX_TICKS + 10,
                    0,
                    false,
                    false,
                    false
            ));
        }

        ACTIVE_SWIRLS.add(new SwirlEffect(
                entity.getUUID(),
                serverLevel.dimension(),
                0
        ));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();

        if (server == null || ACTIVE_SWIRLS.isEmpty()) {
            return;
        }

        Iterator<SwirlEffect> iterator = ACTIVE_SWIRLS.iterator();

        while (iterator.hasNext()) {
            SwirlEffect effect = iterator.next();

            ServerLevel level = server.getLevel(effect.dimension);

            if (level == null) {
                iterator.remove();
                continue;
            }

            Entity entity = level.getEntity(effect.entityUUID);

            if (entity == null || !entity.isAlive()) {
                iterator.remove();
                continue;
            }

            spawnDreamEaterOrbitSwirl(level, entity, effect.ticks);

            effect.ticks++;

            if (effect.ticks >= MAX_TICKS) {
                iterator.remove();
            }
        }
    }

    private static void spawnDreamEaterOrbitSwirl(ServerLevel level, Entity entity, int ticks) {
        double cx = entity.getX();
        double cy = entity.getY() + (entity.getBbHeight() * 0.52D);
        double cz = entity.getZ();

        double r = Math.max(0.45D, entity.getBbWidth() * 1D);
        double v = Math.max(0.55D, entity.getBbHeight() * 1D);

        double spin = ticks * 38.0D;

        /*
         * These two planes make the X shape:
         *
         *  45  = /
         * -45  = \
         */
        spawnXOrbit(level, cx, cy, cz, r, v, spin, 45.0D, 0);
        spawnXOrbit(level, cx, cy, cz, r, v, -spin, -45.0D, 1);
    }

    private static void spawnXOrbit(
            ServerLevel level,
            double cx,
            double cy,
            double cz,
            double r,
            double v,
            double spin,
            double planeYawDegrees,
            int plane
    ) {
        /*
         * Longer trail = more like a visible orbit line.
         */
        int trailLength = 22;

        double planeYaw = Math.toRadians(planeYawDegrees);

        double dirX = Math.cos(planeYaw);
        double dirZ = Math.sin(planeYaw);

        /*
         * Side direction used to make the orbit look thicker.
         */
        double sideX = Math.cos(planeYaw + Math.toRadians(90));
        double sideZ = Math.sin(planeYaw + Math.toRadians(90));

        for (int trail = 0; trail < trailLength; trail++) {
            double angle = Math.toRadians(spin - (trail * 5.5D));

            double horizontal = Math.cos(angle) * r;

            double x = cx + (dirX * horizontal);
            double y = cy + (Math.sin(angle) * v);
            double z = cz + (dirZ * horizontal);

            DustParticleOptions particle;

            if (trail < 2) {
                particle = KH_GOLD;
            } else if (plane == 0) {
                particle = KH_PINK;
            } else {
                particle = KH_RED;
            }

            /*
             * Main center particle.
             */
            spawnDustBlob(level, particle, x, y, z, trail < 2);

            /*
             * Extra side particles make the orbit into a ribbon instead of dots.
             */
            double thickness = trail < 2 ? 0.035D : 0.02D;

            spawnDustBlob(
                    level,
                    particle,
                    x + (sideX * thickness),
                    y,
                    z + (sideZ * thickness),
                    false
            );

            spawnDustBlob(
                    level,
                    particle,
                    x - (sideX * thickness),
                    y,
                    z - (sideZ * thickness),
                    false
            );
        }
    }

    private static void spawnDustBlob(
            ServerLevel level,
            DustParticleOptions particle,
            double x,
            double y,
            double z,
            boolean leading
    ) {
        DustParticleOptions glowParticle = getGlowParticle(particle);

        // Outer fake glow
        level.sendParticles(
                glowParticle,
                x,
                y,
                z,
                leading ? 2 : 1,
                leading ? 0.045D : 0.025D,
                leading ? 0.045D : 0.025D,
                leading ? 0.045D : 0.025D,
                0.0D
        );

        // Bright core
        level.sendParticles(
                particle,
                x,
                y,
                z,
                leading ? 4 : 2,
                leading ? 0.018D : 0.008D,
                leading ? 0.018D : 0.008D,
                leading ? 0.018D : 0.008D,
                0.0D
        );

        // Extra sparkle for the leading point
        if (leading) {
            level.sendParticles(
                    ParticleTypes.END_ROD,
                    x,
                    y,
                    z,
                    1,
                    0.01D,
                    0.01D,
                    0.01D,
                    0.01D
            );
        }
    }


    private static DustParticleOptions getGlowParticle(DustParticleOptions particle) {
        if (particle == KH_GOLD) {
            return KH_GOLD_GLOW;
        }

        if (particle == KH_RED) {
            return KH_RED_GLOW;
        }

        return KH_PINK_GLOW;
    }

    private static class SwirlEffect {
        private final UUID entityUUID;
        private final ResourceKey<Level> dimension;
        private int ticks;

        private SwirlEffect(UUID entityUUID, ResourceKey<Level> dimension, int ticks) {
            this.entityUUID = entityUUID;
            this.dimension = dimension;
            this.ticks = ticks;
        }
    }
}