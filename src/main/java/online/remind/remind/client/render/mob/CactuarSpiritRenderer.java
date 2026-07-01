package online.remind.remind.client.render.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import online.remind.remind.client.model.mob.CactuarSpiritModel;
import online.remind.remind.entity.spirits.CactuarSpiritEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CactuarSpiritRenderer extends GeoEntityRenderer<CactuarSpiritEntity> {

    private static final float SCALE = 0.85F;

    public CactuarSpiritRenderer(EntityRendererProvider.Context context) {
        super(context, new CactuarSpiritModel());
        this.shadowRadius = 0.35F;
    }

    @Override
    public void render(
            CactuarSpiritEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight
    ) {
        poseStack.pushPose();
        poseStack.scale(SCALE, SCALE, SCALE);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }

    @Override
    protected float getDeathMaxRotation(CactuarSpiritEntity entity) {
        return 0.0F;
    }
}