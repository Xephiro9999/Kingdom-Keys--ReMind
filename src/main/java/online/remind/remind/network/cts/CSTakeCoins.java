package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.item.RMCoinItem;

import java.util.Objects;

public class CSTakeCoins implements CustomPacketPayload{
    public static final Type<CSTakeCoins> TYPE = new Type(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_coin"));
    public static final StreamCodec<FriendlyByteBuf, CSTakeCoins> STREAM_CODEC = StreamCodec.of(CSTakeCoins::encode, CSTakeCoins::decode);
    ItemStack stack;



    public CSTakeCoins() {}

    public static final StreamCodec<RegistryFriendlyByteBuf, CSTakeCoins> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            CSTakeCoins::stack,
            CSTakeCoins::new
    );

    public CSTakeCoins(ItemStack stack) {
        this.stack = stack;
    }

    public void encode(RegistryFriendlyByteBuf buffer) {
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, stack);

    }

    public static CSTakeCoins decode(RegistryFriendlyByteBuf buffer) {
        CSTakeCoins msg = new CSTakeCoins();
        msg.stack = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);
        return msg;
    }

    public static void handle(final CSTakeCoins message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            PlayerData playerData = PlayerData.get(player);

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


            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
        });

    }
    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type () {
        return TYPE;
    }

}
