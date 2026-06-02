package online.remind.remind.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.panels.*;

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

                        .then(Commands.literal("grantdefaults")
                                .executes(context -> grantDefaults(context.getSource()))
                        )

                        .then(Commands.literal("list")
                                .executes(context -> listPanelsSelf(context.getSource()))
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(context -> listPanelsTarget(
                                                context.getSource(),
                                                EntityArgument.getPlayer(context, "target")
                                        ))
                                )
                        )

                        .then(Commands.literal("give")
                                .then(Commands.argument("panel", StringArgumentType.word())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                .executes(context -> givePanelSelf(
                                                        context.getSource(),
                                                        StringArgumentType.getString(context, "panel"),
                                                        IntegerArgumentType.getInteger(context, "amount")
                                                ))
                                        )
                                )
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.argument("panel", StringArgumentType.word())
                                                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                        .executes(context -> givePanelTarget(
                                                                context.getSource(),
                                                                EntityArgument.getPlayer(context, "target"),
                                                                StringArgumentType.getString(context, "panel"),
                                                                IntegerArgumentType.getInteger(context, "amount")
                                                        ))
                                                )
                                        )
                                )
                        )

                        .then(Commands.literal("expand")
                                .executes(context -> expandGridSelf(context.getSource()))
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(context -> expandGridTarget(
                                                context.getSource(),
                                                EntityArgument.getPlayer(context, "target")
                                        ))
                                )
                        )
                        .then(Commands.literal("setsize")
                                .then(Commands.argument("width", IntegerArgumentType.integer(1))
                                        .then(Commands.argument("height", IntegerArgumentType.integer(1))
                                                .executes(context -> setGridSizeSelf(
                                                        context.getSource(),
                                                        IntegerArgumentType.getInteger(context, "width"),
                                                        IntegerArgumentType.getInteger(context, "height")
                                                ))
                                        )
                                )
                        )
                        .then(Commands.literal("setslots")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(
                                                        GlobalDataRM.ORGANIZATION_PANEL_STARTING_SLOTS,
                                                        GlobalDataRM.ORGANIZATION_PANEL_MAX_SLOTS
                                                ))
                                                .executes(context -> setUnlockedSlotsSelf(
                                                        context.getSource(),
                                                        IntegerArgumentType.getInteger(context, "amount")
                                                ))
                                )
                        )
        );
    }

    private static int setUnlockedSlotsSelf(CommandSourceStack source, int amount) {
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

        globalData.setUnlockedOrganizationPanelSlots(amount);
        PacketHandlerRM.syncGlobalToAllAround(player, globalData);

        source.sendSuccess(
                () -> Component.literal(
                        "Set unlocked panel slots to "
                                + globalData.getUnlockedOrganizationPanelSlots()
                                + "/"
                                + GlobalDataRM.ORGANIZATION_PANEL_MAX_SLOTS
                ),
                false
        );

        return 1;
    }

    private static int setGridSizeSelf(CommandSourceStack source, int width, int height) {
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


        globalData.setOrganizationPanelGrid(new PanelGrid(width, height));

        PacketHandlerRM.syncGlobalToAllAround(player, globalData);

        source.sendSuccess(
                () -> Component.literal("Set panel grid size to " + width + "x" + height + "."),
                false
        );

        return 1;
    }



    private static int expandGridSelf(CommandSourceStack source) {
        ServerPlayer player;

        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            source.sendFailure(Component.literal("This command must be used by a player."));
            return 0;
        }

        return expandGridForPlayer(source, player);
    }

    private static int expandGridTarget(CommandSourceStack source, ServerPlayer target) {
        return expandGridForPlayer(source, target);
    }

    private static int expandGridForPlayer(CommandSourceStack source, ServerPlayer target) {
        GlobalDataRM globalData = ModDataRM.getGlobal(target);

        if (globalData == null) {
            source.sendFailure(Component.literal("Could not find Re:Mind global data."));
            return 0;
        }

        boolean expanded = globalData.expandOrganizationPanelGrid();

        if (!expanded) {
            source.sendFailure(Component.literal(target.getName().getString() + "'s panel grid is already max size."));
            return 0;
        }

        PacketHandlerRM.syncGlobalToAllAround(target, globalData);

        source.sendSuccess(
                () -> Component.literal(
                        "Expanded " + target.getName().getString()
                                + "'s panel grid to "
                                + globalData.getOrganizationPanelGridWidth()
                                + "x"
                                + globalData.getOrganizationPanelGridHeight()
                                + "."
                ),
                false
        );

        return 1;
    }





    private static int listPanelsSelf(CommandSourceStack source) {
        ServerPlayer player;

        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            source.sendFailure(Component.literal("This command must be used by a player."));
            return 0;
        }

        return listPanelsForPlayer(source, player);
    }

    private static int listPanelsTarget(CommandSourceStack source, ServerPlayer target) {
        return listPanelsForPlayer(source, target);
    }

    private static int listPanelsForPlayer(CommandSourceStack source, ServerPlayer target) {
        GlobalDataRM globalData = ModDataRM.getGlobal(target);

        if (globalData == null) {
            source.sendFailure(Component.literal("Could not find Re:Mind global data for " + target.getName().getString() + "."));
            return 0;
        }

        source.sendSuccess(
                () -> Component.literal("Owned Organization Panels for " + target.getName().getString() + ":")
                        .withColor(0xFFD700),
                false
        );

        boolean foundAny = false;

        for (PanelData panelData : PanelRegistry.getAll()) {
            ResourceLocation panelId = panelData.getId();
            int count = globalData.getOwnedOrganizationPanelCount(panelId);

            if (count <= 0) {
                continue;
            }

            foundAny = true;

            source.sendSuccess(
                    () -> Component.literal(
                            panelId.getPath()
                                    + " x" + count
                                    + " [" + panelData.getWidth() + "x" + panelData.getHeight() + "]"
                    ).withColor(0xFFFFFF),
                    false
            );
        }

        if (!foundAny) {
            source.sendSuccess(
                    () -> Component.literal("No owned Organization Panels found.")
                            .withColor(0xFF5555),
                    false
            );
        }

        return 1;
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

    private static int givePanelSelf(CommandSourceStack source, String panelName, int amount) {
        ServerPlayer player;

        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            source.sendFailure(Component.literal("This command must be used by a player."));
            return 0;
        }

        return givePanelToPlayer(source, player, panelName, amount);
    }

    private static int listPanels(CommandSourceStack source) {
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

        source.sendSuccess(
                () -> Component.literal("Owned Organization Panels:").withColor(0xFFD700),
                false
        );

        for (PanelData panelData : PanelRegistry.getAll()) {
            ResourceLocation panelId = panelData.getId();
            int count = globalData.getOwnedOrganizationPanelCount(panelId);

            if (count <= 0) {
                continue;
            }

            source.sendSuccess(
                    () -> Component.literal(
                            panelId.getPath()
                                    + " x" + count
                                    + " [" + panelData.getWidth() + "x" + panelData.getHeight() + "]"
                    ).withColor(0xFFFFFF),
                    false
            );
        }

        return 1;
    }

    private static int givePanelTarget(CommandSourceStack source, ServerPlayer target, String panelName, int amount) {
        return givePanelToPlayer(source, target, panelName, amount);
    }

    private static int givePanelToPlayer(CommandSourceStack source, ServerPlayer target, String panelName, int amount) {
        ResourceLocation panelId = ResourceLocation.fromNamespaceAndPath(
                KingdomKeysReMind.MODID,
                panelName
        );

        PanelData data = PanelRegistry.get(panelId);

        if (data == null) {
            source.sendFailure(Component.literal("Unknown panel: " + panelId));
            return 0;
        }

        boolean granted = OrganizationPanelRewardHelper.grantOrganizationPanel(
                target,
                panelId,
                amount
        );

        if (!granted) {
            source.sendFailure(Component.literal("Could not grant panel."));
            return 0;
        }

        source.sendSuccess(
                () -> Component.literal(
                        "Granted " + amount + "x "
                                + OrganizationPanelRewardHelper.getPanelDisplayName(panelId)
                                + " to "
                                + target.getName().getString()
                                + "."
                ),
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

    private static int grantDefaults(CommandSourceStack source) {
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

        globalData.addOwnedOrganizationPanel(PanelRegistry.STRENGTH_UNIT, 5);
        globalData.addOwnedOrganizationPanel(PanelRegistry.MAGIC_UNIT, 5);
        globalData.addOwnedOrganizationPanel(PanelRegistry.DEFENSE_UNIT, 5);
        globalData.addOwnedOrganizationPanel(PanelRegistry.AP_UNIT, 3);
        globalData.addOwnedOrganizationPanel(PanelRegistry.LEVEL_UP, 3);

        globalData.addOwnedOrganizationPanel(PanelRegistry.STRENGTH_UNIT_L, 2);
        globalData.addOwnedOrganizationPanel(PanelRegistry.MAGIC_UNIT_L, 2);
        globalData.addOwnedOrganizationPanel(PanelRegistry.DEFENSE_UNIT_L, 2);
        globalData.addOwnedOrganizationPanel(PanelRegistry.AP_UNIT_L, 1);
        globalData.addOwnedOrganizationPanel(PanelRegistry.LEVEL_DOUBLER, 1);

        globalData.addOwnedOrganizationPanel(PanelRegistry.POWER_LINK, 2);
        globalData.addOwnedOrganizationPanel(PanelRegistry.MAGIC_LINK, 2);
        globalData.addOwnedOrganizationPanel(PanelRegistry.GUARD_LINK, 2);
        globalData.addOwnedOrganizationPanel(PanelRegistry.LEVEL_LINK, 1);

        source.sendSuccess(
                () -> Component.literal("Granted default Organization Panels."),
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

        globalData.setOrganizationPanelGrid(new PanelGrid(
                globalData.getOrganizationPanelGridWidth(),
                globalData.getOrganizationPanelGridHeight()
        ));

        source.sendSuccess(
                () -> Component.literal("Organization Panel grid cleared."),
                false
        );

        return 1;
    }

    public static void grantOrganizationPanel(Player player, ResourceLocation panelId, int amount) {
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null || panelId == null || amount <= 0) {
            return;
        }

        globalData.addOwnedOrganizationPanel(panelId, amount);
        PacketHandlerRM.syncGlobalToAllAround(player, globalData);

        if (player instanceof ServerPlayer serverPlayer) {
            // send panel sync packet here too if needed
        }
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
                                + " | Slots "
                                + globalData.getUnlockedOrganizationPanelSlots()
                                + "/"
                                + GlobalDataRM.ORGANIZATION_PANEL_MAX_SLOTS
                ),
                false
        );

        return 1;
    }
}