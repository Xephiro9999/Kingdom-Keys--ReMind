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

public class AutoLifeModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "autolife"), "main");
	private final ModelPart Base;

	public AutoLifeModel(ModelPart root) {
		this.Base = root.getChild("Base");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Base = partdefinition.addOrReplaceChild("Base", CubeListBuilder.create().texOffs(1, 1).addBox(-2.0979F, -14.0F, -4.7769F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(1, 1).addBox(-1.8512F, -14.0F, 3.8746F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition side_r1 = Base.addOrReplaceChild("side_r1", CubeListBuilder.create().texOffs(1, 1).addBox(-2.0F, -14.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -0.6F, -3.1416F, -0.829F, 3.1416F));

		PartDefinition back_r1 = Base.addOrReplaceChild("back_r1", CubeListBuilder.create().texOffs(1, 1).addBox(-2.0F, -14.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6234F, 0.0F, 0.5743F, 0.0F, -0.7418F, 0.0F));

		PartDefinition front_r1 = Base.addOrReplaceChild("front_r1", CubeListBuilder.create().texOffs(1, 1).addBox(-2.0F, -14.0F, -4.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6743F, 0.0F, -0.4766F, 0.0F, -0.7418F, 0.0F));

		PartDefinition side_r2 = Base.addOrReplaceChild("side_r2", CubeListBuilder.create().texOffs(1, 1).addBox(-2.0F, -14.0F, -4.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5509F, 0.0F, 0.6976F, -3.1416F, -0.829F, 3.1416F));

		PartDefinition side_r3 = Base.addOrReplaceChild("side_r3", CubeListBuilder.create().texOffs(1, 1).addBox(-2.0F, -14.0F, -4.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8512F, 0.0F, -0.0746F, 0.0F, -1.5708F, 0.0F));

		PartDefinition side_r4 = Base.addOrReplaceChild("side_r4", CubeListBuilder.create().texOffs(1, 1).addBox(-2.0F, -14.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8003F, 0.0F, 0.1722F, 0.0F, -1.5708F, 0.0F));

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