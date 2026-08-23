package online.remind.remind.dreameater;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import online.remind.remind.entity.ModEntitiesRM;
import online.remind.remind.entity.effects.DreamEaterLevelUpEffectEntity;


public final class DreamEaterLevelUpSwirlHandler {

    private DreamEaterLevelUpSwirlHandler() {
    }


    public static void start(Entity target) {

        if (target == null) {
            return;
        }

        if (!(target.level() instanceof ServerLevel level)) {
            return;
        }


        DreamEaterLevelUpEffectEntity effect =
                new DreamEaterLevelUpEffectEntity(
                        ModEntitiesRM.TYPE_DREAM_EATER_LEVEL_UP_EFFECT.get(),
                        level,
                        target
                );


        level.addFreshEntity(
                effect
        );
    }
}