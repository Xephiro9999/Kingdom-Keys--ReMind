package online.remind.remind.integration.epicfight.init;

import java.util.function.Function;
import net.minecraft.world.item.Item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

//import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKAnimations;
//import online.kingdomkeys.kingdomkeys.integration.epicfight.skills.KKSkills;
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
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

/*public class EpicRMWeapons {
    public static final Function<Item, CapabilityItem.Builder> XEPHIRO = item -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.SWORD)
                .styleProvider(playerpatch -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicRMWeaponEnum.XEPHIRO ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND)
                .hitSound(EpicFightSounds.BLADE_HIT.get())
                .collider(ColliderPreset.LONGSWORD)
                .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicRMWeaponEnum.XEPHIRO)
                .newStyleCombo(CapabilityItem.Styles.ONE_HAND, Animations.GREATSWORD_AUTO1, Animations.GREATSWORD_AUTO2, Animations.TACHI_AUTO1, KKAnimations.SORA_FINISHER1, KKAnimations.SORA_AUTO3, Animations.GREATSWORD_AIR_SLASH)
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND, Animations.SWORD_DUAL_AUTO1, KKAnimations.VALOR_AUTO1, KKAnimations.VALOR_AUTO2, KKAnimations.VALOR_AUTO3, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK).innateSkill(CapabilityItem.Styles.ONE_HAND, itemstack -> EpicFightSkills.EVISCERATE).innateSkill(CapabilityItem.Styles.TWO_HAND, itemstack -> EpicFightSkills.BLADE_RUSH)
                .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_LONGSWORD)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.WALK, Animations.BIPED_WALK_SPEAR)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_RUN_DUAL)
                .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.RUN, Animations.BIPED_RUN_SPEAR)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
                .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD);


        //TODO: Add Styles for FF Characters as either base styles or drive forms.

        return builder;
    };

    private EpicRMWeapons() {
    }

    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put(new ResourceLocation(KingdomKeysReMind.MODID,"xephiro"), XEPHIRO);
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
}*/
//TODO: Help with EFM on KK and find more imports or methods to fix efm for ReMind.