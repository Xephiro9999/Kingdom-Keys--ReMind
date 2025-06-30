package online.remind.remind.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands {

    public static void register(final CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("remind")
                .then(NGPlusCommand.register())
        );
    }
}
