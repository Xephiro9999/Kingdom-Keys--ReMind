package online.remind.remind.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class RegenEffect extends MobEffect {
    public RegenEffect(MobEffectCategory pCategory, int pColor){
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier){
        super.applyEffectTick(pLivingEntity,pAmplifier);

    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}