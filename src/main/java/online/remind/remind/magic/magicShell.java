package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;

public class magicShell extends Magic {


    public magicShell(ResourceLocation registryName, boolean hasToSelect, int tier, ResourceLocation gmAbility) {
        super(registryName, hasToSelect, gmAbility);
setTier(tier);
    }

    @Override
	public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnTarget) {
        GlobalDataRM globalData = ModDataRM.getGlobal(player);
        int time = (int) (((getTier() + 1) * 10.0D + casterMagicStat(caster) * 0.1D) * 20);
        if (globalData != null) {
            caster.swing(InteractionHand.MAIN_HAND);
            player.addEffect(new MobEffectInstance(ModMobEffectsRM.SHELL,time, getTier(),false,false, false));
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.SHELL_CAST.get(), SoundSource.PLAYERS, 1F, 1F);
        }
    }

	@Override
	public void playMagicCastSound(LivingEntity player, LivingEntity caster) {
		player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.PLAYER_CAST.get(), SoundSource.PLAYERS, 1F, 1F);
	}


}
