package online.remind.remind.client.render.mob;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import online.remind.remind.client.model.mob.KomoryBatModel;
import online.remind.remind.entity.spirits.KomoryBatEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class KomoryBatRenderer extends GeoEntityRenderer<KomoryBatEntity> {

    public KomoryBatRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new KomoryBatModel());
        this.shadowRadius = 0.25F;
        this.withScale(0.15f);
    }
}