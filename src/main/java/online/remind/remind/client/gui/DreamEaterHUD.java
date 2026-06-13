package online.remind.remind.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.ClientUtilsRM;
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

        if (player == null) {
            return;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(player);

        if (globalData == null) {
            return;
        }

        if (!globalData.hasDreamEaterSummoned()) {
            return;
        }

        if (globalData.getDreamEaterUUID() == null) {
            return;
        }

        String dreamEaterRL = globalData.getDreamEaterRL();

        if (dreamEaterRL == null || dreamEaterRL.isEmpty()) {
            return;
        }

        DreamEater dreamEater = ModDreamEaters.registry.get(ResourceLocation.parse(dreamEaterRL));

        if (dreamEater == null) {
            return;
        }

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        float scale = 1F;

        PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();
        {
            poseStack.translate(ClientUtils.PARTY_ELEMENT.x - 70, ClientUtils.PARTY_ELEMENT.y - 20, 0);
            renderDreamEater(guiGraphics, dreamEater, screenWidth, screenHeight, scale);
        }
        poseStack.popPose();
    }

    private void renderDreamEater(GuiGraphics gui, DreamEater dreamEater, int screenWidth, int screenHeight, float scale) {
        Player player = minecraft.player;

        if (player == null) {
            return;
        }

        GlobalDataRM globalData = ModDataRM.getGlobal(player);
        PlayerData playerData = PlayerData.get(player);

        if (globalData == null || playerData == null) {
            return;
        }

        if (globalData.getDreamEaterUUID() == null) {
            return;
        }

        Entity entity = ClientUtilsRM.getEntityByUUIDClient(globalData.getDreamEaterUUID());

        // Important:
        // MeowWowEntity is not BaseDreamEaterEntity right now.
        // It is still a LivingEntity, so use LivingEntity for the HP bar.
        if (!(entity instanceof LivingEntity dreamEaterEntity)) {
            return;
        }

        boolean isOrg = playerData.getAlignment() != Utils.OrgMember.NONE;
        int variant = isOrg ? 1 : 0;

        ResourceLocation skin = ResourceLocation.fromNamespaceAndPath(
                KingdomKeysReMind.MODID,
                "textures/entity/models/mobs/icons/" + dreamEater.getName() + variant + ".png"
        );

        int headWidth = 32;
        int headHeight = 32;

        PoseStack matrixStack = gui.pose();

        // Face
        matrixStack.pushPose();
        {
            matrixStack.translate(screenWidth - headWidth, screenHeight - headHeight, 0);
            matrixStack.scale(scale, scale, scale);
            this.blit(gui, skin, 0, 0, 0, 0, headWidth, headHeight);
        }
        matrixStack.popPose();

        scale = 0.5F;
        matrixStack.translate(screenWidth - headWidth, screenHeight - headHeight, 0);

        // Name
        matrixStack.pushPose();
        {
            matrixStack.scale(scale, scale, scale);
            String name = Utils.translateToLocal(dreamEater.getTranslationKey());
            drawCenteredString(gui, minecraft.font, name, 16, -10, 0xFFFFFF);
        }
        matrixStack.popPose();

        // HP
        float val = dreamEaterEntity.getHealth();
        float max = dreamEaterEntity.getMaxHealth();

        if (max <= 0F) {
            return;
        }

        val = Math.max(0F, Math.min(val, max));

        ResourceLocation hptexture = ResourceLocation.fromNamespaceAndPath(
                KingdomKeys.MODID,
                "textures/gui/hpbar.png"
        );

        matrixStack.translate(-4, 0, 1);

        // Top
        matrixStack.pushPose();
        {
            matrixStack.scale(scale / 3F * 2F, scale, 1);
            this.blit(gui, hptexture, 0, 0, 0, 72, 12, 2);
        }
        matrixStack.popPose();

        // Middle
        matrixStack.pushPose();
        {
            matrixStack.translate(0, 1, 1);
            matrixStack.scale(scale / 3F * 2F, scale * 28F, 1);
            this.blit(gui, hptexture, 0, 0, 0, 74, 12, 1);
        }
        matrixStack.popPose();

        // Bottom
        matrixStack.pushPose();
        {
            matrixStack.translate(0, 30, 1);
            matrixStack.scale(scale / 3F * 2F, scale, 1);
            this.blit(gui, hptexture, 0, -30, 0, 72, 12, 2);
        }
        matrixStack.popPose();

        // Bar
        matrixStack.pushPose();
        {
            matrixStack.mulPose(Axis.ZP.rotationDegrees(180));
            matrixStack.translate(-4, -15, 1);
            matrixStack.scale(scale * 0.66F, (scale * 28F) * val / max, 1);
            this.blit(gui, hptexture, 0, 0, 0, 78, 12, 1);
        }
        matrixStack.popPose();
    }
}