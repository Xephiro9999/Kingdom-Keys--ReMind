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

    public PanelData(
            ResourceLocation id,
            PanelType type,
            int width,
            int height,
            int strength,
            int magic,
            int defense,
            int ap,
            int levelBonus
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
}