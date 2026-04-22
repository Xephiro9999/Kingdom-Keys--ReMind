package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;

public class StyleFeverPitch extends StyleForm {
    public StyleFeverPitch(ResourceLocation registryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowthAbilities) {
        super(registryName, order, skinRL, hasKeychain, baseGrowthAbilities);

        this.color = new float[]{0.0f, 1.0F, 0.5F};
    }
}
