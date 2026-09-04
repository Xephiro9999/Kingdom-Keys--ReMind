package online.remind.remind.integration.epicfight;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.api.event.AbilityEvent;
import online.kingdomkeys.kingdomkeys.api.event.MagicSpellCastEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKAnimations;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
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
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EpicFightEvents {

    private LivingEntity target;
    ResourceLocation name;
    float dmg;
    double speed;
    boolean animationsPlayed;
    public int ticks;
    int maxTicks;

    private final Map<UUID, PendingAttackAnimation> pendingAttackAnimations = new HashMap<>();


    // Delayed Attack Command animation data
    private static class PendingAttackAnimation {

        private final String spell;
        private int delay;

        private PendingAttackAnimation(String spell, int delay) {
            this.spell = spell;
            this.delay = delay;
        }
    }


    // Queues a Re:Mind Attack Command animation after Kingdom Keys finishes its cast animation
    private void queueAttackAnimation(Player player, String spell) {

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        pendingAttackAnimations.put(
                serverPlayer.getUUID(),
                new PendingAttackAnimation(spell, 1)
        );
    }


    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {

        Player player = e.getEntity();
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);
        PlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(
                player,
                PlayerPatch.class
        );

        if (playerData == null) {
            return;
        }

        if (playerData.getChosen() == SoAState.GUARDIAN) {

        }

        if (playerData.getChosen() == SoAState.WARRIOR) {

        }
    }


    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {

        if (event.getEntity() instanceof Player player) {

            pendingAttackAnimations.remove(player.getUUID());

            PlayerPatch playerpatch =
                    EpicFightCapabilities.getEntityPatch(
                            player,
                            PlayerPatch.class
                    );

            if (playerpatch != null) {

            }
        }
    }


    // Handles Attack Command casts and queues their Epic Fight animations
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onMagicCast(MagicSpellCastEvent e) {

        LivingEntity caster = e.getCaster();

        if (!(caster instanceof Player player)) {
            return;
        }

        if (!KingdomKeysReMind.efmLoaded) {
            return;
        }

        PlayerData playerData = PlayerData.get(player);

        if (playerData == null) {
            return;
        }

        PlayerPatch playerpatch =
                EpicFightCapabilities.getEntityPatch(
                        player,
                        PlayerPatch.class
                );

        if (playerpatch == null || !playerpatch.isEpicFightMode()) {
            return;
        }

        String spell = String.valueOf(e.getSpellID());

        switch (spell) {

            case "kkremind:attack_quick_blitz",
                 "kkremind:attack_blitz",
                 "kkremind:attack_slot_edge":

                player.addEffect(new MobEffectInstance(
                        ModMobEffectsRM.RM_ANIMATION_LOCK,
                        20,
                        0,
                        false,
                        false,
                        false
                ));

                queueAttackAnimation(player, spell);
                break;


            case "kkremind:attack_sliding_dash":

                player.addEffect(new MobEffectInstance(
                        ModMobEffectsRM.RM_ANIMATION_LOCK,
                        20,
                        0,
                        false,
                        false,
                        false
                ));

                queueAttackAnimation(player, spell);
                break;


            case "kkremind:attack_confusion_strike",
                 "kkremind:attack_binding_strike",
                 "kkremind:attack_dark_strike",
                 "kkremind:attack_fire_strike",
                 "kkremind:attack_blizzard_strike",
                 "kkremind:attack_thunder_strike",
                 "kkremind:attack_water_strike",
                 "kkremind:attack_aero_strike",
                 "kkremind:attack_light_strike":

                player.addEffect(new MobEffectInstance(
                        ModMobEffectsRM.RM_ANIMATION_LOCK,
                        10,
                        0,
                        false,
                        false,
                        false
                ));

                queueAttackAnimation(player, spell);
                break;
        }
    }


    // Plays the queued Attack Command animation after the generic KK cast animation
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onAttackAnimationTick(PlayerTickEvent.Post event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        PendingAttackAnimation pending =
                pendingAttackAnimations.get(player.getUUID());

        if (pending == null) {
            return;
        }

        if (pending.delay > 0) {
            pending.delay--;
            return;
        }

        ServerPlayerPatch patch =
                EpicFightCapabilities.getEntityPatch(
                        player,
                        ServerPlayerPatch.class
                );

        if (patch == null || !patch.isEpicFightMode()) {
            pendingAttackAnimations.remove(player.getUUID());
            return;
        }

        switch (pending.spell) {

            case "kkremind:attack_quick_blitz",
                 "kkremind:attack_blitz",
                 "kkremind:attack_slot_edge":

                patch.playAnimationSynchronized(
                        KKAnimations.SORA_FINISHER1
                                .get()
                                .getRealAnimation(),
                        0.1F
                );
                break;


            case "kkremind:attack_sliding_dash":

                patch.playAnimationSynchronized(
                        Animations.SWORD_DASH
                                .get()
                                .getRealAnimation(),
                        0.25F
                );
                break;


            case "kkremind:attack_confusion_strike",
                 "kkremind:attack_binding_strike",
                 "kkremind:attack_dark_strike",
                 "kkremind:attack_fire_strike",
                 "kkremind:attack_blizzard_strike",
                 "kkremind:attack_thunder_strike",
                 "kkremind:attack_water_strike",
                 "kkremind:attack_aero_strike",
                 "kkremind:attack_light_strike":

                patch.playAnimationSynchronized(
                        Animations.SWORD_AUTO3
                                .get()
                                .getRealAnimation(),
                        0.25F
                );
                break;
        }

        pendingAttackAnimations.remove(player.getUUID());
    }


    // Handles the existing Zantetsuken and Swift Strike cast animations
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Pre event) {

        Player player = event.getEntity();

        if (!KingdomKeysReMind.efmLoaded) {
            return;
        }

        PlayerPatch playerpatch =
                EpicFightCapabilities.getEntityPatch(
                        player,
                        PlayerPatch.class
                );

        PlayerData playerData = PlayerData.get(player);

        if (playerpatch == null || !playerpatch.isEpicFightMode()) {
            return;
        }

        if (playerData == null) {
            return;
        }

        if (playerData.getCastedMagic() != null) {

            String spellName =
                    playerData
                            .getCastedMagic()
                            .magic()
                            .getRegistryName()
                            .toString();

            if (spellName.equals("kkremind:attack_sliding_dash")) {

                if (!animationsPlayed) {

                    if (playerData.getMagicCasttimeTicks() == 1) {
                        animationsPlayed = true;
                    }
                }
            }


            if (spellName.equals("kkremind:attack_quick_blitz")) {

                if (!animationsPlayed) {

                    if (playerData.getMagicCasttimeTicks() == 1) {
                        animationsPlayed = true;
                    }
                }
            }


            if (spellName.equals("kkremind:attack_zantetsuken")
                    || spellName.equals("kkremind:attack_swift_strike")) {

                if (!animationsPlayed) {

                    if (playerData.getMagicCasttimeTicks() <= 50) {

                        playerpatch.playAnimationSynchronized(
                                Animations.BIPED_HOLD_UCHIGATANA
                                        .get()
                                        .getRealAnimation(),
                                0.0F
                        );
                    }

                    if (playerData.getMagicCasttimeTicks() <= 30) {

                        playerpatch.playAnimationSynchronized(
                                Animations.BIPED_HOLD_UCHIGATANA_SHEATHING
                                        .get()
                                        .getRealAnimation(),
                                0.10F
                        );
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

                        playerpatch.playAnimationSynchronized(
                                Animations.UCHIGATANA_SHEATHING_AUTO
                                        .get()
                                        .getRealAnimation(),
                                0.10F
                        );

                        animationsPlayed = true;
                    }
                }
            }

        } else {

            animationsPlayed = false;
        }
    }


    // Handles Re:Mind guard abilities in Epic Fight
    @SubscribeEvent
    public void equipAbility(AbilityEvent.Equip event) {

        if (!KingdomKeysReMind.efmLoaded) {
            return;
        }

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

            PlayerPatch playerPatch =
                    EpicFightCapabilities.getEntityPatch(
                            player,
                            PlayerPatch.class
                    );

            if (playerPatch == null) {
                return;
            }

            SkillContainer skillContainer =
                    playerPatch
                            .getPlayerSkills()
                            .getSkillContainerFor(SkillSlots.GUARD);

            if (playerPatch.getSkill(SkillSlots.GUARD).isEmpty()) {

                playerPatch
                        .getSkill(SkillSlots.GUARD)
                        .setSkill(EpicFightSkills.GUARD.get());

                player.sendSystemMessage(
                        Component.literal(
                                "[KK-Re:Mind]: You now have the Guard Skill from Epic Fight!"
                        )
                );

                if (!player.level().isClientSide
                        && player instanceof ServerPlayer serverPlayer) {

                    EpicFightNetworkManager.sendToAllPlayerTrackingThisEntity(
                            skillContainer.createSyncPacketToRemotePlayer(),
                            player
                    );

                    EpicFightNetworkManager.sendToPlayer(
                            skillContainer.createSyncPacketToLocalPlayer(),
                            serverPlayer
                    );
                }
            }
        }
    }


    // Handles Stop's Epic Fight animator pause
    public void onEffectAdded(MobEffectEvent.Added event) {

        if (event.getEntity() instanceof Player player) {

            PlayerPatch playerpatch =
                    EpicFightCapabilities.getEntityPatch(
                            player,
                            PlayerPatch.class
                    );

            if (playerpatch == null) {
                return;
            }

            playerpatch.isEpicFightMode();

            Animator animator = playerpatch.getAnimator();

            while (player.hasEffect(ModMobEffects.STOP)) {
                animator.setHardPause(true);
            }

        } else {

            EntityPatch<?> patch =
                    EpicFightCapabilities.getEntityPatch(
                            event.getEntity(),
                            EntityPatch.class
                    );

            if (patch != null) {

            }
        }
    }
}