package online.remind.remind.entity.enemies;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.item.ModItemsRM;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;

import java.util.Objects;
import java.util.UUID;

public class TonberryEntity extends Monster implements GeoEntity {

    private static final int LIGHT_UPDATE_INTERVAL_TICKS = 4;

    private BlockPos activeLightBlockPos = null;
    private int lightUpdateTicks = 0;

    protected static final int ACTION_NONE = 0;
    protected static final int ACTION_STAB = 1;
    protected static final int ACTION_GRUDGE = 2;
    protected static final int ACTION_DEATH = 3;

    private static final int STAB_ACTION_TICKS = 26;
    private static final int STAB_DAMAGE_DELAY_TICKS = 13;

    private static final int GRUDGE_ACTION_TICKS = 52;
    private static final int EVERYONES_GRUDGE_DAMAGE_DELAY_TICKS = 28;

    private static final int DEATH_ACTION_TICKS = 80;

    private static final int EVERYONES_GRUDGE_COUNTER_TURNS = 2;
    private static final int EVERYONES_GRUDGE_DAMAGE_PER_KILL = 1;
    private static final int EVERYONES_GRUDGE_COOLDOWN_TICKS = 20 * 20;

    private static final double STAB_EXTRA_REACH = 1.15D;
    private static final double GRUDGE_ANNOUNCE_RANGE = 36.0D;

    private static final EntityDataAccessor<Integer> ACTION =
            SynchedEntityData.defineId(TonberryEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> ACTION_TICKS =
            SynchedEntityData.defineId(TonberryEntity.class, EntityDataSerializers.INT);

    protected static final RawAnimation IDLE =
            RawAnimation.begin().thenLoop("idle");

    protected static final RawAnimation WALK =
            RawAnimation.begin().thenLoop("walk");

    protected static final RawAnimation STAB =
            RawAnimation.begin().thenPlay("stab");

    protected static final RawAnimation GRUDGE =
            RawAnimation.begin().thenPlay("grudge");

    protected static final RawAnimation DEATH =
            RawAnimation.begin().thenPlayAndHold("death");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private UUID currentCombatTargetUUID = null;
    private int turnsTakenAgainstCurrentTarget = 0;

    private int pendingStabTargetId = -1;
    private int pendingStabDamageDelay = 0;

    private int grudgeCooldownTicks = 0;
    private int pendingGrudgeTargetId = -1;
    private int pendingGrudgeDamageDelay = 0;
    private float pendingGrudgeDamage = 0.0F;

    public TonberryEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 20;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 152.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.ATTACK_SPEED, 0.5D)
                .add(Attributes.MOVEMENT_SPEED, 0.10D)
                .add(Attributes.FOLLOW_RANGE, 36.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.35D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        builder.define(ACTION, ACTION_NONE);
        builder.define(ACTION_TICKS, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.65D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isPlayingDeathAnimation()
                || this.isPlayingGrudgeAnimation()
                || this.isPlayingStabAnimation()) {
            lockMovement();
        }

        if (!this.level().isClientSide) {
            tickTonberryLight();
            updateCombatTargetTracking();
            tickTonberryAction();
            tickDelayedStab();
            tickEveryonesGrudge();
        }
    }

    private void tickTonberryLight() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        if (this.lightUpdateTicks > 0) {
            this.lightUpdateTicks--;
            return;
        }

        this.lightUpdateTicks = LIGHT_UPDATE_INTERVAL_TICKS;

        BlockPos lightPos = getTonberryLightPosition();

        if (this.activeLightBlockPos != null && !this.activeLightBlockPos.equals(lightPos)) {
            removeTonberryLightBlock(serverLevel, this.activeLightBlockPos);
            this.activeLightBlockPos = null;
        }

        if (!canPlaceTonberryLight(serverLevel, lightPos)) {
            return;
        }

        int lightLevel = getTonberryLightLevel();

        serverLevel.setBlock(
                lightPos,
                Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, lightLevel),
                3
        );

        this.activeLightBlockPos = lightPos.immutable();
    }

    private BlockPos getTonberryLightPosition() {
        if (this instanceof TonberryKingEntity) {
            return this.blockPosition().above(2);
        }

        return this.blockPosition().above();
    }

    protected int getTonberryLightLevel() {
        if (this instanceof TonberryKingEntity) {
            return 15;
        }

        return 12;
    }

    private boolean canPlaceTonberryLight(ServerLevel serverLevel, BlockPos pos) {
        BlockState state = serverLevel.getBlockState(pos);

        return state.isAir() || state.is(Blocks.LIGHT);
    }

    private void removeTonberryLightBlock(ServerLevel serverLevel, BlockPos pos) {
        BlockState state = serverLevel.getBlockState(pos);

        if (state.is(Blocks.LIGHT)) {
            serverLevel.removeBlock(pos, false);
        }
    }

    private void removeTonberryLightBlock() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        if (this.activeLightBlockPos == null) {
            return;
        }

        removeTonberryLightBlock(serverLevel, this.activeLightBlockPos);
        this.activeLightBlockPos = null;
    }

    @Override
    public void remove(RemovalReason reason) {
        removeTonberryLightBlock();
        super.remove(reason);
    }

    private void updateCombatTargetTracking() {
        LivingEntity target = this.getTarget();
        UUID targetUUID = target == null ? null : target.getUUID();

        if (!Objects.equals(this.currentCombatTargetUUID, targetUUID)) {
            this.currentCombatTargetUUID = targetUUID;
            this.turnsTakenAgainstCurrentTarget = 0;
        }
    }

    private void tickTonberryAction() {
        int ticks = this.getTonberryActionTicks();

        if (ticks <= 0) {
            if (this.getTonberryAction() != ACTION_NONE && !this.isDeadOrDying()) {
                this.setTonberryAction(ACTION_NONE, 0);
            }

            return;
        }

        this.entityData.set(ACTION_TICKS, ticks - 1);

        if (ticks - 1 <= 0 && !this.isDeadOrDying()) {
            this.setTonberryAction(ACTION_NONE, 0);
        }
    }

    private void tickDelayedStab() {
        if (this.pendingStabDamageDelay <= 0) {
            return;
        }

        this.pendingStabDamageDelay--;

        if (this.pendingStabDamageDelay <= 0) {
            applyPendingStabDamage();
        }
    }

    private void tickEveryonesGrudge() {
        if (this.grudgeCooldownTicks > 0) {
            this.grudgeCooldownTicks--;
        }

        if (this.pendingGrudgeDamageDelay <= 0) {
            return;
        }

        this.pendingGrudgeDamageDelay--;

        if (this.pendingGrudgeDamageDelay <= 0) {
            applyPendingEveryonesGrudge();
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (this.level().isClientSide) {
            return false;
        }

        if (!(target instanceof LivingEntity livingTarget)) {
            return false;
        }

        if (!livingTarget.isAlive()) {
            return false;
        }

        if (this.isDeadOrDying()
                || this.isPlayingStabAnimation()
                || this.isPlayingGrudgeAnimation()
                || this.pendingStabTargetId != -1) {
            return false;
        }

        startDelayedStab(livingTarget);
        recordTonberryTurn(livingTarget);

        return true;
    }

    private void startDelayedStab(LivingEntity target) {
        this.pendingStabTargetId = target.getId();
        this.pendingStabDamageDelay = STAB_DAMAGE_DELAY_TICKS;

        this.triggerStabAnimation();

        this.level().playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP,
                this.getSoundSource(),
                0.45F,
                0.65F
        );
    }

    private void applyPendingStabDamage() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            clearPendingStab();
            return;
        }

        Entity entity = serverLevel.getEntity(this.pendingStabTargetId);

        if (!(entity instanceof LivingEntity target) || !target.isAlive()) {
            clearPendingStab();
            return;
        }

        if (!isTargetInStabRange(target)) {
            clearPendingStab();
            return;
        }

        target.invulnerableTime = 0;

        /*
         * Damage is applied here, on the stab animation hit frame.
         * doHurtTarget(...) itself only starts the animation/wind-up.
         */
        super.doHurtTarget(target);

        serverLevel.playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.PLAYER_ATTACK_CRIT,
                this.getSoundSource(),
                0.55F,
                0.75F
        );

        clearPendingStab();
    }

    private boolean isTargetInStabRange(LivingEntity target) {
        double reach = this.getBbWidth() + target.getBbWidth() + STAB_EXTRA_REACH;
        return this.distanceToSqr(target) <= reach * reach;
    }

    private void clearPendingStab() {
        this.pendingStabTargetId = -1;
        this.pendingStabDamageDelay = 0;
    }

    private void recordTonberryTurn(Entity target) {
        if (target == null) {
            return;
        }

        UUID targetUUID = target.getUUID();

        if (!Objects.equals(this.currentCombatTargetUUID, targetUUID)) {
            this.currentCombatTargetUUID = targetUUID;
            this.turnsTakenAgainstCurrentTarget = 0;
        }

        this.turnsTakenAgainstCurrentTarget++;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);

        if (!hurt) {
            return false;
        }

        if (this.level().isClientSide) {
            return true;
        }

        if (this.isDeadOrDying()) {
            return true;
        }

        Entity attacker = source.getEntity();

        if (this.usesEveryonesGrudge() && attacker instanceof LivingEntity livingAttacker) {
            tryStartEveryonesGrudge(livingAttacker);
        }

        return true;
    }

    protected boolean usesEveryonesGrudge() {
        return true;
    }

    private void tryStartEveryonesGrudge(LivingEntity attacker) {
        if (attacker == null || !attacker.isAlive()) {
            return;
        }

        if (this.isDeadOrDying()) {
            return;
        }

        if (this.grudgeCooldownTicks > 0) {
            return;
        }

        if (this.isPlayingGrudgeAnimation()) {
            return;
        }

        if (this.pendingGrudgeTargetId != -1) {
            return;
        }

        UUID attackerUUID = attacker.getUUID();

        if (!Objects.equals(this.currentCombatTargetUUID, attackerUUID)) {
            this.currentCombatTargetUUID = attackerUUID;
            this.turnsTakenAgainstCurrentTarget = 0;
        }

        /*
         * FF rule adapted:
         * Everyone's Grudge counterattacks if Tonberry is attacked
         * before it takes 2 turns.
         *
         * Here, one "turn" means Tonberry has started a stab attack
         * against that target.
         */
        if (this.turnsTakenAgainstCurrentTarget >= EVERYONES_GRUDGE_COUNTER_TURNS) {
            return;
        }

        int targetKills = getEnemyKills(attacker);
        float grudgeDamage = targetKills * EVERYONES_GRUDGE_DAMAGE_PER_KILL;

        if (grudgeDamage <= 0.0F) {
            return;
        }

        this.pendingGrudgeTargetId = attacker.getId();
        this.pendingGrudgeDamage = grudgeDamage;
        this.pendingGrudgeDamageDelay = EVERYONES_GRUDGE_DAMAGE_DELAY_TICKS;
        this.grudgeCooldownTicks = EVERYONES_GRUDGE_COOLDOWN_TICKS;

        this.triggerGrudgeAnimation();
        announceEveryonesGrudge(attacker, targetKills, grudgeDamage);
    }

    private int getEnemyKills(LivingEntity target) {
        if (target instanceof ServerPlayer serverPlayer) {
            return serverPlayer.getStats().getValue(Stats.CUSTOM.get(Stats.MOB_KILLS));
        }

        return 0;
    }

    private void announceEveryonesGrudge(LivingEntity target, int kills, float damage) {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Component message = Component.literal("Everyone's Grudge")
                .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD);


        for (ServerPlayer player : serverLevel.players()) {
            if (player.distanceToSqr(this) <= GRUDGE_ANNOUNCE_RANGE * GRUDGE_ANNOUNCE_RANGE) {
                player.displayClientMessage(message.copy(), true);
            }
        }

        serverLevel.playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SoundEvents.WITHER_AMBIENT,
                this.getSoundSource(),
                0.8F,
                0.55F
        );
    }

    private void applyPendingEveryonesGrudge() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            clearPendingEveryonesGrudge();
            return;
        }

        Entity entity = serverLevel.getEntity(this.pendingGrudgeTargetId);

        if (!(entity instanceof LivingEntity target) || !target.isAlive()) {
            clearPendingEveryonesGrudge();
            return;
        }

        target.invulnerableTime = 0;
        target.hurt(this.damageSources().mobAttack(this), this.pendingGrudgeDamage);

        serverLevel.playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.WITHER_SHOOT,
                this.getSoundSource(),
                0.8F,
                0.65F
        );

        clearPendingEveryonesGrudge();
    }

    private void clearPendingEveryonesGrudge() {
        this.pendingGrudgeTargetId = -1;
        this.pendingGrudgeDamage = 0.0F;
        this.pendingGrudgeDamageDelay = 0;
    }

    @Override
    public void die(DamageSource damageSource) {
        if (!this.level().isClientSide) {
            this.setTonberryAction(ACTION_DEATH, DEATH_ACTION_TICKS);
        }

        lockMovement();

        super.die(damageSource);
    }

    @Override
    protected void tickDeath() {
        this.deathTime++;

        lockMovement();

        if (!this.level().isClientSide && this.deathTime >= DEATH_ACTION_TICKS) {
            this.level().broadcastEntityEvent(this, (byte) 60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    private void lockMovement() {
        this.getNavigation().stop();
        this.setDeltaMovement(Vec3.ZERO);
        this.hasImpulse = true;
        this.hurtMarked = true;
    }

    public void triggerStabAnimation() {
        if (this.isDeadOrDying()) {
            return;
        }

        this.setTonberryAction(ACTION_STAB, STAB_ACTION_TICKS);
    }

    public void triggerGrudgeAnimation() {
        if (this.isDeadOrDying()) {
            return;
        }

        this.setTonberryAction(ACTION_GRUDGE, GRUDGE_ACTION_TICKS);
    }

    protected void setTonberryAction(int action, int ticks) {
        this.entityData.set(ACTION, action);
        this.entityData.set(ACTION_TICKS, Math.max(0, ticks));
    }

    public int getTonberryAction() {
        return this.entityData.get(ACTION);
    }

    public int getTonberryActionTicks() {
        return this.entityData.get(ACTION_TICKS);
    }

    public boolean isPlayingStabAnimation() {
        return this.getTonberryAction() == ACTION_STAB && this.getTonberryActionTicks() > 0;
    }

    public boolean isPlayingGrudgeAnimation() {
        return this.getTonberryAction() == ACTION_GRUDGE && this.getTonberryActionTicks() > 0;
    }

    public boolean isPlayingDeathAnimation() {
        return this.getTonberryAction() == ACTION_DEATH || this.isDeadOrDying();
    }

    private boolean shouldPlayWalkAnimation() {
        if (this.isPlayingStabAnimation()
                || this.isPlayingGrudgeAnimation()
                || this.isPlayingDeathAnimation()) {
            return false;
        }

        if (this.getNavigation() != null && !this.getNavigation().isDone()) {
            return true;
        }

        return this.getDeltaMovement().horizontalDistanceSqr() > 0.00001D;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundsRM.TONBERRY_ALIVE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundsRM.TONBERRY_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.STONE_BUTTON_CLICK_ON, 0.12F, 0.55F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 2, state -> {
            if (this.isPlayingDeathAnimation()) {
                state.setAnimation(DEATH);
                return PlayState.CONTINUE;
            }

            if (this.isPlayingGrudgeAnimation()) {
                state.setAnimation(GRUDGE);
                return PlayState.CONTINUE;
            }

            if (this.isPlayingStabAnimation()) {
                state.setAnimation(STAB);
                return PlayState.CONTINUE;
            }

            if (this.shouldPlayWalkAnimation()) {
                state.setAnimation(WALK);
                return PlayState.CONTINUE;
            }

            state.setAnimation(IDLE);
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static boolean canSpawnUndergroundCave(
            EntityType<? extends Monster> type,
            net.minecraft.world.level.ServerLevelAccessor level,
            net.minecraft.world.entity.MobSpawnType spawnType,
            BlockPos pos,
            net.minecraft.util.RandomSource random
    ) {
        /*
         * Only Overworld-style underground caves.
         */
        if (level.getLevel().dimension() != Level.OVERWORLD) {
            return false;
        }

        /*
         * No surface spawns.
         */
        if (level.canSeeSky(pos)) {
            return false;
        }

        /*
         * Keep them meaningfully underground.
         */
        if (pos.getY() > level.getSeaLevel() - 12) {
            return false;
        }

        /*
         * Do not spawn in tiny tunnels.
         */
        if (!hasLargeCaveSpace(level, pos)) {
            return false;
        }

        /*
         * Needs solid ground.
         */
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);

        if (!belowState.isFaceSturdy(level, below, net.minecraft.core.Direction.UP)) {
            return false;
        }

        /*
         * Needs room for the entity.
         */
        if (!level.isEmptyBlock(pos) || !level.isEmptyBlock(pos.above())) {
            return false;
        }

        /*
         * Still obey vanilla monster spawning rules.
         */
        return Monster.checkMonsterSpawnRules(type, level, spawnType, pos, random);
    }

    private static boolean hasLargeCaveSpace(net.minecraft.world.level.ServerLevelAccessor level, BlockPos center) {
        int airBlocks = 0;
        int tallColumns = 0;

        for (int dx = -5; dx <= 5; dx++) {
            for (int dz = -5; dz <= 5; dz++) {
                boolean columnHasStandingRoom =
                        level.isEmptyBlock(center.offset(dx, 0, dz))
                                && level.isEmptyBlock(center.offset(dx, 1, dz))
                                && level.isEmptyBlock(center.offset(dx, 2, dz));

                if (columnHasStandingRoom) {
                    tallColumns++;
                }

                for (int dy = -1; dy <= 4; dy++) {
                    BlockPos checkPos = center.offset(dx, dy, dz);

                    if (level.isEmptyBlock(checkPos)) {
                        airBlocks++;
                    }
                }
            }
        }

        /*
         * These are the "large cave" requirements.
         * Lower these if Tonberries feel too rare.
         */
        return airBlocks >= 230 && tallColumns >= 18;
    }

    @Override
    protected void dropCustomDeathLoot(
            ServerLevel level,
            DamageSource damageSource,
            boolean recentlyHit
    ) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);

        // Tonberry King: guaranteed Chef's Knife
        if (this instanceof TonberryKingEntity) {
            this.spawnAtLocation(
                    new ItemStack(ModItemsRM.chefsKnife.get(), 1)
            );
            return;
        }

        // Normal Tonberry
        if (this.getRandom().nextFloat() < 0.75F) {
            this.spawnAtLocation(
                    new ItemStack(ModItemsRM.chefsKnife.get(), 1)
            );
        }
    }
}