package online.remind.remind.shotlock;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.remind.remind.entity.shotlock.BubbleBlasterCoreEntity;

import java.util.List;

public class ShotlockBubbleBlaster extends Shotlock {

    public ShotlockBubbleBlaster(String registeryName, int order, int cooldown, int max){
        super(registeryName,order,cooldown,max);
    }

    @Override
    public void onUse(Player player, List<Entity> targetList) {

        float damage = getDamage(player) + (PlayerData.get(player).getNumberOfAbilitiesEquipped(ModAbilities.WATER_BOOST) * 0.2F);
        BubbleBlasterCoreEntity core = new BubbleBlasterCoreEntity(player.level(), player, targetList, damage);
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