package online.remind.remind.magic;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.magic.Magic;

public class magicMineShield extends Magic {

    public magicMineShield(ResourceLocation registryName, boolean hasToSelect, int tier, String gmAbility) {
        super(registryName, false, gmAbility);
        setTier(tier);
    }


    @Override
    public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnTarget) {
        caster.sendSystemMessage(Component.literal("This magic has now been ported to Kingdom Keys, unequip it to get the new spell."));
    }

    @Override
    public void playMagicCastSound(LivingEntity player, Player caster) {

    }
}
