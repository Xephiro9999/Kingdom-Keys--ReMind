package online.remind.remind.entity.enemies;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
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
public class FFDamageHandler {

    /*
     * This runs late so it sees the final damage after other systems,
     * including Kingdom Keys / Keyblade damage changes.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEnemyDamage(LivingDamageEvent.Pre event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }

        DamageSource source = event.getSource();

        /*
         * Do not mess with /kill, void, admin-kill style damage, etc.
         */
        if (source != null && source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return;
        }

        float damage = event.getNewDamage();

        if (damage <= 0.0F) {
            return;
        }

        if (event.getEntity() instanceof CactuarEntity cactuar) {
            damage = handleCactuarDamage(cactuar, source, damage);
            event.setNewDamage(Math.max(1.0F, damage));
            return;
        }

        /*
         * Check TonberryKingEntity BEFORE TonberryEntity because King extends Tonberry.
         */
        if (event.getEntity() instanceof TonberryKingEntity tonberryKing) {
            damage = handleTonberryKingDamage(tonberryKing, source, damage);
            event.setNewDamage(Math.max(1.0F, damage));
            return;
        }

        if (event.getEntity() instanceof TonberryEntity tonberry) {
            damage = handleTonberryDamage(tonberry, source, damage);
            event.setNewDamage(Math.max(1.0F, damage));
        }
    }

    private static float handleCactuarDamage(CactuarEntity cactuar, DamageSource source, float damage) {
        boolean waterDamage = isKKWaterDamage(source);

        if (cactuar.isJumbo()) {
            return handleJumboCactuarDamage(damage, waterDamage);
        }

        return handleNormalCactuarDamage(damage, waterDamage);
    }

    private static float handleNormalCactuarDamage(float damage, boolean waterDamage) {
        if (waterDamage) {
            damage *= 1.75F;
        }

        return damage;
    }

    private static float handleJumboCactuarDamage(float damage, boolean waterDamage) {
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
            damage *= 0.35F;
            damage = Math.min(damage, 35.0F);
        }

        return damage;
    }

    private static float handleTonberryDamage(TonberryEntity tonberry, DamageSource source, float damage) {
        /*
         * Tonberry should be tanky and scary.
         *
         * This makes huge Kingdom Keys / Keyblade burst hits not delete it instantly.
         * Normal Tonberry still takes more damage than Tonberry King.
         */
        damage *= 0.45F;
        damage = Math.min(damage, 65.0F);

        return damage;
    }

    private static float handleTonberryKingDamage(TonberryKingEntity tonberryKing, DamageSource source, float damage) {
        /*
         * Tonberry King is a triggered boss.
         *
         * With 2500+ HP, this makes the fight last long enough for mechanics:
         * - slow approach
         * - stab wind-up
         * - Everyone's Grudge counter
         * - boss reward pacing
         */
        damage *= 0.3F;
        damage = Math.min(damage, 55.0F);

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