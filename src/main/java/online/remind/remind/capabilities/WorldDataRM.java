package online.remind.remind.capabilities;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

public class WorldDataRM extends SavedData {

    private WorldDataRM() {

    }

    private static WorldDataRM create() {
        return new WorldDataRM();
    }

    public static WorldDataRM get(MinecraftServer server) {
        //saving world data only on the overworld, if per dimension data is needed pass the level instead
        return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(WorldDataRM::create, WorldDataRM::load), "kkremind_data");
    }

    private static WorldDataRM clientCache = new WorldDataRM();

    public static WorldDataRM getClient() {
        return clientCache;
    }

    public static void setClientCache(WorldDataRM data) {
        clientCache = data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {

        return tag;
    }

    public static WorldDataRM load(CompoundTag tag, HolderLookup.Provider provider) {
        WorldDataRM data = WorldDataRM.create();



        return data;
    }
}
