package online.remind.remind.dreameater;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.spirits.CactuarSpiritEntity;
import online.remind.remind.entity.spirits.ChirithyEntity;
import online.remind.remind.entity.spirits.KomoryBatEntity;
import online.remind.remind.entity.spirits.MeowWowEntity;

import java.lang.reflect.Method;
import java.util.UUID;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        bus = EventBusSubscriber.Bus.GAME
)
public class DreamEaterKillExpHandler {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }

        LivingEntity killed = event.getEntity();

        Entity sourceEntity = event.getSource().getEntity();
        Entity directEntity = event.getSource().getDirectEntity();

        LivingEntity dreamEater = getDreamEaterFromDamageSource(sourceEntity, directEntity);

        if (dreamEater == null) {
            return;
        }

        ServerPlayer owner = getDreamEaterOwner(dreamEater);

        if (owner == null) {
            return;
        }

        if (!owner.isAlive()) {
            return;
        }

        if (killed == owner || killed == dreamEater) {
            return;
        }

        /*
         * Do not reward EXP for killing other player-owned Dream Eaters/pets.
         */
        if (isOwnedByPlayer(killed)) {
            return;
        }

        PlayerData ownerData = PlayerData.get(owner);

        if (ownerData == null) {
            return;
        }

        int exp = getExperienceFromKilledEntity(killed);

        if (exp <= 0) {
            return;
        }

        ownerData.addExperience(owner, exp, true, true);

        PacketHandler.sendTo(new SCSyncPlayerData(owner), owner);
    }

    private static LivingEntity getDreamEaterFromDamageSource(Entity sourceEntity, Entity directEntity) {
        if (sourceEntity instanceof LivingEntity living && isDreamEaterEntity(living)) {
            return living;
        }

        if (directEntity instanceof LivingEntity living && isDreamEaterEntity(living)) {
            return living;
        }

        if (sourceEntity instanceof Projectile projectile) {
            Entity owner = projectile.getOwner();

            if (owner instanceof LivingEntity living && isDreamEaterEntity(living)) {
                return living;
            }
        }

        if (directEntity instanceof Projectile projectile) {
            Entity owner = projectile.getOwner();

            if (owner instanceof LivingEntity living && isDreamEaterEntity(living)) {
                return living;
            }
        }

        return null;
    }

    private static boolean isDreamEaterEntity(LivingEntity entity) {
        return entity instanceof ChirithyEntity
                || entity instanceof MeowWowEntity
                || entity instanceof KomoryBatEntity
                || entity instanceof CactuarSpiritEntity;
    }

    private static ServerPlayer getDreamEaterOwner(LivingEntity dreamEater) {
        UUID ownerUUID = getDreamEaterOwnerUUID(dreamEater);

        if (ownerUUID == null) {
            return null;
        }

        Level level = dreamEater.level();

        if (level.getServer() == null) {
            return null;
        }

        return level.getServer().getPlayerList().getPlayer(ownerUUID);
    }

    private static UUID getDreamEaterOwnerUUID(LivingEntity dreamEater) {
        if (dreamEater instanceof ChirithyEntity chirithy) {
            return chirithy.getOwnerUUID();
        }

        if (dreamEater instanceof MeowWowEntity meowWow) {
            return meowWow.getOwnerUUID();
        }

        if (dreamEater instanceof KomoryBatEntity komoryBat) {
            return komoryBat.getOwnerUUID();
        }

        if (dreamEater instanceof CactuarSpiritEntity cactuar) {
            return cactuar.getOwnerUUID();
        }

        return null;
    }

    private static boolean isOwnedByPlayer(LivingEntity entity) {
        if (entity instanceof TamableAnimal tamableAnimal) {
            return tamableAnimal.getOwnerUUID() != null;
        }

        if (entity instanceof ChirithyEntity chirithy) {
            return chirithy.getOwnerUUID() != null;
        }

        if (entity instanceof MeowWowEntity meowWow) {
            return meowWow.getOwnerUUID() != null;
        }

        if (entity instanceof KomoryBatEntity komoryBat) {
            return komoryBat.getOwnerUUID() != null;
        }

        if (entity instanceof CactuarSpiritEntity cactuar) {
            return cactuar.getOwnerUUID() != null;
        }

        return false;
    }

    private static int getExperienceFromKilledEntity(LivingEntity killed) {
        /*
         * First try to use Kingdom Keys enemy EXP if the killed entity has it.
         * This avoids hard-breaking if the KK entity class name/package changes.
         */
        int reflectedExp = tryCallIntGetter(killed, "getExperienceGiven");

        if (reflectedExp > 0) {
            return reflectedExp;
        }

        reflectedExp = tryCallIntGetter(killed, "getExpGiven");

        if (reflectedExp > 0) {
            return reflectedExp;
        }

        /*
         * Fallback for vanilla or non-KK mobs.
         */
        if (killed instanceof Monster) {
            return Math.max(1, Mth.floor(killed.getMaxHealth() / 4.0F));
        }

        return Math.max(1, Mth.floor(killed.getMaxHealth() / 8.0F));
    }

    private static int tryCallIntGetter(LivingEntity entity, String methodName) {
        if (entity == null || methodName == null || methodName.isEmpty()) {
            return 0;
        }

        try {
            Method method = entity.getClass().getMethod(methodName);
            Object result = method.invoke(entity);

            if (result instanceof Integer integer) {
                return integer;
            }

            if (result instanceof Number number) {
                return number.intValue();
            }
        } catch (Exception ignored) {
        }

        return 0;
    }
}