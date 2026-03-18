package online.remind.remind.styles.data;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.styles.StyleElement;

import java.util.Set;

/**
 * @param level             Determines the Level of the Style
 * @param triggers          Elements that contribute to this Style's weight
 * @param requiresWeapons   Boolean, determines if Style should be locked to specific weapons
 * @param requiredWeapons   List of weapons the Style is restricted to appear for
 */
public record StyleDefinition(
        int level,
        Set<StyleElement> triggers,
        boolean requiresWeapons,
        Set<ResourceLocation> requiredWeapons
) {}
