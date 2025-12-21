package online.remind.remind.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.model.mineModel;
import online.remind.remind.entity.magic.MineEntity;

import javax.annotation.Nullable;

public class MineEntityRenderer extends EntityRenderer<ThrowableProjectile> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"textures/entity/models/mine.png");
    mineModel<Entity> mineModel;

    public MineEntityRenderer(EntityRendererProvider.Context context){
        super(context);
        this.shadowRadius = 0.0F;
        mineModel = new mineModel<>(context.bakeLayer(mineModel.LAYER_LOCATION));
    }

    @Override
    public void render(ThrowableProjectile entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {



        matrixStackIn.pushPose();
        {
            VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

            float spin = (entity.tickCount + partialTicks) *16f;
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(spin));

            matrixStackIn.scale(2, 2, 2);
            matrixStackIn.translate(0, 0, 0);
            this.mineModel.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFF);
        }

        matrixStackIn.popPose();

        float spin = (entity.tickCount + partialTicks) *6f;
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(spin));


        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }



    @Nullable
    @Override
    public ResourceLocation getTextureLocation(ThrowableProjectile entity) {
        return TEXTURE;
    }
}
