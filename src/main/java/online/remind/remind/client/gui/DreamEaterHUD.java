package online.remind.remind.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.dreameater.DreamEater;
import online.remind.remind.dreameater.ModDreamEaters;

public class DreamEaterHUD extends OverlayBaseRM {

    public static final DreamEaterHUD INSTANCE = new DreamEaterHUD();

    private DreamEaterHUD() {
        super();
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        super.render(guiGraphics, deltaTracker);
        Player player = minecraft.player;
        IGlobalDataRM globalData = ModDataRM.getGlobal(player);
        if(globalData == null)
            return;
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        float scale = 0.5f;

        PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();
        {
            poseStack.translate(ModConfigs.partyXPos, ModConfigs.partyYPos - 100, 0);

            //DreamEater dreameater = ModDreamEaters.registry.get(ResourceLocation.parse(globalData.getDreamEaterRL()));
            /*for (int i = 0; i < allies.size(); i++) {
                Player playerAlly = player.level().getPlayerByUUID(member.getUUID());
                renderFace(guiGraphics, playerAlly, screenWidth, screenHeight, scale, i);
            }*/
        }
        poseStack.popPose();
    }
}

