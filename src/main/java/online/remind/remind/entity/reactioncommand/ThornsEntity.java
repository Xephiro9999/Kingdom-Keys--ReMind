package online.remind.remind.entity.reactioncommand;

import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.remind.remind.entity.ModEntitiesRM;
import org.joml.Vector3f;

import java.util.List;

public class ThornsEntity extends ThrowableProjectile {

    int maxTicks = 40;
    float dmgMult = 1;

    private Player caster;
    static int ticks = 0;
    static double a = 3600;
    LivingEntity lockOnEntity;
    public ThornsEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
    }

    public ThornsEntity(Level world, Player player, LivingEntity lockOnEntity) {
        super(ModEntitiesRM.TYPE_THORNS.get(), world);
        this.blocksBuilding = true;
    }

    public ThornsEntity(Level world, LivingEntity player, Player caster, LivingEntity lockOnEntity) {
        super(ModEntitiesRM.TYPE_THORNS.get(), player, world);
        this.caster = caster;
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
        super.tick();

        Vec3 dir = this.getDeltaMovement().normalize();
        double length = 6.0; // how long the thorn is
        Vec3 start = this.position();

        // --- Phase 1: Launch (first 5 ticks) ---
        if (this.tickCount < 5) {
            this.move(MoverType.SELF, this.getDeltaMovement());
        } else {
            // stop moving after launch
            this.setDeltaMovement(Vec3.ZERO);
        }

        // --- Compute dynamic end with random wiggle ---
        Vec3 randomOffset = Vec3.ZERO;
        if (this.tickCount >= 5) {
            double wiggleStrength = 1; // tweak how crazy it moves
            randomOffset = new Vec3(
                    (this.random.nextDouble() - 0.5) * wiggleStrength,
                    (this.random.nextDouble() - 0.5) * wiggleStrength,
                    (this.random.nextDouble() - 0.5) * wiggleStrength
            );
        }
        Vec3 end = start.add(dir.scale(length)).add(randomOffset);

        // --- Draw full thorn body every tick ---
        int steps = 60; // number of segments along the lance
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            double px = start.x + (end.x - start.x) * t;
            double py = start.y + (end.y - start.y) * t;
            double pz = start.z + (end.z - start.z) * t;

            // subtle black smoke
            this.level().addParticle(new DustParticleOptions(new Vector3f(0F,0F,0F), 2F),
                    px, py, pz,
                    (this.random.nextDouble() - 0.5) * 0.1, 0.02, (this.random.nextDouble() - 0.5) * 0.1);

            // black -> white shifting dust
            this.level().addParticle(
                    new DustColorTransitionOptions(
                            new Vector3f(0f, 0f, 0f),     // black
                            new Vector3f(1f, 1f, 1f),     // white
                            1.0f                          // size
                    ),
                    px, py, pz,
                    0, 0, 0
            );
        }

        // --- Damage hitbox (while active) ---
        if (this.tickCount < 30) {
            AABB hitbox = new AABB(start, end).inflate(0.5);
            List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, hitbox);
            for (LivingEntity target : targets) {
                if (target != this.getOwner()) {
                    float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) / 1.25F : 2;
                    target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS,this, this.getOwner()), (dmg * dmgMult));
                }
            }
        }

        // --- Despawn after linger ---
        if (this.tickCount > 35) {
            this.discard();
        }
    }

}
