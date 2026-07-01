package online.remind.remind.client.model.mob;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.enemies.CactuarEntity;
import software.bernie.geckolib.model.GeoModel;

public class CactuarModel extends GeoModel<CactuarEntity> {

    @Override
    public ResourceLocation getModelResource(CactuarEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(
                KingdomKeysReMind.MODID,
                "geo/entity/cactuar.geo.json"
        );
    }

    @Override
    public ResourceLocation getTextureResource(CactuarEntity animatable) {
        if (animatable.isJumbo()) {
            return ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID,
                    "textures/entity/cactuar/jumbo_cactuar.png"
            );
        }

        return ResourceLocation.fromNamespaceAndPath(
                KingdomKeysReMind.MODID,
                "textures/entity/cactuar/cactuar.png"
        );
    }

    @Override
    public ResourceLocation getAnimationResource(CactuarEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(
                KingdomKeysReMind.MODID,
                "animations/entity/cactuar.animation.json"
        );
    }
}