package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.network.PacketHandlerRM;

public class magicAutoLife extends Magic {


    public magicAutoLife(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
        super(registryName, hasToSelect, maxLevel, null);
    }

    @Override
	public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {
        IGlobalCapabilitiesRM globalData = ModCapabilitiesRM.getGlobal(player);
        if (globalData != null) {
            caster.swing(InteractionHand.MAIN_HAND);
            player.addEffect(new MobEffectInstance(ModMobEffectsRM.AUTO_LIFE.get(),Integer.MAX_VALUE, 0,false,false));

            PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        }
    }

	@Override
	protected void playMagicCastSound(Player player, Player caster, int level) {
		player.level().playSound(null, player.blockPosition(), ModSoundsRM.AUTOLIFE.get(), SoundSource.PLAYERS, 1F, 1F);
	}


}
