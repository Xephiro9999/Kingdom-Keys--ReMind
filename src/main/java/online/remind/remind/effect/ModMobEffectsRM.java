package online.remind.remind.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import online.remind.remind.KingdomKeysReMind;

public class ModMobEffectsRM {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, KingdomKeysReMind.MODID);

    public static final RegistryObject<MobEffect>
            HASTE_RM = MOB_EFFECTS.register("haste_rm", () -> new HasteEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF)),
            SLOW_RM = MOB_EFFECTS.register("slow_rm", () -> new SlowEffect(MobEffectCategory.HARMFUL, 0xFFFFFF)),
            BERSERK = MOB_EFFECTS.register("berserk", () -> new BerserkEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF)),
            SILENCE = MOB_EFFECTS.register("silence", () -> new SilenceEffect(MobEffectCategory.HARMFUL, 0xFFFFFF)),
            AUTO_LIFE = MOB_EFFECTS.register("auto_life", () -> new AutoLifeEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF)),
            REGEN = MOB_EFFECTS.register("regen", () -> new RegenEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF)),
            STONE = MOB_EFFECTS.register("stone", () -> new StoneEffect(MobEffectCategory.HARMFUL, 0xFFFFFF));
}
