package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.styles.StyleElement;


public class StyleFireStorm extends StyleForm {

    public StyleFireStorm(ResourceLocation registryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowthAbilities) {
        super(registryName, order, skinRL, hasKeychain, baseGrowthAbilities);

        // Only visual attributes belong here
        this.color = new float[]{1f, 0f, 0f}; // Firestorm red
    }
}
