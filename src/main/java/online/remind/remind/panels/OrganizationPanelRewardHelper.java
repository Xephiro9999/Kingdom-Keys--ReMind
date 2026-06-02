package online.remind.remind.panels;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;

public class OrganizationPanelRewardHelper {

    public static boolean grantOrganizationPanel(Player player, ResourceLocation panelId, int amount) {
        if (player == null || panelId == null || amount <= 0) {
            return false;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null) {
            return false;
        }

        PanelData data = PanelRegistry.get(panelId);

        if (data == null) {
            return false;
        }

        globalData.addOwnedOrganizationPanel(panelId, amount);

        PacketHandlerRM.syncGlobalToAllAround(player, globalData);

        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.displayClientMessage(
                    Component.literal("Obtained " + amount + "x " + getPanelDisplayName(panelId))
                            .withColor(0xFFD700),
                    true
            );
        }

        return true;
    }

    public static String getPanelDisplayName(ResourceLocation panelId) {
        if (panelId == null) {
            return "Unknown Panel";
        }

        return switch (panelId.getPath()) {
            case "level_up" -> "Level Up";
            case "strength_unit" -> "Strength Unit";
            case "magic_unit" -> "Magic Unit";
            case "defense_unit" -> "Defense Unit";
            case "ap_unit" -> "AP Unit";

            case "strength_unit_l" -> "Strength Unit L";
            case "magic_unit_l" -> "Magic Unit L";
            case "defense_unit_l" -> "Defense Unit L";
            case "ap_unit_l" -> "AP Unit L";
            case "level_doubler" -> "Level Doubler";

            case "power_link" -> "Power Link";
            case "magic_link" -> "Magic Link";
            case "guard_link" -> "Guard Link";
            case "level_link" -> "Level Link";

            case "fire_panel" -> "Fire Panel";
            case "blizzard_panel" -> "Blizzard Panel";
            case "thunder_panel" -> "Thunder Panel";
            case "cure_panel" -> "Cure Panel";
            case "aero_panel" -> "Aero Panel";
            case "water_panel" -> "Water Panel";
            case "dark_panel" -> "Dark Panel";
            case "light_panel" -> "Light Panel";

            default -> panelId.getPath();
        };
    }

    public static boolean useSlotReleaser(Player player) {
        if (player == null) {
            return false;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null) {
            return false;
        }

        boolean expanded = globalData.expandOrganizationPanelGrid();

        if (!expanded) {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.displayClientMessage(
                        Component.literal("Your Panel Grid is already fully expanded.")
                                .withColor(0xFF5555),
                        true
                );
            }

            return false;
        }

        PacketHandlerRM.syncGlobalToAllAround(player, globalData);

        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.displayClientMessage(
                    Component.literal(
                            "Panel slot unlocked! "
                                    + globalData.getUnlockedOrganizationPanelSlots()
                                    + "/"
                                    + GlobalDataRM.ORGANIZATION_PANEL_MAX_SLOTS
                    ).withColor(0xFFD700),
                    true
            );
        }

        return true;
    }
}