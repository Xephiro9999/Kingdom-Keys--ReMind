package online.remind.remind.client.gui.dreameaters;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.gui.DreamEaterMenu;
import online.remind.remind.client.sound.MusicManager;
import online.remind.remind.dreameater.DreamEater;
import online.remind.remind.dreameater.DreamEaterLinkData;
import online.remind.remind.dreameater.ModDreamEaters;
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

        if (!(next instanceof DreamEaterMenu || next instanceof ChangeSpirit || next instanceof CreateSpirit || next instanceof AbilityLinks)) {
            MusicManager.stop();
        }
    }

    protected void action(String string) {
        if (string.equals("back")) {
            minecraft.setScreen(new DreamEaterMenu());
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
            addRenderableWidget(new MenuColourBox(col1X, buttonY, (int) dataWidth, "Error:", "Missing player data", 0xbf1414));
            return;
        }

        DreamEater dreamEater = getEquippedDreamEater(global);

        if (dreamEater == null || StringsRM.none.equals(dreamEater.getName())) {
            addRenderableWidget(new MenuColourBox(col1X, buttonY, (int) dataWidth, "Dream Eater:", "None Equipped", 0xffffff));
            return;
        }

        int playerLevel = Math.max(1, playerData.getLevel());

        addRenderableWidget(new MenuColourBox(
                col1X,
                buttonY + (row++ * spacer),
                (int) dataWidth,
                "Dream Eater:",
                getDreamEaterDisplayName(dreamEater),
                0xffffff
        ));

        addRenderableWidget(new MenuColourBox(
                col1X,
                buttonY + (row++ * spacer),
                (int) dataWidth,
                "Level:",
                "" + playerLevel,
                0xffffff
        ));

        row++;

        addRenderableWidget(new MenuColourBox(col1X, buttonY + (row * spacer), (int) dataWidth, "Ability", "", 0xffffff));
        addRenderableWidget(new MenuColourBox(col2X, buttonY + (row * spacer), (int) dataWidth, "Requirement", "", 0xffffff));
        addRenderableWidget(new MenuColourBox(col3X, buttonY + (row++ * spacer), (int) dataWidth, "Status", "", 0xffffff));

        List<DreamEaterLinkEntry> entries = getAbilityLinksForDreamEater(dreamEater, global, playerData);

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

    private DreamEater getEquippedDreamEater(GlobalDataRM global) {
        String dreamEaterRL = global.getDreamEaterRL();

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            return null;
        }

        return ModDreamEaters.registry.get(ResourceLocation.parse(dreamEaterRL));
    }

    private List<DreamEaterLinkEntry> getAbilityLinksForDreamEater(DreamEater dreamEater, GlobalDataRM global, PlayerData playerData) {
        List<DreamEaterLinkEntry> entries = new ArrayList<>();

        if (dreamEater == null) {
            return entries;
        }

        String name = dreamEater.getName();

        if (StringsRM.chirithy.equals(name)) {
            addChirithyLinks(entries, global);
            return entries;
        }

        if (StringsRM.meowWow.equals(name)) {
            addMeowWowLinks(entries, playerData);
            return entries;
        }

        entries.add(new DreamEaterLinkEntry("No Ability Links", "Future Dream Eater", false));
        return entries;
    }

    private void addChirithyLinks(List<DreamEaterLinkEntry> entries, GlobalDataRM global) {
        boolean hasCure = global.getLearndedMagics().containsKey(Strings.Magic_Cure)
                || global.getLearndedMagics().containsKey(Strings.Magic_Cura)
                || global.getLearndedMagics().containsKey(Strings.Magic_Curaga);

        boolean hasAero = global.getLearndedMagics().containsKey(Strings.Magic_Aero)
                || global.getLearndedMagics().containsKey(Strings.Magic_Aerora)
                || global.getLearndedMagics().containsKey(Strings.Magic_Aeroga);

        boolean hasEsuna = global.getLearndedMagics().containsKey(KingdomKeysReMind.MODID + ":magic_esuna")
                || global.getLearndedMagics().containsKey(KingdomKeysReMind.MODID + ":magic_group_esuna");

        boolean hasAutoLife = global.getLearndedMagics().containsKey(KingdomKeysReMind.MODID + ":magic_auto-life");

        entries.add(new DreamEaterLinkEntry("Cure Support", "Learn Cure", hasCure));
        entries.add(new DreamEaterLinkEntry("Aero Support", "Learn Aero", hasAero));
        entries.add(new DreamEaterLinkEntry("Esuna Support", "Learn Esuna", hasEsuna));
        entries.add(new DreamEaterLinkEntry("Auto-Life Support", "Learn Auto-Life", hasAutoLife));
    }

    private void addMeowWowLinks(List<DreamEaterLinkEntry> entries, PlayerData playerData) {
        int level = Math.max(1, playerData.getLevel());

        for (DreamEaterLinkData.LinkEntry link : DreamEaterLinkData.getMeowWowLinks()) {
            boolean unlocked = DreamEaterLinkData.isUnlocked(link, level);

            String requirement;

            if (link.unlockLevel() <= 1) {
                requirement = link.type();
            } else {
                requirement = link.type() + " - Lv " + link.unlockLevel();
            }

            entries.add(new DreamEaterLinkEntry(
                    link.displayName(),
                    requirement,
                    unlocked
            ));
        }
    }

    private String getDreamEaterDisplayName(DreamEater dreamEater) {
        if (dreamEater == null) {
            return "N/A";
        }

        if (StringsRM.chirithy.equals(dreamEater.getName())) {
            return "Chirithy";
        }

        if (StringsRM.meowWow.equals(dreamEater.getName())) {
            return "Meow Wow";
        }

        return dreamEater.getName();
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