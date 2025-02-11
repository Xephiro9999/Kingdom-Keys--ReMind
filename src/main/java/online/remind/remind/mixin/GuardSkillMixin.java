/* TODO No EFM 1.21
package online.remind.remind.mixin;

import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;

@Mixin(GuardSkill.class)
public class GuardSkillMixin {

    int maxTicks = 200;
    int ticks;

    @Inject(method = "dealEvent", at = @At("TAIL"), remap = false)
    public void dealEventInject(PlayerPatch<?> playerpatch, HurtEvent.Pre event, boolean advanced, CallbackInfo ci) {

        Player player = playerpatch.getOriginal();
        IPlayerCapabilities playerCapabilities = ModCapabilities.getPlayer(player);
        IGlobalCapabilitiesRM globalData = ModCapabilitiesRM.getGlobal(player);

        globalData.setCanCounter(1);
        //System.out.println("Can Counter! " + globalData.getCanCounter());
        PacketHandlerRM.syncGlobalToAllAround(player, globalData);


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




    }
}
*/