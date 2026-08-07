package online.remind.remind.dreameater;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;

@EventBusSubscriber(modid = KingdomKeysReMind.MODID)
public final class DreamEaterKOHandler {

    private DreamEaterKOHandler() {
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        // Damage was already prevented by another handler.
        if (event.getNewDamage() <= 0.0F) {
            return;
        }

        // Only intercept lethal damage.
        if (player.getHealth() - event.getNewDamage() > 0.0F) {
            return;
        }

        // Match base KK behavior: a player already in KO is not rescued again.
        if (player.hasEffect(ModMobEffects.KO)) {
            return;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (!hasActiveDreamEater(player, globalData)) {
            return;
        }

        applyDreamEaterKO(player, event);
    }

    private static boolean hasActiveDreamEater(
            ServerPlayer player,
            GlobalDataRM globalData
    ) {
        if (globalData == null) {
            return false;
        }

        if (!globalData.hasDreamEaterSummoned()) {
            return false;
        }

        if (globalData.getDreamEaterUUID() == null) {
            return false;
        }

        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return false;
        }

        Entity dreamEater = serverLevel.getEntity(
                globalData.getDreamEaterUUID()
        );

        return dreamEater != null
                && dreamEater.isAlive()
                && !dreamEater.isRemoved();
    }

    private static void applyDreamEaterKO(
            ServerPlayer player,
            LivingDamageEvent.Pre event
    ) {
        event.setNewDamage(0.0F);

        player.removeAllEffects();
        player.setHealth(player.getMaxHealth());
        player.invulnerableTime = 40;

        player.getFoodData().setFoodLevel(10);
        player.getFoodData().setExhaustion(0.0F);
        player.getFoodData().setSaturation(0.0F);

        MobEffectInstance koInstance = new MobEffectInstance(
                ModMobEffects.KO,
                MobEffectInstance.INFINITE_DURATION,
                0,
                false,
                false,
                false
        );

        player.addEffect(koInstance);

        player.level().playSound(
                null,
                player.blockPosition(),
                ModSounds.playerDeathHardcore.get(),
                SoundSource.PLAYERS
        );
    }
}