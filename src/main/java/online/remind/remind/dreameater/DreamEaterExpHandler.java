package online.remind.remind.dreameater;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowOverlayPacket;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.entity.spirits.CactuarSpiritEntity;
import online.remind.remind.entity.spirits.ChirithyEntity;
import online.remind.remind.entity.spirits.KomoryBatEntity;
import online.remind.remind.entity.spirits.MeowWowEntity;
import online.remind.remind.network.PacketHandlerRM;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = KingdomKeysReMind.MODID)
public class DreamEaterExpHandler {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity defeated = event.getEntity();

        if (!(defeated.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        ServerPlayer owner = getOwnerFromDamageSource(
                event.getSource().getEntity(),
                event.getSource().getDirectEntity(),
                serverLevel
        );

        if (owner == null) {
            return;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(owner);

        if (globalData == null) {
            return;
        }

        String dreamEaterRL = globalData.getDreamEaterRL();

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            return;
        }

        if (!globalData.hasDreamEaterUnlocked(dreamEaterRL)) {
            return;
        }

        /*
         * Only the currently equipped/summoned Dream Eater gets EXP from kills.
         * Feeding can still use giveDreamEaterExp(...) directly.
         */
        if (!globalData.hasDreamEaterSummoned()) {
            return;
        }

        int baseExp = calculateExp(defeated);

        float dreamEaterXPMult = getDreamEaterExpMultiplier(dreamEaterRL);
        double serverXPMult = ModConfigs.xpMulti;

        int exp = Math.toIntExact(Math.max(1, Math.round(baseExp * dreamEaterXPMult * serverXPMult)));

        if (exp <= 0) {
            return;
        }

        Entity summonedDreamEater = getSummonedDreamEaterEntity(serverLevel, globalData);

        giveDreamEaterExp(
                owner,
                dreamEaterRL,
                exp,
                summonedDreamEater,
                false
        );
    }

    public static boolean giveDreamEaterExp(
            ServerPlayer owner,
            String dreamEaterRL,
            int amount,
            Entity effectTarget
    ) {
        return giveDreamEaterExp(owner, dreamEaterRL, amount, effectTarget, true);
    }

    public static boolean giveDreamEaterExp(
            ServerPlayer owner,
            String dreamEaterRL,
            int amount,
            Entity effectTarget,
            boolean showSmallGainMessage
    ) {
        if (owner == null || dreamEaterRL == null || dreamEaterRL.isEmpty() || amount <= 0) {
            return false;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(owner);

        if (globalData == null) {
            return false;
        }

        if (!globalData.hasDreamEaterUnlocked(dreamEaterRL)) {
            return false;
        }

        int oldLevel = globalData.getDreamEaterLevel(dreamEaterRL);

        boolean leveledUp = globalData.addDreamEaterExp(dreamEaterRL, amount);

        int newLevel = globalData.getDreamEaterLevel(dreamEaterRL);

        Entity target = effectTarget;

        if (target == null && owner.level() instanceof ServerLevel serverLevel) {
            target = getSummonedDreamEaterEntity(serverLevel, globalData);
        }

        if (target == null) {
            target = owner;
        }

        String dreamEaterName = getDreamEaterDisplayName(dreamEaterRL);

        if (leveledUp || newLevel > oldLevel) {
            int levelsGained = Math.max(1, newLevel - oldLevel);

            DreamEaterLevelUpSwirlHandler.start(target);

            sendDreamEaterLevelUpOverlay(owner, dreamEaterName, newLevel, levelsGained);
            playLevelUpEffects(owner, target);
        } else if (showSmallGainMessage) {
            playSmallExpGainEffects(owner, target);

            owner.displayClientMessage(
                    Component.literal(dreamEaterName + " gained " + amount + " EXP.")
                            .withStyle(ChatFormatting.GREEN),
                    true
            );
        }

        PacketHandlerRM.syncGlobalToAllAround(owner, globalData);

        return leveledUp || newLevel > oldLevel;
    }

    private static void sendDreamEaterLevelUpOverlay(
            ServerPlayer owner,
            String dreamEaterName,
            int newLevel,
            int levelsGained
    ) {
        List<String> levelUpLines = new ArrayList<>();

        if (levelsGained > 1) {
            levelUpLines.add("Level +" + levelsGained + " ↑");
        } else {
            levelUpLines.add("Level ↑");
        }

        levelUpLines.add("Spirit Power ↑");

        owner.level().playSound(
                null,
                owner.getX(),
                owner.getY(),
                owner.getZ(),
                ModSounds.levelup.get(),
                SoundSource.MASTER,
                0.5F,
                1.0F
        );

        PacketHandler.sendTo(
                new SCShowOverlayPacket(
                        "levelup",
                        owner.getUUID(),
                        dreamEaterName,
                        newLevel,
                        0xff00f6,
                        levelUpLines
                ),
                owner
        );
    }

    private static void playSmallExpGainEffects(ServerPlayer owner, Entity target) {
        if (target == null) {
            target = owner;
        }

        if (!(target.level() instanceof ServerLevel level)) {
            return;
        }

        level.playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.EXPERIENCE_ORB_PICKUP,
                SoundSource.PLAYERS,
                0.18F,
                1.65F
        );

        level.sendParticles(
                ParticleTypes.HAPPY_VILLAGER,
                target.getX(),
                target.getY() + target.getBbHeight() * 0.75D,
                target.getZ(),
                8,
                0.35D,
                0.35D,
                0.35D,
                0.05D
        );
    }

    private static void playLevelUpEffects(ServerPlayer owner, Entity target) {
        if (target == null) {
            target = owner;
        }

        if (!(target.level() instanceof ServerLevel level)) {
            return;
        }

        double baseX = target.getX();
        double baseY = target.getY();
        double baseZ = target.getZ();

        level.playSound(
                null,
                baseX,
                baseY,
                baseZ,
                ModSounds.levelup.get(),
                SoundSource.PLAYERS,
                0.65F,
                0.85F
        );
    }

    private static Entity getSummonedDreamEaterEntity(ServerLevel currentLevel, GlobalDataRM globalData) {
        UUID dreamEaterUUID = globalData.getDreamEaterUUID();

        if (dreamEaterUUID == null) {
            return null;
        }

        Entity entity = currentLevel.getEntity(dreamEaterUUID);

        if (entity != null) {
            return entity;
        }

        for (ServerLevel level : currentLevel.getServer().getAllLevels()) {
            entity = level.getEntity(dreamEaterUUID);

            if (entity != null) {
                return entity;
            }
        }

        return null;
    }

    private static String getDreamEaterDisplayName(String dreamEaterRL) {
        if (GlobalDataRM.DREAM_EATER_CHIRITHY.equals(dreamEaterRL)) {
            return "Chirithy";
        }

        if (GlobalDataRM.DREAM_EATER_MEOW_WOW.equals(dreamEaterRL)) {
            return "Meow Wow";
        }

        if (GlobalDataRM.DREAM_EATER_KOMORY_BAT.equals(dreamEaterRL)) {
            return "Komory Bat";
        }

        if (GlobalDataRM.DREAM_EATER_CACTUAR.equals(dreamEaterRL)) {
            return "Cactuar";
        }

        return "Dream Eater";
    }

    private static ServerPlayer getOwnerFromDamageSource(Entity attacker, Entity directEntity, ServerLevel level) {
        ServerPlayer owner = getOwnerFromEntity(attacker, level);

        if (owner != null) {
            return owner;
        }

        owner = getOwnerFromEntity(directEntity, level);

        if (owner != null) {
            return owner;
        }

        if (attacker instanceof Projectile projectile) {
            owner = getOwnerFromEntity(projectile.getOwner(), level);

            if (owner != null) {
                return owner;
            }
        }

        if (directEntity instanceof Projectile projectile) {
            owner = getOwnerFromEntity(projectile.getOwner(), level);

            if (owner != null) {
                return owner;
            }
        }

        return null;
    }

    private static ServerPlayer getOwnerFromEntity(Entity entity, ServerLevel level) {
        if (entity instanceof ServerPlayer serverPlayer) {
            return serverPlayer;
        }

        if (entity instanceof ChirithyEntity chirithy && chirithy.getOwnerUUID() != null) {
            Player player = level.getPlayerByUUID(chirithy.getOwnerUUID());

            if (player instanceof ServerPlayer serverPlayer) {
                return serverPlayer;
            }

            return null;
        }

        if (entity instanceof MeowWowEntity meowWow && meowWow.getOwnerUUID() != null) {
            Player player = level.getPlayerByUUID(meowWow.getOwnerUUID());

            if (player instanceof ServerPlayer serverPlayer) {
                return serverPlayer;
            }

            return null;
        }

        if (entity instanceof KomoryBatEntity komoryBat && komoryBat.getOwnerUUID() != null) {
            Player player = level.getPlayerByUUID(komoryBat.getOwnerUUID());

            if (player instanceof ServerPlayer serverPlayer) {
                return serverPlayer;
            }

            return null;
        }

        if (entity instanceof CactuarSpiritEntity cactuarSpirit && cactuarSpirit.getOwnerUUID() != null) {
            Player player = level.getPlayerByUUID(cactuarSpirit.getOwnerUUID());

            if (player instanceof ServerPlayer serverPlayer) {
                return serverPlayer;
            }

            return null;
        }

        return null;
    }

    private static int calculateExp(LivingEntity defeated) {
        int exp = Math.max(1, (int) (defeated.getMaxHealth() / 4.0F));

        if (defeated instanceof Monster) {
            exp += 3;
        }

        return Math.min(exp, 500);
    }

    private static float getDreamEaterExpMultiplier(String dreamEaterRL) {
        if (GlobalDataRM.DREAM_EATER_CHIRITHY.equals(dreamEaterRL)) {
            return 1.20F;
        }

        if (GlobalDataRM.DREAM_EATER_MEOW_WOW.equals(dreamEaterRL)) {
            return 1.05F;
        }

        if (GlobalDataRM.DREAM_EATER_KOMORY_BAT.equals(dreamEaterRL)) {
            return 1.05F;
        }

        if (GlobalDataRM.DREAM_EATER_CACTUAR.equals(dreamEaterRL)) {
            return 1.25F;
        }

        return 1.00F;
    }
}