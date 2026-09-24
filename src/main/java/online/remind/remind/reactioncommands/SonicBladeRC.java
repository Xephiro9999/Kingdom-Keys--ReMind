package online.remind.remind.reactioncommands;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.entity.attacks.BlitzCollider;
import online.remind.remind.entity.attacks.SonicBladeCollider;
import online.remind.remind.integration.epicfight.EpicFightEvents;
import online.remind.remind.integration.epicfight.RMIntegrationHooks;
import online.remind.remind.magic.attacks.attackSonicBlade;

public class SonicBladeRC extends ReactionCommand {

    public SonicBladeRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck, 20, 0xFFD700);
    }

    @Override
    public void onUse(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null) {
            return;
        }

        MobEffectInstance chain = player.getEffect(ModMobEffectsRM.SONIC_BLADE_CHAIN);

        if (chain == null) {
            return;
        }

        int chainStep = chain.getAmplifier();

        player.removeEffect(
                ModMobEffectsRM.SONIC_BLADE_CHAIN
        );

// Actually launch the next Sonic Blade charge.
        if (KingdomKeysReMind.efmLoaded){
            EpicFightEvents.playSonicBladeAnimation((ServerPlayer) player);
        }

        attackSonicBlade.launchSonicBladeDash(
                player,
                chainStep
        );

        float dmg = serverPlayer.getPersistentData().getFloat(
                attackSonicBlade.SONIC_BLADE_DAMAGE
        );

        SonicBladeCollider collider = new SonicBladeCollider(
                player.level(),
                player,
                dmg,
                chainStep
        );

        player.level().addFreshEntity(collider);

        if (chainStep == 6){
            PlayerData playerData = PlayerData.get(player);
            if (playerData != null) {
                if (playerData.isFormActive(ModDriveForms.LIMIT)) {
                    float formXP = playerData.getDriveFormLevel(ModDriveForms.LIMIT.location());
                    playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), (int) (playerData.getDriveFormExp(playerData.getActiveDriveForm()) + formXP + 1));
                    PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer)player);
                }
            }
        }


        /*
         * Tiny RC cooldown so the same input can't accidentally
         * activate the next command immediately.
         */
        globalData.setRCCooldownTicks(4);
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

        return player.hasEffect(ModMobEffectsRM.SONIC_BLADE_CHAIN);
    }
}