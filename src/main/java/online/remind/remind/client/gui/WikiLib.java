package online.remind.remind.client.gui;

import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;

import java.util.ArrayList;

public class WikiLib {
    public ArrayList<MenuColourBox> keybladesList = new ArrayList<>();
    public ArrayList<MenuColourBox> attackList = new ArrayList<>();
    public ArrayList<MenuColourBox> magicList = new ArrayList<>();
    public ArrayList<MenuColourBox> formsList = new ArrayList<>();
    public ArrayList<MenuColourBox> armorList = new ArrayList<>();
    public ArrayList<MenuColourBox> accessoriesList = new ArrayList<>();
    public ArrayList<MenuColourBox> abilitiesList = new ArrayList<>();
    public ArrayList<MenuColourBox> shotlocksList = new ArrayList<>();

    int x;
    int width;

    public WikiLib(int x, int width){
        this.x = x;
        this.width = width;
    }

    public ElementPairBuilder
            KEYBLADES_HEADER = new ElementPairBuilder(keybladesList).setLeft("Keyblade Name", "Origin", 0xaa190f).setPosition(() -> x).setWidths(() -> width + 70, 5, ()-> width + 90).setRight("Description","", 0xaa190f),
            SANGUINE_GAZE = new ElementPairBuilder(keybladesList).setLeft("Sanguine Gaze", "Xephiro's Keyblade", 0x380000).setPosition(() -> x).setWidths(()-> width + 70, 5,()-> width + 90).setRight("A Keyblade whose focus is Fast Vampiric Strikes","", 0x754e1a),
            PUREBLOOD = new ElementPairBuilder(keybladesList).setLeft("Pureblood", "", 0x380000).setPosition(() -> x).setWidths(()-> width + 70, 5,()-> width + 90).setRight("A Keyblade swelling with Darkness","", 0x754e1a),
            ELEMENTAL_CRESCENDO = new ElementPairBuilder(keybladesList).setLeft("Elemental Crescendo", "Requested by Goblex", 0x380000).setPosition(() -> x).setWidths(()-> width + 70, 5,()-> width + 90).setRight("A Keyblade with a knack for spells","", 0x754e1a),
            GAZING_OMEN = new ElementPairBuilder(keybladesList).setLeft("Gazing Omen", "Requested by RealRegen", 0x380000).setPosition(() -> x).setWidths(()-> width + 70, 5,()-> width + 90).setRight("A Keyblade with deadly elemental strikes","", 0x754e1a),
            CRYSTALS_LIGHT = new ElementPairBuilder(keybladesList).setLeft("Crystal's Light", "Re:Mind Original", 0x380000).setPosition(() -> x).setWidths(()-> width + 70, 5,()-> width + 90).setRight("Inspired by WoL from FINAL FANTASY I","", 0x754e1a),
            BLITZERS_DREAM = new ElementPairBuilder(keybladesList).setLeft("Blitzer's Dream", "Re:Mind Original", 0x380000).setPosition(() -> x).setWidths(()-> width + 70, 5,()-> width + 90).setRight("Inspired by Tidus from FINAL FANTASY X","", 0x754e1a),
            LEGENDS_FANG = new ElementPairBuilder(keybladesList).setLeft("Legend's Fang", "Re:Mind Original", 0x380000).setPosition(() -> x).setWidths(()-> width + 70, 5,()-> width + 90).setRight("Inspired by Jecht from FINAL FANTASY X","", 0x754e1a),
            FIERCE_DEITY_KEY = new ElementPairBuilder(keybladesList).setLeft("Fierce Deity Key", "Requested by NolValue", 0x380000).setPosition(() -> x).setWidths(()-> width + 70, 5,()-> width + 90).setRight("Inspired by Fierce Deity Link","", 0x754e1a),
            WRONGFUL_INHERITOR = new ElementPairBuilder(keybladesList).setLeft("Wrongful Inheritor", "Requested by LyricAinu", 0x380000).setPosition(() -> x).setWidths(()-> width + 70, 5,()-> width + 90).setRight("A Keyblade overcharged with Lightning","", 0x754e1a),

            //Attacks
            ATTACK_HEADER = new ElementPairBuilder(attackList).setLeft("Attack Name","Element/Type", 0x6600ff).setPosition(() -> x).setWidths(() -> width + 70, 5, ()-> width + 90).setRight("Description","", 0xd68e2f),

            //Magic
            MAGICS_HEADER = new ElementPairBuilder(magicList).setLeft("Spell Name","Element/Type", 0x6600ff).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Description","", 0xd68e2f),
            ESUNA = new ElementPairBuilder(magicList).setLeft("Esuna", "Buff", 0xB03060).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Removes ALL debuffs!", "Includes stop!","You don't have this spell.","Found via synthesis", 0x754e1a);

    //Magics
            ;

}
