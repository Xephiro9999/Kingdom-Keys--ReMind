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

    public int countPanel(ResourceLocation panelId) {
        if (panelId == null) {
            return 0;
        }

        int count = 0;

        for (PanelSlot slot : this.getPlacedPanels()) {
            if (slot != null && panelId.equals(slot.getPanelId())) {
                count++;
            }
        }

        return count;
    }

    public boolean canPlace(ResourceLocation panelId, int x, int y) {
        PanelData data = PanelRegistry.get(panelId);

        if (data == null) {
            return false;
        }

        if (x < 0 || y < 0) {

            return false;
        }

        /*
         * Shape-aware bounds check.
         *
         * Old behavior treated every panel as a full width x height rectangle.
         * New behavior only treats data.occupies(localX, localY) cells as real cells.
         */
        for (int localY = 0; localY < data.getHeight(); localY++) {
            for (int localX = 0; localX < data.getWidth(); localX++) {
                if (!data.occupies(localX, localY)) {
                    continue;
                }

                int gridX = x + localX;
                int gridY = y + localY;

                if (gridX < 0 || gridY < 0 || gridX >= width || gridY >= height) {

                    return false;
                }
            }
        }

        /*
         * Shape-aware collision check.
         * Empty cells inside a shaped panel's bounding box no longer block placement.
         */
        for (PanelSlot existing : placedPanels) {
            PanelData existingData = PanelRegistry.get(existing.getPanelId());

            if (existingData == null) {
                continue;
            }

            if (occupiedCellsOverlap(
                    panelId,
                    x,
                    y,
                    data,
                    existing.getPanelId(),
                    existing.getX(),
                    existing.getY(),
                    existingData
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

            int localX = x - slot.getX();
            int localY = y - slot.getY();

            if (data.occupies(localX, localY)) {
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

            int localX = x - slot.getX();
            int localY = y - slot.getY();

            if (data.occupies(localX, localY)) {
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

    private boolean occupiedCellsOverlap(
            ResourceLocation incomingPanelId,
            int ax,
            int ay,
            PanelData incomingData,
            ResourceLocation existingPanelId,
            int bx,
            int by,
            PanelData existingData
    ) {
        for (int aLocalY = 0; aLocalY < incomingData.getHeight(); aLocalY++) {
            for (int aLocalX = 0; aLocalX < incomingData.getWidth(); aLocalX++) {
                if (!incomingData.occupies(aLocalX, aLocalY)) {
                    continue;
                }

                int aGridX = ax + aLocalX;
                int aGridY = ay + aLocalY;

                for (int bLocalY = 0; bLocalY < existingData.getHeight(); bLocalY++) {
                    for (int bLocalX = 0; bLocalX < existingData.getWidth(); bLocalX++) {
                        int bGridX = bx + bLocalX;
                        int bGridY = by + bLocalY;

                        if (aGridX != bGridX || aGridY != bGridY) {
                            continue;
                        }

                        /*
                         * Link cells are checked FIRST.
                         * They may still be occupied/body cells, but Level Up is allowed
                         * to overlap them when the existing panel is an LV Doubler.
                         */
                        if (existingData.linksAt(bLocalX, bLocalY)) {
                            return !canPlaceInsideLinkArea(incomingPanelId, existingPanelId);
                        }

                        /*
                         * Non-link occupied cells block everything.
                         */
                        if (existingData.occupies(bLocalX, bLocalY)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean canPlaceInsideLinkArea(ResourceLocation incomingPanelId, ResourceLocation existingPanelId) {
        return PanelRegistry.LEVEL_UP.equals(incomingPanelId)
                && isLevelDoubler(existingPanelId);
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
        applyLevelDoublerBonuses(stats);

        return stats;
    }

    private void applyLevelDoublerBonuses(PanelStats stats) {
        for (PanelSlot doublerSlot : placedPanels) {
            PanelData doublerData = PanelRegistry.get(doublerSlot.getPanelId());

            if (doublerData == null || !isLevelDoubler(doublerSlot.getPanelId())) {
                continue;
            }

            int linkedLevelUps = countLevelUpsInLinkArea(doublerSlot, doublerData);

            /*
             * LV Doubler adds +2 levels for each Level Up panel in its link area.
             * Level Up itself already adds +1, so linked total becomes +3.
             */
            stats.addLevelBonus(linkedLevelUps * 2);
        }
    }

    private int countAdjacentLevelUpPanels(PanelSlot doublerSlot) {
        int count = 0;

        for (PanelSlot other : placedPanels) {
            if (other == doublerSlot) {
                continue;
            }

            if (!PanelRegistry.LEVEL_UP.equals(other.getPanelId())) {
                continue;
            }

            if (isAdjacent(doublerSlot, other)) {
                count++;
            }
        }

        return count;
    }

    private boolean isLevelDoubler(ResourceLocation panelId) {
        return PanelRegistry.LEVEL_DOUBLER.equals(panelId)
                || PanelRegistry.LEVEL_DOUBLER_L_RIGHT.equals(panelId)
                || PanelRegistry.LEVEL_DOUBLER_L_LEFT.equals(panelId)
                || PanelRegistry.LEVEL_DOUBLER_L_TOP_RIGHT.equals(panelId)
                || PanelRegistry.LEVEL_DOUBLER_L_TOP_LEFT.equals(panelId)
                || PanelRegistry.LEVEL_DOUBLER_LINE.equals(panelId);
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

    private int countLevelUpsInLinkArea(PanelSlot doublerSlot, PanelData doublerData) {
        int count = 0;

        for (PanelSlot other : placedPanels) {
            if (other == doublerSlot) {
                continue;
            }

            if (!PanelRegistry.LEVEL_UP.equals(other.getPanelId())) {
                continue;
            }

            if (isPanelInsideLinkArea(doublerSlot, doublerData, other)) {
                count++;
            }
        }

        return count;
    }

    private boolean isPanelInsideLinkArea(
            PanelSlot doublerSlot,
            PanelData doublerData,
            PanelSlot targetSlot
    ) {
        PanelData targetData = PanelRegistry.get(targetSlot.getPanelId());

        if (targetData == null) {
            return false;
        }

        for (int targetLocalY = 0; targetLocalY < targetData.getHeight(); targetLocalY++) {
            for (int targetLocalX = 0; targetLocalX < targetData.getWidth(); targetLocalX++) {
                if (!targetData.occupies(targetLocalX, targetLocalY)) {
                    continue;
                }

                int targetGridX = targetSlot.getX() + targetLocalX;
                int targetGridY = targetSlot.getY() + targetLocalY;

                int localToDoublerX = targetGridX - doublerSlot.getX();
                int localToDoublerY = targetGridY - doublerSlot.getY();

                if (doublerData.linksAt(localToDoublerX, localToDoublerY)) {
                    return true;
                }
            }
        }

        return false;
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

        /*
         * Shape-aware adjacency.
         *
         * Two panels are adjacent when any occupied cell from panel A touches
         * any occupied cell from panel B on a cardinal side.
         */
        for (int aLocalY = 0; aLocalY < aData.getHeight(); aLocalY++) {
            for (int aLocalX = 0; aLocalX < aData.getWidth(); aLocalX++) {
                if (!aData.occupies(aLocalX, aLocalY)) {
                    continue;
                }

                int aGridX = a.getX() + aLocalX;
                int aGridY = a.getY() + aLocalY;

                for (int bLocalY = 0; bLocalY < bData.getHeight(); bLocalY++) {
                    for (int bLocalX = 0; bLocalX < bData.getWidth(); bLocalX++) {
                        if (!bData.occupies(bLocalX, bLocalY)) {
                            continue;
                        }

                        int bGridX = b.getX() + bLocalX;
                        int bGridY = b.getY() + bLocalY;

                        int distanceX = Math.abs(aGridX - bGridX);
                        int distanceY = Math.abs(aGridY - bGridY);

                        if (distanceX + distanceY == 1) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
