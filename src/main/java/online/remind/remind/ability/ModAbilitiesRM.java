package online.remind.remind.ability;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.lib.KKSupplier;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.lib.StringsRM;

import java.util.function.Supplier;

public class ModAbilitiesRM extends ModAbilities{

    public static DeferredRegister<Ability> ABILITIES = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "abilities"), KingdomKeysReMind.MODID);

    static int order = 100;

        //New Abilities

    public static final KKSupplier<Ability>
            // Action
    	DARK_PASSAGE = register(StringsRM.ABMA_Prefix+"dark_passage", () -> new Ability(ResourceLocation.parse(StringsRM.darkPassage), 3, Ability.AbilityType.ACTION, order++)),
        RAGE_AWAKENED = register(StringsRM.ABMA_Prefix+"rage_awakened", () -> new Ability( ResourceLocation.parse(StringsRM.rageAwakened), 3,Ability.AbilityType.ACTION, order++)),
        WAY_TO_LIGHT = register(StringsRM.ABMA_Prefix+"way_to_light", () -> new Ability( ResourceLocation.parse(StringsRM.wayToLight), 3, Ability.AbilityType.ACTION, order++)),
        ROAD_TO_DAWN = register(StringsRM.ABMA_Prefix+"road_to_dawn", () -> new Ability( ResourceLocation.parse(StringsRM.roadToDawn), 3, Ability.AbilityType.ACTION, order++)),
        DARK_POWER = register(StringsRM.ABMA_Prefix+"dark_power", () -> new Ability( ResourceLocation.parse(StringsRM.darkPower), 3, Ability.AbilityType.ACTION, order++)),
        RISKCHARGE = register(StringsRM.ABMA_Prefix+"riskcharge", () -> new Ability( ResourceLocation.parse(StringsRM.riskCharge), 0, Ability.AbilityType.ACTION, order++)),

            // Action - EFM
        RENEWAL_BLOCK = register(StringsRM.ABMA_Prefix+"renewal_block", () -> new Ability( ResourceLocation.parse(StringsRM.renewalBlock), 0, Ability.AbilityType.ACTION, order++)),
        FOCUS_BLOCK = register(StringsRM.ABMA_Prefix+"focus_block", () -> new Ability( ResourceLocation.parse(StringsRM.focusBlock), 0, Ability.AbilityType.ACTION, order++)),
        STOP_BLOCK = register(StringsRM.ABMA_Prefix+"stop_block", () -> new Ability(ResourceLocation.parse(StringsRM.stopBlock), 0, Ability.AbilityType.ACTION, order++)),
        POISON_BLOCK = register(StringsRM.ABMA_Prefix+"poison_block", () -> new Ability(ResourceLocation.parse(StringsRM.poisonBlock), 0, Ability.AbilityType.ACTION, order++)),
        CONFUSION_BLOCK = register(StringsRM.ABMA_Prefix+"confusion_block", () -> new Ability(ResourceLocation.parse(StringsRM.confusionBlock), 0, Ability.AbilityType.ACTION, order++)),
        ROYAL_GUARD = register(StringsRM.ABMA_Prefix+"royal_guard", () -> new Ability(ResourceLocation.parse(StringsRM.royalGuard), 0, Ability.AbilityType.ACTION, order++)),
        COUNTER_HAMMER = register(StringsRM.ABMA_Prefix+"counter_hammer", () -> new Ability( ResourceLocation.parse(StringsRM.counterHammer), 0, Ability.AbilityType.ACTION, order++)),
        COUNTER_BLAST = register(StringsRM.ABMA_Prefix+"counter_blast", () -> new Ability( ResourceLocation.parse(StringsRM.counterBlast), 0, Ability.AbilityType.ACTION, order++)),
        COUNTER_RUSH = register(StringsRM.ABMA_Prefix+"counter_rush", () -> new Ability( ResourceLocation.parse(StringsRM.counterRush), 0, Ability.AbilityType.ACTION, order++)),


            // Growth
        LIGHT_STEP = register(StringsRM.ABMA_Prefix+"light_step", () -> new Ability( ResourceLocation.parse(StringsRM.lightStep), 0, Ability.AbilityType.GROWTH, order++)),
        DARK_STEP = register(StringsRM.ABMA_Prefix+"dark_step", () -> new Ability( ResourceLocation.parse(StringsRM.darkStep), 0, Ability.AbilityType.GROWTH, order++)),

            // Support
        DARKNESS_BOOST = register(StringsRM.ABMA_Prefix+"darkness_boost", () -> new Ability( ResourceLocation.parse(StringsRM.darknessBoost), 3, Ability.AbilityType.SUPPORT, order++)),
        DARKNESS_WITHIN = register(StringsRM.ABMA_Prefix+"darkness_within", () -> new Ability( ResourceLocation.parse(StringsRM.darknessWithin), 5, Ability.AbilityType.SUPPORT, order++)),
        LIGHT_BOOST = register(StringsRM.ABMA_Prefix+"light_boost", () -> new Ability(ResourceLocation.parse(StringsRM.lightBoost), 3, Ability.AbilityType.SUPPORT, order++)),
        LIGHT_WITHIN = register(StringsRM.ABMA_Prefix+"light_within", () -> new Ability(ResourceLocation.parse(StringsRM.lightWithin), 5, Ability.AbilityType.SUPPORT, order++)),
        HP_BOOST = register(StringsRM.ABMA_Prefix+"hp_boost", () -> new Ability(ResourceLocation.parse(StringsRM.hpBoost), 10, Ability.AbilityType.SUPPORT, order++)),
        MP_BOOST = register(StringsRM.ABMA_Prefix+"mp_boost", () -> new Ability(ResourceLocation.parse(StringsRM.mpBoost), 10, Ability.AbilityType.SUPPORT, order++)),
        SITUATION_BOOST = register(StringsRM.ABMA_Prefix+"situation_boost", () -> new Ability(ResourceLocation.parse(StringsRM.situationBoost), 2, Ability.AbilityType.SUPPORT, order++)),
        CURE_CONVERTER = register(StringsRM.ABMA_Prefix+"cure_converter", () -> new Ability(ResourceLocation.parse(StringsRM.cure_converter), 0, Ability.AbilityType.SUPPORT, order++)),
        MP_SHIELD = register(StringsRM.ABMA_Prefix+"mp_shield", () -> new Ability(ResourceLocation.parse(StringsRM.mpShield), 5, Ability.AbilityType.SUPPORT, order++)),
        VEHEMENCE = register(StringsRM.ABMA_Prefix+"vehemence", () -> new Ability(ResourceLocation.parse(StringsRM.vehemence), 5, Ability.AbilityType.SUPPORT, order++)),
        ADRENALINE = register(StringsRM.ABMA_Prefix+"adrenaline", () -> new Ability(ResourceLocation.parse(StringsRM.adrenaline), 4, Ability.AbilityType.SUPPORT, order++)),
        CRITICAL_SURGE = register(StringsRM.ABMA_Prefix+"critical_surge", () -> new Ability(ResourceLocation.parse(StringsRM.critical_surge), 4, Ability.AbilityType.SUPPORT, order++)),
        DEDICATION = register(StringsRM.ABMA_Prefix+"dedication", () -> new Ability(ResourceLocation.parse(StringsRM.dedication), 0, Ability.AbilityType.SUPPORT, order++)),
        HEARTS_POWER = register(StringsRM.ABMA_Prefix+"hearts_power", () -> new Ability(ResourceLocation.parse(StringsRM.heartsPower), 0, Ability.AbilityType.SUPPORT, order++)),
        FRIEND_POWER = register(StringsRM.ABMA_Prefix+"friends_power", () -> new Ability(ResourceLocation.parse(StringsRM.friendsPower), 3, Ability.AbilityType.SUPPORT, order++)),
        SPELLBLADE = register(StringsRM.ABMA_Prefix+"spellblade", () -> new Ability(ResourceLocation.parse(StringsRM.spellblade), 0, Ability.AbilityType.SUPPORT, order++)),
        ULTIMA_WEAPON_ABILITY = register(StringsRM.ABMA_Prefix+"ultima_weapon", () -> new Ability(ResourceLocation.parse(StringsRM.ultima_weapon_ability), 0, Ability.AbilityType.SUPPORT, order++)),
        MUNNY_MAGIC = register(StringsRM.ABMA_Prefix+"munny_magic", () -> new Ability(ResourceLocation.parse(StringsRM.munny_magic), 10, Ability.AbilityType.SUPPORT, order++)),
        BLOCK_REPLENISHER = register(StringsRM.ABMA_Prefix+"block_replenisher", () -> new Ability(ResourceLocation.parse(StringsRM.blockReplenisher), 3, Ability.AbilityType.SUPPORT, order++)),


    LIGHT_INFUSION = register(StringsRM.ABMA_Prefix+"light_infusion", () -> new Ability(ResourceLocation.parse(StringsRM.lightInfusion), 0, Ability.AbilityType.SUPPORT, order++)),
        DARK_INFUSION = register(StringsRM.ABMA_Prefix+"dark_infusion", () -> new Ability(ResourceLocation.parse(StringsRM.darkInfusion), 0, Ability.AbilityType.SUPPORT, order++)),
        TWILIGHT_INFUSION = register(StringsRM.ABMA_Prefix+"twilight_infusion", () -> new Ability(ResourceLocation.parse(StringsRM.twilightInfusion), 0, Ability.AbilityType.SUPPORT, order++)),

        // Grand Magics
        SEEKER_MINE = register(StringsRM.ABMA_Prefix+"seeker_mine", () -> new Ability(ResourceLocation.parse(StringsRM.seekerMine), 0, Ability.AbilityType.SUPPORT, order++)),


        // Placeholders for Spirit System
        CHIRITHY = register(StringsRM.ABMA_Prefix+"chirithy", () -> new Ability( ResourceLocation.parse(StringsRM.chirithy), 0, Ability.AbilityType.ACTION, order++)),





    HP_WALKER = register(StringsRM.ABMA_Prefix+"hp_walker", () -> new Ability(ResourceLocation.parse(StringsRM.hpWalker),10,Ability.AbilityType.SUPPORT, order++)),
        MP_WALKER = register(StringsRM.ABMA_Prefix+"mp_walker", () -> new Ability(ResourceLocation.parse(StringsRM.mpWalker),10,Ability.AbilityType.SUPPORT, order++)),
        FOCUS_WALKER = register(StringsRM.ABMA_Prefix+"focus_walker", () -> new Ability(ResourceLocation.parse(StringsRM.focusWalker),10,Ability.AbilityType.SUPPORT, order++)),
        HEART_WALKER = register(StringsRM.ABMA_Prefix+"heart_walker", () -> new Ability(ResourceLocation.parse(StringsRM.heartWalker),10,Ability.AbilityType.SUPPORT, order++)),
        EXP_WALKER = register(StringsRM.ABMA_Prefix+"exp_walker", () -> new Ability(ResourceLocation.parse(StringsRM.expWalker),10,Ability.AbilityType.SUPPORT, order++)),

        ATTACK_HASTE = register(StringsRM.ABMA_Prefix+"attack_haste", () -> new Ability(ResourceLocation.parse(StringsRM.attackHaste),3,Ability.AbilityType.SUPPORT, order++)),
        MP_SLOW = register(StringsRM.ABMA_Prefix+"mp_slow", () -> new Ability(ResourceLocation.parse(StringsRM.mpSlow),3,Ability.AbilityType.SUPPORT, order++)),
        MP_SLOWRA = register(StringsRM.ABMA_Prefix+"mp_slowra", () -> new Ability(ResourceLocation.parse(StringsRM.mpSlowra),4,Ability.AbilityType.SUPPORT, order++)),
        MP_SLOWGA = register(StringsRM.ABMA_Prefix+"mp_slowga", () -> new Ability(ResourceLocation.parse(StringsRM.mpSlowga),5,Ability.AbilityType.SUPPORT, order++)),
        ONE_HP = register(StringsRM.ABMA_Prefix+"one_hp", () -> new Ability(ResourceLocation.parse(StringsRM.oneHP),5,Ability.AbilityType.SUPPORT, order++)),
        RIBBON = register(StringsRM.ABMA_Prefix+"ribbon", () -> new Ability(ResourceLocation.parse(StringsRM.ribbon),10,Ability.AbilityType.SUPPORT, order++)),

    // Weapon Exclusive Abilities
        TIDUS = register(StringsRM.ABMA_Prefix+"tidus", () -> new Ability(ResourceLocation.parse(StringsRM.Tidus),3,Ability.AbilityType.SUPPORT, order++)),
        JECHT = register(StringsRM.ABMA_Prefix+"jecht", () -> new Ability(ResourceLocation.parse(StringsRM.Jecht),3,Ability.AbilityType.SUPPORT, order++)),

        LYRIC1 = register(StringsRM.ABMA_Prefix+"lyric1", () -> new Ability(ResourceLocation.parse(StringsRM.Lyric1),0,Ability.AbilityType.SUPPORT, order++)),
        LYRIC2 = register(StringsRM.ABMA_Prefix+"lyric2", () -> new Ability(ResourceLocation.parse(StringsRM.Lyric2),0,Ability.AbilityType.SUPPORT, order++)),
        XEPHIRO = register(StringsRM.ABMA_Prefix+"xephiro", () -> new Ability(ResourceLocation.parse(StringsRM.Xephiro),0,Ability.AbilityType.SUPPORT, order++)),
        REGEN = register(StringsRM.ABMA_Prefix+"regen", () -> new Ability(ResourceLocation.parse(StringsRM.Regen),0,Ability.AbilityType.SUPPORT, order++)),
        EXCEED = register(StringsRM.ABMA_Prefix+"exceed", () -> new Ability(ResourceLocation.parse(StringsRM.Exceed),0,Ability.AbilityType.ACTION, order++));


    // Twilight (Double) Form Exclusive Abilities


    private static KKSupplier<Ability> register(String name, Supplier<Ability> abilitySupplier) {
        return new KKSupplier<>(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, name), ABILITIES.register(name, abilitySupplier));
    }
}
