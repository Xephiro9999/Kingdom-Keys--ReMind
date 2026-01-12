package online.remind.remind.client.gui.dreameaters;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.gui.DreamEaterMenu;
import online.remind.remind.client.sound.MusicManager;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.network.cts.CSChangeSpiritPacket;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ChangeSpirit extends MenuBackground {

    public ChangeSpirit(String name, Color rgb) {
        super(name, rgb);
    }
    private MenuButton backButton, selected, none, chirithy, meowwow; //TODO: add more later when the system is created

    public enum DreamEaterType {
        NONE(0, KingdomKeysReMind.MODID+":"+StringsRM.none),
        CHIRITHY(1, KingdomKeysReMind.MODID+":"+StringsRM.chirithy),
        MEOWWOW(2, KingdomKeysReMind.MODID+":"+StringsRM.meowWow);

        private final int id;
        private MenuButton button;
        private final String rl;

        DreamEaterType(int id, String rl) {
            this.id = id;
            this.rl = rl;
        }

        public int getId() {
            return id;
        }

        public MenuButton getButton() {
            return button;
        }
        public void setButton(MenuButton button){
            this.button = button;
        }
        public String getRl() {
            return rl;
        }

        public static DreamEaterType getById(int id) {
            for (DreamEaterType type : values()) {
                if (type.id == id) return type;
            }
            return NONE;
        }
    }


    IGlobalDataRM globalData;

    public ChangeSpirit() {
        super("Change Spirit", new Color(241, 115, 24));
        minecraft = Minecraft.getInstance();
    }

    public void onClose(){
        super.onClose();

        Screen next = Minecraft.getInstance().screen;

        if (!(next instanceof DreamEaterMenu || next instanceof ChangeSpirit || next instanceof CreateSpirit)){
            MusicManager.stop();
        }
    }

    public void reloadMenu(){
        minecraft.setScreen(new ChangeSpirit());
    }
    protected void select(DreamEaterType type) {
        globalData.setDreamEaterRL(type.rl);
        PacketHandlerRM.sendToServer(new CSChangeSpiritPacket(type.rl));
        PacketHandlerRM.syncGlobalToAllAround(minecraft.player, globalData);
        reloadMenu();
    }

    protected void action(String string) {
        if(globalData == null)
            return;

        if (string.equals("back")) {
            minecraft.setScreen(new DreamEaterMenu());
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

        globalData = ModDataRM.getGlobal(minecraft.player);

        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, button_statsY + 18 * i++, (int) buttonWidth, (Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("back");
        }));

        addRenderableWidget(none = new MenuButton((int) buttonPosX, button_statsY + 18 + i++, (int) buttonWidth, ("None"), MenuButton.ButtonType.BUTTON, false, (e) -> {
            select(DreamEaterType.NONE);
        }));

        addRenderableWidget(chirithy = new MenuButton((int) buttonPosX, button_statsY + 18 * i++, (int) buttonWidth, "Chirithy", MenuButton.ButtonType.BUTTON, true, (e) -> {
            select(DreamEaterType.CHIRITHY);
        }));

        addRenderableWidget(meowwow = new MenuButton((int) buttonPosX, button_statsY + 18 * i++, (int) buttonWidth, "Meow Wow", MenuButton.ButtonType.BUTTON, true, (e) -> {
            select(DreamEaterType.MEOWWOW);
        }));

        //Add the buttons to the ENUM so they can be colored gold in the render method
        DreamEaterType.NONE.button = none;
        DreamEaterType.CHIRITHY.button = chirithy;
        DreamEaterType.MEOWWOW.button = meowwow;
    }


    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        if(globalData == null)
            return;
        //System.out.println(globalData.getDreamEaterRL());
        //Read every type and if it's the selected one render it in gold
        for (DreamEaterType type : DreamEaterType.values()) {
            //System.out.println();
            if(type.rl.equals(globalData.getDreamEaterRL())){
                type.button.setMessage(Component.literal(ChatFormatting.GOLD + type.button.getMessage().getString()));
            }
        }
        super.render(gui, mouseX, mouseY, partialTicks);
    }


}
