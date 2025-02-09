package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.*;

public class WikiMenu extends MenuBackground {

    int keybladeHelp;
    int magicHelp;
    int formHelp;
    int armorHelp;
    int accessoryHelp;
    int shotlockHelp;

    private MenuButton backButton, magic, forms, armor, accessory, shotlock, keyblades;

    MenuColourBox addedKeyblades, magics, def, ap;

    MenuColourBox[] playerWidgets = {addedKeyblades, magics, def, ap};

    public WikiMenu(String name, Color rgb) {
        super(name, rgb);
    }

    public WikiMenu() {
        super("Journal - Re:Mind", new Color(44, 196, 168));
        minecraft = Minecraft.getInstance();
    }

    protected void action(String string) {
        if (string.equals("back"))
            GUIHelperRM.openAddonMenu();

        if (string.equals(keyblades)){
            keybladeHelp = 1;
            magicHelp = 0;
            formHelp = 0;
            armorHelp = 0;
            accessoryHelp = 0;
            shotlockHelp = 0;
        }
        if (string.equals(magic)){
            keybladeHelp = 0;
            magicHelp = 1;
            formHelp = 0;
            armorHelp = 0;
            accessoryHelp = 0;
            shotlockHelp = 0;
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

        float buttonWidth = ((float) width * 0.1744F)- 20;
        float subButtonWidth = buttonWidth - 10;

        float dataWidth = ((float) width * 0.1744F)-10;

        int col1X = (int) (subButtonPosX + buttonWidth + 40), col2X=(int) (col1X + dataWidth * 2)+10 ;

        int i = 0;




        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, (Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("back");
        }));

        addRenderableWidget(keyblades = new MenuButton((int) buttonPosX, button_statsY + 20, (int) buttonWidth, "Keyblades", MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("keyblades");
        }));

        addRenderableWidget(magic = new MenuButton((int) buttonPosX, button_statsY + 40, (int) buttonWidth, "Magic", MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("magic");
        }));
        addRenderableWidget(forms = new MenuButton((int) buttonPosX, button_statsY + 60, (int) buttonWidth, "Forms", MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("forms");
        }));
        addRenderableWidget(armor = new MenuButton((int) buttonPosX, button_statsY + 80, (int) buttonWidth, "Armor", MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("armor");
        }));
        addRenderableWidget(accessory = new MenuButton((int) buttonPosX, button_statsY + 100, (int) buttonWidth, "Accessories", MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("accessory");
        }));
        addRenderableWidget(shotlock = new MenuButton((int) buttonPosX, button_statsY + 120, (int) buttonWidth, "Shotlocks", MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("shotlock");
        }));

        // Info Widgets
        int c = 0;
        int d = 0;
        int spacer = 14;

        // Keyblade Render

        if (keybladeHelp == 1){
            addRenderableWidget(addedKeyblades = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth, Utils.translateToLocal("Added Keyblades"),  "1", 0xaa190f));
        }



        // Magic Render

    }
}
