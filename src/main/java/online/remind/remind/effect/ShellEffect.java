
package online.remind.remind.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ShellEffect extends MobEffect {

    public ShellEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public static float getDamageReduction(int amplifier) {
        return switch (Math.max(0, amplifier)) {
            case 0 -> 0.1F; // Shell
            case 1 -> 0.15F; // Shellra
            default -> 0.20F; // Shellga+
        };
    }
}
