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

import java.util.*;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        bus = EventBusSubscriber.Bus.GAME
)
public class DreamEaterAbilityLinkHelper {


    private static final String ITEM_BOOST = "kingdomkeys:ability_item_boost";
    private static final String MP_HASTE = "kingdomkeys:ability_mp_haste";
    //private static final String CONFUSION_BLOCK = "kkremind:ability_confusion_block";
    private static final String LEAF_BRACER = "kingdomkeys:ability_leaf_bracer";

    private static final String HP_BOOST = "kkremind:ability_hp_boost";
    private static final String MP_BOOST = "kkremind:ability_mp_boost";
    private static final String ATTACK_HASTE = "kkremind:attack_haste";
    private static final String POISON_BLOCK = "kkremind:ability_poison_block";


    private static final Map<UUID, Map<String, Integer>> ACTIVE_ABILITY_GRANTS = new HashMap<>();

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

        clearAllGrants(player, false);
    }

    public static void refresh(ServerPlayer player) {
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (playerData == null || globalData == null) {
            clearAllGrants(player, true);
            return;
        }

        // If Meow Wow is not equipped anymore, remove anything Meow Wow granted.
        if (!isMeowWowEquipped(globalData)) {
            clearAllGrants(player, true);
            return;
        }

        // Meow Wow is equipped, so apply only unlocked Meow Wow links.
        applyMeowWowAbilityLinks(player, playerData);
    }

    private static void applyMeowWowAbilityLinks(ServerPlayer player, PlayerData playerData) {
        boolean changed = false;

        UUID uuid = player.getUUID();
        int level = Math.max(1, playerData.getLevel());

        Map<String, Integer> activeAbilities = ACTIVE_ABILITY_GRANTS.computeIfAbsent(uuid, id -> new HashMap<>());
        Set<String> wantedAbilityIds = new HashSet<>();

        for (DreamEaterLinkData.LinkEntry link : DreamEaterLinkData.getMeowWowLinks()) {
            if (!link.grantsPlayerAbility()) {
                continue;
            }

            if (!DreamEaterLinkData.isUnlocked(link, level)) {
                continue;
            }

            String abilityId = link.abilityId();
            int grantAmount = 1;

            wantedAbilityIds.add(abilityId);

            int currentGrant = activeAbilities.getOrDefault(abilityId, 0);

            if (currentGrant != grantAmount) {
                int difference = grantAmount - currentGrant;

                if (addAbilityStack(playerData, abilityId, difference)) {
                    changed = true;
                }

                activeAbilities.put(abilityId, grantAmount);
            }

            if (ensureAbilityEquipped(playerData, abilityId, grantAmount)) {
                changed = true;
            }
        }

        // Remove abilities that were previously granted but are no longer unlocked/wanted.
        for (String activeAbility : new HashSet<>(activeAbilities.keySet())) {
            if (!wantedAbilityIds.contains(activeAbility)) {
                int amount = activeAbilities.getOrDefault(activeAbility, 0);

                if (amount > 0 && addAbilityStack(playerData, activeAbility, -amount)) {
                    changed = true;
                }

                activeAbilities.remove(activeAbility);
            }
        }

        if (activeAbilities.isEmpty()) {
            ACTIVE_ABILITY_GRANTS.remove(uuid);
        }

        if (changed) {
            PacketHandler.sendTo(new SCSyncPlayerData(player), player);
        }
    }

    private static void clearAllGrants(ServerPlayer player, boolean sync) {
        PlayerData playerData = PlayerData.get(player);

        if (playerData == null) {
            ACTIVE_ABILITY_GRANTS.remove(player.getUUID());
            return;
        }

        Map<String, Integer> activeAbilities = ACTIVE_ABILITY_GRANTS.remove(player.getUUID());

        if (activeAbilities == null || activeAbilities.isEmpty()) {
            return;
        }

        boolean changed = false;

        for (Map.Entry<String, Integer> entry : activeAbilities.entrySet()) {
            String abilityId = entry.getKey();
            int amount = entry.getValue();

            if (amount > 0 && addAbilityStack(playerData, abilityId, -amount)) {
                changed = true;
            }
        }

        if (purgeEmptyMeowWowAbilityEntries(playerData)) {
            changed = true;
        }

        if (sync && changed) {
            PacketHandler.sendTo(new SCSyncPlayerData(player), player);
        }
    }

    private static boolean addAbilityStack(PlayerData playerData, String abilityId, int amount) {
        if (playerData == null || abilityId == null || abilityId.isEmpty() || amount == 0) {
            return false;
        }

        if (playerData.getAbilityMap() == null) {
            return false;
        }

        int[] data = playerData.getAbilityMap().get(abilityId);

        if (data == null) {
            if (amount <= 0) {
                return false;
            }

            playerData.getAbilityMap().put(abilityId, new int[]{amount, amount});
            return true;
        }

        int oldOwned = data.length > 0 ? data[0] : 0;
        int oldEquipped = data.length > 1 ? data[1] : 0;

        int newOwned = Math.max(0, oldOwned + amount);
        int newEquipped = Math.max(0, oldEquipped + amount);

        // Important fix:
        // If the player owns 0 and has 0 equipped, remove the ability entry completely.
        if (newOwned <= 0 && newEquipped <= 0) {
            playerData.getAbilityMap().remove(abilityId);
            return true;
        }

        data[0] = newOwned;

        if (data.length > 1) {
            data[1] = newEquipped;
        }

        return oldOwned != newOwned || oldEquipped != newEquipped;
    }

    private static boolean purgeEmptyMeowWowAbilityEntries(PlayerData playerData) {
        if (playerData == null || playerData.getAbilityMap() == null) {
            return false;
        }

        boolean changed = false;

        for (DreamEaterLinkData.LinkEntry link : DreamEaterLinkData.getMeowWowLinks()) {
            if (!link.grantsPlayerAbility()) {
                continue;
            }

            String abilityId = link.abilityId();

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

    private static boolean ensureAbilityEquipped(PlayerData playerData, String abilityId, int minimum) {
        if (playerData == null || playerData.getAbilityMap() == null) {
            return false;
        }

        int[] data = playerData.getAbilityMap().get(abilityId);

        if (data == null) {
            playerData.getAbilityMap().put(abilityId, new int[]{minimum, minimum});
            return true;
        }

        boolean changed = false;

        if (data.length > 0 && data[0] < minimum) {
            data[0] = minimum;
            changed = true;
        }

        if (data.length > 1 && data[1] < minimum) {
            data[1] = minimum;
            changed = true;
        }

        return changed;
    }

    private static boolean isMeowWowEquipped(GlobalDataRM globalData) {
        String dreamEaterRL = globalData.getDreamEaterRL();

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            return false;
        }

        // Direct registry string fallback
        if (dreamEaterRL.equals("kkremind:meow_wow") || dreamEaterRL.equals("kkremind:meowwow")) {
            return true;
        }

        try {
            DreamEater dreamEater = ModDreamEaters.registry.get(ResourceLocation.parse(dreamEaterRL));

            if (dreamEater == null) {
                return false;
            }

            return StringsRM.meowWow.equals(dreamEater.getName());
        } catch (Exception e) {
            return false;
        }
    }

    private record AbilityGrant(String abilityId, int amount) {
    }
}