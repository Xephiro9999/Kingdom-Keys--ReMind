package online.remind.remind.entity.reactioncommand;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.ability.ModAbilitiesRM;
import online.remind.remind.entity.ModEntitiesRM;
import online.remind.remind.lib.StringsRM;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.registry.entries.EpicFightParticles;
import yesman.epicfight.registry.entries.EpicFightSounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CounterRushCore extends ThrowableProjectile {

    int maxTicks = 300;
    List<LivingEntity> targetList = new ArrayList<LivingEntity>();
    float dmg;
    boolean feverPitch;


    float radius= 5;

    public CounterRushCore(EntityType<? extends ThrowableProjectile> type, Level world){
        super(type, world);
        this.blocksBuilding = true;
    }

    public CounterRushCore(Player player, Level world, List<LivingEntity> targets, float dmg, boolean feverPitch) {
        super(ModEntitiesRM.TYPE_COUNTER_RUSH.get(), player, world);
        setCaster(player.getUUID());
        String targetIDS = "";
        for(Entity t : targets) {
            targetIDS+=","+t.getId();
        }
        setTarget(targetIDS.substring(1));
        this.targetList = targets;
        this.dmg = dmg;
        this.feverPitch = feverPitch;
    }

    @Override
    protected double getDefaultGravity() {
        return 0;
    }

    float hits = 0;

    @Override
    public void tick() {
        if (this.tickCount > maxTicks || getCaster() == null) {
            this.remove(RemovalReason.KILLED);
            hits = 0;
        }

        //Since this is a temporary entity we can do the hits as a field here, otherwise we would need a capability for it
        if(hits <= 0 && getCaster() != null) //This is to prevent in every tick to refill the hits before it finishes
            if (!feverPitch) {
                hits = 4 + (PlayerData.get(getCaster()).getNumberOfAbilitiesEquipped(ModAbilitiesRM.ATTACK_HASTE) * 0.5f);
            } else {
                hits = 4;
            }


        if (getCaster() != null && targetList != null && !targetList.isEmpty() && hits > 0) {
            PlayerData playerData = PlayerData.get(getCaster());



                if (tickCount % 5 == 0 && hits > 0) { //Every 0.25s deal a hit if there are hits available
                    int index = Utils.randomWithRange(0, targetList.size() - 1); //Get a random mob from the list
                    Entity target = targetList.get(index);
                    if (target != null) {
                        float dmg = (float) (playerData.getStrengthStat().get() * 0.25f);
                        if (feverPitch){
                            dmg = (float) (playerData.getStrengthStat().get() * 1.5f);
                        }
                        target.invulnerableTime = 1;
                        target.hurt(target.damageSources().indirectMagic(this, this.getOwner()), dmg);
                        //TODO No EFM 1.21
                        EpicFightParticles.HIT_BLADE.get().spawnParticleWithArgument(((ServerLevel) target.level()), HitParticleType.RANDOM_WITHIN_BOUNDING_BOX, HitParticleType.ZERO, target, target);
                        target.level().playSound(null, target.blockPosition(), EpicFightSounds.BLADE_HIT.get(), SoundSource.PLAYERS, 1F, 1F);

                        hits--; //Marks as that single hit being performed
                    }
                }




            if(hits <= 0) { //Once reaches 0 despawn the entity as it's job it's over
                this.remove(RemovalReason.KILLED);
            }
        }
        super.tick();
    }

    @Override
    protected void onHit(HitResult rtRes) {
        // Damage Here
    }

    public int getMaxTicks() {
        return maxTicks;
    }

    public void setMaxTicks(int maxTicks) {
        this.maxTicks = maxTicks;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.entityData.get(OWNER) != null) {
            compound.putString("OwnerUUID", this.entityData.get(OWNER).get().toString());
            compound.putString("TargetsUUID", this.entityData.get(TARGETS));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
        this.entityData.set(TARGETS, compound.getString("TargetUUID"));
    }

    private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(CounterRushCore.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<String> TARGETS = SynchedEntityData.defineId(CounterRushCore.class, EntityDataSerializers.STRING);

    public Player getCaster() {
        return this.getEntityData().get(OWNER).isPresent() ? this.level().getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
    }

    public void setCaster(UUID uuid) {
        this.entityData.set(OWNER, Optional.of(uuid));
    }

   /* public List<Entity> getTargets() {
        List<Entity> list = new ArrayList<Entity>();
        String[] ids = this.getEntityData().get(TARGETS).split(",");

        for(String id : ids) {

            if(!id.equals(""))
                list.add(level().getEntity(Integer.parseInt(id)));
        }
        return list;
    }*/

    public void setTarget(String lists) {
        this.entityData.set(TARGETS, lists);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(OWNER, Optional.of(new UUID(0L, 0L)));
        builder.define(TARGETS, "");
    }
}
