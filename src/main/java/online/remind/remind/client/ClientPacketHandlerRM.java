package online.remind.remind.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.stc.SCSyncGlobalDataRM;

public class ClientPacketHandlerRM {
	public static void syncCapability(SCSyncGlobalDataRM message) {
		ModDataRM.get(message.data(), (Player) Minecraft.getInstance().level.getEntity(message.player()));
	}
}
