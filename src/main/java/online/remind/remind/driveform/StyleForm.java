package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.remind.remind.styles.StyleElement;

import java.util.Set;

public abstract class StyleForm extends DriveForm {

    // --- JSON-loaded metadata ---
    protected int styleLevel;                          // 0 = terminal, 1+ = chainable
    protected Set<StyleElement> elements;              // Style affinity elements
    protected boolean requiresWeapons;                 // Whether weapon restrictions apply
    protected Set<ResourceLocation> requiredWeapons;   // Allowed weapons if restrictions apply
    protected ResourceLocation finisher;               // Finisher RC ID

    public StyleForm(
            ResourceLocation id,
            int order,
            ResourceLocation skinRL,
            boolean hasKeychain,
            boolean baseGrowthAbilities
    ) {
        super(id, order, hasKeychain, baseGrowthAbilities);
        this.skinRL = skinRL;
        ModDriveFormsRM.styles.add(id);
    }

    // ------------------------------------------------------------
    // JSON SETTERS
    // ------------------------------------------------------------

    /** Sets the Style level (0 = terminal, 1+ = chainable). */
    public void setStyleLevel(int level) {
        // Clamp: level <= 0 → terminal Style
        this.styleLevel = Math.max(level, 0);
    }

    /** Sets the Style's affinity elements. */
    public void setElements(Set<StyleElement> elements) {
        this.elements = elements;
    }

    /** Sets weapon restriction rules. */
    public void setWeaponRestrictions(boolean requiresSpecificWeapons, Set<ResourceLocation> requiredWeapons) {
        this.requiresWeapons = requiresSpecificWeapons;
        this.requiredWeapons = requiredWeapons;
    }

    /** Sets the Finisher RC ID for this Style. */
    public void setFinisher(ResourceLocation finisher) {
        this.finisher = finisher;
    }

    // ------------------------------------------------------------
    // GETTERS
    // ------------------------------------------------------------

    public int getStyleLevel() {
        return styleLevel;
    }

    public Set<StyleElement> getElements() {
        return elements;
    }

    public boolean requiresSpecificWeapons() {
        return requiresWeapons;
    }

    public Set<ResourceLocation> getRequiredWeapons() {
        return requiredWeapons;
    }

    public ResourceLocation getFinisher() {
        return finisher;
    }

    // ------------------------------------------------------------
    // OVERRIDES
    // ------------------------------------------------------------

    @Override
    public boolean displayInCommandMenu(Player player) {
        // Styles never appear in the Drive menu
        return false;
    }

    /*@Override
    public void endDrive(Player player) {
        super.endDrive(player);
        PlayerData playerData = PlayerData.get(player);
    }*/
}