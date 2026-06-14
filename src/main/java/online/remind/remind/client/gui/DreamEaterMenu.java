package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.ClientUtilsRM;
import online.remind.remind.client.gui.dreameaters.ChangeSpirit;
import online.remind.remind.client.gui.dreameaters.CreateSpirit;
import online.remind.remind.client.sound.MusicManager;
import online.remind.remind.dreameater.DreamEater;
import online.remind.remind.dreameater.ModDreamEaters;
import online.remind.remind.client.gui.dreameaters.AbilityLinks;
import online.remind.remind.lib.StringsRM;

import java.awt.*;

public class DreamEaterMenu extends MenuBackground {

    public DreamEaterMenu(String name, Color rgb) {
        super(name, rgb);
    }


    private MenuButton backButton, changeSpirit, createSpirit, abilityLinks;

    MenuColourBox level, spiritHP, spiritSTR, spiritMAG, spiritDEF, name, abilities, a1, a2, a3, a4, a5, a6, none;
    MenuColourBox[] spiritWidgets = {level, spiritHP, spiritSTR, spiritMAG, spiritDEF, name, none};

    public DreamEaterMenu() {
        super("Dream Eaters", new Color(236, 85, 236));
        minecraft = Minecraft.getInstance();
    }

    public void onClose() {
        super.onClose();

        Screen next = Minecraft.getInstance().screen;

        if (!(next instanceof DreamEaterMenu || next instanceof ChangeSpirit || next instanceof CreateSpirit)) {
            MusicManager.stop();
        }
    }

    protected void action(String string) {
        if (string.equals("back")) {
            MusicManager.stop();
            PacketHandler.sendToServer(new CSOpenMenu());
        }
        if (string.equals("changeSpirit")) {
            minecraft.setScreen(new ChangeSpirit());
        }
        if (string.equals("createSpirit")) {
            minecraft.setScreen(new CreateSpirit());
        }
        if (string.equals("abilityLinks")) {
            minecraft.setScreen(new AbilityLinks());
        }
        if (string.equals("wip")) {
            minecraft.player.playSound(ModSounds.error.get());
        }
    }

    @Override
    public void init() {
        super.init();

        Minecraft mc = Minecraft.getInstance();
        MusicManager.start();


        this.renderables.clear();

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight + 5;
        int button_stats_playerY = button_statsY;

        float buttonPosX = (float) width * 0.03F;
        float subButtonPosX = buttonPosX + 10;

        float buttonWidth = ((float) width * 0.1744F) + 10;
        float subButtonWidth = buttonWidth - 10;


        float dataWidth = ((float) width * 0.1744F) - 10;

        int col1X = (int) (subButtonPosX + buttonWidth + 40), col2X = (int) (col1X + dataWidth * 2) + 10;

        int i = 0;

        int c = 0;
        int d = 0;
        int spacer = 14;

        addRenderableWidget(changeSpirit = new MenuButton((int) buttonPosX, button_statsY + 18 * i++, (int) buttonWidth, "Change Spirit", MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("changeSpirit");
        }));
        addRenderableWidget(createSpirit = new MenuButton((int) buttonPosX, button_statsY + 18 * i++, (int) buttonWidth, "Create Spirit", MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("createSpirit");
        }));
        addRenderableWidget(abilityLinks = new MenuButton((int) buttonPosX, button_statsY + 18 * i++, (int) buttonWidth, "Ability Links", MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("abilityLinks");
        }));
        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, button_statsY + 18 * i++, (int) buttonWidth, (Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("back");
        }));


        // Display Dream Eater Information
        GlobalDataRM global = ModDataRM.getGlobal(minecraft.player);
        PlayerData playerData = PlayerData.get(minecraft.player);

        if (global != null && playerData != null) {
            DreamEater dreamEater = getEquippedDreamEater(global);

            if (dreamEater == null || StringsRM.none.equals(dreamEater.getName())) {
                addRenderableWidget(name = new MenuColourBox(
                        col1X,
                        button_statsY + (c++ * spacer),
                        (int) dataWidth,
                        "Name:",
                        "N/A",
                        0xffffff
                ));
                return;
            }

            DreamEaterDisplayStats stats = getDisplayStats(global, playerData, dreamEater);

            addRenderableWidget(name = new MenuColourBox(
                    col1X,
                    button_statsY + (c++ * spacer),
                    (int) dataWidth,
                    "Name:",
                    getDreamEaterDisplayName(dreamEater),
                    0xffffff
            ));

            addRenderableWidget(level = new MenuColourBox(
                    col1X,
                    button_statsY + (c++ * spacer),
                    (int) dataWidth,
                    "Level:",
                    "" + stats.level,
                    0xffffff
            ));

            addRenderableWidget(spiritHP = new MenuColourBox(
                    col1X,
                    button_statsY + (c++ * spacer),
                    (int) dataWidth,
                    "Max HP:",
                    formatStat(stats.maxHP),
                    0x31bf14
            ));

            addRenderableWidget(spiritSTR = new MenuColourBox(
                    col1X,
                    button_statsY + (c++ * spacer),
                    (int) dataWidth,
                    "STR:",
                    formatStat(stats.strength),
                    0xbf1414
            ));

            addRenderableWidget(spiritMAG = new MenuColourBox(
                    col1X,
                    button_statsY + (c++ * spacer),
                    (int) dataWidth,
                    "MAG:",
                    formatStat(stats.magic),
                    0x000088
            ));

            addRenderableWidget(spiritDEF = new MenuColourBox(
                    col1X,
                    button_statsY + (c++ * spacer),
                    (int) dataWidth,
                    "DEF:",
                    formatStat(stats.defense),
                    0xbf8d14
            ));

            // Known / learned abilities
            addRenderableWidget(abilities = new MenuColourBox(
                    col2X,
                    button_statsY + (d++ * spacer),
                    (int) dataWidth,
                    "Abilities:",
                    "",
                    0xffffff
            ));

            renderDreamEaterAbilities(global, playerData, dreamEater, col2X, button_statsY, dataWidth, spacer, d);
        }



    }

    private DreamEater getEquippedDreamEater(GlobalDataRM global) {
        String dreamEaterRL = global.getDreamEaterRL();

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            return null;
        }

        return ModDreamEaters.registry.get(ResourceLocation.parse(dreamEaterRL));
    }

    private DreamEaterDisplayStats getDisplayStats(GlobalDataRM global, PlayerData playerData, DreamEater dreamEater) {
        int ownerLevel = Math.max(1, playerData.getLevel());

        // If summoned and loaded client-side, use the actual entity for HP/STR/DEF.
        if (global.hasDreamEaterSummoned() && global.getDreamEaterUUID() != null) {
            Entity entity = ClientUtilsRM.getEntityByUUIDClient(global.getDreamEaterUUID());

            if (entity instanceof LivingEntity livingEntity) {
                float maxHP = livingEntity.getMaxHealth();
                float strength = getEntityAttribute(livingEntity, Attributes.ATTACK_DAMAGE, 0F);
                float defense = getEntityAttribute(livingEntity, Attributes.ARMOR, 0F);

                // Minecraft has no vanilla "magic" attribute, so we still resolve MAG by Dream Eater type.
                float magic = getProjectedMagicForDreamEater(dreamEater, playerData, ownerLevel);

                return new DreamEaterDisplayStats(ownerLevel, maxHP, strength, magic, defense);
            }
        }

        // If not summoned or not loaded yet, show projected equipped stats.
        return getProjectedStatsForDreamEater(dreamEater, playerData, ownerLevel);
    }

    private float getEntityAttribute(LivingEntity entity, net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute, float fallback) {
        if (entity.getAttribute(attribute) == null) {
            return fallback;
        }

        return (float) entity.getAttributeValue(attribute);
    }

    private DreamEaterDisplayStats getProjectedStatsForDreamEater(DreamEater dreamEater, PlayerData playerData, int ownerLevel) {
        String name = dreamEater.getName();

        if (StringsRM.chirithy.equals(name)) {
            return getProjectedChirithyStats(playerData, ownerLevel);
        }

        if (StringsRM.meowWow.equals(name)) {
            return getProjectedMeowWowStats(ownerLevel);
        }

        // Future-proof fallback:
        // Unknown Dream Eaters still show something instead of breaking the menu.
        return new DreamEaterDisplayStats(
                ownerLevel,
                (float) playerData.getMaxHP(),
                (float) playerData.getStrengthStat().getStat(),
                (float) playerData.getMagicStat().getStat(),
                (float) playerData.getDefenseStat().getStat()
        );
    }

    private DreamEaterDisplayStats getProjectedChirithyStats(PlayerData playerData, int ownerLevel) {
        float hp = 20F + (playerData.getMaxHP() / 2F);
        float strength = (float) (2F + (playerData.getStrengthStat().getStat() / 5F));
        float magic = (float) (5F + (playerData.getMagicStat().getStat() * 0.75F));
        float defense = (float) (2F + (playerData.getDefenseStat().getStat() / 2F));

        return new DreamEaterDisplayStats(ownerLevel, hp, strength, magic, defense);
    }

    private DreamEaterDisplayStats getProjectedMeowWowStats(int ownerLevel) {
        ownerLevel = Mth.clamp(ownerLevel, 1, 100);

        if (ownerLevel < 3) {
            return new DreamEaterDisplayStats(ownerLevel, 36F, 8.4F, 11.1F, 6.6F);
        }

        if (ownerLevel < 6) {
            return new DreamEaterDisplayStats(ownerLevel, 37F, 12F, 16F, 6F);
        }

        if (ownerLevel < 8) {
            return new DreamEaterDisplayStats(ownerLevel, 46F, 15F, 20F, 8F);
        }

        if (ownerLevel < 10) {
            return new DreamEaterDisplayStats(ownerLevel, 52F, 17F, 22F, 9F);
        }

        if (ownerLevel < 12) {
            return new DreamEaterDisplayStats(ownerLevel, 58F, 19F, 25F, 10F);
        }

        if (ownerLevel < 14) {
            return new DreamEaterDisplayStats(ownerLevel, 63F, 21F, 27F, 11F);
        }

        if (ownerLevel < 16) {
            return new DreamEaterDisplayStats(ownerLevel, 69F, 23F, 30F, 12F);
        }

        if (ownerLevel < 18) {
            return new DreamEaterDisplayStats(ownerLevel, 75F, 24F, 32F, 12F);
        }

        if (ownerLevel < 20) {
            return new DreamEaterDisplayStats(ownerLevel, 81F, 26F, 35F, 13F);
        }

        if (ownerLevel < 22) {
            return new DreamEaterDisplayStats(ownerLevel, 86F, 28F, 37F, 14F);
        }

        if (ownerLevel < 24) {
            return new DreamEaterDisplayStats(ownerLevel, 92F, 30F, 40F, 15F);
        }

        if (ownerLevel < 26) {
            return new DreamEaterDisplayStats(ownerLevel, 98F, 32F, 42F, 16F);
        }

        int extraLevels = ownerLevel - 26;

        float hp = 104F + (extraLevels * 2.5F);
        float strength = 34F + (extraLevels * 0.50F);
        float magic = 45F + (extraLevels * 0.65F);
        float defense = 17F + (extraLevels * 0.25F);

        return new DreamEaterDisplayStats(ownerLevel, hp, strength, magic, defense);
    }

    private float getProjectedMagicForDreamEater(DreamEater dreamEater, PlayerData playerData, int ownerLevel) {
        if (dreamEater == null) {
            return (float) playerData.getMagicStat().getStat();
        }

        String name = dreamEater.getName();

        if (StringsRM.chirithy.equals(name)) {
            return (float) (5F + (playerData.getMagicStat().getStat() * 0.75F));
        }

        if (StringsRM.meowWow.equals(name)) {
            return getProjectedMeowWowStats(ownerLevel).magic;
        }

        return (float) playerData.getMagicStat().getStat();
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

    private String formatStat(float value) {
        if (Math.abs(value - Math.round(value)) < 0.01F) {
            return "" + Math.round(value);
        }

        return String.format("%.1f", value);
    }

    private void renderDreamEaterAbilities(
            GlobalDataRM global,
            PlayerData playerData,
            DreamEater dreamEater,
            int col2X,
            int button_statsY,
            float dataWidth,
            int spacer,
            int startIndex
    ) {
        int d = startIndex;

        if (dreamEater == null) {
            return;
        }

        String name = dreamEater.getName();

        if (StringsRM.chirithy.equals(name)) {
            d = renderChirithyAbilities(global, col2X, button_statsY, dataWidth, spacer, d);
            return;
        }

        if (StringsRM.meowWow.equals(name)) {
            renderMeowWowAbilities(playerData, col2X, button_statsY, dataWidth, spacer, d);
            return;
        }

        addRenderableWidget(abilities = new MenuColourBox(
                col2X,
                button_statsY + (d * spacer),
                (int) dataWidth,
                "No listed abilities",
                "",
                0x7a8487
        ));
    }

    private int renderChirithyAbilities(GlobalDataRM global, int col2X, int button_statsY, float dataWidth, int spacer, int d) {
        if (global.getLearndedMagics().containsKey(Strings.Magic_Cure)
                || global.getLearndedMagics().containsKey(Strings.Magic_Cura)
                || global.getLearndedMagics().containsKey(Strings.Magic_Curaga)) {

            switch (global.getLearnedMagicLevel(ResourceLocation.parse(Strings.Magic_Cure))) {
                case 2:
                    addRenderableWidget(abilities = new MenuColourBox(col2X, button_statsY + (d++ * spacer), (int) dataWidth, "Curaga", "LEARNED", 0x7a8487));
                    break;
                case 1:
                    addRenderableWidget(abilities = new MenuColourBox(col2X, button_statsY + (d++ * spacer), (int) dataWidth, "Cura", "LEARNED", 0x7a8487));
                    break;
                default:
                    addRenderableWidget(abilities = new MenuColourBox(col2X, button_statsY + (d++ * spacer), (int) dataWidth, "Cure", "LEARNED", 0x7a8487));
                    break;
            }
        } else {
            addRenderableWidget(abilities = new MenuColourBox(col2X, button_statsY + (d++ * spacer), (int) dataWidth, "Cure", "Requires Cure Unlocked", 0x000000));
        }

        if (global.getLearndedMagics().containsKey(KingdomKeysReMind.MODID + ":magic_esuna")) {
            addRenderableWidget(abilities = new MenuColourBox(col2X, button_statsY + (d++ * spacer), (int) dataWidth, "Esuna", "LEARNED", 0x7a8487));
        } else {
            addRenderableWidget(abilities = new MenuColourBox(col2X, button_statsY + (d++ * spacer), (int) dataWidth, "Esuna", "Requires Esuna Unlocked", 0x000000));
        }

        if (global.getLearndedMagics().containsKey(Strings.Magic_Aero)) {
            switch (global.getLearnedMagicLevel(ResourceLocation.parse(Strings.Magic_Aero))) {
                case 2:
                    addRenderableWidget(abilities = new MenuColourBox(col2X, button_statsY + (d++ * spacer), (int) dataWidth, "Aeroga", "LEARNED", 0x7a8487));
                    break;
                case 1:
                    addRenderableWidget(abilities = new MenuColourBox(col2X, button_statsY + (d++ * spacer), (int) dataWidth, "Aerora", "LEARNED", 0x7a8487));
                    break;
                default:
                    addRenderableWidget(abilities = new MenuColourBox(col2X, button_statsY + (d++ * spacer), (int) dataWidth, "Aero", "LEARNED", 0x7a8487));
                    break;
            }
        } else {
            addRenderableWidget(abilities = new MenuColourBox(col2X, button_statsY + (d++ * spacer), (int) dataWidth, "Aero", "Requires Aero Unlocked", 0x000000));
        }

        if (global.getLearndedMagics().containsKey(KingdomKeysReMind.MODID + ":magic_auto-life")) {
            addRenderableWidget(abilities = new MenuColourBox(col2X, button_statsY + (d++ * spacer), (int) dataWidth, "Auto-Life", "LEARNED", 0x7a8487));
        } else {
            addRenderableWidget(abilities = new MenuColourBox(col2X, button_statsY + (d++ * spacer), (int) dataWidth, "Auto-Life", "Requires Auto-Life Unlocked", 0x000000));
        }

        return d;
    }

    private void renderMeowWowAbilities(PlayerData playerData, int col2X, int button_statsY, float dataWidth, int spacer, int d) {
        int level = Math.max(1, playerData.getLevel());

        addRenderableWidget(abilities = new MenuColourBox(
                col2X,
                button_statsY + (d++ * spacer),
                (int) dataWidth,
                getMeowWowCureName(level),
                "Lv " + getMeowWowCureUnlockLevel(level),
                0x7a8487
        ));

        addRenderableWidget(abilities = new MenuColourBox(
                col2X,
                button_statsY + (d++ * spacer),
                (int) dataWidth,
                getMeowWowBalloonName(level),
                "Lv " + getMeowWowBalloonUnlockLevel(level),
                0x7a8487
        ));

        addRenderableWidget(abilities = new MenuColourBox(
                col2X,
                button_statsY + (d++ * spacer),
                (int) dataWidth,
                "Slow",
                "Lv 1",
                0x7a8487
        ));
    }

    private String getMeowWowCureName(int level) {
        if (level >= 20) {
            return "Curaga";
        }

        if (level >= 10) {
            return "Cura";
        }

        return "Cure";
    }

    private int getMeowWowCureUnlockLevel(int level) {
        if (level >= 20) {
            return 20;
        }

        if (level >= 10) {
            return 10;
        }

        return 1;
    }

    private String getMeowWowBalloonName(int level) {
        if (level >= 25) {
            return "Balloonga";
        }

        if (level >= 16) {
            return "Balloonra";
        }

        return "Balloon";
    }

    private int getMeowWowBalloonUnlockLevel(int level) {
        if (level >= 25) {
            return 25;
        }

        if (level >= 16) {
            return 16;
        }

        return 1;
    }

    private static class DreamEaterDisplayStats {
        private final int level;
        private final float maxHP;
        private final float strength;
        private final float magic;
        private final float defense;

        private DreamEaterDisplayStats(int level, float maxHP, float strength, float magic, float defense) {
            this.level = level;
            this.maxHP = maxHP;
            this.strength = strength;
            this.magic = magic;
            this.defense = defense;
        }
    }


}

