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
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.network.cts.CSSyncAllClientDataPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.network.cts.CSBoostPacket;
import online.remind.remind.network.cts.CSBuyOrganizationPanelPacket;
import online.remind.remind.network.cts.CSPanelPacket;
import net.neoforged.neoforge.network.PacketDistributor;
import online.remind.remind.network.cts.CSOrganizationPanelPacket;
import online.remind.remind.network.PanelPacketAction;
import online.remind.remind.panels.*;


import net.minecraft.network.chat.Component;

import java.awt.*;

public class PanelsMenu extends MenuBackground {


    private static final int BASE_ORG_SLOT_SIZE = 18;

    private static final int BASE_ORG_PANEL_PICKER_SLOT_SIZE = 22;
    private static final int BASE_ORG_PANEL_PICKER_GAP = 4;
    private static final int BASE_ORG_PANEL_PICKER_ROW_GAP = 5;

    private static final int BASE_SHOP_VISIBLE_BUTTONS = 5;
    private static final int BASE_SHOP_BUTTON_GAP = 16;

    private static final int ORG_PANEL_PICKER_COLUMNS = 7;

    private int orgSlotSize = BASE_ORG_SLOT_SIZE;
    private int orgPickerSlotSize = BASE_ORG_PANEL_PICKER_SLOT_SIZE;
    private int orgPickerGap = BASE_ORG_PANEL_PICKER_GAP;
    private int orgPickerRowGap = BASE_ORG_PANEL_PICKER_ROW_GAP;
    private int shopVisibleButtons = BASE_SHOP_VISIBLE_BUTTONS;
    private int shopButtonGap = BASE_SHOP_BUTTON_GAP;
    private int orgPickerColumns = ORG_PANEL_PICKER_COLUMNS;

    private boolean compactPanelLayout = false;
    private boolean emergencyPanelLayout = false;

    private int shopScrollOffset = 0;

    private int orgPickerX;
    private int orgPickerY;

    private int orgControlsX;
    private int orgControlsY;

    private int orgPanelAreaX;
    private int orgPanelAreaY;
    private int orgPanelAreaWidth;
    private int orgPanelAreaHeight;

    // ------------- Pretty Stuff ----------------------
    MenuBox box;
    MenuBox shopBox;
    MenuBox editorBox;
    MenuBox detailBox;

    private int shopBoxX;
    private int shopBoxY;
    private int shopBoxW;
    private int shopBoxH;

    private int editorBoxX;
    private int editorBoxY;
    private int editorBoxW;
    private int editorBoxH;

    private int detailBoxX;
    private int detailBoxY;
    private int detailBoxW;
    private int detailBoxH;

    private MenuScrollBar orgInventoryScrollBar;

    private static final int ORG_INVENTORY_ROW_HEIGHT = 16;

    private int orgInventoryListX;
    private int orgInventoryListY;
    private int orgInventoryListW;
    private int orgInventoryListH;

    private MenuScrollBar shopScrollBar;

    private static final int SHOP_ROW_HEIGHT = 16;

    private int shopListX;
    private int shopListY;
    private int shopListW;
    private int shopListH;





    private ResourceLocation selectedOrgPanel = PanelRegistry.STRENGTH_UNIT; // DUMMY


    private int orgGridX;
    private int orgGridY;
    private int shopInfoBoxY;
    private int shopPanelX;
    private int shopPanelWidth;

    int ticks = 0;

    private int lastKnownHearts = -1;

    private int getCurrentHearts() {
        if (minecraft == null || minecraft.player == null) {
            return -1;
        }

        PlayerData playerData = PlayerData.get(minecraft.player);

        if (playerData == null) {
            return -1;
        }

        return playerData.getHearts();
    }

    private MenuButton backButton, strUp, magUp, defUp, apUp, giveAbility, lvl, req0, valorUp, wisdomUp, limitUp, masterUp, finalUp, reqV, reqW, reqL, reqM, reqF, armorUp, accessoryUp, rejectOrg, reset, toggleOff, toggleOn, orgPlaceSTR, orgPlaceMAG, orgPlaceDEF, orgPlaceAP, orgPlaceLV, orgRemove00, orgClear;
    MenuColourBox str, mag, def, ap;

    MenuColourBox[] playerWidgets = {str, mag, def, ap};



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
            case "buy_str_unit" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.STRENGTH_UNIT,
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_mag_unit" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.MAGIC_UNIT,
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_def_unit" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.DEFENSE_UNIT,
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_ap_unit" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.AP_UNIT,
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_level_up" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.LEVEL_UP,
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_slot_releaser" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "slot_releaser"),
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }
            case "buy_str_unit_l" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.STRENGTH_UNIT_L,
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_mag_unit_l" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.MAGIC_UNIT_L,
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_def_unit_l" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.DEFENSE_UNIT_L,
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_ap_unit_l" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.AP_UNIT_L,
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_level_doubler" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.LEVEL_DOUBLER,
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_power_link" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.POWER_LINK,
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_magic_link" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.MAGIC_LINK,
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_guard_link" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.GUARD_LINK,
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_level_link" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.LEVEL_LINK,
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            // Abilities

            case "buy_ultima_weapon_panel" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.ULTIMA_WEAPON_PANEL,
                        1
                ));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_high_jump_panel" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(PanelRegistry.HIGH_JUMP_PANEL, 1));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_dodge_roll_panel" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(PanelRegistry.DODGE_ROLL_PANEL, 1));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_aerial_dodge_panel" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(PanelRegistry.AERIAL_DODGE_PANEL, 1));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_quick_run_panel" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(PanelRegistry.QUICK_RUN_PANEL, 1));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_glide_panel" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(PanelRegistry.GLIDE_PANEL, 1));
                PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            // Forms

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
                minecraft.player.playSound(ModSounds.menu_select.get());
                this.reloadMenu();
            }

            case "orgPlaceMAG" -> {
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.PLACE,
                        PanelRegistry.MAGIC_UNIT,
                        1,
                        0
                ));
                minecraft.player.playSound(ModSounds.menu_select.get());
                this.reloadMenu();
            }

            case "orgPlaceDEF" -> {
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.PLACE,
                        PanelRegistry.DEFENSE_UNIT,
                        2,
                        0
                ));
                minecraft.player.playSound(ModSounds.menu_select.get());
                this.reloadMenu();
            }

            case "orgPlaceAP" -> {
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.PLACE,
                        PanelRegistry.AP_UNIT,
                        3,
                        0
                ));
                minecraft.player.playSound(ModSounds.menu_select.get());
                this.reloadMenu();
            }

            case "orgPlaceLV" -> {
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.PLACE,
                        PanelRegistry.LEVEL_UP,
                        4,
                        0
                ));
                minecraft.player.playSound(ModSounds.menu_select.get());
                this.reloadMenu();
            }

            case "orgRemove00" -> {
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.REMOVE,
                        PanelRegistry.STRENGTH_UNIT,
                        0,
                        0
                ));
                minecraft.player.playSound(ModSounds.menu_back.get());
                this.reloadMenu();
            }

            case "orgClear" -> {
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.CLEAR,
                        PanelRegistry.STRENGTH_UNIT,
                        0,
                        0
                ));
                minecraft.player.playSound(ModSounds.menu_back.get());
                this.reloadMenu();
            }

            case "buy_selected_panel" -> {
                if (selectedOrgPanel != null) {
                    PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                            selectedOrgPanel,
                            1
                    ));
                    PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                    minecraft.player.playSound(ModSounds.itemget.get());
                }
            }

        }

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (shopScrollBar != null) {
            boolean clickedShopScrollbar = mouseX >= shopScrollBar.getX()
                    && mouseX <= shopScrollBar.getX() + shopScrollBar.getWidth()
                    && mouseY >= shopScrollBar.getY()
                    && mouseY <= shopScrollBar.getBottom();

            if (clickedShopScrollbar) {
                shopScrollBar.mouseClicked(mouseX, mouseY, button);
                return true;
            }
        }

        PanelShopEntry clickedShopEntry = getClickedShopEntry((int) mouseX, (int) mouseY);

        if (clickedShopEntry != null && button == 0) {
            selectedOrgPanel = clickedShopEntry.panelId();

            if (minecraft != null && minecraft.player != null) {
                minecraft.player.playSound(ModSounds.menu_select.get());
            }

            return true;
        }

        if (orgInventoryScrollBar != null) {
            boolean clickedScrollbar = mouseX >= orgInventoryScrollBar.getX()
                    && mouseX <= orgInventoryScrollBar.getX() + orgInventoryScrollBar.getWidth()
                    && mouseY >= orgInventoryScrollBar.getY()
                    && mouseY <= orgInventoryScrollBar.getBottom();

            if (clickedScrollbar) {
                orgInventoryScrollBar.mouseClicked(mouseX, mouseY, button);
                return true;
            }
        }

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
                minecraft.player.playSound(ModSounds.menu_select.get());
            }

            return true;
        }

        int gridX = getOrgGridMouseX((int) mouseX);
        int gridY = getOrgGridMouseY((int) mouseY);

        if (gridX >= 0 && gridY >= 0) {
            GlobalDataRM addedData = ModDataRM.getGlobal(minecraft.player);

            if (button == 0) {
                if (addedData != null && !addedData.isOrganizationPanelSlotUnlocked(gridX, gridY)) {
                    if (minecraft != null && minecraft.player != null) {
                        minecraft.player.playSound(ModSounds.error.get());
                    }

                    return true;
                }

                // Left click = place selected panel
                PacketDistributor.sendToServer(new CSOrganizationPanelPacket(
                        PanelPacketAction.PLACE,
                        selectedOrgPanel,
                        gridX,
                        gridY
                ));

                if (minecraft != null && minecraft.player != null) {
                    minecraft.player.playSound(ModSounds.menu_select.get());
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
                    minecraft.player.playSound(ModSounds.menu_back.get());
                }

                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (shopScrollBar != null) {
            shopScrollBar.mouseReleased(mouseX, mouseY, button);
        }
        if (orgInventoryScrollBar != null) {
            orgInventoryScrollBar.mouseReleased(mouseX, mouseY, button);
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (shopScrollBar != null) {
            shopScrollBar.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
        if (orgInventoryScrollBar != null) {
            orgInventoryScrollBar.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (shopScrollBar != null) {
            boolean insideShop = mouseX >= shopListX
                    && mouseX <= shopListX + shopListW
                    && mouseY >= shopListY
                    && mouseY <= shopListY + shopListH;

            if (insideShop) {
                shopScrollBar.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
                return true;
            }
        }

        if (orgInventoryScrollBar != null) {
            boolean insideInventory = mouseX >= orgInventoryListX
                    && mouseX <= orgInventoryListX + orgInventoryListW
                    && mouseY >= orgInventoryListY
                    && mouseY <= orgInventoryListY + orgInventoryListH;

            if (insideInventory) {
                orgInventoryScrollBar.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
                return true;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
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
            PanelRegistry.LEVEL_LINK,

            PanelRegistry.ULTIMA_WEAPON_PANEL,

            PanelRegistry.HIGH_JUMP_PANEL,
            PanelRegistry.DODGE_ROLL_PANEL,
            PanelRegistry.AERIAL_DODGE_PANEL,
            PanelRegistry.QUICK_RUN_PANEL,
            PanelRegistry.GLIDE_PANEL
    };

    private ResourceLocation getClickedPanelPicker(int mouseX, int mouseY) {
        if (orgInventoryScrollBar == null) {
            return null;
        }

        int scrollBarX = orgInventoryScrollBar.getX();

        boolean insideList = mouseX >= orgInventoryListX
                && mouseX < scrollBarX
                && mouseY >= orgInventoryListY
                && mouseY < orgInventoryListY + orgInventoryListH;

        if (!insideList) {
            return null;
        }

        int localY = mouseY - orgInventoryListY + (int) orgInventoryScrollBar.scrollOffset;
        int index = localY / ORG_INVENTORY_ROW_HEIGHT;

        if (index < 0 || index >= ORG_PICKER_PANELS.length) {
            return null;
        }

        return ORG_PICKER_PANELS[index];
    }


    @Override
    public void tick() {
        super.tick();
        ticks++;

        int currentHearts = getCurrentHearts();

        if (currentHearts != -1 && currentHearts != lastKnownHearts) {
            init();
        }
    }

    @Override
    public void init() {
        updateAdaptivePanelLayout();

        Player player;
        final PlayerData playerData = PlayerData.get(minecraft.player);
        GlobalDataRM addedData = ModDataRM.getGlobal(minecraft.player);
        ticks = 0;

        lastKnownHearts = playerData != null ? playerData.getHearts() : -1;

        this.clearWidgets();

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
        int margin = emergencyPanelLayout ? 8 : compactPanelLayout ? 10 : 14;
        int gap = emergencyPanelLayout ? 6 : compactPanelLayout ? 8 : 10;

        int topY = button_statsY;
        int usableBottom = height - 64;

        /*
         * Wallet-style panel boxes.
         * These are intentionally smaller than full screen height
         * so the menu doesn't feel like a giant overlay.
         */
        this.shopBoxX = (int) buttonPosX;
        this.shopBoxY = topY;
        this.shopBoxW = Math.max(125, (int) (width * (emergencyPanelLayout ? 0.20F : compactPanelLayout ? 0.19F : 0.18F)));
        this.shopBoxH = emergencyPanelLayout ? 150 : compactPanelLayout ? 175 : 195;

        this.detailBoxX = shopBoxX;
        this.detailBoxY = shopBoxY + shopBoxH + gap;
        this.detailBoxW = shopBoxW;
        this.detailBoxH = emergencyPanelLayout ? 95 : compactPanelLayout ? 110 : 125;

        this.editorBoxX = shopBoxX + shopBoxW + gap;
        this.editorBoxY = topY;
        this.editorBoxW = width - editorBoxX - margin;

        /*
         * Make editor box wrap around the actual editor content instead of filling
         * almost the entire screen height.
         */
        this.editorBoxH = emergencyPanelLayout ? 245 : compactPanelLayout ? 290 : 335;

        if (editorBoxY + editorBoxH > usableBottom) {
            editorBoxH = usableBottom - editorBoxY;
        }

        this.shopBox = new MenuBox(shopBoxX, shopBoxY, shopBoxW, shopBoxH, 1F, new Color(92, 92, 151));
        this.detailBox = new MenuBox(detailBoxX, detailBoxY, detailBoxW, detailBoxH, 1F, new Color(255, 255, 255));
        this.editorBox = new MenuBox(editorBoxX, editorBoxY, editorBoxW, editorBoxH, 1F, new Color(155, 155, 155));

        int contentX = editorBoxX + 14;
        int contentY = editorBoxY + 20;
        int contentWidth = editorBoxW - 28;

        int orgGridCols = 5;

        if (addedData != null && addedData.getOrganizationPanelGrid() != null) {
            orgGridCols = addedData.getOrganizationPanelGrid().getWidth();
        }

        int gridWidth = orgGridCols * orgSlotSize;

        int pickerColumns = Math.min(ORG_PICKER_PANELS.length, orgPickerColumns);
        int pickerRows = (int) Math.ceil((double) ORG_PICKER_PANELS.length / orgPickerColumns);

        int pickerWidth = (pickerColumns * orgPickerSlotSize)
                + ((pickerColumns - 1) * orgPickerGap);

        int pickerHeight = (pickerRows * orgPickerSlotSize)
                + ((pickerRows - 1) * orgPickerRowGap);

        // --- Panel Inventory + Grid layout ---
        this.orgInventoryListX = editorBoxX + 16;
        this.orgInventoryListY = editorBoxY + 28;
        this.orgInventoryListW = emergencyPanelLayout ? 112 : compactPanelLayout ? 126 : 145;
        this.orgInventoryListH = editorBoxH - 48;

        if (this.orgInventoryListH < 90) {
            this.orgInventoryListH = 90;
        }

        // Keep old picker coords synced so old checks do not drift.
        this.orgPickerX = orgInventoryListX;
        this.orgPickerY = orgInventoryListY;

        // Scrollbar for the Panel Inventory list.
        this.orgInventoryScrollBar = new MenuScrollBar(
                orgInventoryListX + orgInventoryListW - 12,
                orgInventoryListY,
                orgInventoryListY + orgInventoryListH,
                orgInventoryListH,
                0,
                false
        );

        this.orgInventoryScrollBar.setContentHeight(
                ORG_PICKER_PANELS.length * ORG_INVENTORY_ROW_HEIGHT + 4
        );

        addRenderableWidget(this.orgInventoryScrollBar);

        // Grid sits to the right of the inventory list.
        this.orgGridX = orgInventoryListX + orgInventoryListW + 20;
        this.orgGridY = editorBoxY + 56;

        int maxGridX = editorBoxX + editorBoxW - gridWidth - 18;

        if (this.orgGridX > maxGridX) {
            this.orgGridX = maxGridX;
        }

        if (this.orgGridX < editorBoxX + 16) {
            this.orgGridX = editorBoxX + 16;
        }

        int i = 0;

        this.shopListX = shopBoxX + 8;
        this.shopListY = shopBoxY + 22;
        this.shopListW = shopBoxW - 16;
        this.shopListH = shopBoxH - 32;

        this.shopPanelX = detailBoxX + 8;
        this.shopPanelWidth = detailBoxW - 16;

        this.shopScrollBar = new MenuScrollBar(
                shopListX + shopListW - 12,
                shopListY,
                shopListY + shopListH,
                shopListH,
                0,
                false
        );

        this.shopScrollBar.setContentHeight(
                getPanelShopEntries().length * SHOP_ROW_HEIGHT + 4
        );

        addRenderableWidget(this.shopScrollBar);




        this.shopInfoBoxY = detailBoxY + 15;

        gridWidth = addedData != null ? addedData.getOrganizationPanelGridWidth() * orgSlotSize : gridWidth;

        int controlButtonWidth = emergencyPanelLayout ? 110 : compactPanelLayout ? 125 : 145;
        int controlButtonGap = emergencyPanelLayout ? 17 : compactPanelLayout ? 18 : 20;

        int controlButtonX = orgGridX + gridWidth + 16;
        int controlButtonY = orgGridY + 18; // move controls upward

        if (controlButtonX + controlButtonWidth > editorBoxX + editorBoxW - 12) {
            controlButtonX = editorBoxX + editorBoxW - controlButtonWidth - 14;
        }

        if (controlButtonX < orgGridX + gridWidth + 8) {
            controlButtonX = orgGridX;
            controlButtonY = orgGridY + ((addedData != null ? addedData.getOrganizationPanelGridHeight() : 8) * orgSlotSize) + 24;
        }

        addRenderableWidget(new MenuButton(
                controlButtonX,
                controlButtonY,
                controlButtonWidth,
                "Buy Selected",
                MenuButton.ButtonType.BUTTON,
                false,
                e -> action("buy_selected_panel")
        ));

        controlButtonY += controlButtonGap;

        addRenderableWidget(new MenuButton(
                controlButtonX,
                controlButtonY,
                controlButtonWidth,
                "Buy Slot Releaser ",
                MenuButton.ButtonType.BUTTON,
                false,
                e -> action("buy_slot_releaser")
        ));

        controlButtonY += controlButtonGap;

        if (addedData.getPanelsEnabled() == 1) {
            addRenderableWidget(toggleOff = new MenuButton(
                    controlButtonX,
                    controlButtonY,
                    controlButtonWidth,
                    "Boost OFF",
                    MenuButton.ButtonType.BUTTON,
                    false,
                    e -> action("toggleOff")
            ));
        } else {
            addRenderableWidget(toggleOn = new MenuButton(
                    controlButtonX,
                    controlButtonY,
                    controlButtonWidth,
                    "Boost ON",
                    MenuButton.ButtonType.BUTTON,
                    false,
                    e -> action("toggleOn")
            ));
        }

        controlButtonY += controlButtonGap;

        addRenderableWidget(backButton = new MenuButton(
                controlButtonX,
                controlButtonY,
                controlButtonWidth,
                Strings.Gui_Menu_Back,
                MenuButton.ButtonType.BUTTON,
                false,
                e -> action("back")
        ));


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



        // 2.0 Ability Planning.


        //Stats
        int c = 0;
        int d = 0;
        int spacer = 14;

        // Stats Column
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

    private void addPanelShopSelectButton(
            int x,
            int y,
            int width,
            PanelShopEntry entry
    ) {
        boolean selected = entry.panelId().equals(selectedOrgPanel);

        addRenderableWidget(new MenuButton(
                x,
                y,
                width,
                selected ? "> " + entry.label() : entry.label(),
                MenuButton.ButtonType.BUTTON,
                false,
                e -> {
                    selectedOrgPanel = entry.panelId();
                    init();
                }
        ));
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

        int gridX = localX / orgSlotSize;

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

        int gridY = localY / orgSlotSize;

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
            case "strength_unit" -> "S";
            case "magic_unit" -> "M";
            case "defense_unit" -> "D";
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
            case "ultima_weapon_panel" -> "UW";
            case "high_jump_panel" -> "HJ";
            case "dodge_roll_panel" -> "DR";
            case "aerial_dodge_panel" -> "AD";
            case "quick_run_panel" -> "QR";
            case "glide_panel" -> "GL";
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

        gui.drawString(
                this.font,
                "Selected: " + getPanelShortName(selectedOrgPanel.getPath()),
                orgGridX,
                orgGridY - 24,
                0xFFD700,
                false
        );

        gui.drawString(
                this.font,
                "Organization Panels",
                orgGridX,
                orgGridY - 12,
                0xFFFFFF,
                false
        );

        // Draw empty grid slots
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                int sx = orgGridX + x * orgSlotSize;
                int sy = orgGridY + y * orgSlotSize;

                boolean hovered = mouseX >= sx
                        && mouseX < sx + orgSlotSize
                        && mouseY >= sy
                        && mouseY < sy + orgSlotSize;

                boolean unlocked = addedData.isOrganizationPanelSlotUnlocked(x, y);

                int borderColor = unlocked
                        ? hovered ? 0xFFFFFF00 : 0xFF555555
                        : 0xFF222222;

                int fillColor = unlocked
                        ? hovered ? 0x55222222 : 0xAA111111
                        : 0xAA050505;

                gui.fill(sx, sy, sx + orgSlotSize, sy + orgSlotSize, fillColor);

                // border
                gui.fill(sx, sy, sx + orgSlotSize, sy + 1, borderColor);
                gui.fill(sx, sy + orgSlotSize - 1, sx + orgSlotSize, sy + orgSlotSize, borderColor);
                gui.fill(sx, sy, sx + 1, sy + orgSlotSize, borderColor);
                gui.fill(sx + orgSlotSize - 1, sy, sx + orgSlotSize, sy + orgSlotSize, borderColor);

                if (!unlocked) {
                    gui.drawString(
                            this.font,
                            "×",
                            sx + (orgSlotSize / 2) - 2,
                            sy + (orgSlotSize / 2) - 4,
                            0xFF252525,
                            false
                    );
                }
            }
        }


        // Draw placed panels
        for (PanelSlot slot : grid.getPlacedPanels()) {
            PanelData data = PanelRegistry.get(slot.getPanelId());

            if (data == null) {
                continue;
            }

            int px = orgGridX + slot.getX() * orgSlotSize;
            int py = orgGridY + slot.getY() * orgSlotSize;
            int pw = data.getWidth() * orgSlotSize;
            int ph = data.getHeight() * orgSlotSize;

            int color = getPanelColor(data);

            gui.fill(px + 2, py + 2, px + pw - 2, py + ph - 2, color);

            String label = getPanelShortName(slot.getPanelId().getPath());

            int labelColor = data.getType() == PanelType.LINK ? 0x000000 : 0xFFFFFF;

            gui.drawString(
                    this.font,
                    label,
                    px + 2,
                    py + Math.max(3, orgSlotSize / 2 - 4),
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

        if (emergencyPanelLayout) {
            return;
        }

        int gridCols = 5;

        if (minecraft != null && minecraft.player != null) {
            GlobalDataRM addedData = ModDataRM.getGlobal(minecraft.player);

            if (addedData != null && addedData.getOrganizationPanelGrid() != null) {
                gridCols = addedData.getOrganizationPanelGrid().getWidth();
            }
        }

        int x = orgGridX + (gridCols * orgSlotSize) + (compactPanelLayout ? 10 : 18);
        int y = orgGridY;

        if (x + 130 >= this.width - 4) {
            return;
        }
        x += 10;
        y -= 22;
        gui.drawString(this.font, "Panel Controls", x, y, 0xFFD700, false);
        y += 28;

        gui.drawString(this.font, "Left Click: Place", x, y, 0xAAAAAA, false);
        y -= 11;

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

            case "ultima_weapon_panel" -> "Activates Ultima Weapon while equipped";

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

        if (emergencyPanelLayout) {
            return;
        }

        PanelStats stats = addedData.getOrganizationPanelStats();
        PanelGrid grid = addedData.getOrganizationPanelGrid();

        int gridRows = grid != null ? grid.getHeight() : 4;

        int x = orgGridX;
        int y = orgGridY + (gridRows * orgSlotSize) + 4;

        PlayerData playerData = PlayerData.get(minecraft.player);

        int realLevel = playerData != null ? playerData.getLevel() : 0;
        int effectiveLevel = realLevel + stats.getLevelBonus();

        gui.drawString(this.font, "Grid Bonuses", x, y, 0xFFD700, false);
        y += compactPanelLayout ? 10 : 12;

        if (compactPanelLayout) {
            gui.drawString(
                    this.font,
                    "STR +" + stats.getStrength()
                            + "  MAG +" + stats.getMagic()
                            + "  DEF +" + stats.getDefense(),
                    x,
                    y,
                    0xFFFFFF,
                    false
            );

            y += 10;

            gui.drawString(
                    this.font,
                    "AP +" + stats.getAp()
                            + "  LV +" + stats.getLevelBonus()
                            + " (" + realLevel + " > " + effectiveLevel + ")",
                    x,
                    y,
                    0xFFFFFF,
                    false
            );

            return;
        }

        gui.drawString(this.font, "STR +" + stats.getStrength(), x, y, 0xFF5555, false);
        y += 10;

        gui.drawString(this.font, "MAG +" + stats.getMagic(), x, y, 0x5555FF, false);
        y += 10;

        gui.drawString(this.font, "DEF +" + stats.getDefense(), x, y, 0x55FF55, false);
        y += 10;

        gui.drawString(this.font, "AP +" + stats.getAp(), x, y, 0xFF55FF, false);
        y += 10;

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
        if (orgInventoryScrollBar == null) {
            return;
        }

        gui.drawString(
                this.font,
                "Inventory",
                orgInventoryListX,
                orgInventoryListY - 10,
                0xFFD700,
                false
        );

        orgInventoryScrollBar.setContentHeight(
                ORG_PICKER_PANELS.length * ORG_INVENTORY_ROW_HEIGHT + 4
        );

        int scrollOffset = (int) orgInventoryScrollBar.scrollOffset;
        int scrollBarX = orgInventoryScrollBar.getX();

        gui.enableScissor(
                orgInventoryListX,
                orgInventoryListY,
                orgInventoryListX + orgInventoryListW,
                orgInventoryListY + orgInventoryListH
        );

        for (int i = 0; i < ORG_PICKER_PANELS.length; i++) {
            int rowY = orgInventoryListY + (i * ORG_INVENTORY_ROW_HEIGHT) - scrollOffset;

            if (rowY + ORG_INVENTORY_ROW_HEIGHT < orgInventoryListY
                    || rowY > orgInventoryListY + orgInventoryListH) {
                continue;
            }

            renderPanelInventoryRow(
                    gui,
                    mouseX,
                    mouseY,
                    i,
                    ORG_PICKER_PANELS[i],
                    orgInventoryListX,
                    rowY,
                    scrollBarX - orgInventoryListX - 2,
                    ORG_INVENTORY_ROW_HEIGHT
            );
        }

        gui.disableScissor();
    }

    private void renderPanelInventoryRow(
            GuiGraphics gui,
            int mouseX,
            int mouseY,
            int index,
            ResourceLocation panelId,
            int x,
            int y,
            int width,
            int height
    ) {
        PanelData data = PanelRegistry.get(panelId);

        if (data == null) {
            return;
        }

        GlobalDataRM addedData = null;

        if (minecraft != null && minecraft.player != null) {
            addedData = ModDataRM.getGlobal(minecraft.player);
        }

        int count = addedData != null ? addedData.getOwnedOrganizationPanelCount(panelId) : 0;

        boolean hovered = mouseX >= x
                && mouseX < x + width
                && mouseY >= y
                && mouseY < y + height;

        boolean selected = selectedOrgPanel.equals(panelId);

        int bgColor;

        if (selected) {
            bgColor = 0xAA660000;
        } else if (hovered) {
            bgColor = 0xAA222266;
        } else {
            bgColor = 0xAA111144;
        }

        int borderColor = selected ? 0xFFFF0000 : hovered ? 0xFFFFFFFF : 0xFF222255;

        gui.fill(x, y, x + width, y + height - 1, bgColor);

        gui.fill(x, y, x + width, y + 1, borderColor);
        gui.fill(x, y + height - 2, x + width, y + height - 1, 0xFF000022);

        // Small colored panel icon.
        int iconSize = 11;
        int iconX = x + 4;
        int iconY = y + 2;

        int fillColor = count > 0 ? getPanelColor(data) : 0xAA333333;

        gui.fill(iconX, iconY, iconX + iconSize, iconY + iconSize, 0xAA111111);
        gui.fill(iconX + 1, iconY + 1, iconX + iconSize - 1, iconY + iconSize - 1, fillColor);

        String name = getPanelDisplayName(panelId.getPath());
        String countText = "x" + count;

        int nameColor = count > 0 ? 0xFFFFFFFF : 0xFF888888;
        int countColor = count > 0 ? 0xFF55FF55 : 0xFFFF5555;

        gui.drawString(
                this.font,
                name,
                x + 19,
                y + 4,
                nameColor,
                false
        );

        gui.drawString(
                this.font,
                countText,
                x + width - this.font.width(countText) - 5,
                y + 4,
                countColor,
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
            case "ultima_weapon_panel" -> "Ultima Weapon Panel";
            case "high_jump_panel" -> "High Jump Panel";
            case "dodge_roll_panel" -> "Dodge Roll Panel";
            case "aerial_dodge_panel" -> "Aerial Dodge Panel";
            case "quick_run_panel" -> "Quick Run Panel";
            case "glide_panel" -> "Glide Panel";
            default -> path;
        };
    }

    private void renderHeartCount(GuiGraphics gui) {
        int hearts = getCurrentHearts();

        if (hearts < 0) {
            return;
        }

        gui.drawString(
                this.font,
                "Hearts: " + hearts,
                8,
                8,
                0xFFD700,
                false
        );
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {

        super.render(gui, mouseX, mouseY, partialTicks);

        if (shopBox != null) {
            shopBox.renderWidget(gui, mouseX, mouseY, partialTicks);
        }

        if (detailBox != null) {
            detailBox.renderWidget(gui, mouseX, mouseY, partialTicks);
        }

        if (editorBox != null) {
            editorBox.renderWidget(gui, mouseX, mouseY, partialTicks);
        }

        renderPanelShopList(gui, mouseX, mouseY);

        if (detailBox != null) {
            gui.drawString(this.font, "Selected Panel", detailBoxX + 8, detailBoxY + 6, 0xFFFF9900, false);
        }

        if (editorBox != null) {
            gui.drawString(this.font, "Panel Editor", editorBoxX + 10, editorBoxY + 6, 0xFFFF9900, false);
        }

        renderOrganizationPanelPicker(gui, mouseX, mouseY);
        renderOrganizationPanelGrid(gui, mouseX, mouseY);
        renderOrganizationPanelStats(gui);
        renderOrganizationPanelKeyGuide(gui);
        renderPanelInfoBox(gui);

        for (var renderable : this.renderables) {
            renderable.render(gui, mouseX, mouseY, partialTicks);
        }
    }

    private record PanelShopEntry(ResourceLocation panelId, String label, int cost, String description) {
    }

    private PanelShopEntry[] getPanelShopEntries() {
        return new PanelShopEntry[] {
                new PanelShopEntry(PanelRegistry.STRENGTH_UNIT, "STR Unit", 1000, "+1 STR while placed"),
                new PanelShopEntry(PanelRegistry.MAGIC_UNIT, "MAG Unit", 1000, "+1 MAG while placed"),
                new PanelShopEntry(PanelRegistry.DEFENSE_UNIT, "DEF Unit", 1000, "+1 DEF while placed"),
                new PanelShopEntry(PanelRegistry.AP_UNIT, "AP Unit", 500, "+1 AP while placed"),
                new PanelShopEntry(PanelRegistry.LEVEL_UP, "Level Up", 2000, "+1 LV while placed"),

                new PanelShopEntry(PanelRegistry.STRENGTH_UNIT_L, "STR Unit L", 2000, "Large STR panel"),
                new PanelShopEntry(PanelRegistry.MAGIC_UNIT_L, "MAG Unit L", 2000, "Large MAG panel"),
                new PanelShopEntry(PanelRegistry.DEFENSE_UNIT_L, "DEF Unit L", 2000, "Large DEF panel"),
                new PanelShopEntry(PanelRegistry.AP_UNIT_L, "AP Unit L", 1000, "Large AP panel"),
                new PanelShopEntry(PanelRegistry.LEVEL_DOUBLER, "Level Doubler", 4000, "Boosts level panel setups"),

                new PanelShopEntry(PanelRegistry.POWER_LINK, "Power Link", 2500, "Boosts nearby STR panels"),
                new PanelShopEntry(PanelRegistry.MAGIC_LINK, "Magic Link", 2500, "Boosts nearby MAG panels"),
                new PanelShopEntry(PanelRegistry.GUARD_LINK, "Guard Link", 2500, "Boosts nearby DEF panels"),
                new PanelShopEntry(PanelRegistry.LEVEL_LINK, "Level Link", 2500, "Boosts nearby LV panels"),

                new PanelShopEntry(PanelRegistry.ULTIMA_WEAPON_PANEL, "Ultima Weapon", 50000, "Enables Ultima Weapon while equipped"),
                new PanelShopEntry(PanelRegistry.HIGH_JUMP_PANEL, "High Jump", 2500, "Enables High Jump while equipped"),
                new PanelShopEntry(PanelRegistry.DODGE_ROLL_PANEL, "Dodge Roll", 2500, "Enables Dodge Roll while equipped"),
                new PanelShopEntry(PanelRegistry.AERIAL_DODGE_PANEL, "Aerial Dodge", 3000, "Enables Aerial Dodge while equipped"),
                new PanelShopEntry(PanelRegistry.QUICK_RUN_PANEL, "Quick Run", 3000, "Enables Quick Run while equipped"),
                new PanelShopEntry(PanelRegistry.GLIDE_PANEL, "Glide", 4000, "Enables Glide while equipped")

        };
    }

    private PanelShopEntry getSelectedShopEntry() {
        for (PanelShopEntry entry : getPanelShopEntries()) {
            if (entry.panelId().equals(selectedOrgPanel)) {
                return entry;
            }
        }

        return null;
    }

    private void updateAdaptivePanelLayout() {
        compactPanelLayout = this.width < 1000 || this.height < 620;
        emergencyPanelLayout = this.width < 760 || this.height < 500;

        if (emergencyPanelLayout) {
            orgSlotSize = 14;
            orgPickerSlotSize = 18;
            orgPickerGap = 2;
            orgPickerRowGap = 3;
            orgPickerColumns = 6;
            shopVisibleButtons = 3;
            shopButtonGap = 14;
            return;
        }

        if (compactPanelLayout) {
            orgSlotSize = 16;
            orgPickerSlotSize = 20;
            orgPickerGap = 3;
            orgPickerRowGap = 4;
            orgPickerColumns = 7;
            shopVisibleButtons = 4;
            shopButtonGap = 15;
            return;
        }

        orgSlotSize = BASE_ORG_SLOT_SIZE;
        orgPickerSlotSize = BASE_ORG_PANEL_PICKER_SLOT_SIZE;
        orgPickerGap = BASE_ORG_PANEL_PICKER_GAP;
        orgPickerRowGap = BASE_ORG_PANEL_PICKER_ROW_GAP;
        shopVisibleButtons = BASE_SHOP_VISIBLE_BUTTONS;
        shopButtonGap = BASE_SHOP_BUTTON_GAP;
    }

    private void renderPanelInfoBox(GuiGraphics gui) {
        PanelShopEntry entry = getSelectedShopEntry();

        if (entry == null || minecraft == null || minecraft.player == null) {
            return;
        }

        PlayerData playerData = PlayerData.get(minecraft.player);
        GlobalDataRM addedData = ModDataRM.getGlobal(minecraft.player);

        int hearts = playerData != null ? playerData.getHearts() : 0;
        int owned = addedData != null ? addedData.getOwnedOrganizationPanelCount(entry.panelId()) : 0;

        int x = this.shopPanelX;
        int y = this.shopInfoBoxY;
        int width = this.shopPanelWidth;
        int height = Math.max(50, detailBoxH - 20);

        gui.fill(x, y, x + width, y + height, 0x88000000);

        gui.fill(x, y, x + width, y + 1, 0xFF777777);
        gui.fill(x, y + height - 1, x + width, y + height, 0xFF777777);
        gui.fill(x, y, x + 1, y + height, 0xFF777777);
        gui.fill(x + width - 1, y, x + width, y + height, 0xFF777777);

        int textY = y + 6;

        gui.drawString(this.font, entry.label(), x + 8, textY, 0xFFFFD700, false);
        textY += 12;

        int costColor = hearts >= entry.cost() ? 0xFF55FF55 : 0xFFFF5555;

        gui.drawString(this.font, "Cost: " + entry.cost() + " Hearts", x + 8, textY, costColor, false);
        textY += 11;

        gui.drawString(this.font, "Owned: " + owned, x + 8, textY, 0xFFFFFFFF, false);
        textY += 11;

        if (!compactPanelLayout) {
            gui.drawString(this.font, "Hearts: " + hearts, x + 8, textY, 0xFFFF5555, false);
            textY += 13;
        } else {
            textY += 2;
        }

        drawWrappedString(gui, entry.description(), x + 8, textY, width - 16, 0xFFAAAAAA);
    }

    private void drawWrappedString(GuiGraphics gui, String text, int x, int y, int maxWidth, int color) {
        if (text == null || text.isEmpty()) {
            return;
        }

        for (net.minecraft.util.FormattedCharSequence line : this.font.split(Component.literal(text), maxWidth)) {
            gui.drawString(this.font, line, x, y, color, false);
            y += 10;
        }
    }

    private PanelShopEntry getClickedShopEntry(int mouseX, int mouseY) {
        if (shopScrollBar == null) {
            return null;
        }

        int scrollBarX = shopScrollBar.getX();

        boolean insideList = mouseX >= shopListX
                && mouseX < scrollBarX
                && mouseY >= shopListY
                && mouseY < shopListY + shopListH;

        if (!insideList) {
            return null;
        }

        int localY = mouseY - shopListY + (int) shopScrollBar.scrollOffset;
        int index = localY / SHOP_ROW_HEIGHT;

        PanelShopEntry[] entries = getPanelShopEntries();

        if (index < 0 || index >= entries.length) {
            return null;
        }

        return entries[index];
    }

    private void renderPanelShopList(GuiGraphics gui, int mouseX, int mouseY) {
        if (shopScrollBar == null) {
            return;
        }

        gui.drawString(
                this.font,
                "Panel Shop",
                shopBoxX + 8,
                shopBoxY + 6,
                0xFFFF9900,
                false
        );

        PanelShopEntry[] entries = getPanelShopEntries();

        shopScrollBar.setContentHeight(entries.length * SHOP_ROW_HEIGHT + 4);

        int scrollOffset = (int) shopScrollBar.scrollOffset;
        int scrollBarX = shopScrollBar.getX();

        gui.enableScissor(
                shopListX,
                shopListY,
                shopListX + shopListW,
                shopListY + shopListH
        );

        for (int i = 0; i < entries.length; i++) {
            int rowY = shopListY + (i * SHOP_ROW_HEIGHT) - scrollOffset;

            if (rowY + SHOP_ROW_HEIGHT < shopListY
                    || rowY > shopListY + shopListH) {
                continue;
            }

            renderPanelShopRow(
                    gui,
                    mouseX,
                    mouseY,
                    entries[i],
                    shopListX,
                    rowY,
                    scrollBarX - shopListX - 2,
                    SHOP_ROW_HEIGHT
            );
        }

        gui.disableScissor();
    }

    private void renderPanelShopRow(
            GuiGraphics gui,
            int mouseX,
            int mouseY,
            PanelShopEntry entry,
            int x,
            int y,
            int width,
            int height
    ) {
        boolean hovered = mouseX >= x
                && mouseX < x + width
                && mouseY >= y
                && mouseY < y + height;

        boolean selected = selectedOrgPanel != null && selectedOrgPanel.equals(entry.panelId());

        PlayerData playerData = minecraft != null && minecraft.player != null
                ? PlayerData.get(minecraft.player)
                : null;

        int hearts = playerData != null ? playerData.getHearts() : 0;
        boolean canAfford = hearts >= entry.cost();

        int bgColor;

        if (selected) {
            bgColor = 0xAA660000;
        } else if (hovered) {
            bgColor = 0xAA222266;
        } else {
            bgColor = 0xAA111144;
        }

        int borderColor = selected ? 0xFFFF0000 : hovered ? 0xFFFFFFFF : 0xFF222255;

        gui.fill(x, y, x + width, y + height - 1, bgColor);

        gui.fill(x, y, x + width, y + 1, borderColor);
        gui.fill(x, y + height - 2, x + width, y + height - 1, 0xFF000022);

        String selector = selected ? "› " : "";
        String name = selector + entry.label();

        int nameColor = canAfford ? 0xFFFFFFFF : 0xFFAAAAAA;
        int costColor = canAfford ? 0xFF55FF55 : 0xFFFF5555;

        gui.drawString(
                this.font,
                name,
                x + 6,
                y + 4,
                nameColor,
                false
        );

        String costText = String.valueOf(entry.cost());

        gui.drawString(
                this.font,
                costText,
                x + width - this.font.width(costText) - 5,
                y + 4,
                costColor,
                false
        );

        if (hovered) {
            gui.renderTooltip(
                    this.font,
                    Component.literal(entry.label() + " - " + entry.cost() + " Hearts"),
                    mouseX,
                    mouseY
            );
        }
    }
}
