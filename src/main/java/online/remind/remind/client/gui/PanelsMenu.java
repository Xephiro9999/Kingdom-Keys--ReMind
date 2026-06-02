package online.remind.remind.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.network.cts.CSSyncAllClientDataPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.network.cts.CSBoostPacket;
import online.remind.remind.network.cts.CSPanelPacket;
import net.neoforged.neoforge.network.PacketDistributor;
import online.remind.remind.network.cts.CSOrganizationPanelPacket;
import online.remind.remind.network.PanelPacketAction;
import online.remind.remind.panels.*;

import net.minecraft.network.chat.Component;

import org.lwjgl.glfw.GLFW;

import java.util.List;

import java.awt.*;

public class PanelsMenu extends MenuBackground {


    private static final int ORG_SLOT_SIZE = 28;

    private static final int ORG_PANEL_PICKER_SLOT_SIZE = 24;
    private static final int ORG_PANEL_PICKER_GAP = 6;

    private int orgPickerX;
    private int orgPickerY;

    private int orgPanelAreaX;
    private int orgPanelAreaY;
    private int orgPanelAreaWidth;
    private int orgPanelAreaHeight;

    private ResourceLocation selectedOrgPanel = PanelRegistry.STRENGTH_UNIT; // DUMMY

    private int orgGridX;
    private int orgGridY;

    int ticks = 0;

    private MenuButton backButton, strUp, magUp, defUp, apUp, giveAbility, lvl, req0, valorUp, wisdomUp, limitUp, masterUp, finalUp, reqV, reqW, reqL, reqM, reqF, armorUp, accessoryUp, rejectOrg, reset, toggleOff, toggleOn, orgPlaceSTR, orgPlaceMAG, orgPlaceDEF, orgPlaceAP, orgPlaceLV, orgRemove00, orgClear;
    MenuColourBox str, mag, def, ap;

    MenuColourBox[] playerWidgets = {str, mag, def, ap};
    MenuBox box;


    public PanelsMenu(String name, Color rgb) {
        super(name, rgb);
    }

    public PanelsMenu() {
        super("Panel System", new Color(154, 154, 154));
        minecraft = Minecraft.getInstance();
    }

    public void reloadMenu(){
        GUIHelperRM.openPanelMenu();
    }

    protected void action(String string) {
        PlayerData playerData = PlayerData.get(minecraft.player);
        GlobalDataRM globalData = ModDataRM.getGlobal(minecraft.player);

        switch(string){
            case "back" ->
                PacketHandler.sendToServer(new CSOpenMenu());

            case "reg" -> {
                minecraft.setScreen(new PanelsMenu());
                minecraft.player.playSound(ModSounds.error.get());
            }
            case "strUp" -> {
               // globalData.setPanelChoice("STR");
                PacketHandlerRM.sendToServer(new CSPanelPacket(1));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
                //init();
                this.reloadMenu();

            }
            case "magUp" -> {
               // globalData.setPanelChoice("MAG");
                PacketHandlerRM.sendToServer(new CSPanelPacket(2));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
                //init();
                this.reloadMenu();
            }
            case "defUp" -> {
              //  globalData.setPanelChoice("DEF");
                PacketHandlerRM.sendToServer(new CSPanelPacket(3));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
                this.reloadMenu();
            }
            case "apUp" -> {
                PacketHandlerRM.sendToServer(new CSPanelPacket(4));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
                this.reloadMenu();
            }
            case "valorUp" -> {
                PacketHandlerRM.sendToServer(new CSPanelPacket(5));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
                this.reloadMenu();
            }
            case "wisdomUp" -> {
                PacketHandlerRM.sendToServer(new CSPanelPacket(6));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
                this.reloadMenu();
            }
            case "limitUp" -> {
                PacketHandlerRM.sendToServer(new CSPanelPacket(7));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
                this.reloadMenu();
            }
            case "masterUp" -> {
                PacketHandlerRM.sendToServer(new CSPanelPacket(8));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
                this.reloadMenu();
            }
            case "finalUp" -> {
                PacketHandlerRM.sendToServer(new CSPanelPacket(9));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
                this.reloadMenu();
            }
            case "lvl" -> {
                PacketHandlerRM.sendToServer(new CSPanelPacket(10));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                this.reloadMenu();

            }
            case "armorUp" -> {
                PacketHandlerRM.sendToServer(new CSPanelPacket(12));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                init();
            }
            case "accessoryUp" -> {
                PacketHandlerRM.sendToServer(new CSPanelPacket(13));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                init();
            }
            case "rejectOrg" -> {
                PacketHandlerRM.sendToServer(new CSPanelPacket(14));
                //PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                PacketHandler.sendToServer(new CSOpenMenu());
            }
            case "reset" -> {
                PacketHandlerRM.sendToServer(new CSPanelPacket(11));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                PacketHandler.sendToServer(new CSOpenMenu());
            }
            case "toggleOff" -> {
                PacketHandlerRM.sendToServer(new CSBoostPacket(2));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                PacketHandler.sendToServer(new CSOpenMenu());
            }
            case "toggleOn" -> {
                PacketHandlerRM.sendToServer(new CSBoostPacket(4));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                PacketHandler.sendToServer(new CSOpenMenu());
            }
            case "orgPlaceSTR" -> {
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.PLACE,
                        PanelRegistry.STRENGTH_UNIT,
                        0,
                        0
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
                this.reloadMenu();
            }

            case "orgPlaceMAG" -> {
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.PLACE,
                        PanelRegistry.MAGIC_UNIT,
                        1,
                        0
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
                this.reloadMenu();
            }

            case "orgPlaceDEF" -> {
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.PLACE,
                        PanelRegistry.DEFENSE_UNIT,
                        2,
                        0
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
                this.reloadMenu();
            }

            case "orgPlaceAP" -> {
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.PLACE,
                        PanelRegistry.AP_UNIT,
                        3,
                        0
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
                this.reloadMenu();
            }

            case "orgPlaceLV" -> {
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.PLACE,
                        PanelRegistry.LEVEL_UP,
                        4,
                        0
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
                this.reloadMenu();
            }

            case "orgRemove00" -> {
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.REMOVE,
                        PanelRegistry.STRENGTH_UNIT,
                        0,
                        0
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
                this.reloadMenu();
            }

            case "orgClear" -> {
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.CLEAR,
                        PanelRegistry.STRENGTH_UNIT,
                        0,
                        0
                ));
                minecraft.player.playSound(ModSounds.error.get());
                this.reloadMenu();
            }

        }

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        ResourceLocation clickedPickerPanel = getClickedPanelPicker((int) mouseX, (int) mouseY);

        if (clickedPickerPanel != null && button == 0) {
            GlobalDataRM addedData = ModDataRM.getGlobal(minecraft.player);

            if (addedData != null && addedData.getOwnedOrganizationPanelCount(clickedPickerPanel) <= 0) {
                if (minecraft != null && minecraft.player != null) {
                    minecraft.player.playSound(ModSounds.error.get());
                }

                return true;
            }

            selectedOrgPanel = clickedPickerPanel;

            if (minecraft != null && minecraft.player != null) {
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            return true;
        }

        int gridX = getOrgGridMouseX((int) mouseX);
        int gridY = getOrgGridMouseY((int) mouseY);

        if (gridX >= 0 && gridY >= 0) {
            if (button == 0) {
                // Left click = place selected panel
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.PLACE,
                        selectedOrgPanel,
                        gridX,
                        gridY
                ));

                if (minecraft != null && minecraft.player != null) {
                    minecraft.player.playSound(ModSounds.itemget.get());
                }

                return true;
            }

            if (button == 1) {
                // Right click = remove panel
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.REMOVE,
                        selectedOrgPanel,
                        gridX,
                        gridY
                ));

                if (minecraft != null && minecraft.player != null) {
                    minecraft.player.playSound(ModSounds.itemget.get());
                }

                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }


    private static final ResourceLocation[] ORG_PICKER_PANELS = new ResourceLocation[] {
            PanelRegistry.STRENGTH_UNIT,
            PanelRegistry.MAGIC_UNIT,
            PanelRegistry.DEFENSE_UNIT,
            PanelRegistry.AP_UNIT,
            PanelRegistry.LEVEL_UP,

            PanelRegistry.STRENGTH_UNIT_L,
            PanelRegistry.MAGIC_UNIT_L,
            PanelRegistry.DEFENSE_UNIT_L,
            PanelRegistry.AP_UNIT_L,
            PanelRegistry.LEVEL_DOUBLER,

            PanelRegistry.POWER_LINK,
            PanelRegistry.MAGIC_LINK,
            PanelRegistry.GUARD_LINK,
            PanelRegistry.LEVEL_LINK
    };
    private ResourceLocation getClickedPanelPicker(int mouseX, int mouseY) {
        ResourceLocation[] panels = new ResourceLocation[] {
                PanelRegistry.STRENGTH_UNIT,
                PanelRegistry.MAGIC_UNIT,
                PanelRegistry.DEFENSE_UNIT,
                PanelRegistry.AP_UNIT,
                PanelRegistry.LEVEL_UP
        };

        for (int i = 0; i < ORG_PICKER_PANELS.length; i++) {
            int x = orgPickerX + i * (ORG_PANEL_PICKER_SLOT_SIZE + ORG_PANEL_PICKER_GAP);
            int y = orgPickerY;

            boolean inside = mouseX >= x
                    && mouseX < x + ORG_PANEL_PICKER_SLOT_SIZE
                    && mouseY >= y
                    && mouseY < y + ORG_PANEL_PICKER_SLOT_SIZE;

            if (inside) {
                return ORG_PICKER_PANELS[i];
            }
        }

        return null;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_1 -> {
                selectedOrgPanel = PanelRegistry.STRENGTH_UNIT;
                return true;
            }
            case GLFW.GLFW_KEY_2 -> {
                selectedOrgPanel = PanelRegistry.MAGIC_UNIT;
                return true;
            }
            case GLFW.GLFW_KEY_3 -> {
                selectedOrgPanel = PanelRegistry.DEFENSE_UNIT;
                return true;
            }
            case GLFW.GLFW_KEY_4 -> {
                selectedOrgPanel = PanelRegistry.AP_UNIT;
                return true;
            }
            case GLFW.GLFW_KEY_5 -> {
                selectedOrgPanel = PanelRegistry.LEVEL_UP;
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    public void tick() {
        super.tick();
        ticks++;
    }

    @Override
    public void init() {



        Player player;
        final PlayerData playerData = PlayerData.get(minecraft.player);
        GlobalDataRM addedData = ModDataRM.getGlobal(minecraft.player);
        ticks = 0;

        this.renderables.clear();

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight + 5;
        int button_stats_playerY = button_statsY;

        float buttonPosX = (float) width * 0.03F;
        float subButtonPosX = buttonPosX + 10;

        float buttonWidth = ((float) width * 0.1744F) - 40;
        float subButtonWidth = buttonWidth - 10;

        float dataWidth = ((float) width * 0.1744F) - 10;

        int col1X = (int) (subButtonPosX + buttonWidth + 10);
        int col2X = (int) (col1X + dataWidth * 2) + 5;

        // Organization Panel Grid position
        int contentX = (int) (buttonPosX + buttonWidth + 20);
        int contentY = button_statsY + 70;
        int contentWidth = width - contentX - 180;

        int orgGridCols = 5;

        if (addedData != null && addedData.getOrganizationPanelGrid() != null) {
            orgGridCols = addedData.getOrganizationPanelGrid().getWidth();
        }

        int gridWidth = orgGridCols * ORG_SLOT_SIZE;

        this.orgGridX = contentX + (contentWidth / 2) - (gridWidth / 2);
        this.orgGridY = contentY + 16;

        int pickerWidth = (ORG_PICKER_PANELS.length * ORG_PANEL_PICKER_SLOT_SIZE)
                + ((ORG_PICKER_PANELS.length - 1) * ORG_PANEL_PICKER_GAP);

        this.orgPickerX = orgGridX + (gridWidth / 2) - (pickerWidth / 2);
        this.orgPickerY = contentY - 52;

        int i = 0;


        // Form Leveling
//        if (ModConfigs.driveLevelsEnabled) {
//            if (playerData.getDriveFormLevel(Strings.Form_Valor) < 7 && playerData.getHearts() >= 5000) {
//                addRenderableWidget(valorUp = new MenuButton((int) buttonPosX, button_statsY + 80, (int) buttonWidth, (ChatFormatting.DARK_RED + "Valor " + ChatFormatting.WHITE + "EXP Up, Cost: " + ChatFormatting.GREEN + "5000"), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("valorUp");
//                }));
//            } else if (playerData.getDriveFormLevel(Strings.Form_Valor) < 7 && playerData.getHearts() < 5000) {
//                addRenderableWidget(valorUp = new MenuButton((int) buttonPosX, button_statsY + 80, (int) buttonWidth, (ChatFormatting.DARK_RED + "Valor " + ChatFormatting.WHITE + "EXP Up, Cost: " + ChatFormatting.DARK_RED + "5000"), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("req");
//                }));
//            } else if ((playerData.getDriveFormLevel(Strings.Form_Valor) == 7)) {
//                addRenderableWidget(req0 = new MenuButton((int) buttonPosX, button_statsY + 80, (int) buttonWidth, ChatFormatting.GOLD + "☆ Valor Form MAXED ☆", MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("req");
//                }));
//            }
//
//            if (playerData.getDriveFormLevel(Strings.Form_Wisdom) < 7 && playerData.getHearts() >= 5000) {
//                addRenderableWidget(wisdomUp = new MenuButton((int) buttonPosX, button_statsY + 100, (int) buttonWidth, (ChatFormatting.BLUE + "Wisdom " + ChatFormatting.WHITE + "EXP Up, Cost: " + ChatFormatting.GREEN + "5000"), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("wisdomUp");
//                }));
//
//            } else if (playerData.getDriveFormLevel(Strings.Form_Wisdom) < 7 && playerData.getHearts() < 5000) {
//                addRenderableWidget(wisdomUp = new MenuButton((int) buttonPosX, button_statsY + 100, (int) buttonWidth, (ChatFormatting.BLUE + "Wisdom " + ChatFormatting.WHITE + "EXP Up, Cost: " + ChatFormatting.DARK_RED + "5000"), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("req");
//                }));
//            } else if ((playerData.getDriveFormLevel(Strings.Form_Wisdom) == 7)) {
//                addRenderableWidget(req0 = new MenuButton((int) buttonPosX, button_statsY + 100, (int) buttonWidth, ChatFormatting.GOLD + "☆ Wisdom Form MAXED ☆", MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("req");
//                }));
//            }
//            if (playerData.getDriveFormLevel(Strings.Form_Limit) < 7 && playerData.getHearts() >= 5000) {
//                addRenderableWidget(limitUp = new MenuButton((int) buttonPosX, button_statsY + 120, (int) buttonWidth, (ChatFormatting.LIGHT_PURPLE + "Limit " + ChatFormatting.WHITE + "EXP Up, Cost: " + ChatFormatting.GREEN + "5000"), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("limitUp");
//                }));
//            } else if (playerData.getDriveFormLevel(Strings.Form_Limit) < 7 && playerData.getHearts() < 5000) {
//                addRenderableWidget(limitUp = new MenuButton((int) buttonPosX, button_statsY + 120, (int) buttonWidth, (ChatFormatting.LIGHT_PURPLE + "Limit " + ChatFormatting.WHITE + "EXP Up, Cost: " + ChatFormatting.DARK_RED + "5000"), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("req");
//                }));
//            } else if ((playerData.getDriveFormLevel(Strings.Form_Limit) == 7)) {
//                addRenderableWidget(req0 = new MenuButton((int) buttonPosX, button_statsY + 120, (int) buttonWidth, ChatFormatting.GOLD + "☆ Limit Form MAXED ☆", MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("req");
//                }));
//            }
//            if (playerData.getDriveFormLevel(Strings.Form_Master) < 7 && playerData.getHearts() >= 5000) {
//                addRenderableWidget(masterUp = new MenuButton((int) buttonPosX, button_statsY + 140, (int) buttonWidth, (ChatFormatting.YELLOW + "Master " + ChatFormatting.WHITE + "EXP Up, Cost: " + ChatFormatting.GREEN + "5000"), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("masterUp");
//                }));
//            } else if (playerData.getDriveFormLevel(Strings.Form_Master) < 7 && playerData.getHearts() < 5000) {
//                addRenderableWidget(masterUp = new MenuButton((int) buttonPosX, button_statsY + 140, (int) buttonWidth, (ChatFormatting.YELLOW + "Master " + ChatFormatting.WHITE + "EXP Up, Cost: " + ChatFormatting.DARK_RED + "5000"), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("req");
//                }));
//            } else if ((playerData.getDriveFormLevel(Strings.Form_Master) == 7)) {
//                addRenderableWidget(req0 = new MenuButton((int) buttonPosX, button_statsY + 140, (int) buttonWidth, ChatFormatting.GOLD + "☆ Master Form MAXED ☆", MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("req");
//                }));
//            }
//            if (playerData.getDriveFormLevel(Strings.Form_Final) < 7 && playerData.getHearts() >= 5000) {
//                addRenderableWidget(finalUp = new MenuButton((int) buttonPosX, button_statsY + 160, (int) buttonWidth, (ChatFormatting.GRAY + "Final " + ChatFormatting.WHITE + "EXP Up, Cost: " + ChatFormatting.GREEN + "5000"), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("finalUp");
//                }));
//            } else if (playerData.getDriveFormLevel(Strings.Form_Final) < 7 && playerData.getHearts() < 5000) {
//                addRenderableWidget(finalUp = new MenuButton((int) buttonPosX, button_statsY + 160, (int) buttonWidth, (ChatFormatting.GRAY + "Final " + ChatFormatting.WHITE + "EXP Up, Cost: " + ChatFormatting.DARK_RED + "5000"), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("req");
//                }));
//            } else if ((playerData.getDriveFormLevel(Strings.Form_Final) == 7)) {
//                addRenderableWidget(req0 = new MenuButton((int) buttonPosX, button_statsY + 160, (int) buttonWidth, ChatFormatting.GOLD + "☆ Final Form MAXED ☆", MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("req");
//                }));
//            }
//        }

//        if (ModConfigs.levelsEnabled) {
//            if (playerData.getHearts() >= 1000 * playerData.getLevel() && playerData.getLevel() < 100) {
//                addRenderableWidget(lvl = new MenuButton((int) buttonPosX + 180, button_statsY, (int) buttonWidth, ("Level Up - Cost: " + ChatFormatting.GREEN + 1000 * playerData.getLevel()), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("lvl");
//                }));
//            } else if (playerData.getHearts() < 1000 * playerData.getLevel() && playerData.getLevel() < 100) {
//                addRenderableWidget(req0 = new MenuButton((int) buttonPosX + 180, button_statsY, (int) buttonWidth, "Level Up - Cost: " + ChatFormatting.DARK_RED + 1000 * playerData.getLevel(), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("req");
//                }));
//            } else if (playerData.getLevel() == 100) {
//                addRenderableWidget(req0 = new MenuButton((int) buttonPosX + 180, button_statsY, (int) buttonWidth, ChatFormatting.GOLD + "☆ MAX LEVEL! ☆", MenuButton.ButtonType.BUTTON, false, (e) -> {
//                    action("req");
//                }));
//            }
//        }

        // Add Armor/Accessory Slots
//        if (playerData.getHearts() >= 10000 && playerData.getMaxArmors() < 4) {
//            addRenderableWidget(lvl = new MenuButton((int) buttonPosX + 180, button_statsY + 20, (int) buttonWidth + 10, ("Armor Slot Up - Cost: "+ ChatFormatting.GREEN + 10000 * playerData.getLevel()), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                action("armorUp");
//            }));
//        } else if (playerData.getHearts() < 10000){
//            addRenderableWidget(req0 = new MenuButton((int) buttonPosX + 180, button_statsY + 20, (int) buttonWidth + 10,  "Armor Slot  Up - Cost: "+ ChatFormatting.DARK_RED + 10000 * playerData.getLevel(), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                action("req");
//            }));
//        } else if (playerData.getMaxArmors() == 4) {
//            addRenderableWidget(req0 = new MenuButton((int) buttonPosX + 180, button_statsY + 20, (int) buttonWidth + 10, ChatFormatting.GOLD + "☆ MAX ARMOR SLOTS! ☆", MenuButton.ButtonType.BUTTON, false, (e) -> {
//                action("req");
//            }));
//        }
//
//        if (playerData.getHearts() >= 10000 && playerData.getMaxAccessories() < 4) {
//            addRenderableWidget(lvl = new MenuButton((int) buttonPosX + 180, button_statsY + 40, (int) buttonWidth + 30, ("Accessory Slot Up - Cost: "+ ChatFormatting.GREEN + 10000 * playerData.getLevel()), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                action("accessoryUp");
//            }));
//        } else if (playerData.getHearts() < 10000){
//            addRenderableWidget(req0 = new MenuButton((int) buttonPosX + 180, button_statsY + 40, (int) buttonWidth + 30,  "Accessory Slot  Up - Cost: "+ ChatFormatting.DARK_RED + 10000 * playerData.getLevel(), MenuButton.ButtonType.BUTTON, false, (e) -> {
//                action("req");
//            }));
//        } else if (playerData.getMaxAccessories() == 4) {
//            addRenderableWidget(req0 = new MenuButton((int) buttonPosX + 180, button_statsY + 40, (int) buttonWidth + 10, ChatFormatting.GOLD + "☆ MAX ACCESSORY SLOTS! ☆", MenuButton.ButtonType.BUTTON, false, (e) -> {
//                action("req");
//            }));
//        }

        if (playerData.getHearts() >= 10000) {
            addRenderableWidget(lvl = new MenuButton((int) buttonPosX, button_statsY + 220, (int) buttonWidth, "Leave Org - Cost: "+ ChatFormatting.GREEN + 13000, MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("rejectOrg");
            }));
        } else if (playerData.getHearts() < 10000){
            addRenderableWidget(req0 = new MenuButton((int) buttonPosX, button_statsY + 220, (int) buttonWidth,  "Leave Org - Cost: "+ ChatFormatting.DARK_RED + 13000, MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("req");
            }));
       }


        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, button_statsY + 240, (int) buttonWidth, "Reset", MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("reset");
        }));

        if (addedData.getPanelsEnabled() == 1){
            addRenderableWidget(toggleOff = new MenuButton((int) buttonPosX, button_statsY + 260, (int) buttonWidth, "Boost OFF", MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("toggleOff");
            }));
        } else if (addedData.getPanelsEnabled() == 0){
            addRenderableWidget(toggleOn = new MenuButton((int) buttonPosX, button_statsY + 260, (int) buttonWidth, "Boost ON", MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("toggleOn");
            }));
        }

        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, button_statsY + 280, (int) buttonWidth, (Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, false, (e) -> {
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

        PanelStats orgStats = addedData.getOrganizationPanelStats();

//        addRenderableWidget(new MenuColourBox(
//                col2X,
//                button_statsY + (d++ * spacer),
//                (int) dataWidth,
//                Utils.translateToLocal("Org Grid STR: "),
//                "+" + orgStats.getStrength(),
//                0xFFD700
//        ));
//
//        addRenderableWidget(new MenuColourBox(
//                col2X,
//                button_statsY + (d++ * spacer),
//                (int) dataWidth,
//                Utils.translateToLocal("Org Grid MAG: "),
//                "+" + orgStats.getMagic(),
//                0x5555FF
//        ));
//
//        addRenderableWidget(new MenuColourBox(
//                col2X,
//                button_statsY + (d++ * spacer),
//                (int) dataWidth,
//                Utils.translateToLocal("Org Grid DEF: "),
//                "+" + orgStats.getDefense(),
//                0x55FF55
//        ));
//
//        addRenderableWidget(new MenuColourBox(
//                col2X,
//                button_statsY + (d++ * spacer),
//                (int) dataWidth,
//                Utils.translateToLocal("Org Grid AP: "),
//                "+" + orgStats.getAp(),
//                0xFF55FF
//        ));
//
//        addRenderableWidget(new MenuColourBox(
//                col2X,
//                button_statsY + (d++ * spacer),
//                (int) dataWidth,
//                Utils.translateToLocal("Org Grid LV: "),
//                "+" + orgStats.getLevelBonus(),
//                0xFFFFFF
//        ));

        super.init();
    }

    private int getOrgGridMouseX(int mouseX) {
        if (minecraft == null || minecraft.player == null) {
            return -1;
        }

        GlobalDataRM addedData = ModDataRM.getGlobal(minecraft.player);

        if (addedData == null || addedData.getOrganizationPanelGrid() == null) {
            return -1;
        }

        int localX = mouseX - orgGridX;

        if (localX < 0) {
            return -1;
        }

        int gridX = localX / ORG_SLOT_SIZE;

        if (gridX < 0 || gridX >= addedData.getOrganizationPanelGrid().getWidth()) {
            return -1;
        }

        return gridX;
    }

    private int getOrgGridMouseY(int mouseY) {
        if (minecraft == null || minecraft.player == null) {
            return -1;
        }

        GlobalDataRM addedData = ModDataRM.getGlobal(minecraft.player);

        if (addedData == null || addedData.getOrganizationPanelGrid() == null) {
            return -1;
        }

        int localY = mouseY - orgGridY;

        if (localY < 0) {
            return -1;
        }

        int gridY = localY / ORG_SLOT_SIZE;

        if (gridY < 0 || gridY >= addedData.getOrganizationPanelGrid().getHeight()) {
            return -1;
        }

        return gridY;
    }

    private int getPanelColor(PanelData data) {
        return switch (data.getType()) {
            case LEVEL -> 0xAAFFFFFF;
            case STRENGTH -> 0xAAFF5555;
            case MAGIC -> 0xAA5555FF;
            case DEFENSE -> 0xAA55FF55;
            case AP -> 0xAAFF55FF;
            case ABILITY -> 0xAAAAAAFF;
            case SPELL -> 0xAA55FFFF;
            case WEAPON -> 0xAAFFAA55;
            case GEAR -> 0xAAAA8855;
            case LINK -> 0xAAFFFF55;
        };
    }


    private String getPanelShortName(String path) {
        return switch (path) {
            case "level_up" -> "LV";
            case "strength_unit" -> "STR";
            case "magic_unit" -> "MAG";
            case "defense_unit" -> "DEF";
            case "ap_unit" -> "AP";
            case "strength_unit_l" -> "S+";
            case "magic_unit_l" -> "M+";
            case "defense_unit_l" -> "D+";
            case "ap_unit_l" -> "AP+";
            case "level_doubler" -> "LV2";
            case "power_link" -> "P-L";
            case "magic_link" -> "M-L";
            case "guard_link" -> "G-L";
            case "level_link" -> "L-L";
            default -> path.length() > 3 ? path.substring(0, 3).toUpperCase() : path.toUpperCase();
        };
    }

    private void renderOrganizationPanelGrid(GuiGraphics gui, int mouseX, int mouseY) {
        if (minecraft == null || minecraft.player == null) {
            return;
        }



        GlobalDataRM addedData = ModDataRM.getGlobal(minecraft.player);

        if (addedData == null) {
            return;
        }

        PanelGrid grid = addedData.getOrganizationPanelGrid();

        // Title
        gui.drawString(
                this.font,
                "Organization Panels",
                orgGridX,
                orgGridY - 14,
                0xFFFFFF,
                false
        );

        gui.drawString(
                this.font,
                "Selected: " + getPanelShortName(selectedOrgPanel.getPath()),
                orgGridX,
                orgGridY - 26,
                0xFFD700,
                false
        );

        // Draw empty grid slots
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                int sx = orgGridX + x * ORG_SLOT_SIZE;
                int sy = orgGridY + y * ORG_SLOT_SIZE;

                boolean hovered = mouseX >= sx
                        && mouseX < sx + ORG_SLOT_SIZE
                        && mouseY >= sy
                        && mouseY < sy + ORG_SLOT_SIZE;

                int borderColor = hovered ? 0xFFFFFF00 : 0xFF555555;
                int fillColor = hovered ? 0x55222222 : 0xAA111111;

                gui.fill(sx, sy, sx + ORG_SLOT_SIZE, sy + ORG_SLOT_SIZE, fillColor);

                // border
                gui.fill(sx, sy, sx + ORG_SLOT_SIZE, sy + 1, borderColor);
                gui.fill(sx, sy + ORG_SLOT_SIZE - 1, sx + ORG_SLOT_SIZE, sy + ORG_SLOT_SIZE, borderColor);
                gui.fill(sx, sy, sx + 1, sy + ORG_SLOT_SIZE, borderColor);
                gui.fill(sx + ORG_SLOT_SIZE - 1, sy, sx + ORG_SLOT_SIZE, sy + ORG_SLOT_SIZE, borderColor);
            }
        }

        // Draw placed panels
        for (PanelSlot slot : grid.getPlacedPanels()) {
            PanelData data = PanelRegistry.get(slot.getPanelId());

            if (data == null) {
                continue;
            }

            int px = orgGridX + slot.getX() * ORG_SLOT_SIZE;
            int py = orgGridY + slot.getY() * ORG_SLOT_SIZE;
            int pw = data.getWidth() * ORG_SLOT_SIZE;
            int ph = data.getHeight() * ORG_SLOT_SIZE;

            int color = getPanelColor(data);

            gui.fill(px + 2, py + 2, px + pw - 2, py + ph - 2, color);

            String label = getPanelShortName(slot.getPanelId().getPath());

            int labelColor = data.getType() == PanelType.LINK ? 0x000000 : 0xFFFFFF;

            gui.drawString(
                    this.font,
                    label,
                    px + 4,
                    py + 7,
                    labelColor,
                    false
            );
        }

        // Hover tooltip
        int gridCellX = getOrgGridMouseX(mouseX);
        int gridCellY = getOrgGridMouseY(mouseY);

        if (gridCellX >= 0 && gridCellY >= 0) {
            PanelSlot hoveredSlot = grid.getAt(gridCellX, gridCellY);

            if (hoveredSlot != null) {
                PanelData data = PanelRegistry.get(hoveredSlot.getPanelId());

                if (data != null) {
                    gui.renderTooltip(
                            this.font,
                            Component.literal(
                                    getPanelDisplayName(hoveredSlot.getPanelId().getPath())
                                            + " [" + data.getWidth() + "x" + data.getHeight() + "]"
                                            + " - " + getPanelDescription(data)
                            ),
                            mouseX,
                            mouseY
                    );
                }
            }
        }
    }

    private void renderOrganizationPanelKeyGuide(GuiGraphics gui) {
        int gridCols = 5;

        if (minecraft != null && minecraft.player != null) {
            GlobalDataRM addedData = ModDataRM.getGlobal(minecraft.player);

            if (addedData != null && addedData.getOrganizationPanelGrid() != null) {
                gridCols = addedData.getOrganizationPanelGrid().getWidth();
            }
        }

        int x = orgGridX + (gridCols * ORG_SLOT_SIZE) + 18;
        int y = orgGridY;

        gui.drawString(this.font, "Panel Keys", x, y, 0xFFD700, false);
        y += 14;

        gui.drawString(this.font, "1 - STR Unit", x, y, 0xFF5555, false);
        y += 11;

        gui.drawString(this.font, "2 - MAG Unit", x, y, 0x5555FF, false);
        y += 11;

        gui.drawString(this.font, "3 - DEF Unit", x, y, 0x55FF55, false);
        y += 11;

        gui.drawString(this.font, "4 - AP Unit", x, y, 0xFF55FF, false);
        y += 11;

        gui.drawString(this.font, "5 - Level Up", x, y, 0xFFFFFF, false);
        y += 16;

        gui.drawString(this.font, "Left Click: Place", x, y, 0xAAAAAA, false);
        y += 11;

        gui.drawString(this.font, "Right Click: Remove", x, y, 0xAAAAAA, false);
    }

    private String getPanelDescription(PanelData data) {
        String path = data.getId().getPath();

        return switch (path) {
            case "strength_unit" -> "+1 STR";
            case "magic_unit" -> "+1 MAG";
            case "defense_unit" -> "+1 DEF";
            case "ap_unit" -> "+2 AP";
            case "level_up" -> "+1 LV";

            case "strength_unit_l" -> "+3 STR";
            case "magic_unit_l" -> "+3 MAG";
            case "defense_unit_l" -> "+3 DEF";
            case "ap_unit_l" -> "+5 AP";
            case "level_doubler" -> "+2 LV";

            case "power_link" -> "+1 STR for each adjacent STR panel";
            case "magic_link" -> "+1 MAG for each adjacent MAG panel";
            case "guard_link" -> "+1 DEF for each adjacent DEF panel";
            case "level_link" -> "+1 LV for each adjacent LV panel";

            default -> "";
        };
    }

    private void renderOrganizationPanelStats(GuiGraphics gui) {
        if (minecraft == null || minecraft.player == null) {
            return;
        }

        GlobalDataRM addedData = ModDataRM.getGlobal(minecraft.player);

        if (addedData == null) {
            return;
        }

        PanelStats stats = addedData.getOrganizationPanelStats();
        PanelGrid grid = addedData.getOrganizationPanelGrid();

        int gridRows = grid != null ? grid.getHeight() : 4;

        int x = orgGridX;
        int y = orgGridY + (gridRows * ORG_SLOT_SIZE) + 12;

        gui.drawString(this.font, "Grid Bonuses", x, y, 0xFFD700, false);
        y += 12;

        gui.drawString(this.font, "STR +" + stats.getStrength(), x, y, 0xFF5555, false);
        y += 10;

        gui.drawString(this.font, "MAG +" + stats.getMagic(), x, y, 0x5555FF, false);
        y += 10;

        gui.drawString(this.font, "DEF +" + stats.getDefense(), x, y, 0x55FF55, false);
        y += 10;

        gui.drawString(this.font, "AP +" + stats.getAp(), x, y, 0xFF55FF, false);
        y += 10;

        PlayerData playerData = PlayerData.get(minecraft.player);

        int realLevel = playerData != null ? playerData.getLevel() : 0;
        int effectiveLevel = realLevel + stats.getLevelBonus();

        gui.drawString(
                this.font,
                "LV +" + stats.getLevelBonus() + "  (" + realLevel + " > " + effectiveLevel + ")",
                x,
                y,
                0xFFFFFF,
                false
        );
    }

    private void renderOrganizationPanelPicker(GuiGraphics gui, int mouseX, int mouseY) {
        gui.drawString(
                this.font,
                "Panel Inventory",
                orgPickerX,
                orgPickerY - 14,
                0xFFD700,
                false
        );

        for (int i = 0; i < ORG_PICKER_PANELS.length; i++) {
            renderPanelPickerSlot(gui, mouseX, mouseY, i, ORG_PICKER_PANELS[i]);
        }
    }

    private void renderPanelPickerSlot(GuiGraphics gui, int mouseX, int mouseY, int index, ResourceLocation panelId) {
        PanelData data = PanelRegistry.get(panelId);

        if (data == null) {
            return;
        }

        GlobalDataRM addedData = null;

        if (minecraft != null && minecraft.player != null) {
            addedData = ModDataRM.getGlobal(minecraft.player);
        }

        int count = addedData != null ? addedData.getOwnedOrganizationPanelCount(panelId) : 0;

        int x = orgPickerX + index * (ORG_PANEL_PICKER_SLOT_SIZE + ORG_PANEL_PICKER_GAP);
        int y = orgPickerY;

        boolean hovered = mouseX >= x
                && mouseX < x + ORG_PANEL_PICKER_SLOT_SIZE
                && mouseY >= y
                && mouseY < y + ORG_PANEL_PICKER_SLOT_SIZE;

        boolean selected = selectedOrgPanel.equals(panelId);

        int borderColor = selected ? 0xFFFFFF00 : hovered ? 0xFFFFFFFF : 0xFF555555;
        int fillColor = count > 0 ? getPanelColor(data) : 0xAA333333;

        gui.fill(x, y, x + ORG_PANEL_PICKER_SLOT_SIZE, y + ORG_PANEL_PICKER_SLOT_SIZE, 0xAA111111);
        gui.fill(x + 2, y + 2, x + ORG_PANEL_PICKER_SLOT_SIZE - 2, y + ORG_PANEL_PICKER_SLOT_SIZE - 2, fillColor);

        // Border
        gui.fill(x, y, x + ORG_PANEL_PICKER_SLOT_SIZE, y + 1, borderColor);
        gui.fill(x, y + ORG_PANEL_PICKER_SLOT_SIZE - 1, x + ORG_PANEL_PICKER_SLOT_SIZE, y + ORG_PANEL_PICKER_SLOT_SIZE, borderColor);
        gui.fill(x, y, x + 1, y + ORG_PANEL_PICKER_SLOT_SIZE, borderColor);
        gui.fill(x + ORG_PANEL_PICKER_SLOT_SIZE - 1, y, x + ORG_PANEL_PICKER_SLOT_SIZE, y + ORG_PANEL_PICKER_SLOT_SIZE, borderColor);

        String label = getPanelShortName(panelId.getPath());

        gui.drawString(
                this.font,
                label,
                x + 3,
                y + 8,
                0xFFFFFF,
                false
        );

        gui.drawString(
                this.font,
                "x" + count,
                x + 4,
                y + ORG_PANEL_PICKER_SLOT_SIZE + 2,
                count > 0 ? 0xFFFFFF : 0xFF5555,
                false
        );

        if (hovered) {
            gui.renderTooltip(
                    this.font,
                    Component.literal(
                            getPanelDisplayName(panelId.getPath())
                                    + " [" + data.getWidth() + "x" + data.getHeight() + "]"
                                    + " - " + getPanelDescription(data)
                    ),
                    mouseX,
                    mouseY
            );
        }
    }

    private String getPanelDisplayName(String path) {
        return switch (path) {
            case "level_up" -> "Level Up";
            case "strength_unit" -> "Strength Unit";
            case "magic_unit" -> "Magic Unit";
            case "defense_unit" -> "Defense Unit";
            case "ap_unit" -> "AP Unit";
            case "strength_unit_l" -> "Strength Unit L";
            case "magic_unit_l" -> "Magic Unit L";
            case "defense_unit_l" -> "Defense Unit L";
            case "ap_unit_l" -> "AP Unit L";
            case "level_doubler" -> "Level Doubler";
            case "power_link" -> "Power Link";
            case "magic_link" -> "Magic Link";
            case "guard_link" -> "Guard Link";
            case "level_link" -> "Level Link";
            default -> path;
        };
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        super.render(gui, mouseX, mouseY, partialTicks);

        renderOrganizationPanelPicker(gui, mouseX, mouseY);
        renderOrganizationPanelGrid(gui, mouseX, mouseY);
        renderOrganizationPanelStats(gui);
        renderOrganizationPanelKeyGuide(gui);
    }
}
