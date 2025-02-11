package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.magic.FaithEntity;
import online.remind.remind.lib.StringsRM;

public class magicFaith extends Magic {

    public magicFaith(ResourceLocation registryName, int maxLevel, String gmAbility) {
        super(registryName, false, maxLevel, gmAbility);
    }

    @Override
    public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity){

        float dmgMult = getDamageMult(level) + PlayerData.get(player).getNumberOfAbilitiesEquipped(StringsRM.lightBoost) * 0.25F;
        dmgMult *= fullMPBlastMult;

        // Casting Faith go here
        switch(level){
            case 0-> {
                FaithEntity faith = new FaithEntity(player.level(), player, dmgMult, lockOnEntity);
                faith.setOwner(caster);
                faith.setPos(player.getX(), player.getY() + 1.8F, player.getZ());
                player.level().addFreshEntity(faith);
            }
        }

    }

    @Override
    protected void playMagicCastSound(Player player, Player player1, int i) {
        player.level().playSound(null,player.blockPosition(), ModSoundsRM.PLAYER_CAST.get(), SoundSource.PLAYERS,1,1);
    }
}
