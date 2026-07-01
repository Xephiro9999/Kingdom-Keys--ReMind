package online.remind.remind.client.render.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import online.remind.remind.client.model.mob.CactuarModel;
import online.remind.remind.entity.enemies.CactuarEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CactuarRenderer extends GeoEntityRenderer<CactuarEntity> {

    private static final float NORMAL_SCALE = 0.85F;
    private static final float JUMBO_SCALE = 10.0F;

    public CactuarRenderer(EntityRendererProvider.Context context) {
        super(context, new CactuarModel());
        this.shadowRadius = 0.35F;
    }

    @Override
    public void render(
            CactuarEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight
    ) {
        poseStack.pushPose();

        if (entity.isJumbo()) {
            poseStack.scale(JUMBO_SCALE, JUMBO_SCALE, JUMBO_SCALE);
            this.shadowRadius = 4.5F;
        } else {
            poseStack.scale(NORMAL_SCALE, NORMAL_SCALE, NORMAL_SCALE);
            this.shadowRadius = 0.35F;
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }

    /*
     * IMPORTANT:
     * GeckoLib/vanilla applies a death flip by default.
     * That flip uses the entity's facing direction, which is why the body changes angle
     * depending on where/how it dies.
     *
     * Returning 0 disables that renderer death flip.
     * Your cactuar.animation.json "death" animation now controls the full death pose.
     */
    @Override
    protected float getDeathMaxRotation(CactuarEntity entity) {
        return 0.0F;
    }
}