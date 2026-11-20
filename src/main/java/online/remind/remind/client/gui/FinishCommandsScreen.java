package online.remind.remind.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.MenuItemsScreen;
import online.remind.remind.finishcommands.FinishCommandDefinition;
import online.remind.remind.finishcommands.FinishCommandRegistry;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.network.cts.CSFinishCommandEquipPacket;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class FinishCommandsScreen extends MenuBackground {

    private static final int NODE_HEIGHT = 23;
    private static final int COLUMN_GAP = 14;

    private final List<FinishCommandDefinition> entries = new ArrayList<>();

    // Selection is client-side only; equipped command comes from GlobalDataRM.
    private String selectedId = "finish";

    private int scrollX = 0;
    private int maxScrollX = 0;

    private int viewLeft, viewTop, viewRight, viewBottom;
    private int columnWidth;
    private int columnStep;
    private int maxLevel = 1;

    public FinishCommandsScreen() {
        super("Finish Commands", new Color(0, 0, 255));
    }


    private GlobalDataRM getPlayerFinishData() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return null;

        return ModDataRM.getGlobal(mc.player);
    }

    @Override
    public void init() {
        super.init();
        this.renderables.clear();

        initEntries();
        calculateLayout();

        // Keep Back outside the progression tree.
        addRenderableWidget(new MenuButton(
                (int) buttonPosX,
                buttonPosY,
                (int) buttonWidth,
                "Back",
                MenuButton.ButtonType.BUTTON,
                true,
                b -> Minecraft.getInstance()
                        .setScreen(new MenuItemsScreen())
        ));
    }

    private void initEntries() {
        entries.clear();

        entries.addAll(FinishCommandRegistry.all());

        maxLevel = entries.stream()
                .mapToInt(FinishCommandDefinition::level)
                .max()
                .orElse(1);
    }


    private void calculateLayout() {
        viewLeft = 16;
        viewRight = width - 16;

        // Below the KK header, above its bottom information panel.
        viewTop = Math.max(48, (int) (height * 0.22f));
        viewBottom = Math.max(viewTop + 90,
                (int) (height * 0.76f));

        // At least 125 GUI pixels per column.
        int availableWidth = viewRight - viewLeft;

        columnWidth = Math.max(125, (availableWidth - 80) / 3);
        columnStep = columnWidth + COLUMN_GAP;

        int contentWidth = maxLevel * columnStep + 14;

        maxScrollX = Math.max(0, contentWidth - availableWidth);
        scrollX = Math.max(0, Math.min(scrollX, maxScrollX));
    }

    private FinishCommandDefinition getEntry(String id) {
        if (id == null) return null;

        for (FinishCommandDefinition entry : entries) {
            if (entry.id().equals(id)) return entry;
        }
        return null;
    }

    private int nodeX(FinishCommandDefinition entry) {
        return viewLeft + 12
                + (entry.level() - 1) * columnStep
                - scrollX;
    }

    private int nodeY(FinishCommandDefinition entry) {
        int top = viewTop + 32;
        int usableHeight = Math.max(20,
                viewBottom - top - NODE_HEIGHT - 10);

        return top + (int) (entry.rowPosition() * usableHeight);
    }

    private void setScroll(int amount) {
        scrollX = Math.max(0, Math.min(maxScrollX, amount));
    }

    private void ensureVisible(FinishCommandDefinition entry) {
        int left = nodeX(entry);
        int right = left + columnWidth;

        if (left < viewLeft + 6) {
            setScroll(scrollX - (viewLeft + 6 - left));
        } else if (right > viewRight - 6) {
            setScroll(scrollX + right - (viewRight - 6));
        }
    }

    private void toggleEquip(FinishCommandDefinition entry) {
        if (entry == null) return;
        selectedId = entry.id();

        GlobalDataRM data = getPlayerFinishData();
        if (data == null || !data.hasFinishCommand(entry.id())) return;

        // No client-side data mutation. The server validates and synchronizes.
        // Re-selecting the equipped finisher returns to the default Finish.
        String target = entry.id().equals(data.getEquippedFinishCommand())
                ? "finish" : entry.id();
        PacketHandlerRM.sendToServer(new CSFinishCommandEquipPacket(target));
    }

    @Override
    public void render(
            @NotNull GuiGraphics gui,
            int mouseX,
            int mouseY,
            float partialTicks
    ) {
        super.render(gui, mouseX, mouseY, partialTicks);

        calculateLayout();

        renderTreeBackground(gui);

        // Prevent content from drawing outside the tree area.
        gui.enableScissor(
                viewLeft, viewTop,
                viewRight, viewBottom
        );

        renderColumns(gui);
        renderConnections(gui);
        renderNodes(gui, mouseX, mouseY);

        gui.disableScissor();

        renderScrollIndicators(gui);
        renderDescription(gui);
    }

    private void renderTreeBackground(GuiGraphics gui) {
        gui.fill(
                viewLeft, viewTop,
                viewRight, viewBottom,
                0xDD030512
        );
    }

    private void renderColumns(GuiGraphics gui) {
        for (int level = 1; level <= maxLevel; level++) {
            int x = viewLeft + 12
                    + (level - 1) * columnStep - scrollX;

            int right = x + columnWidth;

            gui.fill(
                    x, viewTop + 12,
                    right, viewBottom - 8,
                    level % 2 == 0
                            ? 0x33100000
                            : 0x22101430
            );

            gui.fill(
                    x, viewTop + 9,
                    right, viewTop + 28,
                    0xAA850000
            );

            gui.drawString(
                    font,
                    "LV" + level,
                    x + 8, viewTop + 14,
                    0xFFFFFFFF,
                    false
            );
        }
    }

    private void renderConnections(GuiGraphics gui) {
        for (FinishCommandDefinition entry : entries) {
            FinishCommandDefinition parent = getEntry(entry.parentId());
            if (parent == null) continue;

            int x1 = nodeX(parent) + columnWidth;
            int y1 = nodeY(parent) + NODE_HEIGHT / 2;

            int x2 = nodeX(entry);
            int y2 = nodeY(entry) + NODE_HEIGHT / 2;

            drawConnection(gui, x1, y1, x2, y2,
                    0xFFB00000);
        }
    }

    private void drawConnection(
            GuiGraphics gui,
            int x1, int y1,
            int x2, int y2,
            int color
    ) {
        if (x2 <= x1) return;

        int mid = (x1 + x2) / 2;

        gui.fill(x1, y1, mid, y1 + 2, color);

        gui.fill(
                mid,
                Math.min(y1, y2),
                mid + 2,
                Math.max(y1, y2) + 2,
                color
        );

        gui.fill(mid, y2, x2, y2 + 2, color);
    }

    private void renderNodes(
            GuiGraphics gui, int mouseX, int mouseY
    ) {
        GlobalDataRM data = getPlayerFinishData();
        if (data == null) return;
        for (FinishCommandDefinition entry : entries) {
            int x = nodeX(entry);
            int y = nodeY(entry);

            boolean selected = entry.id().equals(selectedId);
            boolean unlocked = data.hasFinishCommand(entry.id());
            boolean equipped = entry.id().equals(data.getEquippedFinishCommand());

            boolean hovered = insideNode(
                    entry, mouseX, mouseY
            );

            int background;

            if (!unlocked) {
                background = 0xFF50505A;
            } else if (equipped) {
                background = 0xFFB89500;
            } else {
                background = 0xFF8C3100;
            }

            if (selected || hovered) {
                gui.fill(
                        x - 2, y - 2,
                        x + columnWidth + 2,
                        y + NODE_HEIGHT + 2,
                        0xFF30CFFF
                );
            }

            gui.fill(
                    x, y,
                    x + columnWidth, y + NODE_HEIGHT,
                    background
            );

            // Highlight strip
            gui.fill(
                    x, y,
                    x + columnWidth, y + 2,
                    equipped ? 0xFFFFFF55 : 0xFFBB5555
            );

            gui.drawString(
                    font,
                    unlocked ? entry.name() : "???",
                    x + 8, y + 7,
                    0xFFFFFFFF,
                    false
            );

            if (equipped) {
                gui.drawString(
                        font,
                        "[E]",
                        x + columnWidth - 27,
                        y + 7,
                        0xFFFFFF55,
                        false
                );
            } else if (!unlocked && entry.requiredProgress() > 0) {
                renderProgress(gui, entry, x, y, data.getFinishCommandProgress(entry.id()));
            }
        }
    }

    private void renderProgress(
            GuiGraphics gui,
            FinishCommandDefinition entry, int x, int y, int currentProgress
    ) {
        int barWidth = Math.min(42, columnWidth / 3);
        int barX = x + columnWidth - barWidth - 6;

        float fraction = Math.min(
                1.0f,
                Math.max(0.0f,
                        (float) currentProgress / entry.requiredProgress())
        );

        gui.fill(
                barX, y + 4,
                barX + barWidth, y + 7,
                0xFF151515
        );

        gui.fill(
                barX, y + 4,
                barX + (int) (barWidth * fraction), y + 7,
                0xFFFFFF00
        );

        String progress = Integer.toString(currentProgress);

        gui.drawString(
                font,
                progress,
                x + columnWidth - font.width(progress) - 7,
                y + 11,
                0xFFFFFFFF,
                false
        );
    }

    private void renderScrollIndicators(GuiGraphics gui) {
        if (maxScrollX <= 0) return;

        int y = viewBottom - 4;
        int left = viewLeft + 12;
        int right = viewRight - 12;
        int trackWidth = right - left;

        gui.fill(left, y, right, y + 2, 0xFF303050);

        int thumbWidth = Math.max(
                24,
                trackWidth * (viewRight - viewLeft)
                        / (viewRight - viewLeft + maxScrollX)
        );

        int thumbX = left
                + (trackWidth - thumbWidth) * scrollX
                / Math.max(1, maxScrollX);

        gui.fill(
                thumbX, y - 1,
                thumbX + thumbWidth, y + 3,
                0xFF30CFFF
        );
    }

    private void renderDescription(GuiGraphics gui) {
        FinishCommandDefinition selected = getEntry(selectedId);
        if (selected == null) return;
        GlobalDataRM data = getPlayerFinishData();
        if (data == null) return;
        boolean unlocked = data.hasFinishCommand(selected.id());

        int left = Math.max(160, (int) (width * 0.21f));
        int top = viewBottom + 10;
        int right = width - 16;
        int bottom = height - 12;

        if (bottom <= top + 16 || right <= left + 20) return;

        gui.fill(left, top, right, bottom, 0xBB000B38);

        gui.drawString(
                font,
                unlocked ? selected.name() : "???",
                left + 8, top + 6,
                0xFFFFFF55,
                false
        );

        gui.drawWordWrap(
                font,
                Component.literal(selected.description()),
                left + 8,
                top + 20,
                right - left - 16,
                0xFFB8EFFF
        );

        String status = !unlocked
                ? "LOCKED"
                : selected.id().equals(data.getEquippedFinishCommand())
                ? "EQUIPPED"
                : "AVAILABLE";

        gui.drawString(
                font,
                status,
                right - font.width(status) - 8,
                top + 6,
                0xFFFFFFFF,
                false
        );
    }

    private boolean insideNode(
            FinishCommandDefinition entry, double mouseX, double mouseY
    ) {
        int x = nodeX(entry);
        int y = nodeY(entry);

        return mouseX >= Math.max(x, viewLeft)
                && mouseX < Math.min(x + columnWidth, viewRight)
                && mouseY >= Math.max(y, viewTop)
                && mouseY < Math.min(y + NODE_HEIGHT, viewBottom);
    }

    @Override
    public boolean mouseClicked(
            double mouseX, double mouseY, int button
    ) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            for (FinishCommandDefinition entry : entries) {
                if (insideNode(entry, mouseX, mouseY)) {
                    selectedId = entry.id();
                    toggleEquip(entry);
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(
            double mouseX, double mouseY,
            double scrollXDelta, double scrollYDelta
    ) {
        if (mouseX >= viewLeft && mouseX < viewRight
                && mouseY >= viewTop && mouseY < viewBottom) {
            setScroll(scrollX - (int) (scrollYDelta * 35));
            return true;
        }

        return super.mouseScrolled(
                mouseX, mouseY, scrollXDelta, scrollYDelta
        );
    }

    // Horizontal arrow keys select the closest node
    // in the next or previous level.
    private void moveHorizontal(int direction) {
        FinishCommandDefinition selected = getEntry(selectedId);
        if (selected == null) return;

        FinishCommandDefinition nearest = null;
        float bestDistance = Float.MAX_VALUE;
        int targetLevel = selected.level() + direction;

        for (FinishCommandDefinition entry : entries) {
            if (entry.level() != targetLevel) continue;

            float distance = Math.abs(
                    entry.rowPosition() - selected.rowPosition()
            );

            if (distance < bestDistance) {
                bestDistance = distance;
                nearest = entry;
            }
        }

        if (nearest != null) {
            selectedId = nearest.id();
            ensureVisible(nearest);
        }
    }

    private void moveVertical(int direction) {
        FinishCommandDefinition selected = getEntry(selectedId);
        if (selected == null) return;

        FinishCommandDefinition nearest = null;
        float bestDistance = Float.MAX_VALUE;

        for (FinishCommandDefinition entry : entries) {
            if (entry.level() != selected.level()) continue;

            float difference =
                    entry.rowPosition() - selected.rowPosition();

            if (direction < 0 && difference >= 0) continue;
            if (direction > 0 && difference <= 0) continue;

            float distance = Math.abs(difference);

            if (distance < bestDistance) {
                bestDistance = distance;
                nearest = entry;
            }
        }

        if (nearest != null) {
            selectedId = nearest.id();
        }
    }

    @Override
    public boolean keyPressed(
            int keyCode, int scanCode, int modifiers
    ) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_A -> {
                setScroll(scrollX - 60);
                return true;
            }
            case GLFW.GLFW_KEY_D -> {
                setScroll(scrollX + 60);
                return true;
            }
            case GLFW.GLFW_KEY_LEFT -> {
                moveHorizontal(-1);
                return true;
            }
            case GLFW.GLFW_KEY_RIGHT -> {
                moveHorizontal(1);
                return true;
            }
            case GLFW.GLFW_KEY_UP -> {
                moveVertical(-1);
                return true;
            }
            case GLFW.GLFW_KEY_DOWN -> {
                moveVertical(1);
                return true;
            }
            case GLFW.GLFW_KEY_ENTER,
                 GLFW.GLFW_KEY_KP_ENTER,
                 GLFW.GLFW_KEY_E -> {
                toggleEquip(getEntry(selectedId));
                return true;
            }
            case GLFW.GLFW_KEY_ESCAPE -> {
                Minecraft.getInstance()
                        .setScreen(new MenuItemsScreen());
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
