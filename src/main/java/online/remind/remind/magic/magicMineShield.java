package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.entity.magic.MineEntity;

public class magicMineShield extends Magic {

    int mineCount;
    float spacing = 1.5F;
    float forwardOffset = 2.0F;



    public magicMineShield(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
        super(registryName, false, maxLevel, gmAbility);
    }


    @Override
    public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {

        // IGlobalCapabilitiesMA globalData = ModCapabilitiesMA.getGlobal(player);
        float dmgMult = getDamageMult(level) + PlayerData.get(player).getNumberOfAbilitiesEquipped(Strings.fireBoost) * 0.2F;
        dmgMult *= fullMPBlastMult;

// Horizontal forward vector only
        Vec3 look = player.getLookAngle();
        Vec3 forward = new Vec3(look.x, 0, look.z).normalize();
        Vec3 right = forward.cross(new Vec3(0, 1, 0)).normalize();

        Vec3 base = player.position().add(forward.scale(forwardOffset));

        switch (level) {

            case 0 -> { // LV1 → 3 mines
                float spacing = 1.4F;
                int mineCount = 3;

                for (int i = 0; i < mineCount; i++) {
                    float offset = (i - (mineCount - 1) / 2.0F) * spacing;
                    Vec3 spawnPos = base.add(right.scale(offset));
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
            }

            case 1 -> { // LV2 → 5 mines
                float spacing = 1.4F;
                int mineCount = 5;

                for (int i = 0; i < mineCount; i++) {
                    float offset = (i - (mineCount - 1) / 2.0F) * spacing;
                    Vec3 spawnPos = base.add(right.scale(offset));


                    MineEntity mine = new MineEntity(
                            player.level(),
                            player,
                            0,
                            dmgMult
                    );

                    mine.setMaxTicks(220);
                    mine.setCaster(player.getDisplayName().getString());
                    mine.setPos(spawnPos.x, player.getY(), spawnPos.z);
                    player.level().addFreshEntity(mine);
                }
            }

            case 2 -> { // LV3 → 7 mines
                float spacing = 1.4F;
                int mineCount = 7;

                for (int i = 0; i < mineCount; i++) {
                    float offset = (i - (mineCount - 1) / 2.0F) * spacing;
                    Vec3 spawnPos = base.add(right.scale(offset));

                    MineEntity mine = new MineEntity(
                            player.level(),
                            player,
                            0,
                            dmgMult
                    );

                    mine.setMaxTicks(240);
                    mine.setCaster(player.getDisplayName().getString());
                    mine.setPos(spawnPos.x, player.getY(), spawnPos.z);
                    player.level().addFreshEntity(mine);
                }
            }

            case 3 -> { // LV3 → 7 mines
                float spacing = 1.4F;
                int mineCount = 7;

                for (int i = 0; i < mineCount; i++) {
                    float offset = (i - (mineCount - 1) / 2.0F) * spacing;
                    Vec3 spawnPos = base.add(right.scale(offset));

                    MineEntity mine = new MineEntity(
                            player.level(),
                            player,
                            0,
                            dmgMult
                    );

                    mine.setSeeker(true);

                    mine.setMaxTicks(240);
                    mine.setCaster(player.getDisplayName().getString());
                    mine.setPos(spawnPos.x, player.getY(), spawnPos.z);
                    player.level().addFreshEntity(mine);
                }
            }
        }
    }

    @Override
    protected void playMagicCastSound(Player player, Player player1, int i) {

    }
}
