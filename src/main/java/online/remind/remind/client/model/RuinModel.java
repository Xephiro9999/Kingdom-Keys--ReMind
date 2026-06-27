package online.remind.remind.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import online.remind.remind.KingdomKeysReMind;

public class RuinModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "ruin"), "main");

    private final ModelPart bb_main;

    public RuinModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        /*
         * Cube is now centered around its own pivot.
         * This makes it spin in place instead of rotating weirdly around an edge/corner.
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
        // Not using this for spin, because your renderer may not be calling setupAnim().
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        poseStack.pushPose();

        /*
         * Real-time client spin.
         * This works even if setupAnim() is never called.
         */
        long time = System.currentTimeMillis() % 360000L;

        float ySpin = time * 6F;
        float zSpin = time * 6F;

        // Tilt it so it looks less obviously like a cube.
        poseStack.mulPose(Axis.XP.rotationDegrees(35.0F));

        // Main spin.
        poseStack.mulPose(Axis.YP.rotationDegrees(ySpin));

        // Small extra roll to disguise the cube shape more.
        poseStack.mulPose(Axis.ZP.rotationDegrees(zSpin));

        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);

        poseStack.popPose();
    }
}