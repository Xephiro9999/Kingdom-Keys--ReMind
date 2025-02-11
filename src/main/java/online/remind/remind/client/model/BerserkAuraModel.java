package online.remind.remind.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import online.remind.remind.KingdomKeysReMind;

public class BerserkAuraModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "berserk"), "main");
    private final ModelPart Base;

    public BerserkAuraModel(ModelPart root) {
        this.Base = root.getChild("Base");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Base = partdefinition.addOrReplaceChild("Base", CubeListBuilder.create(), PartPose.offset(0.0002F, -0.5769F, 0.3803F));

        PartDefinition right2_r1 = Base.addOrReplaceChild("right2_r1", CubeListBuilder.create().texOffs(21, 0).addBox(-9.5356F, -7.5854F, 12.6266F, 21.0F, 27.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1498F, 2.7623F, -0.4447F, 2.9234F, -0.7854F, -3.1416F));

        PartDefinition back2_r1 = Base.addOrReplaceChild("back2_r1", CubeListBuilder.create().texOffs(21, 0).addBox(-9.85F, -7.5854F, 12.7824F, 21.0F, 27.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1498F, 2.7623F, -0.4447F, -0.2182F, -0.7854F, 0.0F));

        PartDefinition front2_r1 = Base.addOrReplaceChild("front2_r1", CubeListBuilder.create().texOffs(21, 0).addBox(-10.4F, -7.5854F, -12.1229F, 21.0F, 27.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1498F, 2.7623F, -0.4447F, 0.2182F, -0.7854F, 0.0F));

        PartDefinition left2_r1 = Base.addOrReplaceChild("left2_r1", CubeListBuilder.create().texOffs(21, 0).addBox(-9.7356F, -7.5854F, -12.2266F, 21.0F, 27.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1498F, 2.7623F, -0.4447F, -2.9234F, -0.7854F, 3.1416F));

        PartDefinition left_r1 = Base.addOrReplaceChild("left_r1", CubeListBuilder.create().texOffs(21, 0).addBox(-10.0F, -16.0F, 3.2F, 21.0F, 40.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.9998F, 2.0769F, -0.3803F, 0.0F, -1.5708F, 0.0873F));

        PartDefinition right_r1 = Base.addOrReplaceChild("right_r1", CubeListBuilder.create().texOffs(21, 0).addBox(-10.0F, -16.0F, -1.8F, 21.0F, 40.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.8002F, 2.0769F, -0.3803F, 0.0F, -1.5708F, -0.0873F));

        PartDefinition back_r1 = Base.addOrReplaceChild("back_r1", CubeListBuilder.create().texOffs(21, 0).addBox(-10.75F, -16.0F, -2.6F, 21.0F, 40.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0002F, 2.0769F, 9.1197F, -0.0873F, 0.0F, 0.0F));

        PartDefinition front_r1 = Base.addOrReplaceChild("front_r1", CubeListBuilder.create().texOffs(21, 0).addBox(-10.5F, -16.0F, -0.6F, 21.0F, 40.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0002F, 2.0769F, -6.3803F, 0.0873F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        Base.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}

