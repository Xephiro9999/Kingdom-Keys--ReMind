package online.remind.remind.newgameplus;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;

public class NewGamePlusBonuses {

    public void NGPlusBonus() {
        Player player = Minecraft.getInstance().player;
        PlayerData playerData = PlayerData.get(player);
        IGlobalDataRM globalData = ModDataRM.getGlobal(player);
        /*
        switch(globalData.getPrestigeLvl()){
            case 1:
                playerData.getStrengthStat().addModifier("NG+ Bonus",1, true);
                playerData.getMagicStat().addModifier("NG+ Bonus",1, true);
                playerData.getDefenseStat().addModifier("NG+ Bonus",1, true);
                playerData.addAbility(Strings.luckyLucky, true);
                PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
                System.out.println(globalData.getPrestigeLvl());
                PacketHandlerX.syncGlobalToAllAround(player, globalData);
            case 2:
                playerData.getStrengthStat().addModifier("NG+ Bonus",2, true);
                playerData.getMagicStat().addModifier("NG+ Bonus",2, true);
                playerData.getDefenseStat().addModifier("NG+ Bonus",2, true);
                playerData.addAbility(Strings.luckyLucky, true);
                playerData.addAbility(Strings.experienceBoost, true);
                PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
                System.out.println(globalData.getPrestigeLvl());
                PacketHandlerX.syncGlobalToAllAround(player, globalData);
            case 3:
                playerData.getStrengthStat().addModifier("NG+ Bonus",3, true);
                playerData.getMagicStat().addModifier("NG+ Bonus",3, true);
                playerData.getDefenseStat().addModifier("NG+ Bonus",3, true);
                playerData.addAbility(Strings.luckyLucky, true);
                playerData.addAbility(Strings.experienceBoost, true);

                PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
                System.out.println(globalData.getPrestigeLvl());
                PacketHandlerX.syncGlobalToAllAround(player, globalData);
        }
        */
        

    }





}
