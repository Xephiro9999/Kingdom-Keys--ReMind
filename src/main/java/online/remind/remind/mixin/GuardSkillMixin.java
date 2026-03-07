package online.remind.remind.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.client.sound.ModSoundsRM;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.event.types.entity.TakeDamageEvent;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;




@Mixin(GuardSkill.class)
public class GuardSkillMixin {

    int maxTicks = 200;
    int ticks;

    @Inject(method = "dealEvent", at = @At("TAIL"), remap = false)
    public void dealEventInject(PlayerPatch<?> playerpatch, TakeDamageEvent.Income event, boolean advanced, CallbackInfo ci) {
        Player player = playerpatch.getOriginal();
        Entity attacker = event.getDamageSource().getEntity();
        PlayerData playerData = PlayerData.get(player);
        IGlobalDataRM globalData = ModDataRM.getGlobal(player);

        globalData.setCanCounter(1);
        //System.out.println("Debugging Message: Can Counter? " + globalData.getCanCounter());
        PacketHandlerRM.syncGlobalToAllAround(player, globalData);


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
                playerData.addFocus(10);
            } else {
                playerData.addFocus(25);
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
