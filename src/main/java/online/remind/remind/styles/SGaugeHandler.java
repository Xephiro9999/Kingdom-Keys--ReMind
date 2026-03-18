package online.remind.remind.styles;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;

import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.styles.data.*;

import java.util.*;

public class SGaugeHandler {

    private static final Map<UUID, Map<ResourceLocation, Double>> WEIGHTS = new HashMap<>();

    public static void addContribution(Player player,
                                       Set<StyleElement> elements,
                                       Set<ResourceLocation> specificStyles,
                                       int level) {

        IGlobalDataRM globalData = ModDataRM.getGlobal(player);

        // --- 1. Compute SGauge value ---
        int totalValue = 0;
        Set<ContributionDefinition> usedDefs = new HashSet<>();

        for (StyleElement element : elements) {
            ContributionDefinition def = ContributionRegistry.getForElement(element);
            if (def != null && usedDefs.add(def)) {
                totalValue += def.computeValue(level);
            }
        }

        for (ResourceLocation styleId : specificStyles) {
            ContributionDefinition def = ContributionRegistry.getForStyle(styleId);
            if (def != null && usedDefs.add(def)) {
                totalValue += def.computeValue(level);
            }
        }

        System.out.println("SGauge + " + totalValue + " from action (elements=" + elements + ", specific=" + specificStyles + ")");

        // --- 2. Add SGauge ---
        double current = globalData.getSituationValue();
        double updated = current + totalValue;
        globalData.setSituationValue(updated);

        PacketHandlerRM.syncGlobalToAllAround(player, globalData);

        // --- 3. Add weight ---
        Map<ResourceLocation, Double> weightMap =
                WEIGHTS.computeIfAbsent(player.getUUID(), k -> new HashMap<>());

        for (StyleElement element : elements) {
            for (ResourceLocation styleId : StyleRegistry.getStylesForElement(element)) {
                weightMap.merge(styleId, (double) totalValue, Double::sum);
                System.out.println("Weight " + styleId + " += " + totalValue + " (via element " + element + ")");
            }
        }

        for (ResourceLocation styleId : specificStyles) {
            weightMap.merge(styleId, (double) totalValue, Double::sum);
            System.out.println("Weight " + styleId + " += " + totalValue + " (via specific_style)");
        }

        // --- 4. Check for activation ---
        if (updated >= 100) {
            triggerStyleSelection(player, globalData, weightMap);
        }
    }

    private static void triggerStyleSelection(Player player,
                                              IGlobalDataRM globalData,
                                              Map<ResourceLocation, Double> weightMap) {

        System.out.println("SGauge reached 100 for " + player.getName().getString());

        // Determine current Style tier
        String currentStyleId = globalData.getStyle();
        int currentTier = 0;

        if (!currentStyleId.isEmpty()) {
            StyleDefinition def = StyleLoader.get(ResourceLocation.parse(currentStyleId));
            if (def != null) currentTier = def.level();
        }

        // Find eligible Styles
        double highest = 0;
        List<ResourceLocation> eligible = new ArrayList<>();

        for (Map.Entry<ResourceLocation, Double> entry : weightMap.entrySet()) {
            ResourceLocation styleId = entry.getKey();
            double weight = entry.getValue();

            StyleDefinition def = StyleLoader.get(styleId);
            if (def == null) continue;

            if (def.level() != currentTier + 1) continue;

            if (weight > highest) {
                highest = weight;
                eligible.clear();
                eligible.add(styleId);
            } else if (weight == highest) {
                eligible.add(styleId);
            }
        }

        System.out.println("Eligible Styles: " + eligible);

        // TODO: Hook into RC system

        // Reset SGauge
        globalData.setSituationValue(0);
        PacketHandlerRM.syncGlobalToAllAround(player, globalData);

        weightMap.clear();
    }
}
