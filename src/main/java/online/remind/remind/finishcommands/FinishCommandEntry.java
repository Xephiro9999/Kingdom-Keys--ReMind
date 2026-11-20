package online.remind.remind.finishcommands;

import online.remind.remind.finishcommands.FinishCommandDefinition;
import online.remind.remind.finishcommands.FinishCommandRegistry;

public class FinishCommandEntry {
    private final String id;
    private final String name;
    private final int level; // 1, 2, 3, etc.
    private final int x;
    private final int y;
    private final String description;
    private final boolean locked;
    private final boolean equipped;
    private final int progress;
    private final int requiredProgress;

    public FinishCommandEntry(String id, String name, int level, int x, int y,
                              String description, boolean locked, boolean equipped,
                              int progress, int requiredProgress) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.x = x;
        this.y = y;
        this.description = description;
        this.locked = locked;
        this.equipped = equipped;
        this.progress = progress;
        this.requiredProgress = requiredProgress;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getX() { return x; }
    public int getY() { return y; }
    public String getDescription() { return description; }
    public boolean isLocked() { return locked; }
    public boolean isEquipped() { return equipped; }
    public int getProgress() { return progress; }
    public int getRequiredProgress() { return requiredProgress; }
}
