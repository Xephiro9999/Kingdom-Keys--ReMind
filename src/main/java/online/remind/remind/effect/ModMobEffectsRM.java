package online.remind.remind.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.remind.remind.KingdomKeysReMind;

public class ModMobEffectsRM {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, KingdomKeysReMind.MODID);

    public static final Holder<MobEffect>
            HASTE_RM = MOB_EFFECTS.register("haste_rm", () -> new HasteEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF)),
            SLOW_RM = MOB_EFFECTS.register("slow_rm", () -> new SlowEffect(MobEffectCategory.HARMFUL, 0xFFFFFF)),
            BERSERK = MOB_EFFECTS.register("berserk", () -> new BerserkEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF)),
            SILENCE = MOB_EFFECTS.register("silence", () -> new SilenceEffect(MobEffectCategory.HARMFUL, 0xFFFFFF)),
            AUTO_LIFE = MOB_EFFECTS.register("auto_life", () -> new AutoLifeEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF)),
            REGEN = MOB_EFFECTS.register("regen", () -> new RegenEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF)),
            STONE = MOB_EFFECTS.register("stone", () -> new StoneEffect(MobEffectCategory.HARMFUL, 0xFFFFFF)),
            CONFUSE = MOB_EFFECTS.register("confuse", () -> new StoneEffect(MobEffectCategory.HARMFUL, 0xFFFFFF));
}
