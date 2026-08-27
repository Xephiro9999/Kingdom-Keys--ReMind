package online.remind.remind.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.entity.magic.ZettaflareBeamEntity;

import javax.annotation.Nullable;


@OnlyIn(Dist.CLIENT)
public class ZettaflareBeamRenderer
        extends EntityRenderer<ZettaflareBeamEntity> {


    // ============================================================
    // TEXTURES
    // ============================================================

    private static final ResourceLocation ZETTAFLARE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID,
                    "textures/entity/magic/zettaflare_beam.png"
            );


    private static final ResourceLocation FINAL_FLASH_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID,
                    "textures/entity/magic/final_flash_beam.png"
            );


    private static final ResourceLocation KAMEHAMEHA_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    KingdomKeysReMind.MODID,
                    "textures/entity/magic/kamehameha_beam.png"
            );


    // ============================================================
    // CYLINDER SETTINGS
    // ============================================================

    /*
     * 16 sides gives us a reasonably smooth cylinder without
     * getting stupidly expensive.
     */
    private static final int SEGMENTS =
            16;


    // ============================================================
    // DEFAULT SIZE
    // ============================================================

    private static final float DEFAULT_OUTER_RADIUS =
            2.25F;

    private static final float DEFAULT_MIDDLE_RADIUS =
            1.65F;

    private static final float DEFAULT_CORE_RADIUS =
            0.90F;


    // ============================================================
    // TEXTURE ROWS
    // ============================================================

    /*
     * IMPORTANT:
     *
     * Our beam textures are horizontal strips:
     *
     * TOP
     * transparent
     * glow
     * color
     * bright
     * WHITE CORE
     * bright
     * color
     * glow
     * transparent
     * BOTTOM
     *
     * We do NOT wrap that vertical gradient around the cylinder.
     *
     * Instead, each cylinder layer samples one horizontal row.
     */

    private static final float OUTER_TEXTURE_V =
            0.23F;

    private static final float MIDDLE_TEXTURE_V =
            0.36F;

    private static final float CORE_TEXTURE_V =
            0.50F;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public ZettaflareBeamRenderer(
            EntityRendererProvider.Context context
    ) {

        super(context);

        this.shadowRadius =
                0.0F;
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

        Vec3 direction =
                entity.getBeamDirection(
                        partialTick
                );


        if (
                direction.lengthSqr()
                        <= 0.000001D
        ) {

            return;
        }


        direction =
                direction.normalize();


        double length =
                entity.getBeamLength();


        int variant =
                entity.getBeamVariant();


        ResourceLocation texture =
                getBeamTexture(
                        entity
                );


        BeamVisualSettings settings =
                getVisualSettings(
                        variant
                );


        float age =
                entity.tickCount
                        + partialTick;


        float pulse =
                getPulse(
                        variant,
                        age
                );


        // ========================================================
        // OUTER GLOW
        // ========================================================

        renderCylinder(
                poseStack,
                buffer,

                texture,

                direction,
                length,

                settings.outerRadius
                        * pulse,

                settings.outerAlpha,

                age
                        * settings.outerScrollSpeed,

                settings.outerUvScale,

                OUTER_TEXTURE_V
        );


        // ========================================================
        // MIDDLE ENERGY
        // ========================================================

        renderCylinder(
                poseStack,
                buffer,

                texture,

                direction,
                length,

                settings.middleRadius
                        * pulse,

                settings.middleAlpha,

                age
                        * settings.middleScrollSpeed,

                settings.middleUvScale,

                MIDDLE_TEXTURE_V
        );


        // ========================================================
        // WHITE-HOT CORE
        // ========================================================

        renderCylinder(
                poseStack,
                buffer,

                texture,

                direction,
                length,

                settings.coreRadius,

                255,

                -age
                        * settings.coreScrollSpeed,

                settings.coreUvScale,

                CORE_TEXTURE_V
        );


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
    // TEXTURE SELECTION
    // ============================================================

    private ResourceLocation getBeamTexture(
            ZettaflareBeamEntity entity
    ) {

        return switch (
                entity.getBeamVariant()
                ) {

            case ZettaflareBeamEntity.VARIANT_FINAL_FLASH ->

                    FINAL_FLASH_TEXTURE;


            case ZettaflareBeamEntity.VARIANT_KAMEHAMEHA ->

                    KAMEHAMEHA_TEXTURE;


            default ->

                    ZETTAFLARE_TEXTURE;
        };
    }


    // ============================================================
    // VARIANT SETTINGS
    // ============================================================

    private BeamVisualSettings getVisualSettings(
            int variant
    ) {

        // ========================================================
        // FINAL FLASH
        // ========================================================

        if (
                variant
                        == ZettaflareBeamEntity.VARIANT_FINAL_FLASH
        ) {

            /*
             * Final Flash:
             *
             * Big
             * concentrated
             * mostly rigid
             */
            return new BeamVisualSettings(

                    2.35F,
                    1.75F,
                    1.05F,

                    180,
                    230,

                    0.025F,
                    -0.045F,
                    0.075F,

                    5.0F,
                    6.0F,
                    7.0F
            );
        }


        // ========================================================
        // KAMEHAMEHA
        // ========================================================

        if (
                variant
                        == ZettaflareBeamEntity.VARIANT_KAMEHAMEHA
        ) {

            /*
             * Kamehameha:
             *
             * Slightly tighter
             * smoother
             * faster flowing texture
             */
            return new BeamVisualSettings(

                    2.10F,
                    1.55F,
                    0.90F,

                    175,
                    225,

                    0.075F,
                    0.115F,
                    0.150F,

                    4.0F,
                    5.5F,
                    7.0F
            );
        }


        // ========================================================
        // ZETTAFLARE
        // ========================================================

        /*
         * Zettaflare:
         *
         * Biggest visual instability.
         */
        return new BeamVisualSettings(

                DEFAULT_OUTER_RADIUS,
                DEFAULT_MIDDLE_RADIUS,
                DEFAULT_CORE_RADIUS,

                185,
                230,

                0.055F,
                -0.080F,
                0.110F,

                4.5F,
                6.0F,
                8.0F
        );
    }


    // ============================================================
    // PULSE
    // ============================================================

    private float getPulse(
            int variant,
            float age
    ) {

        // ========================================================
        // FINAL FLASH
        // ========================================================

        if (
                variant
                        == ZettaflareBeamEntity.VARIANT_FINAL_FLASH
        ) {

            /*
             * Final Flash barely changes size.
             */
            return 1.0F
                    + (
                    (float) Math.sin(
                            age
                                    * 1.45F
                    )
                            * 0.022F
            );
        }


        // ========================================================
        // KAMEHAMEHA
        // ========================================================

        if (
                variant
                        == ZettaflareBeamEntity.VARIANT_KAMEHAMEHA
        ) {

            /*
             * Smooth breathing wave.
             */
            return 1.0F
                    + (
                    (float) Math.sin(
                            age
                                    * 0.55F
                    )
                            * 0.040F
            );
        }


        // ========================================================
        // ZETTAFLARE
        // ========================================================

        float pulseA =
                (float) Math.sin(
                        age
                                * 0.85F
                )
                        * 0.050F;


        float pulseB =
                (float) Math.sin(
                        age
                                * 1.73F
                )
                        * 0.022F;


        return 1.0F
                + pulseA
                + pulseB;
    }


    // ============================================================
    // CYLINDER RENDERER
    // ============================================================

    private void renderCylinder(
            PoseStack poseStack,
            MultiBufferSource buffer,

            ResourceLocation texture,

            Vec3 direction,
            double length,

            float radius,

            int alpha,

            float uvOffset,

            float uvScale,

            float textureV
    ) {

        VertexConsumer consumer =
                buffer.getBuffer(
                        RenderType.entityTranslucentEmissive(
                                texture
                        )
                );


        // ========================================================
        // BUILD AXES AROUND BEAM
        // ========================================================

        Vec3 reference;


        /*
         * Avoid using Y as the reference if we're looking almost
         * straight up/down.
         */
        if (
                Math.abs(
                        direction.y
                )
                        > 0.95D
        ) {

            reference =
                    new Vec3(
                            1.0D,
                            0.0D,
                            0.0D
                    );

        } else {

            reference =
                    new Vec3(
                            0.0D,
                            1.0D,
                            0.0D
                    );
        }


        Vec3 side =
                direction
                        .cross(
                                reference
                        )
                        .normalize();


        Vec3 up =
                side
                        .cross(
                                direction
                        )
                        .normalize();


        Vec3 beamEnd =
                direction.scale(
                        length
                );


        PoseStack.Pose pose =
                poseStack.last();


        // ========================================================
        // UV
        // ========================================================

        float uStart =
                uvOffset;


        float uEnd =
                uvOffset
                        + (
                        (float) length
                                / uvScale
                );


        // ========================================================
        // CYLINDER SIDES
        // ========================================================

        for (
                int i = 0;
                i < SEGMENTS;
                i++
        ) {

            int next =
                    (i + 1)
                            % SEGMENTS;


            double angleA =
                    (
                            Math.PI
                                    * 2.0D
                                    * i
                    )
                            / SEGMENTS;


            double angleB =
                    (
                            Math.PI
                                    * 2.0D
                                    * next
                    )
                            / SEGMENTS;


            // ====================================================
            // RADIAL NORMALS
            // ====================================================

            Vec3 radialA =
                    side
                            .scale(
                                    Math.cos(
                                            angleA
                                    )
                            )
                            .add(
                                    up.scale(
                                            Math.sin(
                                                    angleA
                                            )
                                    )
                            )
                            .normalize();


            Vec3 radialB =
                    side
                            .scale(
                                    Math.cos(
                                            angleB
                                    )
                            )
                            .add(
                                    up.scale(
                                            Math.sin(
                                                    angleB
                                            )
                                    )
                            )
                            .normalize();


            // ====================================================
            // START
            // ====================================================

            Vec3 startA =
                    radialA.scale(
                            radius
                    );


            Vec3 startB =
                    radialB.scale(
                            radius
                    );


            // ====================================================
            // END
            // ====================================================

            Vec3 endA =
                    beamEnd.add(
                            radialA.scale(
                                    radius
                            )
                    );


            Vec3 endB =
                    beamEnd.add(
                            radialB.scale(
                                    radius
                            )
                    );


            /*
             * IMPORTANT:
             *
             * The SAME V coordinate is used around the entire
             * circumference.
             *
             * This prevents the vertical beam gradient from being
             * wrapped around the cylinder.
             */

            float v =
                    textureV;


            // ====================================================
            // OUTSIDE FACE
            // ====================================================

            addBeamVertex(
                    consumer,
                    pose,

                    startA,
                    radialA,

                    uStart,
                    v,

                    alpha
            );


            addBeamVertex(
                    consumer,
                    pose,

                    startB,
                    radialB,

                    uStart,
                    v,

                    alpha
            );


            addBeamVertex(
                    consumer,
                    pose,

                    endB,
                    radialB,

                    uEnd,
                    v,

                    alpha
            );


            addBeamVertex(
                    consumer,
                    pose,

                    endA,
                    radialA,

                    uEnd,
                    v,

                    alpha
            );


            // ====================================================
            // INSIDE / REVERSE FACE
            // ====================================================

            /*
             * Render the exact same quad in reverse.
             *
             * This prevents half the beam disappearing because of
             * back-face culling.
             */

            addBeamVertex(
                    consumer,
                    pose,

                    endA,
                    radialA.scale(-1.0D),

                    uEnd,
                    v,

                    alpha
            );


            addBeamVertex(
                    consumer,
                    pose,

                    endB,
                    radialB.scale(-1.0D),

                    uEnd,
                    v,

                    alpha
            );


            addBeamVertex(
                    consumer,
                    pose,

                    startB,
                    radialB.scale(-1.0D),

                    uStart,
                    v,

                    alpha
            );


            addBeamVertex(
                    consumer,
                    pose,

                    startA,
                    radialA.scale(-1.0D),

                    uStart,
                    v,

                    alpha
            );
        }
    }


    // ============================================================
    // VERTEX
    // ============================================================

    private void addBeamVertex(
            VertexConsumer consumer,
            PoseStack.Pose pose,

            Vec3 position,
            Vec3 normal,

            float u,
            float v,

            int alpha
    ) {

        consumer
                .addVertex(
                        pose.pose(),

                        (float) position.x,
                        (float) position.y,
                        (float) position.z
                )

                /*
                 * WHITE tint.
                 *
                 * The PNG itself determines whether this is:
                 *
                 * red Zettaflare
                 * yellow Final Flash
                 * blue Kamehameha
                 */
                .setColor(
                        255,
                        255,
                        255,
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

                        (float) normal.x,
                        (float) normal.y,
                        (float) normal.z
                );
    }


    // ============================================================
    // CULLING
    // ============================================================

    @Override
    public boolean shouldRender(
            ZettaflareBeamEntity entity,
            Frustum camera,
            double camX,
            double camY,
            double camZ
    ) {

        /*
         * Beam is way larger than the controller entity.
         */
        return true;
    }


    // ============================================================
    // TEXTURE LOCATION
    // ============================================================

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(
            ZettaflareBeamEntity entity
    ) {

        return getBeamTexture(
                entity
        );
    }


    // ============================================================
    // VISUAL SETTINGS
    // ============================================================

    private static class BeamVisualSettings {

        private final float outerRadius;
        private final float middleRadius;
        private final float coreRadius;


        private final int outerAlpha;
        private final int middleAlpha;


        private final float outerScrollSpeed;
        private final float middleScrollSpeed;
        private final float coreScrollSpeed;


        private final float outerUvScale;
        private final float middleUvScale;
        private final float coreUvScale;


        private BeamVisualSettings(
                float outerRadius,
                float middleRadius,
                float coreRadius,

                int outerAlpha,
                int middleAlpha,

                float outerScrollSpeed,
                float middleScrollSpeed,
                float coreScrollSpeed,

                float outerUvScale,
                float middleUvScale,
                float coreUvScale
        ) {

            this.outerRadius =
                    outerRadius;


            this.middleRadius =
                    middleRadius;


            this.coreRadius =
                    coreRadius;


            this.outerAlpha =
                    outerAlpha;


            this.middleAlpha =
                    middleAlpha;


            this.outerScrollSpeed =
                    outerScrollSpeed;


            this.middleScrollSpeed =
                    middleScrollSpeed;


            this.coreScrollSpeed =
                    coreScrollSpeed;


            this.outerUvScale =
                    outerUvScale;


            this.middleUvScale =
                    middleUvScale;


            this.coreUvScale =
                    coreUvScale;
        }
    }
}