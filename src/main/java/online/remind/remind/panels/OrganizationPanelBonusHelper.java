package online.remind.remind.panels;

import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;

public class OrganizationPanelBonusHelper {

    public static PanelStats getStats(Player player) {
        if (player == null) {
            return new PanelStats();
        }

        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (playerData == null || globalData == null) {
            return new PanelStats();
        }

        // Only Organization XIII members benefit from the Days-style panel grid.
        if (playerData.getAlignment() == Utils.OrgMember.NONE) {
            return new PanelStats();
        }

        // Optional: respect your Boost ON/OFF toggle.
        // If 1 means enabled in your setup, keep this.
        // If your logic is reversed, flip the check.
        if (globalData.getPanelsEnabled() != 1) {
            return new PanelStats();
        }

        return globalData.getOrganizationPanelStats();
    }

    public static int getStrengthBonus(Player player) {
        return getStats(player).getStrength();
    }

    public static int getMagicBonus(Player player) {
        return getStats(player).getMagic();
    }

    public static int getDefenseBonus(Player player) {
        return getStats(player).getDefense();
    }

    public static int getApBonus(Player player) {
        return getStats(player).getAp();
    }

    public static int getLevelBonus(Player player) {
        return getStats(player).getLevelBonus();
    }
}