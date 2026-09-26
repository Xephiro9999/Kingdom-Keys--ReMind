package online.remind.remind.handler;

import java.util.function.BooleanSupplier;

public class ScriptedAttackDamageHandler {

    private static final ThreadLocal<Boolean> APPLYING_DAMAGE = ThreadLocal.withInitial(() -> false);

    public static boolean isApplyingDamage() {
        return APPLYING_DAMAGE.get();
    }

    public static boolean hurt(BooleanSupplier damageAction) {
        APPLYING_DAMAGE.set(true);

        try {
            return damageAction.getAsBoolean();
        } finally {
            APPLYING_DAMAGE.set(false);
        }
    }
}