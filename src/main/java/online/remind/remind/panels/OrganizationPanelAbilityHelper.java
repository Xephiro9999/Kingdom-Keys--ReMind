package online.remind.remind.panels;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.lib.StringsRM;

import net.minecraft.server.level.ServerPlayer;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OrganizationPanelAbilityHelper {

    private static final Map<UUID, Integer> PENDING_PANEL_ABILITY_REFRESHES =
            new ConcurrentHashMap<>();

    private static final Map<UUID, Map<String, int[]>> ORIGINAL_STACKABLE_ABILITY_STATES =
            new ConcurrentHashMap<>();
    private static final int[] NO_ORIGINAL_STACKABLE_ABILITY = new int[] {-1, -1};

    private static final int PANEL_REFRESH_DELAY_TICKS = 2;

    public static boolean hasAbility(Player player, String ability) {
        if (player == null || ability == null || ability.isEmpty()) {
            return false;
        }

        PlayerData playerData = PlayerData.get(player);

        if (playerData != null && playerData.isAbilityEquipped(ResourceLocation.parse(ability))) {
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
        if (!isOrganizationPanelSystemActive(player)) {
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

    public static boolean isOrganizationPanelSystemActive(Player player) {
        if (player == null) {
            return false;
        }

        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (playerData == null || globalData == null) {
            return false;
        }

        if (playerData.getAlignment() == Utils.OrgMember.NONE) {
            return false;
        }

        return globalData.getPanelsEnabled() == 1;
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

        if (globalData == null || !isOrganizationPanelSystemActive(player)) {
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



    private static final Set<String> FAKE_PANEL_ABILITIES = new HashSet<>();
    private static final Map<UUID, Map<String, int[]>> ORIGINAL_PANEL_ABILITY_STATES =
            new HashMap<>();
    private static final Object ORIGINAL_PANEL_ABILITY_LOCK = new Object();

    public static void refreshPanelGrantedAbility(Player player, String ability) {
        if (player == null || ability == null || ability.isEmpty()) {
            return;
        }

        PlayerData playerData = PlayerData.get(player);

        if (playerData == null || playerData.getAbilityMap() == null) {
            return;
        }

        boolean panelActive = hasAbilityPanelEquipped(player, ability);

        synchronized (ORIGINAL_PANEL_ABILITY_LOCK) {
            Map<String, int[]> playerOriginals = ORIGINAL_PANEL_ABILITY_STATES.get(player.getUUID());



            if (playerOriginals == null) {
                playerOriginals = new HashMap<>();
                ORIGINAL_PANEL_ABILITY_STATES.put(player.getUUID(), playerOriginals);
            }

            if (panelActive) {
                if (!playerOriginals.containsKey(ability)) {
                    int[] original = playerData.getAbilityMap().get(ResourceLocation.parse(ability));

                    if (original == null) {
                        playerOriginals.put(ability, null);
                    } else {
                        playerOriginals.put(ability, new int[] { original[0], original[1] });
                    }
                }

                int[] current = playerData.getAbilityMap().get(ResourceLocation.parse(ability));

                int level = 0;

                if (current != null && current.length > 0) {
                    level = Math.max(current[0], 0);
                }

                /*
                 * Add/grant the ability if missing, then equip index 0.
                 * [0] = unlocked level/index
                 * [1] = equipped bit/state
                 */
                playerData.getAbilityMap().put(ResourceLocation.parse(ability), new int[] { level, 0 });
                playerData.equipAbility(ResourceLocation.parse(ability), 0);

                return;
            }

            /*
             * Panel inactive: restore only if the panel system changed this ability.
             */
            if (playerOriginals.containsKey(ability)) {
                int[] original = playerOriginals.get(ability);

                if (original == null) {
                    playerData.getAbilityMap().remove(ResourceLocation.parse(ability));
                } else {
                    playerData.getAbilityMap().put(ResourceLocation.parse(ability), new int[] { original[0], original[1] });
                }

                playerOriginals.remove(ability);
            }

            if (playerOriginals.isEmpty()) {
                ORIGINAL_PANEL_ABILITY_STATES.remove(player.getUUID());
            }
        }
    }

    private static final Set<String> PANEL_FAKE_EQUIPPED = new HashSet<>();

    public static void markPanelAbilityRefreshDirty(Player player) {
        if (player == null || player.level().isClientSide) {
            return;
        }

        PENDING_PANEL_ABILITY_REFRESHES.put(player.getUUID(), PANEL_REFRESH_DELAY_TICKS);
    }

    public static void tickPendingPanelAbilityRefresh(ServerPlayer player) {
        if (player == null || player.level().isClientSide) {
            return;
        }

        UUID uuid = player.getUUID();

        Integer ticksLeft = PENDING_PANEL_ABILITY_REFRESHES.get(uuid);

        if (ticksLeft == null) {
            return;
        }

        if (ticksLeft > 0) {
            PENDING_PANEL_ABILITY_REFRESHES.put(uuid, ticksLeft - 1);
            return;
        }

        PENDING_PANEL_ABILITY_REFRESHES.remove(uuid);

        refreshPanelGrantedMovementAbilities(player);
        refreshPanelGrantedStackableAbilities(player);

        PacketHandler.sendTo(new SCSyncPlayerData(player), player);
    }



    public static void refreshPanelGrantedStackableAbilities(Player player) {
        if (player == null) {
            return;
        }

        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (playerData == null || globalData == null) {
            return;
        }

        refreshStackableAbility(player, playerData, globalData, Strings.fireBoost, PanelRegistry.FIRE_BOOST_PANEL);
        refreshStackableAbility(player, playerData, globalData, Strings.blizzardBoost, PanelRegistry.BLIZZARD_BOOST_PANEL);
        refreshStackableAbility(player, playerData, globalData, Strings.thunderBoost, PanelRegistry.THUNDER_BOOST_PANEL);
        refreshStackableAbility(player, playerData, globalData, Strings.waterBoost, PanelRegistry.WATER_BOOST_PANEL);
        refreshStackableAbility(player, playerData, globalData, StringsRM.lightBoost, PanelRegistry.LIGHT_BOOST_PANEL);
        refreshStackableAbility(player, playerData, globalData, StringsRM.darknessBoost, PanelRegistry.DARK_BOOST_PANEL);
        refreshStackableAbility(player, playerData, globalData, Strings.criticalBoost, PanelRegistry.SIGHT_UNIT);
        refreshStackableAbility(player, playerData, globalData, StringsRM.attackHaste, PanelRegistry.HASTE_PANEL);
        refreshStackableAbility(player, playerData, globalData, Strings.treasureMagnet, PanelRegistry.DRAW_PANEL);
        refreshStackableAbility(player, playerData, globalData, Strings.jackpot, PanelRegistry.JACKPOT_PANEL);
        refreshStackableAbility(player, playerData, globalData, Strings.luckyStrike, PanelRegistry.LUCKY_LUCKY_PANEL);
        refreshStackableAbility(player, playerData, globalData, Strings.comboPlus, PanelRegistry.COMBO_PLUS_PANEL);
    }

    private static void refreshStackableAbility(
            Player player,
            PlayerData playerData,
            GlobalDataRM globalData,
            String ability,
            ResourceLocation panelId
    ) {
        if (player == null || playerData == null || globalData == null || ability == null || panelId == null) {
            return;
        }

        if (playerData.getAbilityMap() == null) {
            return;
        }

        int currentPanelBonus = getEquippedPanelCount(player, panelId);

        Map<String, int[]> playerOriginals =
                ORIGINAL_STACKABLE_ABILITY_STATES.computeIfAbsent(
                        player.getUUID(),
                        uuid -> new ConcurrentHashMap<>()
                );


        /*
         * First time this panel system touches this stackable ability,
         * capture the true base/original state.
         */
        if (!playerOriginals.containsKey(ability)) {
            int previousPanelBonus = globalData.getOrganizationPanelAbilityBonus(ability);
            int[] current = playerData.getAbilityMap().get(ResourceLocation.parse(ability));

            if (current == null) {
                playerOriginals.put(ability, NO_ORIGINAL_STACKABLE_ABILITY);
            } else {
                int baseOwned = Math.max(0, current[0] - previousPanelBonus);
                int baseEquipped = Math.max(0, current[1] - previousPanelBonus);

                if (baseOwned <= 0 && baseEquipped <= 0) {
                    playerOriginals.put(ability, NO_ORIGINAL_STACKABLE_ABILITY);
                } else {
                    playerOriginals.put(ability, new int[] {baseOwned, baseEquipped});
                }
            }
        }

        int[] original = playerOriginals.get(ability);

        boolean hadNoOriginal = original == NO_ORIGINAL_STACKABLE_ABILITY
                || original == null
                || original.length < 2
                || original[0] < 0
                || original[1] < 0;

        if (currentPanelBonus <= 0) {
            if (hadNoOriginal) {
                playerData.getAbilityMap().remove(ResourceLocation.parse(ability));
            } else {
                playerData.getAbilityMap().put(ResourceLocation.parse(ability), new int[] {original[0], original[1]});
            }

            globalData.setOrganizationPanelAbilityBonus(ability, 0);
            playerOriginals.remove(ability);

            if (playerOriginals.isEmpty()) {
                ORIGINAL_STACKABLE_ABILITY_STATES.remove(player.getUUID());
            }

            return;
        }

        /*
         * Panels equipped:
         * rebuild from original/base + current panel count.
         */
        int baseOwned = hadNoOriginal ? 0 : original[0];
        int baseEquipped = hadNoOriginal ? 0 : original[1];

        playerData.getAbilityMap().put(
                ResourceLocation.parse(ability),
                new int[] {
                        baseOwned + currentPanelBonus,
                        baseEquipped + currentPanelBonus
                }
        );

        globalData.setOrganizationPanelAbilityBonus(ability, currentPanelBonus);
    }

    public static boolean hasPanelHighJump(Player player) {
        return hasAbilityPanelEquipped(player, Strings.highJump);
    }

    public static boolean hasPanelDodgeRoll(Player player) {
        return hasAbilityPanelEquipped(player, Strings.dodgeRoll);
    }

    public static boolean hasPanelAerialDodge(Player player) {
        return hasAbilityPanelEquipped(player, Strings.aerialDodge);
    }

    public static boolean hasPanelQuickRun(Player player) {
        return hasAbilityPanelEquipped(player, Strings.quickRun);
    }

    public static boolean hasPanelGlide(Player player) {
        return hasAbilityPanelEquipped(player, Strings.glide);
    }

}