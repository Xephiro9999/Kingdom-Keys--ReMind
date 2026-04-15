package online.remind.remind.integration.epicfight.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.KKStyles;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKAnimations;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKCollider;
import online.kingdomkeys.kingdomkeys.integration.epicfight.skills.KKSkills;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.remind.remind.KingdomKeysReMind;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.event.EpicFightEventHooks;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.registry.entries.EpicFightSounds;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.function.Function;

public class EpicRMWeapons {
    public static final Function<Item, WeaponCapability.Builder> XEPHIRO = item ->
        WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.SWORD).styleProvider(playerpatch ->
                        switch (PlayerData.get((Player) playerpatch.getOriginal()).getActiveDriveForm()) {
                            case Strings.Form_Valor -> KKStyles.VALOR;
                            case Strings.Form_Master -> KKStyles.MASTER;
                            case Strings.Form_Wisdom -> KKStyles.WISDOM;
                            case Strings.Form_Final -> KKStyles.FINAL;
                            default ->
                                playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == CapabilityItem.WeaponCategories.SWORD ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND;
                        })
                .hitSound(EpicFightSounds.BLADE_HIT.get()).collider(KKCollider.KEYBLADE)
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

                .livingMotionModifier(KKStyles.VALOR, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                .livingMotionModifier(KKStyles.WISDOM, LivingMotions.IDLE, KKAnimations.WISDOM_IDLE)
                .livingMotionModifier(KKStyles.MASTER, LivingMotions.IDLE, KKAnimations.MASTER_IDLE)
                .livingMotionModifier(KKStyles.FINAL, LivingMotions.IDLE, KKAnimations.FINAL_IDLE)

                .livingMotionModifier(KKStyles.WISDOM, LivingMotions.WALK, KKAnimations.WISDOM_RUN)
                .livingMotionModifier(KKStyles.FINAL, LivingMotions.WALK, KKAnimations.FINAL_IDLE)

                .livingMotionModifier(KKStyles.VALOR, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                .livingMotionModifier(KKStyles.WISDOM, LivingMotions.RUN, KKAnimations.WISDOM_RUN)
                .livingMotionModifier(KKStyles.FINAL, LivingMotions.RUN, KKAnimations.FINAL_IDLE)

                .newStyleCombo(KKStyles.VALOR, KKAnimations.VALOR_AUTO1, KKAnimations.VALOR_AUTO2, KKAnimations.VALOR_AUTO1, KKAnimations.VALOR_AUTO3, KKAnimations.VALOR_AUTO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                .newStyleCombo(KKStyles.WISDOM, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_FINISHER, KKAnimations.WISDOM_FINISHER, KKAnimations.WISDOM_COMBO1, Animations.SWORD_AIR_SLASH)
                .newStyleCombo(KKStyles.MASTER, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                .newStyleCombo(KKStyles.FINAL, KKAnimations.FINAL_AUTO1, KKAnimations.FINAL_AUTO1, KKAnimations.FINAL_AUTO1, KKAnimations.FINAL_AUTO1, KKAnimations.FINAL_AUTO1, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)

                .passiveSkill(KKSkills.comboExtender.get());


        //TODO: Add Styles for FF Characters as either base styles or drive forms.




    private EpicRMWeapons() {
    }

    public static void register() {
        EpicFightEventHooks.Registry.WEAPON_CAPABILITY_PRESET.registerEvent(event -> {
            event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, EpicRMWeaponEnum.XEPHIRO.toString().toLowerCase()), XEPHIRO);
        });

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