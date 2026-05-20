package online.remind.remind.entity.attacks;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.remind.remind.entity.ModEntitiesRM;

import java.util.Map;

public class ravenousSaberCollider extends ThrowableProjectile {

    private Player caster;
    private float damage;
    private int maxTicks = 40;
    private int ticks = 0;

    private int currentHitIndex = 0;
    private int hitDelay = 8;
    private int hitTimer = 0;

    public ravenousSaberCollider(EntityType<? extends ThrowableProjectile> type, Level level){
        super(type, level);
        this.noPhysics = true;
        //this.setInvisible(true);
        this.setBoundingBox(new AABB(40, 0, 40, -10, 1, -10));

    }

    private static final String[] ELEMENT_ORDER = {
            "Fire", "Ice", "Lightning", "Water", "Air"
            // 0,1,2,3,4
    };

    private static final Map<String, ResourceKey<DamageType>> ELEMENT_DAMAGE_MAP = Map.of(
            "Fire", KKDamageTypes.FIRE,
            "Ice", KKDamageTypes.ICE,
            "Lightning", KKDamageTypes.LIGHTNING,
            "Water", KKDamageTypes.WATER,
            "Air", KKDamageTypes.AIR
    );


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    public ravenousSaberCollider(Level level, Player caster, float damage){
        this(ModEntitiesRM.TYPE_RAVE_SABER.get(),level);
        this.caster = caster;
        this.damage = damage;
        this.setPos(caster.getX(), caster.getY(), caster.getZ());
    }

    public void tick() {

        if (caster == null || !caster.isAlive()) {
            remove(RemovalReason.KILLED);
            return;
        }

        if (this.tickCount > maxTicks) {
            this.remove(RemovalReason.KILLED);
        }


        this.setPos(caster.getX(), caster.getY() + 0.5, caster.getZ());

        if (!level().isClientSide()) {
            hitTimer++;

            if (hitTimer >= hitDelay) {

                hitTimer = 0; // Resets Timer

                AABB hitBox = this.getBoundingBox().inflate(1.75); // easier to land

                for (Entity entity : level().getEntities(this, hitBox, e -> e instanceof LivingEntity && e != caster)) {
                    LivingEntity target = (LivingEntity) entity;
                    String element = ELEMENT_ORDER[currentHitIndex];


                    ResourceKey<DamageType> dmgType = ELEMENT_DAMAGE_MAP.get(ELEMENT_ORDER[currentHitIndex]);
                    //System.out.println("[RavenousSaber] Element: " + element + " -> Type: " + dmgType);

                    if (dmgType == null) {
                        continue; // Hopefully doesn't crash
                    }

                    double radius = 90;
                    if (caster.level() instanceof ServerLevel serverLevel) {
                        switch (element) {


                            case "Fire":

                                level().playSound(null,
                                        target.getX(), target.getY(), target.getZ(),
                                        SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);


                                break;
                            case "Ice":

                                level().playSound(null,
                                        target.getX(), target.getY(), target.getZ(),
                                        SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                                break;
                            case "Lightning":

                                level().playSound(null,
                                        target.getX(), target.getY(), target.getZ(),
                                        SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.0F, 1.0F);
                                break;
                            case "Water":

                                level().playSound(null,
                                        target.getX(), target.getY(), target.getZ(),
                                        SoundEvents.WATER_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                                break;
                            case "Air":

                                level().playSound(null,
                                        target.getX(), target.getY(), target.getZ(),
                                        SoundEvents.BREEZE_WIND_CHARGE_BURST, SoundSource.PLAYERS, 1.0F, 1.0F);
                                break;
                            default:
                                break;
                        }

                        spawnElementFX(level(), element, target.getX(), target.getY() + target.getBbHeight()/2, target.getZ());
                        var src = KKDamageTypes.getElementalDamage(dmgType, this, caster);
                        caster.swing(InteractionHand.MAIN_HAND);
                        target.hurt(src, damage);
                        target.invulnerableTime = 2;
                        // Keep Target from moving
                        target.setDeltaMovement(0, 0, 0);


                        currentHitIndex++;
                        if (currentHitIndex >= ELEMENT_ORDER.length) {
                            currentHitIndex = 0;
                        }
                    }
                }
            }
            super.tick();
        }
    }

    public static void spawnElementFX(Level level, String element, double x, double y, double z) {
        if (level instanceof ServerLevel server) {
            // Use sendParticles for nearby clients
            switch (element) {
                case "Fire":
                    server.sendParticles(ParticleTypes.FLAME, x, y, z, 8, 0.5, 0.5, 0.5, 0.05);
                    server.sendParticles(ParticleTypes.FLAME, x, y+1, z, 8, 0.5, 0.5, 0.5, 0.05);
                    server.sendParticles(ParticleTypes.FLAME, x, y-1, z, 8, 0.5, 0.5, 0.5, 0.05);
                    break;
                case "Ice":
                    server.sendParticles(ParticleTypes.SNOWFLAKE, x, y, z, 8, 0.5, 0.5, 0.5, 0.05);
                    server.sendParticles(ParticleTypes.SNOWFLAKE, x, y+1, z, 8, 0.5, 0.5, 0.5, 0.05);
                    server.sendParticles(ParticleTypes.SNOWFLAKE, x, y-1, z, 8, 0.5, 0.5, 0.5, 0.05);
                    break;
                case "Lightning":
                    server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 8, 0.5, 0.5, 0.5, 0.05);
                    server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y+1, z, 8, 0.5, 0.5, 0.5, 0.05);
                    server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y-1, z, 8, 0.5, 0.5, 0.5, 0.05);
                    break;
                case "Water":
                    server.sendParticles(ParticleTypes.SPLASH, x, y, z, 8, 0.5, 0.5, 0.5, 0.05);
                    server.sendParticles(ParticleTypes.SPLASH, x, y+1, z, 8, 0.5, 0.5, 0.5, 0.05);
                    server.sendParticles(ParticleTypes.SPLASH, x, y-1, z, 8, 0.5, 0.5, 0.5, 0.05);
                    break;
                case "Air":
                    server.sendParticles(ParticleTypes.CLOUD, x, y, z, 8, 0.5, 0.5, 0.5, 0.05);
                    server.sendParticles(ParticleTypes.CLOUD, x, y+1, z, 8, 0.5, 0.5, 0.5, 0.05);
                    server.sendParticles(ParticleTypes.CLOUD, x, y-1, z, 8, 0.5, 0.5, 0.5, 0.05);
                    break;
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}
    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}
}
