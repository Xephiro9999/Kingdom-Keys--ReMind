package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.attacks.fireSurgeCollider;
import online.remind.remind.entity.attacks.ravenousSaberCollider;
import online.remind.remind.entity.reactioncommand.DarkMineEntity;
import online.remind.remind.entity.reactioncommand.ThornsEntity;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

public class RegenRC extends ReactionCommand {


    public RegenRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck);
    }


    @Override
    public void onUse(Player player, LivingEntity livingEntity, LivingEntity lockOnEntity) {
        if (conditionsToAppear(player, player)) {
            PlayerData playerData = PlayerData.get(player);
            IGlobalDataRM globalData = ModDataRM.getGlobal(player);

            // Damage Calculation

            float dmg = playerData.getStrength(true) * 0.25f + playerData.getMagic(true) * 0.25f;
            float dmgmult = 1;
            if (playerData.isAbilityEquipped(StringsRM.spellblade)){
                dmgmult = 1.5f;
            }

            globalData.setRCCooldownTicks(60);
            playerData.setFP(playerData.getFP() - 40);

            player.swing(InteractionHand.MAIN_HAND, true);
            player.level().playSound(null, player.blockPosition(), ModSoundsRM.RISKCHARGE.get(), SoundSource.PLAYERS, 1F, 1F);

            // DEBUGGING
            //System.out.println("Base Damage: "+ dmg);
            //System.out.println("Damage Mult: "+ dmgmult);
            //System.out.println("Damage Total: "+ (dmg *dmgmult));

            ravenousSaberCollider surge = new ravenousSaberCollider(player.level(), player, dmg);
            player.level().addFreshEntity(surge);


            // Sync Packet
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        }
    }


    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        PlayerData playerData = PlayerData.get(player);
        IGlobalDataRM globalData = ModDataRM.getGlobal(player);
        if (playerData != null) {
            if (playerData.getAlignment() == Utils.OrgMember.NONE) {
                if (playerData.isAbilityEquipped(StringsRM.Regen) && globalData.getRCCooldownTicks() == 0) {

                    return true;
                }
            }
        }
        return false;
    }
}