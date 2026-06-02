package online.remind.remind.panels;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PanelGrid {

    private final int width;
    private final int height;
    private final List<PanelSlot> placedPanels = new ArrayList<>();

    public PanelGrid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean canPlace(ResourceLocation panelId, int x, int y) {
        PanelData data = PanelRegistry.get(panelId);

        if (data == null) {
            return false;
        }

        if (x < 0 || y < 0) {
            return false;
        }

        if (x + data.getWidth() > width || y + data.getHeight() > height) {
            return false;
        }

        for (PanelSlot existing : placedPanels) {
            PanelData existingData = PanelRegistry.get(existing.getPanelId());

            if (existingData == null) {
                continue;
            }

            if (rectsOverlap(
                    x,
                    y,
                    data.getWidth(),
                    data.getHeight(),
                    existing.getX(),
                    existing.getY(),
                    existingData.getWidth(),
                    existingData.getHeight()
            )) {
                return false;
            }
        }

        return true;
    }

    public boolean place(ResourceLocation panelId, int x, int y) {
        if (!canPlace(panelId, x, y)) {
            return false;
        }

        placedPanels.add(new PanelSlot(panelId, x, y));
        return true;
    }

    public boolean removeAt(int x, int y) {
        Iterator<PanelSlot> iterator = placedPanels.iterator();

        while (iterator.hasNext()) {
            PanelSlot slot = iterator.next();
            PanelData data = PanelRegistry.get(slot.getPanelId());

            if (data == null) {
                continue;
            }

            if (x >= slot.getX()
                    && x < slot.getX() + data.getWidth()
                    && y >= slot.getY()
                    && y < slot.getY() + data.getHeight()) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    public PanelSlot getAt(int x, int y) {
        for (PanelSlot slot : placedPanels) {
            PanelData data = PanelRegistry.get(slot.getPanelId());

            if (data == null) {
                continue;
            }

            if (x >= slot.getX()
                    && x < slot.getX() + data.getWidth()
                    && y >= slot.getY()
                    && y < slot.getY() + data.getHeight()) {
                return slot;
            }
        }

        return null;
    }

    public PanelStats calculateStats() {
        PanelStats stats = new PanelStats();

        for (PanelSlot slot : placedPanels) {
            PanelData data = PanelRegistry.get(slot.getPanelId());

            if (data != null) {
                stats.add(data);
            }
        }

        return stats;
    }

    public List<PanelSlot> getPlacedPanels() {
        return placedPanels;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        tag.putInt("Width", width);
        tag.putInt("Height", height);

        for (PanelSlot slot : placedPanels) {
            list.add(slot.save());
        }

        tag.put("PlacedPanels", list);

        return tag;
    }

    public static PanelGrid load(CompoundTag tag) {
        int width = tag.contains("Width") ? tag.getInt("Width") : 5;
        int height = tag.contains("Height") ? tag.getInt("Height") : 4;

        PanelGrid grid = new PanelGrid(width, height);

        ListTag list = tag.getList("PlacedPanels", Tag.TAG_COMPOUND);

        for (int i = 0; i < list.size(); i++) {
            PanelSlot slot = PanelSlot.load(list.getCompound(i));

            if (slot != null) {
                grid.placedPanels.add(slot);
            }
        }

        return grid;
    }

    private boolean rectsOverlap(
            int ax,
            int ay,
            int aw,
            int ah,
            int bx,
            int by,
            int bw,
            int bh
    ) {
        return ax < bx + bw
                && ax + aw > bx
                && ay < by + bh
                && ay + ah > by;
    }
}