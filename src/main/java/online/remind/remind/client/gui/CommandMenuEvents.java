package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import online.kingdomkeys.kingdomkeys.api.event.client.CommandMenuEvent;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuItem;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.remind.remind.lib.StringsRM;

@EventBusSubscriber(value= Dist.CLIENT)
public class CommandMenuEvents {

    @SubscribeEvent
    public static void cmEnter(CommandMenuEvent.ItemUpdate event) {
        PlayerData playerData = PlayerData.get(Minecraft.getInstance().player);
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

        if (playerData.isAbilityEquipped(StringsRM.munny_magic)){
            if (playerData.getRecharge()) {
                if (item.getId().equals(CommandMenuGui.INSTANCE.magic)) {
                    CommandMenuGui.INSTANCE.updateRootItem(item, CommandMenuGui.INSTANCE.magic, event.getGuiGraphics());
                    event.getItem().setVisible(true);
                }
            }
        }




    }

}
