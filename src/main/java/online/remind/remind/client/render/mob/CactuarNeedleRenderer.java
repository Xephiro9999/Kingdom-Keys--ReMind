package online.remind.remind.client.render.mob;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import online.remind.remind.entity.projectile.CactuarNeedleProjectile;

public class CactuarNeedleRenderer extends ArrowRenderer<CactuarNeedleProjectile> {

    private static final ResourceLocation ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/projectiles/arrow.png");

    public CactuarNeedleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(CactuarNeedleProjectile entity) {
        return ARROW_TEXTURE;
    }
}