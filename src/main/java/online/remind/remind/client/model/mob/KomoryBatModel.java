package online.remind.remind.client.model.mob;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.spirits.KomoryBatEntity;
import software.bernie.geckolib.model.GeoModel;

public class KomoryBatModel extends GeoModel<KomoryBatEntity> {

    @Override
    public ResourceLocation getModelResource(KomoryBatEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(
                KingdomKeysReMind.MODID,
                "geo/entity/komory_bat.geo.json"
        );
    }

    private static final ResourceLocation SPIRIT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID,
                    "textures/entity/models/mobs/spirit_kb.png"
            );

    private static final ResourceLocation NIGHTMARE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID,
                    "textures/entity/models/mobs/nightmare_kb.png"
            );

    @Override
    public ResourceLocation getTextureResource(KomoryBatEntity animatable) {
        ResourceLocation texture = animatable.getVariant() == KomoryBatEntity.VARIANT_ORG
                ? NIGHTMARE_TEXTURE
                : SPIRIT_TEXTURE;


        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(KomoryBatEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(
                KingdomKeysReMind.MODID,
                "animations/entity/komory_bat.animation.json"
        );
    }


}