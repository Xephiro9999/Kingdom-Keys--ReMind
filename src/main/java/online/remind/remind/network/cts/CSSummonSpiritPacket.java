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
import online.remind.remind.capabilities.GlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.dreameater.DreamEater;
import online.remind.remind.dreameater.ModDreamEaters;
import online.remind.remind.entity.spirits.*;
import online.remind.remind.lib.StringsRM;
import online.remind.remind.network.PacketHandlerRM;

import java.util.UUID;

public class CSSummonSpiritPacket implements CustomPacketPayload {

    public static final Type<CSSummonSpiritPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "cs_summon_spirit"));

    public static final StreamCodec<FriendlyByteBuf, CSSummonSpiritPacket> STREAM_CODEC =
            StreamCodec.of(CSSummonSpiritPacket::encode, CSSummonSpiritPacket::decode);

    public CSSummonSpiritPacket() {
    }

    public static void encode(FriendlyByteBuf buffer, CSSummonSpiritPacket message) {
    }

    public static CSSummonSpiritPacket decode(FriendlyByteBuf buffer) {
        return new CSSummonSpiritPacket();
    }

    private static void spawnArmorParticles(Entity spirit) {
        if (!(spirit.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Vec3 spiritPos = new Vec3(spirit.getX(), spirit.getY() + 3.5D, spirit.getZ());

        serverLevel.sendParticles(
                ParticleTypes.END_ROD,
                spiritPos.x,
                spiritPos.y,
                spiritPos.z,
                150,
                0.0D,
                0.0D,
                0.0D,
                0.2D
        );

        serverLevel.sendParticles(
                ParticleTypes.DRAGON_BREATH,
                spiritPos.x,
                spiritPos.y,
                spiritPos.z,
                150,
                0.0D,
                0.0D,
                0.0D,
                0.2D
        );
    }

    public static void handle(final CSSummonSpiritPacket message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player owner = ctx.player();

            GlobalDataRM globalData = ModDataRM.getGlobal(owner);
            PlayerData kkData = PlayerData.get(owner);

            if (kkData == null || globalData == null) {
                return;
            }

            if (!globalData.hasDreamEaterSummoned() && globalData.getDreamEaterUUID() == null) {
                handleSummon(owner, kkData, globalData);
            } else {
                handleDesummon(owner, globalData);
            }

            PacketHandlerRM.syncGlobalToAllAround(owner, globalData);
        });
    }

    private static void handleSummon(Player owner, PlayerData kkData, GlobalDataRM globalData) {
        if (!(owner.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        String dreamEaterRL = globalData.getDreamEaterRL();

        DreamEater dreamEater = ModDreamEaters.registry.get(ResourceLocation.parse(dreamEaterRL));

        if (dreamEater == null) {
            owner.displayClientMessage(Component.literal("Dream Eater data could not be found!"), true);
            return;
        }

        if (StringsRM.none.equals(dreamEater.getName())) {
            owner.displayClientMessage(Component.literal("You don't have a Dream Eater Equipped!"), true);
            return;
        }

        if (!globalData.hasDreamEaterUnlocked(dreamEaterRL)) {
            owner.displayClientMessage(Component.literal("You have not unlocked this Dream Eater yet."), true);
            return;
        }

        Entity summonedDreamEater = null;

        ChirithyEntity.removeExistingChirithy(serverLevel, owner.getUUID());
        MeowWowEntity.removeExistingMeowWow(serverLevel, owner.getUUID());
        KomoryBatEntity.removeExistingKomoryBat(serverLevel, owner.getUUID());
        CactuarSpiritEntity.removeExistingCactuarSpirit(serverLevel, owner.getUUID());

        switch (dreamEater.getName()) {

            case StringsRM.chirithy: {
                ChirithyEntity chirithy = new ChirithyEntity(owner.level(), owner);
                chirithy.setOwnerUUID(owner.getUUID());
                chirithy.setPos(owner.getX(), owner.getY() + 2.0D, owner.getZ());

                int variant = kkData.getAlignment() != Utils.OrgMember.NONE ? 0 : 1;
                chirithy.setVariant(variant);

                owner.level().addFreshEntity(chirithy);
                summonedDreamEater = chirithy;
                break;
            }

            case StringsRM.meowWow: {
                MeowWowEntity meowWow = new MeowWowEntity(owner.level(), owner);
                meowWow.setOwnerUUID(owner.getUUID());
                meowWow.setPos(owner.getX(), owner.getY() + 2.0D, owner.getZ());

                int variant = kkData.getAlignment() != Utils.OrgMember.NONE
                        ? MeowWowEntity.VARIANT_ORG
                        : MeowWowEntity.VARIANT_NORMAL;

                meowWow.setVariant(variant);

                owner.level().addFreshEntity(meowWow);
                summonedDreamEater = meowWow;
                break;
            }

            case StringsRM.komoryBat: {
                KomoryBatEntity komoryBat = new KomoryBatEntity(owner.level(), owner);
                komoryBat.setOwnerUUID(owner.getUUID());
                komoryBat.setPos(owner.getX(), owner.getY() + 2.4D, owner.getZ());

                int variant = kkData.getAlignment() != Utils.OrgMember.NONE
                        ? KomoryBatEntity.VARIANT_ORG
                        : KomoryBatEntity.VARIANT_NORMAL;

                komoryBat.setVariant(variant);

                owner.level().addFreshEntity(komoryBat);
                summonedDreamEater = komoryBat;
                break;
            }

            case "dreameater_cactuar":
            case "cactuar": {
                CactuarSpiritEntity cactuar = new CactuarSpiritEntity(owner.level(), owner);
                cactuar.setOwnerUUID(owner.getUUID());
                cactuar.setPos(owner.getX(), owner.getY() + 1.0D, owner.getZ());

                owner.level().addFreshEntity(cactuar);
                summonedDreamEater = cactuar;
                break;
            }

            case "dreameater_tonberry":
            case "tonberry": {
                TonberrySpiritEntity tonberry = new TonberrySpiritEntity(owner.level(), owner);
                tonberry.setOwnerUUID(owner.getUUID());
                tonberry.setPos(owner.getX(), owner.getY() + 0.1D, owner.getZ());

                owner.level().addFreshEntity(tonberry);
                summonedDreamEater = tonberry;
                break;
            }

            default: {
                owner.displayClientMessage(
                        Component.literal("Unknown Dream Eater: " + dreamEater.getName()),
                        true
                );
                return;
            }
        }

        if (summonedDreamEater == null) {
            return;
        }

        globalData.setDreamEaterUUID(summonedDreamEater.getUUID());
        globalData.setHasDreamEaterSummoned(true);

        owner.level().playSound(
                null,
                owner.getX(),
                owner.getY(),
                owner.getZ(),
                ModSoundsRM.SPIRIT_SUMMON.get(),
                SoundSource.PLAYERS,
                0.2F,
                1.0F
        );

        spawnArmorParticles(summonedDreamEater);
    }

    private static void handleDesummon(Player owner, GlobalDataRM globalData) {
        UUID dreamEaterUUID = globalData.getDreamEaterUUID();

        if (dreamEaterUUID != null && owner.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(dreamEaterUUID);

            if (entity != null) {
                entity.discard();
            }
        }

        owner.level().playSound(
                null,
                owner.getX(),
                owner.getY(),
                owner.getZ(),
                ModSoundsRM.SPIRIT_DESUMMON.get(),
                SoundSource.PLAYERS,
                0.2F,
                1.0F
        );

        globalData.setDreamEaterUUID(null);
        globalData.setHasDreamEaterSummoned(false);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}