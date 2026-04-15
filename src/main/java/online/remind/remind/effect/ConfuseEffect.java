package online.remind.remind.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class ConfuseEffect extends MobEffect {
    public ConfuseEffect(MobEffectCategory pCategory, int pColor){
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof Mob mob) {

            if (mob.getRandom().nextInt(20) == 0) {
                mob.setTarget(null);
                mob.setLastHurtByMob(null);
            }

            if (mob.onGround() && mob.getRandom().nextInt(12) == 0) {
                double dx = (mob.getRandom().nextDouble() - 0.5D) * 0.35D;
                double dz = (mob.getRandom().nextDouble() - 0.5D) * 0.35D;
                mob.push(dx, 0, dz);
            }

            int retargetChance = Math.max(6, 14 - pAmplifier * 4); // amp makes it more chaotic
            if (mob.getTarget() == null && mob.getRandom().nextInt(retargetChance) == 0) {

                List<LivingEntity> nearby = mob.level().getEntitiesOfClass(
                        LivingEntity.class,
                        mob.getBoundingBox().inflate(8.0D),
                        e -> e.isAlive() && e != mob && !(e instanceof Player p && p.isCreative())
                );

                if (!nearby.isEmpty()) {
                    LivingEntity pick = nearby.get(mob.getRandom().nextInt(nearby.size()));
                    mob.setTarget(pick);

                }
            }

        }
            return true;
        }


        @Override
        public boolean shouldApplyEffectTickThisTick ( int duration, int amplifier){
            return true;
        }
    }
