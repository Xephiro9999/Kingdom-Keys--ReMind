package online.remind.remind.mixin;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.SkillContainer;
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


        if(playerCapabilities.isAbilityEquipped(StringsRM.renewalBlock)) {
            player.heal(player.getMaxHealth() * 0.05F);
            player.getFoodData().eat(3,3);
            //System.out.println("Healed for "+ player.getMaxHealth() * 0.05F +" on Block!");
        }

        if(playerCapabilities.isAbilityEquipped(StringsRM.focusBlock)) {
            playerCapabilities.addFocus(10);
            //System.out.println("Focus Restored on Block!");
            //System.out.println(event.getDamageSource().getEntity());
        }

        // Stop Block Code? :)
        if(playerCapabilities.isAbilityEquipped(StringsRM.stopBlock)){

            System.out.println(attackerData);
            event.getDamageSource().getEntity().level().playSound(player, event.getDamageSource().getEntity().position().x(),event.getDamageSource().getEntity().position().y(),event.getDamageSource().getEntity().position().z(), ModSounds.stop.get(), SoundSource.MASTER, 1.0f, 1.0f);
            attackerData.setStoppedTicks(40);

        }




    }
}
