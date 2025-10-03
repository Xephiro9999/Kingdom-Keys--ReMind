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

        // base parameters (tweak to taste)
        double baseRadius = 1.0;
        double outerRadius = 1.6;
        double baseHeight = 1.0;
        double heightStep = 0.5;
        double speed = 0.12; // orbitSpeed (used in SparkEntity.setOrbitSpeed) - larger = faster

        switch(level){
            case 0:
                // two opposite sparks, opposite directions
                for (int i = 0; i < 2; i++){
                    SparkEntity spark = new SparkEntity(player.level(), player, i, dmgMult);
                    spark.setCaster(player.getDisplayName().getString());
                    spark.setAngleOffset(i * Math.PI); // 0 and PI -> opposite
                    spark.setDirection(i == 0 ? 1 : -1);
                    spark.setOrbitRadius(baseRadius);
                    spark.setOrbitSpeed(speed);
                    spark.setVerticalOffset(0.9);
                    player.level().addFreshEntity(spark);
                }
                break;

            case 1:
                // two low + two slightly above; alternate directions per pair
                for (int i = 0; i < 2; i++){
                    SparkEntity spark = new SparkEntity(player.level(), player, i, dmgMult);
                    spark.setCaster(player.getDisplayName().getString());
                    spark.setAngleOffset(i * Math.PI);
                    spark.setDirection(i == 0 ? 1 : -1);
                    spark.setOrbitRadius(baseRadius);
                    spark.setOrbitSpeed(speed);
                    spark.setVerticalOffset(baseHeight);
                    player.level().addFreshEntity(spark);

                    SparkEntity sparkra = new SparkEntity(player.level(), player, i+2, dmgMult);
                    sparkra.setCaster(player.getDisplayName().getString());
                    sparkra.setAngleOffset(i * Math.PI + Math.PI/8); // slight phase shift so they don't overlap perfectly
                    sparkra.setDirection(i == 0 ? -1 : 1); // opposite direction of the lower one
                    sparkra.setOrbitRadius(baseRadius);
                    sparkra.setOrbitSpeed(speed);
                    sparkra.setVerticalOffset(baseHeight + heightStep);
                    player.level().addFreshEntity(sparkra);
                }
                break;

            case 2:
                // base pair, upper pair, outer pair
                for (int i = 0; i < 2; i++){
                    SparkEntity spark = new SparkEntity(player.level(), player, i, dmgMult);
                    spark.setCaster(player.getDisplayName().getString());
                    spark.setAngleOffset(i * Math.PI);
                    spark.setDirection(i == 0 ? 1 : -1);
                    spark.setOrbitRadius(baseRadius);
                    spark.setOrbitSpeed(speed);
                    spark.setVerticalOffset(baseHeight);
                    player.level().addFreshEntity(spark);

                    SparkEntity sparkra = new SparkEntity(player.level(), player, i+2, dmgMult);
                    sparkra.setCaster(player.getDisplayName().getString());
                    sparkra.setAngleOffset(i * Math.PI + Math.PI/6);
                    sparkra.setDirection(i == 0 ? -1 : 1);
                    sparkra.setOrbitRadius(baseRadius + 0.15);
                    sparkra.setOrbitSpeed(speed);
                    sparkra.setVerticalOffset(baseHeight + heightStep);
                    player.level().addFreshEntity(sparkra);

                    SparkEntity sparkga = new SparkEntity(player.level(), player, i+4, dmgMult);
                    sparkga.setCaster(player.getDisplayName().getString());
                    sparkga.setAngleOffset(i * Math.PI + Math.PI/4);
                    sparkga.setDirection(i == 0 ? 1 : -1);
                    sparkga.setOrbitRadius(outerRadius);
                    sparkga.setOrbitSpeed(speed * 1.05);
                    sparkga.setVerticalOffset(baseHeight + heightStep + 0.25);
                    player.level().addFreshEntity(sparkga);
                }
                break;
        }
    }

    @Override
    protected void playMagicCastSound(Player player, Player player1, int i) {
        // sound handled elsewhere if desired
    }
}
