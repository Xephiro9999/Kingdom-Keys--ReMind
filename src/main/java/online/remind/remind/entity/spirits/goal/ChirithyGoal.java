package online.remind.remind.entity.spirits.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

public class ChirithyGoal extends TargetGoal{


    public ChirithyGoal(Mob pMob, boolean pMustSee) {
        super(pMob, pMustSee);
    }









    @Override
    public boolean canUse() {
        return false;
    }


}
