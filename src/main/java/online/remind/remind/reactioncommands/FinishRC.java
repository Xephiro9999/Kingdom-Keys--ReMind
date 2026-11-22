
package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;

import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.finishcommands.FinishCommandExecutor;
import online.remind.remind.network.PacketHandlerRM;

public class FinishRC extends ReactionCommand {

	public FinishRC(ResourceLocation registryName, boolean constantCheck) {
		super(registryName, constantCheck, 15 * 20);
	}

	@Override
	public void onUse(
			Player player,
			LivingEntity livingEntity,
			LivingEntity livingEntity1
	) {
		// Finishing attacks must execute on the server.
		if (!(player instanceof ServerPlayer serverPlayer)) {
			return;
		}

		if (!conditionsToAppear(player, player)) {
			return;
		}

		PlayerData playerData = PlayerData.get(player);
		GlobalDataRM remindData = ModDataRM.getGlobal(player);

		if (playerData == null || remindData == null) {
			return;
		}

		// NEW: Execute the player's equipped Finish Command.
		FinishCommandExecutor.execute(
				serverPlayer,
				livingEntity
		);

		// Preserve the original Situation Gauge cleanup.
		remindData.setStyle("NONE");
		remindData.setSituationValue(0);
		remindData.clearSituationSpells();

		// Remove the Finish Reaction Command.
		playerData.removeReactionCommand(getRegistryName());

		// Synchronize the updated data.
		PacketHandlerRM.syncGlobalToAllAround(player, remindData);
	}

	@Override
	public boolean conditionsToAppear(
			Player player,
			LivingEntity livingEntity
	) {
		PlayerData playerData = PlayerData.get(player);
		GlobalDataRM remindData = ModDataRM.getGlobal(player);

		if (playerData == null || remindData == null) {
			return false;
		}

		ResourceLocation activeDriveForm = playerData.getActiveDriveForm();

		// Normal Finish Commands are only available outside Styles.
		return DriveForm.NONE.equals(activeDriveForm);
	}
}
