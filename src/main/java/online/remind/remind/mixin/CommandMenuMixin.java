package online.remind.remind.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.remind.remind.lib.StringsRM;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommandMenuGui.class)
public class CommandMenuMixin {

    @ModifyExpressionValue(
            method = "createMagicFromRegistry",
            at = @At(
                    value = "INVOKE",
                    target = "Lonline/kingdomkeys/kingdomkeys/data/PlayerData;getRecharge()Z",
                    shift = At.Shift.BEFORE
            )


    )
    private boolean munnyMagic(boolean rechargeResult, @Local PlayerData playerData, @Local double cost){
        rechargeResult = playerData.getRecharge();
        if (!rechargeResult){
            return false;
        }
        Player player = Minecraft.getInstance().player;
        if (player == null) return true;

        if(playerData.isAbilityEquipped(StringsRM.munny_magic)){
            System.out.println("Munny Magic Activated!");
            return true;
        }

        int munny = playerData.getMunny();
        if (munny >= cost){
            return false;
        }
        playerData.setMunny((int) ((int) munny - cost));
        System.out.println("Munny Magic Activated!");
        return true;

    }

}
