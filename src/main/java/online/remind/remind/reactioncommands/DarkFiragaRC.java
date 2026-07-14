package online.remind.remind.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.ability.ModAbilitiesRM;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.reactioncommand.DarkFiragaEntity;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

public class DarkFiragaRC extends ReactionCommand {

	public DarkFiragaRC(ResourceLocation registryName, boolean constantCheck) {
		super(registryName, constantCheck, -1);
	}

	@Override
	public void onUse(Player player, LivingEntity target, LivingEntity lockedOnEntity) {
		PlayerData playerData = PlayerData.get(player);
		GlobalDataRM globalData = ModDataRM.getGlobal(player);
		float dmgMult = (PlayerData.get(player).getNumberOfAbilitiesEquipped(ModAbilitiesRM.DARKNESS_BOOST) * 0.3F) + (PlayerData.get(player).getNumberOfAbilitiesEquipped(ModAbilities.FIRE_BOOST) * 0.3F);
		globalData.setRCCooldownTicks(60);
		//System.out.println(globalData.getRCCooldownTicks());
		playerData.remFocus(15);
		PacketHandlerRM.syncGlobalToAllAround(player, globalData);

		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSoundsRM.DARK_FIRAGA.get(), SoundSource.PLAYERS, 1F, 0.7F);
		ThrowableProjectile darkFiraga = new DarkFiragaEntity(player.level(), player, dmgMult);
		player.level().addFreshEntity(darkFiraga);
		darkFiraga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);

	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
		PlayerData playerData = PlayerData.get(player);
		GlobalDataRM globalData = ModDataRM.getGlobal(player);
		if (playerData != null && playerData.getEquippedKeychain(DriveForm.NONE) != null && playerData.getAlignment() == Utils.OrgMember.NONE) {
			if(playerData.isFormActive(ModDriveForms.NONE)){
				if (playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.soulEaterChain.get() && globalData.getRCCooldownTicks() == 0) {
					if (playerData.getFocus() >= 15) {
						return true;
					}
				}
				if (playerData.getEquippedKeychain(DriveForm.NONE).getItem() == ModItems.keybladeOfPeoplesHeartsChain.get() && globalData.getRCCooldownTicks() == 0) {
					if (playerData.getFocus() >= 15) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
