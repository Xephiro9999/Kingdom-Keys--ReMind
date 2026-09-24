package online.remind.remind.magic.attacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.handler.ArsArcanumSequenceHandler;

public class attackArsArcanum extends Magic {

    public attackArsArcanum(
            ResourceLocation registryName,
            boolean hasToSelect,
            int tier,
            ResourceLocation gmAbility
    ) {
        super(registryName, hasToSelect, gmAbility);
        setTier(tier);
    }

    @Override
    public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {

        if (!(caster instanceof ServerPlayer serverPlayer)) {
            return;
        }

        // Don't restart Ars Arcanum while the combo / RC chain is active.
        if (ArsArcanumSequenceHandler.isActive(serverPlayer)) {
            return;
        }

        // Ars Arcanum requires a valid target.
        if (lockOnEntity == null || !lockOnEntity.isAlive()) {
            return;
        }

        float dmg = switch (getTier()) {

            case 0 ->
                    casterStrengthStat(caster) * 1.15F;

            case 1 ->
                    casterStrengthStat(caster) * 1.35F;

            case 2 ->
                    casterStrengthStat(caster) * 1.55F;

            default ->
                    casterStrengthStat(caster);
        };

        dmg *= fullMPBlastMult;

        /*
         * Starts ONLY the Ars Arcanum portion.
         *
         * The handler performs:
         *
         * Hits 1-7 automatically
         *      ↓
         * waits for Bash RC
         *
         * Bash RC calls useBash()
         * for hits 8-12
         *
         * Finish RC calls useFinish()
         * for hit 13
         */
        ArsArcanumSequenceHandler.start(
                serverPlayer,
                lockOnEntity,
                dmg
        );
    }

    @Override
    public void playMagicCastSound(
            LivingEntity livingEntity,
            LivingEntity livingEntity1
    ) {

    }
}