package online.remind.remind.shotlock;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.remind.remind.entity.shotlock.ThunderstormCoreEntity;

import java.util.List;

public class ShotlockThunderstorm extends Shotlock {

    public ShotlockThunderstorm(String registeryName, int order, int cooldown, int max){
        super(registeryName,order,cooldown,max);
    }

    @Override
    public void onUse(Player player, List<Entity> targetList) {

        float damage = getDamage(player) + (PlayerData.get(player).getNumberOfAbilitiesEquipped(Strings.thunderBoost) * 0.2F);
        ThunderstormCoreEntity core = new ThunderstormCoreEntity(player.level(), player, targetList, damage);
        core.setPos(player.getX(), player.getY(), player.getZ());
        player.level().addFreshEntity(core);



    }

    @Override
    public void doPartialShotlock(Player player, List<Entity> list) {

    }

    @Override
    public void doFullShotlock(Player player, List<Entity> list) {

    }
}