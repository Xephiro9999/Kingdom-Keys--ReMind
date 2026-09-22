package online.remind.remind.magic.attacks;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.client.gui.FF7AttackHud;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.integration.CrossSlashAnimationBridge;

public class attackCrossSlash extends Magic {

    public attackCrossSlash(ResourceLocation registryName, boolean hasToSelect, int tier, ResourceLocation gmAbility) {
        super(registryName, hasToSelect, gmAbility);
        setTier(tier);
    }

    @Override
    public void magicUse(
            LivingEntity player,
            LivingEntity caster,
            float fullMPBlastMult,
            LivingEntity lockOnEntity
    ) {

        // Cross Slash requires a living target
        if (lockOnEntity == null || !lockOnEntity.isAlive()) {
            return;
        }

        PlayerData playerData = PlayerData.get((Player) caster);
        if (playerData.getFocus() <= 30){
            player.sendSystemMessage(Component.literal("Not enough Focus!"));
            playerData.addMP(getMagicData().getCost());
            playerData.setMagicCooldownTicks(0);
            return;
        }

        playerData.remFocus(30);


        float dmg;

        switch (getTier()) {
            case 0 -> dmg = casterStrengthStat(caster) * 0.5F;
            case 1 -> dmg = casterStrengthStat(caster) * 0.65F;
            case 2 -> dmg = casterStrengthStat(caster) * 0.8F;
            default -> dmg = casterStrengthStat(caster);
        }

        dmg *= fullMPBlastMult;

        System.out.println(
                "Cross Slash started against "
                        + lockOnEntity.getName().getString()
                        + " | damage: "
                        + dmg
        );

        if (!(caster instanceof ServerPlayer serverPlayer)) {
            return;
        }

        FF7AttackHud.show((ServerPlayer) player, "Cross-Slash");
        CrossSlashSequenceHandler.start(
                serverPlayer,
                lockOnEntity,
                dmg
        );
    }


    @Override
    public void playMagicCastSound(LivingEntity livingEntity, LivingEntity livingEntity1) {
        livingEntity.level().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), ModSoundsRM.LIMIT_BREAK.get(), SoundSource.PLAYERS, 1F, 1F);

    }
}