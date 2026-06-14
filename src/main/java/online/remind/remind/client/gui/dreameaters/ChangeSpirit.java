package online.remind.remind.client.gui.dreameaters;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.gui.DreamEaterMenu;
import online.remind.remind.client.sound.MusicManager;
import online.remind.remind.dreameater.DreamEater;
import online.remind.remind.dreameater.ModDreamEaters;
import online.remind.remind.entity.ModEntitiesRM;
import online.remind.remind.entity.spirits.ChirithyEntity;
import online.remind.remind.entity.spirits.KomoryBatEntity;
import online.remind.remind.entity.spirits.MeowWowEntity;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.network.cts.CSChangeSpiritPacket;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ChangeSpirit extends MenuBackground {

    public ChangeSpirit(String name, Color rgb) {
        super(name, rgb);
    }

    private LivingEntity previewDreamEaterEntity;
    private String previewDreamEaterKey = "";

    GlobalDataRM globalData;

    public ChangeSpirit() {
        super("Change Spirit", new Color(241, 115, 24));
        minecraft = Minecraft.getInstance();
    }

    public void onClose() {
        super.onClose();

        Screen next = Minecraft.getInstance().screen;

        if (!(next instanceof DreamEaterMenu || next instanceof ChangeSpirit || next instanceof CreateSpirit)) {
            MusicManager.stop();
        }
    }

    public void reloadMenu() {
        minecraft.setScreen(new ChangeSpirit());
    }

    protected void select(String rl) {
        globalData.setDreamEaterRL(rl);
        PacketHandlerRM.sendToServer(new CSChangeSpiritPacket(rl));
        PacketHandlerRM.syncGlobalToAllAround(minecraft.player, globalData);
        reloadMenu();
    }

    protected void action(String string) {
        if (globalData == null)
            return;

        if (string.equals("back")) {
            minecraft.setScreen(new DreamEaterMenu());
        }
    }

    @Override
    public void init() {
        super.init();
        this.renderables.clear();

        float topBarHeight = (float) height * 0.17F;
        int button_statsY = (int) topBarHeight + 5;
        int button_stats_playerY = button_statsY;

        float buttonPosX = (float) width * 0.03F;
        float subButtonPosX = buttonPosX + 10;

        float buttonWidth = ((float) width * 0.1744F) - 20;

        float dataWidth = ((float) width * 0.1744F) - 10;

        int col1X = (int) (subButtonPosX + buttonWidth + 40), col2X = (int) (col1X + dataWidth * 2) + 10;

        int i = 0;

        globalData = ModDataRM.getGlobal(minecraft.player);

        for (i = 0; i < ModDreamEaters.registry.stream().toList().size(); i++) {
            DreamEater dreamEater = ModDreamEaters.registry.stream().toList().get(i);
            MenuButton btn = new MenuButton((int) buttonPosX, button_statsY + 18 * i, (int) buttonWidth, dreamEater.getTranslationKey(), MenuButton.ButtonType.BUTTON, false, (e) -> {
                select(dreamEater.getRegistryName().toString());
            });
            btn.setData(dreamEater.getRegistryName().toString());
            addRenderableWidget(btn);
        }

        addRenderableWidget(new MenuButton((int) buttonPosX, button_statsY + 18 * i++, (int) buttonWidth, (Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, false, (e) -> {
            action("back");
        }));
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        if (globalData == null)
            return;

        //Read every button data and if it's the selected one render it in gold
        for (Renderable renderable : renderables) {
            if (renderable instanceof MenuButton btn && btn.getData() != null && !btn.getData().isEmpty()) {
                if (btn.getData().equals(globalData.getDreamEaterRL())) {
                    btn.setMessage(Component.literal(ChatFormatting.GOLD + btn.getMessage().getString()));
                }
            }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderDreamEaterPreview(guiGraphics, mouseX, mouseY);
    }

    private void renderDreamEaterPreview(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (minecraft == null || minecraft.player == null || minecraft.level == null) {
            return;
        }

        GlobalDataRM global = ModDataRM.getGlobal(minecraft.player);
        PlayerData playerData = PlayerData.get(minecraft.player);

        if (global == null || playerData == null) {
            return;
        }

        DreamEater dreamEater = getPreviewDreamEater(mouseX, mouseY);

        if (dreamEater == null || StringsRM.none.equals(dreamEater.getName())) {
            return;
        }

        LivingEntity entity = getOrCreatePreviewEntity(dreamEater, playerData);

        if (entity == null) {
            return;
        }

        int boxX = (int) (this.width * 0.23F);
        int boxY = (int) (this.height * 0.21F);
        int boxW = 130;
        int boxH = 145;

        guiGraphics.fill(boxX - 4, boxY - 16, boxX + boxW + 4, boxY + boxH + 4, 0xAA000000);
        guiGraphics.drawString(
                minecraft.font,
                getDreamEaterDisplayName(dreamEater),
                boxX + 8,
                boxY - 12,
                0xFFFFFF,
                false
        );

        int centerX = boxX + (boxW / 2);
        int bottomY = boxY + boxH - 10;
        int scale = getPreviewScale(dreamEater);

        entity.tickCount = minecraft.player.tickCount;

        InventoryScreen.renderEntityInInventoryFollowsMouse(
                guiGraphics,
                boxX,
                boxY,
                boxX + boxW,
                boxY + boxH,
                scale,
                0.25F,
                mouseX,
                mouseY,
                entity
        );
    }

    private DreamEater getPreviewDreamEater(int mouseX, int mouseY) {
        String hoveredRL = getHoveredDreamEaterRL(mouseX, mouseY);

        if (hoveredRL != null && !hoveredRL.isEmpty()) {
            DreamEater hovered = getDreamEaterFromRL(hoveredRL);

            if (hovered != null) {
                return hovered;
            }
        }

        if (globalData == null || globalData.getDreamEaterRL() == null || globalData.getDreamEaterRL().isEmpty()) {
            return null;
        }

        return getDreamEaterFromRL(globalData.getDreamEaterRL());
    }

    private String getHoveredDreamEaterRL(int mouseX, int mouseY) {
        for (Renderable renderable : this.renderables) {
            if (renderable instanceof MenuButton btn) {
                if (btn.getData() == null || btn.getData().isEmpty()) {
                    continue;
                }

                if (btn.isMouseOver(mouseX, mouseY)) {
                    return btn.getData();
                }
            }
        }

        return null;
    }

    private DreamEater getDreamEaterFromRL(String rl) {
        if (rl == null || rl.isEmpty()) {
            return null;
        }

        try {
            return ModDreamEaters.registry.get(ResourceLocation.parse(rl));
        } catch (Exception e) {
            return null;
        }
    }

    private String getDreamEaterDisplayName(DreamEater dreamEater) {
        if (dreamEater == null) {
            return "N/A";
        }

        if (StringsRM.chirithy.equals(dreamEater.getName())) {
            return "Chirithy";
        }

        if (StringsRM.meowWow.equals(dreamEater.getName())) {
            return "Meow Wow";
        }

        if (StringsRM.komoryBat.equals(dreamEater.getName())) {
            return "Komory Bat";
        }

        return Utils.translateToLocal(dreamEater.getTranslationKey());
    }

    private LivingEntity getOrCreatePreviewEntity(DreamEater dreamEater, PlayerData playerData) {
        if (minecraft == null || minecraft.level == null || minecraft.player == null || dreamEater == null) {
            return null;
        }

        String key = dreamEater.getName() + ":" + playerData.getAlignment();

        if (previewDreamEaterEntity != null && key.equals(previewDreamEaterKey)) {
            return previewDreamEaterEntity;
        }

        previewDreamEaterKey = key;
        previewDreamEaterEntity = null;

        boolean isOrg = playerData.getAlignment() != Utils.OrgMember.NONE;

        if (StringsRM.chirithy.equals(dreamEater.getName())) {
            ChirithyEntity chirithy = new ChirithyEntity(ModEntitiesRM.TYPE_CHIRITHY.get(), minecraft.level);
            chirithy.setOwnerUUID(minecraft.player.getUUID());

            // Match your summon packet variant behavior for Chirithy.
            chirithy.setVariant(isOrg ? 0 : 1);

            previewDreamEaterEntity = chirithy;
            return previewDreamEaterEntity;
        }

        if (StringsRM.meowWow.equals(dreamEater.getName())) {
            MeowWowEntity meowWow = new MeowWowEntity(ModEntitiesRM.TYPE_MEOW_WOW.get(), minecraft.level);
            meowWow.setOwnerUUID(minecraft.player.getUUID());
            meowWow.setVariant(isOrg ? MeowWowEntity.VARIANT_ORG : MeowWowEntity.VARIANT_NORMAL);

            previewDreamEaterEntity = meowWow;
            return previewDreamEaterEntity;
        }

        if (StringsRM.komoryBat.equals(dreamEater.getName())) {
            KomoryBatEntity komoryBat = new KomoryBatEntity(ModEntitiesRM.TYPE_KOMORY_BAT.get(), minecraft.level);
            komoryBat.setOwnerUUID(minecraft.player.getUUID());
            komoryBat.setVariant(isOrg ? KomoryBatEntity.VARIANT_ORG : KomoryBatEntity.VARIANT_NORMAL);
            komoryBat.setNoGravity(true);

            previewDreamEaterEntity = komoryBat;
            return previewDreamEaterEntity;
        }

        return null;
    }

    private int getPreviewScale(DreamEater dreamEater) {
        if (dreamEater == null) {
            return 35;
        }

        if (StringsRM.komoryBat.equals(dreamEater.getName())) {
            return 65;
        }

        if (StringsRM.meowWow.equals(dreamEater.getName())) {
            return 42;
        }

        if (StringsRM.chirithy.equals(dreamEater.getName())) {
            return 48;
        }

        return 40;
    }


}
