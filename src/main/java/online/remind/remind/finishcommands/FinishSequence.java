package online.remind.remind.finishcommands;

import net.minecraft.server.level.ServerPlayer;

public interface FinishSequence {

    int getDuration();

    void onStart(ServerPlayer player);

    void onTick(ServerPlayer player, int tick);

    void onEnd(ServerPlayer player, boolean interrupted);

}
