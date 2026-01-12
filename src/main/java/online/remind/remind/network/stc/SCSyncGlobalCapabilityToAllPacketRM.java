package online.remind.remind.network.stc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.remind.remind.KingdomKeysReMind;
import online.remind.remind.capabilities.IGlobalDataRM;
import online.remind.remind.capabilities.ModDataRM;

import java.util.UUID;

public class SCSyncGlobalCapabilityToAllPacketRM implements CustomPacketPayload {
    public static final Type<SCSyncGlobalCapabilityToAllPacketRM> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeysReMind.MODID, "sc_sync_global_capability_to_all"));
    public static final StreamCodec<FriendlyByteBuf, SCSyncGlobalCapabilityToAllPacketRM> STREAM_CODEC = StreamCodec.of(SCSyncGlobalCapabilityToAllPacketRM::encode, SCSyncGlobalCapabilityToAllPacketRM::decode);

    public int id;
    public int berserkLvl, berserkTicks, prestige, strBonus, magBonus, defBonus, NGPlusWarriorCount, NGPlusMysticCount, NGPlusGuardianCount, stepTicks, riskchargeCount, autoLife, rcCooldown, CanCounter, strPanel, magPanel, defPanel, panelsStatus, ngpStatus;
    public String dreamEaterRL;
    public boolean donorGiven, darkMode, dreamEaterSummoned;
    public byte stepType;
    public UUID dreamEaterUUID;

    public SCSyncGlobalCapabilityToAllPacketRM() {

    }

    public SCSyncGlobalCapabilityToAllPacketRM(int id, IGlobalDataRM capability) {
        this.id = id;
        this.berserkLvl= capability.getBerserkLevel();
        this.berserkTicks = capability.getBerserkTicks();
        this.prestige = capability.getPrestigeLvl();
        this.strBonus = capability.getSTRBonus();
        this.magBonus = capability.getMAGBonus();
        this.defBonus = capability.getDEFBonus();
        this.NGPlusWarriorCount = capability.getNGPWarriorCount();
        this.NGPlusMysticCount = capability.getNGPMysticCount();
        this.NGPlusGuardianCount = capability.getNGPGuardianCount();
        this.stepTicks = capability.getStepTicks();
        this.stepType = capability.getStepType();
        this.riskchargeCount = capability.getRiskchargeCount();
        this.autoLife = capability.getAutoLifeActive();
        this.rcCooldown = capability.getRCCooldownTicks();
        this.CanCounter = capability.getCanCounter();
        this.strPanel = capability.getSTRPanel();
        this.magPanel = capability.getMAGPanel();
        this.defPanel = capability.getDEFPanel();
        this.panelsStatus = capability.getPanelsEnabled();
        this.ngpStatus = capability.getNGPEnabled();
        this.donorGiven = capability.getDonorGiven();
        this.darkMode = capability.isDarkMode();
        this.dreamEaterSummoned = capability.hasDreamEaterSummoned();
        this.dreamEaterUUID = capability.getDreamEaterUUID();
        this.dreamEaterRL = capability.getDreamEaterRL();

    }

    public static void encode(FriendlyByteBuf buffer, SCSyncGlobalCapabilityToAllPacketRM message){
        buffer.writeInt(message.id);
        buffer.writeInt(message.berserkLvl);
        buffer.writeInt(message.berserkTicks);
        buffer.writeInt(message.prestige);
        buffer.writeInt(message.strBonus);
        buffer.writeInt(message.magBonus);
        buffer.writeInt(message.defBonus);
        buffer.writeInt(message.NGPlusWarriorCount);
        buffer.writeInt(message.NGPlusMysticCount);
        buffer.writeInt(message.NGPlusGuardianCount);
        buffer.writeInt(message.stepTicks);
        buffer.writeByte(message.stepType);
        buffer.writeInt(message.riskchargeCount);
        buffer.writeInt(message.autoLife);
        buffer.writeInt(message.rcCooldown);
        buffer.writeInt(message.CanCounter);
        buffer.writeInt(message.strPanel);
        buffer.writeInt(message.magPanel);
        buffer.writeInt(message.defPanel);
        buffer.writeInt(message.panelsStatus);
        buffer.writeInt(message.ngpStatus);
        buffer.writeBoolean(message.donorGiven);
        buffer.writeBoolean(message.darkMode);
        buffer.writeBoolean(message.dreamEaterSummoned);
        buffer.writeUtf(message.dreamEaterRL,100);
        if (message.dreamEaterUUID != null) {
            buffer.writeBoolean(true);
            buffer.writeUUID(message.dreamEaterUUID);
        } else {
            buffer.writeBoolean(false);
        }



    }

    public static SCSyncGlobalCapabilityToAllPacketRM decode(FriendlyByteBuf buffer){
        SCSyncGlobalCapabilityToAllPacketRM msg = new SCSyncGlobalCapabilityToAllPacketRM();
        msg.id = buffer.readInt();
        msg.berserkLvl = buffer.readInt();
        msg.berserkTicks = buffer.readInt();
        msg.prestige = buffer.readInt();
        msg.strBonus = buffer.readInt();
        msg.magBonus = buffer.readInt();
        msg.defBonus = buffer.readInt();
        msg.NGPlusWarriorCount = buffer.readInt();
        msg.NGPlusMysticCount = buffer.readInt();
        msg.NGPlusGuardianCount = buffer.readInt();
        msg.stepTicks = buffer.readInt();
        msg.stepType = buffer.readByte();
        msg.riskchargeCount = buffer.readInt();
        msg.autoLife = buffer.readInt();
        msg.rcCooldown = buffer.readInt();
        msg.CanCounter = buffer.readInt();
        msg.strPanel = buffer.readInt();
        msg.magPanel = buffer.readInt();
        msg.defPanel = buffer.readInt();
        msg.panelsStatus = buffer.readInt();
        msg.ngpStatus = buffer.readInt();
        msg.donorGiven = buffer.readBoolean();
        msg.darkMode = buffer.readBoolean();
        msg.dreamEaterSummoned = buffer.readBoolean();
        msg.dreamEaterRL = buffer.readUtf(100);
        if (buffer.readBoolean()) {
            msg.dreamEaterUUID = buffer.readUUID();
        }
        return msg;
    }

    public static void handle(final SCSyncGlobalCapabilityToAllPacketRM message, IPayloadContext ctx) {
    	ctx.enqueueWork(() -> {
			LivingEntity entity = (LivingEntity) ctx.player().level().getEntity(message.id);
			
			if (entity != null) {
                IGlobalDataRM globalData = ModDataRM.getGlobal(entity);
                globalData.setBerserkTicks(message.berserkTicks, message.berserkLvl);
                globalData.setPrestigeLvl(message.prestige);
                globalData.setSTRBonus(message.strBonus);
                globalData.setMAGBonus(message.magBonus);
                globalData.setDEFBonus(message.defBonus);
                globalData.setNGPWarriorCount(message.NGPlusWarriorCount);
                globalData.setNGPMysticCount(message.NGPlusMysticCount);
                globalData.setNGPGuardianCount(message.NGPlusGuardianCount);
                globalData.setStepTicks(message.stepTicks, message.stepType);
                globalData.setRiskchargeCount(message.riskchargeCount);
                globalData.setAutoLifeActive(message.autoLife);
                globalData.setRCCooldownTicks(message.rcCooldown);
                globalData.setCanCounter(message.CanCounter);

                globalData.setSTRPanel(message.strPanel);
                globalData.setMAGPanel(message.magPanel);
                globalData.setDEFPanel(message.defPanel);
                globalData.setPanelsEnabled(message.panelsStatus);
                globalData.setNGPEnabled(message.ngpStatus);
                globalData.setDonorGiven(message.donorGiven);
                globalData.setDarkMode(message.darkMode);
                globalData.setHasDreamEaterSummoned(message.dreamEaterSummoned);
                globalData.setDreamEaterUUID(message.dreamEaterUUID);
                globalData.setDreamEaterRL(message.dreamEaterRL);
			}
		});
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
