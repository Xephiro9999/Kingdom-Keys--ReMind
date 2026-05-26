package online.remind.remind.reactioncommands;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.item.ModItemsRM;

public class ExceedRC extends ReactionCommand {
    public ExceedRC(ResourceLocation registryName, boolean constantCheck, int duration, int color) {
        super(registryName, constantCheck, -1, 0x002E68);
    }

    @Override
    public void onUse(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (playerData == null || globalData == null) {
            return;
        }

        boolean perfect = player.hasEffect(ModMobEffectsRM.EXCEED_WINDOW);

        addFortunaExceed(player, perfect);

        player.removeEffect(ModMobEffectsRM.EXCEED_WINDOW);

        globalData.setRCCooldownTicks(perfect ? 10 : 20);


        player.level().playSound(
                null,
                player.blockPosition(),
                perfect ? SoundEvents.PLAYER_LEVELUP : SoundEvents.BLAZE_SHOOT,
                SoundSource.PLAYERS,
                1.0F,
                perfect ? 1.5F : 1.0F
        );
    }

    private void playFortunaChargeEffects(Player player, int exceedLevel, boolean perfect) {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        double x = player.getX();
        double y = player.getY() + 1.0D;
        double z = player.getZ();

        level.playSound(
                null,
                player.blockPosition(),
                perfect ? SoundEvents.PLAYER_LEVELUP : SoundEvents.FIRECHARGE_USE,
                SoundSource.PLAYERS,
                perfect ? 1.0F : 0.7F,
                perfect ? 1.8F : 1.0F + exceedLevel * 0.15F
        );

        level.sendParticles(
                ParticleTypes.FLAME,
                x, y, z,
                perfect ? 60 : 20 * exceedLevel,
                0.5D, 0.7D, 0.5D,
                0.08D
        );

        level.sendParticles(
                perfect ? ParticleTypes.ENCHANT : ParticleTypes.CRIT,
                x, y, z,
                perfect ? 50 : 10 * exceedLevel,
                0.6D, 0.8D, 0.6D,
                perfect ? 0.7D : 0.2D
        );

        if (perfect) {
            level.sendParticles(
                    ParticleTypes.EXPLOSION,
                    x,
                    y,
                    z,
                    1,
                    0.0D,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }

    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (playerData == null || globalData == null) {
            return false;
        }

        if (playerData.getEquippedKeychain(DriveForm.NONE) == null) {
            return false;
        }

        if (playerData.getAlignment() != Utils.OrgMember.NONE) {
            return false;
        }

        if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
            return false;
        }

        if (playerData.getEquippedKeychain(DriveForm.NONE).getItem() != ModItemsRM.fortunaChain.get()) {
            return false;
        }

        if (globalData.getRCCooldownTicks() != 0) {
            return false;
        }

        MobEffectInstance exceed = player.getEffect(ModMobEffectsRM.EXCEED);
        boolean perfectWindow = player.hasEffect(ModMobEffectsRM.EXCEED_WINDOW);

// If already Level 3, hide RC unless the player is inside the perfect timing window.
        if (exceed != null && exceed.getAmplifier() >= 2 && !perfectWindow) {
            return false;
        }

        return true;
    }

    private void addFortunaExceed(Player player, boolean perfect) {
        MobEffectInstance current = player.getEffect(ModMobEffectsRM.EXCEED);

        int newAmplifier;

        if (perfect) {
            newAmplifier = 2; // instant MAX-Act
        } else if (current != null) {
            newAmplifier = Math.min(current.getAmplifier() + 1, 2);
        } else {
            newAmplifier = 0;
        }

        int duration = perfect ? 300 : 200; // 15 sec perfect, 10 sec normal

        player.addEffect(new MobEffectInstance(
                ModMobEffectsRM.EXCEED,
                duration,
                newAmplifier,
                false,
                true,
                true
        ));

        MobEffectInstance exceed = player.getEffect(ModMobEffectsRM.EXCEED);
        int exceedLevel = exceed != null ? exceed.getAmplifier() + 1 : 1;

        playFortunaChargeEffects(player, exceedLevel, perfect);
    }
}
