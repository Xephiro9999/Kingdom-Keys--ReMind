package online.remind.remind.client;

import net.minecraft.client.Minecraft;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.stc.SCSyncGlobalCapabilityToAllPacketRM;

public class ClientUtilsRM {

	 public static void syncCapability(SCSyncGlobalCapabilityToAllPacketRM message) {
		 IGlobalDataRM globalData = ModDataRM.getGlobal(Minecraft.getInstance().player);
		 globalData.setBerserkTicks(message.berserkTicks, message.berserkLvl);
	 }

}
