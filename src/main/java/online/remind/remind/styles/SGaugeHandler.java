package online.remind.remind.styles;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;

import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.styles.data.*;
import online.remind.remind.lib.StringsRM;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.util.*;
import java.util.stream.Collectors;

public class SGaugeHandler {

    private static final Map<UUID, Map<ResourceLocation, Double>> WEIGHTS = new HashMap<>();

    public static void addContribution(Player player,
                                       ResourceLocation actionId,
                                       Set<StyleElement> elements,
                                       Set<ResourceLocation> specificStyles,
                                       int level) {

        GlobalDataRM globalData = ModDataRM.getGlobal(player);
        PlayerData playerData = PlayerData.get(player);

        // ------------------------------------------------------------
        // 1. Determine which ContributionDefinition to use
        // ------------------------------------------------------------

        // Priority 0: SPELL contribution (PRIMARY)
        ContributionDefinition def = ContributionRegistry.getForSpell(actionId);;

        // Priority 1: specific_styles (override spell if needed)
        if (def == null) {
            for (ResourceLocation styleId : specificStyles) {
                def = ContributionRegistry.getForStyle(styleId);
                if (def != null) break;
            }
        }

        // Priority 2: element fallback (should almost never be used)
        if (def == null) {
            for (StyleElement element : elements) {
                def = ContributionRegistry.getForElement(element);
                if (def != null) break;
            }
        }


        // If no definition found, SGauge contribution is 0
        int totalValue = (def != null) ? def.computeValue(level) : 0;

        // ------------------------------------------------------------
        // Cure Converter special case
        // ------------------------------------------------------------
        if (actionId.equals(ResourceLocation.parse(Strings.Magic_Cure))) {

            int stacks = playerData.getNumberOfAbilitiesEquipped(StringsRM.cure_converter);

            if (stacks > 0) {

                // Clamp to max 3 stacks
                int effectiveStacks = Math.min(stacks, 3);

                // Determine base value based on stack count
                int base;
                switch (effectiveStacks) {
                    case 1 -> base = 35;
                    case 2 -> base = 50;
                    default -> base = 65; // 3 or more
                }

                // MP percentage (0.0 to 1.0)
                double mpPercent = playerData.getMP() / playerData.getMaxMP();

                // KH3 formula: (base * MP%) + 15
                totalValue = (int)((base * mpPercent) + 15);
            }
        }

        // ------------------------------------------------------------
        // Situation Boost (10% per instance, applied AFTER Cure Converter)
        // ------------------------------------------------------------
        int boostStacks = playerData.getNumberOfAbilitiesEquipped(StringsRM.situationBoost);
        if (boostStacks > 0) {
            double multiplier = 1.0 + (0.10 * boostStacks);
            totalValue = (int)(totalValue * multiplier);
        }

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
                                              GlobalDataRM globalData,
                                              Map<ResourceLocation, Double> weightMap) {

        // Determine current Style tier
        StyleDefinition current = StyleRegistry.getCurrentStyleDefinition(player);
        int currentTier = (current != null) ? current.styleLevel() : 0;

        // Find eligible Styles for next tier
        double highest = 0;
        List<ResourceLocation> eligible = new ArrayList<>();

        for (Map.Entry<ResourceLocation, Double> entry : weightMap.entrySet()) {
            ResourceLocation styleId = entry.getKey();
            double weight = entry.getValue();

            StyleDefinition def = StyleRegistry.getStyleForDriveForm(styleId);
            if (def == null) continue;

            PlayerData playerData = PlayerData.get(player);
            boolean notInStyle = playerData.getActiveDriveForm().equals(DriveForm.NONE.toString());
            int nextTier = currentTier + 1;

            if (notInStyle) {
                // Activation: allow Lv0 and Lv1
                if (def.styleLevel() != 0 && def.styleLevel() != 1) continue;
            } else {
                // Lv0 Styles are terminal: no chain-ups
                if (currentTier == 0) continue;

                // Chain-up: must match next tier
                if (def.styleLevel() != nextTier) continue;
            }

            // WEAPON RESTRICTION CHECK
            if (def.requiresSpecificWeapons()) {
                ResourceLocation heldWeapon = player.getMainHandItem()
                        .getItem()
                        .builtInRegistryHolder()
                        .key()
                        .location();

                if (!def.requiredWeapons().contains(heldWeapon)) {
                    continue;
                }
            }

            // Weight comparison logic
            if (weight > highest) {
                highest = weight;
                eligible.clear();
                eligible.add(styleId);
            } else if (weight == highest) {
                eligible.add(styleId);
            }
        }

        System.out.println("Eligible Styles: " + eligible);

        PlayerData playerData = PlayerData.get(player);
        boolean notInStyle = playerData.getActiveDriveForm().equals(DriveForm.NONE.toString());

        if (!eligible.isEmpty()) {
            // 1) Store ALL eligible styles in the style flag
            String combined = eligible.stream()
                    .map(ResourceLocation::toString)
                    .collect(Collectors.joining(","));
            globalData.setStyle(combined);
            PacketHandlerRM.syncGlobalToAllAround(player, globalData);

            // Add Finisher RC for current Style if in one
            if (current != null && current.finisher() != null) {
                String finisherRcId = current.finisher().toString();
                //System.out.println("Adding Finisher RC: " + finisherRcId + " for current Style: " + current.target());
                playerData.addReactionCommand(finisherRcId, player);
            }

            // 2) Add chain-up RCs (or FinishRC if not in a Style)
            for (ResourceLocation styleId : eligible) {
                StyleDefinition def = StyleRegistry.getStyleForDriveForm(styleId);
                if (def != null && def.finisher() != null) {
                    String finisherRcId = def.finisher().toString();
                    //System.out.println("Adding Activation RC: " + finisherRcId + " for Style: " + styleId);
                    playerData.addReactionCommand(finisherRcId, player);
                }
            }

            // Add FinishRC if not in a Style
            if (notInStyle) {
                //System.out.println("DEBUG: Not in a Style - also adding FinishRC");
                ResourceLocation finishRcId = ResourceLocation.parse("kkremind:rc_finish");
                playerData.addReactionCommand(finishRcId.toString(), player);
            }
        } else if (notInStyle) {
            // Player not in a Style and no eligible chain-ups
            // Add the FinishRC (generic finisher for non-Style attacks)
            //System.out.println("DEBUG: Not in a Style and no eligible styles - adding FinishRC");
            ResourceLocation finishRcId = ResourceLocation.parse("kkremind:rc_finish");
            playerData.addReactionCommand(finishRcId.toString(), player);
        } else {
            // No chain-ups available, but player is in a Style
            // Add the Finisher RC for the current Style
            if (current != null && current.finisher() != null) {
                String finisherRcId = current.finisher().toString();
                //System.out.println("Adding Finisher RC (no chain-up): " + finisherRcId + " for current Style: " + current.target());
                playerData.addReactionCommand(finisherRcId, player);
            }
        }

        // Reset SGauge and weights
        weightMap.clear();
    }
}
