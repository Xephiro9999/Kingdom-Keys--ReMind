package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.gui.dreameaters.ChangeSpirit;
import online.remind.remind.client.gui.dreameaters.CreateSpirit;
import online.remind.remind.entity.spirits.BaseDreamEaterEntity;
import online.remind.remind.entity.spirits.ChirithyEntity;

import java.awt.*;
import java.util.UUID;

public class DreamEaterMenu extends MenuBackground {

    public DreamEaterMenu(String name, Color rgb) {
        super(name, rgb);
    }

    private MenuButton backButton, changeSpirit, createSpirit, abilityLinks;

    MenuColourBox level, spiritHP, spiritSTR, spiritMAG,  spiritDEF, name, none;
    MenuColourBox[] spiritWidgets = {level, spiritHP, spiritSTR, spiritMAG, spiritDEF, name, none};

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

        int c = 0;
        int d = 0;
        int spacer = 14;

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


        // Display Dream Eater Information
        IGlobalDataRM global = ModDataRM.getGlobal(minecraft.player);
        PlayerData playerData = PlayerData.get(minecraft.player);

        if (global != null){
            String spiritName = "";
            int id = global.getDreamEaterID();
            switch(id){
                case 0:
                    addRenderableWidget(name = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth, "Name:","N/A", 0xffffff));
                    break;
                case 1:
                    addRenderableWidget(name = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth, "Name:","Chirithy", 0xffffff));
                    addRenderableWidget(spiritHP = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth, "Max HP:",""+(int) (20 + (playerData.getMaxHP() / 2f)), 0x31bf14));
                    addRenderableWidget(spiritSTR = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth, "STR:",""+(int) (2 + (playerData.getStrengthStat().getStat() / 5)), 0xbf1414));
                    addRenderableWidget(spiritMAG = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth, "MAG:",""+(int) ( 5 + (playerData.getMagicStat().getStat() * 0.75)), 0x000088));
                    addRenderableWidget(spiritDEF = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth, "DEF:",""+(int) (2 + (playerData.getDefenseStat().getStat() / 2)), 0xbf8d14));
                    break;
            }



            }

        }


    }

