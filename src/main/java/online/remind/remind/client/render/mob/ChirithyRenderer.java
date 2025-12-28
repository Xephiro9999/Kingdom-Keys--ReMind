package online.remind.remind.client.render.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.model.mob.chirithyModel;
import online.remind.remind.entity.spirits.ChirithyEntity;

public class ChirithyRenderer extends MobRenderer<ChirithyEntity, chirithyModel<ChirithyEntity>> {

    public ChirithyRenderer(EntityRendererProvider.Context context){
        super(context, new chirithyModel<>(context.bakeLayer(chirithyModel.LAYER_LOCATION)),0.35F);
    }

    @Override
    public void render(ChirithyEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn){
        matrixStackIn.pushPose();
        {
            /*if (EntityHelper.getState(entityIn) == 1) {
                matrixStackIn.translate(0, -1.5F, 0);
            } else if(EntityHelper.getState(entityIn) == 2){
                matrixStackIn.translate(0, 0.3F, 0);
                if(entityIn.tickCount % 10 == 0) {
                    matrixStackIn.scale(1.2F, 1.2F, 1.2F);
                }
            }*/
            super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        }
        matrixStackIn.popPose();
    }

    private static final ResourceLocation SPIRIT_TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/entity/models/mobs/chirithy-spirit.png");
    private static final ResourceLocation NIGHTMARE_TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/entity/models/mobs/chirithy-nightmare.png");

    @Override
    public ResourceLocation getTextureLocation(ChirithyEntity entity){
        return entity.getVariant() == 1 ? SPIRIT_TEXTURE : NIGHTMARE_TEXTURE;
    }

    @Override
    protected void scale(ChirithyEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1,1,1);
        super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
}
