package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.attacks.BlitzCollider;

public class attackBlitz extends Magic {

    public attackBlitz(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
        super(registryName, hasToSelect, maxLevel, gmAbility);
    }

    @Override
    public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
        PlayerData playerData = PlayerData.get(caster);

        if (playerData == null) {
            return;
        }

        float dmg = switch (level) {
            case 0 -> playerData.getStrength(true) * 1.1F;
            case 1 -> playerData.getStrength(true) * 1.3F;
            case 2 -> playerData.getStrength(true) * 1.5F;
            default -> playerData.getStrength(true);
        };

        dmg *= fullMPBlastMult;

        launchBlitzDash(caster, 0);

        BlitzCollider blitz = new BlitzCollider(
                player.level(),
                caster,
                dmg,
                0
        );

        caster.level().addFreshEntity(blitz);
    }

    private void launchBlitzDash(Player caster, int chainStep) {
        double speed = switch (chainStep) {
            case 0 -> 1.65D;
            case 1 -> 1.85D;
            case 2 -> 2.05D;
            default -> 1.65D;
        };

        double jump = 0.35D;
        double yawRad = Math.toRadians(caster.getYRot());
        double dx = -Math.sin(yawRad) * speed;
        double dz = Math.cos(yawRad) * speed;

        caster.hurtMarked = true;
        caster.fallDistance = 0.0F;

        if (KingdomKeysReMind.efmLoaded) {
            caster.setDeltaMovement(dx / 2.25D, jump, dz / 2.25D);
        } else {
            caster.setDeltaMovement(dx, jump, dz);
        }
    }

    @Override
    protected void playMagicCastSound(LivingEntity player, Player caster, int level) {
        player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
    }
}