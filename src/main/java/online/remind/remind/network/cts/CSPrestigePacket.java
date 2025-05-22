package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
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

            //Utils.restartLevel(playerData, player);
            Utils.restartLevel2(playerData, player);


            playerData.setSoAState(SoAState.NONE);
            globalData.addPrestigeLvl(+1);

            if (oldChoice == "WARRIOR") {
                globalData.addNGPWarriorCount(+1);
                globalData.addSTRBonus(+2);
                System.out.println("Strength Bonus: " + globalData.getSTRBonus());
                PacketHandlerRM.syncGlobalToAllAround(player, globalData);
            }

            if (oldChoice == "MYSTIC") {
                globalData.addNGPMysticCount(+1);
                globalData.addMAGBonus(+2);
                System.out.println("Magic Bonus: " + globalData.getMAGBonus());
                PacketHandlerRM.syncGlobalToAllAround(player, globalData);
            }

            if (oldChoice == "GUARDIAN") {
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
            playerData.getMagicStat().addModifier("NG+ Bonus", globalData.getMAGBonus(), true, false);
            playerData.getDefenseStat().addModifier("NG+ Bonus", globalData.getDEFBonus(), true, false);
            playerData.addMaxHP(2 * globalData.getPrestigeLvl());
            playerData.addMaxMP(2 * globalData.getPrestigeLvl());
            player.heal(playerData.getMaxHP());
            playerData.setMP(playerData.getMaxMP());

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
                if (globalData.getNGPWarriorCount() >= 5) {
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
                if (globalData.getNGPMysticCount() >= 4) {
                    playerData.addAbility(Strings.grandMagicHaste, true);
                }
                if (globalData.getNGPMysticCount() >= 5) {
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
                if (globalData.getNGPGuardianCount() >= 4) {
                    playerData.addAbility(StringsRM.hpWalker, true);
                }
                if (globalData.getNGPGuardianCount() >= 5) {
                    playerData.addAbility(StringsRM.hpBoost, true);
                }
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
