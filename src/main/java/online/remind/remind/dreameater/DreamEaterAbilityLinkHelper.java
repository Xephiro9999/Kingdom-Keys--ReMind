package online.remind.remind.dreameater;

import net.minecraft.server.level.ServerPlayer;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        bus = EventBusSubscriber.Bus.GAME
)
public class DreamEaterAbilityLinkHelper {

    /*
     * IMPORTANT:
     * This helper treats Dream Eater Link-granted abilities as TEMPORARY ONLY.
     *
     * Any ability ID returned by DreamEaterLinkData through DreamEaterInfoRM
     * is controlled exactly by the currently equipped Dream Eater.
     *
     * This is intentional to purge old duped link abilities.
     *
     * Future-proofing:
     * To add a new Dream Eater later, add its metadata/link supplier to
     * DreamEaterInfoRM. This file should not need another per-Spirit branch.
     */

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
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (playerData == null || globalData == null) {
            clearAllGrants(player, true);
            return;
        }

        String dreamEaterRL = globalData.getDreamEaterRL();

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            clearAllGrants(player, true);
            return;
        }

        List<DreamEaterLinkData.LinkEntry> links = DreamEaterInfo.getLinks(dreamEaterRL);

        if (links == null || links.isEmpty()) {
            clearAllGrants(player, true);
            return;
        }

        applyDreamEaterAbilityLinks(player, playerData, globalData, links);
    }

    public static void onDreamEaterChanged(ServerPlayer player) {
        if (player == null) {
            return;
        }

        PlayerData playerData = PlayerData.get(player);

        if (playerData != null && playerData.getAbilityMap() != null) {
            removeAllDreamEaterLinkAbilities(playerData);
            sync(player);
        }

        refresh(player);
    }

    public static void clearAbilityLinkGrantsForLogout(ServerPlayer player) {
        if (player == null) {
            return;
        }

        PlayerData playerData = PlayerData.get(player);

        if (playerData == null || playerData.getAbilityMap() == null) {
            return;
        }

        boolean changed = removeAllDreamEaterLinkAbilities(playerData);

        if (changed) {
            sync(player);
        }
    }

    private static void applyDreamEaterAbilityLinks(
            ServerPlayer player,
            PlayerData playerData,
            GlobalDataRM globalData,
            List<DreamEaterLinkData.LinkEntry> links
    ) {
        if (player == null || playerData == null || globalData == null) {
            return;
        }

        String dreamEaterRL = globalData.getDreamEaterRL();

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            clearAllGrants(player, true);
            return;
        }

        int dreamEaterLevel = Math.max(1, globalData.getDreamEaterLevel(dreamEaterRL));

        Map<String, Integer> desiredGrants = buildDesiredAbilityGrants(links, dreamEaterLevel);

        boolean changed = enforceExactDreamEaterAbilityState(playerData, desiredGrants);

        if (changed) {
            sync(player);
        }
    }

    private static void clearAllGrants(ServerPlayer player, boolean syncIfChanged) {
        if (player == null) {
            return;
        }

        PlayerData playerData = PlayerData.get(player);

        if (playerData == null || playerData.getAbilityMap() == null) {
            return;
        }

        boolean changed = removeAllDreamEaterLinkAbilities(playerData);

        if (purgeEmptyDreamEaterAbilityEntries(playerData)) {
            changed = true;
        }

        if (changed && syncIfChanged) {
            sync(player);
        }
    }

    private static Map<String, Integer> buildDesiredAbilityGrants(
            List<DreamEaterLinkData.LinkEntry> links,
            int level
    ) {
        Map<String, Integer> desiredGrants = new HashMap<>();

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

            String abilityId = link.abilityId();

            if (abilityId == null || abilityId.isEmpty()) {
                continue;
            }

            desiredGrants.merge(abilityId, 1, Integer::sum);
        }

        return desiredGrants;
    }

    private static boolean enforceExactDreamEaterAbilityState(
            PlayerData playerData,
            Map<String, Integer> desiredGrants
    ) {
        if (playerData == null || playerData.getAbilityMap() == null) {
            return false;
        }

        boolean changed = false;

        Set<String> allDreamEaterAbilityIds = DreamEaterInfo.getAllGrantedAbilityIds();

        for (String abilityId : allDreamEaterAbilityIds) {
            if (abilityId == null || abilityId.isEmpty()) {
                continue;
            }

            int desiredAmount = desiredGrants.getOrDefault(abilityId, 0);

            if (desiredAmount <= 0) {
                if (playerData.getAbilityMap().containsKey(abilityId)) {
                    playerData.getAbilityMap().remove(abilityId);
                    changed = true;
                }

                continue;
            }

            int[] current = playerData.getAbilityMap().get(abilityId);

            if (current == null
                    || current.length < 2
                    || current[0] != desiredAmount
                    || current[1] != desiredAmount) {
                playerData.getAbilityMap().put(abilityId, new int[]{desiredAmount, desiredAmount});
                changed = true;
            }
        }

        return changed;
    }

    private static boolean removeAllDreamEaterLinkAbilities(PlayerData playerData) {
        if (playerData == null || playerData.getAbilityMap() == null) {
            return false;
        }

        boolean changed = false;

        for (String abilityId : DreamEaterInfo.getAllGrantedAbilityIds()) {
            if (abilityId == null || abilityId.isEmpty()) {
                continue;
            }

            if (playerData.getAbilityMap().containsKey(abilityId)) {
                playerData.getAbilityMap().remove(abilityId);
                changed = true;
            }
        }

        return changed;
    }

    private static boolean purgeEmptyDreamEaterAbilityEntries(PlayerData playerData) {
        if (playerData == null || playerData.getAbilityMap() == null) {
            return false;
        }

        boolean changed = false;

        for (String abilityId : DreamEaterInfo.getAllGrantedAbilityIds()) {
            if (abilityId == null || abilityId.isEmpty()) {
                continue;
            }

            int[] data = playerData.getAbilityMap().get(abilityId);

            if (data == null) {
                continue;
            }

            int owned = data.length > 0 ? data[0] : 0;
            int equipped = data.length > 1 ? data[1] : 0;

            if (owned <= 0 && equipped <= 0) {
                playerData.getAbilityMap().remove(abilityId);
                changed = true;
            }
        }

        return changed;
    }

    private static void sync(ServerPlayer player) {
        if (player == null) {
            return;
        }

        PacketHandler.sendTo(new SCSyncPlayerData(player), player);
    }
}