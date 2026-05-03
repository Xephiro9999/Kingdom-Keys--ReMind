package online.remind.remind.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import online.remind.remind.KingdomKeysReMind;

import java.util.function.Supplier;

public class ModDataRM {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, KingdomKeysReMind.MODID);
    public static final Supplier<AttachmentType<GlobalDataRM>> GLOBAL_DATA = ATTACHMENT_TYPES.register("global", () -> AttachmentType.serializable(GlobalDataRM::new).copyOnDeath().build());

    public static GlobalDataRM get(CompoundTag nbt, Player player) {
        //Only deserialize on client, there shouldn't be a reason to do this on the server
        if (FMLEnvironment.dist.isClient()) {
            GlobalDataRM data = new GlobalDataRM();
            data.deserializeNBT(player.level().registryAccess(), nbt);
            player.setData(GLOBAL_DATA, data);
            return data;
        } else {
            return getGlobal(player);
        }
    }

    public static GlobalDataRM getGlobal(LivingEntity e) {
        if (!e.hasData(GLOBAL_DATA)) {
            e.setData(GLOBAL_DATA, new GlobalDataRM());
        }
        return e.getData(GLOBAL_DATA);
    }

    public static WorldDataRM getWorld(Level level) {
        if (level.isClientSide()) {
            return WorldDataRM.getClient();
        } else {
            return WorldDataRM.get(level.getServer());
        }
    }
}

