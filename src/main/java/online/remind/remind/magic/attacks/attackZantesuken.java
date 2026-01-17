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
import online.remind.remind.entity.attacks.zantesukenCollider;

public class attackZantesuken extends Magic {

    public attackZantesuken(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
        super(registryName, hasToSelect, maxLevel, gmAbility);
    }

    private LivingEntity target;
    float dmg;
    int level;

    @Override
    public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
        PlayerData playerData = PlayerData.get(caster);


        double speed = 3;
        double yawRad = Math.toRadians(player.getYRot());
        double dx = -Math.sin(yawRad) * speed;
        double dz = Math.cos(yawRad) * speed;

        caster.hurtMarked = true;
        caster.fallDistance = 0;

            caster.setDeltaMovement(dx, 0, dz);

        zantesukenCollider zantesuken = new zantesukenCollider(player.level(), player, dmg, level);
        caster.level().addFreshEntity(zantesuken);

    }

    @Override
    protected void playMagicCastSound(LivingEntity player, Player player1, int i) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1F, 1F);

    }
}
