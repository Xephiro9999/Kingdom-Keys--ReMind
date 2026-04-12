package online.remind.remind.styles;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;

import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.styles.data.*;

import java.util.*;
import java.util.stream.Collectors;

public class SGaugeHandler {

    private static final Map<UUID, Map<ResourceLocation, Double>> WEIGHTS = new HashMap<>();

    public static void addContribution(Player player,
                                       Set<StyleElement> elements,
                                       Set<ResourceLocation> specificStyles,
                                       int level) {

        IGlobalDataRM globalData = ModDataRM.getGlobal(player);

        // ------------------------------------------------------------
        // 1. Determine which ContributionDefinition to use
        // ------------------------------------------------------------

        ContributionDefinition def = null;

        // Priority 1: specific_styles
        for (ResourceLocation styleId : specificStyles) {
            def = ContributionRegistry.getForStyle(styleId);
            if (def != null) break;
        }

        // Priority 2: first element with a definition
        if (def == null) {
            for (StyleElement element : elements) {
                def = ContributionRegistry.getForElement(element);
                if (def != null) break;
            }
        }

        // If no definition found, SGauge contribution is 0
        int totalValue = (def != null) ? def.computeValue(level) : 0;

        System.out.println("SGauge + " + totalValue +
                " from action (elements=" + elements + ", specific=" + specificStyles + ")");

        // ------------------------------------------------------------
        // 2. Apply SGauge value ONCE
        // ------------------------------------------------------------

        double current = globalData.getSituationValue();
        double updated = current + totalValue;
        globalData.setSituationValue(updated);

        PacketHandlerRM.syncGlobalToAllAround(player, globalData);

        // ------------------------------------------------------------
        // 3. Add WEIGHT per element (NOT SGauge)
        // ------------------------------------------------------------

        Map<ResourceLocation, Double> weightMap =
                WEIGHTS.computeIfAbsent(player.getUUID(), k -> new HashMap<>());

        for (StyleElement element : elements) {
            for (ResourceLocation styleId : StyleRegistry.getStylesForElement(element)) {
                weightMap.merge(styleId, (double) totalValue, Double::sum);
                System.out.println("Weight " + styleId + " += " + totalValue +
                        " (via element " + element + ")");
            }
        }

        for (ResourceLocation styleId : specificStyles) {
            weightMap.merge(styleId, (double) totalValue, Double::sum);
            System.out.println("Weight " + styleId + " += " + totalValue +
                    " (via specific_style)");
        }

        // ------------------------------------------------------------
        // 4. Trigger Style selection if SGauge >= 100
        // ------------------------------------------------------------

        if (updated >= 100) {
            triggerStyleSelection(player, globalData, weightMap);
        }
    }

    private static void triggerStyleSelection(Player player,
                                              IGlobalDataRM globalData,
                                              Map<ResourceLocation, Double> weightMap) {

        System.out.println("SGauge reached 100 for " + player.getName().getString());

        // ------------------------------------------------------------
        // Determine current Style tier
        // ------------------------------------------------------------

        StyleDefinition current = StyleRegistry.getCurrentStyleDefinition(player);
        int currentTier = (current != null) ? current.styleLevel() : 0;

        // ------------------------------------------------------------
        // Find eligible Styles for next tier
        // ------------------------------------------------------------

        double highest = 0;
        List<ResourceLocation> eligible = new ArrayList<>();

        for (Map.Entry<ResourceLocation, Double> entry : weightMap.entrySet()) {
            ResourceLocation styleId = entry.getKey();
            double weight = entry.getValue();

            StyleDefinition def = StyleRegistry.getStyleForDriveForm(styleId);
            if (def == null) continue;

            // Must match next tier
            if (def.styleLevel() != currentTier + 1) continue;

            // ------------------------------------------------------------
            // WEAPON RESTRICTION CHECK (NEW)
            // ------------------------------------------------------------
            if (def.requiresSpecificWeapons()) {

                // Get the held item ID safely
                ResourceLocation heldWeapon = player.getMainHandItem()
                        .getItem()
                        .builtInRegistryHolder()
                        .key()
                        .location();

                // If the held weapon is NOT in the allowed list, skip this Style
                if (!def.requiredWeapons().contains(heldWeapon)) {
                    continue;
                }
            }

            // ------------------------------------------------------------
            // Weight comparison logic (unchanged)
            // ------------------------------------------------------------
            if (weight > highest) {
                highest = weight;
                eligible.clear();
                eligible.add(styleId);
            } else if (weight == highest) {
                eligible.add(styleId);
            }
        }


        System.out.println("Eligible Styles: " + eligible);

    // ------------------------------------------------------------
    // Commit selected Style to globalData
    // ------------------------------------------------------------
        if (!eligible.isEmpty()) {

            // 1) Store ALL eligible styles in the style flag
            String combined = eligible.stream()
                    .map(ResourceLocation::toString)
                    .collect(Collectors.joining(","));
            globalData.setStyle(combined);
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);

            // 2) Add ALL eligible RCs
            PlayerData playerData = PlayerData.get(player);
            for (ResourceLocation styleId : eligible) {
                StyleDefinition def = StyleRegistry.getStyleForDriveForm(styleId);
                if (def != null && def.finisher() != null) {
                    playerData.addReactionCommand(def.finisher().toString(), player);
                }
            }

            // 3) DO NOT reset SGauge here — RCs handle that
        }


        // ------------------------------------------------------------
        // Reset SGauge and weights
        // ------------------------------------------------------------
        //PacketHandlerRM.syncGlobalToAllAround(player, globalData);

        weightMap.clear();

    }
}
