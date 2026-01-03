package online.remind.remind.client.gui.dreameaters;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSyncAllClientDataPacket;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.gui.DreamEaterMenu;
import online.remind.remind.client.gui.GUIHelperRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.network.cts.CSChangeSpiritPacket;
import online.remind.remind.network.cts.CSPanelPacket;
import online.remind.remind.client.sound.DreamEaterMenuSound;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.client.sound.MusicManager;

import java.awt.*;

public class ChangeSpirit extends MenuBackground {


    public ChangeSpirit(String name, Color rgb) {
        super(name, rgb);
    }
    private MenuButton backButton, selected, none, chirithy; //TODO: add more later when the system is created

    int ticks = 0;

    @Override
    public void tick() {
        super.tick();
        ticks++;
        init();

    }

    public void onClose(){
        super.onClose();

        Screen next = Minecraft.getInstance().screen;

        if (!(next instanceof DreamEaterMenu || next instanceof ChangeSpirit || next instanceof CreateSpirit)){
            MusicManager.stop();
        }
    }

    public ChangeSpirit() {
        super("Change Spirit", new Color(241, 115, 24));
        minecraft = Minecraft.getInstance();
    }

    public void reloadMenu(){
        minecraft.setScreen(new ChangeSpirit());
    }

    protected void action(String string) {
        IGlobalDataRM global = ModDataRM.getGlobal(minecraft.player);
        PlayerData playerData = PlayerData.get(minecraft.player);

        if (string.equals("back")) {
            minecraft.setScreen(new DreamEaterMenu());
        }
        if (string.equals("none")){
            PacketHandlerRM.sendToServer(new CSChangeSpiritPacket(0));
            PacketHandlerRM.syncGlobalToAllAround(minecraft.player, global);
            init();
        }
        if (string.equals("selected")){
            init();
        }
        if (string.equals("chirithy")) {
            PacketHandlerRM.sendToServer(new CSChangeSpiritPacket(1));
            PacketHandlerRM.syncGlobalToAllAround(minecraft.player, global);
            init();
        }

    }

    @Override
    public void init() {
        super.init();
        ticks = 0;
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

        IGlobalDataRM global = ModDataRM.getGlobal(minecraft.player);
        PlayerData playerData = PlayerData.get(minecraft.player);


        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, (Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("back");
        }));
        if (global.getDreamEaterID() != 0) {
            addRenderableWidget(none = new MenuButton((int) buttonPosX, button_statsY + 20, (int) buttonWidth, ("None"), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("none");
            }));
        } else {
            addRenderableWidget(selected = new MenuButton((int) buttonPosX, button_statsY + 20, (int) buttonWidth, (ChatFormatting.GOLD + "None"), MenuButton.ButtonType.BUTTON, false, (e) -> {
                action("selected");
            }));
        }
        if (global.getDreamEaterID() == 1){
            addRenderableWidget(selected = new MenuButton((int) buttonPosX, button_statsY + 40, (int) buttonWidth, (ChatFormatting.GOLD + "Chirithy"), MenuButton.ButtonType.BUTTON, true, (e) -> {
                action("selected");
            }));
        } else {
            addRenderableWidget(chirithy = new MenuButton((int) buttonPosX, button_statsY + 40, (int) buttonWidth, ("Chirithy"), MenuButton.ButtonType.BUTTON, true, (e) -> {
                action("chirithy");
            }));
        }
    }




}
