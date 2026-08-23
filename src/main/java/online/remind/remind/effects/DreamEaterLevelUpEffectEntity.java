package online.remind.remind.entity.effects;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;


public class DreamEaterLevelUpEffectEntity extends Entity {

    public static final int DURATION = 36;

    private static final EntityDataAccessor<Integer> TARGET_ID =
            SynchedEntityData.defineId(
                    DreamEaterLevelUpEffectEntity.class,
                    EntityDataSerializers.INT
            );


    public DreamEaterLevelUpEffectEntity(
            EntityType<? extends DreamEaterLevelUpEffectEntity> type,
            Level level
    ) {
        super(type, level);

        this.noPhysics = true;
        this.setNoGravity(true);
        this.setInvisible(true);
    }


    public DreamEaterLevelUpEffectEntity(
            EntityType<? extends DreamEaterLevelUpEffectEntity> type,
            Level level,
            Entity target
    ) {
        this(type, level);

        if (target != null) {
            this.setTargetId(target.getId());

            this.setPos(
                    target.getX(),
                    target.getY(),
                    target.getZ()
            );
        }
    }


    @Override
    protected void defineSynchedData(
            SynchedEntityData.Builder builder
    ) {
        builder.define(TARGET_ID, -1);
    }


    public int getTargetId() {
        return this.entityData.get(TARGET_ID);
    }


    public void setTargetId(int id) {
        this.entityData.set(TARGET_ID, id);
    }


    public Entity getTargetEntity() {

        int id = getTargetId();

        if (id < 0) {
            return null;
        }

        return this.level().getEntity(id);
    }


    @Override
    public void tick() {
        super.tick();

        Entity target = getTargetEntity();

        /*
         * The synced target ID can arrive a moment after the effect
         * itself on the client, so only the server should kill the
         * effect when the target cannot be found.
         */
        if (target == null) {

            if (!this.level().isClientSide) {
                this.discard();
            }

            return;
        }


        /*
         * Follow the Dream Eater / player.
         */
        this.setPos(
                target.getX(),
                target.getY(),
                target.getZ()
        );


        /*
         * Server owns the lifetime.
         */
        if (!this.level().isClientSide
                && this.tickCount >= DURATION) {

            this.discard();
        }
    }


    /*
     * This is a temporary visual.
     *
     * Never write it into the world save.
     */
    @Override
    public boolean shouldBeSaved() {
        return false;
    }


    @Override
    public boolean isAttackable() {
        return false;
    }


    @Override
    public boolean isPickable() {
        return false;
    }


    @Override
    protected void readAdditionalSaveData(
            CompoundTag tag
    ) {
    }


    @Override
    protected void addAdditionalSaveData(
            CompoundTag tag
    ) {
    }
}