package online.remind.remind.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.network.cts.CSPanelPacket;
import online.remind.remind.network.cts.CSPrestigePacket;

import java.awt.*;

public class PanelsMenu extends MenuBackground {

    private MenuButton backButton, strUp, magUp, defUp, apUp, giveAbility, lvl, req0, valorUp, wisdomUp, limitUp, masterUp, finalUp, reqV, reqW, reqL, reqM, reqF, reset;

    MenuColourBox str, mag, def, ap;

    MenuColourBox[] playerWidgets = {str, mag, def, ap};

    public PanelsMenu(String name, Color rgb) {
        super(name, rgb);
    }

    public PanelsMenu() {
        super("Panel System", new Color(154, 154, 154));
        minecraft = Minecraft.getInstance();
    }

    protected void action(String string) {
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
        IGlobalCapabilitiesRM globalData = ModCapabilitiesRM.getGlobal(minecraft.player);

        if (string.equals("back"))
            GUIHelperRM.openAddonMenu();

        if (string.equals("req")) {
            minecraft.setScreen(new PanelsMenu());
            minecraft.player.playSound(ModSounds.error.get());
        }

        //Stat Boosts
        if (string.equals("strUp")) {
            //playerData.getStrengthStat().addModifier("Panel", 1, true, false);
            globalData.setPanelChoice("STR");
            PacketHandlerRM.sendToServer(new CSPanelPacket(1));
            minecraft.player.playSound(ModSounds.itemget.get());
            GUIHelperRM.openAddonMenu();
            //minecraft.setScreen(new PanelsMenu());
        }
        if (string.equals("defUp")) {

            //playerData.getDefenseStat().addModifier("Panel", 1, true, false);
            globalData.setPanelChoice("DEF");
            PacketHandlerRM.sendToServer(new CSPanelPacket(3));
            minecraft.player.playSound(ModSounds.itemget.get());
            GUIHelperRM.openAddonMenu();
            //minecraft.setScreen(new PanelsMenu());

        }
        if (string.equals("magUp")) {

            //playerData.getMagicStat().addModifier("Panel", 1, true, false);
            globalData.setPanelChoice("MAG");
            PacketHandlerRM.sendToServer(new CSPanelPacket(2));
            minecraft.player.playSound(ModSounds.itemget.get());
            GUIHelperRM.openAddonMenu();
            //minecraft.setScreen(new PanelsMenu());
        }
        if (string.equals("apUp")) {
            //
            PacketHandlerRM.sendToServer(new CSPanelPacket(4));
            minecraft.player.playSound(ModSounds.itemget.get());
            GUIHelperRM.openAddonMenu();
            //minecraft.setScreen(new PanelsMenu());
        }
        if (string.equals("valorUp")) {
            //
            PacketHandlerRM.sendToServer(new CSPanelPacket(5));
            minecraft.player.playSound(ModSounds.itemget.get());
            GUIHelperRM.openAddonMenu();
        }
        if (string.equals("wisdomUp")) {
            //
            PacketHandlerRM.sendToServer(new CSPanelPacket(6));
            minecraft.player.playSound(ModSounds.itemget.get());
            GUIHelperRM.openAddonMenu();
        }
        if (string.equals("limitUp")) {
            //
            PacketHandlerRM.sendToServer(new CSPanelPacket(7));
            minecraft.player.playSound(ModSounds.itemget.get());
            GUIHelperRM.openAddonMenu();
        }
        if (string.equals("masterUp")) {
            //
            PacketHandlerRM.sendToServer(new CSPanelPacket(8));
            minecraft.player.playSound(ModSounds.itemget.get());
            GUIHelperRM.openAddonMenu();
        }
        if (string.equals("finalUp")) {
            //
            PacketHandlerRM.sendToServer(new CSPanelPacket(9));
            minecraft.player.playSound(ModSounds.itemget.get());
            GUIHelperRM.openAddonMenu();
        }
        if (string.equals("lvl")){
            PacketHandlerRM.sendToServer(new CSPanelPacket(10));
            GUIHelperRM.openAddonMenu();
        }
        if (string.equals("reset")){
            PacketHandlerRM.sendToServer(new CSPanelPacket(11));
            GUIHelperRM.openAddonMenu();
        }
    }

    @Override
    public void init() {

        Player player;
        final IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
        IGlobalCapabilitiesRM addedData = ModCapabilitiesRM.getGlobal(minecraft.player);

        super.init();
        this.renderables.clear();

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight + 5;
        int button_stats_playerY = button_statsY;

        float buttonPosX = (float) width * 0.03F;
        float subButtonPosX = buttonPosX + 10;

        float buttonWidth = ((float) width * 0.1744F) - 40;
        float subButtonWidth = buttonWidth - 10;


        float dataWidth = ((float) width * 0.1744F) - 10;

        int col1X = (int) (subButtonPosX + buttonWidth + 10), col2X = (int) (col1X + dataWidth * 2) + 5;

        int i = 0;

        if (playerData.getHearts() >= 1000 * addedData.getSTRPanel()) {
            addRenderableWidget(strUp = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth,("STR +  -  Cost: "+ ChatFormatting.GREEN + (1000 * (addedData.getSTRPanel()+1))), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("strUp");
            }));
        } else {
            addRenderableWidget(req0 = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, "STR +  -  Cost: "+ ChatFormatting.DARK_RED + (1000 * (addedData.getSTRPanel()+1)), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("req");
            }));
        }
        if (playerData.getHearts() >= 1000 * addedData.getMAGPanel()) {
            addRenderableWidget(magUp = new MenuButton((int) buttonPosX, button_statsY + 20, (int) buttonWidth, ("MAG +  -  Cost: "+ ChatFormatting.GREEN +  (1000 * (addedData.getMAGPanel() +1))), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("magUp");
            }));
        } else {
            addRenderableWidget(req0 = new MenuButton((int) buttonPosX, button_statsY + 20, (int) buttonWidth, "MAG +  -  Cost: " + ChatFormatting.DARK_RED +  (1000 * (addedData.getMAGPanel()+1)), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("req");
            }));
        }
        if (playerData.getHearts() >= 1000 * addedData.getMAGPanel()) {
                addRenderableWidget(defUp = new MenuButton((int) buttonPosX, button_statsY + 40, (int) buttonWidth, ("DEF +  -  Cost: "+ ChatFormatting.GREEN + (1000 * (addedData.getDEFPanel()+1))), MenuButton.ButtonType.BUTTON, false, (e) -> {
                    action("defUp");
                }));
        } else {
            addRenderableWidget(req0 = new MenuButton((int) buttonPosX, button_statsY + 40, (int) buttonWidth, "DEF +  -  Cost: "+ ChatFormatting.DARK_RED + (1000 * (addedData.getDEFPanel() +1)), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("req");
            }));
        }
        if (playerData.getHearts() >= 1000) {
            addRenderableWidget(apUp = new MenuButton((int) buttonPosX, button_statsY + 60, (int) buttonWidth, "AP +  -  Cost: " + ChatFormatting.GREEN + "1000", MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("apUp");
            }));
        } else {
            addRenderableWidget(req0 = new MenuButton((int) buttonPosX, button_statsY + 60, (int) buttonWidth, "AP +  -  Cost: " + ChatFormatting.DARK_RED + "1000", MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("req");
            }));
        }
        if (playerData.getDriveFormLevel(Strings.Form_Valor) < 7 && playerData.getHearts() >= 10000) {
            addRenderableWidget(valorUp = new MenuButton((int) buttonPosX, button_statsY + 80, (int) buttonWidth, ("Valor EXP Up"), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("valorUp");
            }));
        }
        if (playerData.getDriveFormLevel(Strings.Form_Wisdom) < 7 && playerData.getHearts() >= 10000) {
            addRenderableWidget(wisdomUp = new MenuButton((int) buttonPosX, button_statsY + 100, (int) buttonWidth, ("Wisdom EXP Up"), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("wisdomUp");
            }));
        }
        if (playerData.getDriveFormLevel(Strings.Form_Limit) < 7 && playerData.getHearts() >= 10000) {
            addRenderableWidget(limitUp = new MenuButton((int) buttonPosX, button_statsY + 120, (int) buttonWidth, ("Limit EXP Up"), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("limitUp");
            }));
        }
        if (playerData.getDriveFormLevel(Strings.Form_Master) < 7 && playerData.getHearts() >= 10000) {
            addRenderableWidget(masterUp = new MenuButton((int) buttonPosX, button_statsY + 140, (int) buttonWidth, ("Master EXP Up"), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("masterUp");
            }));
        }
        if (playerData.getDriveFormLevel(Strings.Form_Final) < 7 && playerData.getHearts() >= 10000) {
            addRenderableWidget(finalUp = new MenuButton((int) buttonPosX, button_statsY + 160, (int) buttonWidth, ("Final EXP Up"), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("finalUp");
            }));
        }
        if (playerData.getHearts() >= 10000 * playerData.getLevel() && playerData.getLevel() < 100) {
            addRenderableWidget(lvl = new MenuButton((int) buttonPosX + 180, button_statsY, (int) buttonWidth, ("Level Up - Cost: "+ ChatFormatting.GREEN + 10000 * playerData.getLevel()), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("lvl");
            }));
        } else {
            addRenderableWidget(req0 = new MenuButton((int) buttonPosX + 180, button_statsY, (int) buttonWidth,  "Level Up - Cost: "+ ChatFormatting.DARK_RED + 10000 * playerData.getLevel(), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("req");
            }));
        }

        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, button_statsY + 200, (int) buttonWidth, "Reset", MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("reset");
        }));

        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, button_statsY + 220, (int) buttonWidth, (Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("back");
        }));

        // 2.0 Ability Planning.


        //Stats
        int c = 0;
        int d = 0;
        int spacer = 14;

        // Stats Column
        //addRenderableWidget(gainedHP = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth*2, Utils.translateToLocal("Gained Max HP: "), "" + addedData.getPrestigeLvl() * 2, 0x3ECE44));
        //addRenderableWidget(gainedMP = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth*2, Utils.translateToLocal("Gained Max MP: "), "" + addedData.getPrestigeLvl() * 2, 0x3ECE44));


        addRenderableWidget(str = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth, Utils.translateToLocal("Panel STR: "), "" + addedData.getSTRPanel() + " ["+ playerData.getStrength(true) + "]", 0xaa190f));
        addRenderableWidget(mag = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth, Utils.translateToLocal("Panel MAG: "), "" + addedData.getMAGPanel() + " ["+ playerData.getMagic(true) + "]", 0xaa190f));
        addRenderableWidget(def = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth, Utils.translateToLocal("Panel DEF: "), "" + addedData.getDEFPanel() + " ["+ playerData.getDefense(true) + "]", 0xaa190f));
        addRenderableWidget(ap = new MenuColourBox(col2X, button_statsY + (d++* spacer), (int) dataWidth, Utils.translateToLocal("AP: "), "" + (int) playerData.getMaxAPStat().getStat(), 0xaa190f));
    }
}