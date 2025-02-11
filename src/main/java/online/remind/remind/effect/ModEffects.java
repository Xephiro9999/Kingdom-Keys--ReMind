package online.remind.remind.effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.remind.remind.KingdomKeysReMind;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(Registries.MOB_EFFECT, KingdomKeysReMind.MODID);

    public static void register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }
}
