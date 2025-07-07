package online.remind.remind.network.cts;

import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.remind.remind.item.RMCoinItem;
import online.remind.remind.network.PacketHandlerRM;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSTakeCoins {

    ItemStack stack;

    public CSTakeCoins() {}

    public CSTakeCoins(ItemStack stack) {
        this.stack = stack;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeItem(stack == null ? ItemStack.EMPTY : stack);
    }

    public static CSTakeCoins decode(FriendlyByteBuf buffer) {
        CSTakeCoins msg = new CSTakeCoins();
        msg.stack = buffer.readItem();
        return msg;
    }

    public static void handle(CSTakeCoins message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

            if (!message.stack.isEmpty()) {
                int amount = message.stack.getCount();

                if (message.stack.getItem() instanceof RMCoinItem coin) {
                    String type = coin.getCoinType();
                    int value = coin.getCoinValue();
                    int totalCost = value * amount;

                    // Check if player can afford it
                    if (Objects.equals(type, "munny")) {
                        if (playerData.getMunny() >= totalCost) {
                            playerData.setMunny(playerData.getMunny() - totalCost);
                            player.getInventory().add(message.stack.copy());
                        } else {
                            //System.out.println("[CSTakeCoins] Denied: Not enough Munny");
                            return;
                        }
                    } else if (Objects.equals(type, "hearts") && playerData.getAlignment() != Utils.OrgMember.NONE) {
                        if (playerData.getHearts() >= totalCost) {
                            playerData.setHearts(playerData.getHearts() - totalCost);
                            player.getInventory().add(message.stack.copy());
                        } else {
                            //System.out.println("[CSTakeCoins] Denied: Not enough Hearts");
                            return;
                        }
                    }
                }
            }

            PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
        });
        ctx.get().setPacketHandled(true);
    }
}
