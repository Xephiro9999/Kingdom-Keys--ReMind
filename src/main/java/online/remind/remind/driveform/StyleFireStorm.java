package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.styles.StyleElement;

<<<<<<< HEAD
import java.util.Set;
=======
public class StyleFireStorm extends DriveForm {
    public StyleFireStorm(ResourceLocation registeryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowthAbilities) {
        super(registeryName, order, hasKeychain, baseGrowthAbilities);
        this.color = new float[]{1f, 0.0F, 0.0F};
        this.skinRL = skinRL;
        ModDriveFormsRM.styles.add(registeryName);
    }
>>>>>>> upstream/1.21.1

public class StyleFireStorm extends StyleForm {

    public StyleFireStorm(ResourceLocation id, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowthAbilities) {
        super(id, order, skinRL, hasKeychain, baseGrowthAbilities);

        // Only visual attributes belong here
        this.color = new float[]{1f, 0f, 0f}; // Firestorm red
    }
}
