package online.remind.remind.handler;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import online.kingdomkeys.kingdomkeys.api.event.client.KKInputEvent;
import online.kingdomkeys.kingdomkeys.client.gui.menu.NoChoiceMenuPopup;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSyncAllClientDataPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.gui.GUIHelperRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.network.cts.CSSetStepTicksPacket;
import online.remind.remind.network.cts.CSSummonSpiritPacket;
import online.remind.remind.network.cts.CSSyncAllClientDataPacketRM;
import org.lwjgl.glfw.GLFW;

public class InputHandlerRM {

	@SubscribeEvent
	public void kkInputEvent(KKInputEvent.Pre event) {
		if (event.getKeybind() == InputHandler.Keybinds.ACTION) {
			Player player = event.getHandler().player;
			PlayerData playerData = event.getHandler().playerData;
			IGlobalDataRM globalData = ModDataRM.getGlobal(player);

			// Light/Dark Step Abilities
			if (InputHandler.qrCooldown <= 0 && (player.getDeltaMovement().x != 0 && player.getDeltaMovement().z != 0)) {
				if (player.isSprinting()) {
					int lightLevel = playerData.getDriveFormLevel(ModDriveFormsRM.LIGHT.get().getRegistryName().toString());
					int darkLevel = playerData.getDriveFormLevel(ModDriveFormsRM.DARK.get().getRegistryName().toString());

					//Org Quick Step
					if (playerData.getAlignment() != Utils.OrgMember.NONE){
						float yaw = player.getYRot();
						float motionX = -Mth.sin(yaw / 180.0f * (float) Math.PI);
						float motionZ = Mth.cos(yaw / 180.0f * (float) Math.PI);
						double power = 8;

						PacketHandlerRM.sendToServer(new CSSetStepTicksPacket(15, StringsRM.orgStepType));

						player.push(motionX * power / 1.5, 0, motionZ * power / 1.5);
						InputHandler.qrCooldown = 15;

						event.setCanceled(true);
					}

					// Twilight Step
					 if (playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.twilight) && playerData.isAbilityEquipped(Strings.quickRun)){
						float yaw = player.getYRot();
						float motionX = -Mth.sin(yaw / 180.0f * (float) Math.PI);
						float motionZ = Mth.cos(yaw / 180.0f * (float) Math.PI);
						double power = 3;
						PacketHandlerRM.sendToServer(new CSSetStepTicksPacket(10, StringsRM.twilightStepType));
						player.push(motionX * power / 1.5, 0, motionZ * power / 1.5);
						InputHandler.qrCooldown = 10;
						 player.level().playSound(player, player.blockPosition(), ModSoundsRM.TWILIGHT_STEP.get(), SoundSource.PLAYERS, 1F, 1F);
						event.setCanceled(true);
					} else if (playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.rageForm) && playerData.isAbilityEquipped(Strings.quickRun)) {
						 // Rage Run
						 float yaw = player.getYRot();
						 float motionX = -Mth.sin(yaw / 180.0f * (float) Math.PI);
						 float motionZ = Mth.cos(yaw / 180.0f * (float) Math.PI);
						 double power = 0.5 + (globalData.getRiskchargeCount());
						 PacketHandlerRM.sendToServer(new CSSetStepTicksPacket(10, StringsRM.rageStepType));
						 player.push(motionX * power / 1.5, 0, motionZ * power / 1.5);
						 InputHandler.qrCooldown = 15 - globalData.getRiskchargeCount();
						 //Insert Sound Here
						 //player.level().playSound(player, player.blockPosition(), ModSoundsRM.TWILIGHT_STEP.get(), SoundSource.PLAYERS, 1F, 1F);
						 event.setCanceled(true);
					}
					// Light Step
					if (playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.lightForm) || playerData.isAbilityEquipped(StringsRM.lightStep) && playerData.isAbilityEquipped(Strings.quickRun)  && !playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.darkForm) && !playerData.isAbilityEquipped(StringsRM.darkStep)) {						float yaw = player.getYRot();
						float motionX = -Mth.sin(yaw / 180.0f * (float) Math.PI);
						float motionZ = Mth.cos(yaw / 180.0f * (float) Math.PI);
						double power = lightLevel;
						PacketHandlerRM.sendToServer(new CSSetStepTicksPacket(10, StringsRM.lightStepType));
						// Light Form
						if (playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.lightForm)) {
							player.level().playSound(player, player.blockPosition(), ModSoundsRM.LIGHTSTEP1.get(), SoundSource.PLAYERS, 1F, 1F);

							player.push(motionX * power / 2, 0, motionZ * power / 2);
							InputHandler.qrCooldown = 20;
						} else if (playerData.isAbilityEquipped(StringsRM.lightStep)) {
							if (lightLevel > 2) {
								player.level().playSound(player, player.blockPosition(), ModSoundsRM.LIGHTSTEP1.get(), SoundSource.PLAYERS, 1F, 1F);
								power = lightLevel - 2;
								player.push(motionX * power, 0, motionZ * power);
								InputHandler.qrCooldown = 20;
							}
						}
						event.setCanceled(true);
					} else if (playerData.isAbilityEquipped(StringsRM.darkStep) && playerData.isAbilityEquipped(Strings.quickRun) || playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID+":form_dark") && playerData.isAbilityEquipped(Strings.quickRun)) {
						float yaw = player.getYRot();
						float motionX = -Mth.sin(yaw / 180.0f * (float) Math.PI);
						float motionZ = Mth.cos(yaw / 180.0f * (float) Math.PI);
						double power = darkLevel;

						PacketHandlerRM.sendToServer(new CSSetStepTicksPacket(10, StringsRM.darkStepType));
						// Dark Mode
						if (playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.darkForm)) {
							player.level().playSound(player, player.blockPosition(), ModSoundsRM.DARKSTEP1.get(), SoundSource.PLAYERS, 1F, 1F);

							player.push(motionX * power / 2, 0, motionZ * power / 2);
							InputHandler.qrCooldown = 20;
						} else if (playerData.isAbilityEquipped(StringsRM.darkStep)) {
							if (darkLevel > 2) {
								player.level().playSound(player, player.blockPosition(), ModSoundsRM.DARKSTEP1.get(), SoundSource.PLAYERS, 1F, 1F);
								power = darkLevel - 2;
								player.push(motionX * power, 0, motionZ * power);
								InputHandler.qrCooldown = 20;
							}
						}
						event.setCanceled(true);
					}
					PacketHandlerRM.syncGlobalToAllAround(player, globalData);
					// PacketHandlerRM.sendToServer(new CSSetStepTicksPacket());
				}
			}
		}
	}

	@SubscribeEvent
	public void handleKeyInputEvent(InputEvent.Key event) {
		InputHandlerRM.Keybinds key = getPressedKey();

		if(key != null) {
			switch (key) {
                case SUMMONSPIRIT -> {
                    if(ModConfigs.spiritsEnabled){
                        summonSpirit();
                    }
                }
            }
		}
	}

	public void summonSpirit(){
		PacketHandlerRM.sendToServer(new CSSummonSpiritPacket());
	}

	public enum Keybinds {
		SUMMONSPIRIT("key.remind.summonspirit", GLFW.GLFW_KEY_Y);

		public final KeyMapping keybinding;
		Keybinds(String name, int defaultKey){
			keybinding = new KeyMapping(name, defaultKey, "key.categories.remind");
		}

		public KeyMapping getKeybind(){
			return keybinding;
		}

		private boolean isPressed(){
			return keybinding.consumeClick();
		}
	}

	private Keybinds getPressedKey(){
		for (Keybinds key : Keybinds.values()){
			if (key.isPressed()){
				return key;
			}
		}
		return null;
	}



}
