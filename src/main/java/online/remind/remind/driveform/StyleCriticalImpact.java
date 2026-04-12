package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.styles.StyleElement;

import java.util.Set;

public class StyleCriticalImpact extends StyleForm {

    public StyleCriticalImpact(ResourceLocation registryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowthAbilities) {
        super(registryName, order, skinRL, hasKeychain, baseGrowthAbilities);

        // Only visual attributes belong here
        this.color = new float[]{1f, 1f, 0f};
    }
}
