package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.remind.remind.handler.AerialSlamSequenceHandler;

public class AerialSlamRC extends ReactionCommand {

    public AerialSlamRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck, 30, 0xFFD700);
    }

    @Override
    public void onUse(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        AerialSlamSequenceHandler.useSlam(serverPlayer); // RC triggers the second half
    }

    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        return AerialSlamSequenceHandler.canUseSlam(player);
    }
}