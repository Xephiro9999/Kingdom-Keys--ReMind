package online.remind.remind.panels;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class PanelSlot {

    private ResourceLocation panelId;
    private int x;
    private int y;

    public PanelSlot(ResourceLocation panelId, int x, int y) {
        this.panelId = panelId;
        this.x = x;
        this.y = y;
    }

    public ResourceLocation getPanelId() {
        return panelId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putString("PanelId", panelId.toString());
        tag.putInt("X", x);
        tag.putInt("Y", y);

        return tag;
    }

    public static PanelSlot load(CompoundTag tag) {
        ResourceLocation id = ResourceLocation.tryParse(tag.getString("PanelId"));

        if (id == null) {
            return null;
        }

        return new PanelSlot(
                id,
                tag.getInt("X"),
                tag.getInt("Y")
        );
    }
}