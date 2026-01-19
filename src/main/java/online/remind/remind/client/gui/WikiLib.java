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
            KEYBLADES_HEADER = new ElementPairBuilder(keybladesList).setLeft("Keyblade Name:", "Origin:", 0xaa190f).setPosition(() -> x).setWidths(() -> width + 70, 5, ()-> width + 90).setRight("Description:","", 0xaa190f),
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
            ATTACK_HEADER = new ElementPairBuilder(attackList).setLeft("Attack Name:","Element/Type:", 0x6600ff).setPosition(() -> x).setWidths(() -> width + 70, 5, ()-> width + 90).setRight("Description:","", 0xd68e2f),
            QUICK_BLITZ = new ElementPairBuilder(attackList).setLeft("Quick Blitz","Physical", 0x380000).setPosition(() -> x).setWidths(() -> width + 70, 5, ()-> width + 90).setRight("Perform a jumping attack.","", "","",0xd68e2f),
            SLIDING_DASH = new ElementPairBuilder(attackList).setLeft("Sliding Dash","Physical", 0x380000).setPosition(() -> x).setWidths(() -> width + 70, 5, ()-> width + 90).setRight("Punish faraway enemies with a charging attack.","", "","",0xd68e2f),
            FIRE_SURGE = new ElementPairBuilder(attackList).setLeft("Fire Surge","Fire", 0xE6452D).setPosition(() -> x).setWidths(() -> width + 70, 5, ()-> width + 90).setRight("Rush the enemy while a ring of fire revolves around you.","", "","",0xd68e2f),
            THUNDER_SURGE = new ElementPairBuilder(attackList).setLeft("Thunder Surge","Thunder", 0xF7E600).setPosition(() -> x).setWidths(() -> width + 70, 5, ()-> width + 90).setRight("Rush the enemy while a ring of lightning revolves around you.","", "","",0xd68e2f),
            BLIZZARD_SURGE = new ElementPairBuilder(attackList).setLeft("Blizzard Surge","Ice", 0x7FDBFF).setPosition(() -> x).setWidths(() -> width + 70, 5, ()-> width + 90).setRight("Rush the enemy while a ring of ice revolves around you.","", "","",0xd68e2f),
            WATER_SURGE = new ElementPairBuilder(attackList).setLeft("Water Surge","Water", 0x1CA9C9).setPosition(() -> x).setWidths(() -> width + 70, 5, ()-> width + 90).setRight("Rush the enemy while a ring of water revolves around you.","", "","",0xd68e2f),
            AERO_SURGE = new ElementPairBuilder(attackList).setLeft("Aero Surge","Aero", 0xBEEFFF).setPosition(() -> x).setWidths(() -> width + 70, 5, ()-> width + 90).setRight("Rush the enemy while a ring of air revolves around you.","", "","",0xd68e2f),
            LIGHT_SURGE = new ElementPairBuilder(attackList).setLeft("Light Surge","Light", 0xFFF2A8).setPosition(() -> x).setWidths(() -> width + 70, 5, ()-> width + 90).setRight("Rush the enemy while a ring of light revolves around you.","", "","",0xd68e2f),
            DARK_SURGE = new ElementPairBuilder(attackList).setLeft("Dark Surge","Dark", 0x2A0A3D).setPosition(() -> x).setWidths(() -> width + 70, 5, ()-> width + 90).setRight("Rush the enemy while a ring of darkness revolves around you.","", "","", 0xd68e2f),
            ZANTETSUKEN = new ElementPairBuilder(attackList).setLeft("Zantetsuken","Dark", 0x2A0A3D).setPosition(() -> x).setWidths(() -> width + 70, 5, ()-> width + 90).setRight("Rush with a fast strike! Can instantly kill enemies.","", "","", 0xd68e2f),

            //Magics
            MAGICS_HEADER = new ElementPairBuilder(magicList).setLeft("Spell Name:","Element/Type:", 0x6600ff).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Description:","", 0xd68e2f),
            ESUNA = new ElementPairBuilder(magicList).setLeft("Esuna", "Buff", 0x4CD964).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Removes ALL debuffs!", "Includes stop!","You don't have this spell.","Found via synthesis", 0x754e1a),
            DISPEL = new ElementPairBuilder(magicList).setLeft("Dispel", "Debuff", 0xB03060).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Removes ALL buffs!", "","You don't have this spell.","Found via synthesis", 0x754e1a),
            HASTE = new ElementPairBuilder(magicList).setLeft("Haste", "Buff", 0x4CD964).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Speeds up your movement and attacks.", "","You don't have this spell.","Found via synthesis", 0x754e1a),
            SLOW = new ElementPairBuilder(magicList).setLeft("Slow", "Debuff", 0xB03060).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Slows down your enemies.", "","You don't have this spell.","Found via synthesis", 0x754e1a),
            STEAL = new ElementPairBuilder(magicList).setLeft("Steal", "N/A", 0xCFCFCF).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Steals from your enemies!", "[WIP]","You don't have this spell.","Found via synthesis", 0x754e1a),
            SPARK = new ElementPairBuilder(magicList).setLeft("Spark", "Light", 0xFFF2A8).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Summons circling orbs of light", "","You don't have this spell.","Found via synthesis", 0x754e1a),
            BERSERK = new ElementPairBuilder(magicList).setLeft("Berserk", "Buff", 0x4CD964).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Gives you a boost of strength", "Reduces your defense too","You don't have this spell.","Found via synthesis", 0x754e1a),
            DRAIN = new ElementPairBuilder(magicList).setLeft("Drain", "N/A", 0xCFCFCF).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Drains HP from foes", "Restores some Hunger!","You don't have this spell.","Found via synthesis", 0x754e1a),
            OSMOSE = new ElementPairBuilder(magicList).setLeft("Osmose", "N/A", 0xCFCFCF).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Drains MP from foes", "","You don't have this spell.","Found via synthesis", 0x754e1a),
            SILENCE = new ElementPairBuilder(magicList).setLeft("Silence", "Debuff", 0xB03060).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Prevents spell casting", "Only works on Players","You don't have this spell.","Found via synthesis", 0x754e1a),
            HOLY = new ElementPairBuilder(magicList).setLeft("Holy", "Light", 0xFFF2A8).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Shoots out orbs of piercing Light", "","You don't have this spell.","Found via synthesis", 0x754e1a),
            RUIN = new ElementPairBuilder(magicList).setLeft("Ruin", "Dark", 0x2A0A3D).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Shoots an explosive orb of Darkness", "","You don't have this spell.","Found via synthesis", 0x754e1a),
            BALLOON = new ElementPairBuilder(magicList).setLeft("Balloon", "Water", 0x1CA9C9).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Sends out bouncing balloons!", "","You don't have this spell.","Found via synthesis", 0x754e1a),
            AUTO_LIFE = new ElementPairBuilder(magicList).setLeft("Auto-Life", "Buff", 0x4CD964).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Saves you from Death itself!", "","You don't have this spell.","Found via synthesis", 0x754e1a),
            MINE_SHIELD = new ElementPairBuilder(magicList).setLeft("Mine Shield", "Fire", 0xE6452D).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Places mines that explode after a while!", "","You don't have this spell.","Found via synthesis", 0x754e1a),
            MINE_SQUARE = new ElementPairBuilder(magicList).setLeft("Mine Square", "Fire", 0xE6452D).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Places mines that explode after a while!", "","You don't have this spell.","Found via synthesis", 0x754e1a),
            REGEN = new ElementPairBuilder(magicList).setLeft("Regen", "Buff", 0x4CD964).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Gradually restores HP", "Higher levels restore MP and FP","You don't have this spell.","Found via synthesis", 0x754e1a),
            FAITH = new ElementPairBuilder(magicList).setLeft("Faith", "Light", 0xFFF2A8).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Slows down your enemies.", "","You don't have this spell.","Found via synthesis", 0x754e1a),
            COMET = new ElementPairBuilder(magicList).setLeft("Comet/Meteor", "Dark", 0x2A0A3D).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Sends a force upon your foe!", "Meteor makes it rain!","You don't have this spell.","Found via synthesis", 0x754e1a),
            WARP = new ElementPairBuilder(magicList).setLeft("Warp", "N/A", 0xCFCFCF).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("????", "","You don't have this spell.","Found via Crafting", 0x754e1a),
            CONFUSE = new ElementPairBuilder(magicList).setLeft("Confuse", "Debuff", 0xB03060).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Disorientates enemies!", "Special effect on PLAYERS!","You don't have this spell.","Found via synthesis", 0x754e1a),
            ULTIMA = new ElementPairBuilder(magicList).setLeft("Ultima", "N/A", 0xCFCFCF).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Unleashes a powerful explosion", "","You don't have this spell.","Found via synthesis", 0x754e1a),
            DEATH = new ElementPairBuilder(magicList).setLeft("Death LV?", "N/A", 0x080808).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Chance to kill a foe", "Increases the lower HP you have","You don't have this spell.","Found via ????", 0x754e1a),

            //Forms
            FORMS_HEADER = new ElementPairBuilder(formsList).setLeft("Form Name:","", 0x6600ff).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("Description:","", 0xd68e2f),
            LIGHT = new ElementPairBuilder(formsList).setLeft("Light", "", 0xFFF2A8).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("", "","You don't have this form.","Found via synthesis", 0x754e1a),
            DARK = new ElementPairBuilder(formsList).setLeft("Dark", "", 0x2A0A3D).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("", "","You don't have this form.","Found via synthesis", 0x754e1a),
            RAGE = new ElementPairBuilder(formsList).setLeft("Rage", "", 0x300000).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("", "","You don't have this form.","Found via synthesis", 0x754e1a),
            TWILIGHT = new ElementPairBuilder(formsList).setLeft("Twilight", "", 0xCFCFCF).setPosition(() -> x).setWidths(() -> width + 50, 5, ()-> width + 70).setRight("", "","You don't have this form.","Found via ??????", 0x754e1a);



}
