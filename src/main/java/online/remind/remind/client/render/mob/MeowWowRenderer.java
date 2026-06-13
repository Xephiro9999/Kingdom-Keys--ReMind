package online.remind.remind.client.render.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.model.mob.MeowWowModel;
import online.remind.remind.entity.spirits.MeowWowEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MeowWowRenderer extends GeoEntityRenderer<MeowWowEntity> {

    private static final ResourceLocation SPIRIT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID,
                    "textures/entity/models/mobs/spirit_mw.png"
            );

    private static final ResourceLocation NIGHTMARE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID,
                    "textures/entity/models/mobs/nightmare_mw.png"
            );

    public MeowWowRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MeowWowModel());
        this.shadowRadius = 0.35F;
    }

    @Override
    public ResourceLocation getTextureLocation(MeowWowEntity entity) {
        return entity.getVariant() == MeowWowEntity.VARIANT_ORG
                ? NIGHTMARE_TEXTURE
                : SPIRIT_TEXTURE;
    }

    @Override
    public void render(
            MeowWowEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight
    ) {
        poseStack.pushPose();
        poseStack.scale(2.0F, 2.0F, 2.0F);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }
}