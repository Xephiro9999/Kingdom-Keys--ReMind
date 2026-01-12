package online.remind.remind.dreameater;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.spirits.ChirithyEntity;
import online.remind.remind.lib.StringsRM;

import java.util.function.Supplier;

public class ModDreamEaters {

    public static DeferredRegister<DreamEater> DREAM_EATERS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "dream_eaters"), KingdomKeysReMind.MODID);
    public static Registry<DreamEater> registry = DREAM_EATERS.makeRegistry(builder -> builder.sync(true));

    static int order = 0;
    // 0 = None, 1 = Chirithy, 2 = ???

    //TODO: Create system like the shotlocks/magics/drives for better Dream Eater Tracking?
    public static final Supplier<DreamEater>
            NONE = DREAM_EATERS.register(StringsRM.none, () -> new DreamEater(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.none), order++)),
            CHIRITHY = DREAM_EATERS.register(StringsRM.chirithy, () -> new DreamEater(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.chirithy), order++)),
            MEOWWOW = DREAM_EATERS.register(StringsRM.meowWow, () -> new DreamEater(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.meowWow), order++));


}

