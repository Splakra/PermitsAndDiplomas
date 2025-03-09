package net.splakra.permitsanddiplomas.storage;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class WorldDataManager {
    private static MinecraftServer server;
    private static final String DATA_NAME = "permit_data";

    public static void setServer(MinecraftServer server){
        WorldDataManager.server = server;
    }

    public static DataStorage get(ServerLevel world) {
        DimensionDataStorage storage = world.getDataStorage();
        return storage.computeIfAbsent(DataStorage::load, DataStorage::new, DATA_NAME);
    }

    public static DataStorage getOverworldData() {
        ServerLevel overworld = server.overworld();
        return get(overworld);
    }
}
