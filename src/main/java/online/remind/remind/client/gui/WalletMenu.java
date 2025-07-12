package online.remind.remind.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.IntPriorityQueue;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterable;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuStockItem;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.item.ModItemsRM;
import online.remind.remind.item.RMCoinItem;
import online.remind.remind.network.PacketHandlerRM;
import online.remind.remind.network.cts.CSTakeCoins;
import org.jetbrains.annotations.NotNull;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

public class WalletMenu extends MenuFilterable {

    MenuButton /*deposit,*/back;
    MenuButton take;
    MenuBox boxL, boxR;
    EditBox amountBox;

    WalletMenu parent;

    public WalletMenu(){
        super("Wallet", new Color(75,150,65));
        drawPlayerInfo = true;
    }


    @Override
    public void action(ResourceLocation stackRL, ItemStack stack) {
        super.action(stackRL, stack);
        amountBox.setValue(""+Math.min(64, stack.getCount()));
    }

    protected void action(String string) {
        PlayerData playerData = PlayerData.get(minecraft.player);
        switch(string) {
            // Needs reworking
            /*case "deposit":
                minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);

                LocalPlayer player = minecraft.player;
                try {
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack stack = player.getInventory().getItem(i);

                        if (!ItemStack.matches(stack, ItemStack.EMPTY)) {
                            if (stack.is(Tags.MUNNY)) {
                                //playerData.setMunny(playerData.getMunny() + (stack.getCount()));
                                System.out.println(stack);
                                System.out.println(stack.getCount());
                                System.out.println(stack.getItem());
                                System.out.println(stack.getItem().toString());


                                player.getInventory().setItem(i, ItemStack.EMPTY);
                            }
                        }
                    }
                } catch (ConcurrentModificationException e) {
                    e.printStackTrace();
                }
                //  PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
                //PacketHandler.sendToServer(new CSDepositMaterials(parent.invFile, parent.name, parent.moogle));
                break;*/
            case "back":
                GUIHelperRM.openAddonMenu();
                break;
            case "take":
                int amountSet = Integer.parseInt(amountBox.getValue());
                ItemStack selectedItemstack = new ItemStack(BuiltInRegistries.ITEM.get(selectedRL),amountSet);

                if(!ItemStack.isSameItem(selectedItemstack, ItemStack.EMPTY) && minecraft.player.getInventory().getFreeSlot() > -1) {
                    try {

                        //System.out.println(amountSet);
                        //System.out.println(amountBox.getValue());
                        //System.out.println(selectedItemstack);

                        /*if (selectedItemstack.getItem() instanceof RMCoinItem coin){
                            String type = coin.getCoinType();
                            int value = coin.getCoinValue();
                            if (Objects.equals(type, "munny")){
                                System.out.println(type + ": " + value);
                                playerData.setMunny(playerData.getMunny() - (value * amountSet));
                            }
                            else if (Objects.equals(type, "hearts") && playerData.getAlignment() != Utils.OrgMember.NONE){
                                System.out.println(type + ": " + value);
                                playerData.setHearts(playerData.getHearts() - (value * amountSet));
                            }
                        }*/
                        //System.out.println("Selected ItemStack: " + selectedItemstack);
                        //System.out.println("Selected Item: " + selectedItemstack.getItem());
                        //System.out.println("Amount from box: " + amountBox.getValue());

                        ItemStack copy = selectedItemstack.copy();
                        copy.setCount(Integer.parseInt(amountBox.getValue()));
                        PacketHandlerRM.sendToServer(new CSTakeCoins(copy));

                        //PacketHandlerRM.sendToServer(new CSTakeCoins(selectedItemstack));
                    } catch (NumberFormatException e) {
                        KingdomKeys.LOGGER.error("NaN "+amountBox.getValue());
                    }
                }
                break;
        }

    }

    @Override
    public void init() {
        float boxPosX = (float) width * 0.2F;
        float topBarHeight = (float) height * 0.17F;
        float boxWidth = (float) width * 0.33F;
        float middleHeight = (float) height * 0.6F;
        boxL = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight,1F,  new Color(40, 4, 255));
        boxR = new MenuBox(boxL.getX() + boxL.getWidth(), (int) topBarHeight, (int) (boxWidth), (int) middleHeight, 1F,new Color(69, 69, 69));

        scrollBar = new MenuScrollBar((int) (boxPosX + boxWidth - 17), boxL.getY(), boxL.getPosY()+boxL.getHeight(), (int) middleHeight, 0);
        addRenderableWidget(scrollBar);

        super.init();
        initItems();
    }

    @Override
    public void initItems() {
        float buttonPosX = (float) width * 0.008F;
        int button_statsY = (int) topBarHeight + 10;
        float buttonWidth = ((float) width * 0.1744F) - 20;

        float invPosX = boxL.getX();
        float invPosY = (float) height * 0.1851F;

        inventory.clear();
        children().clear();
        renderables.clear();
        //filterBar.buttons.forEach(this::addButton);

        List<ItemStack> items = new ArrayList<>();

        PlayerData playerData = PlayerData.get(minecraft.player);





        for (DeferredHolder<Item, ? extends Item> itemRegistryObject : ModItemsRM.ITEMS.getEntries()) {
            Item item = BuiltInRegistries.ITEM.get(itemRegistryObject.getKey());
            if (item instanceof RMCoinItem coin) {
                String type = coin.getCoinType();

                // Only show heart coins if alignment is NOT NONE
                if (Objects.equals(type, "hearts") && playerData.getAlignment() == Utils.OrgMember.NONE) {
                    continue;
                }

                ItemStack coinStack = new ItemStack(item, 1);
                items.add(coinStack);
            }
        }

        //items.sort(Comparator.comparing(Utils::getCategoryForStack).thenComparing(ItemStack::getDescriptionId));

        items.sort(Comparator.comparing((ItemStack stack) -> {
            Item item = stack.getItem();
            if (item instanceof RMCoinItem coin) {
                return coin.getCoinType().equals("munny") ? 0 : 1; // 'munny' first, 'hearts' after
            }
            return Integer.MAX_VALUE; // fallback if not an RMCoinItem
        }).thenComparing(stack -> {
            Item item = stack.getItem();
            if (item instanceof RMCoinItem coin) {
                return coin.getCoinValue(); // sort by coin value ascending
            }
            return Integer.MAX_VALUE;
        }));

        for (int i = 0; i < items.size(); i++) {
            MenuStockItem item = new MenuStockItem(this, items.get(i), (int) invPosX, (int) invPosY + (i * 14), boxL.getWidth()-scrollBar.getWidth()-4, false);
            item.setBackgroundColor(new Color(30,30,100));
            inventory.add(item);
        }
        inventory.forEach(this::addWidget);

        //addRenderableWidget(deposit = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Synthesis_Materials_Deposit), ButtonType.BUTTON, (e) -> { action("deposit"); }));
        addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY/* + (18)*/, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, (e) -> action("back")));
        addRenderableWidget(amountBox = new EditBox(minecraft.font, boxR.getX() + 30, (int) (topBarHeight + middleHeight - 30), minecraft.font.width("#####"), 16, Component.translatable("test")) {
            @Override
            public boolean charTyped(char c, int i) {
                if (!Utils.isNumber(c)) {
                    return false;
                }

                String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();

                int enteredAmount;
                try {
                    enteredAmount = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    return false;
                }

                if (enteredAmount > 64) {
                    return false;
                }

                if (selectedItemStack != null && selectedItemStack.getItem() instanceof RMCoinItem coin) {
                    PlayerData playerData = PlayerData.get(minecraft.player);
                    int coinValue = coin.getCoinValue();
                    String coinType = coin.getCoinType();

                    int maxAffordable = switch (coinType) {
                        case "munny" -> playerData.getMunny() / coinValue;
                        case "hearts" -> playerData.getAlignment() != Utils.OrgMember.NONE ? playerData.getHearts() / coinValue : 0;
                        default -> 0;
                    };

                    if (enteredAmount > maxAffordable) {
                        return false;
                    }
                }

                return super.charTyped(c, i);
            }
        });
        take = new MenuButton(amountBox.getX() + amountBox.getWidth()+5, (int) (topBarHeight + middleHeight - 32),58, Strings.Gui_Synthesis_Materials_Take, MenuButton.ButtonType.ROUNDBUTTON,(e) -> action("take"));
        take.setCenterText(true);
        addRenderableWidget(take);

        take.visible = false;
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        drawMenuBackground(gui, mouseX, mouseY, partialTicks);
        boxL.renderWidget(gui, mouseX, mouseY, partialTicks);
        boxR.renderWidget(gui, mouseX, mouseY, partialTicks);
        gui.setColor(1, 1, 1, 1);
        super.render(gui, mouseX, mouseY, partialTicks);

        if (!inventory.isEmpty()) {
            int listHeight = (inventory.get(inventory.size() - 1).getY() + 20) - inventory.get(0).getY() + 3;
            scrollBar.setContentHeight(listHeight);
        }

        if(minecraft.player.getInventory().getFreeSlot() == -1) { //TODO somehow make this detect in singleplayer the inventory changes
            take.active = false;
            take.setMessage(Component.translatable(Strings.Gui_Shop_NoSpace));
        }

        for (MenuStockItem stockItem : inventory) {
            stockItem.active = false;
        }

        for(Renderable renderable : this.inventory){
            if(renderable instanceof MenuStockItem menuStockItem){
                menuStockItem.active = true;
                gui.enableScissor(boxL.getX()+2,scrollBar.getY()+2,boxL.getX()+boxL.getWidth(),scrollBar.getBottom()-5); //Arbitrary number to hide the cut one
                renderable.render(gui,mouseX,mouseY,partialTicks);
                gui.disableScissor();

            } else {
                renderable.render(gui,mouseX,mouseY,partialTicks);
            }
        }

        //deposit.render(gui, mouseX,  mouseY,  partialTicks);
        back.render(gui, mouseX,  mouseY,  partialTicks);
    }

    @Override
    protected void renderSelectedData(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = gui.pose();
        amountBox.render(gui, mouseX,  mouseY,  partialTicks);
        take.render(gui, mouseX, mouseY, partialTicks);

        take.visible = true;
        take.active = false; // default
        take.setMessage(Component.translatable(Strings.Gui_Synthesis_Materials_Take)); // reset label

        if (selectedItemStack != null && !selectedItemStack.isEmpty() && selectedItemStack.getItem() instanceof RMCoinItem coin) {
            PlayerData playerData = PlayerData.get(minecraft.player);
            int amount = 1;
            int value = coin.getCoinValue();
            String type = coin.getCoinType();

            boolean canTake = switch (type) {
                case "munny" -> playerData.getMunny() >= value * amount;
                case "hearts" -> playerData.getAlignment() != Utils.OrgMember.NONE && playerData.getHearts() >= value * amount;
                default -> false;
            };

            if (canTake) {
                take.active = true;
            } else {
                take.active = false;
            }
        }

        float iconPosX = boxR.getX();
        float iconPosY = boxR.getY() + 15;

        matrixStack.pushPose();
        {
            String name = selectedItemStack.getHoverName().getString();
            matrixStack.translate(boxR.getX() + (boxR.getWidth() / 2.0) - minecraft.font.width(name)/2.0, boxR.getY()+3, 1);
            gui.drawString(minecraft.font, Utils.translateToLocal(name), 0, 0, 0xFF9900);
        }
        matrixStack.popPose();

        matrixStack.pushPose();
        {
            float size = 80;
            double offset = (boxR.getWidth()*0.1F);
            matrixStack.translate(boxR.getX() + offset/2, iconPosY, 1);
            matrixStack.translate(boxR.getWidth()*0.7F / 2,boxR.getHeight()/2.0 - size / 2,0);
            ClientUtils.drawItemAsIcon(selectedItemStack, matrixStack, 0, 0, (int)size);
        }
        matrixStack.popPose();

    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        scrollBar.mouseClicked(mouseX, mouseY, mouseButton);
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        scrollBar.mouseReleased(pMouseX, pMouseY, pButton);

        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        scrollBar.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);

        updateScroll();
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    public void updateScroll() {
        inventory.forEach(button -> button.offsetY = (int) scrollBar.scrollOffset);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if(mouseX >= boxL.getX() && mouseX <= scrollBar.getX()+ scrollBar.getWidth())
            scrollBar.mouseScrolled(mouseX, mouseY, deltaX, deltaY);

        updateScroll();
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}