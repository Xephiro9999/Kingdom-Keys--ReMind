package online.remind.remind.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import online.remind.remind.KingdomKeysReMind;

public class HolyModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "holy"), "main");

    private final ModelPart bb_main;

    public HolyModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        /*
         * Original cube was offset weirdly:
         * addBox Y: -16 to -10
         * PartPose Y: 14.5
         *
         * That placed the cube around Y -1.5 to 4.5.
         *
         * This version centers the cube around its own pivot,
         * then puts the pivot at the same visual center.
         * That lets it spin in place properly.
         */
        PartDefinition bb_main = partdefinition.addOrReplaceChild(
                "bb_main",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -3.0F, -3.0F, -3.0F,
                                6.0F, 6.0F, 6.0F,
                                new CubeDeformation(0.0F)
                        ),
                PartPose.offset(0.0F, 1.5F, 0.0F)
        );

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Spin is handled directly in renderToBuffer so it works even if setupAnim is not being called.
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        poseStack.pushPose();

        long time = System.currentTimeMillis() % 360000L;

        /*
         * Spin speeds.
         * These are fast enough to hide the cube shape better.
         */
        float xSpin = time * 1.15F;
        float ySpin = time * 1.85F;
        float zSpin = time * 0.75F;

        /*
         * Small pulse so Holy feels alive/glowing instead of static.
         */
        float pulse = 1.0F + 0.08F * (float) Math.sin(time * 0.01F);

        poseStack.scale(pulse, pulse, pulse);

        poseStack.mulPose(Axis.XP.rotationDegrees(xSpin));
        poseStack.mulPose(Axis.YP.rotationDegrees(ySpin));
        poseStack.mulPose(Axis.ZP.rotationDegrees(zSpin));

        /*
         * Fullbright makes it render as if fully lit.
         * This is the main "glow" effect from the model side.
         */
        int fullBright = LightTexture.FULL_BRIGHT;

        /*
         * Warm white/gold tint.
         * ARGB format: 0xAARRGGBB
         */
        int holyGlowColor = 0xFFFFF4B8;

        bb_main.render(poseStack, vertexConsumer, fullBright, packedOverlay, holyGlowColor);

        poseStack.popPose();
    }
}