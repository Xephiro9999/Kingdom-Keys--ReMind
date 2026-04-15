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
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.styles.data.StyleDefinition;
import online.remind.remind.styles.data.StyleRegistry;

import java.util.List;

public class StyleRC extends ReactionCommand {

	private final String type; // DriveForm ID (e.g. "kkremind:firestorm")

	public StyleRC(ResourceLocation registryName, boolean constantCheck, String type) {
		super(registryName, constantCheck, 30 * 20);
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

		String driveId = type;

		// ------------------------------------------------------------
		// 1. ACTIVATE STYLE (not in this Style yet)
		// ------------------------------------------------------------
		if (!playerData.getActiveDriveForm().equals(driveId)) {

			DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(type));
			if (form != null)
				form.initDrive(player);

			// Reset SGauge + Style state
			remindData.setSituationValue(0);
			remindData.setStyle("NONE");

			StyleDefinition def = StyleRegistry.getStyleForDriveForm(ResourceLocation.parse(type));
			remindData.setStyleTicks(100);

			PacketHandlerRM.syncGlobalToAllAround(player, remindData);

			// Remove RC after activation
			playerData.removeReactionCommand(getRegistryName().toString());
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
				float mult = playerData.getNumberOfAbilitiesEquipped(Strings.fireBoost) * 0.25f;
				damage += damage * mult;
				explosionHurt(player, damage, KKDamageTypes.FIRE);
				playSoundAndParticles(player, SoundEvents.BLAZE_SHOOT,
						ParticleTypes.FLAME, ParticleTypes.SMALL_FLAME, ParticleTypes.ASH);
			}

			case KingdomKeysReMind.MODID + ":" + StringsRM.diamondDust -> {
				float mult = playerData.getNumberOfAbilitiesEquipped(Strings.blizzardBoost) * 0.25f;
				damage += damage * mult;
				explosionHurt(player, damage, KKDamageTypes.ICE);
				playSoundAndParticles(player, SoundEvents.GLASS_BREAK,
						ParticleTypes.SNOWFLAKE, ParticleTypes.ITEM_SNOWBALL);
			}

			case KingdomKeysReMind.MODID + ":" + StringsRM.thunderBolt -> {
				float mult = playerData.getNumberOfAbilitiesEquipped(Strings.thunderBoost) * 0.25f;
				damage += damage * mult;
				explosionHurt(player, damage, KKDamageTypes.LIGHTNING);
				playSoundAndParticles(player, SoundEvents.LIGHTNING_BOLT_THUNDER,
						ParticleTypes.ELECTRIC_SPARK, ParticleTypes.END_ROD);
			}

			case KingdomKeysReMind.MODID + ":" + StringsRM.feverPitch -> {
				float mult = playerData.getNumberOfAbilitiesEquipped(StringsRM.attackHaste) * 0.25f;
				damage += damage * mult;
				playSoundAndParticles(player, SoundEvents.PLAYER_ATTACK_SWEEP,
						ParticleTypes.CRIT);
			}

			case KingdomKeysReMind.MODID + ":" + StringsRM.criticalImpact -> {
				float mult = playerData.getNumberOfAbilitiesEquipped(Strings.criticalBoost) * 0.25f;
				damage += damage * mult;
				playSoundAndParticles(player, SoundEvents.PLAYER_ATTACK_SWEEP,
						ParticleTypes.SNOWFLAKE, ParticleTypes.ITEM_SNOWBALL);
			}

			case KingdomKeysReMind.MODID + ":" + StringsRM.spellweaver -> {
				float mult = playerData.getNumberOfAbilitiesEquipped(Strings.blizzardBoost) * 0.25f;
				damage += damage * mult;
				playSoundAndParticles(player, SoundEvents.EVOKER_CAST_SPELL,
						ParticleTypes.ENCHANT);
			}
		}
	}

	// ------------------------------------------------------------
	// RC VISIBILITY LOGIC (Activation, Chain-up, Finisher)
	// ------------------------------------------------------------
	@Override
	public boolean conditionsToAppear(Player player, LivingEntity ignored) {

		PlayerData playerData = PlayerData.get(player);
		GlobalDataRM global = ModDataRM.getGlobal(player);

		if (playerData == null || global == null)
			return false;

		String styleFlag = global.getStyle();
		double gauge = global.getSituationValue();
		String driveId = type;

		// Activation RC
		if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
			return gauge >= 100 && styleContains(styleFlag, driveId);
		}

		// Chain-up RC
		if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {

			StyleDefinition current = StyleRegistry.getCurrentStyleDefinition(player);
			StyleDefinition target = StyleRegistry.getStyleForDriveForm(ResourceLocation.parse(driveId));

			if (current != null && target != null) {
				if (target.styleLevel() == current.styleLevel() + 1) {
					return gauge >= 100 && styleContains(styleFlag, driveId);
				}
			}
		}

		// Finisher RC
		if (playerData.getActiveDriveForm().equals(driveId)) {
			return gauge >= 100;
		}

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
