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

import org.lwjgl.glfw.GLFW;

import java.util.List;

import java.awt.*;

public class PanelsMenu extends MenuBackground {


    private static final int ORG_SLOT_SIZE = 28;

    private static final int ORG_PANEL_PICKER_SLOT_SIZE = 24;
    private static final int ORG_PANEL_PICKER_GAP = 6;

    private static final int ORG_PANEL_PICKER_COLUMNS = 7;
    private static final int ORG_PANEL_PICKER_ROW_GAP = 14;

    private static final int SHOP_VISIBLE_BUTTONS = 10;
    private static final int SHOP_BUTTON_GAP = 20;

    private int shopScrollOffset = 0;

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
            case "buy_str_unit" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.STRENGTH_UNIT,
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_mag_unit" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.MAGIC_UNIT,
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_def_unit" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.DEFENSE_UNIT,
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_ap_unit" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.AP_UNIT,
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_level_up" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.LEVEL_UP,
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_slot_releaser" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "slot_releaser"),
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }
            case "buy_str_unit_l" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.STRENGTH_UNIT_L,
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_mag_unit_l" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.MAGIC_UNIT_L,
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_def_unit_l" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.DEFENSE_UNIT_L,
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_ap_unit_l" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.AP_UNIT_L,
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_level_doubler" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.LEVEL_DOUBLER,
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_power_link" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.POWER_LINK,
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_magic_link" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.MAGIC_LINK,
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_guard_link" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.GUARD_LINK,
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_level_link" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.LEVEL_LINK,
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            // Abilities

            case "buy_ultima_weapon_panel" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(
                        PanelRegistry.ULTIMA_WEAPON_PANEL,
                        1
                ));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_high_jump_panel" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(PanelRegistry.HIGH_JUMP_PANEL, 1));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_dodge_roll_panel" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(PanelRegistry.DODGE_ROLL_PANEL, 1));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_aerial_dodge_panel" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(PanelRegistry.AERIAL_DODGE_PANEL, 1));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_quick_run_panel" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(PanelRegistry.QUICK_RUN_PANEL, 1));
                minecraft.player.playSound(ModSounds.itemget.get());
            }

            case "buy_glide_panel" -> {
                PacketHandlerRM.sendToServer(new CSBuyOrganizationPanelPacket(PanelRegistry.GLIDE_PANEL, 1));
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
        for (int i = 0; i < ORG_PICKER_PANELS.length; i++) {
            int col = i % ORG_PANEL_PICKER_COLUMNS;
            int row = i / ORG_PANEL_PICKER_COLUMNS;

            int x = orgPickerX + col * (ORG_PANEL_PICKER_SLOT_SIZE + ORG_PANEL_PICKER_GAP);
            int y = orgPickerY + row * (ORG_PANEL_PICKER_SLOT_SIZE + ORG_PANEL_PICKER_ROW_GAP);

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

        int currentHearts = getCurrentHearts();

        if (currentHearts != -1 && currentHearts != lastKnownHearts) {
            init();
        }
    }

    @Override
    public void init() {



        Player player;
        final PlayerData playerData = PlayerData.get(minecraft.player);
        GlobalDataRM addedData = ModDataRM.getGlobal(minecraft.player);
        ticks = 0;

        lastKnownHearts = playerData != null ? playerData.getHearts() : -1;

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
        this.orgGridY = contentY + 42;

        int pickerColumns = Math.min(ORG_PICKER_PANELS.length, ORG_PANEL_PICKER_COLUMNS);

        int pickerWidth = (pickerColumns * ORG_PANEL_PICKER_SLOT_SIZE)
                + ((pickerColumns - 1) * ORG_PANEL_PICKER_GAP);

        this.orgPickerX = orgGridX + (gridWidth / 2) - (pickerWidth / 2);
        this.orgPickerY = contentY - 52;

        int i = 0;

        int shopY = button_statsY;
        int shopGap = 20;
        int shopX = (int) buttonPosX;
        int shopWidth = (int) buttonWidth;

        PanelShopEntry[] shopEntries = getPanelShopEntries();

        int maxScroll = Math.max(0, shopEntries.length - SHOP_VISIBLE_BUTTONS);
        shopScrollOffset = Math.max(0, Math.min(shopScrollOffset, maxScroll));

        for (int shopIndex = 0; shopIndex < SHOP_VISIBLE_BUTTONS; shopIndex++) {
            int entryIndex = shopScrollOffset + shopIndex;

            if (entryIndex >= shopEntries.length) {
                break;
            }

            PanelShopEntry entry = shopEntries[entryIndex];

            addPanelShopButton(
                    playerData,
                    shopX,
                    shopY,
                    shopWidth,
                    entry.label(),
                    entry.cost(),
                    entry.action()
            );

            shopY += SHOP_BUTTON_GAP;
        }

        shopY += 4;

        addRenderableWidget(new MenuButton(
                shopX,
                shopY,
                shopWidth / 60,
                "▲",
                MenuButton.ButtonType.BUTTON,
                false,
                e -> {
                    if (shopScrollOffset > 0) {
                        shopScrollOffset--;
                        init();
                    } else {
                        minecraft.player.playSound(ModSounds.error.get());
                    }
                }
        ));

        addRenderableWidget(new MenuButton(
                shopX + shopWidth / 2 + 2,
                shopY,
                shopWidth / 60,
                "▼",
                MenuButton.ButtonType.BUTTON,
                false,
                e -> {
                    int max = Math.max(0, getPanelShopEntries().length - SHOP_VISIBLE_BUTTONS);

                    if (shopScrollOffset < max) {
                        shopScrollOffset++;
                        init();
                    } else {
                        minecraft.player.playSound(ModSounds.error.get());
                    }
                }
        ));

        shopY += shopGap;

        if (addedData.getPanelsEnabled() == 1) {
            addRenderableWidget(toggleOff = new MenuButton(
                    shopX,
                    shopY,
                    shopWidth,
                    "Boost OFF",
                    MenuButton.ButtonType.BUTTON,
                    false,
                    e -> action("toggleOff")
            ));
        } else {
            addRenderableWidget(toggleOn = new MenuButton(
                    shopX,
                    shopY,
                    shopWidth,
                    "Boost ON",
                    MenuButton.ButtonType.BUTTON,
                    false,
                    e -> action("toggleOn")
            ));
        }

        shopY += shopGap;

        addRenderableWidget(backButton = new MenuButton(
                shopX,
                shopY,
                shopWidth,
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

    private void addPanelShopButton(
            PlayerData playerData,
            int x,
            int y,
            int width,
            String label,
            int cost,
            String action
    ) {
        boolean canAfford = playerData != null && playerData.getHearts() >= cost;

        addRenderableWidget(new MenuButton(
                x,
                y,
                width,
                label + " - " + (canAfford ? ChatFormatting.GREEN : ChatFormatting.DARK_RED) + cost + " Hearts",
                MenuButton.ButtonType.BUTTON,
                false,
                e -> action(canAfford ? action : "req")
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
            case "ultima_weapon_panel" -> "ULT";
            case "high_jump_panel" -> "HJ";
            case "dodge_roll_panel" -> "DR";
            case "aerial_dodge_panel" -> "AD";
            case "quick_run_panel" -> "QR";
            case "glide_panel" -> "GLD";
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

                boolean unlocked = addedData.isOrganizationPanelSlotUnlocked(x, y);

                int borderColor = unlocked
                        ? hovered ? 0xFFFFFF00 : 0xFF555555
                        : 0xFF222222;

                int fillColor = unlocked
                        ? hovered ? 0x55222222 : 0xAA111111
                        : 0xAA050505;

                gui.fill(sx, sy, sx + ORG_SLOT_SIZE, sy + ORG_SLOT_SIZE, fillColor);

                // border
                gui.fill(sx, sy, sx + ORG_SLOT_SIZE, sy + 1, borderColor);
                gui.fill(sx, sy + ORG_SLOT_SIZE - 1, sx + ORG_SLOT_SIZE, sy + ORG_SLOT_SIZE, borderColor);
                gui.fill(sx, sy, sx + 1, sy + ORG_SLOT_SIZE, borderColor);
                gui.fill(sx + ORG_SLOT_SIZE - 1, sy, sx + ORG_SLOT_SIZE, sy + ORG_SLOT_SIZE, borderColor);

                if (!unlocked) {
                    gui.drawString(
                            this.font,
                            "X",
                            sx + 10,
                            sy + 9,
                            0xFF333333,
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

        int col = index % ORG_PANEL_PICKER_COLUMNS;
        int row = index / ORG_PANEL_PICKER_COLUMNS;

        int x = orgPickerX + col * (ORG_PANEL_PICKER_SLOT_SIZE + ORG_PANEL_PICKER_GAP);
        int y = orgPickerY + row * (ORG_PANEL_PICKER_SLOT_SIZE + ORG_PANEL_PICKER_ROW_GAP);

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

        renderHeartCount(gui);

        renderOrganizationPanelPicker(gui, mouseX, mouseY);
        renderOrganizationPanelGrid(gui, mouseX, mouseY);
        renderOrganizationPanelStats(gui);
        renderOrganizationPanelKeyGuide(gui);
    }

    private record PanelShopEntry(String label, int cost, String action) {
    }

    private PanelShopEntry[] getPanelShopEntries() {
        return new PanelShopEntry[] {
                new PanelShopEntry("Buy STR Unit", 250, "buy_str_unit"),
                new PanelShopEntry("Buy MAG Unit", 250, "buy_mag_unit"),
                new PanelShopEntry("Buy DEF Unit", 250, "buy_def_unit"),
                new PanelShopEntry("Buy AP Unit", 400, "buy_ap_unit"),
                new PanelShopEntry("Buy Level Up", 750, "buy_level_up"),

                new PanelShopEntry("Buy STR Unit L", 1200, "buy_str_unit_l"),
                new PanelShopEntry("Buy MAG Unit L", 1200, "buy_mag_unit_l"),
                new PanelShopEntry("Buy DEF Unit L", 1200, "buy_def_unit_l"),
                new PanelShopEntry("Buy AP Unit L", 1600, "buy_ap_unit_l"),
                new PanelShopEntry("Buy Level Doubler", 3000, "buy_level_doubler"),

                new PanelShopEntry("Buy Power Link", 1500, "buy_power_link"),
                new PanelShopEntry("Buy Magic Link", 1500, "buy_magic_link"),
                new PanelShopEntry("Buy Guard Link", 1500, "buy_guard_link"),
                new PanelShopEntry("Buy Level Link", 2000, "buy_level_link"),

                new PanelShopEntry("Buy Slot Releaser", 1000, "buy_slot_releaser"),

                new PanelShopEntry("Buy Ultima Weapon", 50000, "buy_ultima_weapon_panel"),

                new PanelShopEntry("Buy High Jump", 2500, "buy_high_jump_panel"),
                new PanelShopEntry("Buy Dodge Roll", 2500, "buy_dodge_roll_panel"),
                new PanelShopEntry("Buy Aerial Dodge", 3000, "buy_aerial_dodge_panel"),
                new PanelShopEntry("Buy Quick Run", 3000, "buy_quick_run_panel"),
                new PanelShopEntry("Buy Glide", 4000, "buy_glide_panel"),

                new PanelShopEntry("Leave Organization", 13000, "rejectOrg")
        };
    }
}
