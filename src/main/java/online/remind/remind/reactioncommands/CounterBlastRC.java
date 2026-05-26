package online.remind.remind.reactioncommands;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import org.joml.Vector3f;
import yesman.epicfight.registry.entries.EpicFightSounds;

import java.util.List;

public class CounterBlastRC extends ReactionCommand {
    public CounterBlastRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck, 20 * 2);
    }

    @Override
    public void onUse(Player player, LivingEntity target, LivingEntity lockedOnEntity) {
        PlayerData playerData = PlayerData.get(player);
        GlobalDataRM globalData = ModDataRM.getGlobal(player);
        float dmg = DamageCalculation.getMagicDamage(player) * 0.20f;
        float dmgMult = (float) (1 + (playerData.getMaxMP() * 0.01F));
        float radius = (float) (0.05F * playerData.getMaxMP());
        globalData.setRCCooldownTicks(60);

        double X = player.getX();
        double Y = player.getY();
        double Z = player.getZ();

        player.swing(InteractionHand.MAIN_HAND);
        PacketHandlerRM.syncGlobalToAllAround(player, globalData);

        List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty(player, player, radius, radius, radius);

        boolean hasLyric2 = playerData.isAbilityEquipped(StringsRM.Lyric2);

        if (hasLyric2) {
            if (player.level() instanceof ServerLevel level) {

                player.level().playSound(
                        null,
                        target.blockPosition(),
                        ModSounds.wisdom_shot.get(),
                        SoundSource.PLAYERS,
                        1F,
                        1F
                );

                double lightningRadius = radius + (playerData.getNumberOfAbilitiesEquipped(Strings.thunderBoost) * 0.10f);

                // Only spawn a few visual bolts total, not hundreds.
                int boltCount = 3;

                for (int i = 0; i < boltCount; i++) {
                    double angle = 2 * Math.PI * player.getRandom().nextDouble();
                    double lx = player.getX() + lightningRadius * Math.cos(angle);
                    double lz = player.getZ() + lightningRadius * Math.sin(angle);

                    BlockPos pos = level.getHeightmapPos(
                            net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                            new BlockPos((int) lx, (int) player.getY(), (int) lz)
                    );

                    LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);

                    if (lightning != null) {
                        lightning.setVisualOnly(true);
                        lightning.moveTo(pos.getX(), pos.getY(), pos.getZ());
                        level.addFreshEntity(lightning);
                    }
                }

                // Damage each target once.
                for (LivingEntity e : targetList) {
                    e.invulnerableTime = 0;

                    e.hurt(
                            KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHTNING, e, player),
                            dmg
                    );

                    float boostDamage = (playerData.getNumberOfAbilitiesEquipped(Strings.thunderBoost) * 0.20f);

                    if (boostDamage > 0) {
                        double dmgBoost = dmg *= boostDamage;
                        e.hurt(e.damageSources().indirectMagic(e, player), (float) (dmg + dmgBoost));
                    }

                    e.level().addParticle(ParticleTypes.ELECTRIC_SPARK,
                            target.getX(), target.getY() + target.getBbHeight(), target.getZ(),
                            0, 0.1, 0);
                }
            }
        } else {
            // Normal visual sphere + damage.
            target.level().playSound(
                    null,
                    target.blockPosition(),
                    EpicFightSounds.LASER_BLAST.get(),
                    SoundSource.PLAYERS,
                    1F,
                    1F
            );

            if (player.level() instanceof ServerLevel level) {
                for (int t = 1; t < 360; t += 20) {
                    for (int s = 1; s < 360; s += 20) {
                        double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                        double y = Y + (radius * Math.cos(Math.toRadians(t)));
                        double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));

                        level.sendParticles(new DustParticleOptions(new Vector3f(1F, 1F, 1F), 1F), x, y, z, 1, 0, 0, 0, 0);
                        level.sendParticles(new DustParticleOptions(new Vector3f(0.6F, 0.7F, 1F), 1F), x, y - 0.25, z, 1, 0, 0, 0, 0);
                        level.sendParticles(new DustParticleOptions(new Vector3f(0.25F, 0.25F, 1F), 1F), x, y - 0.5, z, 1, 0, 0, 0, 0);
                    }
                }
            }

            for (LivingEntity e : targetList) {
                e.knockback(0.25, -e.getX(), -e.getZ());
                e.hurt(e.damageSources().indirectMagic(e, player), dmg * dmgMult);
            }
        }

        playerData.removeReactionCommand(getRegistryName().toString());
    }

    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        PlayerData playerData = PlayerData.get(player);
        if (playerData != null ){
            return playerData.isAbilityEquipped(StringsRM.counterBlast);
        }
        return false;
    }
}

