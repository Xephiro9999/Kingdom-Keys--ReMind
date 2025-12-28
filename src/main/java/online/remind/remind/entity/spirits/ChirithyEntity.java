package online.remind.remind.entity.spirits;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.entity.ModEntitiesRM;
import online.remind.remind.entity.spirits.goal.ChirithyGoal;
import online.remind.remind.network.PacketHandlerRM;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ChirithyEntity extends TamableAnimal{
    
    Player owner;

    private UUID ownerUUID;

    private double chirithyHP;
    private double chirithyStrength;
    private double chirithyMagic;
    private double chirithyDefense;
    private int magicCooldown;


    public ChirithyEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super((EntityType<? extends TamableAnimal>) type, worldIn);

    }

    public ChirithyEntity(Level worldIn, Player owner){
        this(ModEntitiesRM.TYPE_CHIRITHY.get(),worldIn);
        if (owner != null) {
            this.owner = owner;
            PlayerData ownerData = PlayerData.get(owner);

            // Attribute Scaling
            this.chirithyHP = 20 + (ownerData.getMaxHP() / 2D);
            this.chirithyStrength = 2 + (ownerData.getStrengthStat().getStat() / 5D);
            this.chirithyMagic = 5 + (ownerData.getMagicStat().getStat() / 0.8D);
            this.chirithyDefense = 2 + (ownerData.getDefenseStat().getStat() / 2D);

            this.setHealth((float) chirithyHP);
        }
    }

    public void updateStatsFromOwner() {
        if (owner != null) {
            PlayerData ownerData = PlayerData.get(owner);
            chirithyHP = 20 + (ownerData.getMaxHP() / 2D);
            chirithyStrength = 2 + (ownerData.getStrengthStat().getStat() / 5D);
            chirithyMagic = 5 + (ownerData.getMagicStat().getStat() / 0.8D);
            chirithyDefense = 2 + (ownerData.getDefenseStat().getStat() / 2D);

            this.setHealth((float) chirithyHP);
        }
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public void tick(){
        super.tick();

        if (this.level().isClientSide) return;

        if (this.getOwner() == null){
            this.discard();
            return;
        }


        Player owner = this.level().getPlayerByUUID(this.getOwnerUUID());

        if (owner == null || owner.isDeadOrDying()){
            this.discard();
            return;
        }
        IGlobalDataRM data = ModDataRM.getGlobal(owner);
        if (data != null){
            data.setHasDreamEaterSummoned(false);
            data.setDreamEaterUUID(null);
            data.setDreamEaterSummonedID(-1);
            PacketHandlerRM.syncGlobalToAllAround(owner, data);
        }



        if (data == null || !data.hasDreamEaterSummoned()) {
            this.discard();
        }

        //this.setNoGravity(true);
        castSupportMagic();




        if(this.level().isClientSide()){
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if(this.idleAnimationTimeout <= 0){
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    private void castSupportMagic(){
        if (magicCooldown > 0){
            magicCooldown--;
            return;
        }

        if (owner != null && owner.isAlive()){
            if (magicCooldown == 0) {
                //owner.sendSystemMessage(Component.literal(owner.getHealth() + ""));
            // Cure Logic

                if (owner.getHealth() <= (owner.getMaxHealth() * 0.25f)) {

                    PlayerData ownerData = PlayerData.get(owner);
                    if (ownerData == null) return;

                    int cureLevel = ownerData.getMagicLevel(ResourceLocation.parse(Strings.Magic_Cure));
                    switch (cureLevel){
                        case 0:
                            ((ServerLevel) owner.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), owner.getX(), owner.getY() + 2.3D, owner.getZ(), 5, 0D, 0D, 0D, 0D);
                            float healAmount = (float) (chirithyMagic * 0.5);
                            owner.heal(healAmount);
                            owner.level().playSound(null, owner.position().x(), owner.position().y(), owner.position().z(), ModSounds.cure.get(), SoundSource.NEUTRAL, 1f, 1f);
                            owner.sendSystemMessage(Component.literal("<Chirithy> Cure!"));
                            break;
                        case 1:
                            ((ServerLevel) owner.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), owner.getX(), owner.getY() + 2.3D, owner.getZ(), 5, 0D, 0D, 0D, 0D);
                            healAmount = (float) (chirithyMagic);
                            owner.heal(healAmount);
                            owner.level().playSound(null, owner.position().x(), owner.position().y(), owner.position().z(), ModSounds.cura.get(), SoundSource.NEUTRAL, 1f, 1f);
                            owner.sendSystemMessage(Component.literal("<Chirithy> Cura!"));
                            break;
                        case 2:
                            ((ServerLevel) owner.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), owner.getX(), owner.getY() + 2.3D, owner.getZ(), 5, 0D, 0D, 0D, 0D);
                            healAmount = (float) (chirithyMagic * 1.5);
                            owner.heal(healAmount);
                            owner.level().playSound(null, owner.position().x(), owner.position().y(), owner.position().z(), ModSounds.curaga.get(), SoundSource.NEUTRAL, 1f, 1f);
                            owner.sendSystemMessage(Component.literal("<Chirithy> Curaga!"));
                    }
                    magicCooldown = 300;
                }

            // Aero Logic

            }
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick){
        float f;
        if (this.getPose() == Pose.STANDING){
            f = Math.min(pPartialTick * 6F, 1f);
        } else {
            f = 0f;
        }
        this.walkAnimation.update(f, 0.2f);
    }


    @Override
    protected void registerGoals(){

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 5F));
        this.goalSelector.addGoal(3, new ChirithyGoal(this, 0.85d,2.0f,10.0f,false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this,0.25D));
        // Heal Owner
        // Buff Owner
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        // Targeting
        //this.targetSelector.addGoal(1);

    }


    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.MAX_HEALTH, 35.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1000.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.5D);
    }

    public int getMagic(){
        return 10;
    }

    public int getDefence(){
        return 10;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    @Override
    public boolean hurt(DamageSource source, float amount){
        Entity attacker = source.getEntity();
        if (attacker != null && this.getOwner() != null && attacker.getUUID().equals(this.getOwner().getUUID())){
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {}

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    public void setOwnerUUID(UUID uuid) {
        this.ownerUUID = uuid;
    }

    @Nullable
    public UUID getOwnerUUID(){
        return ownerUUID;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }
}
