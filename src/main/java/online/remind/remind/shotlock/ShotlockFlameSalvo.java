package online.remind.remind.shotlock;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.common.EventBusSubscriber;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.shotlock.FlameSalvoCoreEntity;

import java.util.List;

public class ShotlockFlameSalvo extends Shotlock {

    public ShotlockFlameSalvo(String registeryName, int order, int cooldown, int max){
        super(registeryName,order,cooldown,max);
    }

    @Override
    public void onUse(Player player, List<Entity> targetList) {

        float damage = getDamage(player) + (PlayerData.get(player).getNumberOfAbilitiesEquipped(Strings.fireBoost) * 0.2F);
        FlameSalvoCoreEntity core = new FlameSalvoCoreEntity(player.level(), player, targetList, damage);
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
