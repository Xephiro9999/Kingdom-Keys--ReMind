package online.remind.remind.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class SilenceEffect extends MobEffect {
    public SilenceEffect(MobEffectCategory pCategory, int pColor){
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier){
        if (pLivingEntity instanceof Player player) {
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

            playerData.setMagicCooldownTicks(20);
            playerData.setLimitCooldownTicks(20);
        }


        super.applyEffectTick(pLivingEntity,pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}