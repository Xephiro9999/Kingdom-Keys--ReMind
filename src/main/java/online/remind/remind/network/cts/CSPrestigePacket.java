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
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.function.Supplier;
import java.util.Map.Entry;

public class CSPrestigePacket implements CustomPacketPayload {
    public static final Type<CSPrestigePacket> TYPE = new Type(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_prestige"));
    public static final StreamCodec<FriendlyByteBuf, CSPrestigePacket> STREAM_CODEC = StreamCodec.of(CSPrestigePacket::encode, CSPrestigePacket::decode);

    public CSPrestigePacket(){}

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
            IGlobalDataRM globalData = ModDataRM.getGlobal(player);

            // Storing Old Choice For Bonus
            String oldChoice = String.valueOf(playerData.getChosen());
            System.out.println(oldChoice);

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
            SoAState.applyStatsForChoices(player, playerData, false);

            playerData.setEquippedShotlock("");

            LinkedHashMap<String, int[]> driveForms = playerData.getDriveFormMap();
            Iterator<Entry<String, int[]>> it = driveForms.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, int[]> entry = it.next();
                int dfLevel = entry.getValue()[0];
                DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(entry.getKey()));
                if (!form.getRegistryName().equals(DriveForm.NONE) && !form.getRegistryName().equals(DriveForm.SYNCH_BLADE)) {
                    for (int i = 1; i <= dfLevel; i++) {
                        String baseAbility = form.getBaseAbilityForLevel(i);
                        if (baseAbility != null && !baseAbility.equals("")) {
                            playerData.addAbility(baseAbility, false);
                        }
                    }
                }
            }
            System.out.println("Drive Form Abilities Added!");



            //PacketHandler.sendToServer(new CSEquipAccessories());
            if (playerData.getEquippedAccessories().size() <= Utils.getFreeSlotsForPlayer(player)){
                playerData.getEquippedAccessories().forEach((integer, itemStack) -> {
                    ItemStack unequippedAccessory = playerData.equipAccessory(integer, ItemStack.EMPTY);
                    if (!unequippedAccessory.isEmpty()){
                        player.addItem(unequippedAccessory);
                    }
                });
            }
            System.out.println("Accessories Yeet'd into Inventory");

            //PacketHandler.sendToServer(new CSEquipArmor());
            // Armor
            if (playerData.getEquippedArmors().size() <= Utils.getFreeSlotsForPlayer(player)){
                playerData.getEquippedArmors().forEach((integer, itemStack) -> {
                    ItemStack unequippedArmor = playerData.equipArmor(integer, ItemStack.EMPTY);
                    if (!unequippedArmor.isEmpty()){
                        player.addItem(unequippedArmor);
                    }
                });
            }
            System.out.println("Armor Yeet'd into Inventory");


            playerData.setSoAState(SoAState.NONE);
            globalData.addPrestigeLvl(+1);

            if (oldChoice == "WARRIOR") {
                globalData.addNGPWarriorCount(+1);
                globalData.addSTRBonus(+2);
                if (globalData.getSTRBonus() > 50){
                    globalData.setSTRBonus(50);
                }
                System.out.println("Strength Bonus: " + globalData.getSTRBonus());
                PacketHandlerRM.syncGlobalToAllAround(player, globalData);
            }

            if (oldChoice == "MYSTIC") {
                globalData.addNGPMysticCount(+1);
                globalData.addMAGBonus(+2);
                if (globalData.getMAGBonus() > 50){
                    globalData.setMAGBonus(50);
                }
                System.out.println("Magic Bonus: " + globalData.getMAGBonus());
                PacketHandlerRM.syncGlobalToAllAround(player, globalData);
            }

            if (oldChoice == "GUARDIAN") {
                globalData.addNGPGuardianCount(+1);
                globalData.addDEFBonus(+2);
                if (globalData.getDEFBonus() > 50){
                    globalData.setDEFBonus(50);
                }
                System.out.println("Defense Bonus: " + globalData.getDEFBonus());
                PacketHandlerRM.syncGlobalToAllAround(player, globalData);
            }


            System.out.println("NG+ Counts: " + globalData.getNGPWarriorCount() + ", " + globalData.getNGPMysticCount() + ", " + globalData.getNGPGuardianCount());
            System.out.println("Bonus Stats: " + globalData.getSTRBonus() + ", " + globalData.getMAGBonus() + ", " + globalData.getDEFBonus());

            playerData.getStrengthStat().removeModifier("NG+ Bonus");
            playerData.getMagicStat().removeModifier("NG+ Bonus");
            playerData.getDefenseStat().removeModifier("NG+ Bonus");
            playerData.getStrengthStat().removeModifier("sacrifice");
            playerData.getMagicStat().removeModifier("sacrifice");
            playerData.getDefenseStat().removeModifier("sacrifice");


            playerData.getStrengthStat().addModifier("NG+ Bonus", globalData.getSTRBonus(), true, false);
            playerData.getMagicStat().addModifier("NG+ Bonus", globalData.getMAGBonus(), true, false);
            playerData.getDefenseStat().addModifier("NG+ Bonus", globalData.getDEFBonus(), true, false);
            playerData.addMaxHP(2 * globalData.getPrestigeLvl());
            playerData.addMaxMP(2 * globalData.getPrestigeLvl());
            //player.heal(playerData.getMaxHP());
            playerData.setMP(playerData.getMaxMP());

            // NG+ Bonus Abilities

            playerData.addAbility(Strings.experienceBoost, true);
            playerData.addAbility(Strings.luckyLucky, true);
            playerData.addAbility(StringsRM.dedication, true);


            switch (globalData.getNGPWarriorCount()){
                case 0:
                    break;
                case 1:
                    playerData.addAbility(Strings.synchBlade, true);
                    break;
                case 2:
                    playerData.addAbility(Strings.synchBlade, true);
                    playerData.addAbility(Strings.formBoost, true);
                    break;
                case 3:
                    playerData.addAbility(Strings.criticalBoost, true);
                    playerData.addAbility(Strings.synchBlade, true);
                    playerData.addAbility(Strings.formBoost, true);
                    break;
                case 4:
                    playerData.addAbility(Strings.criticalBoost, true);
                    playerData.addAbility(Strings.synchBlade, true);
                    playerData.addAbility(Strings.formBoost, true);
                    playerData.addAbility(Strings.driveBoost, true);
                    break;
                case 5:
                    playerData.addAbility(StringsRM.attackHaste, true);
                    playerData.addAbility(Strings.criticalBoost, true);
                    playerData.addAbility(Strings.synchBlade, true);
                    playerData.addAbility(Strings.formBoost, true);
                    playerData.addAbility(Strings.driveBoost, true);
                    break;
                case 6:
                    playerData.addAbility(Strings.synchBlade, true);
                    playerData.addAbility(StringsRM.attackHaste, true);
                    playerData.addAbility(Strings.criticalBoost, true);
                    playerData.addAbility(Strings.criticalBoost, true);
                    playerData.addAbility(Strings.formBoost, true);
                    playerData.addAbility(Strings.driveBoost, true);
                    break;
                default:
                    playerData.addAbility(Strings.synchBlade, true);
                    playerData.addAbility(StringsRM.attackHaste, true);
                    playerData.addAbility(Strings.criticalBoost, true);
                    playerData.addAbility(Strings.criticalBoost, true);
                    playerData.addAbility(Strings.formBoost, true);
                    playerData.addAbility(Strings.driveBoost, true);
                    globalData.addSTRBonus(+1);
                    playerData.addMaxAP(2);
                    break;

            }

            switch (globalData.getNGPMysticCount()){
                case 0:
                    break;
                case 1:
                    playerData.addAbility(StringsRM.critical_surge, true);
                    break;
                case 2:
                    playerData.addAbility(StringsRM.critical_surge, true);
                    playerData.addAbility(Strings.mpHastega, true);
                    break;
                case 3:
                    playerData.addAbility(StringsRM.critical_surge, true);
                    playerData.addAbility(Strings.mpHastega, true);
                    playerData.addAbility(Strings.mpThrift, true);
                    break;
                case 4:
                    playerData.addAbility(StringsRM.critical_surge, true);
                    playerData.addAbility(Strings.mpHastega, true);
                    playerData.addAbility(Strings.mpThrift, true);
                    playerData.addAbility(Strings.grandMagicHaste, true);
                    break;
                case 5:
                    playerData.addAbility(StringsRM.critical_surge, true);
                    playerData.addAbility(Strings.mpHastega, true);
                    playerData.addAbility(Strings.mpThrift, true);
                    playerData.addAbility(Strings.grandMagicHaste, true);
                    playerData.addAbility(StringsRM.mpBoost, true);
                    break;
                case 6:
                    playerData.addAbility(StringsRM.critical_surge, true);
                    playerData.addAbility(Strings.mpHastega, true);
                    playerData.addAbility(Strings.mpThrift, true);
                    playerData.addAbility(Strings.grandMagicHaste, true);
                    playerData.addAbility(StringsRM.mpBoost, true);
                    playerData.addAbility(StringsRM.mpShield, true);
                    break;
                default:
                    playerData.addAbility(StringsRM.critical_surge, true);
                    playerData.addAbility(Strings.mpHastega, true);
                    playerData.addAbility(Strings.mpThrift, true);
                    playerData.addAbility(Strings.grandMagicHaste, true);
                    playerData.addAbility(StringsRM.mpBoost, true);
                    playerData.addAbility(StringsRM.mpShield, true);
                    globalData.addMAGBonus(+1);
                    playerData.addMaxAP(2);
                    break;
            }

            switch (globalData.getNGPGuardianCount()){
                case 0:
                    break;
                case 1:
                    playerData.addAbility(Strings.damageControl, true);
                    break;
                case 2:
                    playerData.addAbility(Strings.damageControl, true);
                    playerData.addAbility(Strings.damageDrive, true);
                    break;
                case 3:
                    playerData.addAbility(Strings.damageControl, true);
                    playerData.addAbility(Strings.damageDrive, true);
                    playerData.addAbility(StringsRM.mpWalker, true);
                    break;
                case 4:
                    playerData.addAbility(Strings.damageControl, true);
                    playerData.addAbility(Strings.damageDrive, true);
                    playerData.addAbility(StringsRM.mpWalker, true);
                    playerData.addAbility(StringsRM.hpWalker, true);
                    break;
                case 5:
                    playerData.addAbility(Strings.damageControl, true);
                    playerData.addAbility(Strings.damageDrive, true);
                    playerData.addAbility(StringsRM.mpWalker, true);
                    playerData.addAbility(StringsRM.hpWalker, true);
                    playerData.addAbility(StringsRM.hpBoost, true);
                    break;
                case 6:
                    playerData.addAbility(Strings.damageControl, true);
                    playerData.addAbility(Strings.damageDrive, true);
                    playerData.addAbility(StringsRM.mpWalker, true);
                    playerData.addAbility(StringsRM.hpWalker, true);
                    playerData.addAbility(StringsRM.hpBoost, true);
                    playerData.addAbility(Strings.protect, true);
                    break;
                default:
                    playerData.addAbility(Strings.damageControl, true);
                    playerData.addAbility(Strings.damageDrive, true);
                    playerData.addAbility(StringsRM.mpWalker, true);
                    playerData.addAbility(StringsRM.hpWalker, true);
                    playerData.addAbility(StringsRM.hpBoost, true);
                    playerData.addAbility(Strings.protect, true);
                    globalData.addDEFBonus(+1);
                    playerData.addMaxAP(2);
                    break;
            }

            // Make sure the cap is in place.
            if(globalData.getSTRBonus() > 50){
                globalData.setSTRBonus(50);
            }
            if(globalData.getMAGBonus() > 50){
                globalData.setMAGBonus(50);
            }
            if(globalData.getDEFBonus() > 50){
                globalData.setDEFBonus(50);
            }

            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
            //System.out.println("Prestige Level: " + globalData.getPrestigeLvl());
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
