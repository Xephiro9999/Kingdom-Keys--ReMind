package online.remind.remind.reactioncommands;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.List;

public class ThunderBoltRC extends StyleRC {

	public ThunderBoltRC(ResourceLocation registryName, boolean constantCheck) {
		super(registryName, constantCheck, 30 * 20);
	}

	@Override
	protected String getDriveFormId() {
		return ModDriveFormsRM.THUNDER_BOLT.get().getRegistryName().toString();
	}

	@Override
	protected DriveForm getDriveForm() {
		return ModDriveFormsRM.THUNDER_BOLT.get();
	}

	@Override
	protected int getStyleDuration() {
		return 100;
	}

	@Override
	protected void performFinisher(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
			PlayerData playerData = PlayerData.get(player);
			IGlobalDataRM  remindData = ModDataRM.getGlobal(player);

			double X = player.getX();
			double Y = player.getY();
			double Z = player.getZ();

				float damage = (float) (playerData.getMagic(true) + playerData.getStrength(true)) /2; // AVG of STR + MAG
				float dmgMult = playerData.getNumberOfAbilitiesEquipped(Strings.thunderBoost) * 0.25f;
				damage += (damage * dmgMult);

				Level level = player.level();

				ServerLevel serverLevel = (ServerLevel) player.level();

				double radius = 6;

				List<LivingEntity> targets = level.getEntitiesOfClass(
						LivingEntity.class,
						player.getBoundingBox().inflate(radius)
				);

				for (LivingEntity target : targets){
					if (target != player){
						Party p = null;
						if (player != null) {
							p = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());
						}

						if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) {
							//getOwner().sendSystemMessage(Component.literal("Entity: " + target));
							target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHTNING, player, player), damage);
							target.invulnerableTime = 0;
						}
					}
				}

				for (int t = 1; t < 360; t += 20) {
					for (int s = 1; s < 360 ; s += 20) {
						double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double y = Y + (radius * Math.cos(Math.toRadians(t)));

						serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, x,y,z,2,0.05,0.05,0.05,0.01);
						serverLevel.sendParticles(ParticleTypes.END_ROD, x,y,z,4,0.05,0.05,0.05,0.01);

					}
				}

				level.playSound(
						null,
						player.blockPosition(),
						SoundEvents.LIGHTNING_BOLT_THUNDER,
						SoundSource.PLAYERS,
						1F,
						1F
				);
	}
}
