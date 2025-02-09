package online.remind.remind.handler;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.api.event.client.KKInputEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.menu.NoChoiceMenuPopup;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSyncAllClientDataPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.client.gui.GUIHelperRM;
import online.remind.remind.client.sound.ModSoundsRM;
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
			IPlayerCapabilities playerData = event.getHandler().playerData;
			IGlobalCapabilitiesRM globalData = ModCapabilitiesRM.getGlobal(player);

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
					 if (playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.twilight)){
						float yaw = player.getYRot();
						float motionX = -Mth.sin(yaw / 180.0f * (float) Math.PI);
						float motionZ = Mth.cos(yaw / 180.0f * (float) Math.PI);
						double power = 3;
						PacketHandlerRM.sendToServer(new CSSetStepTicksPacket(10, StringsRM.twilightStepType));
						player.push(motionX * power / 1.5, 0, motionZ * power / 1.5);
						InputHandler.qrCooldown = 10;
						 player.level().playSound(player, player.blockPosition(), ModSoundsRM.TWILIGHT_STEP.get(), SoundSource.PLAYERS, 1F, 1F);
						event.setCanceled(true);
					} else if (playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.rageForm)) {
						 // Rage Run
						 float yaw = player.getYRot();
						 float motionX = -Mth.sin(yaw / 180.0f * (float) Math.PI);
						 float motionZ = Mth.cos(yaw / 180.0f * (float) Math.PI);
						 double power = 0.75 + (globalData.getRiskchargeCount());
						 PacketHandlerRM.sendToServer(new CSSetStepTicksPacket(10, StringsRM.rageStepType));
						 player.push(motionX * power / 1.5, 0, motionZ * power / 1.5);
						 InputHandler.qrCooldown = 15 - globalData.getRiskchargeCount();
						 //Insert Sound Here
						 //player.level().playSound(player, player.blockPosition(), ModSoundsRM.TWILIGHT_STEP.get(), SoundSource.PLAYERS, 1F, 1F);
						 event.setCanceled(true);
					}
					// Light Step
					else if (playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.lightForm) || playerData.isAbilityEquipped(StringsRM.lightStep)  && !playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.darkForm) && !playerData.isAbilityEquipped(StringsRM.darkStep)) {
						float yaw = player.getYRot();
						float motionX = -Mth.sin(yaw / 180.0f * (float) Math.PI);
						float motionZ = Mth.cos(yaw / 180.0f * (float) Math.PI);
						double power = lightLevel + 1;
						PacketHandlerRM.sendToServer(new CSSetStepTicksPacket(10, StringsRM.lightStepType));
						// Light Form
						if (playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.lightForm)) {
							player.level().playSound(player, player.blockPosition(), ModSoundsRM.LIGHTSTEP1.get(), SoundSource.PLAYERS, 1F, 1F);
							player.push(motionX * power / 1.5, 0, motionZ * power / 1.5);
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
					} else if (playerData.isAbilityEquipped(StringsRM.darkStep) || playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID+":form_dark")) {
						float yaw = player.getYRot();
						float motionX = -Mth.sin(yaw / 180.0f * (float) Math.PI);
						float motionZ = Mth.cos(yaw / 180.0f * (float) Math.PI);
						double power = darkLevel + 1;

						PacketHandlerRM.sendToServer(new CSSetStepTicksPacket(10, StringsRM.darkStepType));
						// Dark Mode
						if (playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.darkForm)) {
							player.level().playSound(player, player.blockPosition(), ModSoundsRM.DARKSTEP1.get(), SoundSource.PLAYERS, 1F, 1F);
							player.push(motionX * power / 1.5, 0, motionZ * power / 1.5);
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
		} else if(event.getKeybind() == InputHandler.Keybinds.OPENMENU) {
            PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
            PacketHandlerRM.sendToServer(new CSSyncAllClientDataPacketRM());
            LocalPlayer player = Minecraft.getInstance().player;
            if (ModCapabilities.getPlayer(player).getSoAState() != SoAState.COMPLETE) {
                if (player.level().dimension() != ModDimensions.DIVE_TO_THE_HEART) {
                    Minecraft.getInstance().setScreen(new NoChoiceMenuPopup());
                }
            } else {
                GUIHelperRM.openAddonMenu();
                //return;
            }
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void handleKeyInputEvent(InputEvent.Key event) {
		InputHandlerRM.Keybinds key = getPressedKey();
		if(key != null) {
			switch (key) {
				//case SUMMONSPIRIT -> summonSpirit();
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
