package online.remind.remind.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.remind.remind.KingdomKeysReMind;

@OnlyIn(Dist.CLIENT)
public class OutlineLayerRenderer<T extends LivingEntity> extends RenderLayer<T, PlayerModel<T>> {
    public static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/entity/models/outline.png");

    public OutlineLayerRenderer(RenderLayerParent<T, PlayerModel<T>> p_174540_, EntityModelSet p_174541_) {
        super(p_174540_);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        {
            RenderType OUTLINE_TYPE = RenderType.create(
                    "outline_type",
                    DefaultVertexFormat.NEW_ENTITY,
                    VertexFormat.Mode.QUADS,
                    256,
                    false,
                    true,
                    RenderType.CompositeState.builder()
                            .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                            .setTextureState(new RenderStateShard.TextureStateShard(
                                    this.getTextureLocation(entitylivingbaseIn),
                                    false,
                                    false
                            ))
                            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                            .setCullState(RenderStateShard.CULL) // 🔥 LA CLAVE
                            .setLightmapState(RenderStateShard.LIGHTMAP)
                            .setOverlayState(RenderStateShard.OVERLAY)
                            .createCompositeState(false)
            );
            poseStack.scale(1.05f, 1.05f, 1.05f);
            VertexConsumer vertex = buffer.getBuffer(OUTLINE_TYPE);
            this.getParentModel().renderToBuffer(poseStack, vertex, 15728640, OverlayTexture.NO_OVERLAY);

        }
        poseStack.popPose();
    }
}
