package online.remind.remind.shotlock;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.remind.remind.client.sound.ModSoundsRM;

import java.util.List;

public class ShotlockHeartlessAngel extends Shotlock {

    public ShotlockHeartlessAngel(String registeryName, int order, int cooldown, int max){
        super(registeryName,order,cooldown,max);
    }

    int maxTicks;

    @Override
    public void doPartialShotlock(Player player, List<Entity> list) {

    }

    @Override
    public void doFullShotlock(Player player, List<Entity> list) {
        Entity target = list.get(0);
        if (target instanceof Player){
            PlayerData targetData = PlayerData.get((Player) target);

            targetData.remMP(targetData.getMaxMP());
            targetData.remFocus(100);
            targetData.remDP(1000);
        }
        if (target instanceof LivingEntity le) {
            le.setHealth(2);
        }
        //System.out.println(target);
        double rand = Math.floor(Math.random() * 100);
        if (rand >= 50) {
            player.level().playSound(null, player.blockPosition(), ModSoundsRM.HEARTLESS_ANGEL.get(), SoundSource.PLAYERS, 1F, 1F);
        } else {
            player.level().playSound(null, player.blockPosition(), ModSoundsRM.HEARTLESS_ANGEL_EN.get(), SoundSource.PLAYERS, 1F, 1F);
        }
    }
}
