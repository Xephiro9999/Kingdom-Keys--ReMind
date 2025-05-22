package online.remind.remind.handler;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import online.kingdomkeys.kingdomkeys.api.event.AbilityEvent;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.ability.ModAbilitiesRM;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.item.ModItemsRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.Map;

public class EntityEventsRM {

	public int ticks;

	int maxTicks;

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e){
		Player player = e.getEntity();
		PlayerData playerData = PlayerData.get(player);
		IGlobalDataRM globalData = ModDataRM.getGlobal(player);

		if (playerData != null){
			if (!playerData.getAbilityMap().containsKey(StringsRM.renewalBlock)) {
				playerData.addAbility(StringsRM.renewalBlock, true);
			}

			if (!playerData.getAbilityMap().containsKey(StringsRM.focusBlock)) {
				playerData.addAbility(StringsRM.focusBlock, true);
			}

			if (!playerData.getAbilityMap().containsKey(StringsRM.counterHammer)) {
				playerData.addAbility(StringsRM.counterHammer, true);
			}

			if (!playerData.getAbilityMap().containsKey(StringsRM.counterBlast)) {
				playerData.addAbility(StringsRM.counterBlast, true);
			}

			if (!playerData.getAbilityMap().containsKey(StringsRM.counterRush)) {
				playerData.addAbility(StringsRM.counterRush, true);
			}
			// To initialize the toggle feature
			globalData.setPanelsEnabled(1);
			globalData.setNGPEnabled(1);
		}
	}
	
	/**
	 * 
	 * @param player
	 * @param AbilityName StringsX.darkPower
	 * @param formName ModID + StringsX.darkMode
	 */
	private void updateDriveAbilities(Player player, String AbilityName, String formName) {
		PlayerData playerData = PlayerData.get(player);

		if(playerData.isAbilityEquipped(AbilityName)) { //if ability to use dark form is equipped
			if(!playerData.getDriveFormMap().containsKey(formName)) {
				playerData.setDriveFormLevel(formName, 1); //We give the form to the player
			}
		}

		if(playerData.getDriveFormLevel(ModDriveFormsRM.DARK.get().getRegistryName().toString()) == 7 && playerData.getDriveFormLevel(ModDriveFormsRM.LIGHT.get().getRegistryName().toString()) == 7){
			playerData.setDriveFormLevel(ModDriveFormsRM.TWILIGHT.get().getRegistryName().toString(), 7);
		}
	}

	private void updateEquippedAbilities(Player player){
		PlayerData playerData = PlayerData.get(player);

	}

	@SubscribeEvent
	public void equipAbility(AbilityEvent.Equip event){
		PlayerData playerData = PlayerData.get(event.getPlayer());
		IGlobalDataRM playerData2 = ModDataRM.getGlobal(event.getPlayer());
		if (event.getPlayer().getServer() != null) {
			WorldData worldData = WorldData.get(event.getPlayer().getServer());
		}


		playerData2.setMPOG((int) playerData.getMaxMP());
		float mpBoost = (float) playerData.getMagicStat().get();
			if (event.getAbility().equals(ModAbilitiesRM.MP_BOOST.get())) {
				playerData.addMaxMP(12.5);
			}

			if (event.getAbility().equals(ModAbilitiesRM.HP_BOOST.get())) {
				playerData.addMaxHP(15);
				event.getPlayer().setHealth(playerData.getMaxHP());
				event.getPlayer().getAttribute(Attributes.MAX_HEALTH).setBaseValue(playerData.getMaxHP());
			}

			if (event.getAbility().equals(ModAbilitiesRM.FRIEND_POWER.get())){
				if (event.getPlayer().getServer() != null) {
					WorldData worldData = WorldData.get(event.getPlayer().getServer());
					Party party = worldData.getPartyFromMember(event.getPlayer().getUUID());

					if (party != null){
						float friendBoost = party.getMembers().size() - 1;
					}
				}
			}

			if(event.getAbility().equals(ModAbilitiesRM.RENEWAL_BLOCK.get()) && playerData.isAbilityEquipped(StringsRM.focusBlock)){
				playerData.unequipAbility(StringsRM.focusBlock,0);
			}

			if(event.getAbility().equals(ModAbilitiesRM.FOCUS_BLOCK.get()) && playerData.isAbilityEquipped(StringsRM.renewalBlock)){
				playerData.unequipAbility(StringsRM.renewalBlock,0);
			}

			if (event.getAbility().equals(ModAbilitiesRM.COUNTER_HAMMER.get())) {
				if (playerData.isAbilityEquipped(StringsRM.counterBlast) || playerData.isAbilityEquipped(StringsRM.counterRush)) {
					playerData2.setCanCounter(0);
					playerData.unequipAbility(StringsRM.counterBlast, 0);
					playerData.unequipAbility(StringsRM.counterRush, 0);
				}
			}

			if (event.getAbility().equals(ModAbilitiesRM.COUNTER_BLAST.get())){
			if (playerData.isAbilityEquipped(StringsRM.counterHammer) || playerData.isAbilityEquipped(StringsRM.counterRush)) {
				playerData2.setCanCounter(0);
				playerData.unequipAbility(StringsRM.counterHammer, 0);
				playerData.unequipAbility(StringsRM.counterRush, 0);
			}
		}

		if (event.getAbility().equals(ModAbilitiesRM.COUNTER_RUSH.get())){
			if (playerData.isAbilityEquipped(StringsRM.counterHammer) || playerData.isAbilityEquipped(StringsRM.counterBlast)){
				playerData2.setCanCounter(0);
				playerData.unequipAbility(StringsRM.counterHammer,0);
				playerData.unequipAbility(StringsRM.counterBlast,0);
			}
		}




			
	}

	@SubscribeEvent
	public void unequipAbility(AbilityEvent.Unequip event){
		PlayerData playerData = PlayerData.get(event.getPlayer());
		IGlobalDataRM playerData2 = ModDataRM.getGlobal(event.getPlayer());
		if (event.getAbility().equals(ModAbilitiesRM.MP_BOOST.get())) {
			playerData.addMaxMP(-12.5);

		}

		if (event.getAbility().equals(ModAbilitiesRM.HP_BOOST.get())) {
			playerData.addMaxHP(-15);
			event.getPlayer().setHealth(playerData.getMaxHP());
			event.getPlayer().getAttribute(Attributes.MAX_HEALTH).setBaseValue(playerData.getMaxHP());
		}

		if (event.getAbility().equals(ModAbilitiesRM.DEDICATION.get())){
			playerData.getStrengthStat().removeModifier("Dedication");
			playerData.getMagicStat().removeModifier("Dedication");
			playerData.getDefenseStat().removeModifier("Dedication");
		}

		if (event.getAbility().equals(ModAbilitiesRM.FRIEND_POWER.get())){
			playerData.getStrengthStat().removeModifier("Friendship");
			playerData.getMagicStat().removeModifier("Friendship");
			playerData.getDefenseStat().removeModifier("Friendship");
		}

		if (event.getAbility().equals(ModAbilitiesRM.COUNTER_HAMMER.get()) || event.getAbility().equals(ModAbilitiesRM.COUNTER_BLAST.get()) || event.getAbility().equals(ModAbilitiesRM.COUNTER_RUSH.get())){
			playerData2.remCanCounter(1);
			PacketHandlerRM.syncGlobalToAllAround(event.getPlayer(), playerData2);
		}

	}





	@SubscribeEvent
	public void onLivingUpdate(EntityTickEvent.Pre event) {
		if (event.getEntity() instanceof LivingEntity livingEntity) {
			if (!event.getEntity().level().isClientSide()) {
				IGlobalDataRM globalData = ModDataRM.getGlobal(livingEntity);

				if (livingEntity instanceof Player player) {
					PlayerData playerData = PlayerData.get(player);
					WorldData worldData = WorldData.get(player.getServer());

					// Xephiro Keyblade Debuff
					if (playerData != null && playerData.getEquippedKeychain(DriveForm.NONE) != null) {
						if (playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItemsRM.xephiroKeybladeChain.get() && !player.getUUID().equals("70b48fbd-b67f-4f3e-9369-09cef36d51a3")) {
							System.out.println("Sanguine Gaze Equipped by NOT Xephiro!");
							System.out.println(player.getUUID());
							//playerData.getStrengthStat().addModifier("Not Xephiro", -25, false, true);
							//playerData.getMagicStat().addModifier("Not Xephiro", -25, false, true);
							//playerData.getDefenseStat().addModifier("Not Xephiro", -25, false, true);
						} else {
							playerData.getStrengthStat().removeModifier("Not Xephiro");
							playerData.getMagicStat().removeModifier("Not Xephiro");
							playerData.getDefenseStat().removeModifier("Not Xephiro");
						}
					}

					if (playerData != null) {
						if (globalData.getCanCounter() == 1) {
							maxTicks = 200;
							if (ticks <= maxTicks) {
								ticks += 5;
								//System.out.println(ticks);
							} else {
								globalData.setCanCounter(0);
								ticks = 0;
							}
						}
					}


					// Form Shotlock Change Test
					if (playerData != null && playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.darkForm)) {
						//playerData.setEquippedShotlock(KingdomKeysReMind.MODID + ":" + StringsRM.darkDivide);
						//System.out.println(playerData.getEquippedShotlock());
					} else {
						//System.out.println(playerData.getEquippedShotlock());
					}


					// Org Passives
					if (!player.level().isClientSide) {
						if (playerData != null && playerData.getAlignment() != Utils.OrgMember.NONE) {
							//playerData.addAbility(StringsRM.darknessBoost,true);
							playerData.getStrengthStat().addModifier("Organization", 5, false, true);
							playerData.getMagicStat().addModifier("Organization", 5, false, true);
							playerData.getDefenseStat().addModifier("Organization", 5, false, true);
						} else if (playerData != null && playerData.getAlignment() == Utils.OrgMember.NONE) {
							playerData.getStrengthStat().removeModifier("Organization");
							playerData.getMagicStat().removeModifier("Organization");
							playerData.getDefenseStat().removeModifier("Organization");
						}
					}

					if (playerData != null && globalData != null) {
						updateDriveAbilities(player, StringsRM.darkPower, KingdomKeysReMind.MODID + ":" + StringsRM.darkForm);
						updateDriveAbilities(player, StringsRM.rageAwakened, KingdomKeysReMind.MODID + ":" + StringsRM.rageForm);
						updateDriveAbilities(player, StringsRM.wayToLight, KingdomKeysReMind.MODID + ":" + StringsRM.lightForm);

						// Light/Darkness Within

						double boostWithin = (playerData.getStrengthStat().getStat() + playerData.getMagicStat().get()) / 2;

						int darknessWithinBoost = (int) (boostWithin * playerData.getNumberOfAbilitiesEquipped(StringsRM.darknessBoost) * 0.1F);
						int lightWithinBoost = (int) (boostWithin * playerData.getNumberOfAbilitiesEquipped(StringsRM.lightBoost) * 0.1F);

						if (playerData.isAbilityEquipped(StringsRM.lightWithin)) {
							playerData.getStrengthStat().addModifier("light_within", lightWithinBoost, false, false);
							playerData.getMagicStat().addModifier("light_within", lightWithinBoost, false, false);
						} else {
							playerData.getStrengthStat().removeModifier("light_within");
							playerData.getMagicStat().removeModifier("light_within");
						}
						if (playerData.isAbilityEquipped(StringsRM.darknessWithin)) {
							playerData.getStrengthStat().addModifier("darkness_within", darknessWithinBoost, false, false);
							playerData.getMagicStat().addModifier("darkness_within", darknessWithinBoost, false, false);
						} else {
							playerData.getStrengthStat().removeModifier("darkness_within");
							playerData.getMagicStat().removeModifier("darkness_within");
						}

						if (!playerData.getActiveDriveForm().equals(ModDriveFormsRM.RAGE.get().getRegistryName().toString())) {
							playerData.getStrengthStat().removeModifier("Riskcharge");
						}

						// Vehemence

						if (playerData.isAbilityEquipped(StringsRM.vehemence)) {

							int vehemenceSTR = (int) (playerData.getStrengthStat().getStat() * 0.25F);
							int vehemenceDEF = (int) (playerData.getDefenseStat().getStat() * 0.25F);
							int vehemenceMAG = (int) (playerData.getMagicStat().getStat() * 0.25F);

							if (playerData.getChosen() == SoAState.WARRIOR) {
								playerData.getStrengthStat().addModifier("Vehemence", vehemenceSTR, false, false);
								playerData.getMagicStat().addModifier("Vehemence", -(vehemenceSTR / 2), false, false);
								playerData.getDefenseStat().addModifier("Vehemence", -(vehemenceSTR / 2), false, false);
							}
							if (playerData.getChosen() == SoAState.MYSTIC) {
								playerData.getStrengthStat().addModifier("Vehemence", -(vehemenceMAG / 2), false, false);
								playerData.getMagicStat().addModifier("Vehemence", vehemenceMAG, false, false);
								playerData.getDefenseStat().addModifier("Vehemence", -(vehemenceMAG / 2), false, false);
							}
							if (playerData.getChosen() == SoAState.GUARDIAN) {
								playerData.getStrengthStat().addModifier("Vehemence", -(vehemenceDEF / 2), false, false);
								playerData.getDefenseStat().addModifier("Vehemence", vehemenceDEF, false, false);
								playerData.getStrengthStat().addModifier("Vehemence", -(vehemenceDEF / 2), false, false);
							}
						} else if (!playerData.isAbilityEquipped(StringsRM.vehemence)) {
							playerData.getStrengthStat().removeModifier("Vehemence");
							playerData.getMagicStat().removeModifier("Vehemence");
							playerData.getDefenseStat().removeModifier("Vehemence");
						}

						if (playerData.isAbilityEquipped(StringsRM.dedication)) {
							if (playerData.getChosen() == SoAState.WARRIOR) {
								playerData.getStrengthStat().addModifier("Dedication", (double) (playerData.getLevel()) / 2, false, true);
							}
							if (playerData.getChosen() == SoAState.MYSTIC) {
								playerData.getMagicStat().addModifier("Dedication", (double) playerData.getLevel() / 2, false, true);
							}
							if (playerData.getChosen() == SoAState.GUARDIAN) {
								playerData.getDefenseStat().addModifier("Dedication", (double) playerData.getLevel() / 2, false, true);
							}
						} else {
							playerData.getStrengthStat().removeModifier("Dedication");
							playerData.getMagicStat().removeModifier("Dedication");
							playerData.getDefenseStat().removeModifier("Dedication");
						}

						// Hearts Are Power Ability

						if (playerData.isAbilityEquipped(StringsRM.heartsPower) && playerData.getAlignment() != Utils.OrgMember.NONE) {
							float heartsBoost = (playerData.getHearts() * 0.0002f);
							float overBoost = heartsBoost * 0.025f;
							if (heartsBoost >= 50) {
								playerData.getStrengthStat().addModifier("Hearts Are Power", 50 + overBoost, false, false);
								playerData.getMagicStat().addModifier("Hearts Are Power", 50 + overBoost, false, false);
								playerData.getDefenseStat().addModifier("Hearts Are Power", 50 + overBoost, false, false);
							} else {
								playerData.getStrengthStat().addModifier("Hearts Are Power", heartsBoost, false, false);
								playerData.getMagicStat().addModifier("Hearts Are Power", heartsBoost, false, false);
								playerData.getDefenseStat().addModifier("Hearts Are Power", heartsBoost, false, false);
							}
						} else {
							playerData.getStrengthStat().removeModifier("Hearts Are Power");
							playerData.getMagicStat().removeModifier("Hearts Are Power");
							playerData.getDefenseStat().removeModifier("Hearts Are Power");
						}

						if (playerData.getAlignment() == Utils.OrgMember.NONE) {
							playerData.getStrengthStat().removeModifier("Hearts Are Power");
							playerData.getMagicStat().removeModifier("Hearts Are Power");
							playerData.getDefenseStat().removeModifier("Hearts Are Power");
						}

						// My Friends Are My Power
						if (playerData.isAbilityEquipped(StringsRM.friendsPower)) {
							Party party = worldData.getPartyFromMember(player.getUUID());
							if (party != null) {
								int friendBoost = 5 * (party.getMembers().size() - 1);
								playerData.getStrengthStat().addModifier("Friendship", friendBoost, false, true);
								playerData.getMagicStat().addModifier("Friendship", friendBoost, false, true);
								playerData.getDefenseStat().addModifier("Friendship", friendBoost, false, true);
							}
						} else {
							playerData.getStrengthStat().removeModifier("Friendship");
							playerData.getMagicStat().removeModifier("Friendship");
							playerData.getDefenseStat().removeModifier("Friendship");
						}

						// Attack Haste Ability
						if (!player.level().isClientSide && playerData.isAbilityEquipped(StringsRM.attackHaste)) {
							double attackSpeedBonus = 1.25 * playerData.getNumberOfAbilitiesEquipped(StringsRM.attackHaste);
							player.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(4 + attackSpeedBonus);
						} else if (!playerData.isAbilityEquipped(StringsRM.attackHaste)) {
							player.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(4);
						}

						// Tidus Keyblade
						if (!player.level().isClientSide && playerData.isAbilityEquipped(StringsRM.Tidus)) {
							if (player.isUnderWater()) {
								playerData.getStrengthStat().addModifier("Tidus", 5, false, false);
								player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 10, 1));
								player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 10, 0));
							} else if (!player.isUnderWater()) {
								playerData.getStrengthStat().removeModifier("Tidus");
							}
						} else if (!player.level().isClientSide && !playerData.isAbilityEquipped(StringsRM.Tidus)) {
							playerData.getStrengthStat().removeModifier("Tidus");
						}

						// Panel System
						if (!player.level().isClientSide && playerData.getAlignment() != Utils.OrgMember.NONE && globalData.getPanelsEnabled() == 1) {
							playerData.getStrengthStat().addModifier("Panel", globalData.getSTRPanel(), false, false);
							playerData.getMagicStat().addModifier("Panel", globalData.getMAGPanel(), false, false);
							playerData.getDefenseStat().addModifier("Panel", globalData.getDEFPanel(), false, false);
						} else {
							playerData.getStrengthStat().removeModifier("Panel");
							playerData.getMagicStat().removeModifier("Panel");
							playerData.getDefenseStat().removeModifier("Panel");
						}
					}


					// Haste
					if (globalData.getHasteTicks() > 0) {
						globalData.remHasteTicks(1);
						//System.out.println(globalData.getHasteTicks());
						if (globalData.getHasteTicks() <= 0) {
							player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "Haste"), -(0.25 + (0.25 * globalData.getHasteLevel())), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
							player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "Haste"), -(0.25 + (0.25 * globalData.getHasteLevel())), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

						}
					}

					// Berserk
					if (globalData.getBerserkTicks() > 0) {
						globalData.remBerserkTicks(1);
						if (globalData.getBerserkTicks() <= 0) {
							playerData.getStrengthStat().removeModifier("berserk");
							playerData.getDefenseStat().removeModifier("berserk");
							if (!event.getEntity().level().isClientSide) {
								PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player); //Sync KK stat packet
								PacketHandlerRM.syncGlobalToAllAround((Player) event.getEntity(), (IGlobalDataRM) globalData);
							}
						}
					}


					// HP / MP / EXP Walker
					if (playerData != null) {
						if (player.isSprinting()) {
							if (player.tickCount % 40 == 0 && playerData.isAbilityEquipped(StringsRM.hpWalker)) {
								int hpWalkerMult = playerData.getNumberOfAbilitiesEquipped(StringsRM.hpWalker);
								player.heal(hpWalkerMult);
							}
							if (player.tickCount % 50 == 0 && playerData.isAbilityEquipped(StringsRM.mpWalker)) {
								if (!playerData.getRecharge()) {
									int mpWalkerMult = playerData.getNumberOfAbilitiesEquipped(StringsRM.mpWalker);
									playerData.addMP(0.5 * mpWalkerMult);
								}
							}
							if (!player.level().isClientSide && player.tickCount % 20 == 0 && playerData.isAbilityEquipped(StringsRM.expWalker)) {
								playerData.addExperience(player, 5 * playerData.getNumberOfAbilitiesEquipped(StringsRM.expWalker), false, true);
							}
							if (!player.level().isClientSide && player.tickCount % 20 == 0 && playerData.isAbilityEquipped(StringsRM.heartWalker)) {
								playerData.addHearts(5 * playerData.getNumberOfAbilitiesEquipped(StringsRM.heartWalker));
							}
							if (player.tickCount % 50 == 0 && playerData.isAbilityEquipped(StringsRM.focusWalker)) {
								if (!playerData.getRecharge()) {
									int focusWalkerMult = playerData.getNumberOfAbilitiesEquipped(StringsRM.focusWalker);
									playerData.addFocus(0.5 * focusWalkerMult);
								}
							}
						}
					}

				}

				if (globalData != null) {

					// RC Cooldown mechanic

					if (globalData.getRCCooldownTicks() > 0) {
						globalData.setRCCooldownTicks(globalData.getRCCooldownTicks() - 1);
					}

			/*
			if (globalData.getBlockedTicks() > 0){
				globalData.setBlockedTicks(globalData.getBlockedTicks() - 1);
				System.out.println(globalData.getBlockedTicks());
			}
			*/

					// Step Ticks
					if (globalData.getStepTicks() > 0) {
						globalData.remStepTicks(1);
						if (globalData.getStepTicks() <= 0) { // Step has finished, notify all the clients about it
							PacketHandlerRM.syncGlobalToAllAround((Player) event.getEntity(), (IGlobalDataRM) globalData);
							if (event.getEntity() instanceof Player player) {
								PlayerData playerData = PlayerData.get(player);
								if (playerData.isAbilityEquipped(StringsRM.darkStep) || playerData.getActiveDriveForm().equals(ModDriveFormsRM.DARK.get().getRegistryName().toString())) {
									player.level().playSound(null, player.blockPosition(), ModSoundsRM.DARKSTEP2.get(), SoundSource.PLAYERS, 1F, 1F);
								}
								if (playerData.isAbilityEquipped(StringsRM.lightStep) || playerData.getActiveDriveForm().equals(ModDriveFormsRM.LIGHT.get().getRegistryName().toString())) {
									player.level().playSound(null, player.blockPosition(), ModSoundsRM.LIGHTSTEP2.get(), SoundSource.PLAYERS, 1F, 1F);
								}
								player.invulnerableTime = 4;
							}
						}
					}

					// Spells go Down Below

					// Slow
					if (globalData.getSlowTicks() > 0) {
						globalData.remSlowTicks(1);
						if (globalData.getSlowTicks() > 0) {

						}
						if (globalData.getSlowTicks() <= 0) {
							livingEntity.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "Slow"), 0.25 + (0.25 * globalData.getSlowLevel()), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
							livingEntity.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "Slow"), 0.25 + (0.25 * globalData.getSlowLevel()), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
						}
					}
				}
			}
		}
	}


	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event){
		IGlobalDataRM globalData = ModDataRM.getGlobal(event.getEntity());
		if (event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();

			if (1 == globalData.getAutoLifeActive()){
				if (player.getHealth() <= 0){
					globalData.remAutoLifeActive(1);
					PacketHandlerRM.syncGlobalToAllAround((Player) event.getEntity(), (IGlobalDataRM) globalData);
					event.setCanceled(true);
					player.setHealth(10.0F);
					player.invulnerableTime = 10;
					player.removeAllEffects();
					player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 10));
					player.level().playSound(null, player.blockPosition(), ModSoundsRM.AUTOLIFE.get(), SoundSource.PLAYERS, 1F, 1F);
				}
			}


		}
		// Dream Eater Death Event


	}

	@SubscribeEvent
	public void hurtEvent(LivingDamageEvent.Pre event) {
		if (event.getEntity() instanceof Player player) {
			PlayerData playerData = PlayerData.get(player);
			if (playerData == null)
				return;

			double missingHP = player.getHealth() / playerData.getMaxHP();
			//System.out.println(missingHP);

			// Adrenaline
			if (playerData.isAbilityEquipped(StringsRM.adrenaline)) {
				if (player.getHealth() - event.getNewDamage() <= player.getMaxHealth() / 4) {
					playerData.getStrengthStat().addModifier("adrenaline", 5, false, false);
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				}
			}
			// Critical Surge
			if (playerData.isAbilityEquipped(StringsRM.critical_surge)) {
				if (player.getHealth() - event.getNewDamage() <= player.getMaxHealth() / 4) {
					playerData.getMagicStat().addModifier("critical_surge", 5, false, false);
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				}
			}
			if (player.getHealth() + 1 >= player.getMaxHealth() / 4) {
				playerData.getStrengthStat().removeModifier("adrenaline");
				playerData.getMagicStat().removeModifier("critical_surge");
				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
			}

			//Protect Abilities

			// MP Shield
			if (playerData.isAbilityEquipped(StringsRM.mpShield) && playerData.getMP() > 0 && !playerData.getRecharge()) {
				float DMGTaken = event.getNewDamage();


				event.setNewDamage(0);
				playerData.remMP(DMGTaken * 1.5);
				float mpRageModifier = DMGTaken * (0.1f * playerData.getNumberOfAbilitiesEquipped(Strings.mpRage));
				if (playerData.isAbilityEquipped(Strings.mpRage) && playerData.getMP() > 11) {
					playerData.addMP(mpRageModifier);
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				}
				if (playerData.isAbilityEquipped(Strings.damageDrive)) {
					playerData.addDP(DMGTaken * (0.1F * playerData.getNumberOfAbilitiesEquipped(Strings.damageDrive)));
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				}

			}
			if (playerData.isAbilityEquipped(StringsRM.darknessBoost) || playerData.isAbilityEquipped(StringsRM.lightBoost)) {
				float darkBoosts = playerData.getNumberOfAbilitiesEquipped(StringsRM.darknessBoost) * 0.025F;
				float lightBoosts = playerData.getNumberOfAbilitiesEquipped(StringsRM.lightBoost) * 0.025F;
				float damage = event.getOriginalDamage();

					/*
					System.out.println("Dark Bonus Res? "+darkBoosts);
					System.out.println("Light Bonus Res? "+lightBoosts);
					 */
				//System.out.println("Before Negation: "+damage);


				if (event.getSource().getMsgId().equals(KKResistanceType.darkness.toString())) {
					System.out.println("Darkness");
					damage -= (damage * darkBoosts);
					if (playerData.isAbilityEquipped(StringsRM.lightBoost)) {
						damage += (damage * lightBoosts);
					}
				}
				if (!event.getSource().getMsgId().equals(DamageTypes.PLAYER_EXPLOSION.toString())) {
					System.out.println("Explosion");
					damage -= (damage * darkBoosts);
					if (playerData.isAbilityEquipped(StringsRM.lightBoost)) {
						damage += (damage * lightBoosts);
					}
				}

				if (event.getSource().getMsgId().equals(KKResistanceType.light.toString())) {
					System.out.println("Light");
					damage -= (damage * lightBoosts);
					if (playerData.isAbilityEquipped(StringsRM.darknessBoost)) {
						damage += (damage * darkBoosts);
					}
				}
				//System.out.println("After Negation: "+damage);
				event.setNewDamage(damage);
			}

		}


		// On Hit Effects
		if (event.getSource().getEntity() instanceof Player player) {
			PlayerData playerData = PlayerData.get(player);
			if (playerData != null) {
			/*
			double LifeStealAmount = (float) (playerData.getStrengthStat().getStat() * 0.15F);
			System.out.println("Entity Hit: "+event.getEntity());
			System.out.println("Attacker: "+event.getSource().getEntity());
			System.out.println(LifeStealAmount);
			if (playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItemsRM.xephiroKeybladeChain.get() || player.getUUID().toString().equals("70b48fbd-b67f-4f3e-9369-09cef36d51a3")) {
				player.heal((int) LifeStealAmount);
			}
			 */

				int crtBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.criticalBoost);
				float addDmg = (float) (crtBoosts * 3);
				if (playerData.isAbilityEquipped(StringsRM.Jecht)) {
					System.out.println(addDmg);
					event.getEntity().hurt(event.getEntity().damageSources().magic(), addDmg);
					event.getEntity().invulnerableTime = 0;
				}

				// Spellblade Ability
				int fireBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.fireBoost);
				int blizBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.blizzardBoost);
				int thundBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.thunderBoost);
				int waterBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.waterBoost);
				int darkBoosts = playerData.getNumberOfAbilitiesEquipped(StringsRM.darknessBoost);
				int lightBoosts = playerData.getNumberOfAbilitiesEquipped(StringsRM.lightBoost);

				// ((Base STR * 0.25) + (Base MAG * 0.25)) / 2 -- this is to make it so the boosts are more impactful.
				float dmg = (float) ((playerData.getStrengthStat().get() * 0.25f) + (float) (playerData.getMagicStat().get() * 0.25f) / 2F);
//player

				if (event.getSource().type().msgId().equals("player")) {

					if (playerData.isAbilityEquipped(StringsRM.spellblade)) {
						Map<String, Integer> boosts = Map.of(
								"thunder", thundBoosts,
								"fire", fireBoosts,
								"blizzard", blizBoosts,
								"water", waterBoosts,
								"dark", darkBoosts,
								"light", lightBoosts
						);
						// Get how many have that max value
						int maxBoost = boosts.values().stream().max(Integer::compare).orElse(0);
						// Only continue if ONE boost has the highest value AND it’s >= 4
						long count = boosts.values().stream().filter(v -> v == maxBoost).count();
						// Find which one is the winner
						if (count == 1 && maxBoost >= 4) {
							for (Map.Entry<String, Integer> entry : boosts.entrySet()) {
								if (entry.getValue() == maxBoost) {
									String elementBlade = entry.getKey();

									switch (elementBlade) {
										case "fire":
											// Fire Blade
											event.getEntity().invulnerableTime = 0;
											event.getEntity().hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.FIRE, event.getEntity(), null), (float) ((fireBoosts / 2) * dmg));
											event.getEntity().setRemainingFireTicks(2 * fireBoosts);
											event.getEntity().level().addAlwaysVisibleParticle(ParticleTypes.FLAME, event.getEntity().getX() + event.getEntity().level().random.nextDouble() - 0.5D, event.getEntity().getY() + event.getEntity().level().random.nextDouble() * 2D, event.getEntity().getZ() + event.getEntity().level().random.nextDouble() - 0.5D, 0, 0, 0);
											break;

										case "blizzard":
											// Blizzard Blade
											event.getEntity().invulnerableTime = 0;
											event.getEntity().hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.ICE, event.getEntity(), null), (float) ((blizBoosts / 2) * dmg));
											event.getEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, blizBoosts * 20, blizBoosts + 2));
											break;
										case "thunder":
											event.getEntity().invulnerableTime = 0;
											event.getEntity().hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHTNING, event.getEntity(), null), (float) ((thundBoosts / 2) * dmg));
											event.getEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, thundBoosts * 10, thundBoosts));
											event.getEntity().invulnerableTime = 0;
											LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, event.getEntity().level());
											lightningBolt.moveTo(Vec3.atBottomCenterOf(event.getEntity().getOnPos()));
											event.getEntity().level().addFreshEntity(lightningBolt);
											break;
										case "water":
											event.getEntity().invulnerableTime = 0;
											event.getEntity().hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.WATER, event.getEntity(), null), (float) ((waterBoosts / 2) * dmg));
											event.getEntity().setAirSupply(0);
											if (event.getEntity().getAirSupply() == 0) {
												event.getEntity().invulnerableTime = 0;
												event.getEntity().hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.WATER, event.getEntity(), null), (float) ((waterBoosts / 2) * dmg));
											}
											break;
										case "light":
											// Light Blade
											event.getEntity().invulnerableTime = 0;
											event.getEntity().hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHT, event.getEntity(), null), (float) ((lightBoosts / 2) * dmg));
											event.getEntity().addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * lightBoosts, 3));
											break;
										case "dark":
											// Dark Blade
											event.getEntity().invulnerableTime = 0;
											event.getEntity().hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS, event.getEntity(), null), (float) ((darkBoosts / 2) * dmg));
											event.getEntity().addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20 * darkBoosts, 3));
											event.getEntity().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20 * darkBoosts, darkBoosts));
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