package online.remind.remind.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.event.types.entity.TakeDamageEvent;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;




@Mixin(GuardSkill.class)
public class GuardSkillMixin {

    @Inject(method = "dealEvent", at = @At("TAIL"), remap = false)
    public void dealEventInject(PlayerPatch<?> playerpatch, TakeDamageEvent.Income event, boolean advanced, CallbackInfo ci) {
        Player player = playerpatch.getOriginal();
        Entity attacker = event.getDamageSource().getEntity();
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);
        if(playerData == null)
            return;

        if(globalData.getRCCooldownTicks() <= 0) {
            if(playerData.isAbilityEquipped(StringsRM.counterHammer))
                playerData.addReactionCommand(StringsRM.CounterHammerRC, player);
            if(playerData.isAbilityEquipped(StringsRM.counterBlast))
                playerData.addReactionCommand(StringsRM.CounterBlastRC, player);
            if(playerData.isAbilityEquipped(StringsRM.counterRush))
                playerData.addReactionCommand(StringsRM.CounterRushRC, player);

            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
        }

        // Block Abilities Effects
        if(playerData.isAbilityEquipped(StringsRM.renewalBlock)) {
            if (!event.isParried()){
                player.heal(player.getMaxHealth() * 0.025F);
                player.getFoodData().eat(3,3);
            } else {
                player.heal(player.getMaxHealth() * 0.075F);
                player.getFoodData().eat(3,3);
            }
            event.getEntityPatch().playSound(ModSounds.savepoint.get(), 1f, 1f);
        }

        if(playerData.isAbilityEquipped(StringsRM.focusBlock)) {
            if (!event.isParried()){
                playerData.addFocus(5);
            } else {
                playerData.addFocus(15);
            }
        }

        if(playerData.isAbilityEquipped(StringsRM.blockReplenisher)) {
            if (!event.isParried()){
                playerData.addMP(5);
            } else {
                playerData.addMP(15);
            }
        }


        // Stop Block Code? :)
        if (playerData.isAbilityEquipped(StringsRM.stopBlock)) {
            if (event.isParried()) {
                GlobalData target = GlobalData.get((LivingEntity) attacker);
                if (playerData.getMP() >= 10 && !playerData.getRecharge()) {
                    ((LivingEntity) attacker).addEffect(new MobEffectInstance(ModMobEffects.STOP, 60, 2, false, false, false));
                    event.getEntityPatch().playSound(ModSounds.stop.get(), 1f, 1f);
                    playerData.remMP(10);
                }
            }
        }

        if (playerData.isAbilityEquipped(StringsRM.poisonBlock)) {
            GlobalData target = GlobalData.get((LivingEntity) attacker);
            if (!event.isParried()) {
                    ((LivingEntity) attacker).addEffect(new MobEffectInstance(MobEffects.POISON, 60, 1, true, true, true));
                    //event.getEntityPatch().playSound(ModSounds.stop.get(), 1f, 1f);
            } else {
                ((LivingEntity) attacker).addEffect(new MobEffectInstance(MobEffects.POISON, 80, 2, true, true, true));
            }
        }


        // Royal Guard
        if (playerData.isAbilityEquipped(StringsRM.royalGuard)) {
            if (event.isParried()) {
                if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
                    playerData.addFP(25);
                } else if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())){
                    playerData.addDP(25);
                }
                event.getEntityPatch().playSound(ModSoundsRM.ROYAL_PARRY.get(), 1f, 1f);
            } else {
                if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
                    playerData.addFP(10);
                } else if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())){
                    playerData.addDP(10);
                }
                event.getEntityPatch().playSound(ModSoundsRM.ROYAL_GUARD.get(), 1f, 1f);
            }

            PacketHandler.syncToAllAround(player, playerData);
        }




    }
}
