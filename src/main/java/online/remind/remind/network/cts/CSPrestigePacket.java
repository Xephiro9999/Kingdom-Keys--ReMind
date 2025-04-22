package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.function.Supplier;
import java.util.Map.Entry;

public class CSPrestigePacket {



    public CSPrestigePacket(){}

    public void encode(FriendlyByteBuf buffer) {

    }

    public static CSPrestigePacket decode(FriendlyByteBuf buffer) {
        CSPrestigePacket msg = new CSPrestigePacket();

        return msg;
    }

    public static void handle(final CSPrestigePacket message, Supplier<NetworkEvent.Context> ctx) {
        Player player = ctx.get().getSender();

        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        IGlobalCapabilitiesRM globalData = ModCapabilitiesRM.getGlobal(player);

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
        playerData.setEquippedShotlock("");

        LinkedHashMap<String, int[]> driveForms = playerData.getDriveFormMap();
        Iterator<Entry<String, int[]>> it = driveForms.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, int[]> entry = it.next();
            int dfLevel = entry.getValue()[0];
            DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(entry.getKey()));
            if (!form.getRegistryName().equals(DriveForm.NONE) && !form.getRegistryName().equals(DriveForm.SYNCH_BLADE)) {
                for (int i = 1; i <= dfLevel; i++) {
                    String baseAbility = form.getBaseAbilityForLevel(i);
                    if (baseAbility != null && !baseAbility.equals("")) {
                        playerData.addAbility(baseAbility, false);
                    }
                }
            }
        }


        //Utils.restartLevel(playerData, player);
        //Utils.restartLevel2(playerData, player); // Keep this for Drive Bonuses

        playerData.setSoAState(SoAState.NONE);
        globalData.addPrestigeLvl(+1);

        if (oldChoice == "WARRIOR"){
            globalData.addNGPWarriorCount(+1);
            globalData.addSTRBonus(+2);
            if (globalData.getSTRBonus() > 50){
                globalData.setSTRBonus(50);
            }
            System.out.println("Strength Bonus: " + globalData.getSTRBonus());
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        }
        
        if (oldChoice == "MYSTIC"){
            globalData.addNGPMysticCount(+1);
            globalData.addMAGBonus(+2);
            if (globalData.getMAGBonus() > 50){
                globalData.setMAGBonus(50);
            }
            System.out.println("Magic Bonus: " + globalData.getMAGBonus());
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        }
        
        if (oldChoice == "GUARDIAN"){
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


        //player.heal(playerData.getMaxHP()); // <--- Arclight still hates this
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

        playerData.getStrengthStat().removeModifier("NG+ Bonus");
        playerData.getMagicStat().removeModifier("NG+ Bonus");
        playerData.getDefenseStat().removeModifier("NG+ Bonus");
        playerData.getStrengthStat().removeModifier("sacrifice");
        playerData.getMagicStat().removeModifier("sacrifice");
        playerData.getDefenseStat().removeModifier("sacrifice");


        playerData.getStrengthStat().addModifier("NG+ Bonus", globalData.getSTRBonus(), true, false);
        playerData.getMagicStat().addModifier("NG+ Bonus",globalData.getMAGBonus(), true, false);
        playerData.getDefenseStat().addModifier("NG+ Bonus",globalData.getDEFBonus(), true, false);
        playerData.addMaxHP(2 * globalData.getPrestigeLvl());
        playerData.addMaxMP(2 * globalData.getPrestigeLvl());







        PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
        //System.out.println("Prestige Level: " + globalData.getPrestigeLvl());
        PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        ctx.get().setPacketHandled(true);
    }
}
