package online.remind.remind.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import org.apache.logging.log4j.core.jmx.Server;

public class NGPlusCommand extends AddonCommand{ // remind ng+ <path> <amount> <player>



    public static SuggestionProvider<CommandSourceStack> SUGGEST_CHOICES = (p_198296_0_, p_198296_1_) -> {
        List<String> list = Arrays.asList("warrior", "guardian", "mystic", "all");
        try{
            String chosen = StringArgumentType.getString(p_198296_0_, "chosen");
            if (list.contains(chosen)) {
                return SharedSuggestionProvider.suggest(list.stream().filter(s -> !s.equals(chosen)).map(StringArgumentType::escapeIfRequired), p_198296_1_);
            }
        } catch (IllegalArgumentException ignored) {}
        return SharedSuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
    };



    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("ng+Set").requires(source -> source.hasPermission(2));
        builder.then(Commands.argument("chosen", StringArgumentType.string()).suggests(SUGGEST_CHOICES).then(Commands.argument("amount", IntegerArgumentType.integer(0)).then(Commands.argument("targets", EntityArgument.players()).executes(NGPlusCommand::setNGPlus))));

        return builder;
    }

    public static int setNGPlus(CommandContext<CommandSourceStack> context) throws CommandSyntaxException{

        Collection<ServerPlayer> players = getPlayers(context, 5);
        String chosenOpt = StringArgumentType.getString(context, "chosen");
        int amount = IntegerArgumentType.getInteger(context, "amount");



        for (ServerPlayer player : players){
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            IGlobalCapabilitiesRM globalData = ModCapabilitiesRM.getGlobal(player);

            int addedHP = globalData.getPrestigeLvl() * 2;
            int addedMP = globalData.getPrestigeLvl() * 2;

            if (addedHP > ModConfigs.hpCap){
                addedHP = ModConfigs.hpCap;
            }
            if (addedMP > ModConfigs.mpCap){
                addedMP = ModConfigs.mpCap;
            }

            switch(chosenOpt){
               case "warrior" -> {

                   // HP/MP Adjust (Removal)
                   playerData.addMaxHP(-addedHP);
                   playerData.addMaxMP(-addedMP);

                   globalData.setNGPWarriorCount(amount);
                   globalData.setSTRBonus(amount * ModConfigs.statBonus);
                   playerData.getStrengthStat().removeModifier("NG+ Bonus");
                   playerData.getStrengthStat().addModifier("NG+ Bonus", globalData.getSTRBonus(), true, false);
                   globalData.setPrestigeLvl(globalData.getNGPWarriorCount() + globalData.getNGPMysticCount() + globalData.getNGPGuardianCount());

                   playerData.addMaxHP(globalData.getPrestigeLvl() * 2);
                   playerData.addMaxMP(globalData.getPrestigeLvl() * 2);
               }
               case "mystic" -> {
                   playerData.addMaxHP(-addedHP);
                   playerData.addMaxMP(-addedMP);

                   globalData.setNGPMysticCount(amount);
                   globalData.setMAGBonus(amount * ModConfigs.statBonus);
                   playerData.getMagicStat().removeModifier("NG+ Bonus");
                   playerData.getMagicStat().addModifier("NG+ Bonus", globalData.getMAGBonus(), true, false);
                   globalData.setPrestigeLvl(globalData.getNGPWarriorCount() + globalData.getNGPMysticCount() + globalData.getNGPGuardianCount());
                   playerData.addMaxHP(globalData.getPrestigeLvl() * 2);
               }
               case "guardian" -> {
                   playerData.addMaxHP(-addedHP);
                   playerData.addMaxMP(-addedMP);

                   globalData.setNGPGuardianCount(amount);
                   globalData.setDEFBonus(amount * ModConfigs.statBonus);
                   playerData.getDefenseStat().removeModifier("NG+ Bonus");
                   playerData.getDefenseStat().addModifier("NG+ Bonus", globalData.getDEFBonus(), true, false);
                   globalData.setPrestigeLvl(globalData.getNGPWarriorCount() + globalData.getNGPMysticCount() + globalData.getNGPGuardianCount());
                   playerData.addMaxHP(globalData.getPrestigeLvl() * 2);
                   playerData.addMaxMP(globalData.getPrestigeLvl() * 2);


               }
               case "all" -> {
                   playerData.addMaxHP(-addedHP);
                   playerData.addMaxMP(-addedMP);

                   globalData.setNGPWarriorCount(amount);
                   globalData.setNGPMysticCount(amount);
                   globalData.setNGPGuardianCount(amount);
                   globalData.setSTRBonus(amount * ModConfigs.statBonus);
                   globalData.setMAGBonus(amount * ModConfigs.statBonus);
                   globalData.setDEFBonus(amount * ModConfigs.statBonus);
                   playerData.getStrengthStat().removeModifier("NG+ Bonus");
                   playerData.getMagicStat().removeModifier("NG+ Bonus");
                   playerData.getDefenseStat().removeModifier("NG+ Bonus");
                   if (globalData.getSTRBonus() > ModConfigs.statCap){
                       globalData.setSTRBonus(ModConfigs.statCap);
                   }
                   if (globalData.getMAGBonus() > ModConfigs.statCap){
                       globalData.setMAGBonus(ModConfigs.statCap);
                   }
                   if (globalData.getDEFBonus() > ModConfigs.statCap){
                       globalData.setDEFBonus(ModConfigs.statCap);
                   }
                   playerData.getStrengthStat().addModifier("NG+ Bonus", globalData.getSTRBonus(), true, false);
                   playerData.getMagicStat().addModifier("NG+ Bonus", globalData.getMAGBonus(), true, false);
                   playerData.getDefenseStat().addModifier("NG+ Bonus", globalData.getDEFBonus(), true, false);
                   globalData.setPrestigeLvl(globalData.getNGPWarriorCount() + globalData.getNGPMysticCount() + globalData.getNGPGuardianCount());

               }
            }
            addedHP = globalData.getPrestigeLvl() * 2;
            addedMP = globalData.getPrestigeLvl() * 2;

            if(addedHP > ModConfigs.hpCap){
                playerData.addMaxHP(ModConfigs.hpCap);
            } else {
                playerData.addMaxHP(addedHP);
            }
            if(addedMP > ModConfigs.mpCap){
                playerData.addMaxMP(ModConfigs.mpCap);
            } else {
                playerData.addMaxMP(addedMP);
            }

            // NG+ Abilities

            switch (globalData.getNGPWarriorCount()) {
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
                    break;
            }



            PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);
        }
        return 0;
    }

}
