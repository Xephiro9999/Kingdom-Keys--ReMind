package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.lib.StringsRM;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

public class WikiMenu extends MenuBackground {

    public enum Wiki {
        NONE, KEYBLADES, ATTACK, MAGIC, FORMS, ARMOR, ACCESSORIES, ABILITIES, SHOTLOCKS
    }

    MenuScrollBar scrollBar;

    public ArrayList<MenuColourBox> keybladesList = new ArrayList<>();
    public ArrayList<MenuColourBox> attackList = new ArrayList<>();
    public ArrayList<MenuColourBox> magicList = new ArrayList<>();
    public ArrayList<MenuColourBox> formsList = new ArrayList<>();
    public ArrayList<MenuColourBox> armorList = new ArrayList<>();
    public ArrayList<MenuColourBox> accessoriesList = new ArrayList<>();
    public ArrayList<MenuColourBox> abilitiesList = new ArrayList<>();
    public ArrayList<MenuColourBox> shotlocksList = new ArrayList<>();

    private Wiki activePage = Wiki.NONE;

    private MenuButton backButton, attack, magic, forms, armor, accessory, shotlock, keyblades, ability;

    public WikiMenu(String name, Color rgb) {
        super(name, rgb);
    }

    public WikiMenu() {
        super("Journal - Re:Mind", new Color(44, 196, 168));
        minecraft = Minecraft.getInstance();
    }

    private void setPage(Wiki page) {
        this.activePage = page;
        items.clear();
    }

    protected void action(String string) {
        switch (string) {
            case "back" -> PacketHandler.sendToServer(new CSOpenMenu());
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

    private void addKeyblades(int col1X, int y, float width) {

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight;
        int button_stats_playerY = button_statsY;

        float buttonPosX = (float) width * 0.03F;
        float subButtonPosX = buttonPosX + 10;

        float buttonWidth = ((float) width * 0.1744F);
        float subButtonWidth = buttonWidth - 10;


        float dataWidth = ((float) width * 0.1744F);

        int col2X = (int) (col1X + dataWidth * 2);

        int i = 0;


        int c = 0;
        int d = 0;
        int spacer = 14;

        keybladesList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Keyblade Name"), "Origin", 0xaa190f));
        keybladesList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Description:"), "", 0xd68e2f));

        keybladesList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Sanguine Gaze"), "Xephiro's Keyblade", 0x380000));
        keybladesList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("A Keyblade whose focus is Fast Vampiric Strikes"), "", 0x754e1a));

        keybladesList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Pureblood"), "", 0x380000));
        keybladesList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("A Keyblade swelling with Darkness"), "", 0x754e1a));

        keybladesList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Elemental Crescendo"), "Requested by Goblex", 0x380000));
        keybladesList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("A Keyblade with a knack for spells"), "", 0x754e1a));

        keybladesList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Gazing Omen"), "Requested by RealRegen", 0x380000));
        keybladesList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("A Keyblade with deadly elemental strikes"), "", 0x754e1a));

        keybladesList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Crystal's Light"), "Re:Mind Original", 0x380000));
        keybladesList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Inspired by WoL from FINAL FANTASY I"), "", 0x754e1a));

        keybladesList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Blitzer's Dream"), "Re:Mind Original", 0x380000));
        keybladesList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Inspired by Tidus from FINAL FANTASY X"), "", 0x754e1a));

        keybladesList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Legend's Fang"), "Re:Mind Original", 0x380000));
        keybladesList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Inspired by Jecht from FINAL FANTASY X"), "", 0x754e1a));

        keybladesList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Fierce Deity Key"), "Requested by NolValue", 0x380000));
        keybladesList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Inspired by Fierce Deity Link"), "", 0x754e1a));

        keybladesList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Wrongful Inheritor"), "Requested by LyricAinu", 0x380000));
        keybladesList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("A Keyblade overcharged with Lightning"), "", 0x754e1a));

    }

    private void addAttacks(int col1X, int y, float width) {

        final PlayerData playerData = PlayerData.get(minecraft.player);

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight;
        int button_stats_playerY = button_statsY;

        float buttonPosX = (float) width * 0.03F;
        float subButtonPosX = buttonPosX + 10;

        float buttonWidth = ((float) width * 0.1744F);
        float subButtonWidth = buttonWidth - 10;


        float dataWidth = ((float) width * 0.1744F);

        int col2X = (int) (col1X + dataWidth * 2);

        int i = 0;


        int c = 0;
        int d = 0;
        int spacer = 14;

        attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Attack Name:"), "Element/Type:", 0x6600ff));
        attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Description:"), "", 0xd68e2f));

// D Tier Spells
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_quick_blitz"))) {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Quick Blitz"), "Physical", 0x8f0303));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal(""), "", 0xC47A2C));
        } else {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this command."), "Found via Synthesis", 0x232324));
        }

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_sliding_dash"))) {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Sliding Dash"), "Physical", 0x8f0303));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal(""), "", 0xC47A2C));
        } else {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }

// C Tier Spells
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_fire_surge"))) {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Fire Surge"), "Fire", 0xE6452D));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal(""), "", 0xC47A2C));
        } else {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_blizzard_surge"))) {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Blizzard Surge"), "Ice", 0x7FDBFF));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal(""), "", 0xC47A2C));
        } else {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_thunder_surge"))) {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Thunder Surge"), "Lightning", 0xF7E600));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal(""), "", 0xC47A2C));
        } else {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_water_surge"))) {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Water Surge"), "Water", 0x1CA9C9));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal(""), "", 0xC47A2C));
        } else {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_aero_surge"))) {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Aero Surge"), "Air", 0xBEEFFF));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal(""), "", 0xC47A2C));

        } else {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_light_surge"))) {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Light Surge"), "Light", 0xFFF2A8));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal(""), "", 0xC47A2C));

        } else {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_dark_surge"))) {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Dark Surge"), "Darkness", 0x2A0A3D));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal(""), "", 0xC47A2C));

        } else {
            attackList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            attackList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this command."), "Found via Synthesis", 0x232324));
        }
    }

    private void addMagics(int col1X, int y, float width) {

        final PlayerData playerData = PlayerData.get(minecraft.player);

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight;
        int button_stats_playerY = button_statsY;

        float buttonPosX = (float) width * 0.03F;
        float subButtonPosX = buttonPosX + 10;

        float buttonWidth = ((float) width * 0.1744F);
        float subButtonWidth = buttonWidth - 10;


        float dataWidth = ((float) width * 0.1744F);

        int col2X = (int) (col1X + dataWidth * 2);

        int i = 0;


        int c = 0;
        int d = 0;
        int spacer = 14;

        magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Spell Name:"), "Element/Type:", 0x6600ff));
        magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Description:"), "", 0xd68e2f));

        // D Tier Spells
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_esuna"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Esuna"), "Buff", 0xB03060));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Removes ALL debuffs!"), "Includes Stop from KK!", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_dispel"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Dispel"), "Debuff", 0xB03060));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Removes ALL buffs from enemies!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }

        // C Tier Spells
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_haste"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Haste/Hastera/Hastega"), "Buff", 0x4CD964));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("A speed up to you and your allies!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_slow"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Slow/Slowra/Slowga"), "Debuff", 0xB03060));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("A AoE slow-down to your enemies!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_steal"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Steal"), "N/A", 0xCFCFCF));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Steals from your foe! [WIP]"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_spark"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Spark/Sparkra/Sparkga"), "Light", 0xFFF2A8));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Surrounds you with orbs of Light!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }

        // B Tier Spells
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_berserk"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Berserk/Berserkra/Berserkga"), "Buff", 0x4CD964));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Gives strength in exchange for defense!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_drain"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Drain/Drainra/Drainga"), "N/A", 0xCFCFCF));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Steals HP from your foe!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_osmose"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Osmose/Osmosera/Osmosega"), "N/A", 0xCFCFCF));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Steals MP from your foe!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }

        // A Tier Spells
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_silence"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Silence/Silencera/Silencega"), "Debuff", 0xB03060));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Prevents others from casting magic!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_holy"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Holy/Holyra/Holyga"), "Light", 0xFFF2A8));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Shoots orbs of piercing Light!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_ruin"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Ruin/Ruinra/Ruinga"), "Darkness", 0x2A0A3D));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Shoots an orb of exploding Darkness!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_balloon"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Balloon/Balloonra/Balloonga"), "Water", 0x1CA9C9));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Bounces and splashes your enemies!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }

        // S Tier Spells
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_auto-life"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Auto-Life"), "Buff", 0x4CD964));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Saves you from death itself!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_mine_shield"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Mine Shield"), "Fire", 0xE6452D));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Places mines that explode after a while!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_mine_square"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Mine Square"), "Fire", 0xE6452D));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Places mines that explode after a while!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_regen"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Regen/Regenra/Regenga"), "Buff", 0x4CD964));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Gradually restores HP, MP/Focus at higher levels"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_faith"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Faith"), "Light", 0xFFF2A8));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Rains down piercing Light!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }

        // SS Tier Spells
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_comet"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Comet/Meteor"), "Darkness", 0x2A0A3D));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Rain down the stars upon your enemies!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }

        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_warp"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Warp"), "N/A", 0xCFCFCF));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Chance to TP foe afar or kill them!"), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Crafting", 0x232324));

            if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_confuse"))) {
                magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Confuse"), "Debuff", 0xB03060));
                magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("Disorientates your foes!"), "", 0x754e1a));
            } else {
                magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
                magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
            }
        }

        // SSS Tier Spells
        if (playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_ultima"))) {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Ultima"), "N/A", 0xCFCFCF));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("The ultimate spell in range and destruction."), "", 0x754e1a));
        } else {
            magicList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            magicList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 90, Utils.translateToLocal("You don't have this spell."), "Found via Synthesis", 0x232324));
        }
    }

    private void addDrives(int col1X, int y, float width) {

        final PlayerData playerData = PlayerData.get(minecraft.player);

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight;
        int button_stats_playerY = button_statsY;

        float buttonPosX = (float) width * 0.03F;
        float subButtonPosX = buttonPosX + 10;

        float buttonWidth = ((float) width * 0.1744F);
        float subButtonWidth = buttonWidth - 10;


        float dataWidth = ((float) width * 0.1744F);

        int col2X = (int) (col1X + dataWidth * 2);

        int i = 0;


        int c = 0;
        int d = 0;
        int spacer = 14;

        formsList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Drive Form:"), "Ability:", 0xfefc6a));
        formsList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 70, Utils.translateToLocal("Description:"), "", 0xd68e2f));

        if (playerData.getDriveFormLevel(KingdomKeysReMind.MODID + ":" + StringsRM.lightForm) > 0) {
            formsList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Light Form"), "Way to Light", 0xFFF2A8));
            formsList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 70, Utils.translateToLocal(""), " Level by defeating enemies and using the RC", 0xC47A2C));
        } else {
            formsList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            formsList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 70, Utils.translateToLocal(""), "Found via Synthesis or Keyblade", 0x232324));
        }
        if (playerData.getDriveFormLevel(KingdomKeysReMind.MODID + ":" + StringsRM.darkForm) > 0) {
            formsList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Dark Form"), "Dark Power", 0x2A0A3D));
            formsList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 70, Utils.translateToLocal(""), " Level by defeating enemies and using the RC", 0xC47A2C));
        } else {
            formsList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            formsList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 70, Utils.translateToLocal(""), "Found via Synthesis or Keyblade", 0x232324));
        }

        if (playerData.getDriveFormLevel(KingdomKeysReMind.MODID + ":" + StringsRM.rageForm) > 0) {
            formsList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Rage Form"), "Rage Awakened", 0x8f0303));
            formsList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 70, Utils.translateToLocal(""), "", 0xC47A2C));
        } else {
            formsList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            formsList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 70, Utils.translateToLocal(""), "Found via Synthesis or Keyblade", 0x232324));
        }
        if (playerData.getDriveFormLevel(KingdomKeysReMind.MODID + ":" + StringsRM.twilight) > 0) {
            formsList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Twilight Form"), "Road to Dawn", 0x9E9E9E));
            formsList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 70, Utils.translateToLocal(""), "Defeat Bosses and use the RC", 0xC47A2C));
        } else {
            formsList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("????"), "????", 0x232324));
            formsList.add(new MenuColourBox(col2X + 400, button_statsY + (c++ * spacer), (int) width + 70, Utils.translateToLocal(""), "Found via ??????", 0x232324));
        }
    }

    int scrollTop, scrollBot;

    @Override
    public void init() {
        super.init();
        this.renderables.clear();
        this.items.clear();
        keybladesList.clear();
        attackList.clear();
        magicList.clear();
        abilitiesList.clear();
        formsList.clear();
        armorList.clear();
        accessoriesList.clear();
        shotlocksList.clear();

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight + 5;
        int button_stats_playerY = button_statsY;

        float buttonPosX = (float) width * 0.03F;
        float subButtonPosX = buttonPosX + 10;

        float buttonWidth = ((float) width * 0.1744F) - 20;
        float subButtonWidth = buttonWidth - 10;

        float dataWidth = ((float) width * 0.1744F) - 10;

        int col1X = (int) (subButtonPosX + buttonWidth + 40), col2X = (int) (col1X + dataWidth * 2) + 10;

        int i = 0;

        scrollTop = (int) topBarHeight;
        scrollBot = (int) (scrollTop + middleHeight);
        scrollBar = new MenuScrollBar(width - 17, scrollTop, scrollBot, (int) middleHeight, 0);

        addRenderableWidget(scrollBar);


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

        addKeyblades(col1X - 210, button_statsY, width * 0.25F);
        addAttacks(col1X - 210, button_statsY, dataWidth);
        addMagics(col1X - 210, button_statsY, dataWidth);
        addDrives(col1X - 210, button_statsY, dataWidth);
    }

    ArrayList<MenuColourBox> items = new ArrayList<>();

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        items = (ArrayList<MenuColourBox>) getListFromPage().clone();

        for (int i = 0; i < items.size(); i += 2) {
            //Left col
            if (items.get(i) != null) {
                items.get(i).visible = true;
                items.get(i).active = false;
                //items.get(i).setX((int)(width*0.3F));
                items.get(i).setY((int) (topBarHeight) + (i) * 7 + 2);
            }
            if (i + 1 < items.size()) {
                if (items.get(i + 1) != null) {
                    items.get(i + 1).visible = true;
                    items.get(i + 1).active = false;
                    items.get(i + 1).setX(items.get(i).getX() + items.get(i).getWidth() + 10);
                    items.get(i + 1).setY((int) (topBarHeight) + (i) * 7 + 2);
                }
            }
        }
        if (!items.isEmpty()) {
            int listHeight = (items.get(items.size() - 1).getY() + 20) - items.get(0).getY() + 3;
            scrollBar.setContentHeight(listHeight);
        }

        gui.enableScissor(0, (int) topBarHeight, width, (int) (topBarHeight + middleHeight));
        for (MenuColourBox item : items) {
            if (item != null) {
                item.setY((int) (item.getY() - scrollBar.scrollOffset));
                if (item.getY() < scrollBar.getBottom() && item.getY() >= scrollBar.getY() - 20) {
                    item.active = true;
                    item.render(gui, mouseX, mouseY, partialTicks);
                }
            }
        }
        gui.disableScissor();

        super.render(gui, mouseX, mouseY, partialTicks);
    }

    private ArrayList<MenuColourBox> getListFromPage() {
        switch (activePage) {
            case NONE -> {
                return new ArrayList();
            }
            case KEYBLADES -> {
                return keybladesList;
            }
            case ATTACK -> {
                return attackList;
            }
            case MAGIC -> {
                return magicList;
            }
            case FORMS -> {
                return formsList;
            }
            case ARMOR -> {
                return armorList;
            }
            case ACCESSORIES -> {
                return accessoriesList;
            }
            case ABILITIES -> {
                return abilitiesList;
            }
            case SHOTLOCKS -> {
                return shotlocksList;
            }
        }
        return new ArrayList();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        scrollBar.mouseClicked(mouseX, mouseY, mouseButton);
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        scrollBar.mouseReleased(pMouseX, pMouseY, pButton);

        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        scrollBar.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);

        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }


    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        scrollBar.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
        return false;
    }

}
