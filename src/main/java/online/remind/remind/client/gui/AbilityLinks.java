package online.remind.remind.client.gui.dreameaters;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.gui.DreamEaterMenu;
import online.remind.remind.client.sound.MusicManager;
import online.remind.remind.dreameater.DreamEaterInfo;
import online.remind.remind.dreameater.DreamEaterLinkData;
import online.remind.remind.lib.StringsRM;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class AbilityLinks extends MenuBackground {

    private MenuButton backButton;

    public AbilityLinks() {
        super("Ability Links", new Color(236, 85, 236));
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void onClose() {
        super.onClose();

        Screen next = Minecraft.getInstance().screen;

        if (!(next instanceof DreamEaterMenu
                || next instanceof ChangeSpirit
                || next instanceof CreateSpirit
                || next instanceof AbilityLinks)) {
            MusicManager.stop();
        }
    }

    protected void action(String string) {
        if (string.equals("back")) {
            minecraft.setScreen(new DreamEaterMenu());
            return;
        }

        if (string.equals("locked")) {
            if (minecraft.player != null) {
                minecraft.player.playSound(ModSounds.error.get());
            }
        }
    }

    @Override
    public void init() {
        super.init();

        MusicManager.start();
        this.renderables.clear();

        float topBarHeight = (float) height * 0.17F;
        int buttonY = (int) topBarHeight + 5;

        float buttonPosX = (float) width * 0.03F;
        float buttonWidth = ((float) width * 0.1744F) + 10;

        float dataWidth = ((float) width * 0.1744F) + 30;
        int col1X = (int) (buttonPosX + buttonWidth + 40);
        int col2X = (int) (col1X + dataWidth + 15);
        int col3X = (int) (col2X + dataWidth + 15);

        int spacer = 14;
        int row = 0;

        addRenderableWidget(backButton = new MenuButton(
                (int) buttonPosX,
                buttonY,
                (int) buttonWidth,
                Strings.Gui_Menu_Back,
                MenuButton.ButtonType.BUTTON,
                false,
                e -> action("back")
        ));

        if (minecraft.player == null) {
            return;
        }

        GlobalDataRM global = ModDataRM.getGlobal(minecraft.player);
        PlayerData playerData = PlayerData.get(minecraft.player);

        if (global == null || playerData == null) {
            addRenderableWidget(new MenuColourBox(
                    col1X,
                    buttonY,
                    (int) dataWidth,
                    "Error:",
                    "Missing player data",
                    0xbf1414
            ));
            return;
        }

        String dreamEaterRL = global.getDreamEaterRL();

        if (isNoDreamEaterEquipped(dreamEaterRL)) {
            addRenderableWidget(new MenuColourBox(
                    col1X,
                    buttonY,
                    (int) dataWidth,
                    "Dream Eater:",
                    "None Equipped",
                    0xffffff
            ));
            return;
        }

        int dreamEaterLevel = Math.max(1, global.getDreamEaterLevel(dreamEaterRL));

        addRenderableWidget(new MenuColourBox(
                col1X,
                buttonY + (row++ * spacer),
                (int) dataWidth,
                "Dream Eater:",
                getDreamEaterDisplayName(dreamEaterRL),
                0xffffff
        ));

        addRenderableWidget(new MenuColourBox(
                col1X,
                buttonY + (row++ * spacer),
                (int) dataWidth,
                "Level:",
                String.valueOf(dreamEaterLevel),
                0xffffff
        ));

        row++;

        addRenderableWidget(new MenuColourBox(
                col1X,
                buttonY + (row * spacer),
                (int) dataWidth,
                "Ability",
                "",
                0xffffff
        ));

        addRenderableWidget(new MenuColourBox(
                col2X,
                buttonY + (row * spacer),
                (int) dataWidth,
                "Requirement",
                "",
                0xffffff
        ));

        addRenderableWidget(new MenuColourBox(
                col3X,
                buttonY + (row++ * spacer),
                (int) dataWidth,
                "Status",
                "",
                0xffffff
        ));

        List<DreamEaterLinkEntry> entries = getAbilityLinksForDreamEater(
                dreamEaterRL,
                dreamEaterLevel
        );

        if (entries.isEmpty()) {
            entries.add(new DreamEaterLinkEntry("No Ability Links", "Future Dream Eater", false));
        }

        for (DreamEaterLinkEntry entry : entries) {
            int color = entry.unlocked ? 0x7a8487 : 0x444444;
            String status = entry.unlocked ? "ACTIVE" : "LOCKED";

            addRenderableWidget(new MenuColourBox(
                    col1X,
                    buttonY + (row * spacer),
                    (int) dataWidth,
                    entry.name,
                    "",
                    color
            ));

            addRenderableWidget(new MenuColourBox(
                    col2X,
                    buttonY + (row * spacer),
                    (int) dataWidth,
                    entry.requirement,
                    "",
                    color
            ));

            addRenderableWidget(new MenuColourBox(
                    col3X,
                    buttonY + (row++ * spacer),
                    (int) dataWidth,
                    status,
                    "",
                    color
            ));
        }
    }

    private boolean isNoDreamEaterEquipped(String dreamEaterRL) {
        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            return true;
        }

        if (StringsRM.none.equals(dreamEaterRL)) {
            return true;
        }

        if ("none".equalsIgnoreCase(dreamEaterRL)) {
            return true;
        }

        return "kkremind:none".equalsIgnoreCase(dreamEaterRL);
    }

    private String getDreamEaterDisplayName(String dreamEaterRL) {
        return DreamEaterInfo.getDisplayName(dreamEaterRL);
    }

    private List<DreamEaterLinkEntry> getAbilityLinksForDreamEater(
            String dreamEaterRL,
            int dreamEaterLevel
    ) {
        List<DreamEaterLinkEntry> entries = new ArrayList<>();

        List<DreamEaterLinkData.LinkEntry> links = DreamEaterInfo.getLinks(dreamEaterRL);

        if (links == null || links.isEmpty()) {
            return entries;
        }

        dreamEaterLevel = Math.max(1, dreamEaterLevel);

        for (DreamEaterLinkData.LinkEntry link : links) {
            if (link == null) {
                continue;
            }

            boolean unlocked = DreamEaterLinkData.isUnlocked(link, dreamEaterLevel);
            String requirement = getRequirementText(link);

            entries.add(new DreamEaterLinkEntry(
                    link.displayName(),
                    requirement,
                    unlocked
            ));
        }

        return entries;
    }

    private String getRequirementText(DreamEaterLinkData.LinkEntry link) {
        if (link == null) {
            return "";
        }

        if (link.unlockLevel() <= 1) {
            return link.type();
        }

        return link.type() + " - Lv " + link.unlockLevel();
    }

    private static class DreamEaterLinkEntry {
        private final String name;
        private final String requirement;
        private final boolean unlocked;

        private DreamEaterLinkEntry(String name, String requirement, boolean unlocked) {
            this.name = name;
            this.requirement = requirement;
            this.unlocked = unlocked;
        }
    }
}