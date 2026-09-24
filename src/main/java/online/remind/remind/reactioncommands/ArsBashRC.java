package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.remind.remind.handler.ArsArcanumSequenceHandler;

public class ArsBashRC extends ReactionCommand {

    public ArsBashRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck, 20, 0xFFD700);
    }

    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        return ArsArcanumSequenceHandler.canUseBash(player); // Only show during the Bash phase
    }

    @Override
    public void onUse(Player player, LivingEntity livingEntity, LivingEntity target) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        ArsArcanumSequenceHandler.useBash(serverPlayer); // Advance Ars Arcanum by one Bash hit
    }
}