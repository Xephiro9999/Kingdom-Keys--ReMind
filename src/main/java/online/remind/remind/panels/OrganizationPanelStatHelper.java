package online.remind.remind.panels;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;

public class OrganizationPanelStatHelper {

    public static final String PANEL_MODIFIER_ID = "Panel";

    public static void removePanelModifiers(Player player) {
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (playerData == null || globalData == null) {
            return;
        }

        playerData.getStrengthStat().removeModifier(PANEL_MODIFIER_ID);
        playerData.getMagicStat().removeModifier(PANEL_MODIFIER_ID);
        playerData.getDefenseStat().removeModifier(PANEL_MODIFIER_ID);

        int previousAP = globalData.getLastOrganizationPanelAPBonus();

        if (previousAP != 0) {
            playerData.addMaxAP(-previousAP);
            globalData.setLastOrganizationPanelAPBonus(0);
        }

        globalData.setLastOrganizationPanelLevelBonus(0);
    }

    public static void applyPanelModifiers(Player player) {
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (playerData == null || globalData == null) {
            return;
        }

        PanelStats stats = globalData.getOrganizationPanelStats();

        removePanelModifiers(player);

        if (stats.getStrength() > 0) {
            playerData.getStrengthStat().addModifier("Panel", stats.getStrength(), false, false);
        }

        if (stats.getMagic() > 0) {
            playerData.getMagicStat().addModifier("Panel", stats.getMagic(), false, false);
        }

        if (stats.getDefense() > 0) {
            playerData.getDefenseStat().addModifier("Panel", stats.getDefense(), false, false);
        }

        int apBonus = stats.getAp();

        if (apBonus > 0) {
            playerData.addMaxAP(apBonus);
            globalData.setLastOrganizationPanelAPBonus(apBonus);
        }

        int levelBonus = stats.getLevelBonus();

        if (levelBonus > 0) {
            globalData.setLastOrganizationPanelLevelBonus(levelBonus);
        }
    }

    public static void refreshPanelModifiersIfEnabled(Player player) {
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null) {
            return;
        }

        if (globalData.getPanelsEnabled() == 1) {
            applyPanelModifiers(player);
        } else {
            removePanelModifiers(player);
        }

        sync(player, globalData);
    }

    private static void sync(Player player, GlobalDataRM globalData) {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketHandler.sendTo(new SCSyncPlayerData(player), serverPlayer);
        }

        PacketHandlerRM.syncGlobalToAllAround(player, globalData);
    }

    public static int getPanelLevelBonus(Player player) {
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null) {
            return 0;
        }

        if (globalData.getPanelsEnabled() != 1) {
            return 0;
        }

        return globalData.getLastOrganizationPanelLevelBonus();
    }

    public static int getEffectiveLevel(Player player) {
        PlayerData playerData = PlayerData.get(player);

        if (playerData == null) {
            return 0;
        }

        return playerData.getLevel() + getPanelLevelBonus(player);
    }
}