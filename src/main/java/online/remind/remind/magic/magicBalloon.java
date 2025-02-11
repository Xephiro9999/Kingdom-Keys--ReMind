package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.magic.BalloonEntity;
import online.remind.remind.entity.magic.BalloongaEntity;

public class magicBalloon extends Magic {

	public magicBalloon(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
		super(registryName, hasToSelect, maxLevel, null);
	}

	@Override
	public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {
		float dmgMult = getDamageMult(level) + PlayerData.get(player).getNumberOfAbilitiesEquipped(Strings.waterBoost) * 0.2F;
		caster.swing(InteractionHand.MAIN_HAND);

		// Levels
		switch (level) {
		case 0:
			for (int i = -45; i <= 45; i += 45) {
				ThrowableProjectile balloon = new BalloonEntity(player.level(), player, dmgMult);
				player.level().addFreshEntity(balloon);
				balloon.shootFromRotation(player, player.getXRot(), player.getYRot() + i, 0, 0.5F, 0);
			}
			break;
		case 1:
			for (int i = -90; i <= 90; i += 45) {
				ThrowableProjectile balloonra = new BalloonEntity(player.level(), player, dmgMult);
				player.level().addFreshEntity(balloonra);
				balloonra.shootFromRotation(player, player.getXRot(), player.getYRot() + i, 0, 0.5F, 0);
			}
			break;
		case 2:
			ThrowableProjectile balloonga = new BalloongaEntity(player.level(), player, dmgMult);
			player.level().addFreshEntity(balloonga);
			balloonga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 0.5F, 0);
			break;
		}
	}

	@Override
	protected void playMagicCastSound(Player player, Player caster, int level) {
		player.level().playSound(null, player.blockPosition(), ModSoundsRM.BALLOON.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
