package online.remind.remind.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.panels.PanelData;
import online.remind.remind.panels.PanelGrid;
import online.remind.remind.panels.PanelRegistry;
import online.remind.remind.panels.PanelStats;

public class OrganizationPanelCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("orgpanel")
                        .requires(source -> source.hasPermission(2))

                        .then(Commands.literal("place")
                                .then(Commands.argument("panel", StringArgumentType.word())
                                        .then(Commands.argument("x", IntegerArgumentType.integer(0))
                                                .then(Commands.argument("y", IntegerArgumentType.integer(0))
                                                        .executes(context -> placePanel(
                                                                context.getSource(),
                                                                StringArgumentType.getString(context, "panel"),
                                                                IntegerArgumentType.getInteger(context, "x"),
                                                                IntegerArgumentType.getInteger(context, "y")
                                                        ))
                                                )
                                        )
                                )
                        )

                        .then(Commands.literal("remove")
                                .then(Commands.argument("x", IntegerArgumentType.integer(0))
                                        .then(Commands.argument("y", IntegerArgumentType.integer(0))
                                                .executes(context -> removePanel(
                                                        context.getSource(),
                                                        IntegerArgumentType.getInteger(context, "x"),
                                                        IntegerArgumentType.getInteger(context, "y")
                                                ))
                                        )
                                )
                        )

                        .then(Commands.literal("clear")
                                .executes(context -> clearGrid(context.getSource()))
                        )

                        .then(Commands.literal("stats")
                                .executes(context -> showStats(context.getSource()))
                        )
        );
    }

    private static int placePanel(CommandSourceStack source, String panelName, int x, int y) {
        ServerPlayer player;

        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            source.sendFailure(Component.literal("This command must be used by a player."));
            return 0;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null) {
            source.sendFailure(Component.literal("Could not find Re:Mind global data."));
            return 0;
        }

        ResourceLocation panelId = ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, panelName);
        PanelData panelData = PanelRegistry.get(panelId);

        if (panelData == null) {
            source.sendFailure(Component.literal("Unknown panel: " + panelId));
            return 0;
        }

        boolean placed = globalData.placeOrganizationPanel(panelId, x, y);

        if (!placed) {
            source.sendFailure(Component.literal("Could not place panel there."));
            return 0;
        }

        source.sendSuccess(
                () -> Component.literal("Placed " + panelId + " at [" + x + ", " + y + "]"),
                false
        );

        return 1;
    }

    private static int removePanel(CommandSourceStack source, int x, int y) {
        ServerPlayer player;

        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            source.sendFailure(Component.literal("This command must be used by a player."));
            return 0;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null) {
            source.sendFailure(Component.literal("Could not find Re:Mind global data."));
            return 0;
        }

        boolean removed = globalData.removeOrganizationPanelAt(x, y);

        if (!removed) {
            source.sendFailure(Component.literal("No panel found at [" + x + ", " + y + "]."));
            return 0;
        }

        source.sendSuccess(
                () -> Component.literal("Removed panel at [" + x + ", " + y + "]."),
                false
        );

        return 1;
    }

    private static int clearGrid(CommandSourceStack source) {
        ServerPlayer player;

        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            source.sendFailure(Component.literal("This command must be used by a player."));
            return 0;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null) {
            source.sendFailure(Component.literal("Could not find Re:Mind global data."));
            return 0;
        }

        globalData.setOrganizationPanelGrid(new PanelGrid(5, 4));

        source.sendSuccess(
                () -> Component.literal("Organization Panel grid cleared."),
                false
        );

        return 1;
    }

    private static int showStats(CommandSourceStack source) {
        ServerPlayer player;

        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            source.sendFailure(Component.literal("This command must be used by a player."));
            return 0;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null) {
            source.sendFailure(Component.literal("Could not find Re:Mind global data."));
            return 0;
        }

        PanelStats stats = globalData.getOrganizationPanelStats();

        source.sendSuccess(
                () -> Component.literal(
                        "Panel Stats | STR +" + stats.getStrength()
                                + " | MAG +" + stats.getMagic()
                                + " | DEF +" + stats.getDefense()
                                + " | AP +" + stats.getAp()
                                + " | LV +" + stats.getLevelBonus()
                ),
                false
        );

        return 1;
    }
}