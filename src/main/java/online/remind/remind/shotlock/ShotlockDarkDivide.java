package online.remind.remind.shotlock;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.remind.remind.ability.ModAbilitiesRM;
import online.remind.remind.entity.shotlock.DarkDivideCoreEntity;
import online.remind.remind.entity.shotlock.DarkFiragaCoreEntity;
import online.remind.remind.lib.StringsRM;

import java.util.List;

public class ShotlockDarkDivide extends Shotlock {

    public ShotlockDarkDivide(String registeryName, int order){
        super(registeryName,order);
    }

    @Override
    public void doPartialShotlock(Player player, List<Entity> targetList) {
        float damage = getDamage(player) + ((PlayerData.get(player).getNumberOfAbilitiesEquipped(ModAbilitiesRM.DARKNESS_BOOST) * 0.2F)  + (PlayerData.get(player).getNumberOfAbilitiesEquipped(ModAbilities.FIRE_BOOST) * 0.2F));
        DarkFiragaCoreEntity core = new DarkFiragaCoreEntity(player.level(), player, targetList, getDamage(player));
        core.setPos(player.getX(), player.getY(), player.getZ());
        player.level().addFreshEntity(core);
    }

    @Override
    public void doFullShotlock(Player player, List<Entity> targetList) {
        float damage = getDamage(player) +  ((PlayerData.get(player).getNumberOfAbilitiesEquipped(ModAbilitiesRM.DARKNESS_BOOST) * 0.35F) + (PlayerData.get(player).getNumberOfAbilitiesEquipped(ModAbilities.FIRE_BOOST) * 0.35F));
        DarkDivideCoreEntity core = new DarkDivideCoreEntity(player.level(), player, targetList, getDamage(player));
        core.setPos(player.getX(), player.getY(), player.getZ());
        player.level().addFreshEntity(core);
    }
}

