package online.remind.remind.entity.enemies;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import online.remind.remind.KingdomKeysReMind;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        bus = EventBusSubscriber.Bus.GAME
)
public class CactuarDamageHandler {

    /*
     * This runs late so it sees the final damage after other systems,
     * including Kingdom Keys / Keyblade damage changes.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCactuarDamage(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof CactuarEntity cactuar)) {
            return;
        }

        float damage = event.getNewDamage();

        if (damage <= 0.0F) {
            return;
        }

        boolean waterDamage = isKKWaterDamage(event.getSource());

        if (cactuar.isJumbo()) {
            damage = handleJumboDamage(damage, waterDamage);
        } else {
            damage = handleNormalCactuarDamage(damage, waterDamage);
        }

        event.setNewDamage(Math.max(1.0F, damage));
    }

    private static float handleNormalCactuarDamage(float damage, boolean waterDamage) {
        if (waterDamage) {
            damage *= 1.75F;
        }

        return damage;
    }

    private static float handleJumboDamage(float damage, boolean waterDamage) {
        if (waterDamage) {
            /*
             * Water is the weakness.
             * Still capped so the boss cannot be deleted by one huge Keyblade hit.
             */
            damage *= 1.75F;
            damage = Math.min(damage, 140.0F);
        } else {
            /*
             * Jumbo heavily resists non-Water damage.
             */
            damage *= 0.25F;
            damage = Math.min(damage, 35.0F);
        }

        return damage;
    }

    private static boolean isKKWaterDamage(DamageSource source) {
        if (source == null) {
            return false;
        }

        ResourceKey<DamageType> key = source.typeHolder().unwrapKey().orElse(null);

        if (key == null) {
            return false;
        }

        ResourceLocation location = key.location();

        String namespace = location.getNamespace();
        String path = location.getPath();

        return ("kingdomkeys".equals(namespace) || "kkremind".equals(namespace))
                && path.contains("water");
    }
}