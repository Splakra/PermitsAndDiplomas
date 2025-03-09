package net.splakra.permitsanddiplomas.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;


public class DataStorage extends SavedData {

    private List<String> usedPermits = new ArrayList<String>();

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        pCompoundTag.putInt("usedPermitsSize", usedPermits.size()); // Store list size
        for (int i = 0; i < usedPermits.size(); i++) {
            pCompoundTag.putString("usedPermit_" + i, usedPermits.get(i));
        }
        return pCompoundTag;
    }

    public static DataStorage load(CompoundTag pCompoundTag) {
        DataStorage dataStorage = new DataStorage();
        int size = pCompoundTag.getInt("usedPermitsSize");

        for (int i = 0; i < size; i++) {
            dataStorage.usedPermits.add(pCompoundTag.getString("usedPermit_" + i));
        }

        return dataStorage;
    }

    public List<String> getUsedPermits() {
        return usedPermits;
    }

    public void addUsedPermit(String permit) {
        if (!this.usedPermits.contains(permit)) {
            this.usedPermits.add(permit);
        }
        this.setDirty();
    }

    public void clearUsedPermits() {
        this.usedPermits.clear();
        this.setDirty();
    }

    public void removeUsedPermit(String permit) {
        if (this.usedPermits.contains(permit)) {
            this.usedPermits.remove(permit);
        }
        this.setDirty();
    }
}
