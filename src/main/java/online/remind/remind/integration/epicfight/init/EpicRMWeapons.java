package online.remind.remind.integration.epicfight.init;

import java.util.function.Function;
import net.minecraft.world.item.Item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

import online.kingdomkeys.kingdomkeys.integration.epicfight.init.EpicKKWeapons;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKAnimations;
import online.kingdomkeys.kingdomkeys.integration.epicfight.skills.KKSkills;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.integration.epicfight.capabilities.ReMindWeaponCapabilities;

import org.checkerframework.checker.units.qual.C;

import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.neoevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.Animations.*;
import yesman.epicfight.registry.entries.EpicFightSkills;
import yesman.epicfight.registry.entries.EpicFightSounds;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

public class EpicRMWeapons {
    public static final Function<Item, WeaponCapability.Builder> XEPHIRO = item ->
        WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.SWORD)
                .styleProvider(playerpatch -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicKKWeapons.EpicKKWeaponEnum.KK_KEYBLADE ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND)
                .hitSound(EpicFightSounds.BLADE_HIT.get())
                .collider(ColliderPreset.LONGSWORD)
                .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == CapabilityItem.WeaponCategories.SWORD)
                .newStyleCombo(CapabilityItem.Styles.ONE_HAND, Animations.GREATSWORD_AUTO1, Animations.GREATSWORD_AUTO2, Animations.TACHI_AUTO1, KKAnimations.SORA_FINISHER1, KKAnimations.SORA_AUTO3, Animations.GREATSWORD_AIR_SLASH)
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND, Animations.SWORD_DUAL_AUTO1, KKAnimations.VALOR_AUTO1, KKAnimations.VALOR_AUTO2, KKAnimations.VALOR_AUTO3, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_LONGSWORD)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.WALK, Animations.BIPED_WALK_SPEAR)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_RUN_DUAL)
                .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.RUN, Animations.BIPED_RUN_SPEAR)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
                .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                .passiveSkill(KKSkills.comboExtender.get());


        //TODO: Add Styles for FF Characters as either base styles or drive forms.




    private EpicRMWeapons() {
    }

    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,EpicRMWeaponEnum.XEPHIRO.toString().toLowerCase()), XEPHIRO);

    }

    public enum EpicRMWeaponEnum implements WeaponCategory {
        XEPHIRO;
        private final int id;

        EpicRMWeaponEnum() {
            this.id = WeaponCategory.ENUM_MANAGER.assign(this);
        }

        @Override
        public int universalOrdinal() {
            return id;
        }
    }
}
//TODO: Help with EFM on KK and find more imports or methods to fix efm for ReMind.