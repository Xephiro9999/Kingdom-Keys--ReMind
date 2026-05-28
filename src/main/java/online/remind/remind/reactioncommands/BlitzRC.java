package online.remind.remind.reactioncommands;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.entity.attacks.BlitzCollider;
import online.remind.remind.integration.epicfight.RMEpicFightAnimationHelper;
import online.remind.remind.integration.RMIntegrationHooks;

public class BlitzRC extends ReactionCommand {

    public BlitzRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck, 20, 0xFFD700);
    }



    @Override
    public void onUse(Player player, LivingEntity livingEntity, LivingEntity target) {
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (playerData == null || globalData == null) {
            return;
        }

        MobEffectInstance chain = player.getEffect(ModMobEffectsRM.BLITZ_CHAIN);

        if (chain == null) {
            return;
        }

        int chainStep = chain.getAmplifier();

        RMIntegrationHooks.playBlitzAnimation(player, chainStep);

        player.removeEffect(ModMobEffectsRM.BLITZ_CHAIN);

        float dmg = switch (chainStep) {
            case 1 -> playerData.getStrength(true) * 1.35F;
            case 2 -> playerData.getStrength(true) * 1.8F;
            default -> playerData.getStrength(true) * 1.2F;
        };

        launchBlitzDash(player, chainStep);

        BlitzCollider blitz = new BlitzCollider(
                player.level(),
                player,
                dmg,
                chainStep
        );

        player.level().addFreshEntity(blitz);

        globalData.setRCCooldownTicks(4);

        player.displayClientMessage(
                Component.literal(chainStep >= 2 ? "Blitz Finisher!" : "Blitz Rush!")
                        .withColor(0xFFD700),
                true
        );

        player.level().playSound(
                null,
                player.blockPosition(),
                SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.PLAYERS,
                1.0F,
                1.0F + chainStep * 0.2F
        );
    }

    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null) {
            return false;
        }

        if (globalData.getRCCooldownTicks() > 0) {
            return false;
        }

        return player.hasEffect(ModMobEffectsRM.BLITZ_CHAIN);
    }

    private void launchBlitzDash(Player caster, int chainStep) {
        double speed = switch (chainStep) {
            case 1 -> 1.85D;
            case 2 -> 2.15D;
            default -> 1.65D;
        };

        double jump = chainStep >= 2 ? 0.45D : 0.35D;
        double yawRad = Math.toRadians(caster.getYRot());
        double dx = -Math.sin(yawRad) * speed;
        double dz = Math.cos(yawRad) * speed;

        caster.hurtMarked = true;
        caster.fallDistance = 0.0F;

        if (!RMIntegrationHooks.isEpicFightMode(caster)) {
            caster.setDeltaMovement(dx, jump, dz);
        }
    }
}