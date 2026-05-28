package online.remind.remind.entity.attacks;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.entity.ModEntitiesRM;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.registry.entries.EpicFightParticles;
import yesman.epicfight.registry.entries.EpicFightSounds;

public class SlotEdgeCollider extends ThrowableProjectile {

    private LivingEntity caster;
    private float damage;
    private int chainStep;
    private int maxTicks = 8;

    public SlotEdgeCollider(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setBoundingBox(new AABB(-0.5D, 0.0D, -0.5D, 0.5D, 1.5D, 0.5D));
    }

    public SlotEdgeCollider(Level level, LivingEntity caster, float damage, int chainStep) {
        this(ModEntitiesRM.TYPE_SLOT_EDGE.get(), level);
        this.caster = caster;
        this.damage = damage;
        this.chainStep = chainStep;
        this.setOwner(caster);
        this.setPos(caster.getX(), caster.getY() + 0.5D, caster.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void tick() {
        if (caster == null || !caster.isAlive()) {
            remove(RemovalReason.KILLED);
            return;
        }

        if (this.tickCount > maxTicks) {
            remove(RemovalReason.KILLED);
            return;
        }

        this.setOwner(caster);
        this.setPos(caster.getX(), caster.getY() + 0.5D, caster.getZ());

        spawnTrailParticles();

        AABB hitBox = this.getBoundingBox().inflate(2.0D);

        for (Entity entity : level().getEntities(this, hitBox, e -> e instanceof LivingEntity && e != caster)) {
            if (!(entity instanceof LivingEntity target)) {
                continue;
            }

            if (!canHitTarget(target)) {
                continue;
            }

            hitTarget(target);
            return;
        }

        super.tick();
    }

    private boolean canHitTarget(LivingEntity target) {
        Party party = null;

        if (getOwner() != null && getOwner().getServer() != null) {
            party = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
        }

        return party == null || party.getMember(target.getUUID()) == null || party.getFriendlyFire();
    }

    private void hitTarget(LivingEntity target) {
        target.invulnerableTime = 0;
        target.hurt(caster.damageSources().indirectMagic(this, caster), damage);
        target.invulnerableTime = 0;

        applyHitEffects(target);
        openChainWindow();

        caster.setDeltaMovement(0.0D, 0.0D, 0.0D);
        caster.swing(InteractionHand.MAIN_HAND);

        remove(RemovalReason.KILLED);
    }

    private void openChainWindow() {
        if (!(caster instanceof Player player)) {
            return;
        }

        int maxChainStep = 2;

        if (chainStep >= maxChainStep) {
            player.removeEffect(ModMobEffectsRM.SLOT_EDGE_CHAIN);

            giveSlotEdgeReward(player);

            return;
        }

        player.addEffect(new MobEffectInstance(
                ModMobEffectsRM.SLOT_EDGE_CHAIN,
                40, // testing window: 2 seconds
                chainStep + 1,
                false,
                false,
                false
        ));

        player.displayClientMessage(
                Component.literal("Slot Edge!")
                        .withColor(0xFFD700),
                true
        );
    }

    private void giveSlotEdgeReward(Player player) {
        PlayerData playerData = PlayerData.get(player);

        int luckyLuckyCount = 0;

        if (playerData != null) {
            luckyLuckyCount = playerData.getNumberOfAbilitiesEquipped(Strings.luckyLucky);
        }

        // Each Lucky Lucky improves better reward odds.
        // Example: 2 Lucky Lucky = +10 bonus to reward roll.
        int luckyBonus = luckyLuckyCount * 5;

        int roll = player.getRandom().nextInt(100) + luckyBonus;

        if (roll < 45) {
            giveHPPrize(player, 4.0F);
        } else if (roll < 80) {
            giveMunnyPrize(player, playerData, 50);
        } else if (roll < 95) {
            giveFocusPrize(player, playerData, 10);
        } else {
            giveJackpotPrize(player, playerData);
        }
    }

    private void giveHPPrize(Player player, float healAmount) {
        player.heal(healAmount);

        player.displayClientMessage(
                Component.literal("Slot Edge: HP Prize!")
                        .withColor(0x55FF55),
                true
        );

        player.level().playSound(
                null,
                player.blockPosition(),
                SoundEvents.EXPERIENCE_ORB_PICKUP,
                SoundSource.PLAYERS,
                1.0F,
                1.1F
        );

        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.HEART,
                    player.getX(),
                    player.getY() + 1.0D,
                    player.getZ(),
                    8,
                    0.5D,
                    0.5D,
                    0.5D,
                    0.05D
            );
        }
    }

    private void giveMunnyPrize(Player player, PlayerData playerData, int amount) {
        if (playerData != null) {
            playerData.setMunny(playerData.getMunny() + amount);
        }

        player.displayClientMessage(
                Component.literal("Slot Edge: +" + amount + " Munny!")
                        .withColor(0xFFD700),
                true
        );

        player.level().playSound(
                null,
                player.blockPosition(),
                SoundEvents.EXPERIENCE_ORB_PICKUP,
                SoundSource.PLAYERS,
                1.0F,
                1.25F
        );

        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.HAPPY_VILLAGER,
                    player.getX(),
                    player.getY() + 1.0D,
                    player.getZ(),
                    15,
                    0.6D,
                    0.6D,
                    0.6D,
                    0.08D
            );
        }
    }

    private void giveFocusPrize(Player player, PlayerData playerData, int amount) {
        if (playerData != null) {
            playerData.addFocus(amount);
        }

        player.displayClientMessage(
                Component.literal("Slot Edge: +" + amount + " Focus!")
                        .withColor(0x55AAFF),
                true
        );

        player.level().playSound(
                null,
                player.blockPosition(),
                SoundEvents.AMETHYST_BLOCK_CHIME,
                SoundSource.PLAYERS,
                1.0F,
                1.35F
        );

        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.ENCHANT,
                    player.getX(),
                    player.getY() + 1.0D,
                    player.getZ(),
                    25,
                    0.7D,
                    0.7D,
                    0.7D,
                    0.3D
            );
        }
    }

    private void giveJackpotPrize(Player player, PlayerData playerData) {
        player.heal(6.0F);

        if (playerData != null) {
            playerData.setMunny(playerData.getMunny() + 100);
            playerData.addFocus(15);
        }

        player.displayClientMessage(
                Component.literal("Slot Edge: JACKPOT! +100 Munny, +15 Focus!")
                        .withColor(0xFF55FF),
                true
        );

        player.level().playSound(
                null,
                player.blockPosition(),
                SoundEvents.PLAYER_LEVELUP,
                SoundSource.PLAYERS,
                1.0F,
                1.5F
        );

        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.FIREWORK,
                    player.getX(),
                    player.getY() + 1.0D,
                    player.getZ(),
                    60,
                    0.8D,
                    0.8D,
                    0.8D,
                    0.12D
            );

            serverLevel.sendParticles(
                    ParticleTypes.HAPPY_VILLAGER,
                    player.getX(),
                    player.getY() + 1.0D,
                    player.getZ(),
                    30,
                    0.7D,
                    0.7D,
                    0.7D,
                    0.1D
            );

            serverLevel.sendParticles(
                    ParticleTypes.HEART,
                    player.getX(),
                    player.getY() + 1.2D,
                    player.getZ(),
                    10,
                    0.6D,
                    0.6D,
                    0.6D,
                    0.05D
            );
        }
    }

    private void spawnTrailParticles() {
        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        serverLevel.sendParticles(
                ParticleTypes.CRIT,
                caster.getX(),
                caster.getY() + 1.0D,
                caster.getZ(),
                4,
                0.35D,
                0.25D,
                0.35D,
                0.05D
        );

        if (chainStep >= 2) {
            serverLevel.sendParticles(
                    ParticleTypes.SWEEP_ATTACK,
                    caster.getX(),
                    caster.getY() + 1.0D,
                    caster.getZ(),
                    1,
                    0.0D,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }

    private void applyHitEffects(LivingEntity target) {
        Level level = target.level();

        level.playSound(
                null,
                target.blockPosition(),
                chainStep >= 2 ? SoundEvents.PLAYER_ATTACK_CRIT : SoundEvents.PLAYER_ATTACK_STRONG,
                SoundSource.PLAYERS,
                chainStep >= 2 ? 1.2F : 1.0F,
                chainStep >= 2 ? 1.25F : 1.0F
        );

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.SWEEP_ATTACK,
                    target.getX(),
                    target.getY() + target.getBbHeight() * 0.5D,
                    target.getZ(),
                    chainStep >= 2 ? 3 : 1,
                    0.25D,
                    0.25D,
                    0.25D,
                    0.0D
            );

            serverLevel.sendParticles(
                    ParticleTypes.CRIT,
                    target.getX(),
                    target.getY() + target.getBbHeight() * 0.5D,
                    target.getZ(),
                    20 + chainStep * 10,
                    0.45D,
                    0.45D,
                    0.45D,
                    0.15D
            );

            if (KingdomKeysReMind.efmLoaded) {
                EpicFightParticles.HIT_BLADE.get().spawnParticleWithArgument(
                        serverLevel,
                        HitParticleType.RANDOM_WITHIN_BOUNDING_BOX,
                        HitParticleType.ZERO,
                        target,
                        target
                );

                target.level().playSound(
                        null,
                        target.blockPosition(),
                        EpicFightSounds.BLADE_HIT.get(),
                        SoundSource.PLAYERS,
                        1.0F,
                        1.0F
                );
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.damage = tag.getFloat("Damage");
        this.chainStep = tag.getInt("ChainStep");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Damage", this.damage);
        tag.putInt("ChainStep", this.chainStep);
    }
}