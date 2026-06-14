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

    public attackZantetsuken(ResourceLocation registryName, boolean hasToSelect, int tier, String gmAbility) {
        super(registryName, hasToSelect, gmAbility);
setTier(tier);
    }

    private LivingEntity target;
    float dmg;
    int level;

    @Override
    public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
        PlayerData playerData = PlayerData.get(caster);


        double speed = 2;
        double yawRad = Math.toRadians(player.getYRot());
        double dx = -Math.sin(yawRad) * speed;
        double dz = Math.cos(yawRad) * speed;

        caster.hurtMarked = true;
        if (KingdomKeysReMind.efmLoaded){
            /*PlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
            if (playerpatch.isEpicFightMode()) {
                caster.setDeltaMovement(dx * 0.5, 0, dz * 0.5);
                System.out.println(dx + ", " + dz);
            } else {
                caster.setDeltaMovement(dx, 0, dz);
                System.out.println(dx + ", " + dz);
            }*/
            caster.setDeltaMovement(dx * 0.55f, 0, dz * 0.55f);
        } else {
            caster.setDeltaMovement(dx * 1.1f, 0, dz * 1.1f);
        }


        zantetsukenCollider zantetsuken = new zantetsukenCollider(player.level(), player, dmg, level);
        caster.level().addFreshEntity(zantetsuken);

    }

    @Override
    public void playMagicCastSound(LivingEntity player, Player caster) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.ZANTETSUKEN.get(), SoundSource.PLAYERS, 1F, 1F);

    }
}
