package online.remind.remind.entity.reactioncommand;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.ModEntitiesRM;
import online.remind.remind.lib.StringsRM;
import org.joml.Vector3f;

public class LightBeamEntity extends ThrowableProjectile {
    int maxTicks = 10;
    float dmg;
    boolean faith;

    @Override
    protected float getGravity() {
        return 0F;
    }

    public LightBeamEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
    }

    public LightBeamEntity(PlayMessages.SpawnEntity spawnEntity, Level world){
        super(ModEntitiesRM.TYPE_LIGHT_BEAM.get(), world);
    }

    public LightBeamEntity(Level world, LivingEntity player, float damage, double x, double y, double z, boolean faith) {
        this(world, player, damage, faith);
        this.setPos(x,y,z);
    }

    public LightBeamEntity(Level world) {
        super(ModEntitiesRM.TYPE_LIGHT_BEAM.get(), world);
        this.blocksBuilding = true;
    }

    public LightBeamEntity(Level world, LivingEntity player, float damage, boolean faith) {
        super(ModEntitiesRM.TYPE_LIGHT_BEAM.get(), player, world);
        this.dmg = damage;
        this.faith = faith;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        if (this.tickCount > maxTicks) {
            this.remove(RemovalReason.KILLED);
        }

        //world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
        if(tickCount > 0)
            level().addParticle(ParticleTypes.END_ROD, getX(), getY(), getZ(), 0, 0, 0);

        level().addAlwaysVisibleParticle(ParticleTypes.CLOUD, getX() + level().random.nextDouble() - 0.5D, getY()+ level().random.nextDouble() *2D, getZ() + level().random.nextDouble() - 0.5D, 0, 0, 0);
        level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0F,0.9F,0.9F),1F),getX() + level().random.nextDouble() - 0.5D, getY()+ level().random.nextDouble() *2D, getZ() + level().random.nextDouble() - 0.5D, 0, 0, 0);
        level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F,1F,0.7F),1F),getX() + level().random.nextDouble() - 0.5D, getY()+ level().random.nextDouble() *2D, getZ() + level().random.nextDouble() - 0.5D, 0, 0, 0);

        if(faith)
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.5, 0));

        super.tick();
    }

    @Override
    protected void onHit(HitResult rtRes) {
        if (!level().isClientSide && getOwner() != null) {
            EntityHitResult ertResult = null;
            BlockHitResult brtResult = null;

            if (rtRes instanceof EntityHitResult) {
                ertResult = (EntityHitResult) rtRes;
            }

            if (rtRes instanceof BlockHitResult) {
                brtResult = (BlockHitResult) rtRes;
            }

            if (ertResult != null && ertResult.getEntity() instanceof LivingEntity target) {
                Player player = (Player) this.getOwner();
                if (target != getOwner()) {
                    Party p = null;
                    if (getOwner() != null) {
                        p = ModCapabilities.getWorld(getOwner().level()).getPartyFromMember(getOwner().getUUID());
                    }
                    if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
                        float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) : 2;
                        
                        target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHT,this, this.getOwner()), dmg);
                    }
                    IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
                    if(playerData.getActiveDriveForm().equals(KingdomKeysReMind.MODID + ":" + StringsRM.lightForm)) {
                        playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), playerData.getDriveFormExp(playerData.getActiveDriveForm()) + 2);
                    }

                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {

    }
}
