package online.remind.remind.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.stc.SCSyncGlobalCapabilityToAllPacketRM;

import java.util.UUID;

public class ClientUtilsRM {

	 public static void syncCapability(SCSyncGlobalCapabilityToAllPacketRM message) {
		 GlobalDataRM globalData = ModDataRM.getGlobal(Minecraft.getInstance().player);
		 globalData.setBerserkTicks(message.berserkTicks, message.berserkLvl);
	 }

    public static Entity getEntityByUUIDClient(UUID uuid) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;

        if (level == null)
            return null;

        for (Entity entity : level.entitiesForRendering()) {
            if (entity.getUUID().equals(uuid)) {
                return entity;
            }
        }
        return null;
    }


}
