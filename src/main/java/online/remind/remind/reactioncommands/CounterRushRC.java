package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.entity.reactioncommand.CounterRushCore;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.List;

public class CounterRushRC extends ReactionCommand {
    public CounterRushRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck, 20 * 2);
    }
    int ticks = 0;
    int tickCount = 120;

    @Override
    public void onUse(Player player, LivingEntity target, LivingEntity lockedOnEntity) {
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);
        float dmg = (float) (playerData.getStrengthStat().get() * 0.015f);
        int hits = (int) (4 + (PlayerData.get(player).getNumberOfAbilitiesEquipped(StringsRM.attackHaste) * 0.5));
        float radius = 3;
        globalData.setRCCooldownTicks(60);
        int hitsDealt = 0;

        //System.out.println(dmg);
        //System.out.println(hits);

        double X = player.getX();
        double Z = player.getZ();

        player.swing(InteractionHand.MAIN_HAND);
        PacketHandlerRM.syncGlobalToAllAround(player, globalData);

        List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty((player), player, radius, radius, radius);
        // Comment Here
        /*for (LivingEntity e : targetList) {
            for (int t = 1; t < 360; t += 20) {
                for (int s = 1; s < 360; s += 20) {
                    double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                    double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                    ((ServerLevel) player.level()).sendParticles(new DustParticleOptions(new Vector3f(1F,1F,1F),1F),x,player.getY() ,z,1,0,0,0,0);
                    //EpicFightParticles.HIT_BLADE.get().spawnParticleWithArgument(((ServerLevel)e.level()), HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, e, e);
                    for (int h = 0; h < hits; h += 1) {
                            e.hurt(e.damageSources().indirectMagic(e, player), dmg);
                            System.out.println(dmg);
                            e.invulnerableTime = 0;
                    }
                }
            }
        }*/


        CounterRushCore core = new CounterRushCore(player, player.level(), targetList, dmg, false);
        core.setPos(player.getX(), player.getY(), player.getZ());
        player.level().addFreshEntity(core);
        playerData.removeReactionCommand(getRegistryName().toString());
    }

    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        PlayerData playerData = PlayerData.get(player);
        if (playerData != null ){
	        return playerData.isAbilityEquipped(StringsRM.counterRush);
        }
        return false;
    }
}

