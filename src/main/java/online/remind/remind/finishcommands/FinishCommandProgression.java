package online.remind.remind.finishcommands;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowOverlayPacket;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.finishcommands.FinishCommandDefinition.ProgressType;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.network.stc.SCFinishCommandDataPacket;

import java.util.*;

/** All changes must be made on the logical server. */
public final class FinishCommandProgression {
    private FinishCommandProgression() {}

    private static final Set<UUID> DIRTY = new HashSet<>();

    /** Explicit award hook for gameplay systems (Command Points, Styles, special challenges, etc.). */
    public static void awardProgress(ServerPlayer player, ProgressType type, int amount) {
        awardProgress(player, type, amount, null);
    }

    /**
     * Context distinguishes styles and special challenges. Examples:
     * STYLE_USES / "firestorm"; STYLE_USES / "thunderbolt";
     * STYLE_USES / "diamond_dust"; STYLE_USES / "frozen_fortune";
     * SPECIAL / "survive_lethal".
     */



    private static void sendCommandRewardOverlay(
            ServerPlayer player,
            FinishCommandDefinition command
    ) {
        List<String> lines = new ArrayList<>();

        lines.add("Finish Command Unlocked!");
        lines.add(command.name());

        if (!FinishCommandExecutor.isImplemented(command.id())) {
            lines.add("Attack Coming Soon!");
        }

        player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                ModSounds.levelup.get(),
                SoundSource.MASTER,
                0.5F,
                1.0F
        );

        PacketHandler.sendTo(
                new SCShowOverlayPacket(
                        "levelup",
                        player.getUUID(),
                        player.getName().getString(),
                        1,
                        0xff00f6,
                        lines
                ),
                player
        );
    }



    public static void awardProgress(ServerPlayer player, ProgressType type, int amount, String context) {
        if (player == null || type == null || type == ProgressType.NONE || amount <= 0) return;
        GlobalDataRM data = ModDataRM.getGlobal(player);
        if (data == null) return;

        String equipped = data.getEquippedFinishCommand();
        if (!FinishCommandRegistry.isRegistered(equipped) || !data.hasFinishCommand(equipped)) {
            return;
        }
        boolean changed = false;
        for (FinishCommandDefinition definition : FinishCommandRegistry.all()) {
            if (data.hasFinishCommand(definition.id()) || definition.progressType() != type) continue;
            if (!equipped.equals(definition.parentId())) continue;
            if (!qualifies(definition.id(), type, context)) continue;

            int needed = definition.requiredProgress();
            if (needed <= 0) continue;
            int old = data.getFinishCommandProgress(definition.id());
            if (old >= needed) continue;
            data.addFinishCommandProgress(definition.id(), Math.min(amount, needed - old));
            changed = true;

            if (data.getFinishCommandProgress(definition.id()) >= needed && data.unlockFinishCommand(definition.id())) {
                sendCommandRewardOverlay(player, definition);
                sync(player);
            }
        }
        if (changed) DIRTY.add(player.getUUID());
    }

    private static boolean qualifies(String id, ProgressType type, String context) {
        if (type == ProgressType.STYLE_USES) {
            if (context == null) return false;
            return switch (id) {
                case "heat_slash_1", "heat_slash_2" -> "firestorm".equals(context);
                case "ramuhs_judgement" -> "thunderbolt".equals(context);
                case "ice_burst" -> "diamond_dust".equals(context) || "frozen_fortune".equals(context);
                default -> false;
            };
        }
        if (type == ProgressType.SPECIAL) {
            return "heal_strike".equals(id) && "survive_lethal".equals(context);
        }
        return true;
    }

    public static void sync(ServerPlayer player) {
        GlobalDataRM data = ModDataRM.getGlobal(player);
        if (data == null) return;
        PacketHandlerRM.sendTo(new SCFinishCommandDataPacket(
                data.getEquippedFinishCommand(),
                data.getUnlockedFinishCommands(),
                data.getFinishCommandProgressMap()), player);
        DIRTY.remove(player.getUUID());
    }

    /** Call every 20 server ticks, NOT on every point gained. */
    public static void flushIfDirty(ServerPlayer player) {
        if (DIRTY.contains(player.getUUID())) sync(player);
    }

    public static void onLeave(Player player) {
        DIRTY.remove(player.getUUID());
    }
}
