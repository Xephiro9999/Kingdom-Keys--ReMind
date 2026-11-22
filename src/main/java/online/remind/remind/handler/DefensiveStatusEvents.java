
package online.remind.remind.handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.leveling.Stat;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.effect.ProtectEffect;
import online.remind.remind.effect.ShellEffect;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        bus = EventBusSubscriber.Bus.GAME
)
public final class DefensiveStatusEvents {

    private static final String PROTECT_MODIFIER =
            "kkremind_protect_def";

    // Cache prevents unnecessary stat updates and packets.
    private static final Map<UUID, Double> PROTECT_BONUSES =
            new HashMap<>();

    private DefensiveStatusEvents() {}

    // ============================================================
    // PROTECT - Kingdom Keys DEF stat
    // ============================================================

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        PlayerData data = PlayerData.get(player);

        if (data == null) {
            return;
        }

        Stat defense = data.getDefenseStat();

        MobEffectInstance protect =
                player.getEffect(ModMobEffectsRM.PROTECT);

        UUID playerId = player.getUUID();

        // Remove our bonus when Protect is gone.
        if (protect == null) {
            PROTECT_BONUSES.remove(playerId);

            if (defense.hasModifier(PROTECT_MODIFIER)) {
                defense.removeModifier(PROTECT_MODIFIER);
                sync(player);
            }

            return;
        }

        double bonus = ProtectEffect.getDefenseBonus(
                protect.getAmplifier()
        );

        Double previousBonus =
                PROTECT_BONUSES.get(playerId);

        // Refresh when first applied, upgraded, or base DEF changes.
        if (previousBonus == null
                || Math.abs(previousBonus - bonus) > 0.0001D
                || !defense.hasModifier(PROTECT_MODIFIER)) {

            defense.addModifier(
                    PROTECT_MODIFIER,
                    bonus,
                    false, // Do not stack duplicate modifiers
                    false  // Bonus is calculated manually
            );

            PROTECT_BONUSES.put(playerId, bonus);
            sync(player);
        }
    }

    // ============================================================
    // SHELL - Magic and elemental damage reduction
    // ============================================================

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamage(LivingDamageEvent.Pre event) {

        if (event.getEntity().level().isClientSide) {
            return;
        }

        MobEffectInstance shell =
                event.getEntity().getEffect(ModMobEffectsRM.SHELL);

        MobEffectInstance protect =
                event.getEntity().getEffect(ModMobEffectsRM.PROTECT);

        if (protect != null){
            event.getEntity().level().playSound(null, event.getEntity().getX(),event.getEntity().getY(),event.getEntity().getZ(), ModSoundsRM.PROTECT_HIT.value(), SoundSource.MASTER, 0.5F, 1.0F);

        }


        if (shell == null) {
            return;
        }

        if (!isShellDamage(event.getSource())) {
            return;
        }

        float damage = event.getNewDamage();

        if (damage <= 0.0F) {
            return;
        }

        float reduction =
                ShellEffect.getDamageReduction(
                        shell.getAmplifier()
                );

        event.setNewDamage(
                Math.max(0.0F, damage * (1.0F - reduction))
        );
        event.getEntity().level().playSound(null, event.getEntity().getX(),event.getEntity().getY(),event.getEntity().getZ(), ModSoundsRM.PROTECT_HIT.value(), SoundSource.MASTER, 0.5F, 1.25F);


    }

    private static boolean isShellDamage(DamageSource source) {

        // Kingdom Keys elemental damage.
        if (source.is(KKDamageTypes.FIRE)
                || source.is(KKDamageTypes.ICE)
                || source.is(KKDamageTypes.LIGHTNING)
                || source.is(KKDamageTypes.WATER)
                || source.is(KKDamageTypes.AIR)
                || source.is(KKDamageTypes.LIGHT)
                || source.is(KKDamageTypes.DARKNESS)) {
            return true;
        }

        // Vanilla magical damage.
        if (source.is(DamageTypes.MAGIC)
                || source.is(DamageTypes.INDIRECT_MAGIC)
                || source.is(DamageTypes.DRAGON_BREATH)
                || source.is(DamageTypes.SONIC_BOOM)) {
            return true;
        }

        // Vanilla elemental sources.
        return source.is(DamageTypeTags.IS_FIRE)
                || source.is(DamageTypes.LIGHTNING_BOLT)
                || source.is(DamageTypes.FREEZE);
    }

    private static void sync(ServerPlayer player) {
        PacketHandler.sendTo(
                new SCSyncPlayerData(player),
                player
        );
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PROTECT_BONUSES.remove(event.getEntity().getUUID());
    }
}
