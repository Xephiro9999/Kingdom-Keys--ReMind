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


public class UltimaModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "ultima"), "main");
	private final ModelPart body;

	public UltimaModel(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0F, 0.0F, 1.5708F, 0.0F, 1.5708F));

		PartDefinition hexadecagon = body.addOrReplaceChild("hexadecagon", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition hexadecagon_r1 = hexadecagon.addOrReplaceChild("hexadecagon_r1", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r2 = hexadecagon.addOrReplaceChild("hexadecagon_r2", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r3 = hexadecagon.addOrReplaceChild("hexadecagon_r3", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r4 = hexadecagon.addOrReplaceChild("hexadecagon_r4", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon2 = body.addOrReplaceChild("hexadecagon2", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0611F, 1.5708F));

		PartDefinition hexadecagon_r5 = hexadecagon2.addOrReplaceChild("hexadecagon_r5", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r6 = hexadecagon2.addOrReplaceChild("hexadecagon_r6", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r7 = hexadecagon2.addOrReplaceChild("hexadecagon_r7", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r8 = hexadecagon2.addOrReplaceChild("hexadecagon_r8", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon3 = body.addOrReplaceChild("hexadecagon3", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1239F, 1.5708F));

		PartDefinition hexadecagon_r9 = hexadecagon3.addOrReplaceChild("hexadecagon_r9", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r10 = hexadecagon3.addOrReplaceChild("hexadecagon_r10", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r11 = hexadecagon3.addOrReplaceChild("hexadecagon_r11", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r12 = hexadecagon3.addOrReplaceChild("hexadecagon_r12", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon4 = body.addOrReplaceChild("hexadecagon4", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1868F, 1.5708F));

		PartDefinition hexadecagon_r13 = hexadecagon4.addOrReplaceChild("hexadecagon_r13", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r14 = hexadecagon4.addOrReplaceChild("hexadecagon_r14", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r15 = hexadecagon4.addOrReplaceChild("hexadecagon_r15", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r16 = hexadecagon4.addOrReplaceChild("hexadecagon_r16", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon5 = body.addOrReplaceChild("hexadecagon5", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2496F, 1.5708F));

		PartDefinition hexadecagon_r17 = hexadecagon5.addOrReplaceChild("hexadecagon_r17", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r18 = hexadecagon5.addOrReplaceChild("hexadecagon_r18", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r19 = hexadecagon5.addOrReplaceChild("hexadecagon_r19", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r20 = hexadecagon5.addOrReplaceChild("hexadecagon_r20", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon6 = body.addOrReplaceChild("hexadecagon6", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3124F, 1.5708F));

		PartDefinition hexadecagon_r21 = hexadecagon6.addOrReplaceChild("hexadecagon_r21", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r22 = hexadecagon6.addOrReplaceChild("hexadecagon_r22", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r23 = hexadecagon6.addOrReplaceChild("hexadecagon_r23", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r24 = hexadecagon6.addOrReplaceChild("hexadecagon_r24", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon7 = body.addOrReplaceChild("hexadecagon7", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3752F, 1.5708F));

		PartDefinition hexadecagon_r25 = hexadecagon7.addOrReplaceChild("hexadecagon_r25", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r26 = hexadecagon7.addOrReplaceChild("hexadecagon_r26", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r27 = hexadecagon7.addOrReplaceChild("hexadecagon_r27", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r28 = hexadecagon7.addOrReplaceChild("hexadecagon_r28", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon8 = body.addOrReplaceChild("hexadecagon8", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.4381F, 1.5708F));

		PartDefinition hexadecagon_r29 = hexadecagon8.addOrReplaceChild("hexadecagon_r29", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r30 = hexadecagon8.addOrReplaceChild("hexadecagon_r30", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r31 = hexadecagon8.addOrReplaceChild("hexadecagon_r31", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r32 = hexadecagon8.addOrReplaceChild("hexadecagon_r32", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon9 = body.addOrReplaceChild("hexadecagon9", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.5009F, 1.5708F));

		PartDefinition hexadecagon_r33 = hexadecagon9.addOrReplaceChild("hexadecagon_r33", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r34 = hexadecagon9.addOrReplaceChild("hexadecagon_r34", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r35 = hexadecagon9.addOrReplaceChild("hexadecagon_r35", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r36 = hexadecagon9.addOrReplaceChild("hexadecagon_r36", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon10 = body.addOrReplaceChild("hexadecagon10", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.5637F, 1.5708F));

		PartDefinition hexadecagon_r37 = hexadecagon10.addOrReplaceChild("hexadecagon_r37", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r38 = hexadecagon10.addOrReplaceChild("hexadecagon_r38", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r39 = hexadecagon10.addOrReplaceChild("hexadecagon_r39", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r40 = hexadecagon10.addOrReplaceChild("hexadecagon_r40", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon11 = body.addOrReplaceChild("hexadecagon11", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.6266F, 1.5708F));

		PartDefinition hexadecagon_r41 = hexadecagon11.addOrReplaceChild("hexadecagon_r41", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r42 = hexadecagon11.addOrReplaceChild("hexadecagon_r42", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r43 = hexadecagon11.addOrReplaceChild("hexadecagon_r43", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r44 = hexadecagon11.addOrReplaceChild("hexadecagon_r44", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon12 = body.addOrReplaceChild("hexadecagon12", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.6894F, 1.5708F));

		PartDefinition hexadecagon_r45 = hexadecagon12.addOrReplaceChild("hexadecagon_r45", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r46 = hexadecagon12.addOrReplaceChild("hexadecagon_r46", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r47 = hexadecagon12.addOrReplaceChild("hexadecagon_r47", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r48 = hexadecagon12.addOrReplaceChild("hexadecagon_r48", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon13 = body.addOrReplaceChild("hexadecagon13", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7522F, 1.5708F));

		PartDefinition hexadecagon_r49 = hexadecagon13.addOrReplaceChild("hexadecagon_r49", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r50 = hexadecagon13.addOrReplaceChild("hexadecagon_r50", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r51 = hexadecagon13.addOrReplaceChild("hexadecagon_r51", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r52 = hexadecagon13.addOrReplaceChild("hexadecagon_r52", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon14 = body.addOrReplaceChild("hexadecagon14", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.8151F, 1.5708F));

		PartDefinition hexadecagon_r53 = hexadecagon14.addOrReplaceChild("hexadecagon_r53", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r54 = hexadecagon14.addOrReplaceChild("hexadecagon_r54", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r55 = hexadecagon14.addOrReplaceChild("hexadecagon_r55", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r56 = hexadecagon14.addOrReplaceChild("hexadecagon_r56", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition Sphere2 = body.addOrReplaceChild("Sphere2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.8727F, 0.0F, 0.0F));

		PartDefinition hexadecagon15 = Sphere2.addOrReplaceChild("hexadecagon15", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition hexadecagon_r57 = hexadecagon15.addOrReplaceChild("hexadecagon_r57", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r58 = hexadecagon15.addOrReplaceChild("hexadecagon_r58", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r59 = hexadecagon15.addOrReplaceChild("hexadecagon_r59", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r60 = hexadecagon15.addOrReplaceChild("hexadecagon_r60", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon16 = Sphere2.addOrReplaceChild("hexadecagon16", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0611F, 1.5708F));

		PartDefinition hexadecagon_r61 = hexadecagon16.addOrReplaceChild("hexadecagon_r61", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r62 = hexadecagon16.addOrReplaceChild("hexadecagon_r62", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r63 = hexadecagon16.addOrReplaceChild("hexadecagon_r63", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r64 = hexadecagon16.addOrReplaceChild("hexadecagon_r64", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon17 = Sphere2.addOrReplaceChild("hexadecagon17", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1239F, 1.5708F));

		PartDefinition hexadecagon_r65 = hexadecagon17.addOrReplaceChild("hexadecagon_r65", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r66 = hexadecagon17.addOrReplaceChild("hexadecagon_r66", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r67 = hexadecagon17.addOrReplaceChild("hexadecagon_r67", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r68 = hexadecagon17.addOrReplaceChild("hexadecagon_r68", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon18 = Sphere2.addOrReplaceChild("hexadecagon18", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1868F, 1.5708F));

		PartDefinition hexadecagon_r69 = hexadecagon18.addOrReplaceChild("hexadecagon_r69", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r70 = hexadecagon18.addOrReplaceChild("hexadecagon_r70", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r71 = hexadecagon18.addOrReplaceChild("hexadecagon_r71", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r72 = hexadecagon18.addOrReplaceChild("hexadecagon_r72", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon19 = Sphere2.addOrReplaceChild("hexadecagon19", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2496F, 1.5708F));

		PartDefinition hexadecagon_r73 = hexadecagon19.addOrReplaceChild("hexadecagon_r73", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r74 = hexadecagon19.addOrReplaceChild("hexadecagon_r74", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r75 = hexadecagon19.addOrReplaceChild("hexadecagon_r75", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r76 = hexadecagon19.addOrReplaceChild("hexadecagon_r76", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon20 = Sphere2.addOrReplaceChild("hexadecagon20", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3124F, 1.5708F));

		PartDefinition hexadecagon_r77 = hexadecagon20.addOrReplaceChild("hexadecagon_r77", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r78 = hexadecagon20.addOrReplaceChild("hexadecagon_r78", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r79 = hexadecagon20.addOrReplaceChild("hexadecagon_r79", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r80 = hexadecagon20.addOrReplaceChild("hexadecagon_r80", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon21 = Sphere2.addOrReplaceChild("hexadecagon21", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3752F, 1.5708F));

		PartDefinition hexadecagon_r81 = hexadecagon21.addOrReplaceChild("hexadecagon_r81", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r82 = hexadecagon21.addOrReplaceChild("hexadecagon_r82", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r83 = hexadecagon21.addOrReplaceChild("hexadecagon_r83", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r84 = hexadecagon21.addOrReplaceChild("hexadecagon_r84", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon22 = Sphere2.addOrReplaceChild("hexadecagon22", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.4381F, 1.5708F));

		PartDefinition hexadecagon_r85 = hexadecagon22.addOrReplaceChild("hexadecagon_r85", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r86 = hexadecagon22.addOrReplaceChild("hexadecagon_r86", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r87 = hexadecagon22.addOrReplaceChild("hexadecagon_r87", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r88 = hexadecagon22.addOrReplaceChild("hexadecagon_r88", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon23 = Sphere2.addOrReplaceChild("hexadecagon23", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.5009F, 1.5708F));

		PartDefinition hexadecagon_r89 = hexadecagon23.addOrReplaceChild("hexadecagon_r89", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r90 = hexadecagon23.addOrReplaceChild("hexadecagon_r90", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r91 = hexadecagon23.addOrReplaceChild("hexadecagon_r91", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r92 = hexadecagon23.addOrReplaceChild("hexadecagon_r92", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon24 = Sphere2.addOrReplaceChild("hexadecagon24", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.5637F, 1.5708F));

		PartDefinition hexadecagon_r93 = hexadecagon24.addOrReplaceChild("hexadecagon_r93", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r94 = hexadecagon24.addOrReplaceChild("hexadecagon_r94", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r95 = hexadecagon24.addOrReplaceChild("hexadecagon_r95", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r96 = hexadecagon24.addOrReplaceChild("hexadecagon_r96", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon25 = Sphere2.addOrReplaceChild("hexadecagon25", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.6266F, 1.5708F));

		PartDefinition hexadecagon_r97 = hexadecagon25.addOrReplaceChild("hexadecagon_r97", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r98 = hexadecagon25.addOrReplaceChild("hexadecagon_r98", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r99 = hexadecagon25.addOrReplaceChild("hexadecagon_r99", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r100 = hexadecagon25.addOrReplaceChild("hexadecagon_r100", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon26 = Sphere2.addOrReplaceChild("hexadecagon26", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.6894F, 1.5708F));

		PartDefinition hexadecagon_r101 = hexadecagon26.addOrReplaceChild("hexadecagon_r101", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r102 = hexadecagon26.addOrReplaceChild("hexadecagon_r102", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r103 = hexadecagon26.addOrReplaceChild("hexadecagon_r103", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r104 = hexadecagon26.addOrReplaceChild("hexadecagon_r104", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon27 = Sphere2.addOrReplaceChild("hexadecagon27", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7522F, 1.5708F));

		PartDefinition hexadecagon_r105 = hexadecagon27.addOrReplaceChild("hexadecagon_r105", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r106 = hexadecagon27.addOrReplaceChild("hexadecagon_r106", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r107 = hexadecagon27.addOrReplaceChild("hexadecagon_r107", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r108 = hexadecagon27.addOrReplaceChild("hexadecagon_r108", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon28 = Sphere2.addOrReplaceChild("hexadecagon28", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.8151F, 1.5708F));

		PartDefinition hexadecagon_r109 = hexadecagon28.addOrReplaceChild("hexadecagon_r109", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r110 = hexadecagon28.addOrReplaceChild("hexadecagon_r110", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r111 = hexadecagon28.addOrReplaceChild("hexadecagon_r111", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r112 = hexadecagon28.addOrReplaceChild("hexadecagon_r112", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition Sphere3 = body.addOrReplaceChild("Sphere3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.7453F, 0.0F, 0.0F));

		PartDefinition hexadecagon29 = Sphere3.addOrReplaceChild("hexadecagon29", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition hexadecagon_r113 = hexadecagon29.addOrReplaceChild("hexadecagon_r113", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r114 = hexadecagon29.addOrReplaceChild("hexadecagon_r114", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r115 = hexadecagon29.addOrReplaceChild("hexadecagon_r115", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r116 = hexadecagon29.addOrReplaceChild("hexadecagon_r116", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon30 = Sphere3.addOrReplaceChild("hexadecagon30", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0611F, 1.5708F));

		PartDefinition hexadecagon_r117 = hexadecagon30.addOrReplaceChild("hexadecagon_r117", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r118 = hexadecagon30.addOrReplaceChild("hexadecagon_r118", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r119 = hexadecagon30.addOrReplaceChild("hexadecagon_r119", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r120 = hexadecagon30.addOrReplaceChild("hexadecagon_r120", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon31 = Sphere3.addOrReplaceChild("hexadecagon31", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1239F, 1.5708F));

		PartDefinition hexadecagon_r121 = hexadecagon31.addOrReplaceChild("hexadecagon_r121", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r122 = hexadecagon31.addOrReplaceChild("hexadecagon_r122", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r123 = hexadecagon31.addOrReplaceChild("hexadecagon_r123", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r124 = hexadecagon31.addOrReplaceChild("hexadecagon_r124", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon32 = Sphere3.addOrReplaceChild("hexadecagon32", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1868F, 1.5708F));

		PartDefinition hexadecagon_r125 = hexadecagon32.addOrReplaceChild("hexadecagon_r125", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r126 = hexadecagon32.addOrReplaceChild("hexadecagon_r126", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r127 = hexadecagon32.addOrReplaceChild("hexadecagon_r127", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r128 = hexadecagon32.addOrReplaceChild("hexadecagon_r128", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon33 = Sphere3.addOrReplaceChild("hexadecagon33", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2496F, 1.5708F));

		PartDefinition hexadecagon_r129 = hexadecagon33.addOrReplaceChild("hexadecagon_r129", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r130 = hexadecagon33.addOrReplaceChild("hexadecagon_r130", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r131 = hexadecagon33.addOrReplaceChild("hexadecagon_r131", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r132 = hexadecagon33.addOrReplaceChild("hexadecagon_r132", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon34 = Sphere3.addOrReplaceChild("hexadecagon34", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3124F, 1.5708F));

		PartDefinition hexadecagon_r133 = hexadecagon34.addOrReplaceChild("hexadecagon_r133", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r134 = hexadecagon34.addOrReplaceChild("hexadecagon_r134", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r135 = hexadecagon34.addOrReplaceChild("hexadecagon_r135", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r136 = hexadecagon34.addOrReplaceChild("hexadecagon_r136", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon35 = Sphere3.addOrReplaceChild("hexadecagon35", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3752F, 1.5708F));

		PartDefinition hexadecagon_r137 = hexadecagon35.addOrReplaceChild("hexadecagon_r137", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r138 = hexadecagon35.addOrReplaceChild("hexadecagon_r138", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r139 = hexadecagon35.addOrReplaceChild("hexadecagon_r139", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r140 = hexadecagon35.addOrReplaceChild("hexadecagon_r140", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon36 = Sphere3.addOrReplaceChild("hexadecagon36", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.4381F, 1.5708F));

		PartDefinition hexadecagon_r141 = hexadecagon36.addOrReplaceChild("hexadecagon_r141", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r142 = hexadecagon36.addOrReplaceChild("hexadecagon_r142", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r143 = hexadecagon36.addOrReplaceChild("hexadecagon_r143", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r144 = hexadecagon36.addOrReplaceChild("hexadecagon_r144", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon37 = Sphere3.addOrReplaceChild("hexadecagon37", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.5009F, 1.5708F));

		PartDefinition hexadecagon_r145 = hexadecagon37.addOrReplaceChild("hexadecagon_r145", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r146 = hexadecagon37.addOrReplaceChild("hexadecagon_r146", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r147 = hexadecagon37.addOrReplaceChild("hexadecagon_r147", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r148 = hexadecagon37.addOrReplaceChild("hexadecagon_r148", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon38 = Sphere3.addOrReplaceChild("hexadecagon38", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.5637F, 1.5708F));

		PartDefinition hexadecagon_r149 = hexadecagon38.addOrReplaceChild("hexadecagon_r149", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r150 = hexadecagon38.addOrReplaceChild("hexadecagon_r150", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r151 = hexadecagon38.addOrReplaceChild("hexadecagon_r151", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r152 = hexadecagon38.addOrReplaceChild("hexadecagon_r152", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon39 = Sphere3.addOrReplaceChild("hexadecagon39", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.6266F, 1.5708F));

		PartDefinition hexadecagon_r153 = hexadecagon39.addOrReplaceChild("hexadecagon_r153", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r154 = hexadecagon39.addOrReplaceChild("hexadecagon_r154", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r155 = hexadecagon39.addOrReplaceChild("hexadecagon_r155", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r156 = hexadecagon39.addOrReplaceChild("hexadecagon_r156", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon40 = Sphere3.addOrReplaceChild("hexadecagon40", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.6894F, 1.5708F));

		PartDefinition hexadecagon_r157 = hexadecagon40.addOrReplaceChild("hexadecagon_r157", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r158 = hexadecagon40.addOrReplaceChild("hexadecagon_r158", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r159 = hexadecagon40.addOrReplaceChild("hexadecagon_r159", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r160 = hexadecagon40.addOrReplaceChild("hexadecagon_r160", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon41 = Sphere3.addOrReplaceChild("hexadecagon41", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7522F, 1.5708F));

		PartDefinition hexadecagon_r161 = hexadecagon41.addOrReplaceChild("hexadecagon_r161", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r162 = hexadecagon41.addOrReplaceChild("hexadecagon_r162", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r163 = hexadecagon41.addOrReplaceChild("hexadecagon_r163", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r164 = hexadecagon41.addOrReplaceChild("hexadecagon_r164", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon42 = Sphere3.addOrReplaceChild("hexadecagon42", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.8151F, 1.5708F));

		PartDefinition hexadecagon_r165 = hexadecagon42.addOrReplaceChild("hexadecagon_r165", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r166 = hexadecagon42.addOrReplaceChild("hexadecagon_r166", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r167 = hexadecagon42.addOrReplaceChild("hexadecagon_r167", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r168 = hexadecagon42.addOrReplaceChild("hexadecagon_r168", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition Sphere4 = body.addOrReplaceChild("Sphere4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.2689F, 0.0F, 0.0F));

		PartDefinition hexadecagon43 = Sphere4.addOrReplaceChild("hexadecagon43", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition hexadecagon_r169 = hexadecagon43.addOrReplaceChild("hexadecagon_r169", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r170 = hexadecagon43.addOrReplaceChild("hexadecagon_r170", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r171 = hexadecagon43.addOrReplaceChild("hexadecagon_r171", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r172 = hexadecagon43.addOrReplaceChild("hexadecagon_r172", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon44 = Sphere4.addOrReplaceChild("hexadecagon44", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0611F, 1.5708F));

		PartDefinition hexadecagon_r173 = hexadecagon44.addOrReplaceChild("hexadecagon_r173", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r174 = hexadecagon44.addOrReplaceChild("hexadecagon_r174", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r175 = hexadecagon44.addOrReplaceChild("hexadecagon_r175", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r176 = hexadecagon44.addOrReplaceChild("hexadecagon_r176", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon45 = Sphere4.addOrReplaceChild("hexadecagon45", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1239F, 1.5708F));

		PartDefinition hexadecagon_r177 = hexadecagon45.addOrReplaceChild("hexadecagon_r177", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r178 = hexadecagon45.addOrReplaceChild("hexadecagon_r178", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r179 = hexadecagon45.addOrReplaceChild("hexadecagon_r179", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r180 = hexadecagon45.addOrReplaceChild("hexadecagon_r180", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon46 = Sphere4.addOrReplaceChild("hexadecagon46", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1868F, 1.5708F));

		PartDefinition hexadecagon_r181 = hexadecagon46.addOrReplaceChild("hexadecagon_r181", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r182 = hexadecagon46.addOrReplaceChild("hexadecagon_r182", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r183 = hexadecagon46.addOrReplaceChild("hexadecagon_r183", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r184 = hexadecagon46.addOrReplaceChild("hexadecagon_r184", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon47 = Sphere4.addOrReplaceChild("hexadecagon47", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2496F, 1.5708F));

		PartDefinition hexadecagon_r185 = hexadecagon47.addOrReplaceChild("hexadecagon_r185", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r186 = hexadecagon47.addOrReplaceChild("hexadecagon_r186", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r187 = hexadecagon47.addOrReplaceChild("hexadecagon_r187", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r188 = hexadecagon47.addOrReplaceChild("hexadecagon_r188", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon48 = Sphere4.addOrReplaceChild("hexadecagon48", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3124F, 1.5708F));

		PartDefinition hexadecagon_r189 = hexadecagon48.addOrReplaceChild("hexadecagon_r189", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r190 = hexadecagon48.addOrReplaceChild("hexadecagon_r190", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r191 = hexadecagon48.addOrReplaceChild("hexadecagon_r191", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r192 = hexadecagon48.addOrReplaceChild("hexadecagon_r192", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon49 = Sphere4.addOrReplaceChild("hexadecagon49", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3752F, 1.5708F));

		PartDefinition hexadecagon_r193 = hexadecagon49.addOrReplaceChild("hexadecagon_r193", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r194 = hexadecagon49.addOrReplaceChild("hexadecagon_r194", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r195 = hexadecagon49.addOrReplaceChild("hexadecagon_r195", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r196 = hexadecagon49.addOrReplaceChild("hexadecagon_r196", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon50 = Sphere4.addOrReplaceChild("hexadecagon50", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.4381F, 1.5708F));

		PartDefinition hexadecagon_r197 = hexadecagon50.addOrReplaceChild("hexadecagon_r197", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r198 = hexadecagon50.addOrReplaceChild("hexadecagon_r198", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r199 = hexadecagon50.addOrReplaceChild("hexadecagon_r199", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r200 = hexadecagon50.addOrReplaceChild("hexadecagon_r200", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon51 = Sphere4.addOrReplaceChild("hexadecagon51", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.5009F, 1.5708F));

		PartDefinition hexadecagon_r201 = hexadecagon51.addOrReplaceChild("hexadecagon_r201", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r202 = hexadecagon51.addOrReplaceChild("hexadecagon_r202", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r203 = hexadecagon51.addOrReplaceChild("hexadecagon_r203", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r204 = hexadecagon51.addOrReplaceChild("hexadecagon_r204", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon52 = Sphere4.addOrReplaceChild("hexadecagon52", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.5637F, 1.5708F));

		PartDefinition hexadecagon_r205 = hexadecagon52.addOrReplaceChild("hexadecagon_r205", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r206 = hexadecagon52.addOrReplaceChild("hexadecagon_r206", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r207 = hexadecagon52.addOrReplaceChild("hexadecagon_r207", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r208 = hexadecagon52.addOrReplaceChild("hexadecagon_r208", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon53 = Sphere4.addOrReplaceChild("hexadecagon53", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.6266F, 1.5708F));

		PartDefinition hexadecagon_r209 = hexadecagon53.addOrReplaceChild("hexadecagon_r209", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r210 = hexadecagon53.addOrReplaceChild("hexadecagon_r210", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r211 = hexadecagon53.addOrReplaceChild("hexadecagon_r211", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r212 = hexadecagon53.addOrReplaceChild("hexadecagon_r212", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon54 = Sphere4.addOrReplaceChild("hexadecagon54", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.6894F, 1.5708F));

		PartDefinition hexadecagon_r213 = hexadecagon54.addOrReplaceChild("hexadecagon_r213", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r214 = hexadecagon54.addOrReplaceChild("hexadecagon_r214", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r215 = hexadecagon54.addOrReplaceChild("hexadecagon_r215", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r216 = hexadecagon54.addOrReplaceChild("hexadecagon_r216", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon55 = Sphere4.addOrReplaceChild("hexadecagon55", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7522F, 1.5708F));

		PartDefinition hexadecagon_r217 = hexadecagon55.addOrReplaceChild("hexadecagon_r217", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r218 = hexadecagon55.addOrReplaceChild("hexadecagon_r218", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r219 = hexadecagon55.addOrReplaceChild("hexadecagon_r219", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r220 = hexadecagon55.addOrReplaceChild("hexadecagon_r220", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon56 = Sphere4.addOrReplaceChild("hexadecagon56", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.8151F, 1.5708F));

		PartDefinition hexadecagon_r221 = hexadecagon56.addOrReplaceChild("hexadecagon_r221", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r222 = hexadecagon56.addOrReplaceChild("hexadecagon_r222", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -16.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, 15.0F, -3.1826F, 1.0F, 1.0F, 6.3652F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r223 = hexadecagon56.addOrReplaceChild("hexadecagon_r223", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r224 = hexadecagon56.addOrReplaceChild("hexadecagon_r224", CubeListBuilder.create().texOffs(3, 25).addBox(-0.5F, -3.1826F, 15.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(3, 25).addBox(-0.5F, -3.1826F, -16.0F, 1.0F, 6.3652F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}