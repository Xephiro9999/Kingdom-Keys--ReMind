package online.remind.remind.finishcommands;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static online.remind.remind.finishcommands.FinishCommandDefinition.ProgressType.*;

/**
 * Combined normal Birth by Sleep Finish Command tree.
 * All 28 commands descend from the shared starting Finish.
 * Requirements are based on KHWiki's normal Finish command list.
 * STYLE_USES and SPECIAL need additional per-command event validation in progression logic.
 * Single parent IDs represent one valid route where BBS allows alternatives.
 */
public final class FinishCommandRegistry {
    private static final Map<String, FinishCommandDefinition> COMMANDS = new LinkedHashMap<>();

    private FinishCommandRegistry() {}

    private static void register(String id, String name, String description,
                                 int level, float row, String parent, int required,
                                 FinishCommandDefinition.ProgressType progressType) {
        if (COMMANDS.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate Finish Command: " + id);
        }
        if (parent != null && !COMMANDS.containsKey(parent)) {
            throw new IllegalArgumentException("Unknown Finish Command parent " + parent + " for " + id);
        }
        COMMANDS.put(id, new FinishCommandDefinition(id, name, description,
                level, row, parent, required, progressType));
    }

    static {
        // LEVEL 1: shared starting command
        register("finish", "Finish", "Perform a basic Finish Command, effects change based on your starting Path.",
                1, .50f, null, 0, NONE);

        // LEVEL 2: all five branches originate from Finish
        register("rising_rock_1", "Rising Rock 1", "Strike with a spinning and jumping attack.",
                2, .10f, "finish", 2000, COMMAND_POINTS);
        register("air_flair_1", "Air Flair 1", "Launch foes with a series of wind-powered blows.",
                2, .30f, "finish", 2000, COMMAND_POINTS);
        register("magic_pulse_1", "Magic Pulse 1", "Send magical orbs upward, confusing nearby foes.",
                2, .50f, "finish", 2000, COMMAND_POINTS);
        register("heat_slash_1", "Heat Slash 1", "Spin through opponents with flames.",
                2, .70f, "finish", 8, STYLE_USES); // Firestorm activations
        register("gold_rush", "Gold Rush", "A jumping strike that may make enemies drop munny.",
                2, .90f, "finish", 1000, MUNNY);

        // LEVEL 3
        register("rising_rock_2", "Rising Rock 2", "Finish a spinning assault with rocks erupting beneath foes.",
                3, .08f, "rising_rock_1", 4200, COMMAND_POINTS);
        register("air_flair_2", "Air Flair 2", "Chain wind attacks into a spinning strike.",
                3, .23f, "air_flair_1", 4000, COMMAND_POINTS);
        register("magic_pulse_2", "Magic Pulse 2", "Magic orbs can confuse or freeze targets.",
                3, .38f, "magic_pulse_1", 3800, COMMAND_POINTS);
        register("heat_slash_2", "Heat Slash 2", "A flaming jumping attack followed by a timed spin.",
                3, .53f, "heat_slash_1", 12, STYLE_USES); // Firestorm activations
        register("ramuhs_judgement", "Ramuh's Judgement", "Unleash electrified strikes and a timed follow-up jump.",
                3, .68f, "rising_rock_1", 12, STYLE_USES); // Thunderbolt; alternatively Air Flair 1 or Magic Pulse 1
        register("twisted_hours", "Twisted Hours", "Repeated Keyblade throws that may inflict Stop or Slow.",
                3, .82f, "gold_rush", 7000, STEPS); // alternatively Rising Rock 1 / Air Flair 1 / Magic Pulse 1
        register("surprise_1", "Surprise! 1", "A leaping finish that awards prizes for timed input.",
                3, .96f, "gold_rush", 1400, MUNNY);

        // LEVEL 4
        register("dark_star_1", "Dark Star 1", "Summon a meteor after successive spinning and jumping blows.",
                4, .10f, "rising_rock_2", 420, ENEMY_KILLS);
        register("air_flair_3", "Air Flair 3", "An extended sequence of aerial wind and spin attacks.",
                4, .27f, "air_flair_2", 4500, STEPS);
        register("magic_pulse_3", "Magic Pulse 3", "Conjure magic orbs with several debilitating effects.",
                4, .44f, "magic_pulse_2", 350, ENEMY_KILLS);
        register("heal_strike", "Heal Strike", "Chain charging and spinning attacks that restore HP.",
                4, .61f, "rising_rock_2", 5, SPECIAL); // survive lethal damage; alternatively Air Flair 2 / Magic Pulse 2
        register("random_end", "Random End", "Timed Keyblade throws and strikes that inflict status effects.",
                4, .78f, "twisted_hours", 8000, STEPS);
        register("surprise_2", "Surprise! 2", "An enhanced prize-generating finish with three timed inputs.",
                4, .95f, "surprise_1", 5200, MUNNY); // alternatively Twisted Hours

        // LEVEL 5
        register("dark_star_2", "Dark Star 2", "Rain multiple meteors over enemies after a close-range assault.",
                5, .10f, "dark_star_1", 550, ENEMY_KILLS);
        register("air_flair_4", "Air Flair 4", "A powerful wind finisher that always stuns foes.",
                5, .27f, "air_flair_3", 7000, STEPS);
        register("magic_pulse_4", "Magic Pulse 4", "Timed volleys of magical orbs apply disabling effects.",
                5, .44f, "magic_pulse_3", 500, ENEMY_KILLS);
        register("explosion", "Explosion", "A jumping strike creates an enormous stunning explosion.",
                5, .61f, "dark_star_1", 6400, COMMAND_POINTS); // alternatively Air Flair 3 / Magic Pulse 3
        register("celebration", "Celebration", "Finish by showering the battlefield with prizes and munny.",
                5, .78f, "surprise_2", 7000, MUNNY);
        register("ice_burst", "Ice Burst", "Spin and release repeated Blizzard blasts.",
                5, .95f, "magic_pulse_3", 15, STYLE_USES); // Diamond Dust or Frozen Fortune

        // LEVEL 6
        register("demolition", "Demolition", "Direct meteor impacts and explosive blasts onto enemies.",
                6, .20f, "dark_star_2", 10000, COMMAND_POINTS);
        register("stratosphere", "Stratosphere", "Use wind-powered dives before an immense storm finish.",
                6, .50f, "air_flair_4", 800, ENEMY_KILLS);
        register("teleport_spike", "Teleport Spike", "Teleport among magic orbs, firing them at enemies.",
                6, .80f, "magic_pulse_4", 800, ENEMY_KILLS);
    }

    public static FinishCommandDefinition get(String id) {
        return COMMANDS.get(id);
    }

    public static boolean isRegistered(String id) {
        return COMMANDS.containsKey(id);
    }

    public static Collection<FinishCommandDefinition> all() {
        return Collections.unmodifiableCollection(COMMANDS.values());
    }
}
