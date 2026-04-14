package online.remind.remind.reactioncommands;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.List;

public class StyleRC extends ReactionCommand {

	String type;

	public StyleRC(ResourceLocation registryName, boolean constantCheck, String type) {
		super(registryName, constantCheck, 30 * 20);
		this.type = type;
	}

	@Override
	public void onUse(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
		PlayerData playerData = PlayerData.get(player);
		GlobalDataRM remindData = ModDataRM.getGlobal(player);

		if (!playerData.getActiveDriveForm().equals(type)) { //Enter the drive form
			DriveForm firestorm = ModDriveForms.registry.get(ResourceLocation.parse(type));
			firestorm.initDrive(player);
			playerData.removeReactionCommand(getRegistryName().toString());
			remindData.setSituationValue(0);
			remindData.setStyle("");
			remindData.clearSituationSpells();
			remindData.setStyleTicks(100);
			PacketHandlerRM.syncGlobalToAllAround(player, remindData);
		} else {
			// Finisher Attack Code Below
			useStyleFinisher(player); //Explosion

			// Leave Form
			playerData.addFP(-1000);
			remindData.setStyle("NONE");
			remindData.setSituationValue(0);
			remindData.clearSituationSpells();
			playerData.removeReactionCommand(getRegistryName().toString());
			PacketHandlerRM.syncGlobalToAllAround(player, remindData);
		}
	}

	/**
	 * This method defines the different variables based on the damage type
	 * @param player
	 */
	private void useStyleFinisher(Player player) {
		PlayerData playerData = PlayerData.get(player);
		float damage = (float) (playerData.getMagic(true) + playerData.getStrength(true)) /2; // AVG of STR + MAG

		switch (type) {
			case KingdomKeysReMind.MODID+":"+StringsRM.fireStorm -> {
				float dmgMult = playerData.getNumberOfAbilitiesEquipped(Strings.fireBoost) * 0.25f;
				damage += (damage * dmgMult);

				explosionHurt(player, damage, KKDamageTypes.FIRE);
				playSoundAndParticles(player, SoundEvents.BLAZE_SHOOT, ParticleTypes.FLAME, ParticleTypes.SMALL_FLAME, ParticleTypes.ASH);
			}
			case KingdomKeysReMind.MODID+":"+StringsRM.diamondDust -> {
				float dmgMult = playerData.getNumberOfAbilitiesEquipped(Strings.blizzardBoost) * 0.25f;
				damage += (damage * dmgMult);

				explosionHurt(player, damage, KKDamageTypes.ICE);
				playSoundAndParticles(player, SoundEvents.GLASS_BREAK, ParticleTypes.SNOWFLAKE, ParticleTypes.ITEM_SNOWBALL);
			}
			case KingdomKeysReMind.MODID+":"+StringsRM.thunderBolt -> {
				float dmgMult = playerData.getNumberOfAbilitiesEquipped(Strings.thunderBoost) * 0.25f;
				damage += (damage * dmgMult);

				explosionHurt(player, damage, KKDamageTypes.LIGHTNING);
				playSoundAndParticles(player, SoundEvents.LIGHTNING_BOLT_THUNDER, ParticleTypes.ELECTRIC_SPARK, ParticleTypes.END_ROD);

			}
			case KingdomKeysReMind.MODID+":"+StringsRM.feverPitch -> {
				float dmgMult = playerData.getNumberOfAbilitiesEquipped(StringsRM.attackHaste) * 0.25f;
				damage += (damage * dmgMult);

				//explosionHurt(player, damage, KKDamageTypes.ICE);
				playSoundAndParticles(player, SoundEvents.PLAYER_ATTACK_SWEEP, ParticleTypes.CRIT);

			}
			case KingdomKeysReMind.MODID+":"+StringsRM.criticalImpact -> {
				float dmgMult = playerData.getNumberOfAbilitiesEquipped(Strings.criticalBoost) * 0.25f;
				damage += (damage * dmgMult);

				//explosionHurt(player, damage, KKDamageTypes.ICE);
				playSoundAndParticles(player, SoundEvents.PLAYER_ATTACK_SWEEP, ParticleTypes.SNOWFLAKE, ParticleTypes.ITEM_SNOWBALL);

			}
			case KingdomKeysReMind.MODID+":"+StringsRM.spellweaver -> {
				float dmgMult = playerData.getNumberOfAbilitiesEquipped(Strings.blizzardBoost) * 0.25f;
				damage += (damage * dmgMult);

				//explosionHurt(player, damage, KKDamageTypes.ICE);
				playSoundAndParticles(player, SoundEvents.EVOKER_CAST_SPELL, ParticleTypes.ENCHANT);
			}
		}
	}

	/**
	 * Based on the sound and particles sent
	 * @param player
	 * @param sound
	 * @param particleTypes
	 */
	private void playSoundAndParticles(Player player, SoundEvent sound, SimpleParticleType... particleTypes) {
		ServerLevel serverLevel = (ServerLevel) player.level();
		double X = player.getX();
		double Y = player.getY();
		double Z = player.getZ();

		double radius = 6;
		for (int t = 1; t < 360; t += 20) {
			for (int s = 1; s < 360 ; s += 20) {
				double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
				double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
				double y = Y + (radius * Math.cos(Math.toRadians(t)));

                for (SimpleParticleType particleType : particleTypes) {
                    serverLevel.sendParticles(particleType, x, y, z, 2, 0.05, 0.05, 0.05, 0.01);
                }

			}
		}

		player.level().playSound(null, player.blockPosition(), sound, SoundSource.PLAYERS, 1F, 1F);
	}

	public void explosionHurt(Player player, float damage, ResourceKey<DamageType> dmgType){
		double radius = 6;
		List<LivingEntity> targets = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(radius));
		for (LivingEntity target : targets){
			if (target != player){
				Party p = null;
				if (player != null) {
					p = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());
				}

				if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) {
					//getOwner().sendSystemMessage(Component.literal("Entity: " + target));
					target.hurt(KKDamageTypes.getElementalDamage(dmgType, player, player), damage);
					target.invulnerableTime = 0;
					if(dmgType == KKDamageTypes.FIRE)
						target.igniteForTicks(5);
					else if(dmgType == KKDamageTypes.ICE){
						target.addEffect(new MobEffectInstance(ModMobEffects.FREEZE, 80,0));
						target.isFreezing();
					}
				}
			}
		}
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
		return true;
	}
}
