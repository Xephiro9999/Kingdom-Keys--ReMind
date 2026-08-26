package online.remind.remind.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.magic.ZettaflareBeamEntity;

public class ZettaflareBeamRenderer
        extends EntityRenderer<ZettaflareBeamEntity> {

    private static final ResourceLocation DUMMY_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID,
                    "textures/entity/magic/zettaflare_beam.png"
            );

    public ZettaflareBeamRenderer(
            EntityRendererProvider.Context context
    ) {
        super(context);

        this.shadowRadius = 0.0F;
    }


    // ============================================================
    // DO NOT CULL THE BEAM
    // ============================================================

    @Override
    public boolean shouldRender(
            ZettaflareBeamEntity entity,
            Frustum frustum,
            double cameraX,
            double cameraY,
            double cameraZ
    ) {
        /*
         * The actual entity is tiny, while the visual beam may be
         * 48+ blocks long.
         *
         * Vanilla entity culling would otherwise decide:
         *
         * "controller isn't visible -> don't render"
         *
         * even though the beam itself IS visible.
         */
        return true;
    }


    // ============================================================
    // RENDER
    // ============================================================

    @Override
    public void render(
            ZettaflareBeamEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {

        Vec3 direction =
                entity.getBeamDirection(partialTick);

        if (direction.lengthSqr() <= 0.0001D) {
            return;
        }

        direction = direction.normalize();

        float length =
                (float) entity.getBeamLength();


        // ========================================================
        // Rotate local +Z toward the caster's look direction
        // ========================================================

        float yaw =
                (float) Math.toDegrees(
                        Math.atan2(
                                direction.x,
                                direction.z
                        )
                );

        float pitch =
                (float) -Math.toDegrees(
                        Math.asin(direction.y)
                );


        poseStack.pushPose();

        poseStack.mulPose(
                Axis.YP.rotationDegrees(yaw)
        );

        poseStack.mulPose(
                Axis.XP.rotationDegrees(pitch)
        );


        /*
         * RenderType.lightning() is perfect for the initial test:
         *
         * - no texture required
         * - translucent
         * - bright
         * - simple position/color vertices
         */
        VertexConsumer consumer =
                buffer.getBuffer(
                        RenderType.lightning()
                );


        // ========================================================
        // OUTER ZETTAFLARE BEAM
        // ========================================================

        renderBoxBeam(
                consumer,
                poseStack,
                length,
                2.2F,

                1.0F,
                0.12F,
                0.02F,
                0.35F
        );


        // ========================================================
        // MIDDLE LAYER
        // ========================================================

        renderBoxBeam(
                consumer,
                poseStack,
                length,
                1.45F,

                1.0F,
                0.45F,
                0.05F,
                0.65F
        );


        // ========================================================
        // WHITE-HOT CORE
        // ========================================================

        renderBoxBeam(
                consumer,
                poseStack,
                length,
                0.75F,

                1.0F,
                0.95F,
                0.8F,
                1.0F
        );


        poseStack.popPose();

        super.render(
                entity,
                entityYaw,
                partialTick,
                poseStack,
                buffer,
                packedLight
        );
    }


    // ============================================================
    // BOX BEAM
    // ============================================================

    private void renderBoxBeam(
            VertexConsumer consumer,
            PoseStack poseStack,
            float length,
            float radius,
            float red,
            float green,
            float blue,
            float alpha
    ) {

        PoseStack.Pose pose =
                poseStack.last();


        float r = radius;


        // LEFT
        quad(
                consumer,
                pose,

                -r, -r, 0.0F,
                -r,  r, 0.0F,
                -r,  r, length,
                -r, -r, length,

                red,
                green,
                blue,
                alpha
        );


        // RIGHT
        quad(
                consumer,
                pose,

                r, -r, length,
                r,  r, length,
                r,  r, 0.0F,
                r, -r, 0.0F,

                red,
                green,
                blue,
                alpha
        );


        // TOP
        quad(
                consumer,
                pose,

                -r, r, 0.0F,
                r, r, 0.0F,
                r, r, length,
                -r, r, length,

                red,
                green,
                blue,
                alpha
        );


        // BOTTOM
        quad(
                consumer,
                pose,

                -r, -r, length,
                r, -r, length,
                r, -r, 0.0F,
                -r, -r, 0.0F,

                red,
                green,
                blue,
                alpha
        );
    }


    // ============================================================
    // QUAD
    // ============================================================

    private void quad(
            VertexConsumer consumer,
            PoseStack.Pose pose,

            float x1,
            float y1,
            float z1,

            float x2,
            float y2,
            float z2,

            float x3,
            float y3,
            float z3,

            float x4,
            float y4,
            float z4,

            float red,
            float green,
            float blue,
            float alpha
    ) {

        vertex(
                consumer,
                pose,
                x1,
                y1,
                z1,
                red,
                green,
                blue,
                alpha
        );

        vertex(
                consumer,
                pose,
                x2,
                y2,
                z2,
                red,
                green,
                blue,
                alpha
        );

        vertex(
                consumer,
                pose,
                x3,
                y3,
                z3,
                red,
                green,
                blue,
                alpha
        );

        vertex(
                consumer,
                pose,
                x4,
                y4,
                z4,
                red,
                green,
                blue,
                alpha
        );
    }


    // ============================================================
    // VERTEX
    // ============================================================

    private void vertex(
            VertexConsumer consumer,
            PoseStack.Pose pose,

            float x,
            float y,
            float z,

            float red,
            float green,
            float blue,
            float alpha
    ) {

        consumer.addVertex(
                        pose.pose(),
                        x,
                        y,
                        z
                )
                .setColor(
                        red,
                        green,
                        blue,
                        alpha
                );
    }


    // ============================================================
    // REQUIRED BY ENTITYRENDERER
    // ============================================================

    @Override
    public ResourceLocation getTextureLocation(
            ZettaflareBeamEntity entity
    ) {
        return DUMMY_TEXTURE;
    }
}