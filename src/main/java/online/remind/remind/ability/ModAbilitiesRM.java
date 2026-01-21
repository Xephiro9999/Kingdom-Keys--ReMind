package online.remind.remind.ability;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.lib.StringsRM;

import java.util.function.Supplier;

public class ModAbilitiesRM extends ModAbilities{

    public static DeferredRegister<Ability> ABILITIES = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "abilities"), KingdomKeysReMind.MODID);

    static int order = 100;

        //New Abilities

    public static final Supplier<Ability>
            // Action
    	DARK_PASSAGE = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"dark_passage").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.darkPassage), 3, Ability.AbilityType.ACTION, order++)),
        RAGE_AWAKENED = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"rage_awakened").getPath(), () -> new Ability( ResourceLocation.parse(StringsRM.rageAwakened), 3,Ability.AbilityType.ACTION, order++)),
        WAY_TO_LIGHT = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"way_to_light").getPath(), () -> new Ability( ResourceLocation.parse(StringsRM.wayToLight), 3, Ability.AbilityType.ACTION, order++)),
        ROAD_TO_DAWN = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"road_to_dawn").getPath(), () -> new Ability( ResourceLocation.parse(StringsRM.roadToDawn), 3, Ability.AbilityType.ACTION, order++)),
        DARK_POWER = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"dark_power").getPath(), () -> new Ability( ResourceLocation.parse(StringsRM.darkPower), 3, Ability.AbilityType.ACTION, order++)),
        RISKCHARGE = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"riskcharge").getPath(), () -> new Ability( ResourceLocation.parse(StringsRM.riskCharge), 0, Ability.AbilityType.ACTION, order++)),

            // Action - EFM
        RENEWAL_BLOCK = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"renewal_block").getPath(), () -> new Ability( ResourceLocation.parse(StringsRM.renewalBlock), 0, Ability.AbilityType.ACTION, order++)),
        FOCUS_BLOCK = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"focus_block").getPath(), () -> new Ability( ResourceLocation.parse(StringsRM.focusBlock), 0, Ability.AbilityType.ACTION, order++)),
        STOP_BLOCK = ABILITIES.register(StringsRM.ABMA_Prefix+"stop_block", () -> new Ability(ResourceLocation.parse(StringsRM.stopBlock), 0, Ability.AbilityType.ACTION, order++)),
        POISON_BLOCK = ABILITIES.register(StringsRM.ABMA_Prefix+"poison_block", () -> new Ability(ResourceLocation.parse(StringsRM.poisonBlock), 0, Ability.AbilityType.ACTION, order++)),
        ROYAL_GUARD = ABILITIES.register(StringsRM.ABMA_Prefix+"royal_guard", () -> new Ability(ResourceLocation.parse(StringsRM.royalGuard), 0, Ability.AbilityType.ACTION, order++)),
        COUNTER_HAMMER = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"counter_hammer").getPath(), () -> new Ability( ResourceLocation.parse(StringsRM.counterHammer), 0, Ability.AbilityType.ACTION, order++)),
        COUNTER_BLAST = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"counter_blast").getPath(), () -> new Ability( ResourceLocation.parse(StringsRM.counterBlast), 0, Ability.AbilityType.ACTION, order++)),
        COUNTER_RUSH = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"counter_rush").getPath(), () -> new Ability( ResourceLocation.parse(StringsRM.counterRush), 0, Ability.AbilityType.ACTION, order++)),


            // Growth
        LIGHT_STEP = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"light_step").getPath(), () -> new Ability( ResourceLocation.parse(StringsRM.lightStep), 0, Ability.AbilityType.GROWTH, order++)),
        DARK_STEP = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"dark_step").getPath(), () -> new Ability( ResourceLocation.parse(StringsRM.darkStep), 0, Ability.AbilityType.GROWTH, order++)),

            // Support
        DARKNESS_BOOST = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"darkness_boost").getPath(), () -> new Ability( ResourceLocation.parse(StringsRM.darknessBoost), 3, Ability.AbilityType.SUPPORT, order++)),
        DARKNESS_WITHIN = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"darkness_within").getPath(), () -> new Ability( ResourceLocation.parse(StringsRM.darknessWithin), 5, Ability.AbilityType.SUPPORT, order++)),
        LIGHT_BOOST = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"light_boost").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.lightBoost), 3, Ability.AbilityType.SUPPORT, order++)),
        LIGHT_WITHIN = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"light_within").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.lightWithin), 5, Ability.AbilityType.SUPPORT, order++)),
        HP_BOOST = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"hp_boost").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.hpBoost), 10, Ability.AbilityType.SUPPORT, order++)),
        MP_BOOST = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"mp_boost").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.mpBoost), 10, Ability.AbilityType.SUPPORT, order++)),
        MP_SHIELD = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"mp_shield").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.mpShield), 5, Ability.AbilityType.SUPPORT, order++)),
        VEHEMENCE = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"vehemence").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.vehemence), 5, Ability.AbilityType.SUPPORT, order++)),
        ADRENALINE = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"adrenaline").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.adrenaline), 4, Ability.AbilityType.SUPPORT, order++)),
        CRITICAL_SURGE = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"critical_surge").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.critical_surge), 4, Ability.AbilityType.SUPPORT, order++)),
        DEDICATION = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"dedication").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.dedication), 0, Ability.AbilityType.SUPPORT, order++)),
        HEARTS_POWER = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"hearts_power").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.heartsPower), 0, Ability.AbilityType.SUPPORT, order++)),
        FRIEND_POWER = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"friends_power").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.friendsPower), 3, Ability.AbilityType.SUPPORT, order++)),
        SPELLBLADE = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"spellblade").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.spellblade), 0, Ability.AbilityType.SUPPORT, order++)),
        ULTIMA_WEAPON_ABILITY = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"ultima_weapon").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.ultima_weapon_ability), 0, Ability.AbilityType.SUPPORT, order++)),
        MUNNY_MAGIC = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"munny_magic").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.munny_magic), 10, Ability.AbilityType.SUPPORT, order++)),

        LIGHT_INFUSION = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"light_infusion").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.lightInfusion), 0, Ability.AbilityType.SUPPORT, order++)),
        DARK_INFUSION = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"dark_infusion").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.darkInfusion), 0, Ability.AbilityType.SUPPORT, order++)),
        TWILIGHT_INFUSION = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"twilight_infusion").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.twilightInfusion), 0, Ability.AbilityType.SUPPORT, order++)),

        // Grand Magics
        SEEKER_MINE = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"seeker_mine").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.seekerMine), 0, Ability.AbilityType.SUPPORT, order++)),


        // Placeholders for Spirit System
        CHIRITHY = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"chirithy").getPath(), () -> new Ability( ResourceLocation.parse(StringsRM.chirithy), 0, Ability.AbilityType.ACTION, order++)),





    HP_WALKER = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"hp_walker").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.hpWalker),10,Ability.AbilityType.SUPPORT, order++)),
        MP_WALKER = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"mp_walker").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.mpWalker),10,Ability.AbilityType.SUPPORT, order++)),
        FOCUS_WALKER = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"focus_walker").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.focusWalker),10,Ability.AbilityType.SUPPORT, order++)),
        HEART_WALKER = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"heart_walker").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.heartWalker),10,Ability.AbilityType.SUPPORT, order++)),
        EXP_WALKER = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"exp_walker").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.expWalker),10,Ability.AbilityType.SUPPORT, order++)),

        ATTACK_HASTE = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"attack_haste").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.attackHaste),3,Ability.AbilityType.SUPPORT, order++)),
        MP_SLOW = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"mp_slow").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.mpSlow),3,Ability.AbilityType.SUPPORT, order++)),
        MP_SLOWRA = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"mp_slowra").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.mpSlowra),4,Ability.AbilityType.SUPPORT, order++)),
        MP_SLOWGA = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"mp_slowga").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.mpSlowga),5,Ability.AbilityType.SUPPORT, order++)),
        ONE_HP = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"one_hp").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.oneHP),5,Ability.AbilityType.SUPPORT, order++)),
        RIBBON = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"ribbon").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.ribbon),10,Ability.AbilityType.SUPPORT, order++)),

    // Weapon Exclusive Abilities
        TIDUS = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"tidus").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.Tidus),3,Ability.AbilityType.SUPPORT, order++)),
        JECHT = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"jecht").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.Jecht),3,Ability.AbilityType.SUPPORT, order++)),

        LYRIC1 = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"lyric1").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.Lyric1),0,Ability.AbilityType.SUPPORT, order++)),
        LYRIC2 = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"lyric2").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.Lyric2),0,Ability.AbilityType.SUPPORT, order++)),
        XEPHIRO = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"xephiro").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.Xephiro),0,Ability.AbilityType.SUPPORT, order++)),
        REGEN = ABILITIES.register(ResourceLocation.parse(StringsRM.ABMA_Prefix+"regen").getPath(), () -> new Ability(ResourceLocation.parse(StringsRM.Regen),0,Ability.AbilityType.SUPPORT, order++));


    // Twilight (Double) Form Exclusive Abilities


}
