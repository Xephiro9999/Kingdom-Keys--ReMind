package online.remind.remind.integration.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.model.BerserkAuraModel;
import online.remind.remind.effect.ModMobEffectsRM;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import javax.annotation.Nullable;

public class PatchedBerserkLayerRenderer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends EntityModel<E>> extends PatchedLayer<E, T, M, RenderLayer<E, M>> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID,"textures/entity/models/berserk1.png");
    private final ModelPart box;

    private final BerserkAuraModel<?> berserkAuraModel;

    public PatchedBerserkLayerRenderer(){
        EntityModelSet models = Minecraft.getInstance().getEntityModels();
        this.berserkAuraModel = new BerserkAuraModel<>(models.bakeLayer(BerserkAuraModel.LAYER_LOCATION));
        this.box = models.bakeLayer(ModelLayers.PLAYER_SPIN_ATTACK).getChild("box");

    }

    @Override
    protected void renderLayer(T patch, E entity, @Nullable net.minecraft.client.renderer.entity.layers.RenderLayer<E, M> vanillaLayer, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
        if (ModDataRM.getGlobal(entity) != null) {
            if(entity.hasEffect(ModMobEffectsRM.BERSERK)){
                MobEffectInstance berserk = entity.getEffect(ModMobEffectsRM.BERSERK);
                VertexConsumer vertexConsumer = buffer.getBuffer(EpicFightRenderTypes.getTriangulated(EpicFightRenderTypes.entityCutoutNoCull(TEXTURE)));                for (int i = 1; i <= berserk.getAmplifier() + 1; ++i) {
                    poseStack.pushPose();
                    float f = partialTicks * 20;
                    if (i % 2 == 0)
                        f *= -1;
                    poseStack.mulPose(Axis.YP.rotationDegrees(f));
                    float scale = 1;
                    switch (berserk.getAmplifier()) {
                        case 0:
                            if (entity instanceof Player) {
                                scale = 0.75F * i;
                                poseStack.scale(scale, scale * 1.0F, scale);
                                poseStack.translate(0.0D, (double) (-0.4F + 0.8F * (float) i), 0.0D);
                            } else {
                                scale = 0.35F * i;
                                poseStack.scale(scale, scale, scale);

                            }
                            break;
                        case 1:
                            if (entity instanceof Player) {
                                scale = 0.85F * i;
                                poseStack.scale(scale, scale * 1.25F, scale);
                                poseStack.translate(0.0D, (double) (-0.8F + 0.8F * (float) i) - 1.5, 0.0D);

                            } else {
                                scale = 0.45F * i;
                                poseStack.scale(scale, scale, scale);
                            }

                            break;
                        case 2:
                            if (entity instanceof Player) {
                                scale = 0.7F * i;
                                poseStack.scale(scale, scale * 1.5F, scale);
                                poseStack.translate(0.0D, (double) (-1.2F + 0.6F * (float) i) - 1.5, 0.0D);
                            } else {
                                scale = 0.55F * i;
                                poseStack.scale(scale, scale * 0.6F, scale);
                            }
                            break;
                        //this.box.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFF);

                    }
                    poseStack.popPose();
                }
            }
        }
    }
}
