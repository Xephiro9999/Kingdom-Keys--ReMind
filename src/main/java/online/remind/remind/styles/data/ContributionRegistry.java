package online.remind.remind.styles.data;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.styles.StyleElement;

import java.util.HashMap;
import java.util.Map;

public class ContributionRegistry {

    private static final Map<StyleElement, ContributionDefinition> ELEMENT_CONTRIBUTIONS = new HashMap<>();
    private static final Map<ResourceLocation, ContributionDefinition> STYLE_CONTRIBUTIONS = new HashMap<>();
    private static final Map<ResourceLocation, ContributionDefinition> SPELL_CONTRIBUTIONS = new HashMap<>();

    public static void applyDefinitions() {

        ELEMENT_CONTRIBUTIONS.clear();
        STYLE_CONTRIBUTIONS.clear();
        SPELL_CONTRIBUTIONS.clear();

        for (ContributionDefinition def : ContributionLoader.all().values()) {

            // Register spell-based contribution using the REAL target ID
            SPELL_CONTRIBUTIONS.put(def.target(), def);

            // Register element-based contributions
            for (StyleElement element : def.elements()) {
                ELEMENT_CONTRIBUTIONS.put(element, def);
                System.out.println("Registered element contribution: " + element + " -> " + def.target());
            }

            // Register specific-style contributions
            for (ResourceLocation styleId : def.specificStyles()) {
                STYLE_CONTRIBUTIONS.put(styleId, def);
                System.out.println("Registered specific-style contribution: " + styleId + " -> " + def.target());
            }
        }
    }

    public static ContributionDefinition getForElement(StyleElement element) {
        return ELEMENT_CONTRIBUTIONS.get(element);
    }

    public static ContributionDefinition getForStyle(ResourceLocation styleId) {
        return STYLE_CONTRIBUTIONS.get(styleId);
    }

    public static ContributionDefinition getForSpell(ResourceLocation id) {
        return SPELL_CONTRIBUTIONS.get(id);
    }
}
