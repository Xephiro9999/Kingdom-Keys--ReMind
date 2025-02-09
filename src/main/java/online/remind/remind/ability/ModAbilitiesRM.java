package online.remind.remind.ability;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.lib.StringsRM;

public class ModAbilitiesRM extends ModAbilities{

    public static DeferredRegister<Ability> ABILITIES = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "abilities"), KingdomKeysReMind.MODID);

    static int order = 100;

        //New Abilities

    public static final RegistryObject<Ability>
            // Action
    	DARK_PASSAGE = ABILITIES.register(StringsRM.ABMA_Prefix+"dark_passage", () -> new Ability(new ResourceLocation(StringsRM.darkPassage), 3, Ability.AbilityType.ACTION, order++)),
        RAGE_AWAKENED = ABILITIES.register(StringsRM.ABMA_Prefix+"rage_awakened", () -> new Ability(new ResourceLocation(StringsRM.rageAwakened), 5,Ability.AbilityType.ACTION, order++)),
        WAY_TO_LIGHT = ABILITIES.register(StringsRM.ABMA_Prefix+"way_to_light", () -> new Ability(new ResourceLocation(StringsRM.wayToLight), 5, Ability.AbilityType.ACTION, order++)),
        DARK_POWER = ABILITIES.register(StringsRM.ABMA_Prefix+"dark_power", () -> new Ability(new ResourceLocation(StringsRM.darkPower), 5, Ability.AbilityType.ACTION, order++)),
        RISKCHARGE = ABILITIES.register(StringsRM.ABMA_Prefix+"riskcharge", () -> new Ability(new ResourceLocation(StringsRM.riskCharge), 0, Ability.AbilityType.ACTION, order++)),

            // Action - EFM
        RENEWAL_BLOCK = ABILITIES.register(StringsRM.ABMA_Prefix+"renewal_block", () -> new Ability(new ResourceLocation(StringsRM.renewalBlock), 0, Ability.AbilityType.ACTION, order++)),
        FOCUS_BLOCK = ABILITIES.register(StringsRM.ABMA_Prefix+"focus_block", () -> new Ability(new ResourceLocation(StringsRM.focusBlock), 0, Ability.AbilityType.ACTION, order++)),
        COUNTER_HAMMER = ABILITIES.register(StringsRM.ABMA_Prefix+"counter_hammer", () -> new Ability(new ResourceLocation(StringsRM.counterHammer), 0, Ability.AbilityType.ACTION, order++)),
        COUNTER_BLAST = ABILITIES.register(StringsRM.ABMA_Prefix+"counter_blast", () -> new Ability(new ResourceLocation(StringsRM.counterBlast), 0, Ability.AbilityType.ACTION, order++)),
        COUNTER_RUSH = ABILITIES.register(StringsRM.ABMA_Prefix+"counter_rush", () -> new Ability(new ResourceLocation(StringsRM.counterRush), 0, Ability.AbilityType.ACTION, order++)),


            // Growth
        LIGHT_STEP = ABILITIES.register(StringsRM.ABMA_Prefix+"light_step", () -> new Ability(new ResourceLocation(StringsRM.lightStep), 3, Ability.AbilityType.GROWTH, order++)),
        DARK_STEP = ABILITIES.register(StringsRM.ABMA_Prefix+"dark_step", () -> new Ability(new ResourceLocation(StringsRM.darkStep), 3, Ability.AbilityType.GROWTH, order++)),

            // Support
        DARKNESS_BOOST = ABILITIES.register(StringsRM.ABMA_Prefix+"darkness_boost", () -> new Ability(new ResourceLocation(StringsRM.darknessBoost), 3, Ability.AbilityType.SUPPORT, order++)),
        DARKNESS_WITHIN = ABILITIES.register(StringsRM.ABMA_Prefix+"darkness_within", () -> new Ability(new ResourceLocation(StringsRM.darknessWithin), 5, Ability.AbilityType.SUPPORT, order++)),
        LIGHT_BOOST = ABILITIES.register(StringsRM.ABMA_Prefix+"light_boost", () -> new Ability(new ResourceLocation(StringsRM.lightBoost), 3, Ability.AbilityType.SUPPORT, order++)),
        LIGHT_WITHIN = ABILITIES.register(StringsRM.ABMA_Prefix+"light_within", () -> new Ability(new ResourceLocation(StringsRM.lightWithin), 5, Ability.AbilityType.SUPPORT, order++)),
        HP_BOOST = ABILITIES.register(StringsRM.ABMA_Prefix+"hp_boost", () -> new Ability(new ResourceLocation(StringsRM.hpBoost), 10, Ability.AbilityType.SUPPORT, order++)),
        MP_BOOST = ABILITIES.register(StringsRM.ABMA_Prefix+"mp_boost", () -> new Ability(new ResourceLocation(StringsRM.mpBoost), 10, Ability.AbilityType.SUPPORT, order++)),
        MP_SHIELD = ABILITIES.register(StringsRM.ABMA_Prefix+"mp_shield", () -> new Ability(new ResourceLocation(StringsRM.mpShield), 5, Ability.AbilityType.SUPPORT, order++)),
        VEHEMENCE = ABILITIES.register(StringsRM.ABMA_Prefix+"vehemence", () -> new Ability(new ResourceLocation(StringsRM.vehemence), 5, Ability.AbilityType.SUPPORT, order++)),
        ADRENALINE = ABILITIES.register(StringsRM.ABMA_Prefix+"adrenaline", () -> new Ability(new ResourceLocation(StringsRM.adrenaline), 4, Ability.AbilityType.SUPPORT, order++)),
        CRITICAL_SURGE = ABILITIES.register(StringsRM.ABMA_Prefix+"critical_surge", () -> new Ability(new ResourceLocation(StringsRM.critical_surge), 4, Ability.AbilityType.SUPPORT, order++)),
        DEDICATION = ABILITIES.register(StringsRM.ABMA_Prefix+"dedication", () -> new Ability(new ResourceLocation(StringsRM.dedication), 0, Ability.AbilityType.SUPPORT, order++)),
        HEARTS_POWER = ABILITIES.register(StringsRM.ABMA_Prefix+"hearts_power", () -> new Ability(new ResourceLocation(StringsRM.heartsPower), 0, Ability.AbilityType.SUPPORT, order++)),
        FRIEND_POWER = ABILITIES.register(StringsRM.ABMA_Prefix+"friends_power", () -> new Ability(new ResourceLocation(StringsRM.friendsPower), 3, Ability.AbilityType.SUPPORT, order++)),




    HP_WALKER = ABILITIES.register(StringsRM.ABMA_Prefix+"hp_walker", () -> new Ability(new ResourceLocation(StringsRM.hpWalker),10,Ability.AbilityType.SUPPORT, order++)),
        MP_WALKER = ABILITIES.register(StringsRM.ABMA_Prefix+"mp_walker", () -> new Ability(new ResourceLocation(StringsRM.mpWalker),10,Ability.AbilityType.SUPPORT, order++)),
        FOCUS_WALKER = ABILITIES.register(StringsRM.ABMA_Prefix+"focus_walker", () -> new Ability(new ResourceLocation(StringsRM.focusWalker),10,Ability.AbilityType.SUPPORT, order++)),
        HEART_WALKER = ABILITIES.register(StringsRM.ABMA_Prefix+"heart_walker", () -> new Ability(new ResourceLocation(StringsRM.heartWalker),10,Ability.AbilityType.SUPPORT, order++)),
        EXP_WALKER = ABILITIES.register(StringsRM.ABMA_Prefix+"exp_walker", () -> new Ability(new ResourceLocation(StringsRM.expWalker),10,Ability.AbilityType.SUPPORT, order++)),

        ATTACK_HASTE = ABILITIES.register(StringsRM.ABMA_Prefix+"attack_haste", () -> new Ability(new ResourceLocation(StringsRM.attackHaste),3,Ability.AbilityType.SUPPORT, order++)),

    // Weapon Exclusive Abilities
        TIDUS = ABILITIES.register(StringsRM.ABMA_Prefix+"tidus", () -> new Ability(new ResourceLocation(StringsRM.Tidus),3,Ability.AbilityType.SUPPORT, order++)),
        JECHT = ABILITIES.register(StringsRM.ABMA_Prefix+"jecht", () -> new Ability(new ResourceLocation(StringsRM.Jecht),3,Ability.AbilityType.SUPPORT, order++));


    // Twilight (Double) Form Exclusive Abilities


}
