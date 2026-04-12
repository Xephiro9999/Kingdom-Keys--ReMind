package online.remind.remind.styles.data;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;

import online.remind.remind.styles.StyleElement;

public class ContributionLoader {

    private static final Map<ResourceLocation, ContributionDefinition> DEFINITIONS = new HashMap<>();

    public static void clear() {
        DEFINITIONS.clear();
    }

    public static void load(JsonObject json, ResourceLocation fileId) {

        // 1. Read target from JSON
        if (!json.has("target")) {
            throw new IllegalArgumentException("Contribution JSON " + fileId +
                    " is missing required field: 'target'");
        }

        ResourceLocation target = ResourceLocation.parse(json.get("target").getAsString());

        // 2. Parse elements
        Set<StyleElement> elements = parseElements(
                json.has("elements") ? json.getAsJsonArray("elements") : null
        );

        // 3. Parse specific styles
        Set<ResourceLocation> specificStyles = parseStyles(
                json.has("specific_styles") ? json.getAsJsonArray("specific_styles") : null
        );

        // 4. Required: base_value
        int baseValue = json.get("base_value").getAsInt();

        // 5. Optional: per_level_bonus
        int perLevelBonus = json.has("per_level_bonus") ? json.get("per_level_bonus").getAsInt() : 0;

        // 6. Optional: level_overrides
        Map<Integer, Integer> overrides = new HashMap<>();
        if (json.has("level_overrides")) {
            JsonObject obj = json.getAsJsonObject("level_overrides");
            for (String key : obj.keySet()) {
                overrides.put(Integer.parseInt(key), obj.get(key).getAsInt());
            }
        }

        // 7. Build the new ContributionDefinition
        ContributionDefinition def = new ContributionDefinition(
                target,
                elements,
                specificStyles,
                baseValue,
                perLevelBonus,
                overrides
        );

        // Store using the actual target ID
        DEFINITIONS.put(target, def);

        System.out.println("Loaded ContributionDefinition for target: " + target);
    }

    public static Map<ResourceLocation, ContributionDefinition> all() {
        return DEFINITIONS;
    }

    private static Set<StyleElement> parseElements(JsonArray arr) {
        Set<StyleElement> set = new HashSet<>();
        if (arr == null) return set;

        for (JsonElement el : arr) {
            String name = el.getAsString().toUpperCase();
            try {
                set.add(StyleElement.valueOf(name));
            } catch (IllegalArgumentException e) {
                // Optional: log invalid element names
            }
        }
        return set;
    }

    private static Set<ResourceLocation> parseStyles(JsonArray arr) {
        Set<ResourceLocation> set = new HashSet<>();
        if (arr == null) return set;

        for (JsonElement el : arr) {
            set.add(ResourceLocation.parse(el.getAsString()));
        }
        return set;
    }
}
