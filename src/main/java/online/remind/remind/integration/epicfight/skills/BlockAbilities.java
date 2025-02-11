/* TODO No EFM 1.21
package online.remind.remind.integration.epicfight.skills;

import com.google.common.collect.Maps;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.remind.remind.lib.StringsRM;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public class BlockAbilities extends GuardSkill {
    protected static final UUID EVENT_UUID = UUID.fromString("b422f7a0-f378-11eb-9a03-0242ac130003");
    protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> guardMotions = Maps.newHashMap();
    protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> advancedGuardMotions = Maps.newHashMap();
    protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> guardBreakMotions = Maps.newHashMap();

    public BlockAbilities(Builder builder) {
        super(builder);
    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
    }

    public static GuardSkill.Builder createGuardBuilder() {
        return (new GuardSkill.Builder())
                .setCategory(SkillCategories.GUARD)
                .setActivateType(ActivateType.ONE_SHOT)
                .setResource(Resource.STAMINA)
                .addGuardMotion(CapabilityItem.WeaponCategories.AXE, (item, player) -> Animations.SWORD_GUARD_HIT)
                .addGuardMotion(WeaponCategories.GREATSWORD, (item, player) -> Animations.GREATSWORD_GUARD_HIT)
                .addGuardMotion(WeaponCategories.UCHIGATANA, (item, player) -> Animations.UCHIGATANA_GUARD_HIT)
                .addGuardMotion(WeaponCategories.LONGSWORD, (item, player) -> Animations.LONGSWORD_GUARD_HIT)
                .addGuardMotion(WeaponCategories.SPEAR, (item, player) -> item.getStyle(player) == Styles.TWO_HAND ? Animations.SPEAR_GUARD_HIT : null)
                .addGuardMotion(WeaponCategories.SWORD, (item, player) -> item.getStyle(player) == Styles.ONE_HAND ? Animations.SWORD_GUARD_HIT : Animations.SWORD_DUAL_GUARD_HIT)
                .addGuardMotion(WeaponCategories.TACHI, (item, player) -> Animations.LONGSWORD_GUARD_HIT)
                .addGuardBreakMotion(WeaponCategories.AXE, (item, player) -> Animations.BIPED_COMMON_NEUTRALIZED)
                .addGuardBreakMotion(WeaponCategories.GREATSWORD, (item, player) -> Animations.GREATSWORD_GUARD_BREAK)
                .addGuardBreakMotion(WeaponCategories.UCHIGATANA, (item, player) -> Animations.BIPED_COMMON_NEUTRALIZED)
                .addGuardBreakMotion(WeaponCategories.LONGSWORD, (item, player) -> Animations.BIPED_COMMON_NEUTRALIZED)
                .addGuardBreakMotion(WeaponCategories.SPEAR, (item, player) -> Animations.BIPED_COMMON_NEUTRALIZED)
                .addGuardBreakMotion(WeaponCategories.SWORD, (item, player) -> Animations.BIPED_COMMON_NEUTRALIZED)
                .addGuardBreakMotion(WeaponCategories.TACHI, (item, player) -> Animations.BIPED_COMMON_NEUTRALIZED);
    }



    @Override
    public void dealEvent(PlayerPatch<?> playerpatch, HurtEvent.Pre event, boolean advanced) {

        event.setCanceled(true);
        event.setResult(AttackResult.ResultType.BLOCKED);
        Player player = playerpatch.getOriginal();
        IPlayerCapabilities playerCapabilities = ModCapabilities.getPlayer(player);


        // Block Abilities Effects


        if(playerCapabilities.isAbilityEquipped(StringsRM.renewalBlock)) {
            player.heal(player.getMaxHealth() * 0.05F);
            System.out.println("Healed on Block!");

        }

        if(playerCapabilities.isAbilityEquipped(StringsRM.focusBlock)) {
            playerCapabilities.addFocus(10);
            System.out.println("Focus Restored on Block!");

        }
        
    }

    @Override
    protected boolean isBlockableSource(DamageSource damageSource, boolean advanced) {
        return (damageSource.is(DamageTypeTags.IS_PROJECTILE) && advanced) || super.isBlockableSource(damageSource, false);
    }

    @Override
    protected boolean isAdvancedGuard() {
        return true;
    }


}
*/