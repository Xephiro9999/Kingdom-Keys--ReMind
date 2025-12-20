package online.remind.remind.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKAnimations;
import online.remind.remind.KingdomKeysReMind;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(DriveForm.class)
public class EFMKKMagicMixin {
    @Inject(method = "onUse", at = @At("HEAD"), remap = false)
    public final void onUse(Player player, Player caster, int level, LivingEntity lockOnEntity) {

        // TODO: Figure out how to make spells register specific animations
        /*PlayerData playerData = PlayerData.get(player);
        if (KingdomKeysReMind.efmLoaded) {
            PlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
            String spellName = playerData.getCastedMagic().magic().getRegistryName().toString();
            int spellLevel = playerData.getCastedMagic().level();

            if (spellName.equals("kkremind:attack_sliding_dash")) {
                    playerpatch.playAnimationSynchronized(Animations.SWORD_DASH.get().getRealAnimation(), 0f);
                //player.sendSystemMessage(Component.literal("Sliding Dash"));
            }
            if (spellName.equals("kkremind:attack_quick_blitz")) {
                    playerpatch.playAnimationSynchronized(KKAnimations.SORA_FINISHER1.get().getRealAnimation(), 0.1f);
                //player.sendSystemMessage(Component.literal("Quick Blitz"));
            }
        }*/
    }
}
