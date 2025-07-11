package online.remind.remind.effect;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;


public class BerserkEffect extends MobEffect {
    public BerserkEffect(MobEffectCategory pCategory, int pColor){
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof Player player){


        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }



    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}