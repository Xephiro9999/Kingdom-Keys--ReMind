package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KKArmorItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSEquipAccessories;
import online.kingdomkeys.kingdomkeys.network.cts.CSEquipArmor;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.network.cts.CSBoostPacket;
import online.remind.remind.network.cts.CSPrestigePacket;

import java.awt.*;

public class PrestigeMenu extends MenuBackground{

    public int slot = -1;



    private MenuButton backButton, prestige, levelReq, toggleOn, toggleOff;

    MenuColourBox level, prestigeLevel, gainedHP, gainedMP, gainedSTR, gainedMAG, gainedDEF, currentPath, warriorPath, mysticPath, guardianPath;

    MenuColourBox[] playerWidgets = {level, prestigeLevel, gainedHP, gainedMP, gainedSTR, gainedMAG, gainedDEF, currentPath, warriorPath, mysticPath, guardianPath};



    public PrestigeMenu() {
        super("New Game +", new Color(248, 225, 81));
        minecraft = Minecraft.getInstance();
        this.slot = slot;
    }

    protected void action(String string) {
        if (string.equals("back"))
            GUIHelperRM.openAddonMenu();
        if (string.equals("confirm")){
            PacketHandlerRM.sendToServer(new CSPrestigePacket());
            minecraft.setScreen(null);
        }
        if (string.equals("toggleOff")){
            PacketHandlerRM.sendToServer(new CSBoostPacket(1));
            GUIHelperRM.openAddonMenu();
        }
        if (string.equals("toggleOn")){
            PacketHandlerRM.sendToServer(new CSBoostPacket(3));
            GUIHelperRM.openAddonMenu();
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

        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, button_statsY + 40, (int) buttonWidth, (Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, false, (e) -> {
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
        if(addedData.getNGPEnabled() == 1) {
            addRenderableWidget(toggleOff = new MenuButton((int) buttonPosX, button_statsY + 20, (int) buttonWidth, ("Toggle OFF"), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("toggleOff");
            }));
        } else if (addedData.getNGPEnabled() == 0){
            addRenderableWidget(toggleOn = new MenuButton((int) buttonPosX, button_statsY + 20, (int) buttonWidth, ("Toggle ON"), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("toggleOn");
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
        addRenderableWidget(warriorPath = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal("NG+ \uD83D\uDDE1 Count: "),"" + addedData.getNGPWarriorCount(), 0xe3ce44));
        addRenderableWidget(mysticPath = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal("NG+ ⚚ Count: "),"" + addedData.getNGPMysticCount(), 0xe3ce44));
        addRenderableWidget(guardianPath = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal("NG+ \uD83D\uDEE1 Count: "),"" + addedData.getNGPGuardianCount(), 0xe3ce44));

        // Stats Column
        addRenderableWidget(gainedHP = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth*2, Utils.translateToLocal("Gained Max HP: "), "" + addedData.getPrestigeLvl() * 2, 0x3ECE44));
        addRenderableWidget(gainedMP = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth*2, Utils.translateToLocal("Gained Max MP: "), "" + addedData.getPrestigeLvl() * 2, 0x3ECE44));


        addRenderableWidget(gainedSTR = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth*2, Utils.translateToLocal("Gained STR: "), "" + addedData.getSTRBonus(), 0xaa190f));
        addRenderableWidget(gainedMAG = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth*2, Utils.translateToLocal("Gained MAG: "), "" + addedData.getMAGBonus(), 0xaa190f));
        addRenderableWidget(gainedDEF = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth*2, Utils.translateToLocal("Gained DEF: "), "" + addedData.getDEFBonus(), 0xaa190f));
    }


}
