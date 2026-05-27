package online.remind.remind.entity.attacks;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public enum StrikeElement {
    FIRE(
            ParticleTypes.FLAME,
            SoundEvents.FIRECHARGE_USE,
            1.15F,
            true,
            false,
            false
    ),

    BLIZZARD(
            ParticleTypes.SNOWFLAKE,
            SoundEvents.PLAYER_HURT_FREEZE,
            1.05F,
            false,
            false,
            false
    ),

    THUNDER(
            ParticleTypes.ELECTRIC_SPARK,
            SoundEvents.TRIDENT_THUNDER.value(),
            1.20F,
            false,
            false,
            false
    ),

    WATER(
            ParticleTypes.SPLASH,
            SoundEvents.PLAYER_SPLASH,
            1.00F,
            false,
            false,
            false
    ),

    AERO(
            ParticleTypes.GUST,
            SoundEvents.BREEZE_SHOOT,
            1.00F,
            false,
            false,
            false
    ),

    LIGHT(
            ParticleTypes.END_ROD,
            SoundEvents.AMETHYST_BLOCK_CHIME,
            1.25F,
            false,
            false,
            false
    ),

    DARK(
            ParticleTypes.SOUL,
            SoundEvents.WITHER_SHOOT,
            1.25F,
            false,
            false,
            false
    ),

    BINDING(
            ParticleTypes.ENCHANT,
            SoundEvents.ENCHANTMENT_TABLE_USE,
            0.90F,
            false,
            true,
            false
    ),

    CONFUSION(
            ParticleTypes.WITCH,
            SoundEvents.ILLUSIONER_CAST_SPELL,
            0.90F,
            false,
            false,
            true
    );

    private final ParticleOptions particle;
    private final SoundEvent sound;
    private final float damageMultiplier;
    private final boolean setsFire;
    private final boolean binds;
    private final boolean confuses;

    StrikeElement(
            ParticleOptions particle,
            SoundEvent sound,
            float damageMultiplier,
            boolean setsFire,
            boolean binds,
            boolean confuses
    ) {
        this.particle = particle;
        this.sound = sound;
        this.damageMultiplier = damageMultiplier;
        this.setsFire = setsFire;
        this.binds = binds;
        this.confuses = confuses;
    }

    public ParticleOptions getParticle() {
        return particle;
    }

    public SoundEvent getSound() {
        return sound;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public boolean setsFire() {
        return setsFire;
    }

    public boolean binds() {
        return binds;
    }

    public boolean confuses() {
        return confuses;
    }
}