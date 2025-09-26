package online.remind.remind.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;

import java.util.Objects;

@Mixin(GuardSkill.class)
public class GuardSkillMixin {

    int maxTicks = 200;
    int ticks;

    @Inject(method = "dealEvent", at = @At("TAIL"), remap = false)
    public void dealEventInject(PlayerPatch<?> playerpatch, HurtEvent.Pre event, boolean advanced, CallbackInfo ci) {

        Player player = playerpatch.getOriginal();
        Entity attacker = event.getDamageSource().getEntity();
        IPlayerCapabilities playerCapabilities = ModCapabilities.getPlayer(player);
        IGlobalCapabilitiesRM globalData = ModCapabilitiesRM.getGlobal(player);
        IGlobalCapabilities attackerData = ModCapabilities.getGlobal((LivingEntity) Objects.requireNonNull(event.getDamageSource().getEntity()));

        globalData.setCanCounter(1);
        //System.out.println("Can Counter! " + globalData.getCanCounter());
        PacketHandlerRM.syncGlobalToAllAround(player, globalData);

        // Block Abilities Auto-give parry?


        // Block Abilities Effects


        if (playerCapabilities.isAbilityEquipped(StringsRM.renewalBlock)) {
            if (event.isParried()){
                player.heal(player.getMaxHealth() * 0.075F);
                player.getFoodData().eat(2, 2);
            } else {
                player.heal(player.getMaxHealth() * 0.025F);
                player.getFoodData().eat(1, 1);
            }
            event.getPlayerPatch().playSound(ModSounds.savepoint.get(), 1f, 1f);

        }

        if (playerCapabilities.isAbilityEquipped(StringsRM.focusBlock)) {
            if (event.isParried()){
                playerCapabilities.addFocus(25);
            } else {
                playerCapabilities.addFocus(10);
            }
        }

        // Stop Block Code? :)
        if (playerCapabilities.isAbilityEquipped(StringsRM.stopBlock)) {
            if (event.isParried()) {
                IGlobalCapabilities target = ModCapabilities.getGlobal((LivingEntity) attacker);
                if (playerCapabilities.getMP() >= 10) {
                    ((LivingEntity) attacker).addEffect(new MobEffectInstance(ModMobEffects.STOP.get(), 40, 2, false, false, false));
                    event.getPlayerPatch().playSound(ModSounds.stop.get(), 1f, 1f);
                    playerCapabilities.remMP(10);
                }
            }
        }
        // Royal Guard
        if (playerCapabilities.isAbilityEquipped(StringsRM.royalGuard)) {
            if (event.isParried()) {
                if (!playerCapabilities.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
                    playerCapabilities.addFP(25);
                } else if (playerCapabilities.getActiveDriveForm().equals(DriveForm.NONE.toString())){
                    playerCapabilities.addDP(25);
                }
                event.getPlayerPatch().playSound(ModSoundsRM.ROYAL_PARRY.get(), 1f, 1f);
            } else {
                if (!playerCapabilities.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
                    playerCapabilities.addFP(10);
                } else if (playerCapabilities.getActiveDriveForm().equals(DriveForm.NONE.toString())){
                    playerCapabilities.addDP(10);
                }
                event.getPlayerPatch().playSound(ModSoundsRM.ROYAL_GUARD.get(), 1f, 1f);
            }

            PacketHandler.syncToAllAround(player, playerCapabilities);
        }






    }
}
