package online.remind.remind.styles.data;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.styles.StyleElement;

import java.util.Set;

/**
 * @param target                   DriveForm ID this Style corresponds to
 * @param finisher                 Reaction Command ID for this Style's finisher
 * @param styleLevel               Determines the Style's level (0 = terminal, 1+ = chainable)
 * @param elements                 Elements that contribute to this Style's affinity
 * @param requiresSpecificWeapons  Whether this Style requires specific weapons
 * @param requiredWeapons          List of weapons this Style is restricted to
 */
public record StyleDefinition(
        ResourceLocation target,
        ResourceLocation finisher,
        int styleLevel,
        Set<StyleElement> elements,
        boolean requiresSpecificWeapons,
        Set<ResourceLocation> requiredWeapons
) {}
