
package online.remind.remind.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ProtectEffect extends MobEffect {

    public ProtectEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public static double getDefenseBonus(int amplifier) {
        return switch (Math.max(0, amplifier)) {
            case 0 -> 5; // Protect
            case 1 -> 10; // Protectra
            default -> 15; // Protectga+
        };
    }
}
