package online.remind.remind.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.ClientUtilsRM;
import online.remind.remind.dreameater.DreamEater;
import online.remind.remind.dreameater.ModDreamEaters;
import online.remind.remind.entity.spirits.BaseDreamEaterEntity;

import java.awt.*;

public class StylesHUD extends OverlayBaseRM {
    private static final ResourceLocation STYLES_TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/gui/styles_menu.png");

    public static final StylesHUD INSTANCE = new StylesHUD();

    private StylesHUD() {
        super();
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        super.render(guiGraphics, deltaTracker);
        Player player = minecraft.player;
        IGlobalDataRM globalData = ModDataRM.getGlobal(player);
        if (globalData == null)
            return;

        /*if (ModConfigs.cmHeaderTextVisible)
            ModConfigs.setCmHeaderTextVisible(false);
        */

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        if (CommandMenuGui.INSTANCE.currentSubmenu.equals(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"root"))) {
            int uMax = 63;
            double val = globalData.getSituationValue() * uMax / 100; //TODO change that hardcoded 100 if u increase it eventually

            PoseStack poseStack = guiGraphics.pose();

            poseStack.pushPose();
            {
                float color = 0.7F;
                RenderSystem.setShaderColor(color,color,color, 1F);
                ClientUtils.CM_ELEMENT.applyTransform(guiGraphics, screenWidth, screenHeight);
                poseStack.translate(2, 2, 0);
                guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/gui/styles_menu.png"), 0, 0, 0, 0, (int) val, 11);
                guiGraphics.drawString(minecraft.font, "a", 1, 1, Color.RED.getRGB());

                ClientUtils.CM_ELEMENT.endTransform(guiGraphics);
            }
            poseStack.popPose();
        }
    }
}