package online.remind.remind.client.model.mob;// Made with Blockbench 4.9.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.animations.ModAnimationDefinitions;
import online.remind.remind.entity.spirits.ChirithyEntity;

public class chirithyModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "chirithy"), "main");
	private final ModelPart chirithy;
	private final ModelPart head;

	public chirithyModel(ModelPart root) {
		this.chirithy = root.getChild("Main");
        this.head = chirithy.getChild("head");
    }

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Main = partdefinition.addOrReplaceChild("Main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = Main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(1, 112).addBox(-4.025F, -4.65F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(37, 116).addBox(-4.275F, -4.95F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(37, 116).addBox(2.325F, -4.95F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.025F, -14.35F, 0.0F, 3.1416F, 0.0F, 3.1416F));

		PartDefinition mouth_r1 = head.addOrReplaceChild("mouth_r1", CubeListBuilder.create().texOffs(35, 122).addBox(-3.0F, -2.0F, -1.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.025F, 1.35F, 4.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition body = Main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(103, 93).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		PartDefinition cloak = body.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(97, 108).addBox(-4.0F, -1.0F, -3.2F, 8.0F, 11.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(118, 86).addBox(-0.5F, -0.4F, -3.6F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -3.0F));

		PartDefinition arms = Main.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		PartDefinition LArm = arms.addOrReplaceChild("LArm", CubeListBuilder.create(), PartPose.offset(-3.0F, -12.0F, 0.0F));

		PartDefinition cube_r1 = LArm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 10).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition RArm = arms.addOrReplaceChild("RArm", CubeListBuilder.create(), PartPose.offset(3.0F, -12.0F, 0.0F));

		PartDefinition cube_r2 = RArm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition legs = Main.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		PartDefinition RLeg = legs.addOrReplaceChild("RLeg", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -6.0F, 0.0F));

		PartDefinition LLeg = legs.addOrReplaceChild("LLeg", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.5F, -6.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(ModAnimationDefinitions.CHIRITHY_WALK, limbSwing, limbSwingAmount,2f, 2.5f);
		//this.animate(((ChirithyEntity) entity).idleAnimationState, ModAnimationDefinitions.CHIRITHY_IDLE, ageInTicks, 1f);
		this.animateCast(((ChirithyEntity) entity).attackAnim, ModAnimationDefinitions.CHIRITHY_CAST, ageInTicks, 1f);
	}

	private void animateWalk(AnimationDefinition chirithyWalk, float limbSwing, float limbSwingAmount, float v, float v1) {
	}

	private void animateCast(float attackAnim, AnimationDefinition cast, float ageInTicks, float v) {
	}

	private void animate(AnimationState idleAnimationState, AnimationDefinition idle, float ageInTicks, float v) {

	}


	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks){
		pNetHeadYaw = Mth.clamp(pNetHeadYaw,-30.0F,30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, 180F,200F);

		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		chirithy.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}


	public ModelPart root(){
		return chirithy;
	}

}