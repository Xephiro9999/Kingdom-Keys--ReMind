package online.remind.remind.client.gui;

import online.remind.remind.network.ShowAttackNamePayload;

import net.minecraft.server.level.ServerPlayer;

import net.neoforged.neoforge.network.PacketDistributor;

public final class FF7AttackHud {

    private FF7AttackHud() {
    }

    public static void show(
            ServerPlayer player,
            String attackName
    ) {
        PacketDistributor.sendToPlayer(
                player,
                new ShowAttackNamePayload(attackName)
        );
    }
}