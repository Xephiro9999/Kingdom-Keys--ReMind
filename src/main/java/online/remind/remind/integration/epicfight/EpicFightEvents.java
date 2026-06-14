package online.remind.remind.integration.epicfight;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.api.event.AbilityEvent;
import online.kingdomkeys.kingdomkeys.api.event.MagicSpellCastEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKAnimations;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.ability.ModAbilitiesRM;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.lib.StringsRM;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.registry.entries.EpicFightSkills;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class EpicFightEvents {

    private LivingEntity target;
    ResourceLocation name;
    float dmg;
    double speed;
    boolean animationsPlayed;
    public int ticks;
    int maxTicks;


    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
            //System.out.println(player.level().isClientSide() + " " + playerpatch);
            if (playerpatch != null) {
                //System.out.println(playerpatch.getSkill(SkillSlots.GUARD).getSkill());
            }
        }
    }

    @SubscribeEvent
    public void onMagicCast(MagicSpellCastEvent e){
        LivingEntity caster = e.getCaster();
        if (caster instanceof Player player){
            PlayerData playerData = PlayerData.get(player);
            if (playerData != null) {
            if (KingdomKeysReMind.efmLoaded) {
                PlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
                if (playerpatch.isEpicFightMode()) {
                        String spell = String.valueOf(e.getSpellID());
                        switch (spell) {
                            // Jump Attacks
                            case "kkremind:attack_quick_blitz", "kkremind:attack_blitz", "kkremind:attack_slot_edge":
                                player.addEffect(new MobEffectInstance(
                                        ModMobEffectsRM.RM_ANIMATION_LOCK,
                                        20,
                                        0,
                                        false,
                                        false,
                                        false
                                ));
                                playerpatch.playAnimationSynchronized(KKAnimations.SORA_FINISHER1.get().getRealAnimation(), 0.1f);
                                break;
                            // Dash Attacks
                            case "kkremind:attack_sliding_dash":
                                player.addEffect(new MobEffectInstance(
                                        ModMobEffectsRM.RM_ANIMATION_LOCK,
                                        20,
                                        0,
                                        false,
                                        false,
                                        false
                                ));
                                playerpatch.playAnimationSynchronized(Animations.SWORD_DASH.get().getRealAnimation(), 0.25f);
                                break;
                            case "kkremind:attack_confusion_strike", "kkremind:attack_binding_strike", "kkremind:attack_dark_strike", "kkremind:attack_fire_strike", "kkremind:attack_blizzard_strike", "kkremind:attack_thunder_strike", "kkremind:attack_water_strike", "kkremind:attack_aero_strike", "kkremind:attack_light_strike":
                                player.addEffect(new MobEffectInstance(
                                        ModMobEffectsRM.RM_ANIMATION_LOCK,
                                        10,
                                        0,
                                        false,
                                        false,
                                        false
                                ));
                                playerpatch.playAnimationSynchronized(Animations.SWORD_AUTO3.get().getRealAnimation(), 0.25f);
                                break;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Pre event) {

        Player player = event.getEntity();
        if (KingdomKeysReMind.efmLoaded) {
            PlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
            PlayerData playerData = PlayerData.get(player);
            if (playerpatch.isEpicFightMode()) {
                if (playerData != null) {
                    if (playerData.getCastedMagic() != null) {
                        //player.sendSystemMessage(Component.literal(playerData.getCastedMagic().magic().getRegistryName().toString()));
                        String spellName = playerData.getCastedMagic().magic().getRegistryName().toString();

                        if (spellName.equals("kkremind:attack_sliding_dash")) {
                            if (!animationsPlayed) {
                                if (playerData.getMagicCasttimeTicks() == 1) {
                                    //playerpatch.playAnimationSynchronized(Animations.SWORD_DASH.get().getRealAnimation(), 0f);
                                    animationsPlayed = true;
                                }
                            }

                            //player.sendSystemMessage(Component.literal("Sliding Dash"));

                        }
                        if (spellName.equals("kkremind:attack_quick_blitz")) {
                            if (!animationsPlayed) {
                                if (playerData.getMagicCasttimeTicks() == 1) {
                                    //playerpatch.playAnimationSynchronized(KKAnimations.SORA_FINISHER1.get().getRealAnimation(), 0.1f);
                                    animationsPlayed = true;
                                }
                            }
                        }

                        if (spellName.equals("kkremind:attack_zantetsuken") || spellName.equals("kkremind:attack_swift_strike")) {
                            if (!animationsPlayed) {
                                if (playerData.getMagicCasttimeTicks() <= 40) {
                                    playerpatch.playAnimationSynchronized(Animations.BIPED_HOLD_UCHIGATANA.get().getRealAnimation(), 0.0f);
                                }
                                if (playerData.getMagicCasttimeTicks() <= 30) {
                                    playerpatch.playAnimationSynchronized(Animations.BIPED_HOLD_UCHIGATANA_SHEATHING.get().getRealAnimation(), 0.10f);
                                }
                                if (playerData.getMagicCasttimeTicks() == 1) {
                                    player.addEffect(new MobEffectInstance(
                                            ModMobEffectsRM.RM_ANIMATION_LOCK,
                                            20,
                                            0,
                                            false,
                                            false,
                                            false
                                    ));
                                    playerpatch.playAnimationSynchronized(Animations.UCHIGATANA_SHEATHING_AUTO.get().getRealAnimation(), 0.10f);


                                    animationsPlayed = true;
                                }
                            }
                        }

                        //TODO: Add more attacks and spells later!


                    } else if (playerData.getCastedMagic() == null) {
                        animationsPlayed = false; // to prevent animations not going off again and a reset
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public void equipAbility(AbilityEvent.Equip event) {
        if (KingdomKeysReMind.efmLoaded) {
            Player player = event.getPlayer();
            PlayerData playerData = PlayerData.get(event.getPlayer());
            GlobalDataRM playerData2 = ModDataRM.getGlobal(event.getPlayer());
            WorldData worldData = WorldData.get(event.getPlayer().getServer());

            if (event.getAbility().equals(ModAbilitiesRM.RENEWAL_BLOCK.get())
                    || event.getAbility().equals(ModAbilitiesRM.FOCUS_BLOCK.get())
                    || event.getAbility().equals(ModAbilitiesRM.STOP_BLOCK.get())
                    || event.getAbility().equals(ModAbilitiesRM.ROYAL_GUARD.get())
                    || event.getAbility().equals(ModAbilitiesRM.POISON_BLOCK.get())
                    || event.getAbility().equals(ModAbilitiesRM.CONFUSION_BLOCK.get())) {

                PlayerPatch playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
                SkillContainer skillContainer = playerPatch.getPlayerSkills().getSkillContainerFor(SkillSlots.GUARD);

                if (playerPatch.getSkill(SkillSlots.GUARD).isEmpty()) {
                    playerPatch.getSkill(SkillSlots.GUARD).setSkill(EpicFightSkills.GUARD.get());

                    player.sendSystemMessage(Component.literal("[KK-Re:Mind]: You now have the Guard Skill from Epic Fight!"));

                    if (!player.level().isClientSide) {
                        EpicFightNetworkManager.sendToAllPlayerTrackingThisEntity(skillContainer.createSyncPacketToRemotePlayer(), player);
                        EpicFightNetworkManager.sendToPlayer(skillContainer.createSyncPacketToLocalPlayer(), (ServerPlayer) player);
                    }
                }
            }

            if (event.getAbility().equals(ModAbilitiesRM.RENEWAL_BLOCK.get())) {
                if (playerData.isAbilityEquipped(StringsRM.focusBlock)
                        || playerData.isAbilityEquipped(StringsRM.stopBlock)
                        || playerData.isAbilityEquipped(StringsRM.royalGuard)
                        || playerData.isAbilityEquipped(StringsRM.poisonBlock)
                        || playerData.isAbilityEquipped(StringsRM.confusionBlock)) {

                    playerData.unequipAbility(StringsRM.focusBlock, 0);
                    playerData.unequipAbility(StringsRM.stopBlock, 0);
                    playerData.unequipAbility(StringsRM.royalGuard, 0);
                    playerData.unequipAbility(StringsRM.poisonBlock, 0);
                    playerData.unequipAbility(StringsRM.confusionBlock, 0);
                }
            }

            if (event.getAbility().equals(ModAbilitiesRM.FOCUS_BLOCK.get())) {
                if (playerData.isAbilityEquipped(StringsRM.renewalBlock)
                        || playerData.isAbilityEquipped(StringsRM.stopBlock)
                        || playerData.isAbilityEquipped(StringsRM.royalGuard)
                        || playerData.isAbilityEquipped(StringsRM.poisonBlock)
                        || playerData.isAbilityEquipped(StringsRM.confusionBlock)) {

                    playerData.unequipAbility(StringsRM.renewalBlock, 0);
                    playerData.unequipAbility(StringsRM.stopBlock, 0);
                    playerData.unequipAbility(StringsRM.royalGuard, 0);
                    playerData.unequipAbility(StringsRM.poisonBlock, 0);
                    playerData.unequipAbility(StringsRM.confusionBlock, 0);
                }
            }

            if (event.getAbility().equals(ModAbilitiesRM.STOP_BLOCK.get())) {
                if (playerData.isAbilityEquipped(StringsRM.renewalBlock)
                        || playerData.isAbilityEquipped(StringsRM.focusBlock)
                        || playerData.isAbilityEquipped(StringsRM.royalGuard)
                        || playerData.isAbilityEquipped(StringsRM.poisonBlock)
                        || playerData.isAbilityEquipped(StringsRM.confusionBlock)) {

                    playerData.unequipAbility(StringsRM.renewalBlock, 0);
                    playerData.unequipAbility(StringsRM.focusBlock, 0);
                    playerData.unequipAbility(StringsRM.royalGuard, 0);
                    playerData.unequipAbility(StringsRM.poisonBlock, 0);
                    playerData.unequipAbility(StringsRM.confusionBlock, 0);
                }
            }

            if (event.getAbility().equals(ModAbilitiesRM.ROYAL_GUARD.get())) {
                if (playerData.isAbilityEquipped(StringsRM.renewalBlock)
                        || playerData.isAbilityEquipped(StringsRM.stopBlock)
                        || playerData.isAbilityEquipped(StringsRM.focusBlock)
                        || playerData.isAbilityEquipped(StringsRM.poisonBlock)
                        || playerData.isAbilityEquipped(StringsRM.confusionBlock)) {

                    playerData.unequipAbility(StringsRM.renewalBlock, 0);
                    playerData.unequipAbility(StringsRM.stopBlock, 0);
                    playerData.unequipAbility(StringsRM.focusBlock, 0);
                    playerData.unequipAbility(StringsRM.poisonBlock, 0);
                    playerData.unequipAbility(StringsRM.confusionBlock, 0);
                }
            }

            if (event.getAbility().equals(ModAbilitiesRM.POISON_BLOCK.get())) {
                if (playerData.isAbilityEquipped(StringsRM.renewalBlock)
                        || playerData.isAbilityEquipped(StringsRM.stopBlock)
                        || playerData.isAbilityEquipped(StringsRM.focusBlock)
                        || playerData.isAbilityEquipped(StringsRM.royalGuard)
                        || playerData.isAbilityEquipped(StringsRM.confusionBlock)) {

                    playerData.unequipAbility(StringsRM.renewalBlock, 0);
                    playerData.unequipAbility(StringsRM.stopBlock, 0);
                    playerData.unequipAbility(StringsRM.focusBlock, 0);
                    playerData.unequipAbility(StringsRM.royalGuard, 0);
                    playerData.unequipAbility(StringsRM.confusionBlock, 0);
                }
            }

            if (event.getAbility().equals(ModAbilitiesRM.CONFUSION_BLOCK.get())) {
                if (playerData.isAbilityEquipped(StringsRM.renewalBlock)
                        || playerData.isAbilityEquipped(StringsRM.stopBlock)
                        || playerData.isAbilityEquipped(StringsRM.focusBlock)
                        || playerData.isAbilityEquipped(StringsRM.royalGuard)
                        || playerData.isAbilityEquipped(StringsRM.poisonBlock)) {

                    playerData.unequipAbility(StringsRM.renewalBlock, 0);
                    playerData.unequipAbility(StringsRM.stopBlock, 0);
                    playerData.unequipAbility(StringsRM.focusBlock, 0);
                    playerData.unequipAbility(StringsRM.royalGuard, 0);
                    playerData.unequipAbility(StringsRM.poisonBlock, 0);
                }
            }


            if (event.getAbility().equals(ModAbilitiesRM.COUNTER_HAMMER.get())) {
                if (playerData.isAbilityEquipped(StringsRM.counterBlast) || playerData.isAbilityEquipped(StringsRM.counterRush)) {
                    playerData.unequipAbility(StringsRM.counterBlast, 0);
                    playerData.unequipAbility(StringsRM.counterRush, 0);
                }
            }

            if (event.getAbility().equals(ModAbilitiesRM.COUNTER_BLAST.get())) {
                if (playerData.isAbilityEquipped(StringsRM.counterHammer) || playerData.isAbilityEquipped(StringsRM.counterRush)) {
                    playerData.unequipAbility(StringsRM.counterHammer, 0);
                    playerData.unequipAbility(StringsRM.counterRush, 0);
                }
            }

            if (event.getAbility().equals(ModAbilitiesRM.COUNTER_RUSH.get())) {
                if (playerData.isAbilityEquipped(StringsRM.counterHammer) || playerData.isAbilityEquipped(StringsRM.counterBlast)) {
                    playerData.unequipAbility(StringsRM.counterHammer, 0);
                    playerData.unequipAbility(StringsRM.counterBlast, 0);
                }
            }
        }
    }

    /*public void hurtEvent(LivingDamageEvent.Pre event) {
        System.out.println(event.getSource());
        if (event.getSource().getEntity() instanceof Player player) {
            PlayerData playerData = PlayerData.get(player);
            GlobalDataRM globalData = ModDataRM.getGlobal(player);
            PlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);

        }
    }*/

    public void onEffectAdded(MobEffectEvent.Added event) {
        if (event.getEntity() instanceof Player player) {
            PlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
            playerpatch.isEpicFightMode();
            Animator animator = playerpatch.getAnimator();
            while (player.hasEffect(ModMobEffects.STOP)) {
                animator.setHardPause(true);
            }
        } else {
            EntityPatch<?> patch = EpicFightCapabilities.getEntityPatch(event.getEntity(), EntityPatch.class);
            if (patch != null){
                //Animator animator = patch.getAnimator();

            }
        }
    }
}


