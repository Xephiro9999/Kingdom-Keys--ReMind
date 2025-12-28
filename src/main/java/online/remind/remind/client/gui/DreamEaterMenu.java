package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.gui.dreameaters.ChangeSpirit;
import online.remind.remind.client.gui.dreameaters.CreateSpirit;
import online.remind.remind.entity.spirits.ChirithyEntity;

import java.awt.*;
import java.util.UUID;

public class DreamEaterMenu extends MenuBackground {

    public DreamEaterMenu(String name, Color rgb) {
        super(name, rgb);
    }

    private MenuButton backButton, changeSpirit, createSpirit, abilityLinks;

    public DreamEaterMenu() {
        super("Dream Eaters", new Color(236, 85, 236));
        minecraft = Minecraft.getInstance();
    }




    protected void action(String string) {
        if (string.equals("back")) {
            GUIHelperRM.openAddonMenu();
        }
        if (string.equals("changeSpirit")){
            minecraft.setScreen(new ChangeSpirit());
        }
        if (string.equals("createSpirit")){
            minecraft.setScreen(new CreateSpirit());
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

        addRenderableWidget(changeSpirit = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, "Change Spirit", MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("changeSpirit");
        }));
        addRenderableWidget(createSpirit = new MenuButton((int) buttonPosX, button_statsY + 20, (int) buttonWidth, "Create Spirit", MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("createSpirit");
        }));
        addRenderableWidget(abilityLinks = new MenuButton((int) buttonPosX, button_statsY +40, (int) buttonWidth, "Ability Links", MenuButton.ButtonType.BUTTON, true, (e) -> {
            action(null);
        }));
        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, button_statsY +60, (int) buttonWidth, (Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("back");
        }));
    }
}
