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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean hasPanel(ResourceLocation panelId) {
        if (panelId == null) {
            return false;
        }

        for (PanelSlot slot : this.getPlacedPanels()) {
            if (slot != null && panelId.equals(slot.getPanelId())) {
                return true;
            }
        }

        return false;
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

    public PanelStats calculateStats() {
        PanelStats stats = new PanelStats();

        for (PanelSlot slot : placedPanels) {
            PanelData data = PanelRegistry.get(slot.getPanelId());

            if (data != null) {
                stats.add(data);
            }
        }

        applyLinkBonuses(stats);

        return stats;
    }

    private void applyLinkBonuses(PanelStats stats) {
        for (PanelSlot slot : placedPanels) {
            PanelData data = PanelRegistry.get(slot.getPanelId());

            if (data == null || data.getType() != PanelType.LINK) {
                continue;
            }

            String path = slot.getPanelId().getPath();
            int adjacentMatchingPanels = countAdjacentMatchingPanels(slot, path);

            switch (path) {
                case "power_link" -> stats.addStrength(adjacentMatchingPanels);
                case "magic_link" -> stats.addMagic(adjacentMatchingPanels);
                case "guard_link" -> stats.addDefense(adjacentMatchingPanels);
                case "level_link" -> stats.addLevelBonus(adjacentMatchingPanels);
            }
        }
    }

    private int countAdjacentMatchingPanels(PanelSlot linkSlot, String linkPath) {
        int count = 0;

        for (PanelSlot other : placedPanels) {
            if (other == linkSlot) {
                continue;
            }

            PanelData otherData = PanelRegistry.get(other.getPanelId());

            if (otherData == null) {
                continue;
            }

            if (!isAdjacent(linkSlot, other)) {
                continue;
            }

            switch (linkPath) {
                case "power_link" -> {
                    if (otherData.getType() == PanelType.STRENGTH) {
                        count++;
                    }
                }
                case "magic_link" -> {
                    if (otherData.getType() == PanelType.MAGIC) {
                        count++;
                    }
                }
                case "guard_link" -> {
                    if (otherData.getType() == PanelType.DEFENSE) {
                        count++;
                    }
                }
                case "level_link" -> {
                    if (otherData.getType() == PanelType.LEVEL) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private boolean isAdjacent(PanelSlot a, PanelSlot b) {
        PanelData aData = PanelRegistry.get(a.getPanelId());
        PanelData bData = PanelRegistry.get(b.getPanelId());

        if (aData == null || bData == null) {
            return false;
        }

        int ax1 = a.getX();
        int ay1 = a.getY();
        int ax2 = a.getX() + aData.getWidth() - 1;
        int ay2 = a.getY() + aData.getHeight() - 1;

        int bx1 = b.getX();
        int by1 = b.getY();
        int bx2 = b.getX() + bData.getWidth() - 1;
        int by2 = b.getY() + bData.getHeight() - 1;

        boolean touchingHorizontally = ax2 + 1 == bx1 || bx2 + 1 == ax1;
        boolean yRangesOverlap = ay1 <= by2 && ay2 >= by1;

        boolean touchingVertically = ay2 + 1 == by1 || by2 + 1 == ay1;
        boolean xRangesOverlap = ax1 <= bx2 && ax2 >= bx1;

        return touchingHorizontally && yRangesOverlap
                || touchingVertically && xRangesOverlap;
    }
}