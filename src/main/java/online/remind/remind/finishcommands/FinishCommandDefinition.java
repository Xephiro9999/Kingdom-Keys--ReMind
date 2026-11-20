package online.remind.remind.finishcommands;

public record FinishCommandDefinition(
        String id,
        String name,
        String description,
        int level,
        float rowPosition,
        String parentId,
        int requiredProgress,
        ProgressType progressType
) {
    public enum ProgressType {
        NONE,
        COMMAND_POINTS,
        MUNNY,
        ENEMY_KILLS,
        STEPS,
        FINISHER_USES,
        STYLE_USES,
        SPECIAL
    }
}
