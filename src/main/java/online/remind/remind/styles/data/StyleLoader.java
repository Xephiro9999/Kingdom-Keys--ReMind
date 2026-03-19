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

        // ------------------------------------------------------------
        // REQUIRED FIELDS
        // ------------------------------------------------------------

        // target: DriveForm ID this Style corresponds to
        ResourceLocation target = json.has("target")
                ? ResourceLocation.parse(json.get("target").getAsString())
                : id; // fallback: assume JSON filename matches DriveForm ID

        // finisher: Reaction Command ID for this Style's finisher
        ResourceLocation finisher = json.has("finisher")
                ? ResourceLocation.parse(json.get("finisher").getAsString())
                : null;

        // style_level
        int styleLevel = 1;

        if (json.has("style_level")) {
            try {
                styleLevel = json.get("style_level").getAsInt();
            } catch (Exception e) {
                System.out.println("Warning: Style " + id + " has invalid style_level. Defaulting to 1.");
            }
        }


        // Terminal Style rule: allow 0, clamp negatives
        if (styleLevel < 0) {
            System.out.println("Warning: Style " + id + " has style_level < 0. Clamping to 0.");
            styleLevel = 0;
        }

        // elements
        Set<StyleElement> elements = parseElements(
                json.has("elements") ? json.getAsJsonArray("elements") : null
        );

        // ------------------------------------------------------------
        // OPTIONAL FIELDS
        // ------------------------------------------------------------

        boolean requiresSpecificWeapons =
                json.has("requires_specific_weapons") &&
                json.get("requires_specific_weapons").getAsBoolean();

        Set<ResourceLocation> requiredWeapons = parseWeapons(
                (requiresSpecificWeapons && json.has("required_weapons"))
                        ? json.getAsJsonArray("required_weapons")
                        : null
        );

        // ------------------------------------------------------------
        // STORE DEFINITION
        // ------------------------------------------------------------

        StyleDefinition def = new StyleDefinition(
                target,
                finisher,
                styleLevel,
                elements,
                requiresSpecificWeapons,
                requiredWeapons
        );

        DEFINITIONS.put(id, def);

        System.out.println("Loaded StyleDefinition for: " + id);
    }

    public static StyleDefinition get(ResourceLocation id) {
        return DEFINITIONS.get(id);
    }

    // ------------------------------------------------------------
    // HELPERS
    // ------------------------------------------------------------

    private static Set<StyleElement> parseElements(JsonArray arr) {
        Set<StyleElement> set = new HashSet<>();
        if (arr == null) return set;

        for (JsonElement el : arr) {
            String name = el.getAsString().toUpperCase();
            try {
                set.add(StyleElement.valueOf(name));
            } catch (IllegalArgumentException ignored) {}
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
