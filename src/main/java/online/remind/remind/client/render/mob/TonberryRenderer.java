package online.remind.remind.client.render.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import online.remind.remind.client.model.mob.TonberryModel;
import online.remind.remind.entity.enemies.TonberryEntity;
import online.remind.remind.entity.enemies.TonberryKingEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TonberryRenderer<T extends TonberryEntity> extends GeoEntityRenderer<T> {

    public TonberryRenderer(EntityRendererProvider.Context context) {
        super(context, new TonberryModel<>());
        this.shadowRadius = 0.4F;
    }

    @Override
    public void render(
            T entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight
    ) {
        poseStack.pushPose();

        if (entity instanceof TonberryKingEntity) {
            poseStack.scale(3.2F, 3.2F, 3.2F);
            this.shadowRadius = 1.0F;
        } else {
            poseStack.scale(1.0F, 1.0F, 1.0F);
            this.shadowRadius = 0.4F;
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }

    @Override
    protected float getDeathMaxRotation(T entity) {
        return 0.0F;
    }
}