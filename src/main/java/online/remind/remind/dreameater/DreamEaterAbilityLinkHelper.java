package online.remind.remind.dreameater;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        bus = EventBusSubscriber.Bus.GAME
)
public class DreamEaterAbilityLinkHelper {

    /*
     * IMPORTANT:
     * Dream Eater Ability Links are now treated like FREE accessory/passive abilities.
     *
     * They are NOT written into Kingdom Keys PlayerData abilityMap anymore.
     * That means they should NOT consume AP and should NOT block normal ability equipping.
     *
     * The active link abilities are stored separately in player persistent data.
     *
     * To make an ability effect recognize these, use:
     *
     * DreamEaterAbilityLinkHelper.hasAbility(player, "ability_magic_haste")
     *
     * or merge DreamEaterAbilityLinkHelper.getAccessoryAbilityIds(player)
     * into your accessory/passive ability display.
     */

    private static final String ROOT_KEY = "kkremind_dream_eater_link_accessory_abilities";
    private static final String ABILITIES_KEY = "Abilities";
    private static final String DREAM_EATER_RL_KEY = "DreamEaterRL";
    private static final String DREAM_EATER_LEVEL_KEY = "DreamEaterLevel";

    /*
     * One-time migration flag.
     *
     * The old helper wrote Dream Eater link abilities directly into PlayerData abilityMap.
     * That is what caused AP to be consumed.
     *
     * This purge removes those old AP-eating entries once.
     */
    private static final String LEGACY_PURGE_DONE_KEY = "kkremind_dream_eater_legacy_abilitymap_purge_done";

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        purgeLegacyAbilityMapGrants(player);
        refresh(player);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        /*
         * Once per second is enough.
         * This keeps equipped link abilities synced without constantly writing data.
         */
        if (player.tickCount % 20 != 0) {
            return;
        }

        purgeLegacyAbilityMapGrants(player);
        refresh(player);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        clearAbilityLinkGrantsForLogout(player);
    }

    public static void refresh(ServerPlayer player) {
        if (player == null) {
            return;
        }

        PlayerData playerData = PlayerData.get(player);
        DreamEaterVirtualAbilityBridge.rememberOwner(playerData, player);

        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null) {
            clearAllGrants(player);
            return;
        }

        String dreamEaterRL = globalData.getDreamEaterRL();

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            clearAllGrants(player);
            return;
        }

        java.util.List<DreamEaterLinkData.LinkEntry> links = DreamEaterInfo.getLinks(dreamEaterRL);

        if (links == null || links.isEmpty()) {
            clearAllGrants(player);
            return;
        }

        int dreamEaterLevel = Math.max(1, globalData.getDreamEaterLevel(dreamEaterRL));

        Set<String> desiredGrants = buildDesiredAbilityGrants(links, dreamEaterLevel);

        enforceExactDreamEaterAbilityState(player, dreamEaterRL, dreamEaterLevel, desiredGrants);
    }

    public static void onDreamEaterChanged(ServerPlayer player) {
        if (player == null) {
            return;
        }

        /*
         * Clear the current virtual accessory grants immediately,
         * then rebuild them from the newly equipped Dream Eater.
         */
        clearAllGrants(player);
        refresh(player);
    }

    public static void clearAbilityLinkGrantsForLogout(ServerPlayer player) {
        if (player == null) {
            return;
        }

        /*
         * These are temporary active grants.
         * They will be rebuilt after login from the equipped Dream Eater.
         */
        clearAllGrants(player);
    }

    private static Set<String> buildDesiredAbilityGrants(
            java.util.List<DreamEaterLinkData.LinkEntry> links,
            int level
    ) {
        Set<String> desiredGrants = new HashSet<>();

        if (links == null || links.isEmpty()) {
            return desiredGrants;
        }

        level = Math.max(1, level);

        for (DreamEaterLinkData.LinkEntry link : links) {
            if (link == null) {
                continue;
            }

            if (!link.grantsPlayerAbility()) {
                continue;
            }

            if (!DreamEaterLinkData.isUnlocked(link, level)) {
                continue;
            }

            String abilityId = normalizeAbilityId(link.abilityId());

            if (abilityId.isEmpty()) {
                continue;
            }

            desiredGrants.add(abilityId);
        }

        return desiredGrants;
    }

    private static boolean enforceExactDreamEaterAbilityState(
            ServerPlayer player,
            String dreamEaterRL,
            int dreamEaterLevel,
            Set<String> desiredGrants
    ) {
        if (player == null) {
            return false;
        }

        Set<String> currentGrants = getAccessoryAbilityIds(player);

        CompoundTag root = getOrCreateRoot(player);
        String currentRL = root.getString(DREAM_EATER_RL_KEY);
        int currentLevel = root.getInt(DREAM_EATER_LEVEL_KEY);

        if (currentGrants.equals(desiredGrants)
                && currentRL.equals(dreamEaterRL)
                && currentLevel == dreamEaterLevel) {
            return false;
        }

        ListTag list = new ListTag();

        TreeSet<String> sorted = new TreeSet<>(desiredGrants);

        for (String abilityId : sorted) {
            if (abilityId == null || abilityId.isEmpty()) {
                continue;
            }

            list.add(StringTag.valueOf(abilityId));
        }

        root.putString(DREAM_EATER_RL_KEY, dreamEaterRL == null ? "" : dreamEaterRL);
        root.putInt(DREAM_EATER_LEVEL_KEY, Math.max(1, dreamEaterLevel));
        root.put(ABILITIES_KEY, list);

        player.getPersistentData().put(ROOT_KEY, root);

        return true;
    }

    private static void clearAllGrants(ServerPlayer player) {
        if (player == null) {
            return;
        }

        player.getPersistentData().remove(ROOT_KEY);
    }

    /**
     * Returns true if the player has this ability from Dream Eater Ability Links.
     *
     * Use this in passive/effect checks.
     */
    public static boolean hasAbility(Player player, String abilityId) {
        if (player == null) {
            return false;
        }

        String normalized = normalizeAbilityId(abilityId);

        if (normalized.isEmpty()) {
            return false;
        }

        return getAccessoryAbilityIds(player).contains(normalized);
    }

    /**
     * Alias for readability.
     */
    public static boolean hasDreamEaterLinkAbility(Player player, String abilityId) {
        return hasAbility(player, abilityId);
    }

    /**
     * Alias for treating Dream Eater Ability Links as accessory-style passives.
     */
    public static boolean hasAccessoryAbility(Player player, String abilityId) {
        return hasAbility(player, abilityId);
    }

    /**
     * Use this for UI display.
     *
     * These should be shown under something like:
     * "Spirit Link Abilities"
     * or merged into "Accessory Abilities."
     */
    public static Set<String> getAccessoryAbilityIds(Player player) {
        Set<String> abilities = new HashSet<>();

        if (player == null) {
            return abilities;
        }

        /*
         * First try to compute directly from the current Dream Eater data.
         * This helps the client-side ability menu display the fake abilities
         * without needing them to exist in Kingdom Keys abilityMap.
         */
        try {
            GlobalDataRM globalData = ModDataRM.getGlobal(player);

            if (globalData != null) {
                String dreamEaterRL = globalData.getDreamEaterRL();

                if (dreamEaterRL != null && !dreamEaterRL.isEmpty()) {
                    java.util.List<DreamEaterLinkData.LinkEntry> links = DreamEaterInfo.getLinks(dreamEaterRL);

                    if (links != null && !links.isEmpty()) {
                        int dreamEaterLevel = Math.max(1, globalData.getDreamEaterLevel(dreamEaterRL));
                        abilities.addAll(buildDesiredAbilityGrants(links, dreamEaterLevel));
                    }
                }
            }
        } catch (Exception ignored) {
        }

        if (!abilities.isEmpty()) {
            return abilities;
        }

        /*
         * Fallback to stored persistent data.
         * This is what the server refresh writes.
         */
        CompoundTag data = player.getPersistentData();

        if (!data.contains(ROOT_KEY, Tag.TAG_COMPOUND)) {
            return abilities;
        }

        CompoundTag root = data.getCompound(ROOT_KEY);

        if (!root.contains(ABILITIES_KEY, Tag.TAG_LIST)) {
            return abilities;
        }

        ListTag list = root.getList(ABILITIES_KEY, Tag.TAG_STRING);

        for (int i = 0; i < list.size(); i++) {
            String abilityId = normalizeAbilityId(list.getString(i));

            if (!abilityId.isEmpty()) {
                abilities.add(abilityId);
            }
        }

        return abilities;
    }

    public static boolean hasAnyDreamEaterAbilityLinks(Player player) {
        return !getAccessoryAbilityIds(player).isEmpty();
    }

    public static String getCurrentDreamEaterRL(Player player) {
        if (player == null) {
            return "";
        }

        CompoundTag data = player.getPersistentData();

        if (!data.contains(ROOT_KEY, Tag.TAG_COMPOUND)) {
            return "";
        }

        return data.getCompound(ROOT_KEY).getString(DREAM_EATER_RL_KEY);
    }

    public static int getCurrentDreamEaterLevel(Player player) {
        if (player == null) {
            return 0;
        }

        CompoundTag data = player.getPersistentData();

        if (!data.contains(ROOT_KEY, Tag.TAG_COMPOUND)) {
            return 0;
        }

        return data.getCompound(ROOT_KEY).getInt(DREAM_EATER_LEVEL_KEY);
    }

    private static CompoundTag getOrCreateRoot(Player player) {
        CompoundTag data = player.getPersistentData();

        if (!data.contains(ROOT_KEY, Tag.TAG_COMPOUND)) {
            data.put(ROOT_KEY, new CompoundTag());
        }

        return data.getCompound(ROOT_KEY);
    }

    /**
     * One-time cleanup for old saves.
     *
     * The previous helper wrote Dream Eater link abilities into PlayerData abilityMap.
     * That caused AP cost issues.
     *
     * This removes those old entries once, then marks the player as migrated.
     */
    private static void purgeLegacyAbilityMapGrants(ServerPlayer player) {
        if (player == null) {
            return;
        }

        PlayerData playerData = PlayerData.get(player);

        if (playerData == null || playerData.getAbilityMap() == null) {
            return;
        }

        boolean changed = false;
        Map<ResourceLocation, int[]> abilityMap = playerData.getAbilityMap();

        Set<String> idsToRemove = new HashSet<>();

        for (String rawAbilityId : DreamEaterInfo.getAllGrantedAbilityIds()) {
            collectAbilityIdVariants(idsToRemove, rawAbilityId);
        }

        for (String currentVirtualAbilityId : getAccessoryAbilityIds(player)) {
            collectAbilityIdVariants(idsToRemove, currentVirtualAbilityId);
        }

        for (String abilityId : idsToRemove) {
            if (abilityId == null || abilityId.isEmpty()) {
                continue;
            }

            if (abilityMap.containsKey(ResourceLocation.parse(abilityId))) {
                abilityMap.remove(ResourceLocation.parse(abilityId));
                changed = true;
                System.out.println("[KKReMind/DELinks] Removed AP-eating Dream Eater abilityMap entry: " + abilityId + " from " + player.getGameProfile().getName());
            }
        }

        if (changed) {
            sync(player);
        }
    }

    private static void collectAbilityIdVariants(java.util.Set<String> out, String rawAbilityId) {
        if (out == null || rawAbilityId == null || rawAbilityId.isEmpty()) {
            return;
        }

        String id = rawAbilityId.trim().toLowerCase(java.util.Locale.ROOT);

        if (id.isEmpty()) {
            return;
        }

        out.add(id);

        String normalized = normalizeAbilityId(id);
        out.add(normalized);

        if (id.contains(":")) {
            String[] split = id.split(":", 2);

            if (split.length == 2) {
                String namespace = split[0];
                String path = split[1];

                out.add(namespace + ":" + path);
                out.add(path);

                String normalizedPath = normalizeAbilityId(path);

                out.add(normalizedPath);
                out.add(namespace + ":" + normalizedPath);

                if (path.startsWith("ability_")) {
                    String noPrefix = path.substring("ability_".length());

                    out.add(noPrefix);
                    out.add(namespace + ":" + noPrefix);
                } else {
                    out.add("ability_" + path);
                    out.add(namespace + ":ability_" + path);
                }
            }
        } else {
            out.add("kingdomkeys:" + id);
            out.add("kingdomkeys:" + normalized);
            out.add("kkremind:" + id);
            out.add("kkremind:" + normalized);

            if (id.startsWith("ability_")) {
                String noPrefix = id.substring("ability_".length());

                out.add(noPrefix);
                out.add("kingdomkeys:" + noPrefix);
                out.add("kkremind:" + noPrefix);
            }
        }
    }

    public static String normalizeAbilityId(String abilityId) {
        if (abilityId == null) {
            return "";
        }

        String id = abilityId.trim().toLowerCase(Locale.ROOT);

        if (id.isEmpty()) {
            return "";
        }

        /*
         * Most Kingdom Keys ability IDs in your setup use ability_.
         *
         * If this is a namespaced ID like kingdomkeys:whatever,
         * do not force ability_ onto it.
         */
        if (!id.contains(":") && !id.startsWith("ability_")) {
            id = "ability_" + id;
        }

        return id;
    }

    private static void sync(ServerPlayer player) {
        if (player == null) {
            return;
        }

        PacketHandler.sendTo(new SCSyncPlayerData(player), player);
    }
}