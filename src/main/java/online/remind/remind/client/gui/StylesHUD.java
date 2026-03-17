package online.remind.remind.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.client.CommandMenuEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.HUD.CMElement;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.ClientUtilsRM;
import online.remind.remind.dreameater.DreamEater;
import online.remind.remind.dreameater.ModDreamEaters;
import online.remind.remind.driveform.ModDriveFormsRM;
import online.remind.remind.entity.spirits.BaseDreamEaterEntity;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class StylesHUD {
    private static final ResourceLocation STYLES_TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/gui/styles_menu.png");

    @SubscribeEvent
    public void onSubmenuRender(CommandMenuEvent.SubmenuRender event) {
        event.setCanceled(event.getSubMenu().getId().equals(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"root")));

        GuiGraphics guiGraphics = event.getGuiGraphics();
        guiGraphics.setColor(event.getSubMenu().getColour().getRed() / 255F, event.getSubMenu().getColour().getGreen() / 255F, event.getSubMenu().getColour().getBlue() / 255F, 1);
        guiGraphics.blit(event.getSubMenu().getTexture(), event.getSubMenu().getX(), event.getSubMenu().getY(), 0, 70, 74, 15);
        PlayerData playerData = PlayerData.get(Minecraft.getInstance().player);
        IGlobalDataRM globalData = ModDataRM.getGlobal(Minecraft.getInstance().player);
        if (playerData == null || globalData == null)
            return;

        float color = 1F; //Reset the color
        RenderSystem.setShaderColor(color,color,color, 1F);

        int uMax = 67;
        double val = globalData.getSituationValue() * uMax / 100; //TODO change that hardcoded 100 if u increase it eventually

        PoseStack poseStack = guiGraphics.pose();

        String form = playerData.getActiveDriveForm();
        DriveForm driveForm = ModDriveFormsRM.DRIVE_FORMS.getRegistry().get().get(ResourceLocation.parse(form));

        poseStack.pushPose();
        {
            poseStack.translate(2, 2, 0); //Position for the bar
            guiGraphics.blit(STYLES_TEXTURE, 0, 0, 0, 0, (int) val, 11);
        }
        poseStack.popPose();

        guiGraphics.setColor(event.getSubMenu().getColour().getRed() / 255F, event.getSubMenu().getColour().getGreen() / 255F, event.getSubMenu().getColour().getBlue() / 255F, 1);
        Component title = event.getSubMenu().getTitle();
        Set<String> styles = new HashSet<>();
        styles.add(ModDriveFormsRM.FIRESTORM.get().getRegistryName().toString());
        styles.add(ModDriveFormsRM.DIAMOND_DUST.get().getRegistryName().toString());
        styles.add(ModDriveFormsRM.THUNDER_BOLT.get().getRegistryName().toString());

        if(styles.contains(form)){
            title = Component.literal(driveForm.getTranslationKey());
        }

        if (online.kingdomkeys.kingdomkeys.config.ModConfigs.cmHeaderTextVisible) {
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, title, event.getSubMenu().getX() + ((event.getSubMenu().getWidth() - 8) / 2) + 1, event.getSubMenu().getY() + 4, 0xFFFFFF);
        }

        RenderSystem.setShaderColor(1,1,1,1F);

    }
}