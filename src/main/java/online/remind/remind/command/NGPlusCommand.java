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
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.config.ModConfigs;
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
            switch(chosenOpt){
               case "warrior" -> {
                   globalData.setNGPWarriorCount(amount);
                   globalData.setSTRBonus(amount * ModConfigs.statCap);
                   playerData.getStrengthStat().addModifier("NG+ Bonus", globalData.getSTRBonus(), true, false);
                   globalData.setPrestigeLvl(globalData.getNGPWarriorCount() + globalData.getNGPMysticCount() + globalData.getNGPGuardianCount());
               }
               case "mystic" -> {
                   globalData.setNGPMysticCount(amount);
                   globalData.setMAGBonus(amount * ModConfigs.statCap);
                   playerData.getMagicStat().addModifier("NG+ Bonus", globalData.getMAGBonus(), true, false);
                   globalData.setPrestigeLvl(globalData.getNGPWarriorCount() + globalData.getNGPMysticCount() + globalData.getNGPGuardianCount());
               }
               case "guardian" -> {
                   globalData.setNGPGuardianCount(amount);
                   globalData.setDEFBonus(amount * ModConfigs.statCap);
                   playerData.getDefenseStat().addModifier("NG+ Bonus", globalData.getDEFBonus(), true, false);
                   globalData.setPrestigeLvl(globalData.getNGPWarriorCount() + globalData.getNGPMysticCount() + globalData.getNGPGuardianCount());
               }
               case "all" -> {
                   globalData.setNGPWarriorCount(amount);
                   globalData.setNGPMysticCount(amount);
                   globalData.setNGPGuardianCount(amount);
                   globalData.setSTRBonus(amount * ModConfigs.statCap);
                   globalData.setMAGBonus(amount * ModConfigs.statCap);
                   globalData.setDEFBonus(amount * ModConfigs.statCap);
                   playerData.getStrengthStat().addModifier("NG+ Bonus", globalData.getSTRBonus(), true, false);
                   playerData.getMagicStat().addModifier("NG+ Bonus", globalData.getMAGBonus(), true, false);
                   playerData.getDefenseStat().addModifier("NG+ Bonus", globalData.getDEFBonus(), true, false);
                   globalData.setPrestigeLvl(globalData.getNGPWarriorCount() + globalData.getNGPMysticCount() + globalData.getNGPGuardianCount());
               }
            }
        }
        return 0;
    }

}
