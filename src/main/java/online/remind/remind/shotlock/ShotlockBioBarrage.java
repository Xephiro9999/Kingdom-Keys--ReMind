package online.remind.remind.shotlock;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.remind.remind.entity.shotlock.BioBarrageCoreEntity;

import java.util.List;

public class ShotlockBioBarrage extends Shotlock {

    public ShotlockBioBarrage(String registeryName, int order){
        super(registeryName,order);
    }

    /*
    @Override
    public void onUse(Player player, List<Entity> targetList) {

        float damage = (float) (DamageCalculation.getMagicDamage(player) * 1.25);
        BioBarrageCoreEntity core = new BioBarrageCoreEntity(player.level(), player, targetList, damage);
        core.setPos(player.getX(), player.getY(), player.getZ());
        player.level().addFreshEntity(core);



    }*/

    @Override
    public void doPartialShotlock(Player player, List<Entity> targetList) {
        BioBarrageCoreEntity core = new BioBarrageCoreEntity(player.level(), player, targetList, getDamage(player));
        core.setPos(player.getX(), player.getY(), player.getZ());
        player.level().addFreshEntity(core);
    }

    @Override
    public void doFullShotlock(Player player, List<Entity> targetList) {
        doPartialShotlock(player,targetList);
    }
}
