package online.remind.remind.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.util.IDisabledAnimations;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.model.ConfuseModel;
import online.remind.remind.effect.ModMobEffectsRM;

@OnlyIn(Dist.CLIENT)
public class ConfuseLayerRenderer<T extends LivingEntity>
        extends RenderLayer<T, EntityModel<T>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID,
                    "textures/entity/models/confuse.png"
            );

    private final ConfuseModel<T> model;

    public ConfuseLayerRenderer(RenderLayerParent<T, EntityModel<T>> parent, EntityModelSet models) {
        super(parent);
        this.model = new ConfuseModel<>(models.bakeLayer(ConfuseModel.LAYER_LOCATION));

    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (!entity.hasEffect(ModMobEffectsRM.CONFUSE)) return;

        poseStack.pushPose();

        double height = entity.getBbHeight();
        float eye = entity.getEyeHeight();

        double yOffset = Math.max(height, eye) - 2D;

        // Move above head
        poseStack.translate(-0.18D, yOffset, 0.0D);


        // Optional gentle spin
        //poseStack.mulPose(Axis.YP.rotationDegrees(ageInTicks * 1.5f));

        // Scale
        poseStack.scale(0.75F, 0.75F, 0.75F);

        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));
        model.renderToBuffer(
                poseStack,
                vc,
                packedLight,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();
    }
}
