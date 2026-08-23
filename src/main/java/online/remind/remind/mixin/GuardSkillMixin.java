package online.remind.remind.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
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
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.remind.remind.ability.ModAbilitiesRM;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.reactioncommands.ModReactionCommandsRM;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.event.types.entity.TakeDamageEvent;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;





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
            if(playerData.isAbilityEquipped(ModAbilitiesRM.COUNTER_HAMMER))
                playerData.addReactionCommand(ModReactionCommandsRM.COUNTER_HAMMER.location(), player);
            if(playerData.isAbilityEquipped(ModAbilitiesRM.COUNTER_BLAST))
                playerData.addReactionCommand(ModReactionCommandsRM.COUNTER_BLAST.location(), player);
            if(playerData.isAbilityEquipped(ModAbilitiesRM.COUNTER_RUSH))
                playerData.addReactionCommand(ModReactionCommandsRM.COUNTER_RUSH.location(), player);

            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
        }

        event.getEntityPatch().playSound(ModSounds.guard.get(), 1f, 1f);


        // Block Abilities Effects
        if(playerData.isAbilityEquipped(ModAbilitiesRM.RENEWAL_BLOCK)) {
            if (!event.isParried()){
                player.heal(player.getMaxHealth() * 0.025F);
                player.getFoodData().eat(1,1);
            } else {
                player.heal(player.getMaxHealth() * 0.05F);
                player.getFoodData().eat(2,2);
            }
            event.getEntityPatch().playSound(ModSounds.savepoint.get(), 1f, 1f);

        }

        if(playerData.isAbilityEquipped(ModAbilitiesRM.FOCUS_BLOCK)) {
            if (!event.isParried()){
                playerData.addFocus(5);
            } else {
                playerData.addFocus(15);
            }
        }

        if(playerData.isAbilityEquipped(ModAbilitiesRM.BLOCK_REPLENISHER)) {
            if (!event.isParried()){
                playerData.addMP(5);
            } else {
                playerData.addMP(15);
            }
        }


        if(attacker instanceof LivingEntity livingEntity) {
            // Stop Block Code? :)
            if (playerData.isAbilityEquipped(ModAbilitiesRM.STOP_BLOCK)) {
                if (event.isParried()) {
                    if (playerData.getMP() >= 10 && !playerData.getRecharge()) {
                        livingEntity.addEffect(new MobEffectInstance(ModMobEffects.STOP, 60, 2, false, false, false));
                        event.getEntityPatch().playSound(ModSounds.stop.get(), 1f, 1f);
                        playerData.remMP(10);
                    }
                }
            }

            if (playerData.isAbilityEquipped(ModAbilitiesRM.POISON_BLOCK)) {
                if (!event.isParried()) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 1, true, true, true));
                } else {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 2, true, true, true));
                }
            }

            if (playerData.isAbilityEquipped(ModAbilitiesRM.CONFUSION_BLOCK)) {
                if (!event.isParried()) {
                    livingEntity.addEffect(new MobEffectInstance(ModMobEffectsRM.CONFUSE, 20 * 5, 1, true, true, true));
                } else {
                    livingEntity.addEffect(new MobEffectInstance(ModMobEffectsRM.CONFUSE, 20 * 7, 2, true, true, true));
                }
            }
        }

        // Royal Guard
        if (playerData.isAbilityEquipped(ModAbilitiesRM.ROYAL_GUARD)) {
            if (event.isParried()) {
                if (!playerData.isFormActive(ModDriveForms.NONE)) {
                    playerData.addFP(25);
                } else if (playerData.isFormActive(ModDriveForms.NONE)) {
                    playerData.addDP(25);
                }
                event.getEntityPatch().playSound(ModSoundsRM.ROYAL_PARRY.get(), 1f, 1f);
            } else {
                if (!playerData.isFormActive(ModDriveForms.NONE)) {
                    playerData.addFP(10);
                } else if (playerData.isFormActive(ModDriveForms.NONE)) {
                    playerData.addDP(10);
                }
                event.getEntityPatch().playSound(ModSoundsRM.ROYAL_GUARD.get(), 1f, 1f);
            }

            PacketHandler.syncToAllAround(player, playerData);
        }



    }

    @Redirect(
            method = "guard",
            at = @At(
                    value = "INVOKE",
                    target = "Lyesman/epicfight/world/capabilities/entitypatch/player/ServerPlayerPatch;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"
            ),
            remap = false
    )
    private void kkremind$cancelEpicFightGuardSound(
            ServerPlayerPatch playerPatch,
            SoundEvent sound,
            float pitchModifierMin,
            float pitchModifierMax
    ) {
        // Intentionally do nothing.
        // Re:Mind supplies its own guard sound in dealEventInject().
    }
}
