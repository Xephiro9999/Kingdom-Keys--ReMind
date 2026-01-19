package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.attacks.zantetsukenCollider;

public class attackZantetsuken extends Magic {

    public attackZantetsuken(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
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
        if (KingdomKeysReMind.efmLoaded){
            caster.setDeltaMovement(dx/1.25, 0, dz/1.25);
        } else {
            caster.setDeltaMovement(dx, 0, dz);
        }


        zantetsukenCollider zantetsuken = new zantetsukenCollider(player.level(), player, dmg, level);
        caster.level().addFreshEntity(zantetsuken);

    }

    @Override
    protected void playMagicCastSound(LivingEntity player, Player player1, int i) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.ZANTETSUKEN.get(), SoundSource.PLAYERS, 1F, 1F);

    }
}
