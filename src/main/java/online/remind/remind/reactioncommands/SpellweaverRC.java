package online.remind.remind.reactioncommands;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import org.joml.Vector3f;

import java.util.List;

public class SpellweaverRC extends StyleRC {

	public SpellweaverRC(ResourceLocation registryName, boolean constantCheck) {
		super(registryName, constantCheck, 30 * 20);
	}

	@Override
	protected String getDriveFormId() {
		return ModDriveFormsRM.SPELLWEAVER.get().getRegistryName().toString();
	}

	@Override
	protected DriveForm getDriveForm() {
		return ModDriveFormsRM.SPELLWEAVER.get();
	}

	@Override
	protected int getStyleDuration() {
		return 100;
	}

	@Override
	public void performFinisher(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
			PlayerData playerData = PlayerData.get(player);
			IGlobalDataRM  remindData = ModDataRM.getGlobal(player);

			double X = player.getX();
			double Y = player.getY();
			double Z = player.getZ();

				float damage = (float) playerData.getMagic(true) * 0.80f;
				float dmgMult = (float) playerData.getMaxMP() * 0.015f;

				System.out.println("Damage: "+damage+", Multi: " + dmgMult);

				damage += (damage * dmgMult);
				System.out.println("Damage (After Calc.): "+damage);

				Level level = player.level();

				ServerLevel serverLevel = (ServerLevel) player.level();

				double radius = 6;

				List<LivingEntity> targets = level.getEntitiesOfClass(
						LivingEntity.class,
						player.getBoundingBox().inflate(radius)
				);


				for (int t = 1; t < 360; t += 20) {
					for (int s = 1; s < 360 ; s += 20) {
						double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double y = Y + (radius * Math.cos(Math.toRadians(t)));

						serverLevel.sendParticles(ParticleTypes.ENCHANT, x,y,z,2,0.05,0.05,0.05,0.01);

					}
				}

				List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty((player), player, (float) radius, (float) radius, (float) radius);
				for (LivingEntity e : targetList) {
					for (int t = 1; t < 360; t += 20) {
						for (int s = 1; s < 360; s += 20) {
							double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
							double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
							((ServerLevel) player.level()).sendParticles(new DustParticleOptions(new Vector3f(1F,1F,1F),1F),x,player.getY() ,z,1,0,0,0,0);
							e.knockback(2, -e.getX(),-e.getZ());
							e.hurt(e.damageSources().indirectMagic(e, player), damage);
						}
					}
				}
				level.playSound(
						null,
						player.blockPosition(),
						SoundEvents.EVOKER_CAST_SPELL,
						SoundSource.PLAYERS,
						1F,
						1F
				);
	}
}
