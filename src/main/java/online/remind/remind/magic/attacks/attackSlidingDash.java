package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.entity.attacks.quickBlitzCollider;
import online.remind.remind.entity.attacks.slidingDashCollider;

public class attackSlidingDash extends Magic {

    public attackSlidingDash(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
        super(registryName, hasToSelect, maxLevel, null);
    }

    private LivingEntity target;
    float dmg;
    double speed;

    @Override
    public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
        PlayerData playerData = PlayerData.get(player);

        switch(level){
            case 0:
                dmg = playerData.getStrength(true) * 0.9f;
                speed = 2.5;
                break;
            case 1:
                dmg = playerData.getStrength(true);
                speed = 3.25;
                break;
            case 2:
                dmg = playerData.getStrength(true) * 1.1f;
                speed = 4;
                break;
        }


        double yawRad = Math.toRadians(player.getYRot());
        double dx = -Math.sin(yawRad) * speed;
        double jump = 0.175;
        double dz = Math.cos(yawRad) * speed;
        caster.setDeltaMovement(dx, jump, dz);
        caster.hurtMarked = true;
        caster.fallDistance = 0;

        slidingDashCollider slidingDash = new slidingDashCollider(player.level(), player, dmg);
        caster.level().addFreshEntity(slidingDash);

    }

    @Override
    protected void playMagicCastSound(Player player, Player player1, int i) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1F, 1F);

    }
}
