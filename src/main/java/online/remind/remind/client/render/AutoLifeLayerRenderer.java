package online.remind.remind.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.util.IDisabledAnimations;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.client.model.AutoLifeModel;
import online.remind.remind.effect.ModMobEffectsRM;

@OnlyIn(Dist.CLIENT)
public class AutoLifeLayerRenderer<T extends LivingEntity> extends RenderLayer<T, PlayerModel<T>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(KingdomKeysReMind.MODID, "textures/entity/models/auto-life.png");
    public static final String BOX = "box";
    private final ModelPart box;

    public AutoLifeLayerRenderer(RenderLayerParent<T, PlayerModel<T>> p_174540_, EntityModelSet p_174541_) {
        super(p_174540_);
        ModelPart modelpart = p_174541_.bakeLayer(AutoLifeModel.LAYER_LOCATION);
        this.box = modelpart.getChild("Base");
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {        if (entitylivingbaseIn instanceof AbstractClientPlayer) {
            LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer = (LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer((AbstractClientPlayer) entitylivingbaseIn);
            if (!((IDisabledAnimations) renderer).isDisabled()) {
                renderEntity(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        } else {
            renderEntity(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }

    public void renderEntity(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (ModCapabilitiesRM.getGlobal(entitylivingbaseIn) != null) {
            IGlobalCapabilitiesRM globalData = ModCapabilitiesRM.getGlobal(entitylivingbaseIn);
            if (entitylivingbaseIn.hasEffect(ModMobEffectsRM.AUTO_LIFE.get())) {
                VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));

                matrixStackIn.pushPose();
	                if (entitylivingbaseIn instanceof Player) {
	                    matrixStackIn.scale(1, 1, 1);
	                    matrixStackIn.translate(0.0D, 0.0D, 0.0D);
	                }

				this.box.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY);
                matrixStackIn.popPose();
            }
        }
    }
}
