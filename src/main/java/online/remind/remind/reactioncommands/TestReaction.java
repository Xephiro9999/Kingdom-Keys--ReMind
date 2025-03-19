package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.common.EventBusSubscriber;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.driveform.ModDriveFormsRM;

public class TestReaction extends ReactionCommand {

	public TestReaction(ResourceLocation registryName, boolean constantCheck) {
		super(registryName, constantCheck);
	}

	@Override
	public void onUse(Player player, LivingEntity livingEntity, LivingEntity livingEntity1) {
		if (conditionsToAppear(player, player)) {
			PlayerData playerData = PlayerData.get(player);

		}
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
		PlayerData playerData = PlayerData.get(player);
		IGlobalDataRM globalData = ModDataRM.getGlobal(player);
//		if (playerData != null) {
//			if (playerData.getActiveDriveForm().equals(ModDriveFormsRM.LIGHT.get().getRegistryName().toString())) {
//				return true;
//			}
//		}
		return false;
	}
}