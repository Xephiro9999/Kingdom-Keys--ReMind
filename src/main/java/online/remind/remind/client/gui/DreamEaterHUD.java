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
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.ClientUtilsRM;
import online.remind.remind.dreameater.DreamEater;
import online.remind.remind.dreameater.ModDreamEaters;
import online.remind.remind.entity.spirits.BaseDreamEaterEntity;

public class DreamEaterHUD extends OverlayBaseRM {
    int dreamEaterClientID;

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

        if(!globalData.hasDreamEaterSummoned())
            return;

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        float scale = 1f;

        PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();
        {
            poseStack.translate(ModConfigs.partyXPos-70, ModConfigs.partyYPos - 20, 0);
            DreamEater dreamEater = ModDreamEaters.registry.get(ResourceLocation.parse(globalData.getDreamEaterRL()));
            renderDreamEater(guiGraphics, dreamEater, screenWidth, screenHeight, scale);
        }
        poseStack.popPose();
    }

    private void renderDreamEater(GuiGraphics gui, DreamEater dreamEater, int screenWidth, int screenHeight, float scale) {
        Player player = minecraft.player;
        IGlobalDataRM globalData = ModDataRM.getGlobal(player);
        PlayerData playerData = PlayerData.get(player);

        //Get the actual entity from the id which is updated by the packet
        if (globalData == null || playerData == null)
            return;

        Entity entity = ClientUtilsRM.getEntityByUUIDClient(globalData.getDreamEaterUUID());
        if (entity != null && entity instanceof BaseDreamEaterEntity dreamEaterEntity) {
            boolean isOrg = playerData.getAlignment() != Utils.OrgMember.NONE;

            int variant = isOrg ? 1 : 0;
            ResourceLocation skin = ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "textures/entity/models/mobs/icons/" + dreamEater.getName() + variant + ".png");

            int headWidth = 32;
            int headHeight = 32;
            PoseStack matrixStack = gui.pose();

            //Face
            matrixStack.pushPose();
            {
                matrixStack.translate(screenWidth - headWidth, screenHeight - headHeight, 0);
                matrixStack.scale(scale, scale, scale);
                this.blit(gui, skin, 0, 0, 0, 0, headWidth, headHeight);
            }
            matrixStack.popPose();

            scale = 0.5F;
            matrixStack.translate(screenWidth - headWidth, screenHeight - headHeight, 0);

            //Name
            matrixStack.pushPose();
            {
                matrixStack.scale(scale, scale, scale);
                String name = Utils.translateToLocal(dreamEater.getTranslationKey());
                // name = "Chirithy";
                drawCenteredString(gui, minecraft.font, name, 16, -10, 0xFFFFFF);
            }
            matrixStack.popPose();

            //HP
            //TODO get the entity here
            float val = 80;
            float max = 100;
            val = dreamEaterEntity.getHealth();
            max = dreamEaterEntity.getMaxHealth();
            ResourceLocation hptexture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hpbar.png");
            matrixStack.translate(-4, 0, 1);
            // Top
            matrixStack.pushPose();
            {
                matrixStack.scale(scale / 3 * 2, scale, 1);
                this.blit(gui, hptexture, 0, 0, 0, 72, 12, 2);
            }
            matrixStack.popPose();
            // Middle
            matrixStack.pushPose();
            {
                matrixStack.translate(0, 1, 1);
                matrixStack.scale(scale / 3 * 2, scale * 28, 1);
                this.blit(gui, hptexture, 0, 0, 0, 74, 12, 1);
            }
            matrixStack.popPose();
            // Bottom
            matrixStack.pushPose();
            {
                matrixStack.translate(0, 30, 1);
                matrixStack.scale(scale / 3 * 2, scale, 1);
                this.blit(gui, hptexture, 0, -30, 0, 72, 12, 2);
            }
            matrixStack.popPose();

            // Bar
            matrixStack.pushPose();
            {
                matrixStack.mulPose(Axis.ZP.rotationDegrees(180));
                matrixStack.translate(-4, -15, 1);
                matrixStack.scale(scale * 0.66F, (scale * 28) * val / max, 1);
                this.blit(gui, hptexture, 0, 0, 0, 78, 12, 1);
            }
            matrixStack.popPose();
        }
    }
}

