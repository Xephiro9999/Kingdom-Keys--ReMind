package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.attacks.quickBlitzCollider;

public class attackQuickBlitz extends Magic {

    public attackQuickBlitz(ResourceLocation registryName, boolean hasToSelect, int tier, String gmAbility) {
        super(registryName, hasToSelect, gmAbility);
setTier(tier);
    }

    private LivingEntity target;
    private boolean hasLandedAttack = false;
    private double speed = 1.2;
    private double hitRange = 1.5;
    float dmg;

    @Override
    public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
        PlayerData playerData = PlayerData.get(caster);

        switch(getTier()){
            case 0:
                dmg = playerData.getStrength(true) * 0.7f;
                break;
            case 1:
                dmg = playerData.getStrength(true) * 0.85f;
                break;
            case 2:
                dmg = playerData.getStrength(true);
                break;
        }

        double speed = 1.5;
        double jump = 0.5;
        double yawRad = Math.toRadians(player.getYRot());
        double dx = -Math.sin(yawRad) * speed;
        double dz = Math.cos(yawRad) * speed;

        caster.hurtMarked = true;
        caster.fallDistance = 0;

        if (KingdomKeysReMind.efmLoaded){
            caster.setDeltaMovement(dx/2.25, jump, dz/2.25);
        } else {
            caster.setDeltaMovement(dx, jump, dz);
        }

        quickBlitzCollider quickBlitz = new quickBlitzCollider(player.level(), player, dmg);
        caster.level().addFreshEntity(quickBlitz);

    }

    @Override
    public void playMagicCastSound(LivingEntity player, Player caster) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1F, 1F);

    }
}
