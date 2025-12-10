package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.entity.attacks.blizzardSurgeCollider;

public class attackBlizzardSurge extends Magic {


    public attackBlizzardSurge(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
        super(registryName, hasToSelect, maxLevel, null);
    }

    public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
        PlayerData playerData = PlayerData.get(player);
        float dmg = 0;

        switch(level){
            case 0:
                dmg = playerData.getStrength(true) * (playerData.getNumberOfAbilitiesEquipped(Strings.blizzardBoost) * 0.1f);
                break;
            case 1:
                dmg = (playerData.getStrength(true) * 1.1f) * (playerData.getNumberOfAbilitiesEquipped(Strings.blizzardBoost) * 0.1f);
                break;
            case 2:
                dmg = (playerData.getStrength(true) * 1.25f) * (playerData.getNumberOfAbilitiesEquipped(Strings.blizzardBoost) * 0.1f);
                break;
        }

        float radius = 1.5f + (0.5f * level);

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

        blizzardSurgeCollider surge = new blizzardSurgeCollider(caster.level(), caster, dmg);
        caster.level().addFreshEntity(surge);
    }

        @Override
    protected void playMagicCastSound(Player player, Player player1, int i) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1F, 1F);
    }
}
