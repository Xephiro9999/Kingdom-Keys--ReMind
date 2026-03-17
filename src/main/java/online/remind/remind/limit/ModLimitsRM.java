package online.remind.remind.limit;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.lib.StringsRM;

import java.util.function.Supplier;

public class ModLimitsRM {

    public static DeferredRegister<Limit> LIMITS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "limits"),KingdomKeysReMind.MODID);

    static int order = 13;

    public static final Supplier<Limit>
        // Add Limits Here

        FIRAGA_WALL = LIMITS.register(StringsRM.firagaWall, () -> new LimitFiragaWall(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, StringsRM.firagaWall), order++, Utils.OrgMember.AXEL));




}
