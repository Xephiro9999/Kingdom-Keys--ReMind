package online.remind.remind.mixin;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.lib.StringsRM;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DriveForm.class)
public class KKDriveFormMixin {

    @Inject(method = "initDrive", at = @At("TAIL"), remap = false)
    public void initDriveInject(Player player, CallbackInfo ci) {
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        if (playerData != null && playerData.getEquippedKeychain(DriveForm.NONE) != null) {
            if (playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.darkForm)) {
                if (playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.soulEaterChain.get() || playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.keybladeOfPeoplesHeartsChain.get()) {
                    player.level().playSound(null, player.blockPosition(), ModSoundsRM.DARK_MODE.get(), SoundSource.MASTER, 1.0f, 1.0f);
                }
            }
        }
    }

}
