package online.remind.remind.item;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.item.*;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.dreameater.DreamEater;
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
        hasteSpell = ITEMS.register("haste_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_haste")),
        slowSpell = ITEMS.register("slow_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_slow")),
        holySpell = ITEMS.register("holy_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_holy")),
        ruinSpell = ITEMS.register("ruin_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_ruin")),
        ultimaSpell = ITEMS.register("ultima_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_ultima")),
        cometSpell = ITEMS.register("comet_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_comet")),
        berserkSpell = ITEMS.register("berserk_spell",() -> new RMMagicSpellItem(new Item.Properties(),KingdomKeysReMind.MODID+":magic_berserk")),
        autoLifeSpell = ITEMS.register("autolife_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_auto-life")),
        drainSpell = ITEMS.register("drain_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_drain")),
        osmoseSpell = ITEMS.register("osmose_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_osmose")),
        silenceSpell = ITEMS.register("silence_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_silence")),
        esunaSpell = ITEMS.register("esuna_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_esuna")),
        dispelSpell = ITEMS.register("dispel_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_dispel")),
        faithSpell = ITEMS.register("faith_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_faith")),
        regenSpell = ITEMS.register("regen_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_regen")),
        stealSpell = ITEMS.register("steal_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_steal")),
        confuseSpell = ITEMS.register("confuse_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_confuse")),

        hasteraSpell = ITEMS.register("haste1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_hastera")),
        slowraSpell = ITEMS.register("slow1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_slowra")),
        holyraSpell = ITEMS.register("holy1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_holyra")),
        ruinraSpell = ITEMS.register("ruin1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_ruinra")),
        meteorSpell = ITEMS.register("comet1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_meteor")),
        berserkraSpell = ITEMS.register("berserk1_spell",() -> new RMMagicSpellItem(new Item.Properties(),KingdomKeysReMind.MODID+":magic_berserkra")),
        drainraSpell = ITEMS.register("drain1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_drainra")),
        osmoseraSpell = ITEMS.register("osmose1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_osmosera")),
        silenceraSpell = ITEMS.register("silence1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_silencera")),
        groupEsunaSpell = ITEMS.register("esuna1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_group_esuna")),
        regenraSpell = ITEMS.register("regen1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_regenra")),
        confuse1Spell = ITEMS.register("confuse1_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_confusera")),


        hastegaSpell = ITEMS.register("haste2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_hastega")),
        slowgaSpell = ITEMS.register("slow2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_slowga")),
        holygaSpell = ITEMS.register("holy2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_holyga")),
        ruingaSpell = ITEMS.register("ruin2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_ruinga")),
        berserkgaSpell = ITEMS.register("berserk2_spell",() -> new RMMagicSpellItem(new Item.Properties(),KingdomKeysReMind.MODID+":magic_berserkga")),
        draingaSpell = ITEMS.register("drain2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_drainga")),
        osmosegaSpell = ITEMS.register("osmose2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_osmosega")),
        silencegaSpell = ITEMS.register("silence2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_silencega")),
        regengaSpell = ITEMS.register("regen2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_regenga")),
        confuse2Spell = ITEMS.register("confuse2_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_confusega")),


        // Attack Orbs
        quickBlitzAttack = ITEMS.register("quick_blitz_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_quick_blitz")),
        slidingDashAttack = ITEMS.register("sliding_dash_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_sliding_dash")),
        fireSurgeAttack = ITEMS.register("fire_surge_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_fire_surge")),
        thunderSurgeAttack = ITEMS.register("thunder_surge_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_thunder_surge")),
        blizzardSurgeAttack = ITEMS.register("blizzard_surge_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_blizzard_surge")),
        waterSurgeAttack = ITEMS.register("water_surge_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_water_surge")),
        aeroSurgeAttack = ITEMS.register("aero_surge_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_aero_surge")),
        lightSurgeAttack = ITEMS.register("light_surge_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_light_surge")),
        darkSurgeAttack = ITEMS.register("dark_surge_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_dark_surge")),
        zantetsukenAttack = ITEMS.register("zantetsuken_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_zantetsuken")),

        fireStrikeAttack = ITEMS.register("fire_strike_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID + ":attack_fire_strike")),
        blizzardStrikeAttack = ITEMS.register("blizzard_strike_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID + ":attack_blizzard_strike")),
        thunderStrikeAttack = ITEMS.register("thunder_strike_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID + ":attack_thunder_strike")),
        waterStrikeAttack = ITEMS.register("water_strike_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID + ":attack_water_strike")),
        aeroStrikeAttack = ITEMS.register("aero_strike_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID + ":attack_aero_strike")),
        lightStrikeAttack = ITEMS.register("light_strike_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID + ":attack_light_strike")),
        darkStrikeAttack = ITEMS.register("dark_strike_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID + ":attack_dark_strike")),
        bindingStrikeAttack = ITEMS.register("binding_strike_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID + ":attack_binding_strike")),
        confusionStrikeAttack = ITEMS.register("confusion_strike_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID + ":attack_confusion_strike")),
        blitzAttack = ITEMS.register("blitz_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID + ":attack_blitz")),
        slotEdgeAttack = ITEMS.register("slot_edge_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID + ":attack_slot_edge")),

        fireSurge1Attack = ITEMS.register("fire_surge1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_fira_surge")),
        thunderSurge1Attack = ITEMS.register("thunder_surge1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_thundara_surge")),
        blizzardSurge1Attack = ITEMS.register("blizzard_surge1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_blizzara_surge")),
        waterSurge1Attack = ITEMS.register("water_surge1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_watera_surge")),
        aeroSurge1Attack = ITEMS.register("aero_surge1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_aerora_surge")),
        lightSurge1Attack = ITEMS.register("light_surge1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_lightra_surge")),
        darkSurge1Attack = ITEMS.register("dark_surge1_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_darkra_surge")),

        fireSurge2Attack = ITEMS.register("fire_surge2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_firaga_surge")),
        thunderSurge2Attack = ITEMS.register("thunder_surge2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_thundaga_surge")),
        blizzardSurge2Attack = ITEMS.register("blizzard_surge2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_blizzaga_surge")),
        waterSurge2Attack = ITEMS.register("water_surge2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_waterga_surge")),
        aeroSurge2Attack = ITEMS.register("aero_surge2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_aeroga_surge")),
        lightSurge2Attack = ITEMS.register("light_surge2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_lightga_surge")),
        darkSurge2Attack = ITEMS.register("dark_surge2_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_darkga_surge")),

    // Creative Exclusive
        swiftStrikeAttack = ITEMS.register("swift_strike_attack", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":attack_swift_strike")),
        deathSpell = ITEMS.register("death_lv_spell", () -> new RMMagicSpellItem(new Item.Properties(), KingdomKeysReMind.MODID+":magic_death")),

        // Shotlock Orbs
        flameSalvo = ITEMS.register("flame_salvo_shotlock",() -> new ShotlockOrbItem(new Item.Properties(),KingdomKeysReMind.MODID+":flame_salvo")),
        bubbleBlaster = ITEMS.register("bubble_blaster_shotlock",() -> new ShotlockOrbItem(new Item.Properties(),KingdomKeysReMind.MODID+":bubble_blaster")),
        thunderStorm = ITEMS.register("thunderstorm_shotlock",() -> new ShotlockOrbItem(new Item.Properties(),KingdomKeysReMind.MODID+":thunderstorm")),
        bioBarrage = ITEMS.register("bio_barrage_shotlock",() -> new ShotlockOrbItem(new Item.Properties(),KingdomKeysReMind.MODID+":bio_barrage")),
        meteorShower = ITEMS.register("meteor_shower_shotlock",() -> new ShotlockOrbItem(new Item.Properties(),KingdomKeysReMind.MODID+":meteor_shower")),
        darkDivide = ITEMS.register("dark_divide_shotlock",() -> new ShotlockOrbItem(new Item.Properties(),KingdomKeysReMind.MODID+":dark_divide")),

    // Dream Eater Charms
        meowWowCharm = ITEMS.register("meow_wow_charm",() -> new DreamEaterCharmItem(new Item.Properties().stacksTo(1), GlobalDataRM.DREAM_EATER_MEOW_WOW, "Meow Wow")),
        komoryBatCharm = ITEMS.register("komory_bat_charm",() -> new DreamEaterCharmItem(new Item.Properties().stacksTo(1), GlobalDataRM.DREAM_EATER_KOMORY_BAT, "Komory Bat")),
        cactuarCharm = ITEMS.register("cactuar_charm",() -> new DreamEaterCharmItem(new Item.Properties().stacksTo(1), GlobalDataRM.DREAM_EATER_CACTUAR, "Cactuar")),
        tonberryCharm = ITEMS.register("tonberry_charm",() -> new DreamEaterCharmItem(new Item.Properties().stacksTo(1), GlobalDataRM.DREAM_EATER_TONBERRY, "Tonberry")),


    // Ability Orb?
        abilityOrb = ITEMS.register("ability_orb", () -> new AbilityOrbItem(new Item.Properties(), "")),

        // Org Panel System
        slotReleaser = ITEMS.register("slot_releaser", () -> new SlotReleaserItem(new Item.Properties().stacksTo(64))),


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
        fortuna = ITEMS.register("fortuna", () -> new KeybladeItem(new Item.Properties())),


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
        fortunaChain = ITEMS.register("fortuna_chain", () -> new KeychainItem()),


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
