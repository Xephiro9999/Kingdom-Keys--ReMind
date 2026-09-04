package online.remind.remind.client.render.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import online.remind.remind.client.model.mob.TonberrySpiritModel;
import online.remind.remind.entity.spirits.TonberrySpiritEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TonberrySpiritRenderer extends GeoEntityRenderer<TonberrySpiritEntity> {

    public TonberrySpiritRenderer(EntityRendererProvider.Context context) {
        super(context, new TonberrySpiritModel());
        this.shadowRadius = 0.35F;
    }

    @Override
    public void render(
            TonberrySpiritEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight
    ) {
        poseStack.pushPose();
        poseStack.scale(0.9F, 0.9F, 0.9F);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }

    @Override
    protected float getDeathMaxRotation(TonberrySpiritEntity entity) {
        return 0.0F;
    }
}