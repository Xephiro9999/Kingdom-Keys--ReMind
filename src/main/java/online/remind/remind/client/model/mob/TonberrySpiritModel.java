package online.remind.remind.client.model.mob;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.spirits.TonberrySpiritEntity;
import software.bernie.geckolib.model.GeoModel;

public class TonberrySpiritModel extends GeoModel<TonberrySpiritEntity> {

    @Override
    public ResourceLocation getModelResource(TonberrySpiritEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "geo/entity/tonberry.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TonberrySpiritEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/entity/tonberry.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TonberrySpiritEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "animations/entity/tonberry.animation.json");
    }
}