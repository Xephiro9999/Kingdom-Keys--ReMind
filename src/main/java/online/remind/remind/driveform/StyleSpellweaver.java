package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;

public class StyleSpellweaver extends StyleForm {
    public StyleSpellweaver(ResourceLocation registryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowthAbilities) {
        super(registryName, order, skinRL, hasKeychain, baseGrowthAbilities);

        this.color = new float[]{0.5f, 1.0F, 0.0F};
    }
}
