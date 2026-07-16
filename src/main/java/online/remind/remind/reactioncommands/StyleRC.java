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

import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;

import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.ability.ModAbilitiesRM;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.styles.data.StyleDefinition;
import online.remind.remind.styles.data.StyleRegistry;

import java.util.List;

public class StyleRC extends ReactionCommand {

	private final String type; // DriveForm ID (e.g. "kkremind:form_firestorm")

	public StyleRC(ResourceLocation registryName, boolean constantCheck, String type) {
		super(registryName, constantCheck, 20 * 20, 0xff6f00);
		this.type = type;
	}

	// ------------------------------------------------------------
	// MAIN RC LOGIC
	// ------------------------------------------------------------
	@Override
	public void onUse(Player player, LivingEntity target, LivingEntity ignored) {

		if (!conditionsToAppear(player, player))
			return;

		PlayerData playerData = PlayerData.get(player);
		GlobalDataRM remindData = ModDataRM.getGlobal(player);

		// ------------------------------------------------------------
		// 1. ACTIVATE STYLE (not in this Style yet)
		// ------------------------------------------------------------
		if (!playerData.getActiveDriveForm().equals(ResourceLocation.parse(type))) {

			DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(type));
			if (form != null) {
				form.initDrive(player);
				//System.out.println("Entered Style. Active Form is now: " + playerData.getActiveDriveForm());
			}

			// Reset SGauge + Style state
			remindData.setSituationValue(0);
			remindData.setStyle("NONE");

			StyleDefinition def = StyleRegistry.getStyleForDriveForm(ResourceLocation.parse(type));
			remindData.setStyleTicks(100 + (10 * playerData.getNumberOfAbilitiesEquipped(ModAbilitiesRM.FORM_BOOST)));
			System.out.println(remindData.getStyleTicks());

			PacketHandlerRM.syncGlobalToAllAround(player, remindData);

			// Remove RC after activation
			playerData.removeReactionCommand(getRegistryName());
			return;
		}

		// ------------------------------------------------------------
		// 2. FINISHER (already in this Style)
		// ------------------------------------------------------------
		useStyleFinisher(player);

		// Exit Style
		playerData.addFP(-1000);
		remindData.setSituationValue(0);
		remindData.setStyle("NONE");
		remindData.setStyleTicks(0);

		PacketHandlerRM.syncGlobalToAllAround(player, remindData);
	}

	// ------------------------------------------------------------
	// FINISHER LOGIC (Unified for all Styles)
	// ------------------------------------------------------------
	private void useStyleFinisher(Player player) {
		PlayerData playerData = PlayerData.get(player);
		float damage = (playerData.getMagic(true) + playerData.getStrength(true)) / 2f;

		switch (type) {

			case KingdomKeysReMind.MODID + ":" + StringsRM.fireStorm -> {
				float mult = playerData.getNumberOfAbilitiesEquipped(ModAbilities.FIRE_BOOST) * 0.25f;
				damage += damage * mult;
				explosionHurt(player, damage, KKDamageTypes.FIRE);
				playSoundAndParticles(player, SoundEvents.BLAZE_SHOOT,
						ParticleTypes.FLAME, ParticleTypes.SMALL_FLAME, ParticleTypes.ASH);
			}

			case KingdomKeysReMind.MODID + ":" + StringsRM.diamondDust -> {
				float mult = playerData.getNumberOfAbilitiesEquipped(ModAbilities.BLIZZARD_BOOST) * 0.25f;
				damage += damage * mult;
				explosionHurt(player, damage, KKDamageTypes.ICE);
				playSoundAndParticles(player, SoundEvents.GLASS_BREAK,
						ParticleTypes.SNOWFLAKE, ParticleTypes.ITEM_SNOWBALL);
			}

			case KingdomKeysReMind.MODID + ":" + StringsRM.thunderBolt -> {
				float mult = playerData.getNumberOfAbilitiesEquipped(ModAbilities.THUNDER_BOOST) * 0.25f;
				damage += damage * mult;
				explosionHurt(player, damage, KKDamageTypes.LIGHTNING);
				playSoundAndParticles(player, SoundEvents.LIGHTNING_BOLT_THUNDER,
						ParticleTypes.ELECTRIC_SPARK, ParticleTypes.END_ROD);
			}

			case KingdomKeysReMind.MODID + ":" + StringsRM.feverPitch -> {
				float mult = playerData.getNumberOfAbilitiesEquipped(ModAbilitiesRM.ATTACK_HASTE) * 0.25f;
				damage += damage * mult;
				explosionHurt(player, damage, KKDamageTypes.OFFHAND);
				playSoundAndParticles(player, SoundEvents.PLAYER_ATTACK_SWEEP,
						ParticleTypes.CRIT);
			}

			case KingdomKeysReMind.MODID + ":" + StringsRM.criticalImpact -> {
				float mult = playerData.getNumberOfAbilitiesEquipped(ModAbilities.CRITICAL_BOOST) * 0.25f;
				damage += damage * mult;
				explosionHurt(player, damage, KKDamageTypes.OFFHAND);
				playSoundAndParticles(player, SoundEvents.PLAYER_ATTACK_SWEEP,
						ParticleTypes.SNOWFLAKE, ParticleTypes.ITEM_SNOWBALL);
			}

			case KingdomKeysReMind.MODID + ":" + StringsRM.spellweaver -> {
				float mult = playerData.getNumberOfAbilitiesEquipped(ModAbilities.BLIZZARD_BOOST) * 0.25f;
				damage += damage * mult;
				explosionHurt(player, damage, KKDamageTypes.STOP);
				playSoundAndParticles(player, SoundEvents.EVOKER_CAST_SPELL,
						ParticleTypes.ENCHANT);
			}

			case KingdomKeysReMind.MODID + ":" + StringsRM.bloodlust -> {
				int darkBoosts = playerData.getNumberOfAbilitiesEquipped(ModAbilitiesRM.DARKNESS_BOOST);

				float mult = darkBoosts * 0.25F;
				float finalDamage = damage + (damage * mult);

				float healAmount = Math.min(finalDamage * 0.25F, 8.0F);
				player.heal(healAmount);

				explosionHurt(player, finalDamage, KKDamageTypes.DARKNESS);

				playSoundAndParticles(
						player,
						SoundEvents.EVOKER_CAST_SPELL,
						ParticleTypes.DAMAGE_INDICATOR
				);

				if (player.level() instanceof ServerLevel serverLevel) {
					serverLevel.sendParticles(
							ParticleTypes.DAMAGE_INDICATOR,
							player.getX(),
							player.getY() + 1.0D,
							player.getZ(),
							30,
							1.0D,
							0.6D,
							1.0D,
							0.08D
					);

					serverLevel.sendParticles(
							ParticleTypes.SOUL,
							player.getX(),
							player.getY() + 1.0D,
							player.getZ(),
							20,
							0.8D,
							0.5D,
							0.8D,
							0.05D
					);

					serverLevel.sendParticles(
							ParticleTypes.WITCH,
							player.getX(),
							player.getY() + 1.0D,
							player.getZ(),
							18,
							0.7D,
							0.5D,
							0.7D,
							0.03D
					);
				}

				player.level().playSound(
						null,
						player.blockPosition(),
						SoundEvents.WITHER_AMBIENT,
						SoundSource.PLAYERS,
						0.8F,
						1.25F
				);
			}
		}
	}

	// ------------------------------------------------------------
	// RC VISIBILITY LOGIC (Activation, Chain-up, Finisher)
	// ------------------------------------------------------------
	@Override
	public boolean conditionsToAppear(Player player, LivingEntity ignored) {

		PlayerData playerData = PlayerData.get(player);
		GlobalDataRM remindData = ModDataRM.getGlobal(player);

		if (playerData == null || remindData == null)
			return false;

		String style = remindData.getStyle();
		double gauge = remindData.getSituationValue();
		String driveId = type;

		/*System.out.println("\n=== StyleRC.conditionsToAppear() ===");
		System.out.println("RC Registry Name: " + getRegistryName());
		System.out.println("RC Type (this.type): " + type);
		System.out.println("Active Form: " + playerData.getActiveDriveForm());
		System.out.println("Gauge: " + gauge);
		System.out.println("Style String: " + style);*/

		// Finisher RC - CHECK THIS FIRST
		boolean isFinisher = playerData.getActiveDriveForm().equals(ResourceLocation.parse(driveId));
		//System.out.println("Finisher Check: activeForm.equals(type) = " + playerData.getActiveDriveForm() + ".equals(" + driveId + ") = " + isFinisher);
		if (isFinisher) {
			boolean result = isFinisher;
			//System.out.println("FINISHER MATCHED! gauge >= 100? " + result);
			return result;
		}

		// Activation RC
		boolean isNone = playerData.isFormActive(ModDriveForms.NONE);
		//System.out.println("Activation Check: activeForm == NONE? " + isNone);
		if (isNone) {
			boolean styleContainsCheck = styleContains(style, driveId);
			boolean result = styleContainsCheck;
			//boolean result = gauge >= 100 && styleContainsCheck;
			//System.out.println("ACTIVATION: gauge >= 100? " + (gauge >= 100) + ", styleContains? " + styleContainsCheck + ", Result: " + result);
			return result;
		}

		// Chain-up RC
		//System.out.println("Checking Chain-up...");
		if (!playerData.isFormActive(ModDriveForms.NONE)) {

			StyleDefinition current = StyleRegistry.getCurrentStyleDefinition(player);
			StyleDefinition target = StyleRegistry.getStyleForDriveForm(ResourceLocation.parse(driveId));

			//System.out.println("Current Style: " + (current != null ? current.target() : "null") + ", Level: " + (current != null ? current.styleLevel() : "N/A"));
			//System.out.println("Target Style: " + (target != null ? target.target() : "null") + ", Level: " + (target != null ? target.styleLevel() : "N/A"));

			if (current != null && target != null) {
				boolean isChainUp = target.styleLevel() == current.styleLevel() + 1;
				//System.out.println("isChainUp: " + target.styleLevel() + " == " + (current.styleLevel() + 1) + " = " + isChainUp);
				if (isChainUp) {
					boolean styleContainsCheck = styleContains(style, driveId);
					//boolean result = styleContainsCheck;
					boolean result = gauge >= 100 && styleContainsCheck;
					//System.out.println("CHAIN-UP: gauge >= 100? " + (gauge >= 100) + ", styleContains? " + styleContainsCheck + ", Result: " + result);
					return result;
				}
			}
		}

		//System.out.println("NO CONDITIONS MET - Returning FALSE");
		return false;
	}

	private boolean styleContains(String styleString, String styleId) {
		if (styleString == null || styleString.isEmpty())
			return false;

		for (String s : styleString.split(",")) {
			if (s.equals(styleId))
				return true;
		}
		return false;
	}

	// ------------------------------------------------------------
	// PARTICLES + SOUND HELPERS
	// ------------------------------------------------------------
	private void playSoundAndParticles(Player player, SoundEvent sound, SimpleParticleType... particles) {
		ServerLevel level = (ServerLevel) player.level();
		double X = player.getX(), Y = player.getY(), Z = player.getZ();

		double radius = 6;
		for (int t = 1; t < 360; t += 20) {
			for (int s = 1; s < 360; s += 20) {
				double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
				double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
				double y = Y + (radius * Math.cos(Math.toRadians(t)));

				for (SimpleParticleType p : particles)
					level.sendParticles(p, x, y, z, 2, 0.05, 0.05, 0.05, 0.01);
			}
		}

		player.level().playSound(null, player.blockPosition(), sound, SoundSource.PLAYERS, 1F, 1F);
	}

	// ------------------------------------------------------------
	// DAMAGE HELPER
	// ------------------------------------------------------------
	public void explosionHurt(Player player, float damage, ResourceKey<DamageType> dmgType) {
		double radius = 6;
		List<LivingEntity> targets = player.level().getEntitiesOfClass(LivingEntity.class,
				player.getBoundingBox().inflate(radius));

		for (LivingEntity target : targets) {
			if (target == player)
				continue;

			Party p = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());
			boolean friendly = p != null && p.getMember(target.getUUID()) != null && !p.getFriendlyFire();

			if (!friendly) {
				target.hurt(KKDamageTypes.getElementalDamage(dmgType, player, player), damage);
				target.invulnerableTime = 0;

				if (dmgType == KKDamageTypes.FIRE)
					target.igniteForTicks(5);
				else if (dmgType == KKDamageTypes.ICE)
					target.addEffect(new MobEffectInstance(ModMobEffects.FREEZE, 80, 0));
			}
		}
	}
}
