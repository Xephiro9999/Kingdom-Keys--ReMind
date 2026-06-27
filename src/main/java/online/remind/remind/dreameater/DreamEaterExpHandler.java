package online.remind.remind.dreameater;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowOverlayPacket;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.entity.spirits.ChirithyEntity;
import online.remind.remind.entity.spirits.KomoryBatEntity;
import online.remind.remind.entity.spirits.MeowWowEntity;
import online.remind.remind.network.PacketHandlerRM;
import org.joml.Vector3f;

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

        ServerPlayer owner = getOwnerFromDamageSource(event.getSource().getEntity(), serverLevel);

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

        // Only the currently equipped/summoned Dream Eater gets EXP.
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

        int oldLevel = globalData.getDreamEaterLevel(dreamEaterRL);

        boolean leveledUp = globalData.addDreamEaterExp(dreamEaterRL, exp);

        int newLevel = globalData.getDreamEaterLevel(dreamEaterRL);
        int currentExp = globalData.getDreamEaterExp(dreamEaterRL);
        int expNeeded = globalData.getDreamEaterExpToNextLevel(dreamEaterRL);

        String dreamEaterName = getDreamEaterDisplayName(dreamEaterRL);
        Entity summonedDreamEater = getSummonedDreamEaterEntity(serverLevel, globalData);

        if (leveledUp) {
            int levelsGained = Math.max(1, newLevel - oldLevel);

            if (summonedDreamEater != null) {
                DreamEaterLevelUpSwirlHandler.start(summonedDreamEater);
            } else {
                DreamEaterLevelUpSwirlHandler.start(owner);
            }

            sendDreamEaterLevelUpOverlay(owner, dreamEaterName, newLevel, levelsGained);
            playLevelUpEffects(owner, summonedDreamEater);
        }

        PacketHandlerRM.syncGlobalToAllAround(owner, globalData);
    }

    private static void sendDreamEaterLevelUpOverlay(
            ServerPlayer owner,
            String dreamEaterName,
            int newLevel,
            int levelsGained
    ) {
        PlayerData playerData = PlayerData.get(owner);

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



    private static void playSmallExpGainSound(ServerPlayer owner, Entity summonedDreamEater) {
        Entity target = summonedDreamEater != null ? summonedDreamEater : owner;

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
    }

    private static void playLevelUpEffects(ServerPlayer owner, Entity summonedDreamEater) {
        Entity target = summonedDreamEater != null ? summonedDreamEater : owner;

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
                ModSounds.levelup,
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

        return "Dream Eater";
    }

    private static ServerPlayer getOwnerFromDamageSource(Entity attacker, ServerLevel level) {
        if (attacker instanceof ServerPlayer serverPlayer) {
            return serverPlayer;
        }

        if (attacker instanceof ChirithyEntity chirithy && chirithy.getOwnerUUID() != null) {
            Player player = level.getPlayerByUUID(chirithy.getOwnerUUID());

            if (player instanceof ServerPlayer serverPlayer) {
                return serverPlayer;
            }

            return null;
        }

        if (attacker instanceof MeowWowEntity meowWow && meowWow.getOwnerUUID() != null) {
            Player player = level.getPlayerByUUID(meowWow.getOwnerUUID());

            if (player instanceof ServerPlayer serverPlayer) {
                return serverPlayer;
            }

            return null;
        }

        if (attacker instanceof KomoryBatEntity komoryBat && komoryBat.getOwnerUUID() != null) {
            Player player = level.getPlayerByUUID(komoryBat.getOwnerUUID());

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

        return 1.00F;
    }
}