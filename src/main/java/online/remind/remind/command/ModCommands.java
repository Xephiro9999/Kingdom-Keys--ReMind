package online.remind.remind.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import online.remind.remind.command.OrganizationPanelCommand;

public class ModCommands {

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        OrganizationPanelCommand.register(event.getDispatcher());
    }
}
