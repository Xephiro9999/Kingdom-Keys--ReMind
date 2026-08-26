package online.remind.remind.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.TrailRenderer;
import online.remind.remind.entity.effects.DreamEaterLevelUpEffectEntity;
import org.joml.Matrix4f;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class DreamEaterLevelUpEffectRenderer
        extends EntityRenderer<DreamEaterLevelUpEffectEntity> {

    private static final int TRAIL_LENGTH = 10;

    private static final int OPENING_TRAIL_LENGTH = 2;

    private static final int TRAIL_COUNT = 3;

    private static final float TRAIL_WIDTH = 0.07F;


    private static final Color[] TRAIL_COLORS = {
            new Color(0xD52977),
            new Color(0xFFD52A),
            new Color(0xF80A0A)
    };


    private final Map<Integer, TrailRenderer.Trail[]> trails =
            new HashMap<>();

    private final Map<Integer, Integer> lastTickPushed =
            new HashMap<>();


    public DreamEaterLevelUpEffectRenderer(
            EntityRendererProvider.Context context
    ) {
        super(context);

        this.shadowRadius = 0.0F;
    }


    @Override
    public void render(
            DreamEaterLevelUpEffectEntity entity,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight
    ) {

        renderLevelUpTrails(
                entity,
                partialTicks,
                poseStack,
                bufferSource
        );
    }


    private void renderLevelUpTrails(
            DreamEaterLevelUpEffectEntity entity,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource bufferSource
    ) {

        int id = entity.getId();

        TrailRenderer.Trail[] entityTrails =
                trails.get(id);


        if (entityTrails == null) {

            entityTrails =
                    new TrailRenderer.Trail[TRAIL_COUNT];

            for (int i = 0; i < TRAIL_COUNT; i++) {
                entityTrails[i] =
                        new TrailRenderer.Trail(TRAIL_LENGTH);
            }

            trails.put(
                    id,
                    entityTrails
            );

            removeDeadEffects(entity);
        }


        Entity target =
                entity.getTargetEntity();

        if (target == null) {
            return;
        }

        Integer lastTick =
                lastTickPushed.get(id);

        if (lastTick == null
                || lastTick != entity.tickCount) {

            lastTickPushed.put(
                    id,
                    entity.tickCount
            );


            for (int i = 0; i < TRAIL_COUNT; i++) {

                Vec3 head =
                        calculateTrailHead(
                                entity,
                                target,
                                i
                        );

                entityTrails[i].pushHead(
                        head
                );
            }
        }


        Vec3 origin =
                entity.getPosition(partialTicks);


        VertexConsumer consumer =
                bufferSource.getBuffer(
                        RenderType.debugQuads()
                );

        Matrix4f pose =
                poseStack.last().pose();


        /*
         * Fade the ribbon width during the last few ticks
         * instead of popping out instantly.
         */
        float remaining =
                DreamEaterLevelUpEffectEntity.DURATION
                        - (entity.tickCount + partialTicks);

        float fade =
                Mth.clamp(
                        remaining / 8.0F,
                        0.0F,
                        1.0F
                );

        float width =
                TRAIL_WIDTH * fade;


        for (int i = 0; i < TRAIL_COUNT; i++) {

            TrailRenderer.Trail trail =
                    entityTrails[i];

            Vec3[] points =
                    trail.interpolated(
                            partialTicks
                    );


            /*
             * Same opening behavior as the shotlock renderer.
             */
            if (entity.tickCount < 3
                    && points.length > OPENING_TRAIL_LENGTH) {

                points =
                        Arrays.copyOf(
                                points,
                                OPENING_TRAIL_LENGTH
                        );
            }


            Color color =
                    TRAIL_COLORS[i];


            TrailRenderer.render(
                    points,
                    origin,
                    pose,
                    consumer,

                    color.getRed() / 255F,
                    color.getGreen() / 255F,
                    color.getBlue() / 255F,

                    width
            );
        }
    }


    private Vec3 calculateTrailHead(
            DreamEaterLevelUpEffectEntity effect,
            Entity target,
            int trailIndex
    ) {

        double age = effect.tickCount;

        /*
         * Speed of the "electrons" travelling around the atom.
         */
        double orbitSpeed = 0.30D;


        /*
         * Center the atom effect on the Dream Eater's body.
         */
        double centerX = target.getX();

        double centerY =
                target.getY()
                        + target.getBbHeight() * 0.85D;

        double centerZ = target.getZ();


        /*
         * Overall size of the atom.
         */
        double horizontalRadius =
                Math.max(
                        0.75D,
                        target.getBbWidth() * 1.15D
                );

        double verticalRadius =
                Math.max(
                        0.65D,
                        target.getBbHeight() * 0.65D
                );


        /*
         * Offset the moving heads so all three aren't
         * sitting on exactly the same point.
         */
        double phase =
                trailIndex
                        * ((Math.PI * 2.0D) / TRAIL_COUNT);


        double angle =
                age * orbitSpeed
                        + phase;


        /*
         * Start with an ellipse in a vertical plane.
         */
        double localX =
                Math.cos(angle)
                        * horizontalRadius;

        double localY =
                Math.sin(angle)
                        * verticalRadius;


        /*
         * Rotate each ellipse around the vertical Y axis.
         *
         * With three trails:
         *
         * Trail 0 =   0°
         * Trail 1 =  60°
         * Trail 2 = 120°
         *
         * This produces the classic atom-orbit appearance.
         */
        double planeAngle =
                trailIndex
                        * (Math.PI / 3.0D);


        double x =
                centerX
                        + localX
                        * Math.cos(planeAngle);

        double z =
                centerZ
                        + localX
                        * Math.sin(planeAngle);

        double y =
                centerY
                        + localY;


        return new Vec3(
                x,
                y,
                z
        );
    }


    private void removeDeadEffects(
            DreamEaterLevelUpEffectEntity current
    ) {

        trails.keySet().removeIf(
                id ->
                        current.level()
                                .getEntity(id) == null
        );

        lastTickPushed.keySet().removeIf(
                id ->
                        !trails.containsKey(id)
        );
    }

    @Override
    public ResourceLocation getTextureLocation(
            DreamEaterLevelUpEffectEntity entity
    ) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}