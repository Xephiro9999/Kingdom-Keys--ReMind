package online.remind.remind.styles.data;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;

import online.remind.remind.styles.StyleElement;

public class StyleLoader {

    private static final Map<ResourceLocation, StyleDefinition> DEFINITIONS = new HashMap<>();

    public static void clear() {
        DEFINITIONS.clear();
    }

    public static void load(JsonObject json, ResourceLocation id) {
        // Required field: style_level
        int level = 1; // default if missing

        if (json.has("style_level")) {
            try {
                level = json.get("style_level").getAsInt();
            } catch (Exception e) {
                System.out.println("Warning: Style " + id + " has invalid style_level format. Defaulting to 1.");
                level = 1;
            }
        }

        // Clamp to minimum of 1
        if (level < 1) {
            System.out.println("Warning: Style " + id + " has style_level < 1. Clamping to 1.");
            level = 1;
        }

        Set<StyleElement> elements = parseElements(
                json.has("elements") ? json.getAsJsonArray("elements") : null
        );

        // Optional fields with defaults
        boolean requiresWeapons = json.has("requires_specific_weapons") ?
                json.get("requires_specific_weapons").getAsBoolean() : false;

        Set<ResourceLocation> requiredWeapons = parseWeapons(
                (requiresWeapons && json.has("required_weapons")) ?
                        json.getAsJsonArray("required_weapons") : null
        );

        DEFINITIONS.put(id, new StyleDefinition(level, elements, requiresWeapons, requiredWeapons));

        System.out.println("Loading StyleDefinition for: " + id);
    }

    public static StyleDefinition get(ResourceLocation id) {
        return DEFINITIONS.get(id);
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

    private static Set<ResourceLocation> parseWeapons(JsonArray arr) {
        Set<ResourceLocation> set = new HashSet<>();
        if (arr == null) return set;

        for (JsonElement el : arr) {
            set.add(ResourceLocation.parse(el.getAsString()));
        }
        return set;
    }
}