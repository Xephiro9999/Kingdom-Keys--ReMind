package online.remind.remind.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.client.model.UltimaModel;
import online.remind.remind.entity.magic.UltimaEntity;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;


@OnlyIn(Dist.CLIENT)
public class UltimaEntityRenderer
		extends EntityRenderer<UltimaEntity> {


	// ============================================================
	// TEXTURE
	// ============================================================

	public static final ResourceLocation TEXTURE =
			ResourceLocation.fromNamespaceAndPath(
					KingdomKeysReMind.MODID,
					"textures/entity/models/ultima.png"
			);

	public static final ResourceLocation TRAIL_TEXTURE =
			ResourceLocation.fromNamespaceAndPath(
					"minecraft",
					"textures/entity/beacon_beam.png"
			);


	// ============================================================
	// PHASE TIMING
	// ============================================================

	/*
	 * Keep this equal to UltimaEntity.
	 */
	private static final float IMPLOSION_DURATION =
			16.0F;


	private static final float EXPANSION_DURATION =
			40.0F;


	// ============================================================
	// MODEL SIZE
	// ============================================================

	private static final float FLYING_SCALE =
			0.22F;


	/*
	 * Implosion starts at the SAME size as the flying projectile.
	 *
	 * This prevents:
	 *
	 * tiny orb
	 *      ↓
	 * giant pop
	 *      ↓
	 * shrinking
	 */
	private static final float IMPLOSION_START_SCALE =
			FLYING_SCALE;


	private static final float IMPLOSION_END_SCALE =
			0.045F;


	private static final float MAX_EXPANSION_SCALE =
			8.0F;


	// ============================================================
	// FLYING TRAILS
	// ============================================================

	/*
	 * Three ribbons spiral around the flying projectile.
	 */
	private static final int FLYING_TRAIL_COUNT =
			3;


	/*
	 * Number of historical points in each trail.
	 *
	 * Higher = longer ribbon.
	 */
	private static final int FLYING_TRAIL_LENGTH =
			14;


	/*
	 * Distance the ribbons orbit from the projectile.
	 */
	private static final double FLYING_TRAIL_ORBIT_RADIUS =
			0.17D;


	/*
	 * Rotation speed around the projectile.
	 */
	private static final double FLYING_TRAIL_ROTATION_SPEED =
			0.55D;


	private static final float FLYING_TRAIL_WIDTH =
			0.075F;


	// ============================================================
	// IMPLOSION TRAILS
	// ============================================================

	private static final int IMPLOSION_TRAIL_COUNT =
			3;


	private static final int IMPLOSION_TRAIL_LENGTH =
			16;


	/*
	 * Starting radius of the atom-like trails.
	 *
	 * Unlike the MODEL, these are intentionally larger.
	 *
	 * They represent surrounding magical energy being sucked
	 * into the compressed Ultima core.
	 */
	private static final double IMPLOSION_TRAIL_START_RADIUS =
			1.35D;


	private static final double IMPLOSION_TRAIL_END_RADIUS =
			0.055D;


	private static final double IMPLOSION_TRAIL_ROTATION_SPEED =
			0.78D;


	private static final float IMPLOSION_TRAIL_WIDTH =
			0.09F;


	// ============================================================
	// EXPANSION TRAILS
	// ============================================================

	/*
	 * Six streaks blast away from the detonation point.
	 */
	private static final int EXPANSION_TRAIL_COUNT =
			6;


	private static final int EXPANSION_TRAIL_LENGTH =
			9;


	/*
	 * Only the opening portion of the explosion gets streaks.
	 *
	 * Afterwards they rapidly fade away while the Ultima sphere
	 * continues expanding.
	 */
	private static final float EXPANSION_TRAIL_ACTIVE_TICKS =
			12.0F;


	private static final double EXPANSION_TRAIL_MAX_RADIUS =
			7.0D;


	private static final float EXPANSION_TRAIL_WIDTH =
			0.14F;


	// ============================================================
	// MODEL
	// ============================================================

	private final UltimaModel<Entity> model;


	// ============================================================
	// CLIENT TRAIL STORAGE
	// ============================================================

	/*
	 * Entity renderers are shared between every entity of this type.
	 *
	 * Therefore every Ultima needs its OWN trail history.
	 *
	 * WeakHashMap means old Ultima entities can disappear naturally
	 * without this renderer permanently holding onto them.
	 */
	private final Map<UltimaEntity, UltimaTrailState> trailStates =
			new WeakHashMap<>();


	// ============================================================
	// CONSTRUCTOR
	// ============================================================

	public UltimaEntityRenderer(
			EntityRendererProvider.Context context
	) {

		super(context);


		this.shadowRadius =
				0.0F;


		this.model =
				new UltimaModel<>(
						context.bakeLayer(
								UltimaModel.LAYER_LOCATION
						)
				);
	}


	// ============================================================
	// MAIN RENDER
	// ============================================================

	@Override
	public void render(
			UltimaEntity entity,
			float entityYaw,
			float partialTicks,
			PoseStack poseStack,
			MultiBufferSource buffer,
			int packedLight
	) {

		// ========================================================
		// UPDATE TRAIL HISTORY
		// ========================================================

		UltimaTrailState trailState =
				trailStates.computeIfAbsent(
						entity,
						ignored -> new UltimaTrailState()
				);


		updateTrailState(
				entity,
				trailState
		);


		// ========================================================
		// RENDER TRAILS
		// ========================================================

		/*
		 * Do this BEFORE the actual Ultima model.
		 *
		 * That lets the glowing orb remain the visual focal point.
		 */
		renderTrails(
				entity,
				trailState,
				poseStack,
				buffer
		);


		// ========================================================
		// MODEL BUFFER
		// ========================================================

		VertexConsumer vertexConsumer =
				buffer.getBuffer(
						RenderType.entityTranslucent(
								TEXTURE
						)
				);


		poseStack.pushPose();


		try {

			int phase =
					entity.getPhase();


			// ====================================================
			// FLYING
			// ====================================================

			if (
					phase
							== UltimaEntity.PHASE_FLYING
			) {

				renderFlying(
						entity,
						partialTicks,
						poseStack,
						vertexConsumer,
						packedLight
				);
			}


			// ====================================================
			// IMPLODING
			// ====================================================

			else if (
					phase
							== UltimaEntity.PHASE_IMPLODING
			) {

				renderImplosion(
						entity,
						partialTicks,
						poseStack,
						vertexConsumer,
						packedLight
				);
			}


			// ====================================================
			// EXPANDING
			// ====================================================

			else if (
					phase
							== UltimaEntity.PHASE_EXPANDING
			) {

				renderExpansion(
						entity,
						partialTicks,
						poseStack,
						vertexConsumer,
						packedLight
				);
			}


		} finally {

			poseStack.popPose();
		}


		super.render(
				entity,
				entityYaw,
				partialTicks,
				poseStack,
				buffer,
				packedLight
		);
	}


	// ============================================================
	// FLYING MODEL
	// ============================================================

	private void renderFlying(
			UltimaEntity entity,
			float partialTicks,
			PoseStack poseStack,
			VertexConsumer vertexConsumer,
			int packedLight
	) {

		float age =
				entity.tickCount
						+ partialTicks;


		/*
		 * Extremely subtle breathing pulse.
		 *
		 * Uniform scale means the CENTER of the model never moves
		 * away from the real projectile position.
		 */
		float pulse =
				1.0F
						+ (float) Math.sin(
						age * 0.8F
				)
						* 0.025F;


		float scale =
				FLYING_SCALE
						* pulse;


		/*
		 * IMPORTANT:
		 *
		 * NO TRANSLATION.
		 *
		 * Model
		 * Particle
		 * Hitbox
		 *
		 * all share the same entity origin.
		 */
		poseStack.scale(
				scale,
				scale,
				scale
		);


		renderModel(
				poseStack,
				vertexConsumer,
				packedLight
		);
	}


	// ============================================================
	// IMPLOSION MODEL
	// ============================================================

	private void renderImplosion(
			UltimaEntity entity,
			float partialTicks,
			PoseStack poseStack,
			VertexConsumer vertexConsumer,
			int packedLight
	) {

		float phaseAge =
				getPhaseAge(
						entity,
						partialTicks
				);


		float progress =
				clamp01(
						phaseAge
								/ IMPLOSION_DURATION
				);


		/*
		 * Smoothstep gives a smooth beginning and ending.
		 *
		 * No vibration.
		 * No jitter.
		 * No apparent animation restart.
		 */
		float smoothProgress =
				smoothStep(
						progress
				);


		float scale =
				lerp(
						IMPLOSION_START_SCALE,
						IMPLOSION_END_SCALE,
						smoothProgress
				);


		poseStack.scale(
				scale,
				scale,
				scale
		);


		renderModel(
				poseStack,
				vertexConsumer,
				packedLight
		);
	}


	// ============================================================
	// EXPANSION MODEL
	// ============================================================

	private void renderExpansion(
			UltimaEntity entity,
			float partialTicks,
			PoseStack poseStack,
			VertexConsumer vertexConsumer,
			int packedLight
	) {

		float phaseAge =
				getPhaseAge(
						entity,
						partialTicks
				);


		float progress =
				clamp01(
						phaseAge
								/ EXPANSION_DURATION
				);


		/*
		 * Cubic ease-out.
		 *
		 * Very fast initial blast,
		 * then gradually slows down.
		 */
		float easedProgress =
				1.0F
						- (float) Math.pow(
						1.0F - progress,
						3.0D
				);


		/*
		 * Starts at EXACTLY the final implosion size.
		 *
		 * No scale jump between phases.
		 */
		float scale =
				lerp(
						IMPLOSION_END_SCALE,
						MAX_EXPANSION_SCALE,
						easedProgress
				);


		poseStack.scale(
				scale,
				scale,
				scale
		);


		renderModel(
				poseStack,
				vertexConsumer,
				packedLight
		);
	}


	// ============================================================
	// UPDATE TRAIL STATE
	// ============================================================

	private void updateTrailState(
			UltimaEntity entity,
			UltimaTrailState state
	) {

		/*
		 * render() happens MANY TIMES per second.
		 *
		 * We only want one trail point per game tick.
		 */
		if (
				state.lastTick
						== entity.tickCount
		) {

			return;
		}


		int phase =
				entity.getPhase();


		// ========================================================
		// PHASE CHANGED
		// ========================================================

		if (
				state.lastPhase
						!= phase
		) {

			state.clear();

			state.lastPhase =
					phase;
		}


		// ========================================================
		// UPDATE CORRECT PHASE
		// ========================================================

		switch (phase) {

			case UltimaEntity.PHASE_FLYING ->

					updateFlyingTrails(
							entity,
							state
					);


			case UltimaEntity.PHASE_IMPLODING ->

					updateImplosionTrails(
							entity,
							state
					);


			case UltimaEntity.PHASE_EXPANDING ->

					updateExpansionTrails(
							entity,
							state
					);
		}


		state.lastTick =
				entity.tickCount;
	}


	// ============================================================
	// FLYING TRAILS
	// ============================================================

	private void updateFlyingTrails(
			UltimaEntity entity,
			UltimaTrailState state
	) {

		Vec3 center =
				entity.position();


		Vec3 movement =
				entity.getDeltaMovement();


		Vec3 forward;


		if (
				movement.lengthSqr()
						> 0.000001D
		) {

			forward =
					movement.normalize();

		} else {

			/*
			 * Fallback if interpolation/networking momentarily gives
			 * the client a zero velocity.
			 */
			forward =
					new Vec3(
							0.0D,
							0.0D,
							1.0D
					);
		}


		// ========================================================
		// BUILD PERPENDICULAR AXES
		// ========================================================

		Vec3 reference;


		/*
		 * Don't cross against an almost-parallel vector.
		 */
		if (
				Math.abs(
						forward.y
				)
						> 0.90D
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
				forward
						.cross(
								reference
						)
						.normalize();


		Vec3 up =
				side
						.cross(
								forward
						)
						.normalize();


		// ========================================================
		// THREE CORKSCREW TRAILS
		// ========================================================

		for (
				int i = 0;
				i < FLYING_TRAIL_COUNT;
				i++
		) {

			double phaseOffset =
					i
							* (
							Math.PI
									* 2.0D
									/ FLYING_TRAIL_COUNT
					);


			double angle =
					entity.tickCount
							* FLYING_TRAIL_ROTATION_SPEED
							+ phaseOffset;


			double cos =
					Math.cos(
							angle
					);


			double sin =
					Math.sin(
							angle
					);


			Vec3 offset =
					side
							.scale(
									cos
											* FLYING_TRAIL_ORBIT_RADIUS
							)
							.add(
									up.scale(
											sin
													* FLYING_TRAIL_ORBIT_RADIUS
									)
							);


			Vec3 head =
					center.add(
							offset
					);


			pushPoint(
					state.trails.get(i),
					head,
					FLYING_TRAIL_LENGTH
			);
		}
	}


	// ============================================================
	// IMPLOSION TRAILS
	// ============================================================

	private void updateImplosionTrails(
			UltimaEntity entity,
			UltimaTrailState state
	) {

		Vec3 center =
				entity.position();


		float phaseAge =
				Math.max(
						0.0F,
						entity.tickCount
								- entity.getPhaseStartTick()
				);


		float progress =
				clamp01(
						phaseAge
								/ IMPLOSION_DURATION
				);


		float smoothProgress =
				smoothStep(
						progress
				);


		/*
		 * The line trails physically collapse toward Ultima.
		 */
		double radius =
				lerpDouble(
						IMPLOSION_TRAIL_START_RADIUS,
						IMPLOSION_TRAIL_END_RADIUS,
						smoothProgress
				);


		// ========================================================
		// THREE DIFFERENT ORBITAL PLANES
		// ========================================================

		for (
				int i = 0;
				i < IMPLOSION_TRAIL_COUNT;
				i++
		) {

			/*
			 * Every trail is rotated into a different orbital plane.
			 *
			 * This gives the atom / Kingdom Hearts magic effect.
			 */
			double planeRotation =
					i
							* (
							Math.PI
									/ 3.0D
					);


			double phaseOffset =
					i
							* (
							Math.PI
									* 2.0D
									/ IMPLOSION_TRAIL_COUNT
					);


			double angle =
					phaseAge
							* IMPLOSION_TRAIL_ROTATION_SPEED
							+ phaseOffset;


			double horizontal =
					Math.cos(
							angle
					)
							* radius;


			double orbital =
					Math.sin(
							angle
					)
							* radius;


			double x =
					horizontal;


			double y =
					orbital
							* Math.cos(
							planeRotation
					);


			double z =
					orbital
							* Math.sin(
							planeRotation
					);


			Vec3 head =
					center.add(
							x,
							y,
							z
					);


			pushPoint(
					state.trails.get(i),
					head,
					IMPLOSION_TRAIL_LENGTH
			);
		}
	}


	// ============================================================
	// EXPANSION TRAILS
	// ============================================================

	private void updateExpansionTrails(
			UltimaEntity entity,
			UltimaTrailState state
	) {

		float phaseAge =
				Math.max(
						0.0F,
						entity.tickCount
								- entity.getPhaseStartTick()
				);


		// ========================================================
		// AFTER BURST: LET OLD STREAKS DIE
		// ========================================================

		if (
				phaseAge
						> EXPANSION_TRAIL_ACTIVE_TICKS
		) {

			for (
					int i = 0;
					i < EXPANSION_TRAIL_COUNT;
					i++
			) {

				ArrayDeque<Vec3> trail =
						state.trails.get(i);


				if (!trail.isEmpty()) {

					trail.removeFirst();
				}
			}


			return;
		}


		// ========================================================
		// EXPLOSIVE OUTWARD MOVEMENT
		// ========================================================

		float progress =
				clamp01(
						phaseAge
								/ EXPANSION_TRAIL_ACTIVE_TICKS
				);


		/*
		 * Cubic ease-out = very violent opening.
		 */
		float easedProgress =
				1.0F
						- (float) Math.pow(
						1.0F - progress,
						3.0D
				);


		double radius =
				EXPANSION_TRAIL_MAX_RADIUS
						* easedProgress;


		Vec3 center =
				entity.position();


		for (
				int i = 0;
				i < EXPANSION_TRAIL_COUNT;
				i++
		) {

			Vec3 direction =
					getBurstDirection(
							i
					);


			/*
			 * Tiny curved side movement prevents the lines from
			 * looking like perfectly rigid spokes.
			 */
			Vec3 tangent =
					new Vec3(
							-direction.z,
							0.0D,
							direction.x
					);


			if (
					tangent.lengthSqr()
							< 0.000001D
			) {

				tangent =
						new Vec3(
								1.0D,
								0.0D,
								0.0D
						);
			}


			tangent =
					tangent.normalize();


			double wave =
					Math.sin(
							phaseAge
									* 0.75D
									+ i
					)
							* 0.18D
							* (
							1.0D
									- progress
					);


			Vec3 head =
					center
							.add(
									direction.scale(
											radius
									)
							)
							.add(
									tangent.scale(
											wave
									)
							);


			pushPoint(
					state.trails.get(i),
					head,
					EXPANSION_TRAIL_LENGTH
			);
		}
	}


	// ============================================================
	// EXPLOSION DIRECTIONS
	// ============================================================

	private Vec3 getBurstDirection(
			int index
	) {

		/*
		 * Purposefully asymmetrical.
		 *
		 * Perfectly even XYZ lines look artificial.
		 * These feel more like magical energy breaking apart.
		 */
		return switch (index) {

			case 0 ->
					new Vec3(
							1.0D,
							0.22D,
							0.18D
					).normalize();


			case 1 ->
					new Vec3(
							-1.0D,
							-0.12D,
							-0.26D
					).normalize();


			case 2 ->
					new Vec3(
							0.16D,
							0.72D,
							1.0D
					).normalize();


			case 3 ->
					new Vec3(
							-0.24D,
							-0.54D,
							-1.0D
					).normalize();


			case 4 ->
					new Vec3(
							0.48D,
							1.0D,
							-0.58D
					).normalize();


			default ->
					new Vec3(
							-0.52D,
							-1.0D,
							0.62D
					).normalize();
		};
	}


	// ============================================================
	// PUSH TRAIL POINT
	// ============================================================

	private void pushPoint(
			ArrayDeque<Vec3> trail,
			Vec3 point,
			int maxPoints
	) {

		trail.addLast(
				point
		);


		while (
				trail.size()
						> maxPoints
		) {

			trail.removeFirst();
		}
	}


	// ============================================================
// RENDER ALL TRAILS
// ============================================================

	private void renderTrails(
			UltimaEntity entity,
			UltimaTrailState state,
			PoseStack poseStack,
			MultiBufferSource buffer
	) {

		int phase =
				entity.getPhase();


		/*
		 * IMPORTANT:
		 *
		 * Don't use RenderType.lightning() here.
		 *
		 * We're rendering actual textured ribbon quads now.
		 * Fullbright + emissive makes these MUCH easier to see.
		 */
		VertexConsumer trailConsumer =
				buffer.getBuffer(
						RenderType.entityTranslucentEmissive(
								TRAIL_TEXTURE
						)
				);


		// ========================================================
		// FLYING
		// ========================================================

		if (
				phase
						== UltimaEntity.PHASE_FLYING
		) {

			renderTrail(
					entity,
					state.trails.get(0),
					poseStack,
					trailConsumer,

					255,
					40,
					255,

					FLYING_TRAIL_WIDTH,
					1.0F
			);


			renderTrail(
					entity,
					state.trails.get(1),
					poseStack,
					trailConsumer,

					135,
					55,
					255,

					FLYING_TRAIL_WIDTH,
					1.0F
			);


			renderTrail(
					entity,
					state.trails.get(2),
					poseStack,
					trailConsumer,

					255,
					150,
					255,

					FLYING_TRAIL_WIDTH,
					1.0F
			);
		}


		// ========================================================
		// IMPLODING
		// ========================================================

		else if (
				phase
						== UltimaEntity.PHASE_IMPLODING
		) {

			renderTrail(
					entity,
					state.trails.get(0),
					poseStack,
					trailConsumer,

					255,
					30,
					255,

					IMPLOSION_TRAIL_WIDTH,
					1.0F
			);


			renderTrail(
					entity,
					state.trails.get(1),
					poseStack,
					trailConsumer,

					110,
					40,
					255,

					IMPLOSION_TRAIL_WIDTH,
					1.0F
			);


			renderTrail(
					entity,
					state.trails.get(2),
					poseStack,
					trailConsumer,

					255,
					170,
					255,

					IMPLOSION_TRAIL_WIDTH,
					1.0F
			);
		}


		// ========================================================
		// EXPANDING
		// ========================================================

		else if (
				phase
						== UltimaEntity.PHASE_EXPANDING
		) {

			for (
					int i = 0;
					i < EXPANSION_TRAIL_COUNT;
					i++
			) {

				if (
						i % 3
								== 0
				) {

					renderTrail(
							entity,
							state.trails.get(i),
							poseStack,
							trailConsumer,

							255,
							25,
							255,

							EXPANSION_TRAIL_WIDTH,
							1.0F
					);

				} else if (
						i % 3
								== 1
				) {

					renderTrail(
							entity,
							state.trails.get(i),
							poseStack,
							trailConsumer,

							115,
							45,
							255,

							EXPANSION_TRAIL_WIDTH,
							1.0F
					);

				} else {

					renderTrail(
							entity,
							state.trails.get(i),
							poseStack,
							trailConsumer,

							255,
							180,
							255,

							EXPANSION_TRAIL_WIDTH,
							1.0F
					);
				}
			}
		}
	}


// ============================================================
// RENDER ONE TRAIL
// ============================================================

	private void renderTrail(
			UltimaEntity entity,
			ArrayDeque<Vec3> trail,
			PoseStack poseStack,
			VertexConsumer consumer,
			int red,
			int green,
			int blue,
			float width,
			float alphaMultiplier
	) {

		if (
				trail.size()
						< 2
		) {

			return;
		}


		List<Vec3> points =
				new ArrayList<>(
						trail
				);


		// ========================================================
		// OUTER ENERGY
		// ========================================================

		renderTrailPass(
				entity,
				points,
				poseStack,
				consumer,

				red,
				green,
				blue,

				width,
				alphaMultiplier
		);


		// ========================================================
		// WHITE-HOT CENTER
		// ========================================================

		/*
		 * Second thinner ribbon layered directly on top.
		 *
		 * Gives:
		 *
		 *      purple glow
		 *          ↓
		 *      WHITE CORE
		 *          ↓
		 *      purple glow
		 */
		renderTrailPass(
				entity,
				points,
				poseStack,
				consumer,

				255,
				245,
				255,

				width * 0.32F,
				alphaMultiplier
		);
	}


// ============================================================
// RENDER RIBBON PASS
// ============================================================

	private void renderTrailPass(
			UltimaEntity entity,
			List<Vec3> points,
			PoseStack poseStack,
			VertexConsumer consumer,
			int red,
			int green,
			int blue,
			float width,
			float alphaMultiplier
	) {

		Vec3 entityOrigin =
				entity.position();


		Vec3 cameraPosition =
				Minecraft
						.getInstance()
						.gameRenderer
						.getMainCamera()
						.getPosition();


		PoseStack.Pose pose =
				poseStack.last();


		int lastIndex =
				points.size()
						- 1;


		for (
				int i = 0;
				i < lastIndex;
				i++
		) {

			Vec3 worldA =
					points.get(i);


			Vec3 worldB =
					points.get(i + 1);


			Vec3 segment =
					worldB.subtract(
							worldA
					);


			if (
					segment.lengthSqr()
							< 0.000001D
			) {

				continue;
			}


			// ====================================================
			// TRAIL PROGRESS
			// ====================================================

			float progressA =
					i
							/ (float) lastIndex;


			float progressB =
					(i + 1)
							/ (float) lastIndex;


			// ====================================================
			// ALPHA FADE
			// ====================================================

			/*
			 * Tail starts nearly transparent.
			 *
			 * Head becomes fully visible.
			 */
			float fadeA =
					progressA
							* progressA;


			float fadeB =
					progressB
							* progressB;


			int alphaA =
					clampColor(
							(int) (
									255.0F
											* fadeA
											* alphaMultiplier
							)
					);


			int alphaB =
					clampColor(
							(int) (
									255.0F
											* fadeB
											* alphaMultiplier
							)
					);


			// ====================================================
			// WIDTH TAPER
			// ====================================================

			float widthA =
					width
							* (
							0.15F
									+ 0.85F
									* progressA
					);


			float widthB =
					width
							* (
							0.15F
									+ 0.85F
									* progressB
					);


			// ====================================================
			// CAMERA-FACING RIBBON
			// ====================================================

			Vec3 midpoint =
					worldA
							.add(
									worldB
							)
							.scale(
									0.5D
							);


			Vec3 toCamera =
					cameraPosition.subtract(
							midpoint
					);


			Vec3 segmentDirection =
					segment.normalize();


			Vec3 side =
					segmentDirection.cross(
							toCamera.normalize()
					);


			/*
			 * Camera is almost directly along the segment.
			 */
			if (
					side.lengthSqr()
							< 0.000001D
			) {

				side =
						segmentDirection.cross(
								new Vec3(
										0.0D,
										1.0D,
										0.0D
								)
						);
			}


			/*
			 * Vertical segment fallback.
			 */
			if (
					side.lengthSqr()
							< 0.000001D
			) {

				side =
						segmentDirection.cross(
								new Vec3(
										1.0D,
										0.0D,
										0.0D
								)
						);
			}


			if (
					side.lengthSqr()
							< 0.000001D
			) {

				continue;
			}


			side =
					side.normalize();


			Vec3 sideA =
					side.scale(
							widthA
					);


			Vec3 sideB =
					side.scale(
							widthB
					);


			// ====================================================
			// BUILD CORNERS
			// ====================================================

			/*
			 * EntityRenderer already renders relative to the entity,
			 * so convert world positions into entity-local positions.
			 */

			Vec3 aLeft =
					worldA
							.add(
									sideA
							)
							.subtract(
									entityOrigin
							);


			Vec3 aRight =
					worldA
							.subtract(
									sideA
							)
							.subtract(
									entityOrigin
							);


			Vec3 bLeft =
					worldB
							.add(
									sideB
							)
							.subtract(
									entityOrigin
							);


			Vec3 bRight =
					worldB
							.subtract(
									sideB
							)
							.subtract(
									entityOrigin
							);


			// ====================================================
			// FRONT FACE
			// ====================================================

			trailVertex(
					consumer,
					pose,

					aLeft,

					red,
					green,
					blue,
					alphaA,

					0.0F,
					0.0F
			);


			trailVertex(
					consumer,
					pose,

					aRight,

					red,
					green,
					blue,
					alphaA,

					0.0F,
					1.0F
			);


			trailVertex(
					consumer,
					pose,

					bRight,

					red,
					green,
					blue,
					alphaB,

					1.0F,
					1.0F
			);


			trailVertex(
					consumer,
					pose,

					bLeft,

					red,
					green,
					blue,
					alphaB,

					1.0F,
					0.0F
			);


			// ====================================================
			// BACK FACE
			// ====================================================

			/*
			 * Render opposite winding too.
			 *
			 * This guarantees the ribbon remains visible regardless
			 * of culling / camera direction.
			 */

			trailVertex(
					consumer,
					pose,

					bLeft,

					red,
					green,
					blue,
					alphaB,

					1.0F,
					0.0F
			);


			trailVertex(
					consumer,
					pose,

					bRight,

					red,
					green,
					blue,
					alphaB,

					1.0F,
					1.0F
			);


			trailVertex(
					consumer,
					pose,

					aRight,

					red,
					green,
					blue,
					alphaA,

					0.0F,
					1.0F
			);


			trailVertex(
					consumer,
					pose,

					aLeft,

					red,
					green,
					blue,
					alphaA,

					0.0F,
					0.0F
			);
		}
	}


// ============================================================
// TRAIL VERTEX
// ============================================================

	private void trailVertex(
			VertexConsumer consumer,
			PoseStack.Pose pose,
			Vec3 position,
			int red,
			int green,
			int blue,
			int alpha,
			float u,
			float v
	) {

		consumer
				.addVertex(
						pose.pose(),

						(float) position.x,
						(float) position.y,
						(float) position.z
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

				/*
				 * FULLBRIGHT.
				 */
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
	// MODEL RENDER
	// ============================================================

	private void renderModel(
			PoseStack poseStack,
			VertexConsumer vertexConsumer,
			int packedLight
	) {

		model.renderToBuffer(
				poseStack,
				vertexConsumer,

				/*
				 * Fullbright Ultima.
				 */
				0xF000F0,

				OverlayTexture.NO_OVERLAY,

				0xFFFFFFFF
		);
	}


	// ============================================================
	// PHASE AGE
	// ============================================================

	private float getPhaseAge(
			UltimaEntity entity,
			float partialTicks
	) {

		float age =
				entity.tickCount
						+ partialTicks
						- entity.getPhaseStartTick();


		return Math.max(
				0.0F,
				age
		);
	}


	// ============================================================
	// SMOOTHSTEP
	// ============================================================

	private float smoothStep(
			float value
	) {

		value =
				clamp01(
						value
				);


		return value
				* value
				* (
				3.0F
						- 2.0F
						* value
		);
	}


	// ============================================================
	// CLAMP
	// ============================================================

	private float clamp01(
			float value
	) {

		return Math.max(
				0.0F,
				Math.min(
						1.0F,
						value
				)
		);
	}


	private int clampColor(
			int value
	) {

		return Math.max(
				0,
				Math.min(
						255,
						value
				)
		);
	}


	// ============================================================
	// LERP
	// ============================================================

	private float lerp(
			float start,
			float end,
			float progress
	) {

		return start
				+ (
				end - start
		)
				* progress;
	}


	private double lerpDouble(
			double start,
			double end,
			float progress
	) {

		return start
				+ (
				end - start
		)
				* progress;
	}


	// ============================================================
	// TRAIL STATE
	// ============================================================

	private static class UltimaTrailState {


		/*
		 * We allocate six trails because expansion uses six.
		 *
		 * Flying and implosion simply use the first three.
		 */
		private final List<ArrayDeque<Vec3>> trails =
				new ArrayList<>();


		private int lastTick =
				Integer.MIN_VALUE;


		private int lastPhase =
				-1;


		private UltimaTrailState() {

			for (
					int i = 0;
					i < EXPANSION_TRAIL_COUNT;
					i++
			) {

				trails.add(
						new ArrayDeque<>()
				);
			}
		}


		private void clear() {

			for (
					ArrayDeque<Vec3> trail : trails
			) {

				trail.clear();
			}
		}
	}


	// ============================================================
	// CULLING
	// ============================================================

	@Override
	public boolean shouldRender(
			UltimaEntity entity,
			Frustum camera,
			double camX,
			double camY,
			double camZ
	) {

		/*
		 * Ultima and its trails can extend far outside its normal
		 * entity bounding box.
		 */
		return true;
	}


	// ============================================================
	// TEXTURE
	// ============================================================

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(
			UltimaEntity entity
	) {

		return TEXTURE;
	}
}