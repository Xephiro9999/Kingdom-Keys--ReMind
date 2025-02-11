package online.remind.remind.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.model.UltimaModel;
import online.remind.remind.entity.magic.UltimaEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class UltimaEntityRenderer extends EntityRenderer<UltimaEntity> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"textures/entity/models/ultima.png");

	UltimaModel<Entity> model;

	public UltimaEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.25F;
        model = new UltimaModel<>(context.bakeLayer(UltimaModel.LAYER_LOCATION));

	}

	@Override
	public void render(UltimaEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
		matrixStackIn.pushPose();
		{
            matrixStackIn.translate(0, 1, 0);
			float ticks = entity.tickCount;
			matrixStackIn.scale(1.1F,1.1F,1.1F);
			
			float radius = (ticks-entity.getStartingTicks()) * 0.2f;
			 if(entity.getStartingTicks() > -1) { // Grow alongside hitbox, it no grow
				matrixStackIn.scale(radius, radius, radius);
	            model.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFF);
			} else if (ticks > 25) {
				matrixStackIn.scale(0.5F,2,0.5F);
	            matrixStackIn.translate(0, -1, 0);
	            model.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFF);

			}

		}
		matrixStackIn.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}
	
	@Override
	public boolean shouldRender(UltimaEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
		return true;
	}
	
	@Nullable
	@Override
	public ResourceLocation getTextureLocation(UltimaEntity entity) {
		return TEXTURE;
	}
}
