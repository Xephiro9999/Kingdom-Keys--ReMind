package online.remind.remind.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.Util;

public final class LimitBreakHud {

    private static String attackName = null;
    private static long startTime = 0L;

    // Total time the banner stays visible.
    private static final long DISPLAY_TIME = 1200L;

    // Quick FF-style appearance/disappearance.
    private static final long FADE_IN_TIME = 100L;
    private static final long FADE_OUT_TIME = 0L;

    private LimitBreakHud() {
    }

    public static void show(String name) {
        attackName = name;
        startTime = Util.getMillis();
    }

    public static void render(
            GuiGraphics graphics,
            DeltaTracker deltaTracker
    ) {
        if (attackName == null || attackName.isEmpty()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        long elapsed = Util.getMillis() - startTime;

        if (elapsed >= DISPLAY_TIME) {
            attackName = null;
            return;
        }

        float alpha = 1.0F;

        // Fade in.
        if (elapsed < FADE_IN_TIME) {
            alpha = elapsed / (float) FADE_IN_TIME;
        }

        // Fade out.
        if (elapsed > DISPLAY_TIME - FADE_OUT_TIME) {
            alpha = (DISPLAY_TIME - elapsed) / (float) FADE_OUT_TIME;
        }

        alpha = Mth.clamp(alpha, 0.0F, 1.0F);

        int screenWidth = graphics.guiWidth();

        // Screenshot is roughly 90% of the width.
        int barWidth = Math.min(
                (int) (screenWidth * 0.5F),
                570
        );

        int barHeight = 25;

        int x = (screenWidth - barWidth) / 2;
        int y = 7;

        int a = (int) (255 * alpha);

        /*
         * =========================
         * SHADOW
         * =========================
         */
        graphics.fill(
                x - 3,
                y - 2,
                x + barWidth + 3,
                y + barHeight + 3,
                argb((int) (150 * alpha), 0x000000)
        );

        /*
         * =========================
         * OUTER SILVER BORDER
         * =========================
         */
        graphics.fill(
                x - 2,
                y - 2,
                x + barWidth + 2,
                y + barHeight + 2,
                argb(a, 0xD7D7DF)
        );

        /*
         * Dark outline underneath.
         */
        graphics.fill(
                x,
                y,
                x + barWidth,
                y + barHeight,
                argb((int) (150 * alpha), 0x28202E)
        );

        /*
         * =========================
         * MAIN BAR
         * =========================
         */

        // Main FF7 reddish/purple section.
        graphics.fill(
                x + 2,
                y + 2,
                x + barWidth,
                y + barHeight,
                argb((int) (225 * alpha), 0xB43B62)
        );

        int leftWidth = Math.max(70, barWidth / 5);



        /*
         * Slight highlight across the top.
         */
        graphics.fill(
                x + 3,
                y + 3,
                x + barWidth - 3,
                y + 5,
                argb((int) (115 * alpha), 0xFFFFFF)
        );

        /*
         * Dark lower edge gives it that PS1 beveled look.
         */
        graphics.fill(
                x + 3,
                y + barHeight - 4,
                x + barWidth - 3,
                y + barHeight - 2,
                argb((int) (120 * alpha), 0x501C3D)
        );


        /*
         * =========================
         * ATTACK NAME
         * =========================
         */

        Font font = minecraft.font;

        int textWidth = font.width(attackName);

        int textX =
                x +
                        (barWidth / 2) -
                        (textWidth / 2);

        int textY =
                y +
                        (barHeight - font.lineHeight) / 2;

        graphics.drawString(
                font,
                attackName,
                textX,
                textY,
                argb(a, 0xFFFFFF),
                true
        );
    }

    private static int argb(int alpha, int rgb) {
        alpha = Mth.clamp(alpha, 0, 255);

        return (alpha << 24)
                | (rgb & 0xFFFFFF);
    }
}