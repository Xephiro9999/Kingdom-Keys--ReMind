package online.remind.remind.driveform;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.lib.StringsRM;

import java.util.function.Supplier;

public class ModDriveFormsRM {
    public static DeferredRegister<DriveForm> DRIVE_FORMS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "driveforms"), KingdomKeysReMind.MODID);

    static int order = 10;



     public static final Supplier<DriveForm>

        // Forms list

             LIGHT = DRIVE_FORMS.register(StringsRM.DFMA_Prefix + "light", () -> new DriveFormLight(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.DFMA_Prefix + "light"), order++, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/models/armor/light.png"), false, true)),
             DARK = DRIVE_FORMS.register(StringsRM.DFMA_Prefix + "dark", () -> new DriveFormDark(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.DFMA_Prefix + "dark"), order++, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/models/armor/dark.png"), false, true)),
             RAGE = DRIVE_FORMS.register(StringsRM.DFMA_Prefix + "rage", () -> new DriveFormRage(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.DFMA_Prefix + "rage"), order++, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/models/armor/rage.png"), false, false)),
             TWILIGHT = DRIVE_FORMS.register(StringsRM.DFMA_Prefix + "twilight", () -> new DriveFormTwilight(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.DFMA_Prefix + "twilight"), order++, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/models/armor/twilight.png"), true, true)),

        // Commission Forms

            REGEN = DRIVE_FORMS.register(StringsRM.DFMA_Prefix + "regen", () -> new DriveFormRegen(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.DFMA_Prefix + "regen"), order++, ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/models/armor/regen.png"), false, true));

}
