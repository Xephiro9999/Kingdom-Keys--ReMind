package online.remind.remind.entity.enemies;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.item.ModItemsRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.ArrayList;
import java.util.List;

public class TonberryKingEntity extends TonberryEntity {

    private static final int ITS_SHARP_DAMAGE_PER_KILL = 2;
    private static final int ITS_SHARP_DAMAGE_DELAY_TICKS = 14;
    private static final int ITS_SHARP_COOLDOWN_TICKS = 80;
    private static final float ITS_SHARP_CHANCE = 0.35F;

    private static final int JUNK_ATTACKS_REQUIRED = 5;
    private static final int JUNK_DAMAGE_DELAY_TICKS = 26;
    private static final int JUNK_COOLDOWN_TICKS = 40;
    private static final double JUNK_RANGE = 22.0D;

    private static final int JUNK_MIN_DAMAGE = 16;
    private static final int JUNK_MAX_DAMAGE = 48;

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.literal("Tonberry King"),
            BossEvent.BossBarColor.GREEN,
            BossEvent.BossBarOverlay.PROGRESS
    );

    private int sharpCooldownTicks = 0;
    private int pendingSharpTargetId = -1;
    private int pendingSharpDamageDelay = 0;
    private float pendingSharpDamage = 0.0F;

    private int junkHitCounter = 0;
    private int junkCooldownTicks = 0;
    private int pendingJunkDamageDelay = 0;

    public TonberryKingEntity(EntityType<? extends TonberryEntity> type, Level level) {
        super((EntityType<? extends Monster>) type, level);
        this.xpReward = 250;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TonberryEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 2800.0D)
                .add(Attributes.ATTACK_DAMAGE, 18.0D)
                .add(Attributes.ATTACK_SPEED, 0.55D)
                .add(Attributes.MOVEMENT_SPEED, 0.115D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.ARMOR, 14.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    /*
     * Tonberry King does NOT use Everyone's Grudge.
     * It uses It's Sharp! and Junk instead.
     */
    @Override
    protected boolean usesEveryonesGrudge() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

            tickSharp();
            tickJunk();
        }
    }

    private void tickSharp() {
        if (this.sharpCooldownTicks > 0) {
            this.sharpCooldownTicks--;
        }

        if (this.pendingSharpDamageDelay <= 0) {
            return;
        }

        this.pendingSharpDamageDelay--;

        if (this.pendingSharpDamageDelay <= 0) {
            applyPendingItsSharp();
        }
    }

    private void tickJunk() {
        if (this.junkCooldownTicks > 0) {
            this.junkCooldownTicks--;
        }

        if (this.pendingJunkDamageDelay <= 0) {
            return;
        }

        this.pendingJunkDamageDelay--;

        if (this.pendingJunkDamageDelay <= 0) {
            applyPendingJunk();
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

        if (canUseItsSharp(livingTarget)) {
            startItsSharp(livingTarget);
            return true;
        }

        /*
         * Regular physical attack.
         * This uses TonberryEntity's delayed stab timing.
         */
        return super.doHurtTarget(target);
    }

    private boolean canUseItsSharp(LivingEntity target) {
        if (this.sharpCooldownTicks > 0) {
            return false;
        }

        if (this.pendingSharpTargetId != -1) {
            return false;
        }

        if (this.isDeadOrDying()) {
            return false;
        }

        if (this.isPlayingStabAnimation() || this.isPlayingGrudgeAnimation()) {
            return false;
        }

        if (getEnemyKills(target) <= 0) {
            return false;
        }

        return this.random.nextFloat() < ITS_SHARP_CHANCE;
    }

    private void startItsSharp(LivingEntity target) {
        int kills = getEnemyKills(target);
        float damage = kills * ITS_SHARP_DAMAGE_PER_KILL;

        this.pendingSharpTargetId = target.getId();
        this.pendingSharpDamage = damage;
        this.pendingSharpDamageDelay = ITS_SHARP_DAMAGE_DELAY_TICKS;
        this.sharpCooldownTicks = ITS_SHARP_COOLDOWN_TICKS;

        this.triggerStabAnimation();
        announceItsSharp(target, kills, damage);

        this.level().playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SoundEvents.TRIDENT_THROW,
                this.getSoundSource(),
                0.85F,
                0.65F
        );
    }

    private void applyPendingItsSharp() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            clearPendingItsSharp();
            return;
        }

        Entity entity = serverLevel.getEntity(this.pendingSharpTargetId);

        if (!(entity instanceof LivingEntity target) || !target.isAlive()) {
            clearPendingItsSharp();
            return;
        }

        target.invulnerableTime = 0;
        target.hurt(this.damageSources().mobAttack(this), this.pendingSharpDamage);

        serverLevel.playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.PLAYER_ATTACK_CRIT,
                this.getSoundSource(),
                0.85F,
                0.75F
        );

        clearPendingItsSharp();
    }

    private void clearPendingItsSharp() {
        this.pendingSharpTargetId = -1;
        this.pendingSharpDamage = 0.0F;
        this.pendingSharpDamageDelay = 0;
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

        ServerPlayer attackingPlayer = getAttackingPlayer(source);

        if (attackingPlayer != null) {
            recordPartyAttackForJunk(attackingPlayer);
        }

        return true;
    }

    private void recordPartyAttackForJunk(ServerPlayer attackingPlayer) {
        if (this.junkCooldownTicks > 0) {
            return;
        }

        if (this.pendingJunkDamageDelay > 0) {
            return;
        }

        this.junkHitCounter++;

        if (this.junkHitCounter < JUNK_ATTACKS_REQUIRED) {
            return;
        }

        this.junkHitCounter = 0;
        startJunk(attackingPlayer);
    }

    private void startJunk(ServerPlayer attackingPlayer) {
        this.pendingJunkDamageDelay = JUNK_DAMAGE_DELAY_TICKS;
        this.junkCooldownTicks = JUNK_COOLDOWN_TICKS;

        this.triggerGrudgeAnimation();
        announceJunk();

        if (this.level() instanceof ServerLevel serverLevel) {
            spawnJunkWarningParticles(serverLevel);
        }

        this.level().playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SoundEvents.ANVIL_LAND,
                SoundSource.HOSTILE,
                0.7F,
                1.25F
        );
    }

    private void applyPendingJunk() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            this.pendingJunkDamageDelay = 0;
            return;
        }

        List<ServerPlayer> targets = getJunkTargets(serverLevel);

        for (ServerPlayer target : targets) {
            int damage = JUNK_MIN_DAMAGE + this.random.nextInt((JUNK_MAX_DAMAGE - JUNK_MIN_DAMAGE) + 1);

            spawnJunkImpactParticles(serverLevel, target);

            target.invulnerableTime = 0;
            target.hurt(this.damageSources().mobAttack(this), damage);

            serverLevel.playSound(
                    null,
                    target.getX(),
                    target.getY(),
                    target.getZ(),
                    SoundEvents.ANVIL_LAND,
                    SoundSource.HOSTILE,
                    0.55F,
                    1.35F
            );
        }

        this.pendingJunkDamageDelay = 0;
    }

    private List<ServerPlayer> getJunkTargets(ServerLevel serverLevel) {
        List<ServerPlayer> targets = new ArrayList<>();

        for (ServerPlayer player : serverLevel.players()) {
            if (!player.isAlive()) {
                continue;
            }

            if (player.distanceToSqr(this) > JUNK_RANGE * JUNK_RANGE) {
                continue;
            }

            targets.add(player);
        }

        return targets;
    }

    private void spawnJunkWarningParticles(ServerLevel serverLevel) {
        for (ServerPlayer player : getJunkTargets(serverLevel)) {
            for (int i = 0; i < 16; i++) {
                Item item = getRandomJunkItem();

                serverLevel.sendParticles(
                        new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(item)),
                        player.getX() + (this.random.nextDouble() - 0.5D) * 2.0D,
                        player.getY() + 3.0D + this.random.nextDouble(),
                        player.getZ() + (this.random.nextDouble() - 0.5D) * 2.0D,
                        1,
                        0.0D,
                        -0.35D,
                        0.0D,
                        0.12D
                );
            }
        }
    }

    private void spawnJunkImpactParticles(ServerLevel serverLevel, ServerPlayer target) {
        for (int i = 0; i < 28; i++) {
            Item item = getRandomJunkItem();

            serverLevel.sendParticles(
                    new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(item)),
                    target.getX() + (this.random.nextDouble() - 0.5D) * 2.4D,
                    target.getY() + 1.5D + this.random.nextDouble() * 1.5D,
                    target.getZ() + (this.random.nextDouble() - 0.5D) * 2.4D,
                    1,
                    (this.random.nextDouble() - 0.5D) * 0.35D,
                    0.12D,
                    (this.random.nextDouble() - 0.5D) * 0.35D,
                    0.18D
            );
        }
    }

    private Item getRandomJunkItem() {
        int roll = this.random.nextInt(6);

        return switch (roll) {
            case 0 -> Items.FLOWER_POT;      // pot
            case 1 -> Items.CAULDRON;        // kettle stand-in
            case 2 -> Items.GLASS_BOTTLE;    // bottle / wine bottle stand-in
            case 3 -> Items.BOWL;            // cup stand-in
            case 4 -> Items.SNOWBALL;        // soccer ball stand-in
            default -> Items.OBSERVER;       // television stand-in
        };
    }

    private void announceItsSharp(LivingEntity target, int kills, float damage) {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Component message = Component.literal("It's Sharp!")
                .withStyle(ChatFormatting.RED, ChatFormatting.BOLD);

        for (ServerPlayer player : serverLevel.players()) {
            if (player.distanceToSqr(this) <= 40.0D * 40.0D) {
                player.displayClientMessage(message.copy(), true);
            }
        }
    }

    private void announceJunk() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Component message = Component.literal("Junk!")
                .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD);

        Component detail = Component.literal(" Items rain down!")
                .withStyle(ChatFormatting.GRAY);

        for (ServerPlayer player : serverLevel.players()) {
            if (player.distanceToSqr(this) <= 40.0D * 40.0D) {
                player.displayClientMessage(message.copy().append(detail), true);
            }
        }
    }

    private int getEnemyKills(LivingEntity target) {
        if (target instanceof ServerPlayer serverPlayer) {
            return serverPlayer.getStats().getValue(Stats.CUSTOM.get(Stats.MOB_KILLS));
        }

        return 0;
    }

    private ServerPlayer getAttackingPlayer(DamageSource source) {
        if (source == null) {
            return null;
        }

        Entity attacker = source.getEntity();

        if (attacker instanceof ServerPlayer serverPlayer) {
            return serverPlayer;
        }

        Entity directEntity = source.getDirectEntity();

        if (directEntity instanceof Projectile projectile && projectile.getOwner() instanceof ServerPlayer serverPlayer) {
            return serverPlayer;
        }

        if (attacker instanceof Projectile projectile && projectile.getOwner() instanceof ServerPlayer serverPlayer) {
            return serverPlayer;
        }

        return null;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void remove(RemovalReason reason) {
        this.bossEvent.removeAllPlayers();
        super.remove(reason);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);

        ServerPlayer killer = getTonberryKingKiller(damageSource);

        if (killer == null) {
            return;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(killer);

        if (globalData == null) {
            return;
        }

        if (globalData.hasDefeatedTonberryKing()) {
            return;
        }

        ItemStack charm = new ItemStack(ModItemsRM.tonberryCharm.get());

        boolean added = killer.getInventory().add(charm);

        if (!added) {
            killer.displayClientMessage(
                    Component.literal("Your inventory is full! Clear a slot before defeating Tonberry King again.")
                            .withStyle(ChatFormatting.RED),
                    false
            );

            return;
        }

        globalData.setDefeatedTonberryKing(true);

        killer.displayClientMessage(
                Component.literal("You received a Tonberry Charm!")
                        .withStyle(ChatFormatting.DARK_PURPLE),
                false
        );

        PacketHandlerRM.syncGlobalToAllAround(killer, globalData);
    }

    private ServerPlayer getTonberryKingKiller(DamageSource damageSource) {
        if (damageSource != null && damageSource.getEntity() instanceof ServerPlayer serverPlayer) {
            return serverPlayer;
        }

        if (this.getKillCredit() instanceof ServerPlayer serverPlayer) {
            return serverPlayer;
        }

        return null;
    }
}