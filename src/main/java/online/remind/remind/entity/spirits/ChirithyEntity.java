package online.remind.remind.entity.spirits;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.entity.ModEntitiesRM;

import java.util.UUID;

public class ChirithyEntity extends PathfinderMob{
    
    Player owner;


    public ChirithyEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);

    }

    public ChirithyEntity(Level worldIn, Player owner){
        this(ModEntitiesRM.TYPE_CHIRITHY.get(),worldIn);
        if (owner != null) {
            this.owner = owner;
            PlayerData ownerData = PlayerData.get(owner);

            // Attribute Scaling
            double hp = 20 + (ownerData.getMaxHP() / 2D);
            double str = 2 + (ownerData.getStrengthStat().getStat() / 5D);
            double mag = 5 + (ownerData.getMagicStat().getStat() / 0.8D);
            double def = 2 + (ownerData.getDefenseStat().getStat() / 2D);
        }
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public void tick(){
        super.tick();

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

    protected void update(int i){
        IGlobalDataRM playerData = ModDataRM.getGlobal(owner);
        if (playerData.getDreamEaterSummonedID() == -1){
            this.remove(RemovalReason.KILLED);
        }
    }





    @Override
    protected void registerGoals(){

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 5F));
        //this.goalSelector.addGoal(3, new FollowOwnerGoal(this,1.0D,2F));
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
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    public void setOwnerUUID(UUID uuid) {
    }
}
