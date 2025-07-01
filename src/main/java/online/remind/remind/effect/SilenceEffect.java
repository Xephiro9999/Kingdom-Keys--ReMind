package online.remind.remind.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;

public class SilenceEffect extends MobEffect {
    public SilenceEffect(MobEffectCategory pCategory, int pColor){
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier){
        if (pLivingEntity instanceof Player player) {
            PlayerData playerData = PlayerData.get(player);

            playerData.setMagicCooldownTicks(20);
            playerData.setLimitCooldownTicks(20);
        }


        super.applyEffectTick(pLivingEntity,pAmplifier);

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
