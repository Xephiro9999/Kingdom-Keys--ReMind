package online.remind.remind.entity.magic;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.entity.ModEntitiesRM;
import org.joml.Vector3f;

import java.util.List;

public class SilenceEntity extends ThrowableProjectile {
    int maxTicks = 100, radius = 2;
    float timeMult;

    WorldData worldData = WorldData.get(level().getServer());

    LivingEntity lockOnEntity;

    public SilenceEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
    }

    public SilenceEntity(Level world) {
        super(ModEntitiesRM.TYPE_SILENCE.get(), world);
        this.blocksBuilding = true;
    }

    public SilenceEntity(Level world, LivingEntity player, float timeMult, LivingEntity lockOnEntity) {
        super(ModEntitiesRM.TYPE_SILENCE.get(), player, world);
        this.timeMult = timeMult;
        this.lockOnEntity = lockOnEntity;
    }

    @Override
    protected double getDefaultGravity() {
        return 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void tick() {
        if (this.tickCount > maxTicks) {
            this.remove(RemovalReason.KILLED);
        }

        //world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
        if(tickCount > 0)
            //level().addParticle(ParticleTypes.SQUID_INK, getX(), getY(), getZ(), 0, 0, 0);
            level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F,1F,1F),1F),getX(), getY(), getZ(), 0, 0, 0);
        level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.5F,0.5F,0.8F),1F),getX() + level().random.nextDouble() - 0.5D, getY(), getZ() + level().random.nextDouble() - 0.5D, 0, 0, 0);


        super.tick();
    }
    @Override
    protected void onHit(HitResult rtRes) {
        if (!level().isClientSide && getOwner() != null) {
            EntityHitResult ertResult = null;
            BlockHitResult brtResult = null;

            if (rtRes instanceof EntityHitResult) {
                ertResult = (EntityHitResult) rtRes;
            }

            if (rtRes instanceof BlockHitResult) {
                brtResult = (BlockHitResult) rtRes;
            }

            if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) ertResult.getEntity();
                PlayerData casterData = PlayerData.get((Player) getOwner());
                if (ertResult != null && ertResult.getEntity() instanceof Player) {
                    PlayerData targetData = PlayerData.get((Player) target);
                    if (target != getOwner()) {
                        Party p = null;
                        if (getOwner() != null) {
                            p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
                        }
                        if(p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
                            double time = (timeMult * (casterData.getMaxMP()/2));
                            //System.out.println(time);
                            if(this.getOwner() instanceof Player) {
                                List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty((Player) this.getOwner(), this, radius,radius,radius);
                                for(LivingEntity e : targetList) {
                                e.addEffect(new MobEffectInstance(ModMobEffectsRM.SILENCE, (int) time, 0, false, false));
                                playSound(ModSoundsRM.SILENCEHIT.get(),1F,1F);
                                }
                            }
                        }
                    }
                } else {
                    remove(RemovalReason.KILLED);
                }
            }

            if (brtResult != null) {
                if (this.getOwner() instanceof Player) {
                    remove(RemovalReason.KILLED);
                }
            }


        }
    }

}
