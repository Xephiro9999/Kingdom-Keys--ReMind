package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.magic.ModMagicsRM;

import java.awt.*;

public class WikiMenu extends MenuBackground {

    public enum Wiki{
        NONE,
        KEYBLADES,
        ATTACK,
        MAGIC,
        FORMS,
        ARMOR,
        ACCESSORIES,
        ABILITIES,
        SHOTLOCKS
    }

    private Wiki activePage = Wiki.NONE;

    private MenuButton backButton, attack, magic, forms, armor, accessory, shotlock, keyblades, ability;

    MenuColourBox addedKeyblades, magics, def, acc;

    MenuColourBox[] playerWidgets = {addedKeyblades, magics, def, acc};

    public WikiMenu(String name, Color rgb) {
        super(name, rgb);
    }

    public WikiMenu() {
        super("Journal - Re:Mind", new Color(44, 196, 168));
        minecraft = Minecraft.getInstance();
    }

    private void setPage(Wiki page){
        this.activePage = page;
        this.init();
    }

    protected void action(String string) {
        switch(string) {
            case "back" -> GUIHelperRM.openAddonMenu();
            case "keyblades" -> setPage(Wiki.KEYBLADES);
            case "attack" -> setPage(Wiki.ATTACK);
            case "magic" -> setPage(Wiki.MAGIC);
            case "ability" -> setPage(Wiki.ABILITIES);
            case "forms" -> setPage(Wiki.FORMS);
            case "armor" -> setPage(Wiki.ARMOR);
            case "accessory" -> setPage(Wiki.ACCESSORIES);
            case "shotlock" -> setPage(Wiki.SHOTLOCKS);
        }
    }

    private void renderKeyblades(int x, int y, float width){

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight;
        int button_stats_playerY = button_statsY;

        float buttonPosX = (float) width * 0.03F;
        float subButtonPosX = buttonPosX + 10;

        float buttonWidth = ((float) width * 0.1744F);
        float subButtonWidth = buttonWidth - 10;


        float dataWidth = ((float) width * 0.1744F);

        int col1X = (int) (subButtonPosX + buttonWidth + 40), col2X=(int) (col1X + dataWidth * 2) ;

        int i = 0;


        int c = 0;
        int d = 0;
        int spacer = 14;

        addRenderableWidget(
                new MenuColourBox(
                        col1X + 200, button_stats_playerY + (d++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Keyblade Name"),
                        "Origin",
                        0xaa190f
                )
        );

        // col1X is for Keyblades for now
        addRenderableWidget(
                new MenuColourBox(
                        col1X + 200, button_stats_playerY + (d++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Sanguine Gaze"),
                        "Xephiro's Keyblade",
                        0x380000
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col1X + 200, button_stats_playerY + (d++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Pureblood"),
                        "",
                        0x380000
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col1X + 200, button_stats_playerY + (d++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Elemental Crescendo"),
                        "Requested by Goblex",
                        0x380000
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col1X + 200, button_stats_playerY + (d++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Gazing Omen"),
                        "Requested by RealRegen",
                        0x380000
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col1X + 200, button_stats_playerY + (d++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Crystal's Light"),
                        "Re:Mind Original",
                        0x380000
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col1X + 200, button_stats_playerY + (d++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Blitzer's Dream"),
                        "Re:Mind Original",
                        0x380000
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col1X + 200, button_stats_playerY + (d++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Legend's Fang"),
                        "Re:Mind Original",
                        0x380000
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col1X + 200, button_stats_playerY + (d++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Fierce Deity Key"),
                        "Requested by NolValue",
                        0x380000
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col1X + 200, button_stats_playerY + (d++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Wrongful Inheritor"),
                        "Requested by LyricAinu",
                        0x380000
                )
        );

        // ColX2 is for descriptions
        addRenderableWidget(
                new MenuColourBox(
                        col2X +400, button_statsY + (c++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Description:"),
                        "",
                        0xd68e2f
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col2X +400, button_statsY + (c++ * spacer),
                        (int) width + 90,
                        Utils.translateToLocal("A Keyblade whose focus is Fast Vampiric Strikes"),
                        "",
                        0x754e1a
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col2X + 400, button_statsY + (c++ * spacer),
                        (int) width + 90,
                        Utils.translateToLocal("A Keyblade swelling with Darkness"),
                        "",
                        0x754e1a
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col2X + 400, button_statsY + (c++ * spacer),
                        (int) width + 90,
                        Utils.translateToLocal("A Keyblade with a knack for spells"),
                        "",
                        0x754e1a
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col2X + 400, button_statsY + (c++ * spacer),
                        (int) width + 90,
                        Utils.translateToLocal("A Keyblade with deadly elemental strikes"),
                        "",
                        0x754e1a
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col2X + 400, button_statsY + (c++ * spacer),
                        (int) width + 90,
                        Utils.translateToLocal("Inspired by WoL from FINAL FANTASY I"),
                        "",
                        0x754e1a
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col2X + 400, button_statsY + (c++ * spacer),
                        (int) width + 90,
                        Utils.translateToLocal("Inspired by Tidus from FINAL FANTASY X"),
                        "",
                        0x754e1a
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col2X + 400, button_statsY + (c++ * spacer),
                        (int) width + 90,
                        Utils.translateToLocal("Inspired by Jecht from FINAL FANTASY X"),
                        "",
                        0x754e1a
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col2X + 400, button_statsY + (c++ * spacer),
                        (int) width + 90,
                        Utils.translateToLocal("Inspired by Fierce Deity Link"),
                        "",
                        0x754e1a
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col2X + 400, button_statsY + (c++ * spacer),
                        (int) width + 90,
                        Utils.translateToLocal("A Keyblade overcharged with Lightning"),
                        "",
                        0x754e1a
                )
        );
    }

    private void renderAttack(int x, int y, float width){

        final PlayerData playerData = PlayerData.get(minecraft.player);

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight;
        int button_stats_playerY = button_statsY;

        float buttonPosX = (float) width * 0.03F;
        float subButtonPosX = buttonPosX + 10;

        float buttonWidth = ((float) width * 0.1744F);
        float subButtonWidth = buttonWidth - 10;


        float dataWidth = ((float) width * 0.1744F);

        int col1X = (int) (subButtonPosX + buttonWidth + 40), col2X=(int) (col1X + dataWidth * 2) ;

        int i = 0;


        int c = 0;
        int d = 0;
        int spacer = 14;

        addRenderableWidget(
                new MenuColourBox(
                        col1X + 200, button_stats_playerY + (d++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Attack Name:"),
                        "Element/Type:",
                        0x6600ff
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col2X +400, button_statsY + (c++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Description:"),
                        "",
                        0xd68e2f
                )
        );

        // D Tier Spells

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_quick_blitz"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Quick Blitz"),
                            "Physical",
                            0x8f0303
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal(""),
                            "",
                            0xC47A2C
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this command."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_sliding_dash"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Sliding Dash"),
                            "Physical",
                            0x8f0303
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal(""),
                            "",
                            0xC47A2C
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }

        // C Tier Spells

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_fire_surge"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Fire Surge"),
                            "Fire",
                            0xE6452D
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal(""),
                            "",
                            0xC47A2C
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_blizzard_surge"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Blizzard Surge"),
                            "Ice",
                            0x7FDBFF
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal(""),
                            "",
                            0xC47A2C
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_thunder_surge"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Thunder Surge"),
                            "Lightning",
                            0xF7E600
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal(""),
                            "",
                            0xC47A2C
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_water_surge"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Water Surge"),
                            "Water",
                            0x1CA9C9
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal(""),
                            "",
                            0xC47A2C
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_aero_surge"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Aero Surge"),
                            "Air",
                            0xBEEFFF
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal(""),
                            "",
                            0xC47A2C
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_light_surge"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Light Surge"),
                            "Light",
                            0xFFF2A8
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal(""),
                            "",
                            0xC47A2C
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }

        // SS Tier Spells
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_dark_surge"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Dark Surge"),
                            "Darkness",
                            0x2A0A3D
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal(""),
                            "",
                            0xC47A2C
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this command."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }
    }

    private void renderMagic(int x, int y, float width){

        final PlayerData playerData = PlayerData.get(minecraft.player);

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight;
        int button_stats_playerY = button_statsY;

        float buttonPosX = (float) width * 0.03F;
        float subButtonPosX = buttonPosX + 10;

        float buttonWidth = ((float) width * 0.1744F);
        float subButtonWidth = buttonWidth - 10;


        float dataWidth = ((float) width * 0.1744F);

        int col1X = (int) (subButtonPosX + buttonWidth + 40), col2X=(int) (col1X + dataWidth * 2) ;

        int i = 0;


        int c = 0;
        int d = 0;
        int spacer = 14;

        addRenderableWidget(
                new MenuColourBox(
                        col1X + 200, button_stats_playerY + (d++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Spell Name:"),
                        "Element/Type:",
                        0x6600ff
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col2X +400, button_statsY + (c++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Description:"),
                        "",
                        0xd68e2f
                )
        );

        // D Tier Spells

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_esuna"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Esuna"),
                            "Buff",
                            0xB03060
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Removes ALL debuffs!"),
                            "Includes Stop from KK!",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_dispel"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Dispel"),
                            "Debuff",
                            0xB03060
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Removes ALL buffs from enemies!"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }

        // C Tier Spells

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_haste"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Haste/Hastera/Hastega"),
                            "Buff",
                            0x4CD964
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("A speed up to you and your allies!"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_slow"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Slow/Slowra/Slowga"),
                            "Debuff",
                            0xB03060
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("A AoE slow-down to your enemies!"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_steal"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Steal"),
                            "N/A",
                            0xCFCFCF
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Steals from your foe! [WIP]"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_spark"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Spark/Sparkra/Sparkga"),
                            "Light",
                            0xFFF2A8
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Surrounds you with orbs of Light!"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }

        // B Tier Spells
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_berserk"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Berserk/Berserkra/Berserkga"),
                            "Buff",
                            0x4CD964
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Gives strength in exchange for defense!"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_drain"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Drain/Drainra/Drainga"),
                            "N/A",
                            0xCFCFCF
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Steals HP from your foe!"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_osmose"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Osmose/Osmosera/Osmosega"),
                            "N/A",
                            0xCFCFCF
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Steals MP from your foe!"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }

        // A Tier Spells
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_silence"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Silence/Silencera/Silencega"),
                            "Debuff",
                            0xB03060
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Prevents others from casting magic!"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_holy"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Holy/Holyra/Holyga"),
                            "Light",
                            0xFFF2A8
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Shoots orbs of piercing Light!"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_ruin"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Ruin/Ruinra/Ruinga"),
                            "Darkness",
                            0x2A0A3D
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Shoots an orb of exploding Darkness!"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_balloon"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Balloon/Balloonra/Balloonga"),
                            "Water",
                            0x1CA9C9
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Bounces and splashes your enemies!"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }

        // S Tier Spells
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_auto-life"))) {

            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Auto-Life"),
                            "Buff",
                            0x4CD964
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Saves you from death itself!"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_regen"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Regen/Regenra/Regenga"),
                            "Buff",
                            0x4CD964
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Gradually restores HP, MP/Focus at higher levels"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_faith"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Faith"),
                            "Light",
                            0xFFF2A8
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Rains down piercing Light!"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }

        // SS Tier Spells
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_comet"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Comet/Meteor"),
                            "Darkness",
                            0x2A0A3D
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Rain down the stars upon your enemies!"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_warp"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Warp"),
                            "N/A",
                            0xCFCFCF
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("Chance to TP foe afar or kill them!"),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Crafting",
                            0x232324
                    )
            );
        }
        // SSS Tier Spells

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_ultima"))) {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Ultima"),
                            "N/A",
                            0xCFCFCF
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("The ultimate spell in range and destruction."),
                            "",
                            0x754e1a
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 90,
                            Utils.translateToLocal("You don't have this spell."),
                            "Found via Synthesis",
                            0x232324
                    )
            );
        }
    }

    private void renderDrive(int x, int y, float width){

        final PlayerData playerData = PlayerData.get(minecraft.player);

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight;
        int button_stats_playerY = button_statsY;

        float buttonPosX = (float) width * 0.03F;
        float subButtonPosX = buttonPosX + 10;

        float buttonWidth = ((float) width * 0.1744F);
        float subButtonWidth = buttonWidth - 10;


        float dataWidth = ((float) width * 0.1744F);

        int col1X = (int) (subButtonPosX + buttonWidth + 40), col2X=(int) (col1X + dataWidth * 2) ;

        int i = 0;


        int c = 0;
        int d = 0;
        int spacer = 14;

        addRenderableWidget(
                new MenuColourBox(
                        col1X + 200, button_stats_playerY + (d++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Drive Form:"),
                        "Ability:",
                        0xfefc6a
                )
        );
        addRenderableWidget(
                new MenuColourBox(
                        col2X +400, button_statsY + (c++ * spacer),
                        (int) width + 70,
                        Utils.translateToLocal("Description:"),
                        "",
                        0xd68e2f
                )
        );

        if (playerData.getDriveFormLevel(KingdomKeysReMind.MODID + ":" + StringsRM.lightForm) > 0){
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Light Form"),
                            "Way to Light",
                            0xFFF2A8
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal(""),
                            " Level by defeating enemies and using the RC",
                            0xC47A2C
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal(""),
                            "Found via Synthesis or Keyblade",
                            0x232324
                    )
            );
        }
        if (playerData.getDriveFormLevel(KingdomKeysReMind.MODID + ":" + StringsRM.darkForm) > 0){
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Dark Form"),
                            "Dark Power",
                            0x2A0A3D
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal(""),
                            " Level by defeating enemies and using the RC",
                            0xC47A2C
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal(""),
                            "Found via Synthesis or Keyblade",
                            0x232324
                    )
            );
        }

        if (playerData.getDriveFormLevel(KingdomKeysReMind.MODID + ":" + StringsRM.rageForm) > 0){
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Rage Form"),
                            "Rage Awakened",
                            0x8f0303
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal(""),
                            "",
                            0xC47A2C
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal(""),
                            "Found via Synthesis or Keyblade",
                            0x232324
                    )
            );
        }
        if (playerData.getDriveFormLevel(KingdomKeysReMind.MODID + ":" + StringsRM.twilight) > 0){
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("Twilight Form"),
                            "Road to Dawn",
                            0x9E9E9E
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal(""),
                            "Defeat Bosses and use the RC",
                            0xC47A2C
                    )
            );
        } else {
            addRenderableWidget(
                    new MenuColourBox(
                            col1X + 200, button_stats_playerY + (d++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal("????"),
                            "????",
                            0x232324
                    )
            );
            addRenderableWidget(
                    new MenuColourBox(
                            col2X + 400, button_statsY + (c++ * spacer),
                            (int) width + 70,
                            Utils.translateToLocal(""),
                            "Found via ??????",
                            0x232324
                    )
            );
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




        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, (Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("back");
        }));

        addRenderableWidget(keyblades = new MenuButton((int) buttonPosX, button_statsY + 20, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Keyblades), MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("keyblades");
        }));

        addRenderableWidget(attack = new MenuButton((int) buttonPosX, button_statsY + 40, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Attack), MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("attack");
        }));

        addRenderableWidget(magic = new MenuButton((int) buttonPosX, button_statsY + 60, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Magic), MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("magic");
        }));
        addRenderableWidget(ability = new MenuButton((int) buttonPosX, button_statsY + 80, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Ability), MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("ability");
        }));
        addRenderableWidget(forms = new MenuButton((int) buttonPosX, button_statsY + 100, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Forms), MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("forms");
        }));
        addRenderableWidget(armor = new MenuButton((int) buttonPosX, button_statsY + 120, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Armor), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("armor");
        }));
        addRenderableWidget(accessory = new MenuButton((int) buttonPosX, button_statsY + 140, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Accessories), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("accessory");
        }));
        addRenderableWidget(shotlock = new MenuButton((int) buttonPosX, button_statsY + 160, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Shotlocks), MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("shotlock");
        }));

        // Info Widgets
        int c = 0;
        int d = 0;
        int spacer = 14;

        switch (activePage){
            case KEYBLADES -> renderKeyblades(col2X, button_statsY, dataWidth);
            case ATTACK -> renderAttack(col2X, button_statsY, dataWidth);
            case MAGIC -> renderMagic(col2X,button_statsY, dataWidth);
            case FORMS -> renderDrive(col2X, button_statsY, dataWidth);
        }

    }
}
