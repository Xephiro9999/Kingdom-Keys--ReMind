package online.remind.remind.dreameater;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public final class DreamEaterVirtualAbilityBridge {

    private static final Map<PlayerData, WeakReference<Player>> PLAYER_DATA_OWNERS =
            Collections.synchronizedMap(new WeakHashMap<>());

    private DreamEaterVirtualAbilityBridge() {
    }

    public static void rememberOwner(PlayerData playerData, Player player) {
        if (playerData == null || player == null) {
            return;
        }

        PLAYER_DATA_OWNERS.put(playerData, new WeakReference<>(player));
    }

    public static Player getOwner(PlayerData playerData) {
        if (playerData == null) {
            return null;
        }

        WeakReference<Player> ref = PLAYER_DATA_OWNERS.get(playerData);

        if (ref == null) {
            return null;
        }

        return ref.get();
    }

    public static boolean hasDreamEaterLinkAbility(Player player, String requestedAbilityId) {
        if (player == null || requestedAbilityId == null || requestedAbilityId.isEmpty()) {
            return false;
        }

        Set<String> activeAbilities = DreamEaterAbilityLinkHelper.getAccessoryAbilityIds(player);

        if (activeAbilities == null || activeAbilities.isEmpty()) {
            return false;
        }

        String requested = requestedAbilityId.trim().toLowerCase(Locale.ROOT);

        if (requested.isEmpty()) {
            return false;
        }

        if (activeAbilities.contains(requested)) {
            return true;
        }

        String normalized = DreamEaterAbilityLinkHelper.normalizeAbilityId(requested);

        if (activeAbilities.contains(normalized)) {
            return true;
        }

        /*
         * Kingdom Keys may ask for:
         *
         * ability_magic_haste
         * kingdomkeys:ability_magic_haste
         * kingdomkeys:magic_haste
         *
         * Our Dream Eater data may store one of those forms.
         * This checks all common variants.
         */
        try {
            ResourceLocation rl = ResourceLocation.parse(requested);

            String namespace = rl.getNamespace();
            String path = rl.getPath();

            if (activeAbilities.contains(namespace + ":" + path)) {
                return true;
            }

            String normalizedPath = DreamEaterAbilityLinkHelper.normalizeAbilityId(path);

            if (activeAbilities.contains(normalizedPath)) {
                return true;
            }

            if (activeAbilities.contains(namespace + ":" + normalizedPath)) {
                return true;
            }

            if (path.startsWith("ability_")) {
                String noPrefix = path.substring("ability_".length());

                if (activeAbilities.contains(noPrefix)) {
                    return true;
                }

                if (activeAbilities.contains(namespace + ":" + noPrefix)) {
                    return true;
                }

                String normalizedNoPrefix = DreamEaterAbilityLinkHelper.normalizeAbilityId(noPrefix);

                if (activeAbilities.contains(normalizedNoPrefix)) {
                    return true;
                }

                if (activeAbilities.contains(namespace + ":" + normalizedNoPrefix)) {
                    return true;
                }
            } else {
                String withAbilityPrefix = "ability_" + path;

                if (activeAbilities.contains(withAbilityPrefix)) {
                    return true;
                }

                if (activeAbilities.contains(namespace + ":" + withAbilityPrefix)) {
                    return true;
                }
            }
        } catch (Exception ignored) {
            /*
             * If it is not a valid ResourceLocation, the plain string checks above
             * were already attempted.
             */
        }

        return false;
    }
}