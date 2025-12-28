package online.remind.remind.entity.spirits.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class ChirithyGoal extends Goal {

    private final TamableAnimal spirit;
    private LivingEntity owner;
    private final double speed;
    private final float minDist;
    private final float maxDist;

    public ChirithyGoal(TamableAnimal spirit, double speed, float minDist, float maxDist, boolean pMustSee) {
        this.spirit = spirit;
        this.speed = speed;
        this.minDist = minDist;
        this.maxDist = maxDist;
    }

    @Override
    public boolean canUse() {
        this.owner = spirit.getOwner();
        if (owner == null || !owner.isAlive()) return false;
        return spirit.distanceTo(owner) > minDist;
    }

    @Override
    public boolean canContinueToUse() {
        return owner != null && owner.isAlive() &&
                spirit.distanceTo(owner) > minDist;
    }

    @Override
    public void tick() {
        Vec3 targetPos = owner.position()
                .add(owner.getLookAngle().normalize().scale(-1.5)) // behind owner
                .add(0, 1.2, 0); // slightly above

        spirit.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, speed);

        // Teleport if too far
        if (spirit.distanceTo(owner) > maxDist) {
            spirit.teleportTo(owner.getX(), owner.getY() + 1.5, owner.getZ());
        }
    }


}
