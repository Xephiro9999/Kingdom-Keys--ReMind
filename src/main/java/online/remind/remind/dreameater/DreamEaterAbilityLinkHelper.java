package online.remind.remind.dreameater;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.command.ExpCommand;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        bus = EventBusSubscriber.Bus.GAME
)
public final class DreamEaterAbilityLinkHelper {

    /*
     * ============================================================
     * DREAM EATER PERMANENT ABILITY LINKS
     * ============================================================
     *
     * Dream Eater data is the source of truth for how many
     * PERMANENT Dream Eater copies should exist.
     *
     * We now manage the REAL Kingdom Keys permanent ability list:
     *
     *     PlayerData.getPAbilitiesList()
     *
     * This is critical because addPAbility() adds an entry to that
     * list as well as adding the ability to abilityMap.
     *
     * The old runaway-grant bug polluted BOTH.
     *
     * This helper:
     *
     * 1. Reads every unlocked Dream Eater.
     * 2. Reads that Dream Eater's saved level.
     * 3. Reads its unlocked Ability Links.
     * 4. Calculates the exact permanent DE contribution.
     * 5. Repairs excess permanent entries.
     * 6. Rebuilds normal KK progression when a repair is needed.
     * 7. Uses addPAbility() for future newly-earned Link abilities.
     */


    // ============================================================
    // REPAIR VERSION
    // ============================================================

    /*
     * Increase this if another destructive migration is ever needed.
     *
     * Version 3 specifically repairs the permanent_abilities list
     * polluted by the repeated addPAbility bug.
     */
    private static final String REPAIR_VERSION_KEY =
            "kkremind_de_permanent_ability_repair_version";

    private static final int CURRENT_REPAIR_VERSION = 3;


    // ============================================================
    // LEGACY BOOKKEEPING
    // ============================================================

    private static final String LEGACY_TEMP_ROOT =
            "kkremind_dream_eater_ability_links";

    private static final String LEGACY_VIRTUAL_ROOT =
            "kkremind_dream_eater_link_accessory_abilities";

    private static final String LEGACY_PURGE_FLAG =
            "kkremind_dream_eater_legacy_abilitymap_purge_done";

    private static final String OLD_PERMANENT_ROOT =
            "kkremind_dream_eater_permanent_link_abilities";


    // ============================================================
    // STACKABLE ABILITIES
    // ============================================================

    /*
     * UNIQUE is the default.
     *
     * Only abilities listed here are allowed multiple Dream Eater
     * permanent copies.
     */
    private static final Set<String> STACKABLE_ABILITIES =
            Set.of(

                    // Kingdom Keys
                    "kingdomkeys:ability_item_boost",
                    "kingdomkeys:ability_mp_haste",

                    "kingdomkeys:ability_treasure_magnet",
                    "kingdomkeys:ability_jackpot",
                    "kingdomkeys:ability_lucky_lucky",

                    "kingdomkeys:ability_combo_plus",
                    "kingdomkeys:ability_air_combo_plus",

                    "kingdomkeys:ability_fire_boost",
                    "kingdomkeys:ability_blizzard_boost",
                    "kingdomkeys:ability_thunder_boost",
                    "kingdomkeys:ability_water_boost",

                    // KKReMind
                    "kkremind:ability_hp_boost",
                    "kkremind:ability_mp_boost",
                    "kkremind:ability_attack_haste",

                    "kkremind:ability_light_boost",
                    "kkremind:ability_darkness_boost"
            );


    private DreamEaterAbilityLinkHelper() {
    }


    // ============================================================
    // LOGIN
    // ============================================================

    @SubscribeEvent
    public static void onPlayerLogin(
            PlayerEvent.PlayerLoggedInEvent event
    ) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        clearLegacyBookkeeping(player);

        refresh(player);
    }


    // ============================================================
    // TICK
    // ============================================================

    @SubscribeEvent
    public static void onPlayerTick(
            PlayerTickEvent.Pre event
    ) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        /*
         * Once per second is enough.
         *
         * This is an audit, NOT a blind grant.
         */
        if (player.tickCount % 20 != 0) {
            return;
        }

        refresh(player);
    }


    // ============================================================
    // CLONE / RESPAWN
    // ============================================================

    @SubscribeEvent
    public static void onPlayerClone(
            PlayerEvent.Clone event
    ) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        CompoundTag oldData =
                event
                        .getOriginal()
                        .getPersistentData();

        CompoundTag newData =
                player
                        .getPersistentData();

        /*
         * Preserve migration state across death/clone.
         */
        if (oldData.contains(REPAIR_VERSION_KEY)) {

            newData.putInt(
                    REPAIR_VERSION_KEY,
                    oldData.getInt(REPAIR_VERSION_KEY)
            );
        }

        refresh(player);
    }


    // ============================================================
    // DREAM EATER CHANGE
    // ============================================================

    public static void onDreamEaterChanged(
            ServerPlayer player
    ) {
        if (player == null
                || player.level().isClientSide) {
            return;
        }

        /*
         * Permanent Link rewards do NOT disappear when changing
         * Dream Eaters.
         */
        refresh(player);
    }


    // ============================================================
    // OLD COMPATIBILITY METHOD
    // ============================================================

    public static void clearAbilityLinkGrantsForLogout(
            ServerPlayer player
    ) {
        /*
         * Intentionally empty.
         *
         * Dream Eater Link abilities are permanent.
         */
    }


    // ============================================================
    // MAIN REFRESH
    // ============================================================

    public static void refresh(
            ServerPlayer player
    ) {
        if (player == null
                || player.level().isClientSide) {
            return;
        }

        PlayerData playerData =
                PlayerData.get(player);

        GlobalDataRM globalData =
                ModDataRM.getGlobal(player);

        if (playerData == null
                || globalData == null
                || playerData.getAbilityMap() == null
                || playerData.getPAbilitiesList() == null) {
            return;
        }


        /*
         * Exact permanent contribution currently deserved from
         * ALL unlocked Dream Eaters.
         */
        Map<ResourceLocation, Integer> desired =
                buildExactDreamEaterGrants(
                        globalData
                );


        /*
         * Every ability ID that can EVER be granted by a Dream
         * Eater.
         *
         * This lets us clean garbage from locked/unearned Links too.
         */
        Set<ResourceLocation> allDreamEaterAbilityIds =
                getAllDreamEaterAbilityIds();


        // ========================================================
        // ONE-TIME CORRUPTED SAVE REPAIR
        // ========================================================

        int repairVersion =
                player
                        .getPersistentData()
                        .getInt(
                                REPAIR_VERSION_KEY
                        );

        if (repairVersion < CURRENT_REPAIR_VERSION) {

            repairCorruptedPermanentAbilities(
                    player,
                    playerData,
                    desired,
                    allDreamEaterAbilityIds
            );

            player
                    .getPersistentData()
                    .putInt(
                            REPAIR_VERSION_KEY,
                            CURRENT_REPAIR_VERSION
                    );

            return;
        }


        // ========================================================
        // NORMAL ONGOING RECONCILIATION
        // ========================================================

        boolean changed =
                reconcilePermanentAbilityList(
                        player,
                        playerData,
                        desired,
                        allDreamEaterAbilityIds
                );


        /*
         * Unique abilities should never display multiple copies,
         * even if normal KK progression and a DE both grant it.
         */
        if (clampUniqueAbilities(
                player,
                playerData,
                desired
        )) {

            changed = true;
        }


        if (changed) {
            sync(player);
        }
    }


    // ============================================================
    // EXACT DREAM EATER GRANTS
    // ============================================================

    private static Map<ResourceLocation, Integer>
    buildExactDreamEaterGrants(
            GlobalDataRM globalData
    ) {
        Map<ResourceLocation, Integer> desired =
                new LinkedHashMap<>();

        if (globalData == null) {
            return desired;
        }


        /*
         * IMPORTANT:
         *
         * Scan EVERY registered Dream Eater.
         *
         * Not merely the currently equipped Dream Eater.
         */
        for (DreamEater dreamEater :
                ModDreamEaters.registry
                        .stream()
                        .toList()) {

            if (dreamEater == null
                    || dreamEater.getRegistryName() == null) {
                continue;
            }

            String dreamEaterRL =
                    dreamEater
                            .getRegistryName()
                            .toString();


            // ----------------------------------------------------
            // Must actually be unlocked.
            // ----------------------------------------------------

            if (!globalData.hasDreamEaterUnlocked(
                    dreamEaterRL
            )) {
                continue;
            }


            // ----------------------------------------------------
            // Saved level for THIS Dream Eater.
            // ----------------------------------------------------

            int dreamEaterLevel =
                    Math.max(
                            1,
                            globalData.getDreamEaterLevel(
                                    dreamEaterRL
                            )
                    );


            // ----------------------------------------------------
            // Link tree for THIS Dream Eater.
            // ----------------------------------------------------

            List<DreamEaterLinkData.LinkEntry> links =
                    DreamEaterInfo.getLinks(
                            dreamEaterRL
                    );

            if (links == null
                    || links.isEmpty()) {
                continue;
            }


            // ----------------------------------------------------
            // Count unlocked grants.
            // ----------------------------------------------------

            for (DreamEaterLinkData.LinkEntry link :
                    links) {

                if (link == null) {
                    continue;
                }

                if (!link.grantsPlayerAbility()) {
                    continue;
                }

                if (!DreamEaterLinkData.isUnlocked(
                        link,
                        dreamEaterLevel
                )) {
                    continue;
                }


                ResourceLocation abilityRL =
                        parseAbilityId(
                                link.abilityId()
                        );

                if (abilityRL == null) {
                    continue;
                }


                // -----------------------------------------------
                // Stackable.
                // -----------------------------------------------

                if (isStackableLinkAbility(
                        abilityRL
                )) {

                    desired.merge(
                            abilityRL,
                            1,
                            Integer::sum
                    );

                    continue;
                }


                // -----------------------------------------------
                // Unique.
                // -----------------------------------------------

                desired.put(
                        abilityRL,
                        1
                );
            }
        }

        return desired;
    }


    // ============================================================
    // GET ALL POSSIBLE DREAM EATER ABILITY IDS
    // ============================================================

    private static Set<ResourceLocation>
    getAllDreamEaterAbilityIds() {

        Set<ResourceLocation> abilities =
                new HashSet<>();


        for (DreamEater dreamEater :
                ModDreamEaters.registry
                        .stream()
                        .toList()) {

            if (dreamEater == null
                    || dreamEater.getRegistryName() == null) {
                continue;
            }

            String dreamEaterRL =
                    dreamEater
                            .getRegistryName()
                            .toString();


            List<DreamEaterLinkData.LinkEntry> links =
                    DreamEaterInfo.getLinks(
                            dreamEaterRL
                    );

            if (links == null
                    || links.isEmpty()) {
                continue;
            }


            for (DreamEaterLinkData.LinkEntry link :
                    links) {

                if (link == null
                        || !link.grantsPlayerAbility()) {
                    continue;
                }


                ResourceLocation abilityRL =
                        parseAbilityId(
                                link.abilityId()
                        );

                if (abilityRL != null) {

                    abilities.add(
                            abilityRL
                    );
                }
            }
        }

        return abilities;
    }


    // ============================================================
    // ONE-TIME CORRUPTION REPAIR
    // ============================================================

    private static void repairCorruptedPermanentAbilities(
            ServerPlayer player,
            PlayerData playerData,
            Map<ResourceLocation, Integer> desired,
            Set<ResourceLocation> allDreamEaterAbilityIds
    ) {
        /*
         * ========================================================
         * IMPORTANT MIGRATION
         * ========================================================
         *
         * The bad helper repeatedly called:
         *
         *     addPAbility(...)
         *
         * Therefore we have to repair PlayerData's permanent
         * ability list itself.
         *
         *
         * Migration strategy:
         *
         * 1. Preserve permanent abilities that are NOT Dream Eater
         *    Link abilities.
         *
         * 2. Remove ALL old permanent entries for Dream Eater
         *    ability IDs.
         *
         * 3. Put back exactly what current Dream Eater data says
         *    the player has earned.
         *
         * 4. Ask Kingdom Keys to rebuild normal level progression.
         */


        List<ResourceLocation> oldPermanent =
                new ArrayList<>(
                        playerData.getPAbilitiesList()
                );


        List<ResourceLocation> cleanedPermanent =
                new ArrayList<>();


        int removed =
                0;


        // --------------------------------------------------------
        // Preserve non-DE permanent abilities.
        // --------------------------------------------------------

        for (ResourceLocation abilityRL :
                oldPermanent) {

            if (abilityRL == null) {
                continue;
            }

            if (allDreamEaterAbilityIds.contains(
                    abilityRL
            )) {

                removed++;

                continue;
            }

            cleanedPermanent.add(
                    abilityRL
            );
        }


        int added =
                0;


        // --------------------------------------------------------
        // Add exact DE permanent contribution.
        // --------------------------------------------------------

        for (Map.Entry<ResourceLocation, Integer> entry :
                desired.entrySet()) {

            ResourceLocation abilityRL =
                    entry.getKey();

            int amount =
                    Math.max(
                            0,
                            entry.getValue()
                    );

            if (abilityRL == null
                    || amount <= 0) {
                continue;
            }


            if (!isStackableLinkAbility(
                    abilityRL
            )) {

                amount =
                        1;
            }


            for (int i = 0; i < amount; i++) {

                cleanedPermanent.add(
                        abilityRL
                );

                added++;
            }
        }


        // --------------------------------------------------------
        // Replace the actual permanent list.
        // --------------------------------------------------------

        playerData
                .getPAbilitiesList()
                .clear();

        playerData
                .getPAbilitiesList()
                .addAll(
                        cleanedPermanent
                );


        System.out.println(
                "[KKReMind/DELinks] Repairing corrupted permanent abilities for "
                        + player.getGameProfile().getName()
                        + ". Removed "
                        + removed
                        + " old DE permanent entries; rebuilding with "
                        + added
                        + " exact DE entries."
        );


        /*
         * ========================================================
         * REBUILD KINGDOM KEYS PLAYER ABILITIES
         * ========================================================
         *
         * This clears/recalculates normal level-up progression and
         * then reapplies the newly-cleaned permanent list.
         */
        ExpCommand.fix(
                playerData,
                player
        );


        /*
         * If a UNIQUE ability was earned normally AND exists as a
         * permanent DE reward, ExpCommand.fix can temporarily
         * create two owned copies.
         *
         * Collapse unique abilities back to x1.
         */
        boolean clamped =
                clampUniqueAbilities(
                        player,
                        playerData,
                        desired
                );


        if (clamped) {
            sync(player);
        }


        System.out.println(
                "[KKReMind/DELinks] Permanent ability repair complete for "
                        + player.getGameProfile().getName()
        );
    }


    // ============================================================
    // NORMAL PERMANENT LIST RECONCILIATION
    // ============================================================

    private static boolean reconcilePermanentAbilityList(
            ServerPlayer player,
            PlayerData playerData,
            Map<ResourceLocation, Integer> desired,
            Set<ResourceLocation> allDreamEaterAbilityIds
    ) {
        boolean changed =
                false;


        List<ResourceLocation> permanent =
                playerData.getPAbilitiesList();


        /*
         * ========================================================
         * FIRST CHECK FOR EXCESS
         * ========================================================
         *
         * Excess DE permanent entries are NEVER adopted as
         * "legitimate base copies."
         *
         * If they exist, rebuild the DE portion exactly.
         */
        boolean hasExcess =
                false;


        for (ResourceLocation abilityRL :
                allDreamEaterAbilityIds) {

            int actualPermanent =
                    countOccurrences(
                            permanent,
                            abilityRL
                    );

            int desiredPermanent =
                    Math.max(
                            0,
                            desired.getOrDefault(
                                    abilityRL,
                                    0
                            )
                    );


            if (!isStackableLinkAbility(
                    abilityRL
            )
                    && desiredPermanent > 0) {

                desiredPermanent =
                        1;
            }


            if (actualPermanent > desiredPermanent) {

                hasExcess =
                        true;

                System.out.println(
                        "[KKReMind/DELinks] Excess permanent copies detected: "
                                + abilityRL
                                + " actual="
                                + actualPermanent
                                + " desired="
                                + desiredPermanent
                );
            }
        }


        // ========================================================
        // REPAIR EXCESS
        // ========================================================

        if (hasExcess) {

            List<ResourceLocation> cleaned =
                    new ArrayList<>();


            /*
             * Preserve permanent abilities unrelated to DE Links.
             */
            for (ResourceLocation abilityRL :
                    permanent) {

                if (abilityRL == null) {
                    continue;
                }

                if (!allDreamEaterAbilityIds.contains(
                        abilityRL
                )) {

                    cleaned.add(
                            abilityRL
                    );
                }
            }


            /*
             * Add exact DE entries.
             */
            for (Map.Entry<ResourceLocation, Integer> entry :
                    desired.entrySet()) {

                ResourceLocation abilityRL =
                        entry.getKey();

                int amount =
                        Math.max(
                                0,
                                entry.getValue()
                        );


                if (abilityRL == null
                        || amount <= 0) {
                    continue;
                }


                if (!isStackableLinkAbility(
                        abilityRL
                )) {

                    amount =
                            1;
                }


                for (int i = 0; i < amount; i++) {

                    cleaned.add(
                            abilityRL
                    );
                }
            }


            permanent.clear();

            permanent.addAll(
                    cleaned
            );


            /*
             * Rebuild abilityMap from:
             *
             * normal progression
             * +
             * cleaned permanent list
             */
            ExpCommand.fix(
                    playerData,
                    player
            );


            System.out.println(
                    "[KKReMind/DELinks] Rebuilt ability data after permanent-list correction for "
                            + player.getGameProfile().getName()
            );


            return true;
        }


        // ========================================================
        // ADD MISSING NEW LINK REWARDS
        // ========================================================

        /*
         * This is the normal level-up path.
         *
         * Example:
         *
         * Komory reaches the second Attack Haste Link.
         *
         * desired = 2
         * permanent currently = 1
         *
         * addPAbility() exactly once.
         */
        for (Map.Entry<ResourceLocation, Integer> entry :
                desired.entrySet()) {

            ResourceLocation abilityRL =
                    entry.getKey();

            int desiredPermanent =
                    Math.max(
                            0,
                            entry.getValue()
                    );


            if (abilityRL == null
                    || desiredPermanent <= 0) {
                continue;
            }


            if (!isStackableLinkAbility(
                    abilityRL
            )) {

                desiredPermanent =
                        1;
            }


            int actualPermanent =
                    countOccurrences(
                            permanent,
                            abilityRL
                    );


            while (actualPermanent < desiredPermanent) {

                int before =
                        countOccurrences(
                                permanent,
                                abilityRL
                        );


                playerData.addPAbility(
                        abilityRL
                );


                int after =
                        countOccurrences(
                                permanent,
                                abilityRL
                        );


                if (after <= before) {

                    System.err.println(
                            "[KKReMind/DELinks] addPAbility failed for "
                                    + abilityRL
                    );

                    break;
                }


                actualPermanent =
                        after;


                changed =
                        true;


                System.out.println(
                        "[KKReMind/DELinks] Granted permanent Link ability "
                                + abilityRL
                                + " "
                                + actualPermanent
                                + "/"
                                + desiredPermanent
                                + " to "
                                + player.getGameProfile().getName()
                );
            }
        }


        return changed;
    }


    // ============================================================
    // UNIQUE ABILITY CLAMP
    // ============================================================

    private static boolean clampUniqueAbilities(
            ServerPlayer player,
            PlayerData playerData,
            Map<ResourceLocation, Integer> desired
    ) {
        boolean changed =
                false;


        for (Map.Entry<ResourceLocation, Integer> entry :
                desired.entrySet()) {

            ResourceLocation abilityRL =
                    entry.getKey();

            int desiredDreamEaterCopies =
                    entry.getValue();


            if (abilityRL == null
                    || desiredDreamEaterCopies <= 0) {
                continue;
            }


            if (isStackableLinkAbility(
                    abilityRL
            )) {
                continue;
            }


            int[] data =
                    playerData
                            .getAbilityMap()
                            .get(
                                    abilityRL
                            );


            if (data == null
                    || data.length == 0) {
                continue;
            }


            int owned =
                    Math.max(
                            0,
                            data[0]
                    );


            if (owned <= 1) {
                continue;
            }


            int equipped =
                    0;


            if (data.length > 1) {

                equipped =
                        Math.min(
                                Math.max(
                                        0,
                                        data[1]
                                ),
                                1
                        );
            }


            playerData
                    .getAbilityMap()
                    .put(
                            abilityRL,
                            new int[]{
                                    1,
                                    equipped
                            }
                    );


            System.out.println(
                    "[KKReMind/DELinks] Collapsed unique ability "
                            + abilityRL
                            + " x"
                            + owned
                            + " -> x1 for "
                            + player.getGameProfile().getName()
            );


            changed =
                    true;
        }


        return changed;
    }


    // ============================================================
    // COUNT PERMANENT LIST OCCURRENCES
    // ============================================================

    private static int countOccurrences(
            List<ResourceLocation> list,
            ResourceLocation abilityRL
    ) {
        if (list == null
                || abilityRL == null) {
            return 0;
        }


        int count =
                0;


        for (ResourceLocation entry :
                list) {

            if (abilityRL.equals(
                    entry
            )) {

                count++;
            }
        }


        return count;
    }


    // ============================================================
    // STACKABLE CHECK
    // ============================================================

    private static boolean isStackableLinkAbility(
            ResourceLocation abilityRL
    ) {
        if (abilityRL == null) {
            return false;
        }


        return STACKABLE_ABILITIES.contains(
                abilityRL.toString()
        );
    }


    // ============================================================
    // PARSE ABILITY ID
    // ============================================================

    private static ResourceLocation parseAbilityId(
            String abilityId
    ) {
        if (abilityId == null
                || abilityId.isBlank()) {
            return null;
        }


        try {

            return ResourceLocation.parse(
                    abilityId.trim()
            );

        } catch (Exception e) {

            System.err.println(
                    "[KKReMind/DELinks] Invalid ability ID: "
                            + abilityId
            );

            return null;
        }
    }


    // ============================================================
    // LEGACY CLEANUP
    // ============================================================

    private static void clearLegacyBookkeeping(
            ServerPlayer player
    ) {
        if (player == null) {
            return;
        }


        CompoundTag data =
                player.getPersistentData();


        /*
         * ONLY old Re:Mind helper state.
         *
         * Do not delete KK abilities here.
         */
        data.remove(
                LEGACY_TEMP_ROOT
        );

        data.remove(
                LEGACY_VIRTUAL_ROOT
        );

        data.remove(
                LEGACY_PURGE_FLAG
        );

        data.remove(
                OLD_PERMANENT_ROOT
        );
    }


    // ============================================================
    // SYNC
    // ============================================================

    private static void sync(
            ServerPlayer player
    ) {
        if (player == null) {
            return;
        }


        PacketHandler.sendTo(
                new SCSyncPlayerData(
                        player
                ),
                player
        );
    }
}