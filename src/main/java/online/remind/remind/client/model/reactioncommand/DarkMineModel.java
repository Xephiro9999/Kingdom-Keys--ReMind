package online.remind.remind.client.model.reactioncommand;

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

public class DarkMineModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "darkmine"), "main");
    private final ModelPart Main;

    public DarkMineModel(ModelPart root) {
        this.Main = root.getChild("Main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Main = partdefinition.addOrReplaceChild("Main", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, 3.4143F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(-1.5F, 6.4143F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, 5.4143F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(16, -2).addBox(-3.5F, 4.0143F, -3.5F, 7.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, 2.4143F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(-1.5F, 1.4143F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(4, 0).addBox(-0.5F, -9.3857F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(-1.5F, -8.3857F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, -7.3857F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(16, -2).addBox(-3.5F, -5.9857F, -3.5F, 7.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, -4.3857F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(-1.5F, -3.3857F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(-8, 24).addBox(-4.1F, -0.0857F, -4.1F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.5F, 0.0F));

        PartDefinition center_r1 = Main.addOrReplaceChild("center_r1", CubeListBuilder.create().texOffs(28, 30).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0857F, 0.0F, -1.4885F, 0.6373F, 2.1796F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        Main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
