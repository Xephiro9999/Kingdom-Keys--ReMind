package online.remind.remind.client.model.mob;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.spirits.CactuarSpiritEntity;
import software.bernie.geckolib.model.GeoModel;

public class CactuarSpiritModel extends GeoModel<CactuarSpiritEntity> {

    @Override
    public ResourceLocation getModelResource(CactuarSpiritEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(
                KingdomKeysReMind.MODID,
                "geo/entity/cactuar.geo.json"
        );
    }

    @Override
    public ResourceLocation getTextureResource(CactuarSpiritEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(
                KingdomKeysReMind.MODID,
                "textures/entity/cactuar/cactuar.png"
        );
    }

    @Override
    public ResourceLocation getAnimationResource(CactuarSpiritEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(
                KingdomKeysReMind.MODID,
                "animations/entity/cactuar.animation.json"
        );
    }
}