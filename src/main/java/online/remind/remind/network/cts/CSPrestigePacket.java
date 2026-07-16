package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.ability.ModAbilitiesRM;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class CSPrestigePacket implements CustomPacketPayload {
    public static final Type<CSPrestigePacket> TYPE = new Type(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_prestige"));
    public static final StreamCodec<FriendlyByteBuf, CSPrestigePacket> STREAM_CODEC = StreamCodec.of(CSPrestigePacket::encode, CSPrestigePacket::decode);

    public CSPrestigePacket() {
    }

    public static void encode(FriendlyByteBuf buffer, CSPrestigePacket message) {

    }

    public static CSPrestigePacket decode(FriendlyByteBuf buffer) {
        CSPrestigePacket msg = new CSPrestigePacket();

        return msg;
    }

    public static void handle(final CSPrestigePacket message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();

            PlayerData playerData = PlayerData.get(player);
            GlobalDataRM globalData = ModDataRM.getGlobal(player);

            // Storing Old Choice For Bonus
            String oldChoice = String.valueOf(playerData.getChosen());

            // Until Arclight Fix is Found
            playerData.setLevel(1);
            playerData.setExperience(0);
            playerData.setMaxHP(20);
            player.setHealth(playerData.getMaxHP());
            player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(playerData.getMaxHP());
            playerData.setMaxMP(0);
            playerData.setMP(playerData.getMaxMP());
            playerData.setStrength(1);
            playerData.setMagic(1);
            playerData.setDefense(1);
            playerData.setMaxAP(0);
            playerData.setMaxAccessories(0);
            playerData.setMaxArmors(0);

            playerData.clearAbilities();
            playerData.setEquippedShotlock(null);

            playerData.setSoAState(SoAState.NONE);
            globalData.addPrestigeLvl(1);

            LinkedHashMap<ResourceLocation, int[]> driveForms = playerData.getDriveFormMap();
	        for (Entry<ResourceLocation, int[]> entry : driveForms.entrySet()) {
		        int dfLevel = entry.getValue()[0];
		        DriveForm form = ModDriveForms.registry.get(entry.getKey());
		        if (!form.getRegistryName().equals(DriveForm.NONE) && !form.getRegistryName().equals(DriveForm.SYNCH_BLADE)) {
			        for (int i = 1; i <= dfLevel; i++) {
				        form.getBaseAbilityForLevel(i).ifPresent(baseAbility -> {
                            playerData.addAbility(baseAbility, false);
                        });
			        }
		        }
	        }
            System.out.println("Drive Form Abilities Added!");

            //PacketHandler.sendToServer(new CSEquipAccessories());
            if (playerData.getEquippedAccessories().size() <= Utils.getFreeSlotsForPlayer(player)) {
                playerData.getEquippedAccessories().forEach((integer, itemStack) -> {
                    ItemStack unequippedAccessory = playerData.equipAccessory(integer, ItemStack.EMPTY);
                    if (!unequippedAccessory.isEmpty()) {
                        player.addItem(unequippedAccessory);
                    }
                });
            }
            System.out.println("Accessories Yeet'd into Inventory");

            //PacketHandler.sendToServer(new CSEquipArmor());
            // Armor
            if (playerData.getEquippedArmors().size() <= Utils.getFreeSlotsForPlayer(player)) {
                playerData.getEquippedArmors().forEach((integer, itemStack) -> {
                    ItemStack unequippedArmor = playerData.equipArmor(integer, ItemStack.EMPTY);
                    if (!unequippedArmor.isEmpty()) {
                        player.addItem(unequippedArmor);
                    }
                });
            }
            System.out.println("Armor Yeet'd into Inventory");

            //Utils.restartLevel(playerData, player);
            //Utils.restartLevel2(playerData, player); // Keep this for Drive Bonuses

            switch(oldChoice) {
                case "WARRIOR" -> {
                    globalData.addNGPWarriorCount(1);
                    globalData.addSTRBonus(ModConfigs.statBonus);
                    if (globalData.getSTRBonus() > ModConfigs.statCap) {
                        globalData.setSTRBonus(ModConfigs.statCap);
                    }
                    System.out.println("Strength Bonus: " + globalData.getSTRBonus());
                }
                case "MYSTIC" -> {
                    globalData.addNGPMysticCount(1);
                    globalData.addMAGBonus(ModConfigs.statBonus);
                    if (globalData.getMAGBonus() > ModConfigs.statCap) {
                        globalData.setMAGBonus(ModConfigs.statCap);
                    }
                    System.out.println("Magic Bonus: " + globalData.getMAGBonus());
                }
                case "GUARDIAN" -> {
                    globalData.addNGPGuardianCount(1);
                    globalData.addDEFBonus(ModConfigs.statBonus);
                    if (globalData.getDEFBonus() > ModConfigs.statCap) {
                        globalData.setDEFBonus(ModConfigs.statCap);
                    }
                    System.out.println("Defense Bonus: " + globalData.getDEFBonus());
                }
            }
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);

            System.out.println("NG+ Counts: " + globalData.getNGPWarriorCount() + ", " + globalData.getNGPMysticCount() + ", " + globalData.getNGPGuardianCount());
            System.out.println("Bonus Stats: " + globalData.getSTRBonus() + ", " + globalData.getMAGBonus() + ", " + globalData.getDEFBonus());

            //player.heal(playerData.getMaxHP()); // <--- Arclight still hates this
            playerData.setMP(playerData.getMaxMP());

            //Since we cleared abilities earlier we iterate through all the perma abilities and add em back (Utils#restartLevel2 already does this, so if you reimplement it removing it from here should be safe)
            playerData.getPAbilitiesList().forEach(a -> {
                playerData.addAbility(a,false);
            });

            // NG+ Bonus Abilities
            playerData.addPAbility(ModAbilities.EXPERIENCE_BOOST.location());
            playerData.addPAbility(ModAbilities.LUCKY_STRIKE.location());
            playerData.addPAbility(ModAbilitiesRM.DEDICATION.location());

            switch (globalData.getNGPWarriorCount()) {
                case 1 -> playerData.addPAbility(ModAbilities.SYNCH_BLADE.location());
                case 2 -> playerData.addPAbility(ModAbilities.FORM_BOOST.location());
                case 3 -> playerData.addPAbility(ModAbilities.CRITICAL_BOOST.location());
                case 4 -> playerData.addPAbility(ModAbilities.DRIVE_BOOST.location());
                case 5 -> playerData.addPAbility(ModAbilitiesRM.ATTACK_HASTE.location());
                case 6 -> playerData.addPAbility(ModAbilities.CRITICAL_BOOST.location());
            }

            switch (globalData.getNGPMysticCount()) {
                case 1 -> playerData.addPAbility(ModAbilitiesRM.CRITICAL_SURGE.location());
                case 2 -> playerData.addPAbility(ModAbilities.MP_HASTEGA.location());
                case 3 -> playerData.addPAbility(ModAbilities.MP_THRIFT.location());
                case 4 -> playerData.addPAbility(ModAbilities.GRAND_MAGIC_HASTE.location());
                case 5 -> playerData.addPAbility(ModAbilitiesRM.MP_BOOST.location());
                case 6 -> playerData.addPAbility(ModAbilitiesRM.MP_SHIELD.location());
            }

            switch (globalData.getNGPGuardianCount()) {
                case 1 -> playerData.addPAbility(ModAbilities.DAMAGE_CONTROL.location());
                case 2 -> playerData.addPAbility(ModAbilities.DAMAGE_DRIVE.location());
                case 3 -> playerData.addPAbility(ModAbilitiesRM.MP_WALKER.location());
                case 4 -> playerData.addPAbility(ModAbilitiesRM.HP_WALKER.location());
                case 5 -> playerData.addPAbility(ModAbilitiesRM.HP_BOOST.location());
                case 6 -> playerData.addPAbility(ModAbilities.PROTECT.location());
            }


            // Make sure the cap is in place.
        /*
        if(globalData.getSTRBonus() > ModConfigs.statCap){
            globalData.setSTRBonus(ModConfigs.statCap);
        }
        if(globalData.getMAGBonus() > ModConfigs.statCap){
            globalData.setMAGBonus(ModConfigs.statCap);
        }
        if(globalData.getDEFBonus() > ModConfigs.statCap){
            globalData.setDEFBonus(ModConfigs.statCap);
        }
         */

            playerData.getStrengthStat().removeModifier("NG+ Bonus");
            playerData.getMagicStat().removeModifier("NG+ Bonus");
            playerData.getDefenseStat().removeModifier("NG+ Bonus");
            playerData.getStrengthStat().removeModifier("sacrifice");
            playerData.getMagicStat().removeModifier("sacrifice");
            playerData.getDefenseStat().removeModifier("sacrifice");

            if (globalData.getNGPEnabled() == 1) {
	            playerData.getStrengthStat().addModifier("NG+ Bonus", Math.min(globalData.getSTRBonus(), ModConfigs.statCap), true, false);
	            playerData.getMagicStat().addModifier("NG+ Bonus", Math.min(globalData.getMAGBonus(), ModConfigs.statCap), true, false);
	            playerData.getDefenseStat().addModifier("NG+ Bonus", Math.min(globalData.getDEFBonus(), ModConfigs.statCap), true, false);
            }

            int addedHP = 2 * globalData.getPrestigeLvl();
            int addedMP = 2 * globalData.getPrestigeLvl();

	        playerData.addMaxHP(Math.min(addedHP, ModConfigs.hpCap));
	        playerData.addMaxMP(Math.min(addedMP, ModConfigs.mpCap));

            //System.out.println("Prestige Level: " + globalData.getPrestigeLvl());
            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);

            System.out.println("Perma abilities kept: "+playerData.getPAbilitiesList());
            });
        }

        @Override
        public Type<? extends CustomPacketPayload> type () {
            return TYPE;
    }
}
