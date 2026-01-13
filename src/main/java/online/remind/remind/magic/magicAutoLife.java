package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.network.PacketHandlerRM;

public class magicAutoLife extends Magic {


    public magicAutoLife(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
        super(registryName, hasToSelect, maxLevel, gmAbility);
    }

    @Override
	public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {
        IGlobalDataRM globalData = ModDataRM.getGlobal(player);
        if (globalData != null) {
            caster.swing(InteractionHand.MAIN_HAND);
            player.addEffect(new MobEffectInstance(ModMobEffectsRM.AUTO_LIFE,Integer.MAX_VALUE, 0,false,false));
        }
    }

	@Override
	protected void playMagicCastSound(LivingEntity player, Player caster, int level) {
		player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.AUTOLIFE.get(), SoundSource.PLAYERS, 1F, 1F);
	}


}
