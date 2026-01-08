package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.*;

public class CreditsScreen extends MenuBackground {

    private MenuButton backButton;

    MenuColourBox donators, specialThanks;

    MenuColourBox[] thanksWidgets = {donators, specialThanks};

    public CreditsScreen() {
        super("Credits", new Color(97, 12, 12));
        minecraft = Minecraft.getInstance();
    }

    protected void action(String string) {
        if (string.equals("back"))
            PacketHandler.sendToServer(new CSOpenMenu());
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

        float buttonWidth = ((float) width * 0.1744F)- 20;
        float subButtonWidth = buttonWidth - 10;


        float dataWidth = ((float) width * 0.1744F)-10;

        int col1X = (int) (subButtonPosX + buttonWidth + 40), col2X=(int) (col1X + dataWidth * 2)+10 ;

        int i = 0;


        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, (Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("back");
        }));


        // Credits Spacers

        int c = 0;
        int d = 0;
        int spacer = 14;

        // Special Thanks

        addRenderableWidget(specialThanks = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal("Special Thanks to: "),"", 0xe3ce44));
        addRenderableWidget(specialThanks = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal("My loving Fiancee, Ayame."),"", 0x9900cb));
        addRenderableWidget(specialThanks = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal("Estelle"),"For helping teach me coding.", 0xfe8600));
        addRenderableWidget(specialThanks = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal("Nathan"),"For dealing with my BS", 0x00d18a));
        addRenderableWidget(specialThanks = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal("Abelatox"),"For dealing with me and being patient.", 0x1b8621));
        addRenderableWidget(specialThanks = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal("PROJECT: Keyblade Community"),"", 0xf7514f));




        // Donators

        addRenderableWidget(donators = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth*2, Utils.translateToLocal("Donators:"),"", 0x0b0fff));
        addRenderableWidget(donators = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth*2, Utils.translateToLocal("RealRegen"),"", 0x0b0fff));




    }
}
