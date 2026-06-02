package online.remind.remind.panels;

import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.panels.OrganizationPanelAbilityHelper;

public class OrganizationPanelAbilityApplier {

    private static final String[] PANEL_ABILITIES = new String[] {
            Strings.highJump,
            Strings.dodgeRoll,
            Strings.aerialDodge,
            Strings.quickRun,
            Strings.glide

    };

    public static void refreshPanelAbilities(Player player) {
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (playerData == null || globalData == null) {
            return;
        }

        for (String ability : PANEL_ABILITIES) {
            boolean shouldHave = OrganizationPanelAbilityHelper.hasAbilityPanelEquipped(player, ability);

            if (shouldHave) {
                // fake equip/add ability here
            } else {
                // remove only panel-added version here
            }
        }
    }
}