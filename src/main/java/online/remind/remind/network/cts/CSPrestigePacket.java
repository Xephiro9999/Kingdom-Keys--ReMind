package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.function.Supplier;

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
        SoAState.applyStatsForChoices(player, playerData, false);

        playerData.setEquippedShotlock("");

        //Utils.restartLevel(playerData, player);
        //Utils.restartLevel2(playerData, player);


        playerData.setSoAState(SoAState.NONE);
        globalData.addPrestigeLvl(+1);

        if (oldChoice == "WARRIOR"){
            globalData.addNGPWarriorCount(+1);
            globalData.addSTRBonus(+2);
            System.out.println("Strength Bonus: " + globalData.getSTRBonus());
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        }
        
        if (oldChoice == "MYSTIC"){
            globalData.addNGPMysticCount(+1);
            globalData.addMAGBonus(+2);
            System.out.println("Magic Bonus: " + globalData.getMAGBonus());
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        }
        
        if (oldChoice == "GUARDIAN"){
            globalData.addNGPGuardianCount(+1);
            globalData.addDEFBonus(+2);
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
        playerData.getMagicStat().addModifier("NG+ Bonus",globalData.getMAGBonus(), true, false);
        playerData.getDefenseStat().addModifier("NG+ Bonus",globalData.getDEFBonus(), true, false);
        playerData.addMaxHP(2 * globalData.getPrestigeLvl());
        playerData.addMaxMP(2 * globalData.getPrestigeLvl());

        // NG+ Bonus Abilities

        playerData.addAbility(Strings.experienceBoost, true);
        playerData.addAbility(Strings.luckyLucky, true);
        playerData.addAbility(StringsRM.dedication, true);


        if (globalData.getNGPWarriorCount() >= 1) {
            playerData.addAbility(StringsRM.adrenaline, true);
            if (globalData.getNGPWarriorCount() >= 2) {
                playerData.addAbility(Strings.formBoost, true);
            }
            if (globalData.getNGPWarriorCount() >= 3) {
                playerData.addAbility(Strings.criticalBoost, true);
            }
            if (globalData.getNGPWarriorCount() >= 4) {
                playerData.addAbility(Strings.driveBoost, true);
            }
            if (globalData.getNGPWarriorCount() >= 5){
                playerData.addAbility(StringsRM.attackHaste, true);
            }
        }

        if (globalData.getNGPMysticCount() >= 1) {
            playerData.addAbility(StringsRM.critical_surge, true);
            if (globalData.getNGPMysticCount() >= 2) {
                playerData.addAbility(Strings.mpHastega, true);
            }
            if (globalData.getNGPMysticCount() >= 3) {
                playerData.addAbility(Strings.mpThrift, true);
            }
            if (globalData.getNGPMysticCount() >= 4){
                playerData.addAbility(Strings.grandMagicHaste, true);
            }
            if (globalData.getNGPMysticCount() >= 5){
                playerData.addAbility(StringsRM.mpBoost, true);
            }
        }

        if (globalData.getNGPGuardianCount() >= 1) {
            playerData.addAbility(Strings.damageControl, true);
            if (globalData.getNGPGuardianCount() >= 2) {
                playerData.addAbility(Strings.damageDrive, true);
            }
            if (globalData.getNGPGuardianCount() >= 3) {
                playerData.addAbility(StringsRM.mpWalker, true);
            }
            if (globalData.getNGPGuardianCount() >= 4){
                playerData.addAbility(StringsRM.hpWalker, true);
            }
            if (globalData.getNGPGuardianCount() >= 5){
                playerData.addAbility(StringsRM.hpBoost, true);
            }
        }

        PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
        //System.out.println("Prestige Level: " + globalData.getPrestigeLvl());
        PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        ctx.get().setPacketHandled(true);
    }
}
