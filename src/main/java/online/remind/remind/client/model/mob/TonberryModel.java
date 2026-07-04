package online.remind.remind.client.model.mob;

import net.minecraft.resources.ResourceLocation;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.enemies.TonberryEntity;
import online.remind.remind.entity.enemies.TonberryKingEntity;
import software.bernie.geckolib.model.GeoModel;

public class TonberryModel<T extends TonberryEntity> extends GeoModel<T> {

    @Override
    public ResourceLocation getModelResource(T animatable) {
        if (animatable instanceof TonberryKingEntity) {
            return ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "geo/entity/tonberry_king.geo.json");
        }

        return ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "geo/entity/tonberry.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        if (animatable instanceof TonberryKingEntity) {
            return ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/entity/tonberry_king.png");
        }

        return ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/entity/tonberry.png");
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        if (animatable instanceof TonberryKingEntity) {
            return ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "animations/entity/tonberry_king.animation.json");
        }

        return ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "animations/entity/tonberry.animation.json");
    }
}