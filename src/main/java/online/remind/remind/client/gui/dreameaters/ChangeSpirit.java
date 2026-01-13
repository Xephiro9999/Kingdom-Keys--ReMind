package online.remind.remind.client.gui.dreameaters;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.gui.DreamEaterMenu;
import online.remind.remind.client.sound.MusicManager;
import online.remind.remind.dreameater.DreamEater;
import online.remind.remind.dreameater.ModDreamEaters;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.network.cts.CSChangeSpiritPacket;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ChangeSpirit extends MenuBackground {

    public ChangeSpirit(String name, Color rgb) {
        super(name, rgb);
    }

    IGlobalDataRM globalData;

    public ChangeSpirit() {
        super("Change Spirit", new Color(241, 115, 24));
        minecraft = Minecraft.getInstance();
    }

    public void onClose() {
        super.onClose();

        Screen next = Minecraft.getInstance().screen;

        if (!(next instanceof DreamEaterMenu || next instanceof ChangeSpirit || next instanceof CreateSpirit)) {
            MusicManager.stop();
        }
    }

    public void reloadMenu() {
        minecraft.setScreen(new ChangeSpirit());
    }

    protected void select(String rl) {
        globalData.setDreamEaterRL(rl);
        PacketHandlerRM.sendToServer(new CSChangeSpiritPacket(rl));
        PacketHandlerRM.syncGlobalToAllAround(minecraft.player, globalData);
        reloadMenu();
    }

    protected void action(String string) {
        if (globalData == null)
            return;

        if (string.equals("back")) {
            minecraft.setScreen(new DreamEaterMenu());
        }
    }

    @Override
    public void init() {
        super.init();
        this.renderables.clear();

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight + 5;
        int button_stats_playerY = button_statsY;

        float buttonPosX = (float) width * 0.03F;
        float subButtonPosX = buttonPosX + 10;

        float buttonWidth = ((float) width * 0.1744F) - 20;

        float dataWidth = ((float) width * 0.1744F) - 10;

        int col1X = (int) (subButtonPosX + buttonWidth + 40), col2X = (int) (col1X + dataWidth * 2) + 10;

        int i = 0;

        globalData = ModDataRM.getGlobal(minecraft.player);

        for (i = 0; i < ModDreamEaters.registry.stream().toList().size(); i++) {
            DreamEater dreamEater = ModDreamEaters.registry.stream().toList().get(i);
            MenuButton btn = new MenuButton((int) buttonPosX, button_statsY + 18 * i, (int) buttonWidth, dreamEater.getTranslationKey(), MenuButton.ButtonType.BUTTON, false, (e) -> {
                select(dreamEater.getRegistryName().toString());
            });
            btn.setData(dreamEater.getRegistryName().toString());
            addRenderableWidget(btn);
        }

        addRenderableWidget(new MenuButton((int) buttonPosX, button_statsY + 18 * i++, (int) buttonWidth, (Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("back");
        }));
    }


    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        if (globalData == null)
            return;

        //Read every button data and if it's the selected one render it in gold
        for (Renderable renderable : renderables) {
            if (renderable instanceof MenuButton btn && btn.getData() != null && !btn.getData().isEmpty()) {
                if (btn.getData().equals(globalData.getDreamEaterRL())) {
                    btn.setMessage(Component.literal(ChatFormatting.GOLD + btn.getMessage().getString()));
                }
            }
        }

        super.render(gui, mouseX, mouseY, partialTicks);
    }


}
