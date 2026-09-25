package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.handler.AerialSlamSequenceHandler;

public class attackAerialSlam extends Magic {

    public attackAerialSlam(ResourceLocation registryName, boolean hasToSelect, int tier, ResourceLocation gmAbility) {
        super(registryName, hasToSelect, gmAbility);
        setTier(tier);
    }

    @Override
    public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
        if (!(caster instanceof ServerPlayer serverPlayer)) return;
        if (lockOnEntity == null || !lockOnEntity.isAlive()) return;
        if (AerialSlamSequenceHandler.isActive(serverPlayer)) return;

        float damage = casterStrengthStat(caster) * fullMPBlastMult;
        AerialSlamSequenceHandler.start(serverPlayer, lockOnEntity, damage);
    }

    @Override
    public void playMagicCastSound(LivingEntity livingEntity, LivingEntity livingEntity1) {

    }
}