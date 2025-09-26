package online.remind.remind.handler;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.lib.StringsRM;
import org.joml.Vector3f;

import java.util.Map;

public class ClientEventsRM {


	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void RenderEntity(RenderLivingEvent.Pre event) {
		if (event.getEntity() != null) {
			if (event.getEntity() instanceof Player) {
				Player player = (Player) event.getEntity();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				IGlobalCapabilitiesRM globalData = ModCapabilitiesRM.getGlobal(event.getEntity());
				if (playerData != null) {

					// Light and Dark Step VFX
					if (globalData.getStepTicks() > 0) {
						event.setCanceled(true);
						player.invulnerableTime = globalData.getStepTicks();
						if (globalData.getStepType() == StringsRM.orgStepType) {
							if (playerData.getAlignment().equals(Utils.OrgMember.XEMNAS)) {
								player.level().addAlwaysVisibleParticle(ParticleTypes.END_ROD, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F, 0F, 0F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.XIGBAR)) {
								player.level().addAlwaysVisibleParticle(ParticleTypes.ASH, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 3, 3, 3);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.XALDIN)) {
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.9f, 0.9F, 1F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.POOF, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 3, 3, 3);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.VEXEN)) {
								player.level().addAlwaysVisibleParticle(ParticleTypes.SNOWFLAKE, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.ITEM_SNOWBALL, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F, 0F, 0F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.LEXAEUS)) {
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F, 0F, 0F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F, 0.95F, 0F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F, 0.25F, 0.35F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.ZEXION)) {
								player.level().addAlwaysVisibleParticle(ParticleTypes.SOUL_FIRE_FLAME, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.SOUL, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F, 0F, 0F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.SAIX)) {
								player.level().addAlwaysVisibleParticle(ParticleTypes.SOUL_FIRE_FLAME, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F, 0F, 0F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.2F, 0.2F, 1F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.AXEL)) {
								player.level().addAlwaysVisibleParticle(ParticleTypes.FLAME, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.SMALL_FLAME, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F, 0F, 0F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.DEMYX)) {
								player.level().addAlwaysVisibleParticle(ParticleTypes.BUBBLE, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.NOTE, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.DRIPPING_WATER, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.LUXORD)) {
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F, 0F, 0F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.ENCHANT, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.MARLUXIA)) {
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F, 0.4F, 0.5F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F, 0.2F, 0.3F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.LARXENE)) {
								player.level().addAlwaysVisibleParticle(ParticleTypes.ELECTRIC_SPARK, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.CRIT, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F, 0F, 0F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							}
							if (playerData.getAlignment().equals(Utils.OrgMember.ROXAS)) {
								player.level().addAlwaysVisibleParticle(ParticleTypes.ENCHANTED_HIT, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
								player.level().addAlwaysVisibleParticle(ParticleTypes.END_ROD, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);

							}


						} else if (globalData.getStepType() == StringsRM.twilightStepType) {
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F, 1F, 1F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F, 0F, 0F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.5F, 0.5F, 0.5F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
						} else if (globalData.getStepType() == StringsRM.rageStepType) {
							player.level().addParticle(new DustParticleOptions(new Vector3f(0.1F, 0F, 0F), 1F), player.getX() + player.level().random.nextDouble() - 0.55D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.55D, 0, 0, 0);
							player.level().addParticle(new DustParticleOptions(new Vector3f(0.3F, 0F, 0F), 1F), player.getX() + player.level().random.nextDouble() - 0.55D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.55D, 0, 0, 0);
							player.level().addParticle(new DustParticleOptions(new Vector3f(0F, 0F, 0F), 1F), player.getX() + player.level().random.nextDouble() - 0.55D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.55D, 0, 0, 0);
						} else if (globalData.getStepType() == StringsRM.darkStepType && !playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.twilight)) {
							player.level().addAlwaysVisibleParticle(ParticleTypes.SQUID_INK, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.5F, 0F, 0.5F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.5F, 0F, 1F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.2F, 0F, 0F), 1F), player.getX() + player.level().random.nextDouble() - 0.55D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.55D, 0, 0, 0);
						} else if (globalData.getStepType() == StringsRM.lightStepType && !playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.twilight)) {
							player.level().addAlwaysVisibleParticle(ParticleTypes.END_ROD, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(ParticleTypes.CLOUD, player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F, 0.9F, 0.9F), 1F), player.getX() + player.level().random.nextDouble() - 0.5D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F, 1F, 0.7F), 1F), player.getX() + player.level().random.nextDouble() - 0.55D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.55D, 0, 0, 0);
						}
					}

					// Rage Form Active and Walk particles
					if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.RAGE.get().getRegistryName().toString())) {
						player.level().addParticle(new DustParticleOptions(new Vector3f(0.1F, 0F, 0F), 1F), player.getX() + player.level().random.nextDouble() - 0.55D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.55D, 0, 0, 0);

						if (player.onGround()) {
							player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.2F, 0F, 0F), 1F), player.getX(), player.getY(), player.getZ(), 0, 0, 0);

						}
					}

					// Twilight Form Active
					if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.TWILIGHT.get().getRegistryName().toString())) {
						player.level().addParticle(new DustParticleOptions(new Vector3f(0.45F, 0.45F, 0.45F), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
						player.level().addParticle(new DustParticleOptions(new Vector3f(0.55F, 0.55F, 0.55F), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);

					}


					// When I can get particles in other hand
					//if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.DARK.get().getRegistryName().toString())){
					//player.level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.5F,0F,0.5F),1F),player.getX(), player.getY()+ player.level().random.nextDouble() *2D, player.getZ() + player.level().random.nextDouble() - 0.5D, 0, 0, 0);
					//}

					// Spellblade Visual Effects

					int fireBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.fireBoost);
					int blizBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.blizzardBoost);
					int thundBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.thunderBoost);
					int waterBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.waterBoost);
					int darkBoosts = playerData.getNumberOfAbilitiesEquipped(StringsRM.darknessBoost);
					int lightBoosts = playerData.getNumberOfAbilitiesEquipped(StringsRM.lightBoost);

					Vec3 look = player.getLookAngle();
					double x = player.getX() + look.x * 0.4;
					double y = player.getY() + player.getEyeHeight() - 0.2;
					double z = player.getZ() + look.z * 0.4;

					ItemStack mainHand = player.getMainHandItem();
					Vec3 basePos = player.position();
					Vec3 eye = player.getEyePosition(0);
					float yawRad = (float) Math.toRadians(player.getYRot());
					//float pitchRad = (float) Math.toRadians(player.getXRot());


					boolean isRightHanded = player.getMainArm() == HumanoidArm.RIGHT;

					double sideOffset = isRightHanded ? 0.35 : -0.35;
					double forwardOffset = 0.3;
					double heightOffset = 0.9; // Around chest height

					double xOffset = -Math.sin(yawRad) * forwardOffset + Math.cos(yawRad) * sideOffset;
					double zOffset = Math.cos(yawRad) * forwardOffset + Math.sin(yawRad) * sideOffset;


					Vec3 handPos = eye.add(xOffset, heightOffset, zOffset);

					for (int i = 0; i < 5; i++) {
						double x2 = handPos.x() + (player.getRandom().nextDouble() - 0.5) * 0.2;
						double y2 = handPos.y() + (player.getRandom().nextDouble() - 0.5) * 0.2;
						double z2 = handPos.z() + (player.getRandom().nextDouble() - 0.5) * 0.2;


						if (playerData.isAbilityEquipped(StringsRM.spellblade)) {
							Map<String, Integer> boosts = Map.of(
									"thunder", thundBoosts,
									"fire", fireBoosts,
									"blizzard", blizBoosts,
									"water", waterBoosts,
									"dark", darkBoosts,
									"light", lightBoosts
							);
							int maxBoost = boosts.values().stream().max(Integer::compare).orElse(0);

							long count = boosts.values().stream().filter(v -> v == maxBoost).count();

							if (count == 1 && maxBoost >= 4) {
								for (Map.Entry<String, Integer> entry : boosts.entrySet()) {
									if (entry.getValue() == maxBoost) {
										String elementBlade = entry.getKey();

										switch (elementBlade) {
											case "fire":
												// Fire Blade - VFX
												player.level().addParticle(new DustParticleOptions(new Vector3f(0.55F, 0.0f, 0.0F), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
												player.level().addParticle(new DustParticleOptions(new Vector3f(1F, 25.0f, 0.0F), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);

												break;

											case "blizzard":
												// Blizzard Blade - VFX
												player.level().addParticle(new DustParticleOptions(new Vector3f(0.0F, 0.95f, 1F), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
												player.level().addParticle(new DustParticleOptions(new Vector3f(0.95F, 0.95f, 0.95F), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
												break;
											case "thunder":
												// Thunder Blade - VFX
												player.level().addParticle(new DustParticleOptions(new Vector3f(1.0F, 1.00f, 0F), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
												player.level().addParticle(new DustParticleOptions(new Vector3f(1.0F, 1.00f, 0.5F), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
												break;
											case "water":
												// Water Blade - VFX
												player.level().addParticle(new DustParticleOptions(new Vector3f(0.0F, 0.75f, 1.0F), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
												player.level().addParticle(new DustParticleOptions(new Vector3f(0.0F, 1.0f, 1.0F), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
												break;
											case "light":
												// Light Blade - VFX
												player.level().addParticle(new DustParticleOptions(new Vector3f(1.0F, 1.00f, 0.5F), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
												player.level().addParticle(new DustParticleOptions(new Vector3f(1.0F, 1.00f, 1F), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
												break;
											case "dark":
												// Dark Blade - VFX
												player.level().addParticle(new DustParticleOptions(new Vector3f(0.0F, 0.0f, 0f), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
												player.level().addParticle(new DustParticleOptions(new Vector3f(0.5F, 0.0f, 1f), 0.25F), player.getX() + player.level().random.nextDouble() - 0.45D, player.getY() + player.level().random.nextDouble() * 2D, player.getZ() + player.level().random.nextDouble() - 0.45D, -1, -1, -1);
												break;
										}
									}
								}

							}
						}
					}
				}
			}
		}
	}
}
