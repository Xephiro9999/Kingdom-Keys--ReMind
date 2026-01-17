package online.remind.remind.entity.attacks;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
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

import java.util.List;

public class zantesukenCollider extends ThrowableProjectile {

    private LivingEntity caster;
    private float damage;
    private int maxTicks = 50;
    private int spellLevel = 0;

    public zantesukenCollider(EntityType<? extends ThrowableProjectile> type, Level level){
        super(type, level);
        this.noPhysics = true;
        //this.setInvisible(true);
        this.setBoundingBox(new AABB(-0.5, 0, -0.5, 0.5, 1.5, 0.5));

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    public zantesukenCollider(Level level, LivingEntity caster, float damage, int spellLevel){
        this(ModEntitiesRM.TYPE_QUICK_BLITZ.get(),level);
        this.caster = caster;
        this.damage = damage;
        this.spellLevel = spellLevel;
        this.setPos(caster.getX(), caster.getY(), caster.getZ());
    }

    public void tick() {

        float radius = 4.5F;

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

        if (tickCount < 20) {
            if (caster.level() instanceof ServerLevel serverLevel) {

                serverLevel.sendParticles(ParticleTypes.CHERRY_LEAVES,
                        caster.getX() * random.nextInt(),
                        caster.getY()+1,
                        caster.getZ()* random.nextInt(),
                        1, 0, 0, 0, 0);
                caster.setDeltaMovement(Vec3.ZERO);
            }
        }

        if (tickCount > 30 && tickCount < 40){
            caster.setDeltaMovement(Vec3.ZERO);
        }

        // Check Collision

        double chance = 0;
        double power = 1;

        switch(spellLevel){
            case 0:
                chance = 50;
                power = 4.2;
                break;
            case 1:
                chance = 60;
                power = 4.4;
                break;
            case 2:
                chance = 70;
                power = 4.6;
                break;
        }

        System.out.println("Chance: " + chance + ", Power: " + power); //Debugging Line

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
                            if (e instanceof ChirithyEntity){
                                list.remove(e);
                            }
                            if (rand == chance){
                                e.kill();
                            } else {
                                float dmg = (float) (casterData.getStrength(true) * power);
                                e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS,this, this.getOwner()), dmg);
                            }
                        }
                    }
                }
            }
        }


        for (Entity entity : level().getEntities(this, this.getBoundingBox(), e -> e instanceof LivingEntity && e != caster)) {
            if (entity != getOwner()) {
                Party p = null;
                if (getOwner() != null) {
                    p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
                }
                LivingEntity target = (LivingEntity) entity;
                if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())){

                    //TODO: Math and such for an instant kill

                    target.hurt(caster.damageSources().mobAttack(caster), damage);
                    caster.setDeltaMovement(0, 0, 0);
                    caster.swing(InteractionHand.MAIN_HAND);
                    target.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                            SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (KingdomKeysReMind.efmLoaded) {
                        EpicFightParticles.HIT_BLADE.get().spawnParticleWithArgument(((ServerLevel) target.level()), HitParticleType.RANDOM_WITHIN_BOUNDING_BOX, HitParticleType.ZERO, target, target);
                        target.level().playSound(null, target.blockPosition(), EpicFightSounds.BLADE_HIT.get(), SoundSource.PLAYERS, 1F, 1F);
                    } else {
                        level().addParticle(ParticleTypes.CRIT,
                                target.getX(), target.getY() + target.getBbHeight(), target.getZ(),
                                0, 0.1, 0);
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
