package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.attacks.lightSurgeCollider;
import online.remind.remind.lib.StringsRM;

public class attackLightSurge extends Magic {


    public attackLightSurge(ResourceLocation registryName, boolean hasToSelect, int tier, String gmAbility) {
        super(registryName, hasToSelect, gmAbility);
setTier(tier);
    }

    public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
        PlayerData playerData = PlayerData.get(caster);
        float dmg = playerData.getStrength(true) * (playerData.getNumberOfAbilitiesEquipped(StringsRM.lightBoost) * 0.1f);

        if (getTier() == 0){
            dmg = playerData.getStrength(true) * (playerData.getNumberOfAbilitiesEquipped(StringsRM.lightBoost) * 0.1f);
        } else if (getTier() == 1){
            dmg = (playerData.getStrength(true) * 1.1f) * (playerData.getNumberOfAbilitiesEquipped(StringsRM.lightBoost) * 0.1f);
        } else if (getTier() == 2){
            dmg = (playerData.getStrength(true) * 1.25f) * (playerData.getNumberOfAbilitiesEquipped(StringsRM.lightBoost) * 0.1f);
        }



        float radius = 1.5f + (0.5f * getTier());

        double speed = 0.75;

        double yawRad = Math.toRadians(player.getYRot());
        double dx = -Math.sin(yawRad) * speed;
        double jump = 0.175;
        double dz = Math.cos(yawRad) * speed;
        float yaw = player.getYRot();
        float motionX = -Mth.sin(yaw / 180.0f * (float) Math.PI);
        float motionZ = Mth.cos(yaw / 180.0f * (float) Math.PI);
        caster.setDeltaMovement(dx, jump, dz);
        caster.push(motionX, 0, motionZ);
        caster.hurtMarked = true;
        caster.fallDistance = 0;

        lightSurgeCollider surge = new lightSurgeCollider(caster.level(), caster, dmg);
        caster.level().addFreshEntity(surge);
    }

        @Override
    public void playMagicCastSound(LivingEntity player, Player caster) {
            player.level().playSound(null, player.blockPosition(), ModSoundsRM.HOLY.get(), SoundSource.PLAYERS, 1F, 1F);
    }
}
