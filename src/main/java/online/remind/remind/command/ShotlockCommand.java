package online.remind.remind.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;

import java.util.ArrayList;
import java.util.List;

public class ShotlockCommand extends AddonCommand {
    //shotlock <give/take> <shotlock> [player]
    private static final SuggestionProvider<CommandSourceStack> SUGGEST_MAGICS = (p_198296_0_, p_198296_1_) -> {
        List<String> list = new ArrayList<>();
        for (ResourceLocation location : ModShotlocks.registry.keySet()) {
            list.add(location.toString());
        }
        return SharedSuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
    };

    public static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> builder = Commands.literal("shotlock").requires(source -> source.hasPermission(2));

    /*
    builder.then(Commands.literal("give")

            .then(Commands.argument("shotlock", StringArgumentType.string()).suggests(SUGGEST_MAGICS)
            .then(Commands.argument("targets", EntityArgument.players())
            .executes(ShotlockCommand::setValue))
                        );

     */


}
