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
        PlayerData playerData = PlayerData.get(player);
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

            String form = playerData.getActiveDriveForm();

            poseStack.pushPose();

            {
                float color = 0.7F;
                RenderSystem.setShaderColor(color,color,color, 1F);
                ClientUtils.CM_ELEMENT.applyTransform(guiGraphics, screenWidth, screenHeight);
                poseStack.translate(2, 2, 0);
                guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/gui/styles_menu.png"), 0, 0, 0, 0, (int) val, 11);
                switch (form){
                    default:
                        guiGraphics.drawCenteredString(minecraft.font, "", 1, 1, Color.RED.getRGB());
                        break;
                    case "kkremind:form_firestorm":
                        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/gui/styles_menu_firestorm.png"), 0, 0, 0, 0, (int) val, 11);
                        guiGraphics.drawCenteredString(minecraft.font, "FIRESTORM", 30, 2, Color.ORANGE.getRGB());

                        break;
                    case "kkremind:form_diamond_dust":
                        guiGraphics.pose().pushPose();
                            {
                                guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/gui/styles_menu_diamond_dust.png"), 0, 0, 0, 0, (int) val, 11);
                                guiGraphics.pose().scale(0.80f,1f,1f);
                                guiGraphics.drawCenteredString(minecraft.font, "DIAMOND DUST", 38, 2, Color.CYAN.getRGB());
                            }
                        guiGraphics.pose().popPose();
                        break;
                    case "kkremind:form_thunder_bolt":
                        guiGraphics.pose().pushPose();
                        {
                        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/gui/styles_menu_thunder_bolt.png"), 0, 0, 0, 0, (int) val, 11);
                        guiGraphics.pose().scale(0.80f,1f,1f);
                        guiGraphics.drawCenteredString(minecraft.font, "THUNDER BOLT", 37, 2, Color.YELLOW.getRGB());
                        }
                        guiGraphics.pose().popPose();
                        break;
                }


                ClientUtils.CM_ELEMENT.endTransform(guiGraphics);
            }
            poseStack.popPose();
        }
    }
}