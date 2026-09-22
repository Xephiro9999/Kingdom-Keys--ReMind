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

public class attackBraver extends Magic {

    public attackBraver(ResourceLocation registryName, boolean hasToSelect, int tier, ResourceLocation gmAbility) {
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
        if (lockOnEntity == null || !lockOnEntity.isAlive()) {
            return;
        }

        if (!(caster instanceof ServerPlayer serverPlayer)) {
            return;
        }

        PlayerData playerData = PlayerData.get((Player) caster);
        if (playerData.getFocus() <= 20){
            player.sendSystemMessage(Component.literal("Not enough Focus!"));
            playerData.addMP(getMagicData().getCost());
            playerData.setMagicCooldownTicks(0);
            return;
        }

        playerData.remFocus(20);


        float dmg;

        switch (getTier()) {
            case 0 -> dmg = casterStrengthStat(caster) * 1.50F;
            case 1 -> dmg = casterStrengthStat(caster) * 1.75F;
            case 2 -> dmg = casterStrengthStat(caster) * 2.00F;
            default -> dmg = casterStrengthStat(caster);
        }

        dmg *= fullMPBlastMult;

        FF7AttackHud.show((ServerPlayer) player, "Braver");
        BraverSequenceHandler.start(
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