package online.remind.remind.item;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.item.*;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.lib.StringsRM;

import java.util.function.Supplier;


public class ModItemsRM{
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.createItems(KingdomKeysReMind.MODID);

    public static int copperCoinValue = ModConfigs.copperCoinValue;
    public static int silverCoinValue = ModConfigs.silverCoinValue;
    public static int goldCoinValue = ModConfigs.goldCoinValue;


    public static final Supplier<Item>
            // Spell Orbs
        hasteSpell = ITEMS.register("haste_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_haste",0)),
        slowSpell = ITEMS.register("slow_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_slow",0)),
        holySpell = ITEMS.register("holy_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_holy",0)),
        ruinSpell = ITEMS.register("ruin_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_ruin",0)),
        balloonSpell = ITEMS.register("balloon_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_balloon",0)),
        ultimaSpell = ITEMS.register("ultima_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_ultima",0)),
        cometSpell = ITEMS.register("comet_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_comet",0)),
        berserkSpell = ITEMS.register("berserk_spell",() -> new RMMagicSpellItem(new Item.Properties(),KingdomKeysReMind.MODID+":magic_berserk",0)),
        autoLifeSpell = ITEMS.register("autolife_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_auto-life",0)),
        drainSpell = ITEMS.register("drain_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_drain",0)),
        osmoseSpell = ITEMS.register("osmose_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_osmose",0)),
        silenceSpell = ITEMS.register("silence_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_silence",0)),
        esunaSpell = ITEMS.register("esuna_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_esuna",0)),
        dispelSpell = ITEMS.register("dispel_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_dispel",0)),
        warpSpell = ITEMS.register("warp_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_warp",0)),
        faithSpell = ITEMS.register("faith_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_faith",0)),
        regenSpell = ITEMS.register("regen_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_regen",0)),
        stealSpell = ITEMS.register("steal_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_steal",0)),
        sparkSpell = ITEMS.register("spark_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_spark",0)),
        mineSquareSpell = ITEMS.register("mine_square_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_mine_square",0)),
        mineShieldSpell = ITEMS.register("mine_shield_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_mine_shield",0)),
        confuseSpell = ITEMS.register("confuse_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_confuse",0)),

        hasteraSpell = ITEMS.register("haste1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_haste",1)),
        slowraSpell = ITEMS.register("slow1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_slow",1)),
        holyraSpell = ITEMS.register("holy1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_holy",1)),
        ruinraSpell = ITEMS.register("ruin1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_ruin",1)),
        balloonraSpell = ITEMS.register("balloon1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_balloon",1)),
        meteorSpell = ITEMS.register("comet1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_comet",1)),
        berserkraSpell = ITEMS.register("berserk1_spell",() -> new RMMagicSpellItem(new Item.Properties(),KingdomKeysReMind.MODID+":magic_berserk",1)),
        drainraSpell = ITEMS.register("drain1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_drain",1)),
        osmoseraSpell = ITEMS.register("osmose1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_osmose",1)),
        silenceraSpell = ITEMS.register("silence1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_silence",1)),
        groupEsunaSpell = ITEMS.register("esuna1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_esuna",1)),
        regenraSpell = ITEMS.register("regen1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_regen",1)),
        sparkraSpell = ITEMS.register("spark1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_spark",1)),
        mineSquare1Spell = ITEMS.register("mine_square1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_mine_square",1)),
        mineShield1Spell = ITEMS.register("mine_shield1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_mine_shield",1)),
        confuse1Spell = ITEMS.register("confuse1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_confuse",1)),


    hastegaSpell = ITEMS.register("haste2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_haste",2)),
        slowgaSpell = ITEMS.register("slow2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_slow",2)),
        holygaSpell = ITEMS.register("holy2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_holy",2)),
        ruingaSpell = ITEMS.register("ruin2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_ruin",2)),
        balloongaSpell = ITEMS.register("balloon2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_balloon",2)),
        berserkgaSpell = ITEMS.register("berserk2_spell",() -> new RMMagicSpellItem(new Item.Properties(),KingdomKeysReMind.MODID+":magic_berserk",2)),
        draingaSpell = ITEMS.register("drain2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_drain",2)),
        osmosegaSpell = ITEMS.register("osmose2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_osmose",2)),
        silencegaSpell = ITEMS.register("silence2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_silence",2)),
        regengaSpell = ITEMS.register("regen2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_regen",2)),
        sparkgaSpell = ITEMS.register("spark2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_spark",2)),
        mineSquare2Spell = ITEMS.register("mine_square2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_mine_square",2)),
        mineShield2Spell = ITEMS.register("mine_shield2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_mine_shield",2)),
        confuse2Spell = ITEMS.register("confuse2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_confuse",2)),

        mineSquare3Spell = ITEMS.register("mine_square3_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_mine_square",3)),
        mineShield3Spell = ITEMS.register("mine_shield3_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_mine_shield",3)),


        // Attack Orbs
        quickBlitzAttack = ITEMS.register("quick_blitz_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_quick_blitz",0)),
        slidingDashAttack = ITEMS.register("sliding_dash_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_sliding_dash",0)),
        fireSurgeAttack = ITEMS.register("fire_surge_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_fire_surge",0)),
        thunderSurgeAttack = ITEMS.register("thunder_surge_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_thunder_surge",0)),
        blizzardSurgeAttack = ITEMS.register("blizzard_surge_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_blizzard_surge",0)),
        waterSurgeAttack = ITEMS.register("water_surge_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_water_surge",0)),
        aeroSurgeAttack = ITEMS.register("aero_surge_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_aero_surge",0)),
        lightSurgeAttack = ITEMS.register("light_surge_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_light_surge",0)),
        darkSurgeAttack = ITEMS.register("dark_surge_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_dark_surge",0)),
        zantetsukenAttack = ITEMS.register("zantetsuken_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_zantetsuken",0)),

        quickBlitz1Attack = ITEMS.register("quick_blitz1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_quick_blitz",1)),
        slidingDash1Attack = ITEMS.register("sliding_dash1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_sliding_dash",1)),
        fireSurge1Attack = ITEMS.register("fire_surge1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_fire_surge",1)),
        thunderSurge1Attack = ITEMS.register("thunder_surge1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_thunder_surge",1)),
        blizzardSurge1Attack = ITEMS.register("blizzard_surge1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_blizzard_surge",1)),
        waterSurge1Attack = ITEMS.register("water_surge1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_water_surge",1)),
        aeroSurge1Attack = ITEMS.register("aero_surge1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_aero_surge",1)),
        lightSurge1Attack = ITEMS.register("light_surge1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_light_surge",1)),
        darkSurge1Attack = ITEMS.register("dark_surge1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_dark_surge",1)),
        zantetsuken1Attack = ITEMS.register("zantetsuken1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_zantetsuken",1)),

        quickBlitz2Attack = ITEMS.register("quick_blitz2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_quick_blitz",2)),
        slidingDash2Attack = ITEMS.register("sliding_dash2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_sliding_dash",2)),
        fireSurge2Attack = ITEMS.register("fire_surge2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_fire_surge",2)),
        thunderSurge2Attack = ITEMS.register("thunder_surge2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_thunder_surge",2)),
        blizzardSurge2Attack = ITEMS.register("blizzard_surge2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_blizzard_surge",2)),
        waterSurge2Attack = ITEMS.register("water_surge2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_water_surge",2)),
        aeroSurge2Attack = ITEMS.register("aero_surge2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_aero_surge",2)),
        lightSurge2Attack = ITEMS.register("light_surge2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_light_surge",2)),
        darkSurge2Attack = ITEMS.register("dark_surge2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_dark_surge",2)),
        zantetsuken2Attack = ITEMS.register("zantetsuken2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_zantetsuken",2)),

        // Shotlock Orbs
        flameSalvo = ITEMS.register("flame_salvo_shotlock",() -> new ShotlockOrbItem(new Item.Properties(),KingdomKeysReMind.MODID+":flame_salvo")),
        bubbleBlaster = ITEMS.register("bubble_blaster_shotlock",() -> new ShotlockOrbItem(new Item.Properties(),KingdomKeysReMind.MODID+":bubble_blaster")),
        thunderStorm = ITEMS.register("thunderstorm_shotlock",() -> new ShotlockOrbItem(new Item.Properties(),KingdomKeysReMind.MODID+":thunderstorm")),
        bioBarrage = ITEMS.register("bio_barrage_shotlock",() -> new ShotlockOrbItem(new Item.Properties(),KingdomKeysReMind.MODID+":bio_barrage")),
        meteorShower = ITEMS.register("meteor_shower_shotlock",() -> new ShotlockOrbItem(new Item.Properties(),KingdomKeysReMind.MODID+":meteor_shower")),
        darkDivide = ITEMS.register("dark_divide_shotlock",() -> new ShotlockOrbItem(new Item.Properties(),KingdomKeysReMind.MODID+":dark_divide")),

        // Ability Orb?
        abilityOrb = ITEMS.register("ability_orb", () -> new AbilityOrbItem(new Item.Properties(), "")),

        // Keyblades
        xephiroKeyblade = ITEMS.register("xephiro_keyblade", () -> new KeybladeItem(new Item.Properties())),
        pureblood = ITEMS.register("pureblood", () -> new KeybladeItem(new Item.Properties())),
        elemental_crescendo = ITEMS.register("elemental_crescendo", () -> new KeybladeItem(new Item.Properties())),
        gazing_omen = ITEMS.register("gazing_omen", () -> new KeybladeItem(new Item.Properties())),
        crystalsLight = ITEMS.register("crystals_light", () -> new KeybladeItem(new Item.Properties())),
        blitzersDream = ITEMS.register("blitzers_dream", () -> new KeybladeItem(new Item.Properties())),
        legendsFang = ITEMS.register("legends_fang", () -> new KeybladeItem(new Item.Properties())),
        fierceDeityKey = ITEMS.register("fierce_deity_key", () -> new KeybladeItem(new Item.Properties())),
        lyric2025Tournament = ITEMS.register("lyric_2025_tournament", () -> new KeybladeItem(new Item.Properties())),
        voidlight = ITEMS.register("voidlight", () -> new KeybladeItem(new Item.Properties())),


        // Keychains
        xephiroKeybladeChain = ITEMS.register("xephiro_keyblade_chain", () -> new KeychainItem()),
        purebloodChain = ITEMS.register("pureblood_chain", () -> new KeychainItem()),
        elementalCrescendoChain = ITEMS.register("elemental_crescendo_chain", () -> new KeychainItem()),
        gazingOmenChain = ITEMS.register("gazing_omen_chain", () -> new KeychainItem()),
        crystalsLightChain = ITEMS.register("crystals_light_chain", () -> new KeychainItem()),
        blitzersDreamChain = ITEMS.register("blitzers_dream_chain", () -> new KeychainItem()),
        legendsFangChain = ITEMS.register("legends_fang_chain", () -> new KeychainItem()),
        fierceDeityKeyChain = ITEMS.register("fierce_deity_key_chain", () -> new KeychainItem()),
        lyric2025TournamentChain = ITEMS.register("lyric_2025_tournament_chain", () -> new KeychainItem()),
        voidlightChain = ITEMS.register("voidlight_chain", () -> new KeychainItem()),


        // And this is where I'd put my Org Weapons... IF I HAD ONE!



        // KK Armors
        aquaChaplet = ITEMS.register("aqua_chaplet", () -> new KKArmorItem(new Item.Properties().stacksTo(1),1, ImmutableMap.of(KKResistanceType.water,50))),
        herosGlove = ITEMS.register("heros_glove", () -> new KKArmorItem(new Item.Properties().stacksTo(1),4, ImmutableMap.of(KKResistanceType.fire,20,KKResistanceType.ice,20,KKResistanceType.darkness,20))),
        herosBelt = ITEMS.register("heros_belt", () -> new KKArmorItem(new Item.Properties().stacksTo(1),3, ImmutableMap.of(KKResistanceType.lightning,20,KKResistanceType.ice,20,KKResistanceType.darkness,20))),
        masterBelt = ITEMS.register("master_belt", () -> new KKArmorItem(new Item.Properties().stacksTo(1),7, ImmutableMap.of(KKResistanceType.darkness,20, KKResistanceType.light,20))),
        ultima_ribbon = ITEMS.register("ultima_ribbon", () -> new KKArmorItem(new Item.Properties().stacksTo(1),5, ImmutableMap.of(KKResistanceType.fire,50,KKResistanceType.ice,50,KKResistanceType.lightning,50,KKResistanceType.darkness,50, KKResistanceType.light,50, KKResistanceType.water,50, KKResistanceType.air, 50))),

        // KK Accessories
        luckOfTheDraw = ITEMS.register("luck_of_the_draw", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1), 0,0,0,new String[] {Strings.luckyLucky,Strings.treasureMagnet})),
        lightHeart = ITEMS.register("light_heart", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1), 5,0,0,new String[] {StringsRM.wayToLight})),
        darkHeart = ITEMS.register("dark_heart", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1), 5,0,0,new String[] {StringsRM.darkPower})),
        ragingHeart = ITEMS.register("raging_heart", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1), 5,0,0,new String[] {StringsRM.rageAwakened})),
        celestriad = ITEMS.register("celestriad", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1), 5,2,3,new String[] {Strings.fireBoost,Strings.blizzardBoost,Strings.thunderBoost})),
        forestClasp = ITEMS.register("forest_clasp", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),8,2,3,new String[] {StringsRM.hpWalker})),
        laughterPin = ITEMS.register("laughter_pin", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),6,3,2,new String[] {StringsRM.mpWalker})),
        crystalRegalia = ITEMS.register("crystal_regalia", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),16,5,5,new String[] {Strings.mpHastega})),
        crystalRegaliaPlus = ITEMS.register("crystal_regalia_plus", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),25,6,6,new String[] {Strings.mpHastega})),
        flanniversaryBadge = ITEMS.register("flanniversary_badge", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),0,4,4,new String[] {Strings.mpHastera,Strings.mpThrift})),
        mickeyClasp = ITEMS.register("mickey_clasp", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),0,3,5,new String[] {Strings.mpHastega,Strings.endlessMagic})),
        breakthrough = ITEMS.register("breakthrough", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),15,7,0, new String[] {StringsRM.cure_converter})),
        hasteBracer = ITEMS.register("haste_bracer", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),5,3,0, new String[] {StringsRM.attackHaste})),
        sacrificeBracer = ITEMS.register("sacrifice_bracer", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),5,5,5, new String[] {StringsRM.vehemence})),
        darkRing = ITEMS.register("dark_ring", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),5,5,5, new String[] {StringsRM.darknessBoost})),
        lightRing = ITEMS.register("light_ring", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),5,5,5, new String[] {StringsRM.lightBoost})),

        expRing = ITEMS.register("exp_ring", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),5,1,1, new String[] {StringsRM.expWalker, Strings.experienceBoost})),
        focusSash = ITEMS.register("focus_sash", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),3,0,3, new String[] {StringsRM.focusWalker})),
        heartLocket = ITEMS.register("heart_locket", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),5,1,1, new String[] {StringsRM.heartWalker})),
        friendBinder = ITEMS.register("friendbinder", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),10,0,0, new String[] {StringsRM.friendsPower})),
        nothingnessBinder = ITEMS.register("nothingness_binder", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),10,0,0, new String[] {StringsRM.heartsPower})),
        silverArmlet = ITEMS.register("silver_armlet", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),10,0,0, new String[] {StringsRM.spellblade,StringsRM.mpBoost})),
        daredevil = ITEMS.register("daredevil", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),0,0,0, new String[] {StringsRM.oneHP, Strings.experienceBoost, Strings.experienceBoost})),
        ribbon_ff7 = ITEMS.register("ribbon_ff7", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),10,0,0, new String[] {StringsRM.ribbon})),
        furyRing = ITEMS.register("fury_ring", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1),0,2,0, new String[] {Strings.berserkCharge, StringsRM.mpSlow, StringsRM.mpSlowra, StringsRM.mpSlowga})),
        braveWarrior = ITEMS.register("brave_warrior", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1), 2,1,0,new String[] {StringsRM.hpBoost,StringsRM.hpBoost})),
        omegaArts = ITEMS.register("omega_arts", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1), 3,4,0,new String[] {StringsRM.hpBoost, StringsRM.hpBoost})),
        rayOfLight = ITEMS.register("ray_of_light", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1), 3,0,4,new String[] {StringsRM.hpBoost, StringsRM.mpBoost})),
        alluringSkull = ITEMS.register("alluring_skull", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1), 3,0,0,new String[] {Strings.encounterPlus, Strings.encounterPlus})),
        whiteFang = ITEMS.register("white_fang", () -> new KKAccessoryItem(new Item.Properties().stacksTo(1), 1,1,0,new String[] {Strings.criticalBoost})),



        // Coins
        copperCoin = ITEMS.register("copper_coin", () -> new RMCoinItem(new Item.Properties().stacksTo(64), () -> ModConfigs.copperCoinValue, "munny")),
        silverCoin = ITEMS.register("silver_coin", () -> new RMCoinItem(new Item.Properties().stacksTo(64), () -> ModConfigs.silverCoinValue, "munny")),
        goldCoin = ITEMS.register("gold_coin", () -> new RMCoinItem(new Item.Properties().stacksTo(64), () -> ModConfigs.goldCoinValue, "munny")),
        emeraldCoin = ITEMS.register("emerald_coin", () -> new RMCoinItem(new Item.Properties().stacksTo(64), () -> ModConfigs.emeraldCoinValue, "munny")),
        diamondCoin = ITEMS.register("diamond_coin", () -> new RMCoinItem(new Item.Properties().stacksTo(64), () -> ModConfigs.diamondCoinValue, "munny")),
        netheriteCoin = ITEMS.register("netherite_coin", () -> new RMCoinItem(new Item.Properties().stacksTo(64), () -> ModConfigs.netheriteCoinValue, "munny")),
        amethystCoin = ITEMS.register("amethyst_coin", () -> new RMCoinItem(new Item.Properties().stacksTo(64), () -> ModConfigs.amethystCoinValue, "munny")),
        heartCoin = ITEMS.register("heart_coin", () -> new RMCoinItem(new Item.Properties().stacksTo(64), () -> ModConfigs.heartCoinValue, "hearts"));

        // Music Discs



    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
