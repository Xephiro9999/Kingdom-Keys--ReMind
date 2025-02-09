package online.remind.remind.lib;

import online.remind.remind.KingdomKeysReMind;

public class StringsRM {

	public static final byte
		darkStepType = 0, 
		lightStepType = 1,
        twilightStepType = 2,
        rageStepType = 3,
        orgStepType = 4;
	
    public static final String
            // Prefixes
        ABMA_Prefix = "ability_",
        DFMA_Prefix = "form_",
        KBMA_Prefix = "keyblade_",
        KCMA_Prefix = "keychain_",
        SLMA_Prefix = "shotlock_",
        ARMA_Prefix = "armor_",
        ACMA_Prefix = "accessory_",
        MENU_Prefix = "menu_button",
        RCMA_Prefix = "rc_",


        //Forms
        rageForm = StringsRM.DFMA_Prefix+"rage",
        darkForm = StringsRM.DFMA_Prefix+"dark",
        lightForm = StringsRM.DFMA_Prefix+"light",
        twilight = StringsRM.DFMA_Prefix+"twilight",


        //Ability List
        darkPassage = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"dark_passage",
        darknessBoost = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"darkness_boost",
        lightBoost = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"light_boost",
        mpBoost = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"mp_boost",
        hpBoost = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"hp_boost",
        rageAwakened = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"rage_awakened",
        darkPower = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"dark_power",
        wayToLight = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"way_to_light",
        lightStep = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"light_step",
        darkStep = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"dark_step",
        adrenaline = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"adrenaline",
        critical_surge = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"critical_surge",
        lightWithin = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"light_within",
        darknessWithin = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"darkness_within",
        dedication = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"dedication",
        hpWalker = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"hp_walker",
        mpWalker = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"mp_walker",
        focusWalker = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"focus_walker",
        heartWalker = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"heart_walker",
        expWalker = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"exp_walker",
        mpShield = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"mp_shield",
        vehemence = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"vehemence",
        riskCharge = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"riskcharge",
        attackHaste = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"attack_haste",
        heartsPower = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"hearts_power",
        friendsPower = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"friends_power",
        renewalBlock = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"renewal_block",
        focusBlock = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"focus_block",

        // FF Keyblade Abilities
        Tidus = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"tidus",
        Jecht = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"jecht",

        // Reprisals

        counterHammer = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"counter_hammer",
        counterBlast = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"counter_blast",
        counterRush = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"counter_rush",

        // Shotlocks
        flameSalvo = "flame_salvo",
        bubbleBlaster = "bubble_blaster",

        thunderStorm = "thunderstorm",
        bioBarrage = "bio_barrage",
        meteorShower = "meteor_shower",
        darkDivide = "dark_divide",

        heartlessAngel = "heartless_angel",

        // Reaction Commands
        riskchargeRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"riskcharge",
        ragingBurst = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"raging_burst",
        testRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"test",
        LightBeamRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"light_beam",
        DarkMineRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"dark_mine",
        TwilightRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"twilight",
        DualShotRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"dual_shot",
        DarkFiragaRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"dark_firaga",
        XemnasRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"xemnas",
        XaldinRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"xaldin",
        XigbarRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"xigbar",
        vexenRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"vexen",

        CounterHammerRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"counter_hammer",
        CounterBlastRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"counter_blast",
        CounterRushRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"counter_rush",


        //Keyblades
        xephiroKeyblade = KingdomKeysReMind.MODID+":"+ StringsRM.KBMA_Prefix+"xephiro_keyblade",

        //Keychains
        xephiroKeybladeChain = KingdomKeysReMind.MODID+":"+ StringsRM.KCMA_Prefix+"xephiro_keyblade_chain",

        // KK Armor


        // KK Accessories
        luckOfTheDraw = KingdomKeysReMind.MODID+":"+ StringsRM.ACMA_Prefix+"luck_of_the_draw",
        lightHeart = KingdomKeysReMind.MODID+":"+ StringsRM.ACMA_Prefix+"lightHeart",
        darkHeart = KingdomKeysReMind.MODID+":"+ StringsRM.ACMA_Prefix+"darkHeart",

        // GUI
        Gui_Menu_Button_Prestige = StringsRM.MENU_Prefix + ".prestige",
        Gui_Menu_Button_PrestigeLevel = StringsRM.MENU_Prefix + ".prestigeLevel",
        Gui_Menu_Button_PrestigeConfirm = StringsRM.MENU_Prefix +".prestigeConfirm",
        Gui_Menu_Button_DreamEater = StringsRM.MENU_Prefix + ".dreamEater",
        Gui_Menu_Button_Panel = StringsRM.MENU_Prefix + ".panel",
        Gui_Menu_Button_Wiki = StringsRM.MENU_Prefix + ".wiki",
        Gui_Menu_Button_Credits = StringsRM.MENU_Prefix + ".creditsScreen";
}
