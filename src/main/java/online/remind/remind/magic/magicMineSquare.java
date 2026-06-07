package online.remind.remind.magic;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.magic.Magic;

public class magicMineSquare extends Magic {

    int mineCount;
    float spacing = 1.5F;
    float forwardOffset = 2.0F;



    public magicMineSquare(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
        super(registryName, false, maxLevel, gmAbility);
    }


    @Override
    public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {
        caster.sendSystemMessage(Component.literal("This magic has now been ported to Kingdom Keys, unequip it to get the new spell."));
    }

    @Override
    protected void playMagicCastSound(LivingEntity player, Player player1, int i) {

    }
}
