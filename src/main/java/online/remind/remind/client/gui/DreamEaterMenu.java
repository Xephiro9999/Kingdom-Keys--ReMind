package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.ClientUtilsRM;
import online.remind.remind.client.gui.dreameaters.AbilityLinks;
import online.remind.remind.client.gui.dreameaters.ChangeSpirit;
import online.remind.remind.client.gui.dreameaters.CreateSpirit;
import online.remind.remind.client.sound.MusicManager;
import online.remind.remind.dreameater.DreamEater;
import online.remind.remind.dreameater.DreamEaterLinkData;
import online.remind.remind.dreameater.ModDreamEaters;
import online.remind.remind.entity.ModEntitiesRM;
import online.remind.remind.entity.spirits.CactuarSpiritEntity;
import online.remind.remind.entity.spirits.ChirithyEntity;
import online.remind.remind.entity.spirits.KomoryBatEntity;
import online.remind.remind.entity.spirits.MeowWowEntity;
import online.remind.remind.lib.StringsRM;

import java.awt.*;

public class DreamEaterMenu extends MenuBackground {

    public DreamEaterMenu(String name, Color rgb) {
        super(name, rgb);
    }

    private LivingEntity previewDreamEaterEntity;
    private String previewDreamEaterKey = "";

    private MenuButton backButton, changeSpirit, createSpirit, abilityLinks;

    MenuColourBox level, exp, spiritHP, spiritSTR, spiritMAG, spiritDEF, name, abilities, none;
    MenuColourBox[] spiritWidgets = {level, exp, spiritHP, spiritSTR, spiritMAG, spiritDEF, name, none};

    public DreamEaterMenu() {
        super("Dream Eaters", new Color(236, 85, 236));
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

        MusicManager.start();

        this.renderables.clear();

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight + 5;

        float buttonPosX = (float) width * 0.03F;
        float subButtonPosX = buttonPosX + 10;

        float buttonWidth = ((float) width * 0.1744F) + 10;
        float dataWidth = ((float) width * 0.1744F) - 10;

        int col1X = (int) (subButtonPosX + buttonWidth + 40);
        int col2X = (int) (col1X + dataWidth * 2) + 10;

        int i = 0;
        int c = 0;
        int d = 0;
        int spacer = 14;

        addRenderableWidget(changeSpirit = new MenuButton(
                (int) buttonPosX,
                button_statsY + 18 * i++,
                (int) buttonWidth,
                "Change Spirit",
                MenuButton.ButtonType.BUTTON,
                true,
                (e) -> action("changeSpirit")
        ));

        addRenderableWidget(createSpirit = new MenuButton(
                (int) buttonPosX,
                button_statsY + 18 * i++,
                (int) buttonWidth,
                "Create Spirit",
                MenuButton.ButtonType.BUTTON,
                false,
                (e) -> action("createSpirit")
        ));

        addRenderableWidget(abilityLinks = new MenuButton(
                (int) buttonPosX,
                button_statsY + 18 * i++,
                (int) buttonWidth,
                "Ability Links",
                MenuButton.ButtonType.BUTTON,
                true,
                (e) -> action("abilityLinks")
        ));

        addRenderableWidget(backButton = new MenuButton(
                (int) buttonPosX,
                button_statsY + 18 * i++,
                (int) buttonWidth,
                Strings.Gui_Menu_Back,
                MenuButton.ButtonType.BUTTON,
                false,
                (e) -> action("back")
        ));

        GlobalDataRM global = ModDataRM.getGlobal(minecraft.player);
        PlayerData playerData = PlayerData.get(minecraft.player);

        if (global == null || playerData == null) {
            return;
        }

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

        String dreamEaterRL = getDreamEaterRL(dreamEater);

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
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

        String expText = stats.expNeeded <= 0
                ? "MAX"
                : stats.exp + " / " + stats.expNeeded;

        addRenderableWidget(exp = new MenuColourBox(
                col1X,
                button_statsY + (c++ * spacer),
                (int) dataWidth,
                "EXP:",
                expText,
                0xAEEAFF
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

        addRenderableWidget(abilities = new MenuColourBox(
                col2X,
                button_statsY + (d++ * spacer),
                (int) dataWidth,
                "Abilities:",
                "",
                0xffffff
        ));

        renderDreamEaterAbilities(
                playerData,
                dreamEater,
                stats.level,
                col2X,
                button_statsY,
                dataWidth,
                spacer,
                d
        );
    }

    private DreamEater getEquippedDreamEater(GlobalDataRM global) {
        String dreamEaterRL = global.getDreamEaterRL();

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            return null;
        }

        try {
            return ModDreamEaters.registry.get(ResourceLocation.parse(dreamEaterRL));
        } catch (Exception e) {
            return null;
        }
    }

    private String getDreamEaterRL(DreamEater dreamEater) {
        if (dreamEater == null || dreamEater.getRegistryName() == null) {
            return "";
        }

        return dreamEater.getRegistryName().toString();
    }

    private boolean isCactuar(DreamEater dreamEater) {
        if (dreamEater == null) {
            return false;
        }

        String name = dreamEater.getName();

        return StringsRM.cactuar.equals(name)
                || "dreameater_cactuar".equals(name)
                || "cactuar".equals(name);
    }

    private DreamEaterDisplayStats getDisplayStats(GlobalDataRM global, PlayerData playerData, DreamEater dreamEater) {
        String dreamEaterRL = getDreamEaterRL(dreamEater);

        int dreamEaterLevel = global.getDreamEaterLevel(dreamEaterRL);
        int dreamEaterExp = global.getDreamEaterExp(dreamEaterRL);
        int dreamEaterExpNeeded = global.getDreamEaterExpToNextLevel(dreamEaterRL);

        if (global.hasDreamEaterSummoned() && global.getDreamEaterUUID() != null) {
            Entity entity = ClientUtilsRM.getEntityByUUIDClient(global.getDreamEaterUUID());

            if (entity instanceof LivingEntity livingEntity) {
                float maxHP = livingEntity.getMaxHealth();
                float strength = getEntityAttribute(livingEntity, Attributes.ATTACK_DAMAGE, 0F);
                float defense = getEntityAttribute(livingEntity, Attributes.ARMOR, 0F);

                /*
                 * Minecraft has no vanilla magic attribute,
                 * so MAG is projected by Dream Eater type.
                 */
                float magic = getProjectedMagicForDreamEater(dreamEater, dreamEaterLevel);

                return new DreamEaterDisplayStats(
                        dreamEaterLevel,
                        dreamEaterExp,
                        dreamEaterExpNeeded,
                        maxHP,
                        strength,
                        magic,
                        defense
                );
            }
        }

        return getProjectedStatsForDreamEater(dreamEater, playerData, dreamEaterLevel, dreamEaterExp, dreamEaterExpNeeded);
    }

    private float getEntityAttribute(
            LivingEntity entity,
            net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute,
            float fallback
    ) {
        if (entity.getAttribute(attribute) == null) {
            return fallback;
        }

        return (float) entity.getAttributeValue(attribute);
    }

    private DreamEaterDisplayStats getProjectedStatsForDreamEater(
            DreamEater dreamEater,
            PlayerData playerData,
            int dreamEaterLevel,
            int dreamEaterExp,
            int dreamEaterExpNeeded
    ) {
        String name = dreamEater.getName();

        if (StringsRM.chirithy.equals(name)) {
            return getProjectedChirithyStats(dreamEaterLevel, dreamEaterExp, dreamEaterExpNeeded);
        }

        if (StringsRM.meowWow.equals(name)) {
            return getProjectedMeowWowStats(dreamEaterLevel, dreamEaterExp, dreamEaterExpNeeded);
        }

        if (StringsRM.komoryBat.equals(name)) {
            return getProjectedKomoryBatStats(dreamEaterLevel, dreamEaterExp, dreamEaterExpNeeded);
        }

        if (isCactuar(dreamEater)) {
            return getProjectedCactuarStats(dreamEaterLevel, dreamEaterExp, dreamEaterExpNeeded);
        }

        return new DreamEaterDisplayStats(
                dreamEaterLevel,
                dreamEaterExp,
                dreamEaterExpNeeded,
                (float) playerData.getMaxHP(),
                (float) playerData.getStrengthStat().getStat(),
                (float) playerData.getMagicStat().getStat(),
                (float) playerData.getDefenseStat().getStat()
        );
    }

    private DreamEaterDisplayStats getProjectedChirithyStats(int level, int currentExp, int expNeeded) {
        level = Mth.clamp(level, 1, GlobalDataRM.DREAM_EATER_MAX_LEVEL);

        float hp = 22 + (float) Math.round((level - 1) * 1.25D);

        float strength = 1F;

        if (level >= 50) {
            strength = 2F;
        }

        if (level >= 90) {
            strength = 3F;
        }

        float magic = 8 + (float) Math.round((level - 1) * 0.55D);
        float defense = 4 + (float) Math.round((level - 1) * 0.35D);

        return new DreamEaterDisplayStats(level, currentExp, expNeeded, hp, strength, magic, defense);
    }

    private DreamEaterDisplayStats getProjectedMeowWowStats(int level, int currentExp, int expNeeded) {
        level = Mth.clamp(level, 1, GlobalDataRM.DREAM_EATER_MAX_LEVEL);

        if (level < 3) {
            return new DreamEaterDisplayStats(level, currentExp, expNeeded, 36F, 8.4F, 11.1F, 6.6F);
        }

        if (level < 6) {
            return new DreamEaterDisplayStats(level, currentExp, expNeeded, 37F, 12F, 16F, 6F);
        }

        if (level < 8) {
            return new DreamEaterDisplayStats(level, currentExp, expNeeded, 46F, 15F, 20F, 8F);
        }

        if (level < 10) {
            return new DreamEaterDisplayStats(level, currentExp, expNeeded, 52F, 17F, 22F, 9F);
        }

        if (level < 12) {
            return new DreamEaterDisplayStats(level, currentExp, expNeeded, 58F, 19F, 25F, 10F);
        }

        if (level < 14) {
            return new DreamEaterDisplayStats(level, currentExp, expNeeded, 63F, 21F, 27F, 11F);
        }

        if (level < 16) {
            return new DreamEaterDisplayStats(level, currentExp, expNeeded, 69F, 23F, 30F, 12F);
        }

        if (level < 18) {
            return new DreamEaterDisplayStats(level, currentExp, expNeeded, 75F, 24F, 32F, 12F);
        }

        if (level < 20) {
            return new DreamEaterDisplayStats(level, currentExp, expNeeded, 81F, 26F, 35F, 13F);
        }

        if (level < 22) {
            return new DreamEaterDisplayStats(level, currentExp, expNeeded, 86F, 28F, 37F, 14F);
        }

        if (level < 24) {
            return new DreamEaterDisplayStats(level, currentExp, expNeeded, 92F, 30F, 40F, 15F);
        }

        if (level < 26) {
            return new DreamEaterDisplayStats(level, currentExp, expNeeded, 98F, 32F, 42F, 16F);
        }

        int extraLevels = level - 26;

        float hp = 104F + (extraLevels * 2.5F);
        float strength = 34F + (extraLevels * 0.50F);
        float magic = 45F + (extraLevels * 0.65F);
        float defense = 17F + (extraLevels * 0.25F);

        return new DreamEaterDisplayStats(level, currentExp, expNeeded, hp, strength, magic, defense);
    }

    private DreamEaterDisplayStats getProjectedKomoryBatStats(int level, int currentExp, int expNeeded) {
        level = Mth.clamp(level, 1, GlobalDataRM.DREAM_EATER_MAX_LEVEL);

        float hp;
        float strength;
        float magic;
        float defense;

        if (level <= 3) {
            hp = (float) (32.7D + ((level - 1) * 0.65D));
            strength = (float) (8.2D + ((level - 1) * 1.9D));
            magic = (float) (10.8D + ((level - 1) * 2.6D));
            defense = (float) (5.9D + ((level - 1) * 0.05D));
        } else {
            int extraLevels = level - 3;

            hp = (float) (34.0D + (extraLevels * 2.15D));
            strength = (float) (12.0D + (extraLevels * 0.48D));
            magic = (float) (16.0D + (extraLevels * 0.62D));
            defense = (float) (6.0D + (extraLevels * 0.22D));
        }

        return new DreamEaterDisplayStats(level, currentExp, expNeeded, hp, strength, magic, defense);
    }

    private DreamEaterDisplayStats getProjectedCactuarStats(int level, int currentExp, int expNeeded) {
        level = Mth.clamp(level, 1, GlobalDataRM.DREAM_EATER_MAX_LEVEL);

        /*
         * Cactuar is meant to be fast, evasive, and needle-focused.
         * Lower HP/DEF than Meow Wow, better STR, low MAG.
         */
        float hp;
        float strength;
        float magic;
        float defense;

        if (level <= 3) {
            hp = (float) (28.0D + ((level - 1) * 0.75D));
            strength = (float) (10.0D + ((level - 1) * 2.0D));
            magic = (float) (4.0D + ((level - 1) * 0.75D));
            defense = (float) (4.0D + ((level - 1) * 0.15D));
        } else {
            int extraLevels = level - 3;

            hp = (float) (30.0D + (extraLevels * 1.85D));
            strength = (float) (14.0D + (extraLevels * 0.60D));
            magic = (float) (5.5D + (extraLevels * 0.28D));
            defense = (float) (4.3D + (extraLevels * 0.18D));
        }

        return new DreamEaterDisplayStats(level, currentExp, expNeeded, hp, strength, magic, defense);
    }

    private float getProjectedMagicForDreamEater(DreamEater dreamEater, int dreamEaterLevel) {
        if (dreamEater == null) {
            return 0F;
        }

        String name = dreamEater.getName();

        if (StringsRM.chirithy.equals(name)) {
            return getProjectedChirithyStats(dreamEaterLevel, 0, 0).magic;
        }

        if (StringsRM.meowWow.equals(name)) {
            return getProjectedMeowWowStats(dreamEaterLevel, 0, 0).magic;
        }

        if (StringsRM.komoryBat.equals(name)) {
            return getProjectedKomoryBatStats(dreamEaterLevel, 0, 0).magic;
        }

        if (isCactuar(dreamEater)) {
            return getProjectedCactuarStats(dreamEaterLevel, 0, 0).magic;
        }

        return 0F;
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

        if (StringsRM.komoryBat.equals(dreamEater.getName())) {
            return "Komory Bat";
        }

        if (isCactuar(dreamEater)) {
            return "Cactuar";
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
            PlayerData playerData,
            DreamEater dreamEater,
            int dreamEaterLevel,
            int col2X,
            int button_statsY,
            float dataWidth,
            int spacer,
            int startIndex
    ) {
        int d = startIndex;

        if (dreamEater == null || playerData == null) {
            return;
        }

        String name = dreamEater.getName();

        if (StringsRM.chirithy.equals(name)) {
            renderLinkDataAbilities(
                    DreamEaterLinkData.getChirithyLinks(),
                    dreamEaterLevel,
                    col2X,
                    button_statsY,
                    dataWidth,
                    spacer,
                    d
            );
            return;
        }

        if (StringsRM.meowWow.equals(name)) {
            renderLinkDataAbilities(
                    DreamEaterLinkData.getMeowWowLinks(),
                    dreamEaterLevel,
                    col2X,
                    button_statsY,
                    dataWidth,
                    spacer,
                    d
            );
            return;
        }

        if (StringsRM.komoryBat.equals(name)) {
            renderLinkDataAbilities(
                    DreamEaterLinkData.getKomoryBatLinks(),
                    dreamEaterLevel,
                    col2X,
                    button_statsY,
                    dataWidth,
                    spacer,
                    d
            );
            return;
        }

        if (isCactuar(dreamEater)) {
            renderLinkDataAbilities(
                    DreamEaterLinkData.getCactuarLinks(),
                    dreamEaterLevel,
                    col2X,
                    button_statsY,
                    dataWidth,
                    spacer,
                    d
            );
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

    private int renderLinkDataAbilities(
            java.util.List<DreamEaterLinkData.LinkEntry> links,
            int dreamEaterLevel,
            int col2X,
            int button_statsY,
            float dataWidth,
            int spacer,
            int d
    ) {
        int level = Math.max(1, dreamEaterLevel);

        for (DreamEaterLinkData.LinkEntry link : links) {
            boolean unlocked = DreamEaterLinkData.isUnlocked(link, level);

            String rightText = unlocked
                    ? link.type()
                    : "Lv " + link.unlockLevel();

            int color = unlocked ? 0x7a8487 : 0x444444;

            addRenderableWidget(abilities = new MenuColourBox(
                    col2X,
                    button_statsY + (d++ * spacer),
                    (int) dataWidth,
                    link.displayName(),
                    rightText,
                    color
            ));
        }

        return d;
    }

    private static class DreamEaterDisplayStats {
        private final int level;
        private final int exp;
        private final int expNeeded;
        private final float maxHP;
        private final float strength;
        private final float magic;
        private final float defense;

        private DreamEaterDisplayStats(
                int level,
                int exp,
                int expNeeded,
                float maxHP,
                float strength,
                float magic,
                float defense
        ) {
            this.level = level;
            this.exp = exp;
            this.expNeeded = expNeeded;
            this.maxHP = maxHP;
            this.strength = strength;
            this.magic = magic;
            this.defense = defense;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderDreamEaterPreview(guiGraphics, mouseX, mouseY);
    }

    private void renderDreamEaterPreview(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (minecraft == null || minecraft.player == null || minecraft.level == null) {
            return;
        }

        GlobalDataRM global = ModDataRM.getGlobal(minecraft.player);
        PlayerData playerData = PlayerData.get(minecraft.player);

        if (global == null || playerData == null) {
            return;
        }

        DreamEater dreamEater = getEquippedDreamEater(global);

        if (dreamEater == null || StringsRM.none.equals(dreamEater.getName())) {
            return;
        }

        LivingEntity entity = getOrCreatePreviewEntity(dreamEater, playerData);

        if (entity == null) {
            return;
        }

        String dreamEaterRL = getDreamEaterRL(dreamEater);

        int dreamEaterLevel = global.getDreamEaterLevel(dreamEaterRL);
        int dreamEaterExp = global.getDreamEaterExp(dreamEaterRL);
        int dreamEaterExpNeeded = global.getDreamEaterExpToNextLevel(dreamEaterRL);

        int boxX = (int) (this.width * 0.45F);
        int boxY = (int) (this.height * 0.24F);
        int boxW = 130;
        int boxH = 145;

        guiGraphics.fill(boxX - 4, boxY - 16, boxX + boxW + 4, boxY + boxH + 4, 0xAA000000);

        guiGraphics.drawString(
                minecraft.font,
                getDreamEaterDisplayName(dreamEater),
                boxX + 8,
                boxY - 12,
                0xFFFFFF,
                false
        );

        guiGraphics.drawString(
                minecraft.font,
                "Lv. " + dreamEaterLevel,
                boxX + 8,
                boxY,
                0xFFD966,
                false
        );

        String expLine = dreamEaterExpNeeded <= 0
                ? "EXP MAX"
                : "EXP " + dreamEaterExp + " / " + dreamEaterExpNeeded;

        guiGraphics.drawString(
                minecraft.font,
                expLine,
                boxX + 8,
                boxY + 10,
                0xffae00,
                false
        );

        if (dreamEaterExpNeeded > 0) {
            int barX = boxX + 8;
            int barY = boxY + 21;
            int barW = boxW - 16;
            int barH = 5;

            float progress = Math.min(1.0F, Math.max(0.0F, dreamEaterExp / (float) dreamEaterExpNeeded));
            int filledW = (int) (barW * progress);

            guiGraphics.fill(barX - 1, barY - 1, barX + barW + 1, barY + barH + 1, 0xAA000000);
            guiGraphics.fill(barX, barY, barX + barW, barY + barH, 0xFF222222);
            guiGraphics.fill(barX, barY, barX + filledW, barY + barH, 0xFFf0ac19);
        }

        int scale = getPreviewScale(dreamEater);

        entity.tickCount = minecraft.player.tickCount;

        InventoryScreen.renderEntityInInventoryFollowsMouse(
                guiGraphics,
                boxX,
                boxY + 24,
                boxX + boxW,
                boxY + boxH,
                scale,
                0.25F,
                mouseX,
                mouseY,
                entity
        );
    }

    private LivingEntity getOrCreatePreviewEntity(DreamEater dreamEater, PlayerData playerData) {
        if (minecraft == null || minecraft.level == null || minecraft.player == null || dreamEater == null) {
            return null;
        }

        String key = dreamEater.getName() + ":" + playerData.getAlignment();

        if (previewDreamEaterEntity != null && key.equals(previewDreamEaterKey)) {
            return previewDreamEaterEntity;
        }

        previewDreamEaterKey = key;
        previewDreamEaterEntity = null;

        boolean isOrg = playerData.getAlignment() != Utils.OrgMember.NONE;

        if (StringsRM.chirithy.equals(dreamEater.getName())) {
            ChirithyEntity chirithy = new ChirithyEntity(ModEntitiesRM.TYPE_CHIRITHY.get(), minecraft.level);
            chirithy.setOwnerUUID(minecraft.player.getUUID());
            chirithy.setVariant(isOrg ? 0 : 1);

            previewDreamEaterEntity = chirithy;
            return previewDreamEaterEntity;
        }

        if (StringsRM.meowWow.equals(dreamEater.getName())) {
            MeowWowEntity meowWow = new MeowWowEntity(ModEntitiesRM.TYPE_MEOW_WOW.get(), minecraft.level);
            meowWow.setOwnerUUID(minecraft.player.getUUID());
            meowWow.setVariant(isOrg ? MeowWowEntity.VARIANT_ORG : MeowWowEntity.VARIANT_NORMAL);

            previewDreamEaterEntity = meowWow;
            return previewDreamEaterEntity;
        }

        if (StringsRM.komoryBat.equals(dreamEater.getName())) {
            KomoryBatEntity komoryBat = new KomoryBatEntity(ModEntitiesRM.TYPE_KOMORY_BAT.get(), minecraft.level);
            komoryBat.setOwnerUUID(minecraft.player.getUUID());
            komoryBat.setVariant(isOrg ? KomoryBatEntity.VARIANT_ORG : KomoryBatEntity.VARIANT_NORMAL);
            komoryBat.setNoGravity(true);

            previewDreamEaterEntity = komoryBat;
            return previewDreamEaterEntity;
        }

        if (isCactuar(dreamEater)) {
            CactuarSpiritEntity cactuar = new CactuarSpiritEntity(ModEntitiesRM.TYPE_CACTUAR_SPIRIT.get(), minecraft.level);
            cactuar.setOwnerUUID(minecraft.player.getUUID());
            cactuar.setNoAi(true);
            cactuar.setNoGravity(false);

            previewDreamEaterEntity = cactuar;
            return previewDreamEaterEntity;
        }

        return null;
    }

    private int getPreviewScale(DreamEater dreamEater) {
        if (dreamEater == null) {
            return 35;
        }

        if (isCactuar(dreamEater)) {
            return 58;
        }

        if (StringsRM.komoryBat.equals(dreamEater.getName())) {
            return 65;
        }

        if (StringsRM.meowWow.equals(dreamEater.getName())) {
            return 42;
        }

        if (StringsRM.chirithy.equals(dreamEater.getName())) {
            return 48;
        }

        return 40;
    }
}