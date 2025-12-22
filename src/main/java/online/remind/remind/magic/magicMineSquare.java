package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.entity.magic.MineEntity;
import online.remind.remind.lib.StringsRM;

public class magicMineSquare extends Magic {

    int mineCount;
    float spacing = 1.5F;
    float forwardOffset = 2.0F;



    public magicMineSquare(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
        super(registryName, false, maxLevel, gmAbility);
    }


    @Override
    public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {

        Vec3 forward = player.getLookAngle().normalize();
        Vec3 right = forward.cross(new Vec3(0, 1, 0)).normalize();

        // IGlobalCapabilitiesMA globalData = ModCapabilitiesMA.getGlobal(player);
        float dmgMult = getDamageMult(level) + PlayerData.get(player).getNumberOfAbilitiesEquipped(Strings.fireBoost) * 0.2F;
        dmgMult *= fullMPBlastMult;

        switch (level) {
            case 0:
                 spacing = 1.4F;

                Vec3 base = player.position();

                Vec3[] offsets = {
                        new Vec3( spacing, 0,  spacing),
                        new Vec3( spacing, 0, -spacing),
                        new Vec3(-spacing, 0,  spacing),
                        new Vec3(-spacing, 0, -spacing)
                };

                for (Vec3 off : offsets) {
                    Vec3 spawnPos = base.add(off);

                    MineEntity mine = new MineEntity(
                            player.level(),
                            player,
                            0,
                            dmgMult
                    );

                    mine.setMaxTicks(200);
                    mine.setCaster(player.getDisplayName().getString());
                    mine.setPos(spawnPos.x, player.getY(), spawnPos.z);
                    player.level().addFreshEntity(mine);
                }

                break;
            case 1:
                int mineCount = 6;
                double radius = 2.0D;

                base = player.position();

                for (int i = 0; i < mineCount; i++) {
                    double angle = (Math.PI * 2 / mineCount) * i;

                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;

                    Vec3 spawnPos = base.add(x, 0, z);

                    MineEntity mine = new MineEntity(
                            player.level(),
                            player,
                            0,
                            dmgMult
                    );

                    mine.setMaxTicks(220);
                    mine.setCaster(player.getDisplayName().getString());
                    mine.setPos(spawnPos.x, player.getY() + 1.2D, spawnPos.z);

                    player.level().addFreshEntity(mine);
                }
                break;
            case 2:
                mineCount = 8;      // BBS feels ~8
                radius = 2.75F;

                for (int i = 0; i < mineCount; i++) {
                    double angle = (Math.PI * 2 / mineCount) * i;

                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;

                    Vec3 spawnPos = player.position().add(x, 0, z);

                    MineEntity mine = new MineEntity(player.level(), player, i, dmgMult);
                    mine.setMaxTicks(240);
                    mine.setCaster(player.getDisplayName().getString());
                    mine.setPos(spawnPos.x, player.getY(), spawnPos.z);
                    player.level().addFreshEntity(mine);
                }
                break;
            case 3:
                int seekerMineCount = 8;
                radius = 2.5F;

                for (int i = 0; i < seekerMineCount; i++) {
                    double angle = (Math.PI * 2 / seekerMineCount) * i;

                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;

                    Vec3 spawnPos = player.position().add(x, 0, z);

                    MineEntity mine = new MineEntity(player.level(), player, i, dmgMult);
                    mine.setMaxTicks(150);
                    mine.setCaster(player.getDisplayName().getString());
                    mine.setPos(spawnPos.x, player.getY(), spawnPos.z);
                    mine.setSeeker(true);
                    player.level().addFreshEntity(mine);

                }
                break;
        }
    }

    @Override
    protected void playMagicCastSound(Player player, Player player1, int i) {

    }
}
