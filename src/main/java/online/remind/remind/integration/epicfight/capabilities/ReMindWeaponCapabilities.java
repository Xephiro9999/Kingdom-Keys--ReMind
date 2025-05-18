package online.remind.remind.integration.epicfight.capabilities;

import online.kingdomkeys.kingdomkeys.integration.epicfight.capabilities.GuardObject;
import online.remind.remind.integration.epicfight.init.EpicRMWeapons;
import online.remind.remind.integration.epicfight.init.RMStyles;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.HashMap;
import java.util.Map;

public class ReMindWeaponCapabilities extends WeaponCapability {

    private final Map<String, GuardObject> guardMap = new HashMap<>();

    public ReMindWeaponCapabilities(CapabilityItem.Builder builder) {
        super(builder);
        guardMap.put(EpicRMWeapons.EpicRMWeaponEnum.XEPHIRO.toString()+ Styles.TWO_HAND, new GuardObject(Animations.SWORD_DUAL_GUARD_HIT, Animations.BIPED_COMMON_NEUTRALIZED, Animations.SWORD_DUAL_GUARD_HIT));
        guardMap.put(EpicRMWeapons.EpicRMWeaponEnum.XEPHIRO.toString()+ Styles.ONE_HAND, new GuardObject(Animations.SWORD_GUARD_HIT, Animations.BIPED_COMMON_NEUTRALIZED, Animations.SWORD_GUARD_HIT));

        //guardMap.put(EpicRMWeapons.EpicKKWeaponEnum.KK_KEYBLADE.toString()+ RMStyles.VALOR, new GuardObject(Animations.SWORD_DUAL_GUARD_HIT, Animations.BIPED_COMMON_NEUTRALIZED, Animations.SWORD_DUAL_GUARD_HIT));

    }
    @Override
    public StaticAnimation getGuardMotion(GuardSkill skill, GuardSkill.BlockType blockType, PlayerPatch<?> playerpatch) {
        return guardMap.get(this.getWeaponCategory().toString()+this.getStyle(playerpatch)).getGuardAnimation(blockType);
    }

    public Map<String, GuardObject> getGuardMap() {
        return guardMap;
    }

}
