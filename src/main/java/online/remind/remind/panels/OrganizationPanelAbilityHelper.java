package online.remind.remind.panels;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.lib.StringsRM;

import java.util.HashMap;
import java.util.Map;

public class OrganizationPanelAbilityHelper {

    public static boolean hasAbility(Player player, String ability) {
        if (player == null || ability == null || ability.isEmpty()) {
            return false;
        }

        PlayerData playerData = PlayerData.get(player);

        if (playerData != null && playerData.isAbilityEquipped(ability)) {
            return true;
        }

        return hasAbilityPanelEquipped(player, ability);
    }

    public static boolean hasAbilityPanelEquipped(Player player, String ability) {
        if (player == null || ability == null || ability.isEmpty()) {
            return false;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null) {
            return false;
        }

        // Panel abilities only work while Organization Panel boosts are ON.
        if (globalData.getPanelsEnabled() != 1) {
            return false;
        }

        ResourceLocation panelId = getPanelForAbility(ability);

        if (panelId == null) {
            return false;
        }

        return globalData.hasOrganizationPanelEquipped(panelId);
    }

    public static boolean hasUltimaWeapon(Player player) {
        return hasAbility(player, StringsRM.ultima_weapon_ability);
    }

    public static boolean hasHeartsPower(Player player) {
        return hasAbility(player, StringsRM.heartsPower);
    }

    public static boolean hasHighJump(Player player) {
        return hasAbility(player, Strings.highJump);
    }

    public static boolean hasDodgeRoll(Player player) {
        return hasAbility(player, Strings.dodgeRoll);
    }

    public static boolean hasAerialDodge(Player player) {
        return hasAbility(player, Strings.aerialDodge);
    }

    public static boolean hasQuickRun(Player player) {
        return hasAbility(player, Strings.quickRun);
    }

    public static boolean hasGlide(Player player) {
        return hasAbility(player, Strings.glide);
    }

    private static ResourceLocation getPanelForAbility(String ability) {
        return switch (ability) {
            case StringsRM.heartsPower -> id("hearts_power_panel");
            case StringsRM.ultima_weapon_ability -> id("ultima_weapon_panel");
            case Strings.highJump -> id("high_jump_panel");
            case Strings.dodgeRoll -> id("dodge_roll_panel");
            case Strings.aerialDodge -> id("aerial_dodge_panel");
            case Strings.quickRun -> id("quick_run_panel");
            case Strings.glide -> id("glide_panel");

            default -> null;
        };
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, path);
    }

    public static void refreshFakeMovementAbilities(Player player) {
        refreshFakePanelAbility(player, Strings.highJump);
        refreshFakePanelAbility(player, Strings.dodgeRoll);
        refreshFakePanelAbility(player, Strings.aerialDodge);
        refreshFakePanelAbility(player, Strings.quickRun);
        refreshFakePanelAbility(player, Strings.glide);
    }

    public static void debugAbilityPanel(Player player, String ability) {
        if (player == null || ability == null) {
            return;
        }

        PlayerData playerData = PlayerData.get(player);

        if (playerData == null) {
            return;
        }

        boolean panelEquipped = hasAbilityPanelEquipped(player, ability);
        boolean normalEquipped = playerData.isAbilityEquipped(ability);
        int[] equippedLevel = playerData.getEquippedAbilityLevel(ability);

        System.out.println("[OrgPanelAbilities] Ability: " + ability);
        System.out.println("[OrgPanelAbilities] Side: " + (player.level().isClientSide ? "CLIENT" : "SERVER"));
        System.out.println("[OrgPanelAbilities] Panel equipped: " + panelEquipped);
        System.out.println("[OrgPanelAbilities] Normal equipped: " + normalEquipped);

        if (equippedLevel != null && equippedLevel.length >= 2) {
            System.out.println("[OrgPanelAbilities] KK equipped level [0]: " + equippedLevel[0]);
            System.out.println("[OrgPanelAbilities] KK equipped level [1]: " + equippedLevel[1]);
        } else {
            System.out.println("[OrgPanelAbilities] KK equipped level: null");
        }
    }

    private static final java.util.Set<String> FAKE_PANEL_ABILITIES = new java.util.HashSet<>();
    private static final java.util.Map<java.util.UUID, java.util.Map<String, int[]>> ORIGINAL_PANEL_ABILITY_STATES =
            new java.util.HashMap<>();



    public static void refreshFakePanelAbility(Player player, String ability) {
        if (player == null || ability == null || ability.isEmpty()) {
            return;
        }

        PlayerData playerData = PlayerData.get(player);

        if (playerData == null || playerData.getAbilityMap() == null) {
            return;
        }

        boolean panelActive = hasAbilityPanelEquipped(player, ability);

        int[] current = playerData.getAbilityMap().get(ability);

        if (current == null || current.length < 2) {
            return;
        }

        int index = 0;
        int bit = (int) Math.pow(2, index);

        String key = player.getUUID() + "|" + ability;

        if (panelActive) {
            if ((current[1] & bit) == 0) {
                PANEL_FAKE_EQUIPPED.add(key);
                playerData.equipAbility(ability, index);
            }

            return;
        }

        if (PANEL_FAKE_EQUIPPED.contains(key)) {
            int[] latest = playerData.getAbilityMap().get(ability);

            if (latest != null && latest.length >= 2) {
                latest[1] &= ~bit;
                playerData.getAbilityMap().put(ability, new int[] { latest[0], latest[1] });
            }

            PANEL_FAKE_EQUIPPED.remove(key);
        }
    }

    private static final java.util.Set<String> PANEL_FAKE_EQUIPPED = new java.util.HashSet<>();

}