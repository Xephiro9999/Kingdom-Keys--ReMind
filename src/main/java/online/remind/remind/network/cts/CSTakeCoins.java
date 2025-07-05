package online.remind.remind.network.cts;

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
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSTakeCoins {

    ItemStack stack;
    String inv;
    String name;

    public CSTakeCoins() {}

    public CSTakeCoins(Item item, int amount, String inv, String name) {
        this.stack = new ItemStack(item,amount);
        this.inv = inv;
        this.name = name;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeItem(stack);
        buffer.writeUtf(inv);
        buffer.writeUtf(name);
    }

    public static CSTakeCoins decode(FriendlyByteBuf buffer) {
        CSTakeCoins msg = new CSTakeCoins();
        msg.stack = buffer.readItem();
        msg.inv = buffer.readUtf();
        msg.name = buffer.readUtf();
        return msg;
    }

    public static void handle(CSTakeCoins message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            if(!ItemStack.isSameItem(message.stack, ItemStack.EMPTY)) {

                if(playerData.getMaterialAmount(message.stack.getItem())<message.stack.getCount()) {

                } else {
                    //playerData.removeMaterial(message.stack.getItem(), message.stack.getCount());
                    player.getInventory().add(message.stack);
                }
            }
            PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
            //PacketHandler.sendTo(new SCOpenMaterialsScreen(message.inv, message.name, message.moogle), (ServerPlayer) player);
        });
        ctx.get().setPacketHandled(true);
    }

}
