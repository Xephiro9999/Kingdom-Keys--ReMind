package online.remind.remind.network.cts;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.spirits.ChirithyEntity;
import online.remind.remind.network.PacketHandlerRM;

import java.util.UUID;

public class CSSummonSpiritPacket implements CustomPacketPayload {
    public static final Type<CSSummonSpiritPacket> TYPE = new Type(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_summon_spirit"));
    public static final StreamCodec<FriendlyByteBuf, CSSummonSpiritPacket> STREAM_CODEC = StreamCodec.of(CSSummonSpiritPacket::encode, CSSummonSpiritPacket::decode);

    public CSSummonSpiritPacket(){}

    public static void encode(FriendlyByteBuf buffer, CSSummonSpiritPacket message) {
    }

    public static CSSummonSpiritPacket decode(FriendlyByteBuf buffer) {
        CSSummonSpiritPacket msg = new CSSummonSpiritPacket();
        return msg;
    }

    private static void spawnArmorParticles(Entity spirit) {
        Vec3 spiritPos = new Vec3(spirit.getX(), spirit.getY() + 3.5, spirit.getZ());
        ((ServerLevel)spirit.level()).sendParticles(ParticleTypes.END_ROD, spirit.getX(), spirit.getY(), spirit.getZ(), 150, 0,0,0, 0.2);
        ((ServerLevel)spirit.level()).sendParticles(ParticleTypes.DRAGON_BREATH, spirit.getX(), spirit.getY(), spirit.getZ(), 150, 0,0,0, 0.2);
    }


    public static void handle(final CSSummonSpiritPacket message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player owner = ctx.player();

            IGlobalDataRM playerData = ModDataRM.getGlobal(owner);
            PlayerData kkData = PlayerData.get(owner);
            if (kkData == null) return;
            if (playerData == null)

                return;

            if (!playerData.hasDreamEaterSummoned() && playerData.getDreamEaterUUID() == null) {
                    // Spawn
                    if (!(owner.level() instanceof ServerLevel serverLevel)) return;

                    // Todo: Make dynamic system for reading what Dream Eater should be summoned
                    // NONE - 0, Chirithy = 1, Meow-Wow = 2, etc...
                        switch (playerData.getDreamEaterID()) {
                            case 0:
                                // Tell Player that they do not have a Spirit
                                owner.displayClientMessage(Component.translatable("You don't have a Dream Eater Equipped!"), true);
                                break;
                            case 1:
                                // Chirithy Summon
                                ChirithyEntity dreamEater = new ChirithyEntity(owner.level(), owner);
                                dreamEater.setOwnerUUID(owner.getUUID());
                                dreamEater.setPos(owner.getX(), owner.getY() + 2, owner.getZ());
                                owner.level().addFreshEntity(dreamEater);
                                if (kkData.getAlignment() != Utils.OrgMember.NONE) {
                                    dreamEater.setVariant(0);
                                } else {
                                    dreamEater.setVariant(1);
                                }
                                playerData.setDreamEaterUUID(dreamEater.getUUID());
                                owner.level().playSound(null, owner.position().x(), owner.position().y(), owner.position().z(), ModSoundsRM.SPIRIT_SUMMON.get(), SoundSource.PLAYERS, 0.2f, 1.0f);
                                playerData.setHasDreamEaterSummoned(true);
                                spawnArmorParticles(dreamEater);
                                break;

                        }
                    } else {
                    // Despawn
                    if (playerData.getDreamEaterUUID() != null) {
                        UUID dreamEaterUUID = playerData.getDreamEaterUUID();
                        if (dreamEaterUUID != null && owner.level() instanceof ServerLevel serverLevel) {
                            Entity entity = serverLevel.getEntity(dreamEaterUUID);
                            if (entity != null) {
                                entity.discard();
                            }
                        }
                        owner.level().playSound(null, owner.position().x(), owner.position().y(), owner.position().z(), ModSoundsRM.SPIRIT_DESUMMON.get(), SoundSource.PLAYERS, 0.2f, 1.0f);
                        playerData.setDreamEaterUUID(null);
                        playerData.setHasDreamEaterSummoned(false);

                    }
                }
                PacketHandlerRM.syncGlobalToAllAround(owner, playerData);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
