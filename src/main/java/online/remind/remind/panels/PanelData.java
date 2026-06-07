package online.remind.remind.panels;

import net.minecraft.resources.ResourceLocation;

public class PanelData {

    private final ResourceLocation id;
    private final PanelType type;
    private final int width;
    private final int height;

    private final int strength;
    private final int magic;
    private final int defense;
    private final int ap;
    private final int levelBonus;

    /*
     * shape = physical occupied cells.
     * These cells block placement.
     *
     * linkArea = special link cells.
     * These cells do NOT block placement.
     * They are used for effects like LV Doubler link zones.
     */
    private final boolean[][] shape;
    private final boolean[][] linkArea;

    public PanelData(
            ResourceLocation id,
            PanelType type,
            int width,
            int height,
            int strength,
            int magic,
            int defense,
            int ap,
            int levelBonus,
            boolean[][] shape,
            boolean[][] linkArea
    ) {
        this.id = id;
        this.type = type;
        this.width = width;
        this.height = height;
        this.strength = strength;
        this.magic = magic;
        this.defense = defense;
        this.ap = ap;
        this.levelBonus = levelBonus;
        this.shape = shape;
        this.linkArea = linkArea;
    }

    /*
     * Compatibility constructor.
     * Use this for shaped panels that do not have a link area.
     */
    public PanelData(
            ResourceLocation id,
            PanelType type,
            int width,
            int height,
            int strength,
            int magic,
            int defense,
            int ap,
            int levelBonus,
            boolean[][] shape
    ) {
        this(
                id,
                type,
                width,
                height,
                strength,
                magic,
                defense,
                ap,
                levelBonus,
                shape,
                null
        );
    }

    public ResourceLocation getId() {
        return id;
    }

    public PanelType getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStrength() {
        return strength;
    }

    public int getMagic() {
        return magic;
    }

    public int getDefense() {
        return defense;
    }

    public int getAp() {
        return ap;
    }

    public int getLevelBonus() {
        return levelBonus;
    }

    public boolean hasCustomShape() {
        return shape != null;
    }

    public boolean hasLinkArea() {
        return linkArea != null;
    }

    public boolean occupies(int localX, int localY) {
        if (localX < 0 || localY < 0 || localX >= width || localY >= height) {
            return false;
        }

        if (shape == null) {
            return true;
        }

        if (localY >= shape.length || localX >= shape[localY].length) {
            return false;
        }

        return shape[localY][localX];
    }

    public boolean linksAt(int localX, int localY) {
        if (localX < 0 || localY < 0 || localX >= width || localY >= height) {
            return false;
        }

        if (linkArea == null) {
            return false;
        }

        if (localY >= linkArea.length || localX >= linkArea[localY].length) {
            return false;
        }

        return linkArea[localY][localX];
    }
}