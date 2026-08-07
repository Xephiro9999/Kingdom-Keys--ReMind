package online.remind.remind.init;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import online.remind.remind.KingdomKeysReMind;

public final class ModDamageTypes {

    public static final ResourceKey<DamageType> CACTUAR_NEEDLE =
            ResourceKey.create(
                    Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(
                            KingdomKeysReMind.MODID,
                            "cactuar_needle"
                    )
            );

    private ModDamageTypes() {
    }

    public static DamageSource cactuarNeedle(
            Level level,
            Entity projectile,
            Entity owner
    ) {
        Holder<DamageType> damageType = level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(CACTUAR_NEEDLE);

        return new DamageSource(
                damageType,
                projectile,
                owner
        );
    }
}