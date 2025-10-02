package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.entity.magic.SparkEntity;
import online.remind.remind.lib.StringsRM;

public class magicSpark extends Magic {

    public magicSpark(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
        super(registryName, hasToSelect, maxLevel, gmAbility);
    }

    @Override
    public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {
        float dmgMult = getDamageMult(level) + PlayerData.get(player).getNumberOfAbilitiesEquipped(StringsRM.lightBoost) * 0.2F;
        dmgMult *= fullMPBlastMult;

        int pairs;
        double verticalStep;

        switch(level){
            case 0:
                for (int i = 0; i <2; i++){
                    SparkEntity spark = new SparkEntity(player.level(), player, i, dmgMult);
                    spark.setCaster(player.getDisplayName().getString());
                    player.level().addFreshEntity(spark);
                }
                break;
            case 1:
                for (int i = 0; i < 2; i++){
                    SparkEntity spark = new SparkEntity(player.level(), player, i, dmgMult);
                    spark.setCaster(player.getDisplayName().getString());
                    player.level().addFreshEntity(spark);

                    SparkEntity sparkra = new SparkEntity(player.level(), player, i, dmgMult);
                    sparkra.setCaster(player.getDisplayName().getString());
                    sparkra.setPos(sparkra.getX(), sparkra.getY() + 0.5, sparkra.getZ());
                    player.level().addFreshEntity(sparkra);
                }
                break;
            case 2:
                for (int i = 0; i < 2; i++){
                    SparkEntity spark = new SparkEntity(player.level(), player, i, dmgMult);
                    spark.setCaster(player.getDisplayName().getString());
                    player.level().addFreshEntity(spark);

                    SparkEntity sparkra = new SparkEntity(player.level(), player, i, dmgMult);
                    sparkra.setCaster(player.getDisplayName().getString());
                    sparkra.setPos(sparkra.getX(), sparkra.getY() + 0.5, sparkra.getZ());
                    player.level().addFreshEntity(sparkra);

                    SparkEntity sparkga = new SparkEntity(player.level(), player, i, dmgMult);
                    sparkga.setCaster(player.getDisplayName().getString());
                    double radius = 1.2;
                    double angle = Math.toRadians(i * 180);
                    sparkga.setPos(sparkga.getX(), sparkga.getY() + 0.5, sparkga.getZ() + Math.sin(angle) * radius);
                    player.level().addFreshEntity(sparkga);
                }
                break;
        }


    }

    @Override
    protected void playMagicCastSound(Player player, Player player1, int i) {

    }
}
