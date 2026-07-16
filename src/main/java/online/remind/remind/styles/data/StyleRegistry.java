package online.remind.remind.styles.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;

import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.remind.remind.driveform.StyleForm;
import online.remind.remind.driveform.ModDriveFormsRM;

import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.styles.StyleElement;

import java.util.*;

public class StyleRegistry {

    // ------------------------------------------------------------
    // MASTER REGISTRIES
    // ------------------------------------------------------------

    /** Maps DriveForm ID → StyleDefinition */
    private static final Map<ResourceLocation, StyleDefinition> STYLE_BY_DRIVEFORM = new HashMap<>();

    /** Maps Finisher RC ID → StyleDefinition */
    private static final Map<ResourceLocation, StyleDefinition> STYLE_BY_FINISHER = new HashMap<>();

    /** Reverse lookup: Element → Styles that use that element */
    private static final Map<StyleElement, Set<ResourceLocation>> ELEMENT_TO_STYLES = new HashMap<>();


    // ------------------------------------------------------------
    // MAIN LOADER
    // ------------------------------------------------------------

    public static void applyDefinitions() {

        STYLE_BY_DRIVEFORM.clear();
        STYLE_BY_FINISHER.clear();
        ELEMENT_TO_STYLES.clear();

        for (DeferredHolder<DriveForm, ? extends DriveForm> entry : ModDriveFormsRM.DRIVE_FORMS.getEntries()) {

            DriveForm form = entry.value();

            if (form instanceof StyleForm style) {

                ResourceLocation driveFormId = entry.getId();
                StyleDefinition def = StyleLoader.get(driveFormId);

                if (def == null) {
                    KingdomKeysReMind.LOGGER.warn(
                            "No Style JSON found for Style '{}'", driveFormId
                    );
                    continue;
                }

                // ------------------------------------------------------------
                // Inject JSON metadata into the StyleForm instance
                // ------------------------------------------------------------
                style.setStyleLevel(def.styleLevel());
                style.setElements(def.elements());
                style.setWeaponRestrictions(
                        def.requiresSpecificWeapons(),
                        def.requiredWeapons()
                );
                style.setFinisher(def.finisher());

                // ------------------------------------------------------------
                // Register in lookup tables
                // ------------------------------------------------------------
                STYLE_BY_DRIVEFORM.put(def.target(), def);
                STYLE_BY_FINISHER.put(def.finisher(), def);

                // Reverse lookup for SGauge contributions
                for (StyleElement element : def.elements()) {
                    ELEMENT_TO_STYLES
                            .computeIfAbsent(element, k -> new HashSet<>())
                            .add(def.target());
                }

                KingdomKeysReMind.LOGGER.info(
                        "Loaded Style '{}' (level {}, {} elements, {} required weapons)",
                        def.target(),
                        def.styleLevel(),
                        def.elements().size(),
                        def.requiredWeapons().size()
                );
            }
        }
    }


    // ------------------------------------------------------------
    // LOOKUP METHODS
    // ------------------------------------------------------------

    /** Returns the StyleDefinition for a given DriveForm ID. */
    public static StyleDefinition getStyleForDriveForm(ResourceLocation id) {
        return STYLE_BY_DRIVEFORM.get(id);
    }

    /** Returns the StyleDefinition for a given Finisher RC ID. */
    public static StyleDefinition getStyleForFinisher(ResourceLocation rcId) {
        return STYLE_BY_FINISHER.get(rcId);
    }

    /** Returns all Styles that use a given element. */
    public static Set<ResourceLocation> getStylesForElement(StyleElement element) {
        return ELEMENT_TO_STYLES.getOrDefault(element, Collections.emptySet());
    }


    // ------------------------------------------------------------
    // PLAYER STATE HELPERS
    // ------------------------------------------------------------

    public static boolean isInStyle(Player player) {
        var playerData = PlayerData.get(player);
        ResourceLocation active = playerData.getActiveDriveForm();

        if (active.equals(DriveForm.NONE))
            return false;

        DriveForm form = ModDriveForms.registry.get(active);
        return form instanceof StyleForm;
    }

    public static StyleForm getCurrentStyleForm(Player player) {
        var playerData = PlayerData.get(player);
        ResourceLocation active = playerData.getActiveDriveForm();

        DriveForm form = ModDriveForms.registry.get(active);
        return (form instanceof StyleForm style) ? style : null;
    }


    /** Returns the StyleDefinition for the player's current Style, or null. */
    public static StyleDefinition getCurrentStyleDefinition(net.minecraft.world.entity.player.Player player) {
        StyleForm style = getCurrentStyleForm(player);
        return (style != null) ? STYLE_BY_DRIVEFORM.get(style.getRegistryName()) : null;
    }

    /** Returns the current Style level, or 0 if not in a Style. */
    public static int getCurrentStyleLevel(net.minecraft.world.entity.player.Player player) {
        StyleForm style = getCurrentStyleForm(player);
        return (style != null) ? style.getStyleLevel() : 0;
    }

    /** Returns true if the current Style can transition into a higher-level Style. */
    public static boolean canCurrentStyleTransition(net.minecraft.world.entity.player.Player player) {
        StyleDefinition def = getCurrentStyleDefinition(player);
        if (def == null)
            return false;

        // Terminal Styles (level 0) cannot transition
        return def.styleLevel() >= 1;
    }
}
