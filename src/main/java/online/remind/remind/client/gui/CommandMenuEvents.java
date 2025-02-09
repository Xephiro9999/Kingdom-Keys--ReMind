package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.client.CommandMenuEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuItem;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.remind.remind.lib.StringsRM;

@Mod.EventBusSubscriber(value= Dist.CLIENT)
public class CommandMenuEvents {

    @SubscribeEvent
    public static void cmEnter(CommandMenuEvent.ItemUpdate event) {
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(Minecraft.getInstance().player);
        CommandMenuItem item = event.getItem();
        if (playerData.isAbilityEquipped(StringsRM.darkPassage)) {
            if (item.getId().equals(CommandMenuGui.INSTANCE.portals)) {
                //Show portals
                event.setCanceled(true);
                CommandMenuGui.INSTANCE.updateRootItem(item, CommandMenuGui.INSTANCE.portals, event.getGuiGraphics()); //TODO replace with this when KK 2.5.3 is up event.getItem().getOnUpdate().onUpdate(event.getGuiGraphics());
                event.getItem().setVisible(true);
            } else if (item.getId().equals(CommandMenuGui.INSTANCE.attack)) {
                //Hide attack
                event.setCanceled(true);
                CommandMenuGui.INSTANCE.updateRootItem(item, null, event.getGuiGraphics()); //TODO replace with this when KK 2.5.3 is up event.getItem().getOnUpdate().onUpdate(event.getGuiGraphics());
                event.getItem().setVisible(false);
            }
        }
    }

}
