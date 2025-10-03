package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;

public class attackQuickBlitz extends Magic {

    public attackQuickBlitz(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
        super(registryName, hasToSelect, maxLevel, null);
    }

    private LivingEntity target;
    private boolean hasLandedAttack = false;
    private double speed = 1.2;
    private double hitRange = 1.5;

    @Override
    public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
        PlayerData playerData = PlayerData.get(player);
        float dmg = playerData.getStrength(true) * 0.75f;
        if (lockOnEntity != null){
            this.target = lockOnEntity;
            this.hasLandedAttack = false;

            // Vector @ Target
            double dx = lockOnEntity.getX() - caster.getX();
            double dy = (lockOnEntity.getY() + lockOnEntity.getBbHeight() * 0.5) - caster.getY();
            double dz = lockOnEntity.getZ() - caster.getZ();

            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

            if (dist < 0.0001){
                return;
            }

            double speed = 1.2;
            //double mx = dx / dist * 0.7; //speed to target
            //double mz = dz / dist * 0.7;
            dx /= dist;
            dy /= dist;
            dz /= dist;

            double jump = 0.6; // leap

            caster.setDeltaMovement(
                    dx * speed,
                    dy * speed + jump,
                    dz * speed);
            caster.hurtMarked = true;
            caster.fallDistance = 0;


                caster.getServer().execute(() -> {
                    if (lockOnEntity.isAlive()) {
                        lockOnEntity.hurt(caster.damageSources().playerAttack(caster), dmg);
                        caster.swing(InteractionHand.MAIN_HAND);
                        // Optional knockback
                        //lockOnEntity.push(mx * 0.5, 0.1, mz * 0.5);
                    }
                });
        }
    }

    @Override
    protected void playMagicCastSound(Player player, Player player1, int i) {

    }
}
