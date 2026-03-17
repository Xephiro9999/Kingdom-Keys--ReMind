package online.remind.remind.styles;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.remind.remind.driveform.StyleForm;
import online.remind.remind.driveform.ModDriveFormsRM;

import online.remind.remind.styles.data.StyleDefinition;
import online.remind.remind.styles.data.StyleLoader;

import online.remind.remind.KingdomKeysReMind;


public class StyleRegistry {

    public static void applyDefinitions() {
        for (DeferredHolder<DriveForm, ? extends DriveForm> entry : ModDriveFormsRM.DRIVE_FORMS.getEntries()) {

            DriveForm form = entry.value();

            // Only Styles get JSON metadata
            if (form instanceof StyleForm style) {

                ResourceLocation id = entry.getId();
                StyleDefinition def = StyleLoader.get(id);

                //System.out.println("Applying definitions to: " + id);


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
}
