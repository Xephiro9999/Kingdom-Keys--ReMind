package online.remind.remind.styles.data;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.remind.remind.driveform.StyleForm;
import online.remind.remind.driveform.ModDriveFormsRM;

import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.styles.StyleElement;

import java.util.*;

public class StyleRegistry {

    // NEW: Reverse lookup table for SGaugeHandler
    private static final Map<StyleElement, Set<ResourceLocation>> ELEMENT_TO_STYLES = new HashMap<>();

    public static void applyDefinitions() {

        ELEMENT_TO_STYLES.clear(); // Important: rebuild on reload

        for (DeferredHolder<DriveForm, ? extends DriveForm> entry : ModDriveFormsRM.DRIVE_FORMS.getEntries()) {

            DriveForm form = entry.value();

            if (form instanceof StyleForm style) {

                ResourceLocation id = entry.getId();
                StyleDefinition def = StyleLoader.get(id);

                if (def == null) {
                    KingdomKeysReMind.LOGGER.warn(
                            "No Style JSON found for Style '{}'", id
                    );
                    continue;
                }

                // Inject JSON metadata into the StyleForm instance
                style.setStyleLevel(def.level());
                style.setTriggers(def.triggers());
                style.setWeaponRestrictions(
                        def.requiresWeapons(),
                        def.requiredWeapons()
                );

                // NEW: Populate reverse lookup table
                for (StyleElement element : def.triggers()) {
                    ELEMENT_TO_STYLES
                            .computeIfAbsent(element, k -> new HashSet<>())
                            .add(id);
                }

                KingdomKeysReMind.LOGGER.info(
                        "Loaded Style '{}' (level {}, {} triggers, {} required weapons)",
                        id,
                        def.level(),
                        def.triggers().size(),
                        def.requiredWeapons().size()
                );
            }
        }
    }

    // SGaugeHandler uses this
    public static Set<ResourceLocation> getStylesForElement(StyleElement element) {
        return ELEMENT_TO_STYLES.getOrDefault(element, Collections.emptySet());
    }
}
