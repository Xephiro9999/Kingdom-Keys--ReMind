package online.remind.remind.handler;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.event.AbilityEvent;
import online.kingdomkeys.kingdomkeys.api.event.EquipmentEvent;
import online.kingdomkeys.kingdomkeys.api.event.MagicSpellCastEvent;
import online.kingdomkeys.kingdomkeys.api.event.ReactionCommandCastEvent;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.MagicSpellItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.ability.ModAbilitiesRM;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.item.ModItemsRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.*;

public class EntityEventsRM {

	public int ticks;

	public static Map<UUID, Item> ALLOWED_UUIDS = new HashMap<>();


	@SubscribeEvent
	public void onPlayerChangeMagic(EquipmentEvent.Magic e) {
		PlayerData playerData = PlayerData.get(e.getPlayer());
		GlobalDataRM globalData = ModDataRM.getGlobal(e.getPlayer());

		if (e.getNewStack() != null && e.getNewStack().getItem() instanceof MagicSpellItem newSpell) { //If it's a valid spell (people love to play with NBT editing)
			ItemStack oldSpell = playerData.getEquippedMagics().get(newSpell.getMagic());

			if (oldSpell != null && oldSpell.getItem() instanceof MagicSpellItem foundSpell) { //If it already exists, update its value
				if (foundSpell.getLevel() < newSpell.getLevel()) { //Update only if the new spell is higher
					globalData.setLearnedMagicLevel(ResourceLocation.parse(newSpell.getMagic()), newSpell.getLevel());
					PacketHandlerRM.syncGlobalToAllAround(e.getPlayer(), globalData);
				}
			} else { //If doesn't exist add it either way
				globalData.setLearnedMagicLevel(ResourceLocation.parse(newSpell.getMagic()), newSpell.getLevel());
				PacketHandlerRM.syncGlobalToAllAround(e.getPlayer(), globalData);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent e) {
		Player player = e.getEntity();
		GlobalDataRM globalData = ModDataRM.getGlobal(player);

		if (globalData != null) {
			globalData.setHasDreamEaterSummoned(false);
			globalData.setDreamEaterUUID(null);
			PacketHandlerRM.syncGlobalToAllAround(e.getEntity(), globalData);
		}
	}

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {
		Player player = e.getEntity();
		PlayerData playerData = PlayerData.get(player);
		GlobalDataRM globalData = ModDataRM.getGlobal(player);

		if (globalData.getDreamEaterRL() == null || globalData.getDreamEaterRL().isEmpty()) { //One time event here for remind
			globalData.setDreamEaterRL(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.none).toString());
		}

		if (!player.level().isClientSide) {
			PacketHandlerRM.syncGlobalToAllAround(player, globalData);
		}

		if (playerData != null) {
			if (KingdomKeysReMind.efmLoaded) {
				if (!playerData.getAbilityMap().containsKey(StringsRM.renewalBlock)) {
					playerData.addAbility(StringsRM.renewalBlock, true);
				}

				if (!playerData.getAbilityMap().containsKey(StringsRM.focusBlock)) {
					playerData.addAbility(StringsRM.focusBlock, true);
				}

				if (!playerData.getAbilityMap().containsKey(StringsRM.royalGuard)) {
					playerData.addAbility(StringsRM.royalGuard, true);
				}

				if (!playerData.getAbilityMap().containsKey(StringsRM.stopBlock)) {
					playerData.addAbility(StringsRM.stopBlock, true);
				}

				if (!playerData.getAbilityMap().containsKey(StringsRM.poisonBlock)) {
					playerData.addAbility(StringsRM.poisonBlock, true);
				}

				if (!playerData.getAbilityMap().containsKey(StringsRM.blockReplenisher)) {
					playerData.addAbility(StringsRM.blockReplenisher, true);
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
			}

			// To initialize the toggle feature
			if (playerData != null && playerData.getAlignment() == Utils.OrgMember.NONE) {
				globalData.setPanelsEnabled(0);
			} else if (globalData.getPanelsEnabled() == 1) {
				// Fail Safe -- Login
				if (globalData.getSTRPanel() > ModConfigs.panelLimit) {
					playerData.getStrengthStat().addModifier("Panel", ModConfigs.panelLimit, false, false);
					globalData.setSTRPanel(ModConfigs.panelLimit);
				}
				if (globalData.getMAGPanel() > ModConfigs.panelLimit) {
					playerData.getMagicStat().addModifier("Panel", ModConfigs.panelLimit, false, false);
					globalData.setMAGPanel(ModConfigs.panelLimit);
				}
				if (globalData.getDEFPanel() > ModConfigs.panelLimit) {
					playerData.getDefenseStat().addModifier("Panel", ModConfigs.panelLimit, false, false);
					globalData.setDEFPanel(ModConfigs.panelLimit);
				}
			}


			globalData.setNGPEnabled(1);

			if (globalData.getNGPEnabled() == 1) {
				playerData.getStrengthStat().addModifier("NG+ Bonus", Math.min(globalData.getSTRBonus(), ModConfigs.statCap), true, false);
				playerData.getMagicStat().addModifier("NG+ Bonus", Math.min(globalData.getMAGBonus(), ModConfigs.statCap), true, false);
				playerData.getDefenseStat().addModifier("NG+ Bonus", Math.min(globalData.getDEFBonus(), ModConfigs.statCap), true, false);
			}

			// If player was inflicted with slow/haste before logging out

			if (globalData.getHasteTicks() > 0) {
				player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "Haste"), (0.15 + (0.15 * globalData.getHasteLevel())), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
				player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "Haste"), (0.15 + (0.15 * globalData.getHasteLevel())), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

			}
			if (globalData.getSlowTicks() > 0) {
				player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "Slow"), -(0.15 + (0.15 * globalData.getSlowLevel())), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
				player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "Slow"), -(0.15 + (0.15 * globalData.getSlowLevel())), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
			}

			// TODO: Add other Donor Keyblades to this, but refine the system to make sure no duping bs happens.

			if (ModConfigs.donorKeybladeGrant) {
				MinecraftServer server = player.getServer();
				if (server != null && server.getPlayerList().isOp(player.getGameProfile())) {
					// Player is OP
					player.sendSystemMessage(Component.literal("[Re:Mind] Hey! Letting you know that the config for Re:Mind Donators getting their Keyblades is set to true! If you do not wish for this to be active, please go set the config to 'false'."));
				}

				if (!globalData.getDonorGiven()) {
					if (ALLOWED_UUIDS.containsKey(player.getUUID())) {
						//System.out.println(player.getName().getString() + " is on the list of Donators and has not yet received their Keyblade.");
						UUID uuid = player.getUUID();
						player.sendSystemMessage(Component.literal("[Re:Mind] Hello " + player.getDisplayName().getString() + " here's your Keyblade!"));
						ItemStack item = new ItemStack(ALLOWED_UUIDS.get(uuid));
						player.addItem(item);
						globalData.setDonorGiven(true);

						PacketHandlerRM.syncGlobalToAllAround(e.getEntity(), globalData);
					}
				}
			} else {
				player.sendSystemMessage(Component.literal("[Re:Mind] The Server has the config disabled for you to recieve your Keyblade, please contact them if you wish to have it changed."));
			}
		}
	}


	/**
	 * @param player
	 * @param AbilityName StringsRM.darkPower
	 * @param formName    ModID + StringsRM.darkForm
	 */
	private void updateDriveAbilities(Player player, String AbilityName, String formName) {
		PlayerData playerData = PlayerData.get(player);

		if (playerData.isAbilityEquipped(AbilityName)) { //if ability to use x form is equipped
			if (!playerData.getDriveFormMap().containsKey(formName)) {
				playerData.setDriveFormLevel(formName, 1); //We give the form to the player
			}
		}

		if (playerData.getDriveFormLevel(ModDriveFormsRM.DARK.get().getRegistryName().toString()) == 7 && playerData.getDriveFormLevel(ModDriveFormsRM.LIGHT.get().getRegistryName().toString()) == 7) {
			if (playerData.getDriveFormLevel(ModDriveFormsRM.TWILIGHT.get().getRegistryName().toString()) == 0) {
				playerData.setDriveFormLevel(ModDriveFormsRM.TWILIGHT.get().getRegistryName().toString(), 1);
			}
		}

		// Formchanges
		playerData.setDriveFormLevel(ModDriveFormsRM.FIRESTORM.get().getRegistryName().toString(), 1);
		playerData.setDriveFormLevel(ModDriveFormsRM.DIAMOND_DUST.get().getRegistryName().toString(), 1);
		playerData.setDriveFormLevel(ModDriveFormsRM.THUNDER_BOLT.get().getRegistryName().toString(), 1);
		playerData.setDriveFormLevel(ModDriveFormsRM.FEVER_PITCH.get().getRegistryName().toString(), 1);
		playerData.setDriveFormLevel(ModDriveFormsRM.CRITICAL_IMPACT.get().getRegistryName().toString(), 1);
		playerData.setDriveFormLevel(ModDriveFormsRM.SPELLWEAVER.get().getRegistryName().toString(), 1);

	}

	@SubscribeEvent
	public void equipAbility(AbilityEvent.Equip event) {
		PlayerData playerData = PlayerData.get(event.getPlayer());
		GlobalDataRM remindData = ModDataRM.getGlobal(event.getPlayer());
		WorldData worldData = WorldData.get(event.getPlayer().getServer());
		Player player = event.getPlayer();

		if (event.getAbility().equals(ModAbilitiesRM.FRIEND_POWER.get())) {
			Party party = worldData.getPartyFromMember(event.getPlayer().getUUID());
			if (party != null) {
				float friendBoost = party.getMembers().size() - 1;
				//System.out.println(friendBoost);
			}
		}

		if (event.getAbility().equals(ModAbilitiesRM.DARK_STEP.get())) {
			playerData.unequipAbility(StringsRM.lightStep, 0);
		}

		if (event.getAbility().equals(ModAbilitiesRM.LIGHT_STEP.get())) {
			playerData.unequipAbility(StringsRM.darkStep, 0);
		}

		if (event.getAbility().equals(ModAbilitiesRM.MP_SLOW.get())) {
			playerData.unequipAbility(Strings.mpHaste, 0);
		}

		if (event.getAbility().equals(ModAbilitiesRM.MP_SLOWRA.get())) {
			playerData.unequipAbility(Strings.mpHastera, 0);
		}

		if (event.getAbility().equals(ModAbilitiesRM.MP_SLOWGA.get())) {
			playerData.unequipAbility(Strings.mpHastega, 0);
		}

		if (event.getAbility().equals(ModAbilities.MP_HASTE.get())) {
			playerData.unequipAbility(StringsRM.mpSlow, 0);
		}
		if (event.getAbility().equals(ModAbilities.MP_HASTERA.get())) {
			playerData.unequipAbility(StringsRM.mpSlowra, 0);
		}
		if (event.getAbility().equals(ModAbilities.MP_HASTEGA.get())) {
			playerData.unequipAbility(StringsRM.mpSlowga, 0);
		}

	}


	@SubscribeEvent
	public void unequipAbility(AbilityEvent.Unequip event) {
		PlayerData playerData = PlayerData.get(event.getPlayer());
		if (playerData != null && event.getAbility() != null) {
			if (event.getAbility().equals(ModAbilitiesRM.DEDICATION.get())) {
				playerData.getStrengthStat().removeModifier("Dedication");
				playerData.getMagicStat().removeModifier("Dedication");
				playerData.getDefenseStat().removeModifier("Dedication");
			}

			if (event.getAbility().equals(ModAbilitiesRM.FRIEND_POWER.get())) {
				playerData.getStrengthStat().removeModifier("Friendship");
				playerData.getMagicStat().removeModifier("Friendship");
				playerData.getDefenseStat().removeModifier("Friendship");
			}
		}
	}

	// Helper Method for the Situation Gauge System
	/*public enum SpellElement {
		FIRE,
		BLIZZARD,
		THUNDER,
		WATER,
		AIR,
		LIGHT,
		DARK,
		PHYSICAL,
		MAGIC,
		NONE
	}*/

	/*private SpellElement getElement(String spellID){

		if (spellID.contains("fire") || spellID.contains("mine")) return SpellElement.FIRE;
		if (spellID.contains("blizzard")) return SpellElement.BLIZZARD;
		if (spellID.contains("thunder") ) return SpellElement.THUNDER;
		if (spellID.contains("water") || spellID.contains("balloon")) return SpellElement.WATER;
		if (spellID.contains("aero")) return SpellElement.AIR;
		if (spellID.contains("holy") || spellID.contains("faith") || spellID.contains("light") || spellID.contains("spark")) return SpellElement.LIGHT;
		if (spellID.contains("ruin") || spellID.contains("comet") || spellID.contains("dark") || spellID.contains("zantetsuken")) return SpellElement.DARK;
		if (spellID.contains("quick") || spellID.contains("sliding") || spellID.contains("blitz")) return SpellElement.PHYSICAL;
		if (spellID.contains("cure") || spellID.contains("auto-life") || spellID.contains("regen") || spellID.contains("stop")|| spellID.contains("gravity") || spellID.contains("magnet") || spellID.contains("reflect")) return SpellElement.MAGIC;


		return SpellElement.NONE;
	}*/

	@SubscribeEvent
	public void onRCCast(ReactionCommandCastEvent e) {

		//System.out.println(e.getRCID());//TODO add the RC versions too
		LivingEntity caster = e.getCaster();
		if (caster instanceof Player player) {
			PlayerData playerData = PlayerData.get(player);
			GlobalDataRM remindData = ModDataRM.getGlobal(player);
			if (playerData != null) {

			}
		}


	}

	@SubscribeEvent
	public void onMagicCast(MagicSpellCastEvent e) {
		LivingEntity caster = e.getCaster();
		if (caster instanceof Player player) {
			PlayerData playerData = PlayerData.get(player);
			GlobalDataRM remindData = ModDataRM.getGlobal(player);
			if (playerData != null) {
				//System.out.println(e.getSpellID());
				/*String spellID = e.getSpellID().toString();
				int spellLvl = e.getLevel() + 1;
				int cureConverterCount = playerData.getNumberOfAbilitiesEquipped(StringsRM.cure_converter);



				float situationBoost = playerData.getNumberOfAbilitiesEquipped(StringsRM.situationBoost);
				double situationValue = 5 * spellLvl;*/
				/*switch (spellID){
					case "default":
                        break;
					case "kkremind:magic_ultima":
						situationValue += 10;
						//System.out.println("Added "+situationValue);
						break;
					case "kkremind:warp":
						situationValue += 10;
						break;
					case "kkremind:magic_death":
						situationValue += 20;
						break;
					case "kkremind:magic_auto-life":
						situationValue = 0;
						break;
					case "kkremind:magic_comet":
						if (spellLvl == 2){
							situationValue += 5;
						}
					case "kingdomkeys:magic_cure":
						if (playerData.isAbilityEquipped(StringsRM.cure_converter)){
							situationValue = 35 + ((playerData.getNumberOfAbilitiesEquipped(StringsRM.cure_converter) - 1) * 15);
							//System.out.println("Cure Converters Equipped: "+cureConverterCount);
							//System.out.println(situationValue);
							break;
						} else {
							situationValue = 0;
						}
						break;
				}*/


				//remindData.addSituationSpell(spellID);
				/*remindData.addSituationValue(situationValue + (situationBoost * 1.25f)); //Magic increase
				addSituationRCs(player);*/

				//System.out.println("Situation Gauge: "+ remindData.getSituationValue());
				//System.out.println("Situation Spells: "+ remindData.getSituationSpells());

				remindData.setSCooldownTicks(60);
				if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
					remindData.setStyleTicks(100);
				}

				/*if (remindData.getSituationValue() >= 100) {
					Map<SpellElement, Integer> counts = new HashMap<>();
					for (String s : remindData.getSituationSpells()) {
						SpellElement element = getElement(s);
						counts.put(element, counts.getOrDefault(element, 0) + 1);
					}

					SpellElement majority = SpellElement.NONE;
					int highest = 0;

					for (Map.Entry<SpellElement, Integer> entry : counts.entrySet()) {
						if (entry.getValue() > highest) {
							highest = entry.getValue();
							majority = entry.getKey();
						}
					}
					//System.out.println(majority);

					switch (majority) {
						case FIRE:
							remindData.setStyle(majority.toString());
							break;
						case BLIZZARD:
							remindData.setStyle(majority.toString());
							break;
						case THUNDER:
							remindData.setStyle(majority.toString());
							break;
						case WATER:
							remindData.setStyle(majority.toString());
							break;
						case AIR:
							remindData.setStyle(majority.toString());
							break;
						case LIGHT:
							remindData.setStyle(majority.toString());
							break;
						case DARK:
							remindData.setStyle(majority.toString());
							break;
						case PHYSICAL:
							remindData.setStyle(majority.toString());
							break;
						case MAGIC:
							remindData.setStyle(majority.toString());
							break;
						case NONE:
							remindData.setStyle(majority.toString());
							break;
					}
				}*/

				//PacketHandlerRM.syncGlobalToAllAround(caster, remindData);
			}
		}
	}

	/*private void addSituationRCs(Player player) {
		PlayerData playerData = PlayerData.get(player);
		GlobalDataRM remindData = ModDataRM.getGlobal(player);
		if(playerData != null && remindData != null) {
			if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
				if (remindData.getSituationValue() >= 100) { // Base form finisher
					if (remindData.getStyle().equals("NONE") || remindData.getStyle().equals("")) {
						playerData.addReactionCommand(StringsRM.FinishRC, player);
						PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
					}
				}

				if (remindData.getStyle().equals("FIRE")) {
					playerData.addReactionCommand(StringsRM.FireStormRC, player); //To enter form
				}

				if (remindData.getStyle().equals("BLIZZARD")) {
					playerData.addReactionCommand(StringsRM.DiamondDustRC, player);
				}

				if (remindData.getStyle().equals("THUNDER")) {
					playerData.addReactionCommand(StringsRM.ThunderBoltRC, player);
				}

				if (remindData.getStyle().equals("PHYSICAL") || remindData.getStyle().equals("AIR")) {
					if (playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.waywardWindChain.get() || playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.lostMemoryChain.get() || playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.missingAcheChain.get()) {
						playerData.addReactionCommand(StringsRM.FeverPitchRC, player);
					}
				}

				if (remindData.getStyle().equals("PHYSICAL") || remindData.getStyle().equals("NONE")) {
					if (playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.earthshakerChain.get() || playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.endsOfTheEarthChain.get()) {
						playerData.addReactionCommand(StringsRM.CriticalImpactRC, player);
					}
				}

				if (remindData.getStyle().equals("MAGIC")) {
					if (playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.rainfellChain.get() || playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.stormfallChain.get()) {
						playerData.addReactionCommand(StringsRM.SpellweaverRC, player);
					}
				}

			} else if (ModDriveFormsRM.styles.contains(ResourceLocation.parse(playerData.getActiveDriveForm()))) {
				if (remindData.getSituationValue() >= 100) {
					if(playerData.getActiveDriveForm().equals(ModDriveFormsRM.FIRESTORM.get().getRegistryName().toString())) { // To finish form
						playerData.addReactionCommand(StringsRM.FireStormRC, player);
					}
					if(playerData.getActiveDriveForm().equals(ModDriveFormsRM.DIAMOND_DUST.get().getRegistryName().toString())) { // To finish form
						playerData.addReactionCommand(StringsRM.DiamondDustRC, player);
					}
					if(playerData.getActiveDriveForm().equals(ModDriveFormsRM.THUNDER_BOLT.get().getRegistryName().toString())) { // To finish form
						playerData.addReactionCommand(StringsRM.ThunderBoltRC, player);
					}
					if(playerData.getActiveDriveForm().equals(ModDriveFormsRM.FEVER_PITCH.get().getRegistryName().toString())) { // To finish form
						playerData.addReactionCommand(StringsRM.FeverPitchRC, player);
					}
					if(playerData.getActiveDriveForm().equals(ModDriveFormsRM.CRITICAL_IMPACT.get().getRegistryName().toString())) { // To finish form
						playerData.addReactionCommand(StringsRM.CriticalImpactRC, player);
					}
					if(playerData.getActiveDriveForm().equals(ModDriveFormsRM.SPELLWEAVER.get().getRegistryName().toString())) { // To finish form
						playerData.addReactionCommand(StringsRM.SpellweaverRC, player);
					}

				}
			}
		}
	}*/


	private void playFortunaMaxExceedEffects(ServerLevel level, Player player, LivingEntity target) {
		double x = target.getX();
		double y = target.getY() + target.getBbHeight() * 0.5D;
		double z = target.getZ();


		level.playSound(
				null,
				target.blockPosition(),
				SoundEvents.PLAYER_LEVELUP,
				SoundSource.PLAYERS,
				0.8F,
				1.8F
		);

		level.sendParticles(
				ParticleTypes.EXPLOSION,
				x, y, z,
				1,
				0.0D, 0.0D, 0.0D,
				0.0D
		);

		level.sendParticles(
				ParticleTypes.FLAME,
				x, y, z,
				80,
				1.0D, 0.6D, 1.0D,
				0.12D
		);

		level.sendParticles(
				ParticleTypes.LAVA,
				x, y, z,
				20,
				0.7D, 0.4D, 0.7D,
				0.1D
		);

		level.sendParticles(
				ParticleTypes.ENCHANT,
				x, y, z,
				50,
				0.8D, 0.8D, 0.8D,
				0.6D
		);

		for (LivingEntity nearby : level.getEntitiesOfClass(
				LivingEntity.class,
				target.getBoundingBox().inflate(4.0D),
				entity -> entity != player && entity != target && entity.isAlive()
		)) {
			Vec3 knockback = nearby.position().subtract(target.position()).normalize().scale(0.6D);
			nearby.push(knockback.x, 0.25D, knockback.z);

			level.sendParticles(
					ParticleTypes.FLAME,
					nearby.getX(),
					nearby.getY() + nearby.getBbHeight() * 0.5D,
					nearby.getZ(),
					20,
					0.4D, 0.3D, 0.4D,
					0.08D
			);
		}
	}


	private void playFortunaExceedEffects(ServerLevel level, Player player, LivingEntity target, int exceedLevel) {
		double x = target.getX();
		double y = target.getY() + target.getBbHeight() * 0.5D;
		double z = target.getZ();

		float pitch = switch (exceedLevel) {
			case 1 -> 1.0F;
			case 2 -> 1.25F;
			case 3 -> 1.55F;
			default -> 1.0F;
		};

		float volume = switch (exceedLevel) {
			case 1 -> 0.8F;
			case 2 -> 1.0F;
			case 3 -> 1.35F;
			default -> 1.0F;
		};

		level.playSound(
				null,
				target.blockPosition(),
				SoundEvents.BLAZE_SHOOT,
				SoundSource.PLAYERS,
				volume,
				pitch
		);

		level.playSound(
				null,
				target.blockPosition(),
				SoundEvents.PLAYER_ATTACK_CRIT,
				SoundSource.PLAYERS,
				volume,
				0.8F + exceedLevel * 0.2F
		);

		level.sendParticles(
				ParticleTypes.FLAME,
				x, y, z,
				18 * exceedLevel,
				0.35D, 0.35D, 0.35D,
				0.08D
		);

		level.sendParticles(
				ParticleTypes.CRIT,
				x, y, z,
				12 * exceedLevel,
				0.4D, 0.4D, 0.4D,
				0.2D
		);

		level.sendParticles(
				ParticleTypes.LAVA,
				x, y, z,
				4 * exceedLevel,
				0.25D, 0.25D, 0.25D,
				0.05D
		);

		if (exceedLevel >= 2) {
			level.sendParticles(
					ParticleTypes.SOUL_FIRE_FLAME,
					x, y + 0.1D, z,
					10,
					0.45D, 0.25D, 0.45D,
					0.04D
			);

			level.playSound(
					null,
					target.blockPosition(),
					SoundEvents.FIRECHARGE_USE,
					SoundSource.PLAYERS,
					1.0F,
					1.2F
			);
		}
	}

	@SubscribeEvent
	public void onLivingDamagePre(LivingDamageEvent.Pre event) {
		Entity sourceEntity = event.getSource().getEntity();
		Entity directEntity = event.getSource().getDirectEntity();

		Player player = null;

		if (sourceEntity instanceof Player sourcePlayer) {
			player = sourcePlayer;
		} else if (directEntity instanceof Player directPlayer) {
			player = directPlayer;
		}

		if (player == null) {
			return;
		}

		PlayerData playerData = PlayerData.get(player);

		if (playerData == null) {
			return;
		}

		MobEffectInstance exceed = player.getEffect(ModMobEffectsRM.EXCEED);

		if (exceed == null) {
			return;
		}

		if (playerData.getEquippedKeychain(DriveForm.NONE) == null ||
				!playerData.getEquippedKeychain(DriveForm.NONE).is(ModItemsRM.fortunaChain.get())) {
			player.removeEffect(ModMobEffectsRM.EXCEED);
			return;
		}

		int exceedLevel = exceed.getAmplifier() + 1;
		int fireBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.fireBoost);

		float exceedMultiplier = switch (exceedLevel) {
			case 1 -> 1.25F;
			case 2 -> 1.50F;
			case 3 -> 2.00F;
			default -> 1.0F;
		};

		float fireBoostMultiplier = 1.0F + fireBoosts * 0.15F;

		float oldDamage = event.getNewDamage();
		float newDamage = oldDamage * exceedMultiplier * fireBoostMultiplier;

		event.setNewDamage(newDamage);

		if (player.level() instanceof ServerLevel level) {
			playFortunaExceedEffects(level, player, event.getEntity(), exceedLevel);

			if (exceedLevel >= 3) {
				playFortunaMaxExceedEffects(level, player, event.getEntity());
			}
		}

		player.removeEffect(ModMobEffectsRM.EXCEED);
	}

	@SubscribeEvent
	public void onLivingUpdate(EntityTickEvent.Pre event) {
		if (event.getEntity() instanceof LivingEntity livingEntity) {
			GlobalDataRM globalData = ModDataRM.getGlobal(livingEntity);
			/*if (event.getEntity() instanceof Player player) {
				PlayerData playerData = PlayerData.get(player);
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
					} else if (globalData.getCanCounter() > 1 || globalData.getCanCounter() < 0){
						globalData.setCanCounter(0);
						ticks = 0;
					}
				}
			}*/

			// MP Boost Test
			/*if (event.getEntity() instanceof Player player) {
				PlayerData playerData = PlayerData.get(player);
				if (playerData != null){

				}
			}*/


			// Form Shotlock Change Test
			/*if (event.getEntity() instanceof Player player) {
				PlayerData playerData = PlayerData.get(player);
				if (playerData != null && playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.darkForm)) {
					//playerData.setEquippedShotlock(KingdomKeysReMind.MODID + ":" + StringsRM.darkDivide);
					//System.out.println(playerData.getEquippedShotlock());
				} else {
					//System.out.println(playerData.getEquippedShotlock());
				}
			}*/


			// Org Passives
			if (event.getEntity() instanceof Player player) {
				PlayerData playerData = PlayerData.get(player);

				if (playerData != null && playerData.getAlignment() != Utils.OrgMember.NONE) {
					playerData.getStrengthStat().addModifier("Organization", 5, false, true);
					playerData.getMagicStat().addModifier("Organization", 5, false, true);
					playerData.getDefenseStat().addModifier("Organization", 5, false, true);
				} else if (playerData != null && playerData.getAlignment() == Utils.OrgMember.NONE) {
					playerData.getStrengthStat().removeModifier("Organization");
					playerData.getMagicStat().removeModifier("Organization");
					playerData.getDefenseStat().removeModifier("Organization");
				}
			}

			if (event.getEntity() instanceof Player player) {
				PlayerData playerData = PlayerData.get(player);
				WorldData worldData = WorldData.get(player.getServer());
				if (playerData != null && globalData != null) {
					RageFormChance state = playerStates.computeIfAbsent(player, p -> new RageFormChance());
					// Reset Rage form roll
					if (!player.level().isClientSide() && !playerData.getActiveDriveForm().equals(ModDriveFormsRM.RAGE.get().getRegistryName().toString())) {
						if(!Utils.isLowHP(player.getHealth(), player.getMaxHealth())) {
							if(state.hasRolled) {
								state.hasRolled = false;
								state.shouldAppear = false;
								//System.out.println("Resetted roll");
							}
						}
					}

					updateDriveAbilities(player, StringsRM.darkPower, KingdomKeysReMind.MODID + ":" + StringsRM.darkForm);
					updateDriveAbilities(player, StringsRM.rageAwakened, KingdomKeysReMind.MODID + ":" + StringsRM.rageForm);
					updateDriveAbilities(player, StringsRM.wayToLight, KingdomKeysReMind.MODID + ":" + StringsRM.lightForm);
					updateDriveAbilities(player, StringsRM.roadToDawn, KingdomKeysReMind.MODID+":"+ StringsRM.twilight);
					updateDriveAbilities(player, StringsRM.Regen, KingdomKeysReMind.MODID + ":" + StringsRM.regenForm);

					// Light/Darkness Within

					double boostWithin = (playerData.getStrengthStat().get() + playerData.getMagicStat().get()) / 2;

					double regenBoost = (playerData.getMagicStat().get() * 0.1f) * (playerData.getNumberOfAbilitiesEquipped(StringsRM.darknessBoost));

					//System.out.println("STR: "+playerData.getStrengthStat().get());
					//System.out.println("MAG: "+playerData.getMagicStat().get());

					//System.out.println("Potential Boost: "+boostWithin);

					int darknessWithinBoost = (int) (boostWithin * (PlayerData.get(player).getNumberOfAbilitiesEquipped(StringsRM.darknessBoost) * 0.1F));
					int lightWithinBoost = (int) (boostWithin * (PlayerData.get(player).getNumberOfAbilitiesEquipped(StringsRM.lightBoost) * 0.1F));

					if (playerData.isAbilityEquipped(StringsRM.lightWithin)) {
						//System.out.println("Light Boost: "+ lightWithinBoost);
						playerData.getStrengthStat().addModifier("light_within", lightWithinBoost, false, false);
						playerData.getMagicStat().addModifier("light_within", lightWithinBoost, false, false);
					} else {
						playerData.getStrengthStat().removeModifier("light_within");
						playerData.getMagicStat().removeModifier("light_within");
					}
					if (playerData.isAbilityEquipped(StringsRM.darknessWithin)) {
						//System.out.println("Dark Boost: "+ darknessWithinBoost);
						playerData.getStrengthStat().addModifier("darkness_within", darknessWithinBoost, false, false);
						playerData.getMagicStat().addModifier("darkness_within", darknessWithinBoost, false, false);
					} else {
						playerData.getStrengthStat().removeModifier("darkness_within");
						playerData.getMagicStat().removeModifier("darkness_within");
					}

					if (playerData.isAbilityEquipped(StringsRM.Regen)){
						playerData.getMagicStat().addModifier("regen_buff", regenBoost, false, false);
					} else {
						playerData.getMagicStat().removeModifier("regen_buff");
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
						//System.out.println(playerData.getHearts() + " > " + heartsBoost);
						float overBoost = heartsBoost * 0.025f;
						//System.out.println(overBoost);
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
						int friendBoost = 0;
						if (party != null || globalData.hasDreamEaterSummoned()) {
							if (party != null) {
								friendBoost = 5 * (party.getMembers().size() - 1);
							}
							if (globalData.hasDreamEaterSummoned()){
								friendBoost ++;
							}
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
						double attackSpeedBonus = 0.25 * playerData.getNumberOfAbilitiesEquipped(StringsRM.attackHaste);
						player.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(4 + attackSpeedBonus);
					} else if (!playerData.isAbilityEquipped(StringsRM.attackHaste)) {
						player.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(4);
					}

					// Ultima Weapon Ability
					if (playerData.isAbilityEquipped(StringsRM.ultima_weapon_ability)) {
						ItemStack heldStack = player.getMainHandItem();
						Item heldItem = heldStack.getItem();

						boolean hasAttackDamage = false;

						ItemAttributeModifiers component = heldStack.get(DataComponents.ATTRIBUTE_MODIFIERS);

						if (component != null) {
							for (ItemAttributeModifiers.Entry entry : component.modifiers()) {
								if (entry.attribute().equals(Attributes.ATTACK_DAMAGE) && entry.slot().test(EquipmentSlot.MAINHAND)) {
									if (entry.modifier().amount() > 0) {
										hasAttackDamage = true;
										break;
									}
								}
							}
						}

						boolean validWeapon = heldItem instanceof KeybladeItem || heldItem instanceof IOrgWeapon || hasAttackDamage;

						if (validWeapon && !heldStack.isEmpty()) {
							int weaponSTR = 0;
							int weaponMAG = 0;

							// Keyblade stats (uses keyblade level)
							if (heldItem instanceof KeybladeItem kb) {
								weaponSTR = kb.getStrength(heldStack);
								weaponMAG = kb.getMagic(heldStack);
							}

							// Organization weapons (flat stats)
							else if (heldItem instanceof IOrgWeapon org) {
								weaponSTR = org.getStrength();
								weaponMAG = org.getMagic();
							}

							// Initialize boost variables
							int addSTR;
							int addMAG;

							int posSTR = ModConfigs.ultimaPositiveSTR;
							int negSTR = ModConfigs.ultimaNegativeSTR;

							int posMAG = ModConfigs.ultimaPositiveMAG;
							int negMAG = ModConfigs.ultimaNegativeMAG;

							// Strength
							int rawSTR = posSTR - weaponSTR;

							if (weaponSTR < 0) {
								// Negative stat → clamp to negative cap
								addSTR = Math.clamp(rawSTR, 0, negSTR);
							} else {
								// Normal stat → clamp to positive cap
								addSTR = Math.clamp(rawSTR, 0, posSTR);
							}

							// Magic
							int rawMAG = posMAG - weaponMAG;

							if (weaponMAG < 0) {
								addMAG = Math.clamp(rawMAG, 0, negMAG);
							} else {
								addMAG = Math.clamp(rawMAG, 0, posMAG);
							}

							// Remove old modifiers to prevent stacking
							playerData.getStrengthStat().removeModifier("Ultima Weapon");
							playerData.getMagicStat().removeModifier("Ultima Weapon");

							// Apply new modifiers
							if (addSTR != 0) {
								playerData.getStrengthStat().addModifier("Ultima Weapon", addSTR, false, false);
							}

							if (addMAG != 0) {
								playerData.getMagicStat().addModifier("Ultima Weapon", addMAG, false, false);
							}

						} else {
							// Not holding a valid weapon → remove buffs
							playerData.getStrengthStat().removeModifier("Ultima Weapon");
							playerData.getMagicStat().removeModifier("Ultima Weapon");
						}

					} else {
						// Ability not equipped → ensure buffs are gone
						playerData.getStrengthStat().removeModifier("Ultima Weapon");
						playerData.getMagicStat().removeModifier("Ultima Weapon");
					}

					// HP Boost
					if (playerData.isAbilityEquipped(StringsRM.hpBoost)) {
						int countHP = playerData.getNumberOfAbilitiesEquipped(StringsRM.hpBoost);
						int newHpBonus = countHP * 10;

						int lastHp = globalData.getLastHpBoostBonus();
						int diffHp = newHpBonus - lastHp;

						if (diffHp != 0) {
							globalData.setLastHpBoostBonus(newHpBonus);

							int baseHp = playerData.getMaxHP();
							int boostedHp = baseHp + newHpBonus;

							player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(boostedHp);
						}
					} else {
						int lastHp = globalData.getLastHpBoostBonus();
						if (lastHp != 0) {
							globalData.setLastHpBoostBonus(0);

							int baseHp = playerData.getMaxHP();
							player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(baseHp);
						}
					}


					// MP Boost
					if (playerData.isAbilityEquipped(StringsRM.mpBoost)) {
						int countMP = playerData.getNumberOfAbilitiesEquipped(StringsRM.mpBoost);
						int newMpBonus = countMP * 10;

						int lastMp = globalData.getLastMpBoostBonus();
						int diffMp = newMpBonus - lastMp;

						if (diffMp != 0) {
							playerData.addMaxMP(diffMp);
							globalData.setLastMpBoostBonus(newMpBonus);
						}
					} else {
						int lastMp = globalData.getLastMpBoostBonus();
						if (lastMp != 0) { playerData.addMaxMP(-lastMp);
							globalData.setLastMpBoostBonus(0);
						}
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
					if (!player.level().isClientSide) {
						if (playerData.getAlignment() != Utils.OrgMember.NONE && globalData.getPanelsEnabled() == 1) {

							if (globalData.getSTRPanel() > ModConfigs.panelLimit) {
								playerData.getStrengthStat().addModifier("Panel", ModConfigs.panelLimit, false, false);
								globalData.setSTRPanel(ModConfigs.panelLimit);
							}
							if (globalData.getMAGPanel() > ModConfigs.panelLimit) {
								playerData.getMagicStat().addModifier("Panel", ModConfigs.panelLimit, false, false);
								globalData.setMAGPanel(ModConfigs.panelLimit);
							}
							if (globalData.getDEFPanel() > ModConfigs.panelLimit) {
								playerData.getDefenseStat().addModifier("Panel", ModConfigs.panelLimit, false, false);
								globalData.setDEFPanel(ModConfigs.panelLimit);
							}

							playerData.getStrengthStat().addModifier("Panel", globalData.getSTRPanel(), false, false);
							playerData.getMagicStat().addModifier("Panel", globalData.getMAGPanel(), false, false);
							playerData.getDefenseStat().addModifier("Panel", globalData.getDEFPanel(), false, false);


						}
					} else {
						playerData.getStrengthStat().removeModifier("Panel");
						playerData.getMagicStat().removeModifier("Panel");
						playerData.getDefenseStat().removeModifier("Panel");
						//PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
					}
				}
			}

			if (globalData != null) {
				if (event.getEntity() instanceof Player player) {
					PlayerData playerData = PlayerData.get(player);
					if (playerData != null) {
						// RC Cooldown mechanic
						if (globalData.getRCCooldownTicks() > 0) {
							globalData.remRCCooldownTicks(1);
						}

						// Formchange/Situation Gauge System
						if (globalData.getSCooldownTicks() > 0 ){
							globalData.remSCooldownTicks(1);
							//System.out.println("Situation Gauge Ticks: " + globalData.getSCooldownTicks());
						}
						if (!player.hasEffect(ModMobEffects.STOP)){
							if (globalData.getSCooldownTicks() == 0 && globalData.getSituationValue() > 0){
								// Possible Haste and Slow Interactions?

								globalData.setSituationValue(globalData.getSituationValue() - 0.2);
							}
						}


						if (globalData.getSituationValue() <= 0) {
							//globalData.clearSituationSpells();
							//globalData.setStyle("");
							PacketHandlerRM.syncGlobalToAllAround(player, globalData);
							if (globalData.getStyleTicks() > 0) {
								globalData.remStyleTicks(1);
								//System.out.println(globalData.getStyleTicks());
							} else if (globalData.getStyleTicks() <= 0) {
								if (ModDriveFormsRM.styles.contains(ResourceLocation.parse(playerData.getActiveDriveForm()))) { //Only check styles bruh
									playerData.setActiveDriveForm(DriveForm.NONE.toString());
								}
							}

						} else if (globalData.getSituationValue() > 100) {
							globalData.setSituationValue(100);
						}
					}
				}

				// Step Ticks
				if (globalData.getStepTicks() > 0) {
					globalData.remStepTicks(1);
					if (globalData.getStepTicks() <= 0) { // Step has finished, notify all the clients about it
						PacketHandlerRM.syncGlobalToAllAround((Player) event.getEntity(), (GlobalDataRM) globalData);
						if (event.getEntity() instanceof Player player) {
							PlayerData playerData = PlayerData.get(player);

							player.invulnerableTime = globalData.getStepTicks();

							if (playerData.isAbilityEquipped(StringsRM.darkStep) || playerData.getActiveDriveForm().equals(ModDriveFormsRM.DARK.get().getRegistryName().toString())) {
								player.level().playSound(null, player.blockPosition(), ModSoundsRM.DARKSTEP2.get(), SoundSource.PLAYERS, 1F, 1F);
							}
							if (playerData.isAbilityEquipped(StringsRM.lightStep) || playerData.getActiveDriveForm().equals(ModDriveFormsRM.LIGHT.get().getRegistryName().toString())) {
								player.level().playSound(null, player.blockPosition(), ModSoundsRM.LIGHTSTEP2.get(), SoundSource.PLAYERS, 1F, 1F);
							}
						}
					}
				}

				// Spells go Down Below
				// Berserk
				if (event.getEntity() instanceof Player player) {
					boolean sync;
					PlayerData playerData = PlayerData.get(player);
					if (player.hasEffect(ModMobEffectsRM.BERSERK)){
						MobEffectInstance berserk = player.getEffect(ModMobEffectsRM.BERSERK);

						int amp = berserk.getAmplifier();

						double strBonus = (playerData.getStrengthStat().getStat() * 0.15D) * (amp + 1);
						double defDebuff = (playerData.getDefenseStat().getStat() * 0.15D) * (amp + 1);

						sync = !playerData.getStrengthStat().hasModifier("berserk");
						playerData.getStrengthStat().addModifier("berserk", strBonus, false, false);
						playerData.getDefenseStat().addModifier("berserk", -defDebuff, false, false);

					} else {
						sync = playerData.getStrengthStat().hasModifier("berserk");
						playerData.getStrengthStat().removeModifier("berserk");
						playerData.getDefenseStat().removeModifier("berserk");

					}
					if(!event.getEntity().level().isClientSide && sync) {
						PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
					}
				}

				// Stone
				if (event.getEntity() instanceof Player player) {
					PlayerData playerData = PlayerData.get(player);
					if (player.hasEffect(ModMobEffectsRM.STONE)) {
						if (player.isCreative()){
							event.setCanceled(true);
						}
						MobEffectInstance stone = player.getEffect(ModMobEffectsRM.STONE);
						int amp = stone.getAmplifier();

						playerData.setMagicCooldownTicks(5);

						float wobbleYaw = (player.getRandom().nextFloat() - 0.5f) * (2.0f + amp * 1.5f); // stronger = more struggle
						player.setYRot(player.getYRot() + wobbleYaw);
						player.yRotO = player.getYRot();

						float wobblePitch = (player.getRandom().nextFloat() - 0.5f) * (1.0f + amp * 0.75f);
						player.setXRot(player.getXRot() + wobblePitch);
						player.xRotO = player.getXRot();

						player.setDeltaMovement(0,0,0);
						player.hurtMarked = true;

					}
				}

				if (event.getEntity() instanceof Mob mob){
					if (mob.hasEffect(ModMobEffectsRM.STONE)) {
						MobEffectInstance stone = mob.getEffect(ModMobEffectsRM.STONE);
						int amp = stone.getAmplifier();
						float wobbleYaw = (mob.getRandom().nextFloat() - 0.5f) * (2.0f + amp * 1.5f); // stronger = more struggle
						mob.setYRot(mob.getYRot() + wobbleYaw);
						mob.yRotO = mob.getYRot();

						float wobblePitch = (mob.getRandom().nextFloat() - 0.5f) * (1.0f + amp * 0.75f);
						mob.setXRot(mob.getXRot() + wobblePitch);
						mob.xRotO = mob.getXRot();
						mob.setTarget(null);
						mob.setDeltaMovement(0,0,0);
						mob.hurtMarked = true;
					}
				}

				// Confuse - Mob Logic
				if (event.getEntity() instanceof Mob mob) {
					if (mob.hasEffect(ModMobEffectsRM.CONFUSE)) {
						MobEffectInstance confuse = mob.getEffect(ModMobEffectsRM.CONFUSE);

						int amp = confuse.getAmplifier();
						if (mob.getRandom().nextInt(20) == 0) {
							mob.setTarget(null);
							mob.setLastHurtByMob(null);

						}

						if (mob.onGround() && mob.getRandom().nextInt(12) == 0) {
							double dx = (mob.getRandom().nextDouble() - 0.5D) * 0.35D;
							double dz = (mob.getRandom().nextDouble() - 0.5D) * 0.35D;
							mob.push(dx, 0, dz);

						}

						int retargetChance = Math.max(6, 14 - amp * 4); // amp makes it more chaotic
						if (mob.getTarget() == null && mob.getRandom().nextInt(retargetChance) == 0) {

							List<LivingEntity> nearby = mob.level().getEntitiesOfClass(
									LivingEntity.class,
									mob.getBoundingBox().inflate(8.0D),
									e -> e.isAlive() && e != mob && !(e instanceof Player p && p.isCreative())
							);

							if (!nearby.isEmpty()) {
								LivingEntity pick = nearby.get(mob.getRandom().nextInt(nearby.size()));
								mob.setTarget(pick);

							}
						}
					}
				}

				// Confuse - Player Logic
				if (event.getEntity() instanceof Player player) {
					if (player.hasEffect(ModMobEffectsRM.CONFUSE)) {
						MobEffectInstance confuse = player.getEffect(ModMobEffectsRM.CONFUSE);
						int amp = confuse.getAmplifier();
						RandomSource rand = player.getRandom();


						if (rand.nextInt(Math.max(10, 6 - amp)) == 0){
							double strafe = (rand.nextDouble() - 0.5D) * 0.75D;
							double forward = (rand.nextDouble() - 0.5D) * 0.75D;
							player.moveRelative(0.75F, new Vec3(strafe, 0, forward));
						}

						if (rand.nextInt(20) == 0) {
							float yaw = (rand.nextFloat() - 0.15F) * (5 + amp * 4);
							player.setYRot(player.getYRot() + yaw);
							player.yRotO = player.getYRot();
						}

						if (rand.nextInt(Math.max(6, 12 - amp * 2)) == 0) {
							//event.setCanceled(true);
							if (player.onGround()) {
								player.jumpFromGround();
							} else {
								player.isCrouching();
							}
						}
					}
				}

				// Regen
				if (event.getEntity() instanceof Player player) {
					PlayerData playerData = PlayerData.get(player);
					if (player.hasEffect(ModMobEffectsRM.REGEN)) {
						MobEffectInstance regen = player.getEffect(ModMobEffectsRM.REGEN);

						int amp = regen.getAmplifier();

						switch (amp) {
							case 0:
								player.heal(0.25f);
								break;
							case 1:
								player.heal(0.5f);
								break;
							case 2:
								player.heal(1f);
								playerData.addMP(0.5);
								playerData.addFocus(0.5);
								break;
						}
					}
				} else if (event.getEntity() instanceof Mob mob) {
					if (mob.hasEffect(ModMobEffectsRM.REGEN)) {
						MobEffectInstance regen = mob.getEffect(ModMobEffectsRM.REGEN);

						int amp = regen.getAmplifier();

						switch (amp) {
							case 0:
								mob.heal(0.25f);
								break;
							case 1:
								mob.heal(0.5f);
								break;
							case 2:
								mob.heal(1f);
								break;
						}
					}
				}

				if (event.getEntity() instanceof Player player) {
					PlayerData playerData = PlayerData.get(player);
					// MP Slow Testing
					if (playerData.isAbilityEquipped(StringsRM.mpSlow) || playerData.isAbilityEquipped(StringsRM.mpSlowra) || playerData.isAbilityEquipped(StringsRM.mpSlowga)){
						//System.out.println(Utils.getMPHasteValue(playerData));
						double val = 0;
						val += (1.5 * playerData.getNumberOfAbilitiesEquipped(StringsRM.mpSlow));
						val += (3.5 * playerData.getNumberOfAbilitiesEquipped(StringsRM.mpSlowra));
						val += (5.5 * playerData.getNumberOfAbilitiesEquipped(StringsRM.mpSlowga));

						//System.out.println("Slow Haste Value: "+val);

						// Cap so your MP doesn't stop recharging
						if (val > 16){
							val = 16;
						}

						//System.out.println("Slow Haste Value (After Adjust/Cap): "+val);

						if (playerData.getRecharge() && playerData.getMP() < playerData.getMaxMP()){
							//playerData.addMP(playerData.getMaxMP() / 500 * ((Utils.getMPHasteValue(playerData) / 10) + 2));
							playerData.addMP(playerData.getMaxMP() / 500 * (((Utils.getMPHasteValue(playerData) - val) / 10)));
						}
					}

					// One HP Ability
					if (playerData.isAbilityEquipped(StringsRM.oneHP)){
						if (player.getHealth() > 1){
							player.setHealth(1);
						}
					}

					// Fever Pitch Passive
					if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.FEVER_PITCH.get().getRegistryName().toString())) {
						player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED,2,1,true,true,true));
					}

				}


				// HP / MP / EXP Walker
				if (event.getEntity() instanceof Player player) {
					PlayerData playerData = PlayerData.get(player);
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
							if (!player.level().isClientSide && player.tickCount % 20 == 0 && playerData.isAbilityEquipped(StringsRM.expWalker) && player.onGround()) {
								if (!playerData.isAbilityEquipped(Strings.zeroExp)) {
									if (playerData.isAbilityEquipped(Strings.experienceBoost) && player.getHealth() <= player.getMaxHealth() / 2) {
										int expBoost = playerData.getNumberOfAbilitiesEquipped(Strings.experienceBoost);
										playerData.addExperience(player, (int) ((playerData.getNumberOfAbilitiesEquipped(StringsRM.expWalker) * expBoost)  * online.kingdomkeys.kingdomkeys.config.ModConfigs.SERVER.xpMultiplier.get()), false, true);
									} else {
										playerData.addExperience(player, (int) (playerData.getNumberOfAbilitiesEquipped(StringsRM.expWalker) * online.kingdomkeys.kingdomkeys.config.ModConfigs.SERVER.xpMultiplier.get()), false, true);
									}
								}
							}
							if (!player.level().isClientSide && player.tickCount % 20 == 0 && playerData.isAbilityEquipped(StringsRM.heartWalker)) {
								playerData.addHearts((int) (5 * playerData.getNumberOfAbilitiesEquipped(StringsRM.heartWalker) * online.kingdomkeys.kingdomkeys.config.ModConfigs.SERVER.heartMultiplier.get()));
							}
							if (player.tickCount % 50 == 0 && playerData.isAbilityEquipped(StringsRM.focusWalker)) {
								if (!playerData.getRecharge()) {
									int focusWalkerMult = playerData.getNumberOfAbilitiesEquipped(StringsRM.focusWalker);
									playerData.addFocus(0.5 * focusWalkerMult);
								}
							}
						}

						if (playerData.isAbilityEquipped(StringsRM.ribbon)){
							List<MobEffectInstance> effectsList = new ArrayList<>();
							for (MobEffectInstance e : player.getActiveEffects()) {
								if (e.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
									effectsList.add(e);
								}
							}

							for(MobEffectInstance badEffect: effectsList){
								player.removeEffect(badEffect.getEffect());
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void playerHeal(LivingHealEvent event){
		if (event.getEntity() instanceof Player player){
			PlayerData playerData = PlayerData.get(player);
			if (playerData.isAbilityEquipped(StringsRM.oneHP)){
				event.setCanceled(true);
			}
		}
	}

	private void applyFortunaBurst(Player player, LivingEntity target, float damage) {
		for (LivingEntity nearby : target.level().getEntitiesOfClass(
				LivingEntity.class,
				target.getBoundingBox().inflate(3.0D),
				entity -> entity != player && entity != target && entity.isAlive()
		)) {
			nearby.hurt(
					player.damageSources().indirectMagic(player, player),
					damage * 0.5F
			);
		}
	}

	private void applyFortunaExceedBonus(Player player, LivingEntity target) {
		PlayerData playerData = PlayerData.get(player);

		if (playerData == null) {
			return;
		}

		MobEffectInstance exceed = player.getEffect(ModMobEffectsRM.EXCEED);

		if (exceed == null) {
			return;
		}

		if (playerData.getEquippedKeychain(DriveForm.NONE) == null ||
				!playerData.getEquippedKeychain(DriveForm.NONE).is(ModItemsRM.fortunaChain.get())) {
			player.removeEffect(ModMobEffectsRM.EXCEED);
			return;
		}

		int exceedLevel = exceed.getAmplifier() + 1;
		int fireBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.fireBoost);

		float baseBonusDamage = switch (exceedLevel) {
			case 1 -> 8.0F;
			case 2 -> 16.0F;
			case 3 -> 28.0F;
			default -> 0.0F;
		};

		float fireBoostMultiplier = 1.0F + fireBoosts * 0.15F;
		float finalBonusDamage = baseBonusDamage * fireBoostMultiplier;

		target.hurt(
				player.damageSources().indirectMagic(player, player),
				finalBonusDamage
		);

		player.removeEffect(ModMobEffectsRM.EXCEED);
	}

	@SubscribeEvent
	public void onPlayerAttack(AttackEntityEvent event) {
		if (event.getEntity() instanceof Player player) {
			if (player.hasEffect(ModMobEffectsRM.STONE)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onKnockback(LivingKnockBackEvent event){
		if (event.getEntity() instanceof Player player){
			PlayerData playerData = PlayerData.get(player);
			GlobalDataRM globalData = ModDataRM.getGlobal(event.getEntity());

			if (playerData == null)
				return;
			if (globalData == null)
				return;

			if (globalData.getStepTicks() > 0 ){
				event.setCanceled(true);
			}
		}
	}




	@SubscribeEvent
	public void onEffectAdded(MobEffectEvent.Added event){
		if (event.getEntity() instanceof Player player){
			PlayerData playerData = PlayerData.get(player);
			if (playerData.isAbilityEquipped(StringsRM.ribbon)){
				MobEffectInstance effect = event.getEffectInstance();
				if (effect != null){
					var effectHolder = effect.getEffect();
					if (effectHolder.value().getCategory() == MobEffectCategory.HARMFUL){
						player.removeEffect(effectHolder);
					}
					//System.out.println(effect.getEffect().value());
					if (effect.getEffect().getKey() == ModMobEffects.FREEZE.getKey()){
						//System.out.println("Freeze!");
						player.removeEffect(effectHolder);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		GlobalDataRM data = ModDataRM.getGlobal(event.getEntity());
		if (data != null) {
			data.setHasDreamEaterSummoned(false);
			data.setDreamEaterUUID(null);
		}
	}


	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event){
		GlobalDataRM globalData = ModDataRM.getGlobal(event.getEntity());
		if (event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
				if (player.hasEffect(ModMobEffectsRM.AUTO_LIFE)){
					if(player.getHealth() <= 0){
						event.setCanceled(true);
						player.setHealth(0.1F);
						player.invulnerableTime = 10;
						player.removeAllEffects();
						player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 10));
						player.level().playSound(null, player.blockPosition(), ModSoundsRM.AUTOLIFE.get(), SoundSource.PLAYERS, 1F, 1F);
					}
				}
			}
		// Dream Eater Death Event

	}

	private static final WeakHashMap<Player, RageFormChance> playerStates = new WeakHashMap<>();

	private boolean rollChance(double percent){
		Random rand = new Random();
		return rand.nextDouble() * 100 < percent;
	}

	private static class RageFormChance {
		boolean hasRolled = false;
		boolean shouldAppear = false;
	}

	private double calculateDynamicChance(float hpPercent) {
		// Cap HP percent at 25, and floor at 1 to avoid going above 34%
		hpPercent = Math.max(1.0f, Math.min(25.0f, hpPercent));
		double baseChance = ModConfigs.rageFormPercent;
		float missingPercent = 0;
		// Percent below 25, unless config is set to 0
		if (baseChance != 0.0) {
			missingPercent = 25.0f - hpPercent;

		}

		// Base chance is 10% at 25% HP, +1% for each % below
		return baseChance + missingPercent;
	}

	@SubscribeEvent
	public void hurtPostEvent(LivingDamageEvent.Post event) {
		if(event.getEntity() instanceof Player player) {
			PlayerData playerData = PlayerData.get(player);
			if(playerData == null)
				return;

			// Rage form
			float formMissingHP = (player.getHealth() / player.getMaxHealth()) * 100;
			RageFormChance state = playerStates.computeIfAbsent(player, p -> new RageFormChance());
			if (!player.level().isClientSide() && !playerData.getActiveDriveForm().equals(ModDriveFormsRM.RAGE.get().getRegistryName().toString())) {
				if (Utils.isLowHP(player.getHealth(), player.getMaxHealth())) {
					if (!state.hasRolled) {
						double chance = calculateDynamicChance(formMissingHP);
						//System.out.println("Client: "+player.level().isClientSide+" Chance: " + chance + "Roll Chance: " + state.shouldAppear);
						boolean chanced = rollChance(chance);
						//System.out.println("Chance result: "+chanced);
						state.shouldAppear = chanced;
						state.hasRolled = true;
					}

					if (state.shouldAppear) {
						playerData.addReactionCommand(StringsRM.RageRC, player);
						PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void hurtEvent(LivingDamageEvent.Pre event){
		if(event.getEntity() instanceof Player player) {
			PlayerData playerData = PlayerData.get(player);
			GlobalDataRM globalData = ModDataRM.getGlobal(player);

			if (globalData == null)
				return;
			if(playerData == null)
				return;

			if (globalData.getStepTicks() > 0){
				event.setNewDamage(0);
			}

			// Adrenaline
			if (playerData.isAbilityEquipped(StringsRM.adrenaline)) {
				if (player.getHealth() - event.getNewDamage() <= player.getMaxHealth() / 4){
					playerData.getStrengthStat().addModifier("adrenaline", 5, false, false);
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);				}
			}
			// Critical Surge
			if (playerData.isAbilityEquipped(StringsRM.critical_surge)){
				if (player.getHealth() - event.getNewDamage() <= player.getMaxHealth() / 4){
					playerData.getMagicStat().addModifier("critical_surge", 5, false, false);
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);				}
			}
			if (player.getHealth() + 1 >= player.getMaxHealth() / 4) {
				playerData.getStrengthStat().removeModifier("adrenaline");
				playerData.getMagicStat().removeModifier("critical_surge");
				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
			}

			if (playerData.isAbilityEquipped(StringsRM.Lyric1)){
				if (event.getSource().getMsgId().equals(KKResistanceType.lightning.toString()) || event.getSource().type().msgId().equals("lightning")){
					float thunderDMG = event.getNewDamage();
					//System.out.println("Thunder Resist, " + thunderDMG +" Reduced to -> "+ (thunderDMG - (thunderDMG *= 0.1f))); //Uncomment me for debugging ig
					event.setNewDamage(thunderDMG - (thunderDMG *= 0.1f));
				}
			}

			//Protect Abilities

			// MP Shield
			if (playerData.isAbilityEquipped(StringsRM.mpShield) && playerData.getMP() > 0 && !playerData.getRecharge()) {
				float DMGTaken = event.getNewDamage();

				if (DMGTaken > playerData.getMP()) {
					float overflowDMG = (float) (DMGTaken - playerData.getMP());
					event.setNewDamage(overflowDMG);
				} else {
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

			}


		}



		// On Hit Effects
		if (event.getSource().getEntity() instanceof Player player){
			PlayerData playerData = PlayerData.get(player);
			GlobalDataRM remindData = ModDataRM.getGlobal(player);
			if(playerData != null) {


					/*double situationGain = (event.getNewDamage() * 0.1);
					float situationMulti = (playerData.getNumberOfAbilitiesEquipped(StringsRM.situationBoost) *0.1f) + 1;
					situationGain *= situationMulti;*/

					//System.out.println(situationGain + " * " + situationMulti +" = " + (situationGain*situationMulti));
					//System.out.println(situationGain);

				/*if (remindData != null){
					remindData.addSituationValue(situationGain); //Hit increase
					addSituationRCs(player);
					remindData.setSCooldownTicks(60);
					if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) ) {
						remindData.setStyleTicks(100);
					}
					PacketHandlerRM.syncGlobalToAllAround(player, remindData);
				}*/

				// Critical Impact Passive
				if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.CRITICAL_IMPACT.get().getRegistryName().toString())) {
					if (event.getSource().type().msgId().equals("player")) {
						float dmg = playerData.getStrength(true) * 0.15f;
						LivingEntity target = event.getEntity();
						target.invulnerableTime = 0;
						target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.OFFHAND, event.getEntity(), null), dmg);
					}
				}

				// Spellweaver Passive
				if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.SPELLWEAVER.get().getRegistryName().toString())) {
					if (event.getSource().type().msgId().equals("player")) {
						float dmg = playerData.getMagic(true) * 0.1f;
						LivingEntity target = event.getEntity();
						target.invulnerableTime = 0;
						target.hurt(event.getEntity().damageSources().magic(), dmg);
					}
				}


				int crtBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.criticalBoost);
				float addDmg = (float) (crtBoosts * 3);
				if (playerData.isAbilityEquipped(StringsRM.Jecht)){
					if (event.getSource().type().msgId().equals("player")) { // Applies to ONLY melee
						//System.out.println(addDmg);
						event.getEntity().hurt(event.getEntity().damageSources().magic(), addDmg);
						event.getEntity().invulnerableTime = 0;
					}
				}

				// My Exclusive Ability
				if (playerData.isAbilityEquipped(StringsRM.Xephiro)){

					float currentHP = player.getHealth();
					float maxHP =  player.getMaxHealth();
					float missingHPRatio = 1.f - (currentHP / maxHP);

					float darkScaling = 1f + (playerData.getNumberOfAbilitiesEquipped(StringsRM.darknessBoost) * 0.1f);
					//float bonusDamage = (playerData.getStrength(true) * 0.25f) * (darkScaling);
					//event.getEntity().hurt(event.getEntity().damageSources().playerAttack(player), bonusDamage);
					if (event.getSource().type().msgId().equals("player")) { // Applies to ONLY melee
						player.heal((playerData.getStrength(true) * 0.1f) * darkScaling);
						player.getFoodData().eat(3, 10);
					}




					//System.out.println("%: " + darkScaling);
					//System.out.println("Healing: " + event.getOriginalDamage() * darkScaling);
					//System.out.println("Bonus Damage: " + bonusDamage);
				}


				// Spellblade Ability
				int fireBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.fireBoost);
				int blizBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.blizzardBoost);
				int thundBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.thunderBoost);
				int waterBoosts = playerData.getNumberOfAbilitiesEquipped(Strings.waterBoost);
				int darkBoosts = playerData.getNumberOfAbilitiesEquipped(StringsRM.darknessBoost);
				int lightBoosts = playerData.getNumberOfAbilitiesEquipped(StringsRM.lightBoost);

				// ((Base STR * 0.25) + (Base MAG * 0.25)) / 2 -- this is to make it so the boosts are more impactful.
				float dmg = (float) ((playerData.getStrengthStat().get() * 0.25f) + (float) (playerData.getMagicStat().get() * 0.25f) / 2F); //player

				if (event.getSource().type().msgId().equals("player")) { // Applies to ONLY melee

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
											// Water Blade TODO: Change KKDamageTypes.ICE to KKDamageTypes.WATER when I port to 1.21.1 NeoForge
											event.getEntity().invulnerableTime = 0;
											event.getEntity().hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.ICE, event.getEntity(), null), (float) ((waterBoosts / 2) * dmg));
											event.getEntity().setAirSupply(0);
											if (event.getEntity().getAirSupply() == 0) {
												event.getEntity().invulnerableTime = 0;
												event.getEntity().hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.ICE, event.getEntity(), null), (float) ((waterBoosts / 2) * dmg));
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


					// Xephiro Keyblade Buff - Me Exclusive
					if (playerData.getEquippedKeychain(DriveForm.NONE) != null) {
						if (playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItemsRM.xephiroKeybladeChain.get()) {
							if (event.getSource().getEntity().getUUID().toString().equals("70b48fbd-b67f-4f3e-9369-09cef36d51a3") || event.getSource().getEntity().getUUID().toString().equals("380df991-f603-344c-a090-369bad2a924a")) {

								float vamp = (float) playerData.getStrengthStat().getStat() * 0.10f;
								//System.out.println("Life Steal for " + vamp + "HP.");

								player.heal(vamp);
							}
						}
					}
				}

				if (!event.getSource().type().msgId().equals("player")) { // Applies on any damage source that ISN'T melee

					//player.sendSystemMessage(Component.literal("Damage Type: "+ event.getSource().type().msgId())); // Debugging Message

					if (playerData.isAbilityEquipped(StringsRM.lightInfusion)){
						if (!event.getSource().type().msgId().equals("light")){
							//player.sendSystemMessage(Component.literal("Light Infusion Applied!"));
							event.getEntity().hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHT, event.getEntity(), null), (((float) lightBoosts / 2) * dmg));
							}
						}
					}
				if (playerData.isAbilityEquipped(StringsRM.darkInfusion)){
					if (!event.getSource().type().msgId().equals("darkness") && !event.getSource().type().msgId().equals("explosion.player")){
						//player.sendSystemMessage(Component.literal("Light Infusion Applied!"));
						event.getEntity().hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS, event.getEntity(), null), (((float) darkBoosts / 2) * dmg));
					}
				}
				if (playerData.isAbilityEquipped(StringsRM.twilightInfusion)) {
						event.getEntity().hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHT, event.getEntity(), null), (((float) darkBoosts / 2) * dmg)/2);
						event.getEntity().invulnerableTime = 0;
						event.getEntity().hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS, event.getEntity(), null), (((float) darkBoosts / 2) * dmg)/2);
				}
			}
		}
	}

	public void hitEntity(LivingDamageEvent.Pre event){
		if(event.getEntity() instanceof Player player) {
			PlayerData playerData = PlayerData.get(player);
			if(playerData == null)
				return;

			// Light/Dark Boost downsides
			if (playerData.isAbilityEquipped(StringsRM.darknessBoost) || playerData.isAbilityEquipped(StringsRM.lightBoost)){
				float darkBoosts = playerData.getNumberOfAbilitiesEquipped(StringsRM.darknessBoost) * 0.025F;
				float lightBoosts = playerData.getNumberOfAbilitiesEquipped(StringsRM.lightBoost) * 0.025F;
				float damage = event.getNewDamage();

					/*
					System.out.println("Dark Bonus Res? "+darkBoosts);
					System.out.println("Light Bonus Res? "+lightBoosts);
					 */
				//System.out.println("Before Negation: "+damage);


				if (event.getSource().getMsgId().equals(KKResistanceType.darkness.toString())) {
					//System.out.println("Darkness");
					damage -= (damage * darkBoosts);
					if (playerData.isAbilityEquipped(StringsRM.lightBoost)){
						damage += (damage * lightBoosts);
					}
				}
				/*
				if (!event.getSource().getMsgId().equals(DamageTypes.PLAYER_EXPLOSION.toString())){
					System.out.println("Explosion");
					damage -= (damage * darkBoosts);
					if (playerData.isAbilityEquipped(StringsRM.lightBoost)){
						damage += (damage * lightBoosts);
					}
				}
				 */

				if (event.getSource().getMsgId().equals(KKResistanceType.light.toString())) {
					//System.out.println("Light");
					damage -= (damage * lightBoosts);
					if (playerData.isAbilityEquipped(StringsRM.darknessBoost)){
						damage += (damage * darkBoosts);
					}
				}
				//System.out.println("After Negation: "+damage);
				event.setNewDamage(damage);
			}




		}
	}

	// EFM Stuff Below





}