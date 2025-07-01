package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.magic.DrainEntity;

public class magicDrain extends Magic {

	public magicDrain(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
		super(registryName, hasToSelect, maxLevel, null);
	}

	@Override
	public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float hpTaken = getDamageMult(level);
		hpTaken *= fullMPBlastMult;

		lockOnEntity = getMagicLockOn(level) ? lockOnEntity : null;
		caster.swing(InteractionHand.MAIN_HAND);

		switch (level) {
		case 0:
			ThrowableProjectile drain = new DrainEntity(player.level(), player, hpTaken, lockOnEntity);
			player.level().addFreshEntity(drain);
			drain.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			break;
		case 1:
			ThrowableProjectile drainra = new DrainEntity(player.level(), player, hpTaken, lockOnEntity);
			player.level().addFreshEntity(drainra);
			drainra.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.5F, 0);
			break;
		case 2:
			ThrowableProjectile drainga = new DrainEntity(player.level(), player, hpTaken, lockOnEntity);
			player.level().addFreshEntity(drainga);
			drainga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 3F, 0);
			break;
		}
	}

	@Override
	protected void playMagicCastSound(Player player, Player caster, int level) {
		player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundsRM.DRAIN.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
