package online.remind.remind.dreameater;

import net.minecraft.resources.ResourceLocation;
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
import online.remind.remind.lib.StringsRM;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
     * That means any ability ID returned by DreamEaterLinkData as a player grant
     * will be controlled exactly by the currently equipped Dream Eater.
     *
     * This is intentional to purge old duped link abilities.
     */

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

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

        if (playerData == null || globalData == null || playerData.getAbilityMap() == null) {
            return;
        }

        List<DreamEaterLinkData.LinkEntry> links = getEquippedDreamEaterLinks(globalData);

        if (links == null || links.isEmpty()) {
            boolean changed = removeAllDreamEaterLinkAbilities(playerData);

            if (changed) {
                sync(player);
            }

            return;
        }

        int level = getEquippedDreamEaterLevel(globalData);

        Map<String, Integer> desiredGrants = buildDesiredAbilityGrants(links, level);

        boolean changed = enforceExactDreamEaterAbilityState(playerData, desiredGrants);

        if (changed) {
            sync(player);
        }
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

    private static List<DreamEaterLinkData.LinkEntry> getEquippedDreamEaterLinks(GlobalDataRM globalData) {
        if (globalData == null) {
            return Collections.emptyList();
        }

        if (isChirithyEquipped(globalData)) {
            return DreamEaterLinkData.getChirithyLinks();
        }

        if (isMeowWowEquipped(globalData)) {
            return DreamEaterLinkData.getMeowWowLinks();
        }

        if (isKomoryBatEquipped(globalData)) {
            return DreamEaterLinkData.getKomoryBatLinks();
        }

        if (isCactuarEquipped(globalData)) {
            return DreamEaterLinkData.getCactuarLinks();
        }

        return Collections.emptyList();
    }

    private static int getEquippedDreamEaterLevel(GlobalDataRM globalData) {
        if (globalData == null) {
            return 1;
        }

        String dreamEaterRL = globalData.getDreamEaterRL();

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            return 1;
        }

        return Math.max(1, globalData.getDreamEaterLevel(dreamEaterRL));
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

        Set<String> allDreamEaterAbilityIds = getAllDreamEaterGrantedAbilityIds();

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

        for (String abilityId : getAllDreamEaterGrantedAbilityIds()) {
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

    private static Set<String> getAllDreamEaterGrantedAbilityIds() {
        Set<String> abilityIds = new HashSet<>();

        collectGrantedAbilityIds(abilityIds, DreamEaterLinkData.getChirithyLinks());
        collectGrantedAbilityIds(abilityIds, DreamEaterLinkData.getMeowWowLinks());
        collectGrantedAbilityIds(abilityIds, DreamEaterLinkData.getKomoryBatLinks());
        collectGrantedAbilityIds(abilityIds, DreamEaterLinkData.getCactuarLinks());

        return abilityIds;
    }

    private static void collectGrantedAbilityIds(
            Set<String> abilityIds,
            List<DreamEaterLinkData.LinkEntry> links
    ) {
        if (abilityIds == null || links == null) {
            return;
        }

        for (DreamEaterLinkData.LinkEntry link : links) {
            if (link == null) {
                continue;
            }

            if (!link.grantsPlayerAbility()) {
                continue;
            }

            String abilityId = link.abilityId();

            if (abilityId == null || abilityId.isEmpty()) {
                continue;
            }

            abilityIds.add(abilityId);
        }
    }

    private static boolean isChirithyEquipped(GlobalDataRM globalData) {
        String dreamEaterRL = globalData.getDreamEaterRL();

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            return false;
        }

        if (dreamEaterRL.equals("kkremind:chirithy")
                || dreamEaterRL.equals("kkremind:dreameater_chirithy")) {
            return true;
        }

        DreamEater dreamEater = getDreamEater(dreamEaterRL);

        return dreamEater != null && StringsRM.chirithy.equals(dreamEater.getName());
    }

    private static boolean isMeowWowEquipped(GlobalDataRM globalData) {
        String dreamEaterRL = globalData.getDreamEaterRL();

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            return false;
        }

        if (dreamEaterRL.equals("kkremind:meow_wow")
                || dreamEaterRL.equals("kkremind:meowwow")
                || dreamEaterRL.equals("kkremind:dreameater_meowwow")
                || dreamEaterRL.equals("kkremind:dreameater_meow_wow")) {
            return true;
        }

        DreamEater dreamEater = getDreamEater(dreamEaterRL);

        return dreamEater != null && StringsRM.meowWow.equals(dreamEater.getName());
    }

    private static boolean isKomoryBatEquipped(GlobalDataRM globalData) {
        String dreamEaterRL = globalData.getDreamEaterRL();

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            return false;
        }

        if (dreamEaterRL.equals("kkremind:komory_bat")
                || dreamEaterRL.equals("kkremind:komorybat")
                || dreamEaterRL.equals("kkremind:dreameater_komory_bat")) {
            return true;
        }

        DreamEater dreamEater = getDreamEater(dreamEaterRL);

        return dreamEater != null && StringsRM.komoryBat.equals(dreamEater.getName());
    }

    private static boolean isCactuarEquipped(GlobalDataRM globalData) {
        String dreamEaterRL = globalData.getDreamEaterRL();

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            return false;
        }

        if (dreamEaterRL.equals("kkremind:dreameater_cactuar")
                || dreamEaterRL.equals("kkremind:cactuar")) {
            return true;
        }

        DreamEater dreamEater = getDreamEater(dreamEaterRL);

        return dreamEater != null
                && (StringsRM.cactuar.equals(dreamEater.getName())
                || "dreameater_cactuar".equals(dreamEater.getName())
                || "cactuar".equals(dreamEater.getName()));
    }

    private static DreamEater getDreamEater(String dreamEaterRL) {
        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            return null;
        }

        try {
            return ModDreamEaters.registry.get(ResourceLocation.parse(dreamEaterRL));
        } catch (Exception e) {
            return null;
        }
    }

    private static void sync(ServerPlayer player) {
        if (player == null) {
            return;
        }

        PacketHandler.sendTo(new SCSyncPlayerData(player), player);
    }
}