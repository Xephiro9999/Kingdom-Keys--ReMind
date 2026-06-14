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
        ATMA_Prefix = "attack_",
        MAMA_Prefix = "magic_",
        LIMA_Prefix = "limit_",
        DE_Prefix = "dreameater_",


        //Forms
        rageForm = StringsRM.DFMA_Prefix+"rage",
        darkForm = StringsRM.DFMA_Prefix+"dark",
        lightForm = StringsRM.DFMA_Prefix+"light",
        twilight = StringsRM.DFMA_Prefix+"twilight",

        fireStorm = StringsRM.DFMA_Prefix+"firestorm",
        diamondDust = StringsRM.DFMA_Prefix+"diamond_dust",
        thunderBolt = StringsRM.DFMA_Prefix+"thunder_bolt",
        feverPitch = StringsRM.DFMA_Prefix+"fever_pitch",
        criticalImpact = StringsRM.DFMA_Prefix+"critical_impact",
        spellweaver = StringsRM.DFMA_Prefix+"spellweaver",
        bloodlust = StringsRM.DFMA_Prefix+"bloodlust",

        regenForm = StringsRM.DFMA_Prefix+"regen",

        // Magic
        Magic_Auto_Life = StringsRM.MAMA_Prefix+"auto-life",
        Magic_Balloon = StringsRM.MAMA_Prefix+"balloon",
        Magic_Berserk = StringsRM.MAMA_Prefix+"berserk",
        Magic_Comet = StringsRM.MAMA_Prefix+"comet",
        Magic_Confuse = StringsRM.MAMA_Prefix+"confuse",
        Magic_Death = StringsRM.MAMA_Prefix+"death",
        Magic_Dispel= StringsRM.MAMA_Prefix+"dispel",
        Magic_Drain= StringsRM.MAMA_Prefix+"drain",
        Magic_Esuna= StringsRM.MAMA_Prefix+"esuna",
        Magic_Faith= StringsRM.MAMA_Prefix+"faith",
        Magic_Haste= StringsRM.MAMA_Prefix+"haste",
        Magic_Hastera= StringsRM.MAMA_Prefix+"hastera",
        Magic_Hastega= StringsRM.MAMA_Prefix+"hastega",
        Magic_Holy= StringsRM.MAMA_Prefix+"holy",
        Magic_Mine_Shield = StringsRM.MAMA_Prefix+"mine_shield",
        Magic_Mine_Square = StringsRM.MAMA_Prefix+"mine_square",



        //Ability List
        darkPassage = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"dark_passage",
        darknessBoost = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"darkness_boost",
        lightBoost = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"light_boost",
        mpBoost = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"mp_boost",
        hpBoost = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"hp_boost",
        situationBoost = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"situation_boost",
        cure_converter = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"cure_converter",
        rageAwakened = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"rage_awakened",
        darkPower = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"dark_power",
        wayToLight = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"way_to_light",
        roadToDawn = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"road_to_dawn",
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
        stopBlock = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"stop_block",
        poisonBlock = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"poison_block",
        royalGuard = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"royal_guard",
        blockReplenisher = KingdomKeysReMind.MODID+":"+StringsRM.ABMA_Prefix+"block_replenisher",
        spellblade = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"spellblade",
        ultima_weapon_ability = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"ultima_weapon",
        munny_magic = KingdomKeysReMind.MODID+":"+StringsRM.ABMA_Prefix+"munny_magic",

        lightInfusion = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"light_infusion",
        darkInfusion = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"dark_infusion",
        twilightInfusion = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"twilight_infusion",

        mpSlow = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"mp_slow",
        mpSlowra = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"mp_slowra",
        mpSlowga = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"mp_slowga",
        oneHP = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"one_hp",
        ribbon = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"ribbon",

        //Placeholders for Spirit System
        none = "none",
        chirithy = StringsRM.DE_Prefix+"chirithy",
        meowWow = StringsRM.DE_Prefix+"meowwow",
        komoryBat = StringsRM.DE_Prefix+"komory_bat",

        // Grand Magics
        seekerMine = KingdomKeysReMind.MODID+":"+StringsRM.ABMA_Prefix+"seeker_mine",

        // FF Keyblade Abilities
        Tidus = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"tidus",
        Jecht = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"jecht",

        // Custom Abilities
        Lyric1 = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"lyric1",
        Lyric2 = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"lyric2",

        Xephiro = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"xephiro",

        Regen = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"regen",
        Exceed = KingdomKeysReMind.MODID+":"+ StringsRM.ABMA_Prefix+"exceed",

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
        RageRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"rage",
        DualShotRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"dual_shot",
        DarkFiragaRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"dark_firaga",
        XemnasRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"xemnas",
        XaldinRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"xaldin",
        XigbarRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"xigbar",
        vexenRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"vexen",
        ZexionRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"zexion",
        BlitzRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"blitz",
        SlotEdgeRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"slot_edge",

        CounterHammerRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"counter_hammer",
        CounterBlastRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"counter_blast",
        CounterRushRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"counter_rush",

        FinishRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"finish",
        FireStormRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"firestorm",
        DiamondDustRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"diamond_dust",
        ThunderBoltRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"thunder_bolt",
        FeverPitchRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"fever_pitch",
        CriticalImpactRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"critical_impact",
        SpellweaverRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"spellweaver",
        BloodlustRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"bloodlust",

        RegenRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"regen",
        ExceedRC = KingdomKeysReMind.MODID+":"+StringsRM.RCMA_Prefix+"exceed",

        // Limits

        firagaWall = "firaga_wall",


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
        Gui_Menu_Button_Keyblades = StringsRM.MENU_Prefix + ".keyblades",
        Gui_Menu_Button_Attack = StringsRM.MENU_Prefix + ".attack",
        Gui_Menu_Button_Magic = StringsRM.MENU_Prefix + ".magic",
        Gui_Menu_Button_Ability = StringsRM.MENU_Prefix + ".ability",
        Gui_Menu_Button_Forms = StringsRM.MENU_Prefix + ".forms",
        Gui_Menu_Button_Armor = StringsRM.MENU_Prefix + ".armor",
        Gui_Menu_Button_Accessories = StringsRM.MENU_Prefix + ".accessory",
        Gui_Menu_Button_Shotlocks = StringsRM.MENU_Prefix + ".shotlock",
        Gui_Menu_Button_Credits = StringsRM.MENU_Prefix + ".creditsScreen",
        Gui_Menu_Button_Wallet = StringsRM.MENU_Prefix + ".wallet";
}
