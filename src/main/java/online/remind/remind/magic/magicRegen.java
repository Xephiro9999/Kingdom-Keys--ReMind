package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;

public class magicRegen extends Magic {


    public magicRegen(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
        super(registryName, hasToSelect, maxLevel, null);
    }

    @Override
	public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {
        IGlobalDataRM globalData = ModDataRM.getGlobal(player);
        int time = (int) (PlayerData.get(caster).getMaxMP() * ((level + 1) * 2));
        if (globalData != null) {
            caster.swing(InteractionHand.MAIN_HAND);
            player.addEffect(new MobEffectInstance(ModMobEffectsRM.REGEN,time, level,false,false, false));
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.REGEN.get(), SoundSource.PLAYERS, 1F, 1F);
        }
    }

	@Override
	protected void playMagicCastSound(Player player, Player caster, int level) {
		player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.PLAYER_CAST.get(), SoundSource.PLAYERS, 1F, 1F);
	}


}
