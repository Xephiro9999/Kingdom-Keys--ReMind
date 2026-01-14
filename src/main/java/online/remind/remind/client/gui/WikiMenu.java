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

   /* public ArrayList<MenuColourBox> keybladesList = new ArrayList<>();
    public ArrayList<MenuColourBox> attackList = new ArrayList<>();
    public ArrayList<MenuColourBox> magicList = new ArrayList<>();
    public ArrayList<MenuColourBox> formsList = new ArrayList<>();
    public ArrayList<MenuColourBox> armorList = new ArrayList<>();
    public ArrayList<MenuColourBox> accessoriesList = new ArrayList<>();
    public ArrayList<MenuColourBox> abilitiesList = new ArrayList<>();
    public ArrayList<MenuColourBox> shotlocksList = new ArrayList<>();
*/
    private Wiki activePage = Wiki.NONE;
    WikiLib wikiLib;
    PlayerData playerData;

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

    private void addKeybladeElements() {
        wikiLib.KEYBLADES_HEADER.add();
        wikiLib.SANGUINE_GAZE.add();
        wikiLib.PUREBLOOD.add();
        wikiLib.ELEMENTAL_CRESCENDO.add();
        wikiLib.GAZING_OMEN.add();
        wikiLib.CRYSTALS_LIGHT.add();
        wikiLib.BLITZERS_DREAM.add();
        wikiLib.LEGENDS_FANG.add();
        wikiLib.FIERCE_DEITY_KEY.add();
        wikiLib.WRONGFUL_INHERITOR.add();
    }

    private void addAttackElements() {
        wikiLib.ATTACK_HEADER.add();
        boolean condition = playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_quick_blitz"));
        wikiLib.QUICK_BLITZ.setCondition(condition).add();
        condition = playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_sliding_dash"));
        wikiLib.SLIDING_DASH.setCondition(condition).add();
        condition = playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_fire_surge"));
        wikiLib.FIRE_SURGE.setCondition(condition).add();
        condition = playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_thunder_surge"));
        wikiLib.THUNDER_SURGE.setCondition(condition).add();
        condition = playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_blizzard_surge"));
        wikiLib.BLIZZARD_SURGE.setCondition(condition).add();
        condition = playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_water_surge"));
        wikiLib.WATER_SURGE.setCondition(condition).add();
        condition = playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_aero_surge"));
        wikiLib.AERO_SURGE.setCondition(condition).add();
        condition = playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_light_surge"));
        wikiLib.LIGHT_SURGE.setCondition(condition).add();
        condition = playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "attack_dark_surge"));
        wikiLib.DARK_SURGE.setCondition(condition).add();
    }

    private void addMagicElements() {
        wikiLib.MAGICS_HEADER.add();
        boolean condition = playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_esuna"));
        wikiLib.ESUNA.setCondition(condition).add();
        condition = playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_dispel"));
        wikiLib.DISPEL.setCondition(condition).add();
        condition = playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_haste"));
        wikiLib.HASTE.setCondition(condition).add();
        condition = playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_slow"));
        wikiLib.SLOW.setCondition(condition).add();
        //TODO rest of them
        /*
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
        condition = playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_auto-life"));
        //addHiddenElementPair(magicList,col1X+200, 0, (int) width + 70,"Auto-Life", "Buff", 0x4CD964, 20, (int) width + 90,"Saves you from death itself!","", 0x754e1a, condition, "You don't have this spell.", "Found via synthesis");
        new ElementPairBuilder(magicList).setLeft("Auto-Life", "Buff", 0x4CD964).setPosition(col1X+200).setWidths((int)width+70, 10,(int)width+90).setRight("Saves you from death itself!", "","You don't have this spell.", "Found via synthesis", 0x754e1a).setCondition(condition).add();


        condition = playerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_mine_shield"));
        //addHiddenElementPair(magicList,col1X+200, 0, (int) width + 70,"Mine Shield", "Fire", 0xE6452D, 20, (int) width + 90,"Places mines that explode after a while!","", 0x754e1a, condition, "You don't have this spell.", "Found via synthesis");
        new ElementPairBuilder(magicList).setLeft("Mine Shield", "Fire", 0x4CD964).setPosition(col1X+200).setWidths((int)width+70, 10,(int)width+90).setRight("Places mines that explode after a while!", "", "You don't have this spell.", "Found via synthesis", 0x754e1a).setCondition(condition).add();

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
        }*/
    }

    private void addDriveElements() {

       /* formsList.add(new MenuColourBox(col1X + 200, button_stats_playerY + (d++ * spacer), (int) width + 70, Utils.translateToLocal("Drive Form:"), "Ability:", 0xfefc6a));
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
        }*/
    }

    int scrollTop, scrollBot;

    @Override
    public void init() {
        super.init();
        this.renderables.clear();
        this.items.clear();

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight + 5;

        float subButtonPosX = (float) width * 0.03F + 10;
        float buttonWidth = ((float) width * 0.1744F) - 20;

        int col1X = (int) (subButtonPosX + buttonWidth + 25);

        scrollTop = (int) topBarHeight;
        scrollBot = (int) (scrollTop + middleHeight);
        scrollBar = new MenuScrollBar(width - 17, scrollTop, scrollBot, (int) middleHeight, 0);

        addRenderableWidget(scrollBar);

        int i = 0;
        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, button_statsY+18*i++, (int) buttonWidth, (Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("back");
        }));

        addRenderableWidget(keyblades = new MenuButton((int) buttonPosX, button_statsY + 18*i++, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Keyblades), MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("keyblades");
        }));

        addRenderableWidget(attack = new MenuButton((int) buttonPosX, button_statsY + 18*i++, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Attack), MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("attack");
        }));

        addRenderableWidget(magic = new MenuButton((int) buttonPosX, button_statsY + 18*i++, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Magic), MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("magic");
        }));
        addRenderableWidget(ability = new MenuButton((int) buttonPosX, button_statsY + 18*i++, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Ability), MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("ability");
        }));
        addRenderableWidget(forms = new MenuButton((int) buttonPosX, button_statsY + 18*i++, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Forms), MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("forms");
        }));
        addRenderableWidget(armor = new MenuButton((int) buttonPosX, button_statsY + 18*i++, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Armor), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("armor");
        }));
        addRenderableWidget(accessory = new MenuButton((int) buttonPosX, button_statsY + 18*i++, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Accessories), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("accessory");
        }));
        addRenderableWidget(shotlock = new MenuButton((int) buttonPosX, button_statsY + 18*i++, (int) buttonWidth, (StringsRM.Gui_Menu_Button_Shotlocks), MenuButton.ButtonType.BUTTON, true, (e) -> {
            action("shotlock");
        }));

        playerData = PlayerData.get(minecraft.player);
        wikiLib = new WikiLib(col1X, (int)(width*0.25));

        //Just in case we clear them even tho they should be empty when creating the instance
        wikiLib.keybladesList.clear();
        wikiLib.attackList.clear();
        wikiLib.magicList.clear();
        wikiLib.abilitiesList.clear();
        wikiLib.formsList.clear();
        wikiLib.armorList.clear();
        wikiLib.accessoriesList.clear();
        wikiLib.shotlocksList.clear();

        addKeybladeElements();
        addAttackElements();
        addMagicElements();
        addDriveElements();
    }

    ArrayList<MenuColourBox> items = new ArrayList<>();

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        super.render(gui, mouseX, mouseY, partialTicks);

        items = (ArrayList<MenuColourBox>) getListFromPage().clone();

        for (int i = 0; i < items.size(); i += 2) {
            //Left col
            if (items.get(i) != null) {
                items.get(i).visible = true;
                items.get(i).active = false;
                items.get(i).setY((int) (topBarHeight) + (i) * 7 + 2);
            }
            if (i + 1 < items.size()) {
                if (items.get(i + 1) != null) {
                    items.get(i + 1).visible = true;
                    items.get(i + 1).active = false;
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

    }

    private ArrayList<MenuColourBox> getListFromPage() {
        return switch (activePage) {
            case NONE -> new ArrayList();
            case KEYBLADES -> wikiLib.keybladesList;
            case ATTACK -> wikiLib.attackList;
            case MAGIC -> wikiLib.magicList;
            case FORMS -> wikiLib.formsList;
            case ARMOR -> wikiLib.armorList;
            case ACCESSORIES -> wikiLib.accessoriesList;
            case ABILITIES -> wikiLib.abilitiesList;
            case SHOTLOCKS -> wikiLib.shotlocksList;
        };
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