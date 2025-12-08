package online.remind.remind.entity.attacks;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
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
        this.setBoundingBox(new AABB(-0.5, 0, -0.5, 0.5, 1.5, 0.5));

    }

    private static final String[] ELEMENT_ORDER = {
            "KKDamageTypes.FIRE", "KKDamageTypes.ICE", "KKDamageTypes.LIGHTNING", "KKDamageTypes.WATER", "KKDamageTypes.AIR"
            // 0,1,2,3,4
    };

    private static final Map<String, ResourceKey<DamageType>> ELEMENT_DAMAGE_MAP = Map.of(
            "Fire", KKDamageTypes.FIRE,
            "Blizzard", KKDamageTypes.ICE,
            "Lightning", KKDamageTypes.LIGHTNING,
            "Water", KKDamageTypes.WATER,
            "Air", KKDamageTypes.AIR
    );


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    public ravenousSaberCollider(Level level, Player caster, float damage){
        this(ModEntitiesRM.TYPE_QUICK_BLITZ.get(),level);
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

        hitTimer++;
        System.out.println(hitTimer);
        if (hitTimer >= hitDelay){

            hitTimer = 0; // Resets Timer

            AABB hitBox = this.getBoundingBox().inflate(1.2); // easier to land

            for (Entity entity : level().getEntities(this, this.getBoundingBox(), e -> e instanceof LivingEntity && e != caster)){
                LivingEntity target = (LivingEntity) entity;

                ResourceKey<DamageType> type = ELEMENT_DAMAGE_MAP.get(ELEMENT_ORDER[currentHitIndex]);
                System.out.println(type);
                target.hurt(KKDamageTypes.getElementalDamage(type,this, this.getOwner()), damage);
                target.invulnerableTime = 0;

            }

        }
        super.tick();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}
    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}
}
