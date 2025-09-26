package online.remind.remind.reactioncommands;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.magic.ThundagaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderBoltEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import org.joml.Vector3f;
import yesman.epicfight.gameasset.EpicFightSounds;

import java.util.List;

@Mod.EventBusSubscriber(modid = KingdomKeysReMind.MODID)
public class CounterBlastRC extends ReactionCommand {
    public CounterBlastRC(ResourceLocation registryName, boolean constantCheck) {
        super(registryName, constantCheck);
    }

    @Override
    public void onUse(Player player, LivingEntity target, LivingEntity lockedOnEntity) {
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        IGlobalCapabilitiesRM globalData = ModCapabilitiesRM.getGlobal(player);
        float dmg = DamageCalculation.getMagicDamage(player) * 0.5f;
        float dmgMult = (float) (1 + (playerData.getMaxMP() * 0.010F));
        float radius = (float) (0.075F * playerData.getMaxMP());
        globalData.setRCCooldownTicks(60);

        double X = player.getX();
        double Y = player.getY();
        double Z = player.getZ();

        globalData.remCanCounter(1);
        player.swing(InteractionHand.MAIN_HAND);
        PacketHandlerRM.syncGlobalToAllAround(player, globalData);

        target.level().playSound(null, target.blockPosition(), EpicFightSounds.LASER_BLAST.get(), SoundSource.PLAYERS, 1F, 1F);


        List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty((player), player, radius, radius, radius);
        for (LivingEntity e : targetList) {
            for (int t = 1; t < 360; t += 20) {
                for (int s = 1; s < 360; s += 20) {
                    double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                    double y = Y + (radius * Math.cos(Math.toRadians(t)));
                    double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                    if (playerData.isAbilityEquipped(StringsRM.Lyric2)){
                        e.hurt(e.damageSources().indirectMagic(e, player), dmg * (playerData.getNumberOfAbilitiesEquipped(Strings.thunderBoost) * 0.25f));
                        if (player.level() instanceof ServerLevel level) {
                        double lightningRadius = radius;
                        int boltCount = 1;

                        for (int i = 0; i < boltCount; i++) {
                            double angle = 2 * Math.PI * player.getRandom().nextDouble();
                            double lx = player.getX() + lightningRadius * Math.cos(angle);
                            double lz = player.getZ() + lightningRadius * Math.sin(angle);

                            BlockPos pos = level.getHeightmapPos(
                                    net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                                    new BlockPos((int) lx, (int) player.getY(), (int) lz)
                            );



                            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                            lightning.setVisualOnly(true);
                            if (lightning != null) {
                                lightning.moveTo(pos.getX(), pos.getY(), pos.getZ());
                                level.addFreshEntity(lightning);
                            }
                        }
                    }
                    } else {
                        ((ServerLevel) player.level()).sendParticles(new DustParticleOptions(new Vector3f(1F,1F,1F),1F),x,y,z,1,0,0,0,0);
                        ((ServerLevel) player.level()).sendParticles(new DustParticleOptions(new Vector3f(0.6F,0.7F,1F),1F),x,y -0.25,z,1,0,0,0,0);
                        ((ServerLevel) player.level()).sendParticles(new DustParticleOptions(new Vector3f(0.25F,0.25F,1F),1F),x,y -0.5,z,1,0,0,0,0);
                        e.knockback(0.5, -e.getX(),-e.getZ());
                        e.hurt(e.damageSources().indirectMagic(e, player), dmg * dmgMult);
                    }

                    }

            }
        }
    }

    @Override
    public boolean conditionsToAppear(Player player, LivingEntity livingEntity) {
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        IGlobalCapabilitiesRM globalData = ModCapabilitiesRM.getGlobal(player);
        if (playerData != null ){
           if (playerData.isAbilityEquipped(StringsRM.counterBlast) && globalData.getCanCounter() == 1 && globalData.getRCCooldownTicks() == 0) {
               return true;
            }
        }
        return false;
    }
}

