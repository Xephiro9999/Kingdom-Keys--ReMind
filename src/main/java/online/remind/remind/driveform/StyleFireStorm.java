package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.styles.StyleElement;

import java.util.Set;

public class StyleFireStorm extends StyleForm {
    public StyleFireStorm(
            ResourceLocation registryName,
            int order,
            ResourceLocation skinRL,
            boolean hasKeychain,
            boolean baseGrowthAbilities
    ) {
        super(
                registryName,
                order,
                skinRL,
                hasKeychain,
                baseGrowthAbilities,
                1,                   // level
                Set.of(StyleElement.FIRE),
                false,               // requiresWeapons
                Set.of()
        );
        this.color = new float[]{1f, 0.0F, 0.0F};
    }
}
