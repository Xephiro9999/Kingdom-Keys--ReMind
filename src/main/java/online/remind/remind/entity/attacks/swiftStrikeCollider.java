package online.remind.remind.entity.attacks;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.ModEntitiesRM;
import online.remind.remind.entity.spirits.ChirithyEntity;
import org.joml.Vector3f;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.registry.entries.EpicFightParticles;
import yesman.epicfight.registry.entries.EpicFightSounds;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class swiftStrikeCollider extends ThrowableProjectile {

    private LivingEntity caster;
    private float damage;
    private int maxTicks = 50;
    private int spellLevel = 0;
    private int hits = 0;
    private int maxHits = 13;
    private final Set<UUID> hitEntities = new HashSet<>();


    public swiftStrikeCollider(EntityType<? extends ThrowableProjectile> type, Level level){
        super(type, level);
        this.noPhysics = true;
        //this.setInvisible(true);
        this.setBoundingBox(new AABB(-0.5, 0, -0.5, 0.5, 1.5, 0.5));

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    public swiftStrikeCollider(Level level, LivingEntity caster, float damage, int spellLevel){
        this(ModEntitiesRM.TYPE_QUICK_BLITZ.get(),level);
        this.caster = caster;
        this.damage = damage;
        this.spellLevel = spellLevel;
        this.setPos(caster.getX(), caster.getY(), caster.getZ());
    }

    public void tick() {

        float radius = 6.5F;

        if (caster == null || !caster.isAlive()) {
            remove(RemovalReason.KILLED);
            return;
        }

        if (this.tickCount > maxTicks) {
            this.remove(RemovalReason.KILLED);
        }

        this.setPos(caster.getX(), caster.getY() + 0.5, caster.getZ());

        if (tickCount > 2){
            AABB hitBox = this.getBoundingBox().inflate(2.0); // easier to land
        }

        if (tickCount <= 30) {
            //player.getX() + player.level().random.nextDouble()



            if (caster.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SQUID_INK,
                        caster.getX() + caster.level().random.nextDouble() - 0.5D,
                        caster.getY()+ caster.level().random.nextDouble() * 2D,
                        caster.getZ() + caster.level().random.nextDouble() - 0.5D,
                        10, 0, 0, 0, 0);
                serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH,
                        caster.getX() + caster.level().random.nextDouble() - 0.5D,
                        caster.getY()+ caster.level().random.nextDouble() * 2D,
                        caster.getZ() + caster.level().random.nextDouble() - 0.5D,
                        10, 0, 0, 0, 0);
            }
        }

        if (tickCount > 20 && tickCount < 30){
            caster.setDeltaMovement(Vec3.ZERO);
        }

        // Check Collision

        double power = 1;

        switch(spellLevel){
            case 0:
                power = 6;
                break;
            case 1:
                power = 6;
                break;
            case 2:
                power = 6;
                break;
        }

        if (tickCount >= 30){

            this.setOwner(caster);
            WorldData worldData = WorldData.get(level().getServer());
            PlayerData casterData = PlayerData.get((Player) getOwner());
            if (getOwner() != null && worldData != null) {
                List<Entity> list = level().getEntities(getOwner(), getBoundingBox().inflate(radius));
                Party casterParty = worldData.getPartyFromMember(getOwner().getUUID());
                if (casterParty != null && !casterParty.getFriendlyFire()){
                    for(Party.Member m : casterParty.getMembers()) {
                        list.remove(level().getPlayerByUUID(m.getUUID()));
                    }
                } else {
                    list.remove(getOwner());
                }

                if (!list.isEmpty()){

                    for (int i = 0; i < list.size(); i++){

                        double rand = Math.floor(Math.random() * 100);
                        Entity e = list.get(i);
                        if (e instanceof LivingEntity){
                            if(Utils.isHostile(e) || e instanceof ServerPlayer) {
                                if (e instanceof ChirithyEntity) {
                                    list.remove(e);
                                }
                                if (hits <= maxHits){

                                    float dmg = (float) ((casterData.getStrength(true) * 0.3f) * power);
                                    //System.out.println("Damage:" + dmg);
                                    e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS, this, this.getOwner()), dmg);
                                    e.invulnerableTime = 0;
                                    if (KingdomKeysReMind.efmLoaded) {
                                        EpicFightParticles.HIT_BLADE.get().spawnParticleWithArgument(((ServerLevel) e.level()), HitParticleType.RANDOM_WITHIN_BOUNDING_BOX, HitParticleType.ZERO, e, e);
                                        e.level().playSound(null, e.blockPosition(), EpicFightSounds.BLADE_HIT.get(), SoundSource.PLAYERS, 1F, 1F);
                                    } else {
                                        level().addParticle(ParticleTypes.CRIT,
                                                e.getX(), e.getY() + e.getBbHeight(), e.getZ(),
                                                0, 0.1, 0);
                                    }
                                    hits++;
                                }
                            }
                        }
                    }
                }
            }
        }
        super.tick();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}
    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}
}
