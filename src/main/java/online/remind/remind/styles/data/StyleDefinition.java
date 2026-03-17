package online.remind.remind.styles.data;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.styles.StyleElement;

import java.util.Set;

public record StyleDefinition(
        int level,
        Set<StyleElement> triggers,
        boolean requiresWeapons,
        Set<ResourceLocation> requiredWeapons
) {}
