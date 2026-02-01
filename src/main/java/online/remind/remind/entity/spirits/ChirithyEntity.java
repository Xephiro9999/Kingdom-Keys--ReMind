package online.remind.remind.entity.spirits;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCAeroSoundPacket;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.dreameater.ModDreamEaters;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.entity.ModEntitiesRM;
import online.remind.remind.entity.spirits.goal.ChirithyGoal;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.UUID;

public class ChirithyEntity extends BaseDreamEaterEntity implements GeoEntity {
    
    Player owner;

    private UUID ownerUUID;

    private double chirithyHP;
    private double chirithyStrength;
    private double chirithyMagic;
    private double chirithyDefense;
    private int cureCooldown;
    private int aeroCooldown;
    private int esunaCooldown;
    private double autoLifeCooldown;
    private int castCooldown;

    public static final int
            IDLE = 0,
            WALK = 1,
            CAST = 2;

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenPlay("idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    protected static final RawAnimation CAST_ANIM = RawAnimation.begin().thenPlay("cast");

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(ChirithyEntity.class, EntityDataSerializers.INT);


    public final AnimationState castAnimationState = new AnimationState();

    private void startCasting() {
        if (!castAnimationState.isStarted()) {
            castAnimationState.start(this.tickCount);
        }
    }

    private void stopCasting() {
        castAnimationState.stop();
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }


    public ChirithyEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super((EntityType<? extends TamableAnimal>) type, worldIn);

    }

    public ChirithyEntity(Level worldIn, Player owner){
        this(ModEntitiesRM.TYPE_CHIRITHY.get(),worldIn);
        if (owner != null) {
            this.owner = owner;
            PlayerData ownerData = PlayerData.get(owner);

            // Attribute Scaling
            this.hp = (int) (20 + (ownerData.getMaxHP() / 2D));
            this.str = (int) (2 + (ownerData.getStrengthStat().getStat() / 5D));
            this.mag = (int) ( 5 + (ownerData.getMagicStat().getStat() * 0.75D));
            this.def = (int) (2 + (ownerData.getDefenseStat().getStat() / 2D));

            this.setHealth((float) hp);
        }
    }

    public void updateStatsFromOwner() {
        if (owner != null) {
            PlayerData ownerData = PlayerData.get(owner);
            hp = (int) (20 + (ownerData.getMaxHP() / 2D));
            str = (int) (2 + (ownerData.getStrengthStat().getStat() / 5D));
            mag = (int) (5 + (ownerData.getMagicStat().getStat() / 0.75D));
            def = (int) (2 + (ownerData.getDefenseStat().getStat() / 2D));

            //System.out.println(hp+ ", " + str + ", " + mag + ", " + def);

            this.setHealth((float) hp);
            this.setStr(str);
            this.setMag(mag);
            this.setDef(def);
        }
    }

    public void tick() {
        super.tick();

        if (this.level().isClientSide) return;

        if (this.getOwner() == null) {
            this.discard();
            return;
        }

        updateStatsFromOwner();




        Player owner = this.level().getPlayerByUUID(this.getOwnerUUID());
        IGlobalDataRM data = ModDataRM.getGlobal(owner);

        // Sorry gamer, but if I die, you die.
        if (owner == null || owner.isDeadOrDying()) {

            if (data != null) {
                data.setHasDreamEaterSummoned(false);
                data.setDreamEaterUUID(null);
                PacketHandlerRM.syncGlobalToAllAround(owner, data);
            }
            this.discard();
            return;
        }




        // If you die, I no longer am bound to you.
        if (!this.isAlive()){
            data.setHasDreamEaterSummoned(false);
            data.setDreamEaterUUID(null);
            PacketHandlerRM.syncGlobalToAllAround(owner, data);
        }


        // Desummon if player's data doesn't match the current ID
        if (data != null) {
            if (!data.getDreamEaterRL().equals(ModDreamEaters.CHIRITHY.get().getRegistryName().toString())) {
                data.setHasDreamEaterSummoned(false);
                data.setDreamEaterUUID(null);
                PacketHandlerRM.syncGlobalToAllAround(owner, data);
                this.discard();
            }
        }

        //this.setNoGravity(true);
        castSupportMagic();
    }


    private void castSupportMagic(){
        if (cureCooldown > 0){
            cureCooldown--;
            return;
        }

        if (aeroCooldown > 0){
            aeroCooldown--;
        }

        if (esunaCooldown > 0){
            esunaCooldown--;
        }

        if (autoLifeCooldown > 0){
            autoLifeCooldown--;
        }

        if (castCooldown > 0){
            castCooldown--;
        }

        if (owner != null && owner.isAlive()){
            if (castCooldown == 0) {



                //owner.sendSystemMessage(Component.literal(owner.getHealth() + ""));
                // Cure Logic
                float healAmount;
                PlayerData ownerData = PlayerData.get(owner);
                if (ownerData == null) return;
                if (ownerData.getMagicsMap().containsKey(Strings.Magic_Cure)) {
                    if (cureCooldown == 0) {
                        if (owner.isHurt() || owner.hasEffect(ModMobEffects.KO)) {
                            int cureLevel = ownerData.getMagicLevel(ResourceLocation.parse(Strings.Magic_Cure));
                            switch (cureLevel) {
                                case 0:
                                    ((ServerLevel) owner.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), owner.getX(), owner.getY() + 2.3D, owner.getZ(), 5, 0D, 0D, 0D, 0D);
                                    healAmount = (float) (mag);
                                    owner.heal(healAmount);
                                    System.out.println(healAmount);
                                    if (owner.hasEffect(ModMobEffects.KO)){
                                        owner.removeEffect(ModMobEffects.KO);
                                    }
                                    owner.level().playSound(null, owner.position().x(), owner.position().y(), owner.position().z(), ModSounds.cure.get(), SoundSource.PLAYERS, 1f, 1f);
                                    owner.sendSystemMessage(Component.literal("<Chirithy> Cure!"));
                                    break;
                                case 1:
                                    ((ServerLevel) owner.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), owner.getX(), owner.getY() + 2.3D, owner.getZ(), 5, 0D, 0D, 0D, 0D);
                                    healAmount = (float) (mag * 1.1f);
                                    owner.heal(healAmount);
                                    System.out.println(healAmount);
                                    if (owner.hasEffect(ModMobEffects.KO)){
                                        owner.removeEffect(ModMobEffects.KO);
                                    }
                                    owner.level().playSound(null, owner.position().x(), owner.position().y(), owner.position().z(), ModSounds.cura.get(), SoundSource.PLAYERS, 1f, 1f);
                                    owner.sendSystemMessage(Component.literal("<Chirithy> Cura!"));
                                    break;
                                case 2:
                                    ((ServerLevel) owner.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), owner.getX(), owner.getY() + 2.3D, owner.getZ(), 5, 0D, 0D, 0D, 0D);
                                    healAmount = (float) (mag * 1.25f);
                                    owner.heal(healAmount);
                                    System.out.println(healAmount);
                                    if (owner.hasEffect(ModMobEffects.KO)){
                                        owner.removeEffect(ModMobEffects.KO);
                                    }
                                    owner.level().playSound(null, owner.position().x(), owner.position().y(), owner.position().z(), ModSounds.curaga.get(), SoundSource.PLAYERS, 1f, 1f);
                                    owner.sendSystemMessage(Component.literal("<Chirithy> Curaga!"));
                            }
                            this.startCasting();
                            cureCooldown = 400;
                            castCooldown = 20;
                        }
                    }
                }


                // Aero Logic
                if (ownerData.getMagicsMap().containsKey(Strings.Magic_Aero)) {
                    if (aeroCooldown == 0) {
                        if (owner.hurtTime > 0) {
                            int aeroLevel = ownerData.getMagicLevel(ResourceLocation.parse(Strings.Magic_Aero));
                            int time = (int) (chirithyMagic * 100) * (1 + aeroLevel);
                            owner.addEffect(new MobEffectInstance(ModMobEffects.AERO, time, aeroLevel, false, false, true));
                            PacketHandler.sendToAll(new SCAeroSoundPacket(owner.getId()));
                            owner.level().playSound(null, owner.position().x(), owner.position().y(), owner.position().z(), ModSounds.aero1.get(), SoundSource.PLAYERS, 1F, 1F);
                            owner.sendSystemMessage(Component.literal("<Chirithy> Winds guard you!"));
                            this.startCasting();
                            aeroCooldown = 300;
                            castCooldown = 20;

                        }
                    }
                }

                // Heal Self
                if (cureCooldown == 0) {
                    if (this.getHealth() < this.getMaxHealth() && !owner.isHurt()) {
                        this.heal((float) mag);
                        ((ServerLevel) this.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), this.getX(), this.getY() + 2.3D, this.getZ(), 5, 0D, 0D, 0D, 0D);
                        this.level().playSound(null, this.position().x(), this.position().y(), this.position().z(), ModSounds.cure.get(), SoundSource.NEUTRAL, 1f, 1f);
                        owner.sendSystemMessage(Component.literal("<Chirithy> Gotta patch myself up!"));
                        this.startCasting();
                        cureCooldown = 400;
                        castCooldown = 20;

                    }
                }

                // Esuna Logic

                if (ownerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_esuna"))) {
                    if (esunaCooldown == 0) {
                        for (MobEffectInstance effect : owner.getActiveEffects()) {
                            if (effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                                owner.removeEffect(effect.getEffect());
                                owner.sendSystemMessage(Component.literal("<Chirithy> No more ailments!"));
                                owner.level().playSound(null, owner.position().x(), owner.position().y(), owner.position().z(), ModSoundsRM.ESUNA.get(), SoundSource.PLAYERS, 1F, 1F);
                                this.startCasting();
                                esunaCooldown = 600;
                                castCooldown = 20;

                            }
                        }
                    }
                }

                // Auto-Life
                if (ownerData.getMagicsMap().containsKey((KingdomKeysReMind.MODID + ":" + "magic_auto-life"))) {
                    if (!owner.hasEffect(ModMobEffectsRM.AUTO_LIFE)){
                        if (autoLifeCooldown == 0){
                            owner.addEffect(new MobEffectInstance(ModMobEffectsRM.AUTO_LIFE,Integer.MAX_VALUE, 0,false,false));
                            owner.level().playSound(null, owner.getX(), owner.getY(), owner.getZ(), ModSoundsRM.AUTOLIFE.get(), SoundSource.PLAYERS, 1F, 1F);
                            owner.sendSystemMessage(Component.literal("<Chirithy> Not gonna let you die! Auto-Life!"));
                            this.startCasting();
                            autoLifeCooldown = ModConfigs.autoLifeCD * 1200;
                            castCooldown = 20;

                        }
                    }
                }
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
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        // Targeting
        //this.targetSelector.addGoal(1);

    }


    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1000.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.5D);
    }

    public int getMagic(){
        return (int) chirithyMagic;
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
        } else {
            if (chirithyDefense > 0){
                amount = (float) Math.round((amount * 100 / (300 + chirithyDefense)));
            }
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
        builder.define(VARIANT, 0); // 0 = normal, 1 = alt
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


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }

    BaseDreamEaterEntity data;

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        // TODO Stats
        //System.out.println(data.serializeNBT());
        compound.put("data", data.serializeNBT());
        super.addAdditionalSaveData(compound);
    }
}
