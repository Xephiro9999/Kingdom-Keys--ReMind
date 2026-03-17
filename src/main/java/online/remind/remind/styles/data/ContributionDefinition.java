package online.remind.remind.styles.data;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.styles.StyleElement;

import java.util.Map;
import java.util.Set;

public class ContributionDefinition {

    public final ResourceLocation id;

    // Elements this contribution applies to (FIRE, STRIKE, PHYSICAL, etc.)
    public final Set<StyleElement> elements;

    // Specific Styles this contribution applies to (form_firestorm, form_bladecharge, etc.)
    public final Set<ResourceLocation> specificStyles;

    // Base SGauge value (Level 1)
    public final int baseValue;

    // SGauge added per level above 1
    public final int perLevelBonus;

    // Optional overrides for specific levels
    public final Map<Integer, Integer> levelOverrides;

    public ContributionDefinition(
            ResourceLocation id,
            Set<StyleElement> elements,
            Set<ResourceLocation> specificStyles,
            int baseValue,
            int perLevelBonus,
            Map<Integer, Integer> levelOverrides
    ) {
        this.id = id;
        this.elements = elements;
        this.specificStyles = specificStyles;
        this.baseValue = baseValue;
        this.perLevelBonus = perLevelBonus;
        this.levelOverrides = levelOverrides;
    }

    /**
     * Computes the SGauge value for a given spell/RC level.
     * Level is 1-based (Fire=1, Fira=2, Firaga=3, Firaza=4).
     */
    public int computeValue(int level) {
        if (levelOverrides.containsKey(level)) {
            return levelOverrides.get(level);
        }
        return baseValue + ((level - 1) * perLevelBonus);
    }
}
