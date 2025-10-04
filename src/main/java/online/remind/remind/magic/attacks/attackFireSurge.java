package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.entity.attacks.fireSurgeCollider;

public class attackFireSurge extends Magic {


    public attackFireSurge(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
        super(registryName, hasToSelect, maxLevel, null);
    }

    public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
        PlayerData playerData = PlayerData.get(player);
        float dmg = playerData.getStrength(true) * (playerData.getNumberOfAbilitiesEquipped(Strings.fireBoost) * 0.1f);
        float radius = 1.5f + (0.5f * level);

        double speed = 1.5;

        double yawRad = Math.toRadians(player.getYRot());
        double dx = -Math.sin(yawRad) * speed;
        double jump = 0.175;
        double dz = Math.cos(yawRad) * speed;
        caster.setDeltaMovement(dx, jump, dz);
        caster.hurtMarked = true;
        caster.fallDistance = 0;

        fireSurgeCollider surge = new fireSurgeCollider(caster.level(), caster, dmg);
        caster.level().addFreshEntity(surge);


    }

        @Override
    protected void playMagicCastSound(Player player, Player player1, int i) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1F, 1F);
    }
}
