package online.remind.remind.entity.spirits;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCAeroSoundPacket;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.config.ModConfigs;
import online.remind.remind.dreameater.DreamEaterPetHelper;
import online.remind.remind.dreameater.ModDreamEaters;
import online.remind.remind.effect.ModMobEffectsRM;
import online.remind.remind.entity.ModEntitiesRM;
import online.remind.remind.entity.spirits.goal.ChirithyGoal;
import online.remind.remind.network.PacketHandlerRM;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ChirithyEntity extends BaseDreamEaterEntity implements GeoEntity {
    
    Player owner;

    private UUID ownerUUID;


    private double chirithyHP;
    private double chirithyStrength;
    private double chirithyMagic;
    private double chirithyDefense;
    private int cureCooldown = 60;
    private int aeroCooldown = 60;
    private int esunaCooldown = 60;
    private double autoLifeCooldown = 60;
    private int castCooldown = 60;
    private float mpHasteMult;

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

    public ChirithyEntity(Level worldIn, Player owner) {
        this(ModEntitiesRM.TYPE_CHIRITHY.get(), worldIn);

        if (owner != null) {
            this.owner = owner;
            this.setOwnerUUID(owner.getUUID());
            this.setTame(true, true);

            PlayerData ownerData = PlayerData.get(owner);

            this.hp = (int) (20 + (ownerData.getMaxHP() / 2D));
            this.str = (int) (2 + (ownerData.getStrengthStat().getStat() / 5D));
            this.mag = (int) (5 + (ownerData.getMagicStat().getStat() * 0.75D));
            this.def = (int) (2 + (ownerData.getDefenseStat().getStat() / 2D));

            this.setHealth((float) hp);
        }
    }



    public void updateStatsFromOwner() {
        if (owner == null) {
            return;
        }

        PlayerData ownerData = PlayerData.get(owner);

        if (ownerData == null) {
            return;
        }

        hp = (int) (20 + (ownerData.getMaxHP() / 2D));
        str = (int) (2 + (ownerData.getStrengthStat().getStat() / 5D));
        mag = (int) (5 + (ownerData.getMagicStat().getStat() * 0.75D));
        def = (int) (2 + (ownerData.getDefenseStat().getStat() / 2D));

        chirithyHP = hp;
        chirithyStrength = str;
        chirithyMagic = mag;
        chirithyDefense = def;

        if (this.getAttribute(Attributes.MAX_HEALTH) != null) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(hp);
        }

        if (this.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(str);
        }

        if (this.getAttribute(Attributes.ARMOR) != null) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(def);
        }

        // Do NOT heal every tick.
        // Only clamp down if max HP got lower.
        if (this.getHealth() > this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }

        this.setStr(str);
        this.setMag(mag);
        this.setDef(def);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) return;

        UUID ownerId = this.getOwnerUUID();

        // No owner UUID = invalid summon
        if (ownerId == null) {
            this.discard();
            return;
        }

        Player owner = this.level().getPlayerByUUID(ownerId);

        // Owner is not in this dimension / not loaded / offline
        if (owner == null) {
            this.discard();
            return;
        }

        // Keep your field synced
        this.owner = owner;

        GlobalDataRM data = ModDataRM.getGlobal(owner);

        if (data == null) {
            this.discard();
            return;
        }

        if (owner.isDeadOrDying()) {
            data.setHasDreamEaterSummoned(false);
            data.setDreamEaterUUID(null);
            PacketHandlerRM.syncGlobalToAllAround(owner, data);
            this.discard();
            return;
        }

        updateStatsFromOwner();

        if (!this.isAlive()) {
            data.setHasDreamEaterSummoned(false);
            data.setDreamEaterUUID(null);
            PacketHandlerRM.syncGlobalToAllAround(owner, data);
            return;
        }

        if (!data.getDreamEaterRL().equals(ModDreamEaters.CHIRITHY.get().getRegistryName().toString())) {
            data.setHasDreamEaterSummoned(false);
            data.setDreamEaterUUID(null);
            PacketHandlerRM.syncGlobalToAllAround(owner, data);
            this.discard();
            return;
        }

        castSupportMagic();
    }


    private void castSupportMagic(){
        float healAmount;
        PlayerData ownerData = PlayerData.get(owner);
        GlobalDataRM globalData = ModDataRM.getGlobal(owner);

        if (ownerData == null) return;
        if (ownerData.isAbilityEquipped(Strings.mpHaste) || ownerData.isAbilityEquipped(Strings.mpHastera) || ownerData.isAbilityEquipped(Strings.mpHastega)) {
            int mpHastes = ownerData.getNumberOfAbilitiesEquipped(Strings.mpHaste);
            int mpHasteras = ownerData.getNumberOfAbilitiesEquipped(Strings.mpHastera);
            int mpHastegas = ownerData.getNumberOfAbilitiesEquipped(Strings.mpHastega);
            mpHasteMult = (mpHastes * 0.15f) + (mpHasteras * 0.3f) + (mpHastegas * 0.45f);
            //System.out.println("MP Haste Mult: " + mpHasteMult);
        }

        if (cureCooldown <= 0){
            cureCooldown = 0;
        }
        if (aeroCooldown <= 0){
            aeroCooldown = 0;
        }
        if (esunaCooldown <= 0){
            esunaCooldown = 0;
        }
        if (autoLifeCooldown <= 0){
            autoLifeCooldown = 0;
        }


        if (cureCooldown > 0){
            cureCooldown--;
            cureCooldown -= (int) mpHasteMult;
            return;
        }

        if (aeroCooldown > 0){
            aeroCooldown--;
            aeroCooldown -= (int) mpHasteMult;
            return;
        }

        if (esunaCooldown > 0){
            esunaCooldown--;
            esunaCooldown -= (int) mpHasteMult;
            return;
        }

        if (autoLifeCooldown > 0){
            autoLifeCooldown--;
            autoLifeCooldown -= (int) mpHasteMult;
            return;
        }

        if (castCooldown > 0){
            castCooldown--;
        }

        if (owner != null && owner.isAlive()){



            if (castCooldown == 0) {

                if (ownerData == null ||globalData == null) return;

                // --------------- Cure ---------------------

                if (globalData.getLearndedMagics().containsKey(Strings.Magic_Cure)) {

                    if (cureCooldown == 0 && castCooldown == 0) {
                        if (owner.isHurt() || owner.hasEffect(ModMobEffects.KO)) {

                            int cureLevel = globalData.getLearnedMagicLevel(ResourceLocation.parse(Strings.Magic_Cure));


                            switch (cureLevel) {
                                case 0:
                                    // Cure
                                    healAmount = (float) mag;
                                    owner.level().playSound(null, owner.getX(), owner.getY(), owner.getZ(), ModSounds.cure.get(), SoundSource.PLAYERS, 1f, 1f);
                                    owner.sendSystemMessage(Component.literal("<Chirithy> Cure!"));
                                    break;

                                case 1:
                                    // Cura
                                    healAmount = mag * 1.1f;
                                    owner.level().playSound(null, owner.getX(), owner.getY(), owner.getZ(), ModSounds.cura.get(), SoundSource.PLAYERS, 1f, 1f);
                                    owner.sendSystemMessage(Component.literal("<Chirithy> Cura!"));
                                    break;

                                case 2:
                                    // Curaga
                                    healAmount = mag * 1.25f;
                                    owner.level().playSound(null, owner.getX(), owner.getY(), owner.getZ(), ModSounds.curaga.get(), SoundSource.PLAYERS, 1f, 1f);
                                    owner.sendSystemMessage(Component.literal("<Chirithy> Curaga!"));
                                    break;

                                default:
                                    healAmount = (float) mag;
                                    owner.level().playSound(null, owner.getX(), owner.getY(), owner.getZ(), ModSounds.cure.get(), SoundSource.PLAYERS, 1f, 1f);
                                    owner.sendSystemMessage(Component.literal("<Chirithy> Cure!"));
                                    break;
                            }

                            ((ServerLevel) owner.level()).sendParticles(
                                    ParticleTypes.HAPPY_VILLAGER,
                                    owner.getX(),
                                    owner.getY() + 2.3D,
                                    owner.getZ(),
                                    5,
                                    0D,
                                    0D,
                                    0D,
                                    0D
                            );

                            owner.heal(healAmount);

                            if (owner.hasEffect(ModMobEffects.KO)) {
                                owner.removeEffect(ModMobEffects.KO);
                            }

                            this.startCasting();
                            cureCooldown = 180;
                            castCooldown = 40;
                        }
                    }
                }




                // --------------- Aero ---------------------

                if (globalData.getLearndedMagics().containsKey(Strings.Magic_Aero)) {
                    if (aeroCooldown == 0 && castCooldown == 0) {
                        if (owner.hurtTime > 0) {

                            int aeroLevel = globalData.getLearnedMagicLevel(ResourceLocation.parse(Strings.Magic_Aero));

                            int amplifier = aeroLevel;
                            int time;

                            switch (aeroLevel) {
                                case 0:
                                    // Aero
                                    time = (int) (chirithyMagic * 100);
                                    owner.addEffect(new MobEffectInstance(ModMobEffects.AERO, time * 20, amplifier, false, false, true));

                                    PacketHandler.sendToAll(new SCAeroSoundPacket(owner.getId()));
                                    owner.level().playSound(
                                            null,
                                            owner.position().x(),
                                            owner.position().y(),
                                            owner.position().z(),
                                            ModSounds.aero1.get(),
                                            SoundSource.PLAYERS,
                                            1F,
                                            1F
                                    );

                                    owner.sendSystemMessage(Component.literal("<Chirithy> Aero! Winds guard you!"));
                                    break;

                                case 1:
                                    // Aerora
                                    time = (int) (chirithyMagic * 125);
                                    owner.addEffect(new MobEffectInstance(ModMobEffects.AERO, time * 20, amplifier, false, false, true));

                                    PacketHandler.sendToAll(new SCAeroSoundPacket(owner.getId()));
                                    owner.level().playSound(
                                            null,
                                            owner.position().x(),
                                            owner.position().y(),
                                            owner.position().z(),
                                            ModSounds.aero1.get(),
                                            SoundSource.PLAYERS,
                                            1F,
                                            1F
                                    );

                                    owner.sendSystemMessage(Component.literal("<Chirithy> Aerora! Winds guard you!"));
                                    break;

                                case 2:
                                    // Aeroga
                                    time = (int) (chirithyMagic * 150);
                                    owner.addEffect(new MobEffectInstance(ModMobEffects.AERO, time * 20, amplifier, false, false, true));

                                    PacketHandler.sendToAll(new SCAeroSoundPacket(owner.getId()));
                                    owner.level().playSound(
                                            null,
                                            owner.position().x(),
                                            owner.position().y(),
                                            owner.position().z(),
                                            ModSounds.aero1.get(),
                                            SoundSource.PLAYERS,
                                            1F,
                                            1F
                                    );

                                    owner.sendSystemMessage(Component.literal("<Chirithy> Aeroga! Winds guard you!"));
                                    break;

                                default:
                                    // Fallback to Aero if something weird happens
                                    time = (int) (chirithyMagic * 100);
                                    owner.addEffect(new MobEffectInstance(ModMobEffects.AERO, time * 20, 0, false, false, true));

                                    PacketHandler.sendToAll(new SCAeroSoundPacket(owner.getId()));
                                    owner.level().playSound(
                                            null,
                                            owner.position().x(),
                                            owner.position().y(),
                                            owner.position().z(),
                                            ModSounds.aero1.get(),
                                            SoundSource.PLAYERS,
                                            1F,
                                            1F
                                    );

                                    owner.sendSystemMessage(Component.literal("<Chirithy> Aero! Winds guard you!"));
                                    break;
                            }

                            this.startCasting();
                            aeroCooldown = 300;
                            castCooldown = 20;
                        }
                    }
                }

                // Heal Self
                if (cureCooldown == 0 && castCooldown == 0) {
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

                if (globalData.getLearndedMagics().containsKey((KingdomKeysReMind.MODID + ":" + "magic_esuna")) || globalData.getLearndedMagics().containsKey((KingdomKeysReMind.MODID + ":" + "magic_group_esuna"))) {
                    if (esunaCooldown == 0 && castCooldown == 0) {

                        List<Holder<MobEffect>> toRemove = new ArrayList<>();

                        Iterator<MobEffectInstance> iterator = owner.getActiveEffects().iterator();

                        while (iterator.hasNext()){
                            MobEffectInstance effect = iterator.next();

                            if (effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                                toRemove.add(effect.getEffect());
                            }
                        }

                        if (!toRemove.isEmpty()){
                            for (Holder<MobEffect> effect : toRemove){
                                owner.removeEffect(effect);
                            }
                            owner.sendSystemMessage(Component.literal("<Chirithy> No more ailments!"));
                            owner.level().playSound(null, owner.position().x(), owner.position().y(), owner.position().z(), ModSoundsRM.ESUNA.get(), SoundSource.PLAYERS, 1F, 1F);
                            this.startCasting();
                            esunaCooldown = 600;
                            castCooldown = 20;
                        }
                    }
                }

                // Auto-Life
                if (globalData.getLearndedMagics().containsKey((KingdomKeysReMind.MODID + ":" + "magic_auto-life"))) {
                    if (!owner.hasEffect(ModMobEffectsRM.AUTO_LIFE)){
                        if (autoLifeCooldown == 0 && castCooldown == 0){
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
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult result = DreamEaterPetHelper.tryPetDreamEater(
                this,
                player,
                hand,
                this.getOwnerUUID(),
                "Chirithy"
        );

        if (result != InteractionResult.PASS) {
            return result;
        }

        return super.mobInteract(player, hand);
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

    @Override
    public void setOwnerUUID(@Nullable UUID uuid) {
        this.ownerUUID = uuid;
        super.setOwnerUUID(uuid);
    }

    @Override
    @Nullable
    public UUID getOwnerUUID() {
        return this.ownerUUID != null ? this.ownerUUID : super.getOwnerUUID();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    public static void removeExistingChirithy(ServerLevel level, UUID ownerUUID) {
        MinecraftServer server = level.getServer();

        for (ServerLevel serverLevel : server.getAllLevels()) {
            for (Entity entity : serverLevel.getAllEntities()) {
                if (entity instanceof ChirithyEntity chirithy) {
                    if (ownerUUID.equals(chirithy.getOwnerUUID())) {
                        chirithy.discard();
                    }
                }
            }
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }

    private BaseDreamEaterEntity data;

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        if (this.getOwnerUUID() != null) {
            compound.putUUID("DreamEaterOwner", this.getOwnerUUID());
        }

        if (this.data != null) {
            compound.put("data", this.data.serializeNBT());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.hasUUID("DreamEaterOwner")) {
            this.setOwnerUUID(compound.getUUID("DreamEaterOwner"));
        }

        if (this.data == null) {
            this.data = new BaseDreamEaterEntity((EntityType<? extends TamableAnimal>) this.getType(), this.level());
        }

        if (compound.contains("data")) {
            this.data.readAdditionalSaveData(compound.getCompound("data"));
        }
    }
}
