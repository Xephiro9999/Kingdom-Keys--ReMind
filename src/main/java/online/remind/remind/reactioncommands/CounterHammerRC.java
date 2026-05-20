package online.remind.remind.reactioncommands;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import org.joml.Vector3f;
import yesman.epicfight.registry.entries.EpicFightSounds;

import java.util.List;

public class CounterHammerRC extends ReactionCommand {
    public CounterHammerRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck, 20 * 2);
    }

    @Override
    public void onUse(Player player, LivingEntity target, LivingEntity lockedOnEntity) {
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);
        float dmg = (float) playerData.getStrengthStat().get();
        float dmgMult = 1 + (PlayerData.get(player).getNumberOfAbilitiesEquipped(Strings.criticalBoost) * 0.10F);
        float radius = 3 + (PlayerData.get(player).getNumberOfAbilitiesEquipped(Strings.criticalBoost) * 0.25F);
        globalData.setRCCooldownTicks(60);


        double X = player.getX();
        double Z = player.getZ();

        player.swing(InteractionHand.MAIN_HAND);
        PacketHandlerRM.syncGlobalToAllAround(player, globalData);

        target.level().playSound(null, target.blockPosition(), EpicFightSounds.BLUNT_HIT_HARD.get(), SoundSource.PLAYERS, 1F, 1F);


        List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty((player), player, radius, radius, radius);
        for (LivingEntity e : targetList) {
            for (int t = 1; t < 360; t += 20) {
                for (int s = 1; s < 360; s += 20) {
                    double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                    double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                    ((ServerLevel) player.level()).sendParticles(new DustParticleOptions(new Vector3f(1F,1F,1F),1F),x,player.getY() ,z,1,0,0,0,0);
                    e.knockback(2, -e.getX(),-e.getZ());
                    e.hurt(e.damageSources().indirectMagic(e, player), dmg * dmgMult);
                }
            }
        }
        playerData.removeReactionCommand(getRegistryName().toString());

    }

    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        PlayerData playerData = PlayerData.get(player);
        if (playerData != null ){
            return playerData.isAbilityEquipped(StringsRM.counterHammer);
        }
        return false;
    }
}

