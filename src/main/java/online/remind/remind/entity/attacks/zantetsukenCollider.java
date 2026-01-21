package online.remind.remind.entity.attacks;

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
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.registry.entries.EpicFightParticles;
import yesman.epicfight.registry.entries.EpicFightSounds;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class zantetsukenCollider extends ThrowableProjectile {

    private LivingEntity caster;
    private float damage;
    private int maxTicks = 50;
    private int spellLevel = 0;
    private final Set<UUID> hitEntities = new HashSet<>();


    public zantetsukenCollider(EntityType<? extends ThrowableProjectile> type, Level level){
        super(type, level);
        this.noPhysics = true;
        //this.setInvisible(true);
        this.setBoundingBox(new AABB(-0.5, 0, -0.5, 0.5, 1.5, 0.5));

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    public zantetsukenCollider(Level level, LivingEntity caster, float damage, int spellLevel){
        this(ModEntitiesRM.TYPE_QUICK_BLITZ.get(),level);
        this.caster = caster;
        this.damage = damage;
        this.spellLevel = spellLevel;
        this.setPos(caster.getX(), caster.getY(), caster.getZ());
    }

    public void tick() {

        float radius = 5F;

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

        if (tickCount < 30) {
            //player.getX() + player.level().random.nextDouble()



            if (caster.level() instanceof ServerLevel serverLevel) {

                serverLevel.sendParticles(ParticleTypes.CHERRY_LEAVES,
                        caster.getX() + caster.level().random.nextDouble() - 0.5D,
                        caster.getY()+ caster.level().random.nextDouble() * 2D,
                        caster.getZ() + caster.level().random.nextDouble() - 0.5D,
                        1, 0, 0, 0, 0);
                serverLevel.sendParticles(ParticleTypes.CHERRY_LEAVES,
                        caster.getX() + caster.level().random.nextDouble() - 1.5D,
                        caster.getY()+ caster.level().random.nextDouble() * 2D,
                        caster.getZ() + caster.level().random.nextDouble() - 1.5D,
                        1, 0, 0, 0, 0);
                caster.setDeltaMovement(Vec3.ZERO);
            }
        }

        if (tickCount > 20 && tickCount < 30){
            caster.setDeltaMovement(Vec3.ZERO);
        }

        // Check Collision

        double chance = 0;
        double power = 1;

        switch(spellLevel){
            case 0:
                chance = 40;
                power = 4.2;
                break;
            case 1:
                chance = 50;
                power = 4.4;
                break;
            case 2:
                chance = 60;
                power = 4.6;
                break;
        }

        //System.out.println("Chance: " + chance + ", Power: " + power); //Debugging Line
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
                                if (!hitEntities.contains(e.getUUID())){
                                if (rand <= chance) {

                                    //System.out.println("Spell Level: " + spellLevel);
                                    e.hurt(caster.damageSources().mobAttack(caster), 999999);
                                    //System.out.println(rand);
                                    //System.out.println("Death");
                                    if (KingdomKeysReMind.efmLoaded) {
                                        EpicFightParticles.HIT_BLADE.get().spawnParticleWithArgument(((ServerLevel) e.level()), HitParticleType.RANDOM_WITHIN_BOUNDING_BOX, HitParticleType.ZERO, e, e);
                                        e.level().playSound(null, e.blockPosition(), EpicFightSounds.BLADE_HIT.get(), SoundSource.PLAYERS, 1F, 0.5F);
                                        e.level().playSound(null, e.blockPosition(), SoundEvents.TRIDENT_RETURN, SoundSource.PLAYERS, 1f, 1f);

                                    } else {
                                        level().addParticle(ParticleTypes.CRIT,
                                                e.getX(), e.getY() + e.getBbHeight(), e.getZ(),
                                                0, 0.1, 0);
                                    }
                                    hitEntities.add(e.getUUID());

                                } else {

                                    //System.out.println("Spell Level: " + spellLevel);
                                    //System.out.println(rand);
                                    float dmg = (float) ((casterData.getStrength(true) * 0.25f) * power);
                                    //System.out.println("Damage:" + dmg);
                                    //e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS, this, this.getOwner()), dmg);
                                    e.hurt(caster.damageSources().mobAttack(caster), dmg);
                                    if (KingdomKeysReMind.efmLoaded) {
                                        EpicFightParticles.HIT_BLADE.get().spawnParticleWithArgument(((ServerLevel) e.level()), HitParticleType.RANDOM_WITHIN_BOUNDING_BOX, HitParticleType.ZERO, e, e);
                                        e.level().playSound(null, e.blockPosition(), EpicFightSounds.BLADE_HIT.get(), SoundSource.PLAYERS, 1F, 1F);
                                    } else {
                                        level().addParticle(ParticleTypes.CRIT,
                                                e.getX(), e.getY() + e.getBbHeight(), e.getZ(),
                                                0, 0.1, 0);
                                    }
                                    hitEntities.add(e.getUUID());

                                }
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
