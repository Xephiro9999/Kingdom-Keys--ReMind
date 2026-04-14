package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.attacks.swiftStrikeCollider;

public class attackSwiftStrike extends Magic {

    public attackSwiftStrike(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
        super(registryName, hasToSelect, maxLevel, gmAbility);
    }

    private LivingEntity target;
    float dmg;
    int level;

    @Override
    public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
        PlayerData playerData = PlayerData.get(caster);

        if (lockOnEntity != null){
            lockOnEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0));
        }



        double speed = 3.25;
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
            caster.setDeltaMovement(dx * 0.6f, 0, dz * 0.6f);
        } else {
            caster.setDeltaMovement(dx * 0.75f, 0, dz * 0.75f);
        }


        swiftStrikeCollider swiftStrike = new swiftStrikeCollider(player.level(), player, dmg, level);
        caster.level().addFreshEntity(swiftStrike);

    }

    @Override
    protected void playMagicCastSound(LivingEntity player, Player player1, int i) {
        double rand = Math.floor(Math.random() * 100);
        if (rand >= 50) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.SWIFT_STRIKE.get(), SoundSource.PLAYERS, 1F, 1F);
        } else {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.SWIFT_STRIKE_EN.get(), SoundSource.PLAYERS, 1F, 1F);
        }

    }
}
