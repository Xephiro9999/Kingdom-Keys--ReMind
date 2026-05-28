package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;

public class StyleBloodlust extends StyleForm {
    public StyleBloodlust(ResourceLocation registryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowthAbilities) {
        super(registryName, order, skinRL, hasKeychain, baseGrowthAbilities);

        this.color = new float[]{0.75f, 0.0F, 0.0F};
    }
}
