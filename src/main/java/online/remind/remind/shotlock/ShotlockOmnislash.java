package online.remind.remind.shotlock;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.remind.remind.client.gui.FF7AttackHud;
import online.remind.remind.client.sound.ModSoundsRM;

import java.util.List;

public class ShotlockOmnislash extends Shotlock {

    public ShotlockOmnislash(String registeryName, int order) {
        super(registeryName, order);
    }

    @Override
    public void doPartialShotlock(
            Player player,
            List<Entity> targetList
    ) {

    }

    @Override
    public void doFullShotlock(
            Player player,
            List<Entity> targetList
    ) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        if (targetList == null || targetList.isEmpty()) {
            return;
        }

        LivingEntity target = null;

        for (Entity entity : targetList) {
            if (entity instanceof LivingEntity living
                    && living.isAlive()) {

                target = living;
                break;
            }
        }

        if (target == null) {
            return;
        }

        PlayerData playerData =
                PlayerData.get(player);

        if (playerData == null) {
            return;
        }

        /*
         * Use the player's actual combined STR as Omnislash's
         * damage basis instead of Shotlock.getDamage().
         */
        float strength =
                playerData.getStrength(true);

        FF7AttackHud.show(serverPlayer, "Omnislash");
        OmnislashSequenceHandler.start(
                serverPlayer,
                target,
                strength
        );

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.LIMIT_BREAK.get(), SoundSource.PLAYERS, 1F, 1F);

    }
}