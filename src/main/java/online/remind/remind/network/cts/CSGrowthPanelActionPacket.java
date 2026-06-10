package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetAerialDodgeTicksPacket;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.network.GrowthPanelAction;
import online.remind.remind.panels.OrganizationPanelAbilityHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CSGrowthPanelActionPacket implements CustomPacketPayload {

    public static final Type<CSGrowthPanelActionPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_growth_panel_action"));

    public static final StreamCodec<FriendlyByteBuf, CSGrowthPanelActionPacket> STREAM_CODEC =
            StreamCodec.of(CSGrowthPanelActionPacket::encode, CSGrowthPanelActionPacket::decode);

    private static final Set<java.util.UUID> USED_AERIAL_DODGE = new HashSet<>();
    private static final Map<java.util.UUID, Integer> QUICK_RUN_COOLDOWNS = new HashMap<>();
    private static final Map<java.util.UUID, Integer> DODGE_ROLL_COOLDOWNS = new HashMap<>();

    private GrowthPanelAction action;

    public CSGrowthPanelActionPacket() {
    }

    private static final Set<java.util.UUID> GLIDING_PLAYERS = new HashSet<>();

    public CSGrowthPanelActionPacket(GrowthPanelAction action) {
        this.action = action;
    }

    private static void encode(FriendlyByteBuf buf, CSGrowthPanelActionPacket packet) {
        buf.writeEnum(packet.action);
    }

    private static CSGrowthPanelActionPacket decode(FriendlyByteBuf buf) {
        return new CSGrowthPanelActionPacket(buf.readEnum(GrowthPanelAction.class));
    }

    public static void handle(final CSGrowthPanelActionPacket packet, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();

            if (!(player instanceof ServerPlayer serverPlayer)) {
                return;
            }

            if (packet.action == null) {
                return;
            }

            switch (packet.action) {
                case AERIAL_DODGE -> handleAerialDodge(serverPlayer);
                //case QUICK_RUN -> handleQuickRun(serverPlayer);
                //case DODGE_ROLL -> handleDodgeRoll(serverPlayer);
                case GLIDE_START -> handleGlideStart(serverPlayer);
                case GLIDE_STOP -> handleGlideStop(serverPlayer);
            }
        });
    }

    private static void handleAerialDodge(ServerPlayer player) {
        int aerialDodgePanels = OrganizationPanelAbilityHelper.getAerialDodgePanelLevel(player);

        if (aerialDodgePanels <= 0) {
            return;
        }

        if (player.onGround()) {
            return;
        }

        if (USED_AERIAL_DODGE.contains(player.getUUID())) {
            return;
        }

        USED_AERIAL_DODGE.add(player.getUUID());

        int index = Math.max(
                0,
                Math.min(aerialDodgePanels - 1, DriveForm.MASTER_AERIAL_DODGE_BOOST.length - 1)
        );

        float boost = DriveForm.MASTER_AERIAL_DODGE_BOOST[index];

        Vec3 current = player.getDeltaMovement();

        /*
         * KK-style Aerial Dodge, but panel-safe:
         * - always gives upward lift
         * - scales with panel count
         * - keeps some horizontal momentum
         */
        double horizontalScale = boost;
        double upward = 0.45D + (0.12D * (index + 1));

        player.setDeltaMovement(
                current.x * horizontalScale,
                upward,
                current.z * horizontalScale
        );

        player.hurtMarked = true;
        player.hasImpulse = true;
        player.fallDistance = 0.0F;
    }

    private static void handleQuickRun(ServerPlayer player) {
        if (!OrganizationPanelAbilityHelper.hasAbilityPanelEquipped(player, Strings.quickRun)) {
            return;
        }

        if (!player.onGround()) {
            return;
        }

        int cooldown = QUICK_RUN_COOLDOWNS.getOrDefault(player.getUUID(), 0);

        if (cooldown > 0) {
            return;
        }

        Vec3 look = player.getLookAngle();
        double speed = 1.45D;

        player.setDeltaMovement(
                look.x * speed,
                0.05D,
                look.z * speed
        );

        player.hurtMarked = true;
        player.fallDistance = 0.0F;

        QUICK_RUN_COOLDOWNS.put(player.getUUID(), 12);
    }

    private static void handleDodgeRoll(ServerPlayer player) {
        if (!OrganizationPanelAbilityHelper.hasAbilityPanelEquipped(player, Strings.dodgeRoll)) {
            return;
        }

        if (!player.onGround()) {
            return;
        }

        int cooldown = DODGE_ROLL_COOLDOWNS.getOrDefault(player.getUUID(), 0);

        if (cooldown > 0) {
            return;
        }

        Vec3 look = player.getLookAngle();
        double speed = 0.95D;

        player.setDeltaMovement(
                look.x * speed,
                0.08D,
                look.z * speed
        );

        player.hurtMarked = true;
        player.fallDistance = 0.0F;

        /*
         * Optional tiny safety window.
         * Remove this if you do not want roll i-frames.
         */
        player.invulnerableTime = Math.max(player.invulnerableTime, 8);

        DODGE_ROLL_COOLDOWNS.put(player.getUUID(), 10);
    }

    private static void handleGlideStart(ServerPlayer player) {
        if (player.onGround()) {
            GLIDING_PLAYERS.remove(player.getUUID());
            return;
        }

        if (!OrganizationPanelAbilityHelper.hasAbilityPanelEquipped(player, Strings.glide)) {
            GLIDING_PLAYERS.remove(player.getUUID());
            return;
        }

        GLIDING_PLAYERS.add(player.getUUID());
    }

    private static void handleGlideStop(ServerPlayer player) {
        GLIDING_PLAYERS.remove(player.getUUID());

        if (player.getForcedPose() == Pose.SWIMMING) {
            player.setForcedPose(null);
        }
    }

    public static boolean isGliding(Player player) {
        return player != null && GLIDING_PLAYERS.contains(player.getUUID());
    }

    public static void stopGliding(Player player) {
        if (player != null) {
            GLIDING_PLAYERS.remove(player.getUUID());

            if (player.getForcedPose() == Pose.SWIMMING) {
                player.setForcedPose(null);
            }
        }
    }

    public static void tick(ServerPlayer player) {
        java.util.UUID uuid = player.getUUID();

        if (player.onGround()) {
            USED_AERIAL_DODGE.remove(uuid);
            stopGliding(player);
        }

        tickCooldown(QUICK_RUN_COOLDOWNS, uuid);
        tickCooldown(DODGE_ROLL_COOLDOWNS, uuid);

        handleGlideTick(player);
    }

    private static void handleGlideTick(ServerPlayer player) {
        if (!OrganizationPanelAbilityHelper.isOrganizationPanelSystemActive(player)) {
            stopGliding(player);
            return;
        }

        if (!isGliding(player)) {
            if (player.getForcedPose() == Pose.SWIMMING) {
                player.setForcedPose(null);
            }

            return;
        }

        int glidePanels = OrganizationPanelAbilityHelper.getGlidePanelLevel(player);

        if (glidePanels <= 0) {
            stopGliding(player);
            return;
        }

        int glideIndex = Math.max(
                0,
                Math.min(glidePanels - 1, DriveForm.FINAL_GLIDE.length - 1)
        );

        float glide = DriveForm.FINAL_GLIDE[glideIndex];

        double rawLimit = DriveForm.FINAL_GLIDE_SPEED[glideIndex];

        /*
         * Make Glide 1 usable, but prevent higher levels from getting crazy.
         */
        double minLimit = 0.40D;
        double maxLimit = 0.80D;

        double limit = Math.max(rawLimit, minLimit);
        limit = Math.min(limit, maxLimit);

        /*
         * Keep acceleration mostly stable.
         * Higher levels should feel better from speed limit, not from launching instantly.
         */
        double accelFactor = 0.085D + (0.010D * glideIndex);
        accelFactor = Math.min(accelFactor, 0.12D);

        /*
         * Server-safe glide movement.
         *
         * Do not use player.zza/player.xxa here, because this is running
         * server-side and those input values are often 0.
         */
        float yaw = player.getYRot();
        float rad = (float) Math.toRadians(yaw);

        double moveX = -Math.sin(rad);
        double moveZ = Math.cos(rad);

        Vec3 current = player.getDeltaMovement();

        double targetX = moveX * limit;
        double targetZ = moveZ * limit;

        double xSpeed = current.x + (targetX - current.x) * accelFactor;
        double zSpeed = current.z + (targetZ - current.z) * accelFactor;

        double ySpeed = current.y;

        if (current.y < glide) {
            ySpeed = glide;
        }

        System.out.println(accelFactor);

        player.setDeltaMovement(new Vec3(xSpeed, ySpeed, zSpeed));
        player.fallDistance = 0.0F;
        player.hurtMarked = true;
        player.hasImpulse = true;

        if (player.getForcedPose() != Pose.SWIMMING) {
            player.setForcedPose(Pose.SWIMMING);
        }
    }

    private static void tickCooldown(Map<java.util.UUID, Integer> map, java.util.UUID uuid) {
        int current = map.getOrDefault(uuid, 0);

        if (current <= 0) {
            map.remove(uuid);
            return;
        }

        map.put(uuid, current - 1);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}