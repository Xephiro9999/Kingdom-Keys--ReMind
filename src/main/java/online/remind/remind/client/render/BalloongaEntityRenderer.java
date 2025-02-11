package online.remind.remind.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.model.BalloonModel;
import online.remind.remind.client.model.BalloongaModel;

import javax.annotation.Nullable;

public class BalloongaEntityRenderer extends EntityRenderer<ThrowableProjectile> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"textures/entity/models/balloonga.png");
    BalloongaModel balloongaModel;

    public BalloongaEntityRenderer(EntityRendererProvider.Context context){
        super(context);
        this.shadowRadius = 0.25F;
        balloongaModel = new BalloongaModel<>(context.bakeLayer(BalloonModel.LAYER_LOCATION));
    }

    float ticks = 0F;
    float prevRotationTicks = 0F;

    @Override
    public void render(ThrowableProjectile entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        {
            VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
            matrixStackIn.scale(3, 3, 3);
            matrixStackIn.translate(0, -0.65, 0);

            this.balloongaModel.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFF);
        }

        matrixStackIn.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(ThrowableProjectile entity) {
        return TEXTURE;
    }
}
