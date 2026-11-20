
package online.remind.remind.finishcommands;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import online.remind.remind.KingdomKeysReMind;

@EventBusSubscriber(
        modid = KingdomKeysReMind.MODID,
        bus = EventBusSubscriber.Bus.GAME
)
public class FinishCommandCPEvents {

    @SubscribeEvent
    public static void onEnemyDeath(LivingDeathEvent event) {

        LivingEntity enemy = event.getEntity();

        if (enemy.level().isClientSide()) {
            return;
        }

        if (enemy instanceof ServerPlayer) {
            return;
        }

        if (!(event.getSource().getEntity()
                instanceof ServerPlayer player)) {
            return;
        }

        // Proposed CP rewards.
        int commandPoints = Math.max(
                1,
                Math.min(100, (int) enemy.getMaxHealth() / 5)
        );

        FinishCommandProgression.awardProgress(
                player,
                FinishCommandDefinition.ProgressType.COMMAND_POINTS,
                commandPoints
        );
    }
}
