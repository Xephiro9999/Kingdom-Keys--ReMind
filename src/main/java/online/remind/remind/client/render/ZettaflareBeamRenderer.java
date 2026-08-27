package online.remind.remind.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.magic.ZettaflareBeamEntity;

public class ZettaflareBeamRenderer
        extends EntityRenderer<ZettaflareBeamEntity> {

    // ============================================================
    // TEXTURE
    // ============================================================

    private static final ResourceLocation BEAM_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID,
                    "textures/entity/magic/zettaflare_beam.png"
            );


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public ZettaflareBeamRenderer(
            EntityRendererProvider.Context context
    ) {
        super(context);

        this.shadowRadius = 0.0F;
    }


    // ============================================================
    // DON'T CULL THE GIANT BEAM
    // ============================================================

    @Override
    public boolean shouldRender(
            ZettaflareBeamEntity entity,
            Frustum frustum,
            double cameraX,
            double cameraY,
            double cameraZ
    ) {
        return true;
    }


    // ============================================================
    // MAIN RENDER
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

        // --------------------------------------------------------
        // Direction
        // --------------------------------------------------------

        Vec3 direction =
                entity.getBeamDirection(
                        partialTick
                );


        if (direction.lengthSqr() <= 0.0001D) {
            return;
        }


        direction =
                direction.normalize();


        // ========================================================
        // BEAM VARIANT
        // ========================================================

        int variant =
                entity.getBeamVariant();


        int outerR;
        int outerG;
        int outerB;

        int middleR;
        int middleG;
        int middleB;

        int coreR;
        int coreG;
        int coreB;


        switch (variant) {


            // ====================================================
            // FINAL FLASH
            // ====================================================

            case ZettaflareBeamEntity.VARIANT_FINAL_FLASH -> {

                // Deep gold outer glow
                outerR = 255;
                outerG = 145;
                outerB = 0;


                // Bright yellow middle
                middleR = 255;
                middleG = 255;
                middleB = 0;


                // White-yellow core
                coreR = 255;
                coreG = 255;
                coreB = 255;
            }


            // ====================================================
            // KAMEHAMEHA
            // ====================================================

            case ZettaflareBeamEntity.VARIANT_KAMEHAMEHA -> {

                // Deep blue outer glow
                outerR = 0;
                outerG = 0;
                outerB = 255;


                // Bright cyan-blue middle
                middleR = 0;
                middleG = 180;
                middleB = 255;


                // White-blue core
                coreR = 255;
                coreG = 255;
                coreB = 255;
            }


            // ====================================================
            // ZETTAFLARE
            // ====================================================

            default -> {

                // Red outer glow
                outerR = 255;
                outerG = 0;
                outerB = 0;


                // Orange middle
                middleR = 255;
                middleG = 135;
                middleB = 0;


                // White-hot core
                coreR = 255;
                coreG = 255;
                coreB = 255;
            }
        }


        // ========================================================
        // ANIMATION AGE
        // ========================================================

        float age =
                entity.tickCount
                        + partialTick;


        // ========================================================
        // BEAM GROWTH
        // ========================================================

        /*
         * Beam reaches full length after roughly 8 ticks.
         */
        float growProgress =
                Math.min(
                        1.0F,
                        age / 8.0F
                );


        /*
         * Ease-out:
         *
         * violently launches out,
         * then slows near full extension.
         */
        float easedGrowth =
                1.0F
                        - (
                        1.0F - growProgress
                )
                        * (
                        1.0F - growProgress
                );


        float length =
                (float) entity.getBeamLength()
                        * easedGrowth;


        // ========================================================
        // PULSE
        // ========================================================

        float pulse =
                1.0F
                        + (
                        (float) Math.sin(
                                age * 0.8F
                        )
                                * 0.05F
                );


        // ========================================================
        // SPIN
        // ========================================================

        /*
         * Base rotation speed.
         *
         * Individual layers rotate at different rates below.
         */
        float spin =
                age * 3.0F;


        // ========================================================
        // AIM BEAM
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
                        Math.asin(
                                direction.y
                        )
                );


        // ========================================================
        // RENDER
        // ========================================================

        poseStack.pushPose();


        try {

            /*
             * Our beam geometry extends along local +Z.
             *
             * Rotate local +Z toward where the caster is looking.
             */
            poseStack.mulPose(
                    Axis.YP.rotationDegrees(
                            yaw
                    )
            );


            poseStack.mulPose(
                    Axis.XP.rotationDegrees(
                            pitch
                    )
            );


            VertexConsumer consumer =
                    buffer.getBuffer(
                            RenderType.entityTranslucentEmissive(
                                    BEAM_TEXTURE
                            )
                    );


            // ====================================================
            // OUTER GLOW
            // ====================================================

            renderCrossBeamLayer(
                    consumer,
                    poseStack,

                    length,

                    5.0F * pulse,

                    spin,

                    outerR,
                    outerG,
                    outerB,

                    75
            );


            // ====================================================
            // MIDDLE ENERGY
            // ====================================================

            renderCrossBeamLayer(
                    consumer,
                    poseStack,

                    length,

                    3.25F * pulse,

                    -spin * 1.35F,

                    middleR,
                    middleG,
                    middleB,

                    155
            );


            // ====================================================
            // WHITE-HOT CORE
            // ====================================================

            renderCrossBeamLayer(
                    consumer,
                    poseStack,

                    length,

                    1.5F * pulse,

                    spin * 1.75F,

                    coreR,
                    coreG,
                    coreB,

                    255
            );


        } finally {

            /*
             * Prevent:
             *
             * IllegalStateException:
             * Pose stack not empty
             */
            poseStack.popPose();
        }


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
    // CROSS-BEAM LAYER
    // ============================================================

    private void renderCrossBeamLayer(
            VertexConsumer consumer,
            PoseStack poseStack,
            float length,
            float radius,
            float spin,
            int red,
            int green,
            int blue,
            int alpha
    ) {

        poseStack.pushPose();


        try {

            /*
             * Spin this complete layer around its forward axis.
             */
            poseStack.mulPose(
                    Axis.ZP.rotationDegrees(
                            spin
                    )
            );


            /*
             * Four crossed planes.
             *
             * Viewed from the front:
             *
             *       |
             *     \ | /
             *   ---+---
             *     / | \
             *       |
             */

            renderPlane(
                    consumer,
                    poseStack,
                    length,
                    radius,
                    0.0F,
                    red,
                    green,
                    blue,
                    alpha
            );


            renderPlane(
                    consumer,
                    poseStack,
                    length,
                    radius,
                    45.0F,
                    red,
                    green,
                    blue,
                    alpha
            );


            renderPlane(
                    consumer,
                    poseStack,
                    length,
                    radius,
                    90.0F,
                    red,
                    green,
                    blue,
                    alpha
            );


            renderPlane(
                    consumer,
                    poseStack,
                    length,
                    radius,
                    135.0F,
                    red,
                    green,
                    blue,
                    alpha
            );


        } finally {

            poseStack.popPose();
        }
    }


    // ============================================================
    // SINGLE BEAM PLANE
    // ============================================================

    private void renderPlane(
            VertexConsumer consumer,
            PoseStack poseStack,
            float length,
            float radius,
            float rotationDegrees,
            int red,
            int green,
            int blue,
            int alpha
    ) {

        poseStack.pushPose();


        try {

            /*
             * Rotate this sheet around the beam axis.
             */
            poseStack.mulPose(
                    Axis.ZP.rotationDegrees(
                            rotationDegrees
                    )
            );


            PoseStack.Pose pose =
                    poseStack.last();


            float r =
                    radius;


            /*
             * CORRECT UV MAPPING:
             *
             * U = ALONG BEAM LENGTH
             *
             * V = ACROSS BEAM WIDTH
             *
             *
             * So:
             *
             *       START                END
             *
             *     0,0 ------------------ 1,0
             *      |                      |
             *      |                      |
             *     0,1 ------------------ 1,1
             *
             *
             * This prevents the texture's bright center from
             * becoming a giant blob halfway down the beam.
             */


            // START / LEFT
            vertex(
                    consumer,
                    pose,

                    -r,
                    0.0F,
                    0.0F,

                    0.0F,
                    0.0F,

                    red,
                    green,
                    blue,
                    alpha
            );


            // START / RIGHT
            vertex(
                    consumer,
                    pose,

                    r,
                    0.0F,
                    0.0F,

                    0.0F,
                    1.0F,

                    red,
                    green,
                    blue,
                    alpha
            );


            // END / RIGHT
            vertex(
                    consumer,
                    pose,

                    r,
                    0.0F,
                    length,

                    1.0F,
                    1.0F,

                    red,
                    green,
                    blue,
                    alpha
            );


            // END / LEFT
            vertex(
                    consumer,
                    pose,

                    -r,
                    0.0F,
                    length,

                    1.0F,
                    0.0F,

                    red,
                    green,
                    blue,
                    alpha
            );


        } finally {

            poseStack.popPose();
        }
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
            float u,
            float v,
            int red,
            int green,
            int blue,
            int alpha
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
                )

                .setUv(
                        u,
                        v
                )

                .setOverlay(
                        OverlayTexture.NO_OVERLAY
                )

                .setLight(
                        0xF000F0
                )

                .setNormal(
                        pose,
                        0.0F,
                        1.0F,
                        0.0F
                );
    }


    // ============================================================
    // TEXTURE
    // ============================================================

    @Override
    public ResourceLocation getTextureLocation(
            ZettaflareBeamEntity entity
    ) {

        return BEAM_TEXTURE;
    }
}