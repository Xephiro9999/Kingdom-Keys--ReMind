package online.remind.remind.client.model.mob;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.spirits.MeowWowEntity;
import software.bernie.geckolib.model.GeoModel;

public class MeowWowModel extends GeoModel<MeowWowEntity> {

    private static final ResourceLocation MODEL =
            ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID,
                    "geo/entity/meow_wow.geo.json"
            );

    private static final ResourceLocation ANIMATIONS =
            ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID,
                    "animations/entity/meow_wow.animation.json"
            );

    private static final ResourceLocation SPIRIT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID,
                    "textures/entity/models/mobs/spirit_mw.png"
            );

    private static final ResourceLocation NIGHTMARE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID,
                    "textures/entity/models/mobs/nightmare_mw.png"
            );

    @Override
    public ResourceLocation getModelResource(MeowWowEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(MeowWowEntity animatable) {
        ResourceLocation texture = animatable.getVariant() == MeowWowEntity.VARIANT_ORG
                ? NIGHTMARE_TEXTURE
                : SPIRIT_TEXTURE;

        System.out.println("MEOW WOW TEXTURE = " + texture);

        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(MeowWowEntity animatable) {
        return ANIMATIONS;
    }
}