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

    private static final Map<String, Integer> LAST_PANEL_STACK_BONUSES = new HashMap<>();

    private static String stackKey(Player player, String ability) {
        return player.getUUID() + ":" + ability;
    }

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
            case Strings.criticalBoost -> id("sight_panel");
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

    public static void refreshPanelGrantedMovementAbilities(Player player) {
        refreshPanelGrantedAbility(player, Strings.highJump);
        refreshPanelGrantedAbility(player, Strings.dodgeRoll);
        refreshPanelGrantedAbility(player, Strings.aerialDodge);
        refreshPanelGrantedAbility(player, Strings.quickRun);
        refreshPanelGrantedAbility(player, Strings.glide);
    }

    public static int getEquippedPanelCount(Player player, ResourceLocation panelId) {
        if (player == null || panelId == null) {
            return 0;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null || globalData.getPanelsEnabled() != 1) {
            return 0;
        }

        return globalData.countOrganizationPanelEquipped(panelId);
    }

    public static int getHighJumpLevel(Player player) {
        return getEquippedPanelCount(player, PanelRegistry.HIGH_JUMP_PANEL);
    }

    public static int getAerialDodgePanelLevel(Player player) {
        return getEquippedPanelCount(player, PanelRegistry.AERIAL_DODGE_PANEL);
    }

    public static int getGlidePanelLevel(Player player) {
        return getEquippedPanelCount(player, PanelRegistry.GLIDE_PANEL);
    }



    private static final java.util.Set<String> FAKE_PANEL_ABILITIES = new java.util.HashSet<>();
    private static final java.util.Map<java.util.UUID, java.util.Map<String, int[]>> ORIGINAL_PANEL_ABILITY_STATES =
            new java.util.HashMap<>();

    public static void refreshPanelGrantedAbility(Player player, String ability) {
        if (player == null || ability == null || ability.isEmpty()) {
            return;
        }

        PlayerData playerData = PlayerData.get(player);

        if (playerData == null || playerData.getAbilityMap() == null) {
            return;
        }

        boolean panelActive = hasAbilityPanelEquipped(player, ability);

        java.util.Map<String, int[]> playerOriginals =
                ORIGINAL_PANEL_ABILITY_STATES.computeIfAbsent(
                        player.getUUID(),
                        uuid -> new java.util.HashMap<>()
                );

        if (panelActive) {
            if (!playerOriginals.containsKey(ability)) {
                int[] original = playerData.getAbilityMap().get(ability);

                if (original == null) {
                    playerOriginals.put(ability, null);
                } else {
                    playerOriginals.put(ability, new int[] { original[0], original[1] });
                }
            }

            int[] current = playerData.getAbilityMap().get(ability);

            int level = 0;

            if (current != null && current.length > 0) {
                level = Math.max(current[0], 0);
            }

            /*
             * Add/grant the ability if missing, then equip index 0.
             * [0] = unlocked level/index
             * [1] = equipped bit/state
             */
            playerData.getAbilityMap().put(ability, new int[] { level, 0 });
            playerData.equipAbility(ability, 0);

            return;
        }

        /*
         * Panel inactive: restore only if the panel system changed this ability.
         */
        if (playerOriginals.containsKey(ability)) {
            int[] original = playerOriginals.get(ability);

            if (original == null) {
                playerData.getAbilityMap().remove(ability);
            } else {
                playerData.getAbilityMap().put(ability, new int[] { original[0], original[1] });
            }

            playerOriginals.remove(ability);
        }

        if (playerOriginals.isEmpty()) {
            ORIGINAL_PANEL_ABILITY_STATES.remove(player.getUUID());
        }
    }

    private static final java.util.Set<String> PANEL_FAKE_EQUIPPED = new java.util.HashSet<>();



    public static void refreshPanelGrantedStackableAbilities(Player player) {
        if (player == null) {
            return;
        }

        PlayerData playerData = PlayerData.get(player);

        if (playerData == null) {
            return;
        }

        refreshStackableAbility(player, playerData, Strings.criticalBoost, PanelRegistry.SIGHT_UNIT);
        refreshStackableAbility(player, playerData, Strings.comboPlus, PanelRegistry.COMBO_PLUS_PANEL);
        refreshStackableAbility(player, playerData, StringsRM.attackHaste, PanelRegistry.HASTE_PANEL);
        //refreshStackableAbility(player, playerData, Strings.airComboPlus, PanelRegistry.AIR_COMBO_PLUS_PANEL);

        //refreshStackableAbility(player, playerData, Strings.comboBoost, PanelRegistry.COMBO_BOOST_PANEL);
        //refreshStackableAbility(player, playerData, Strings.airComboBoost, PanelRegistry.AIR_COMBO_BOOST_PANEL);

        refreshStackableAbility(player, playerData, Strings.fireBoost, PanelRegistry.FIRE_BOOST_PANEL);
        refreshStackableAbility(player, playerData, Strings.blizzardBoost, PanelRegistry.BLIZZARD_BOOST_PANEL);
        refreshStackableAbility(player, playerData, Strings.thunderBoost, PanelRegistry.THUNDER_BOOST_PANEL);

        refreshStackableAbility(player, playerData, Strings.treasureMagnet, PanelRegistry.DRAW_PANEL);
        refreshStackableAbility(player, playerData, Strings.jackpot, PanelRegistry.JACKPOT_PANEL);
        refreshStackableAbility(player, playerData, Strings.luckyLucky, PanelRegistry.LUCKY_LUCKY_PANEL);
    }

    private static void refreshStackableAbility(
            Player player,
            PlayerData playerData,
            String ability,
            ResourceLocation panelId
    ) {
        if (player == null || playerData == null || ability == null || panelId == null) {
            return;
        }

        String key = stackKey(player, ability);

        int lastApplied = LAST_PANEL_STACK_BONUSES.getOrDefault(key, 0);

        /*
         * Remove whatever the panel system added last time.
         * This prevents infinite stacking.
         */
        if (lastApplied > 0) {
            addPanelAbilityStackSafe(playerData, ability, -lastApplied);
        }

        int newPanelStacks = getEquippedPanelCount(player, panelId);

        /*
         * Add current panel count as temporary owned + equipped stacks.
         */
        if (newPanelStacks > 0) {
            addPanelAbilityStackSafe(playerData, ability, newPanelStacks);
            LAST_PANEL_STACK_BONUSES.put(key, newPanelStacks);
        } else {
            LAST_PANEL_STACK_BONUSES.remove(key);
        }

        if (ability.equals(Strings.fireBoost)) {
            int[] data = playerData.getAbilityMap().get(ability);

            if (data != null) {
                System.out.println("[OrgPanelStackables] Fire Boost panels: " + newPanelStacks);
                System.out.println("[OrgPanelStackables] Fire Boost [0]: " + data[0]);
                System.out.println("[OrgPanelStackables] Fire Boost [1]: " + data[1]);
            }
        }
    }

    private static void addPanelAbilityStackSafe(PlayerData playerData, String ability, int amount) {
        if (playerData == null || ability == null || amount == 0) {
            return;
        }

        if (playerData.getAbilityMap() == null) {
            return;
        }

        int[] data = playerData.getAbilityMap().get(ability);

        if (data == null) {
            /*
             * [0] = owned/unlocked stack count
             * [1] = equipped/active stack count
             */
            int value = Math.max(0, amount);
            playerData.getAbilityMap().put(ability, new int[] {value, value});
            return;
        }

        data[0] = Math.max(0, data[0] + amount);
        data[1] = Math.max(0, data[1] + amount);
    }

}