package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.network.cts.CSPrestigePacket;

import java.awt.*;

public class PrestigeMenu extends MenuBackground{



    private MenuButton backButton, prestige, levelReq;

    MenuColourBox level, prestigeLevel, gainedHP, gainedMP, gainedSTR, gainedMAG, gainedDEF, currentPath;

    MenuColourBox[] playerWidgets = {level, prestigeLevel, gainedHP, gainedMP, gainedSTR, gainedMAG, gainedDEF, currentPath};



    public PrestigeMenu() {
        super("New Game +", new Color(248, 225, 81));
        minecraft = Minecraft.getInstance();
    }

    protected void action(String string) {
        if (string.equals("back"))
            GUIHelperRM.openAddonMenu();
        if (string.equals("confirm")){
            PacketHandlerRM.sendToServer(new CSPrestigePacket());
            minecraft.setScreen(null);
        }

    }

    @Override
    public void init() {

        Player player;
        final PlayerData playerData = PlayerData.get(minecraft.player);
        IGlobalDataRM addedData = ModDataRM.getGlobal(minecraft.player);

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

        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, button_statsY + 20, (int) buttonWidth, (Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("back");
        }));
        if (playerData.getLevel() == 100) {
            addRenderableWidget(prestige = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, (StringsRM.Gui_Menu_Button_PrestigeConfirm), MenuButton.ButtonType.BUTTON, true, (e) -> {
                action("confirm");

            }));
        } else {
            addRenderableWidget(levelReq = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, "Levels Until NG+: " + (100 - playerData.getLevel()), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("prestige");
            }));
        }


        //Stats
        int c = 0;
        int d = 0;
        int spacer = 14;


        // Levels
        addRenderableWidget(level = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_Level),"" + playerData.getLevel(), 0x000088));
        addRenderableWidget(prestigeLevel = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(StringsRM.Gui_Menu_Button_PrestigeLevel),"" + addedData.getPrestigeLvl(), 0xe3ce44));
        addRenderableWidget(currentPath = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal("Current Path: "),"" + playerData.getChosen(), 0xe3ce44));

        // Stats Column
        addRenderableWidget(gainedHP = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth*2, Utils.translateToLocal("Gained Max HP: "), "" + addedData.getPrestigeLvl() * 2, 0x3ECE44));
        addRenderableWidget(gainedMP = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth*2, Utils.translateToLocal("Gained Max MP: "), "" + addedData.getPrestigeLvl() * 2, 0x3ECE44));


        addRenderableWidget(gainedSTR = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth*2, Utils.translateToLocal("Gained STR: "), "" + addedData.getSTRBonus(), 0xaa190f));
        addRenderableWidget(gainedMAG = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth*2, Utils.translateToLocal("Gained MAG: "), "" + addedData.getMAGBonus(), 0xaa190f));
        addRenderableWidget(gainedDEF = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth*2, Utils.translateToLocal("Gained DEF: "), "" + addedData.getDEFBonus(), 0xaa190f));
    }


}
