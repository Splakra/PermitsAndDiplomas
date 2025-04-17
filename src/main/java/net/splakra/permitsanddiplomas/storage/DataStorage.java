package net.splakra.permitsanddiplomas.storage;

import net.minecraft.client.main.GameConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.registries.GameData;

import java.util.ArrayList;
import java.util.List;


public class DataStorage extends SavedData {

    private List<String> usedPermits = new ArrayList<>();

    private List<PermitEntry> permitEntries = new ArrayList<>();

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        pCompoundTag.putInt("usedPermitsSize", usedPermits.size()); // Store list size
        for (int i = 0; i < usedPermits.size(); i++) {
            pCompoundTag.putString("usedPermit_" + i, usedPermits.get(i));
        }
        pCompoundTag.putInt("permitEntriesSize", permitEntries.size());
        for (int i = 0; i < permitEntries.size(); i++) {
            CompoundTag permitEntryTag = new CompoundTag();
            permitEntryTag.putString("title", permitEntries.get(i).getTitle());
            permitEntryTag.putString("player", permitEntries.get(i).getPlayerName());
            permitEntryTag.putString("items", permitEntries.get(i).getItemsAsString());
            pCompoundTag.put("permitEntry_" + i, permitEntryTag);
        }
        return pCompoundTag;
    }

    public static DataStorage load(CompoundTag pCompoundTag) {
        DataStorage dataStorage = new DataStorage();
        int size = pCompoundTag.getInt("usedPermitsSize");

        for (int i = 0; i < size; i++) {
            dataStorage.usedPermits.add(pCompoundTag.getString("usedPermit_" + i));
        }

        size = pCompoundTag.getInt("permitEntriesSize");
        for (int i = 0; i < size; i++) {
            CompoundTag permitEntryTag = pCompoundTag.getCompound("permitEntry_" + i);
            PermitEntry permitEntry = new PermitEntry(
                    permitEntryTag.getString("title"),
                    permitEntryTag.getString("player"),
                    permitEntryTag.getString("items")
                    );
            dataStorage.permitEntries.add(permitEntry);
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

    public List<PermitEntry> getPermitEntries() {
        return permitEntries;
    }

    public List<PermitEntry> getClaimedPermitEntries() {
        return permitEntries.stream().filter(permitEntry -> !permitEntry.getPlayerName().equalsIgnoreCase("unclaimed")).toList();
    }

    public List<PermitEntry> getUnclaimedPermitEntries() {
        return permitEntries.stream().filter(permitEntry -> permitEntry.getPlayerName().equalsIgnoreCase("unclaimed")).toList();
    }

    public void changePermitEntryPlayer(String title, String playerName) {
        PermitEntry permitEntry = getPermitEntries().stream().filter(permitEntry1 -> permitEntry1.getTitle().equalsIgnoreCase(title)).findFirst().orElse(null);
        if (permitEntry != null) {
            permitEntry.setPlayerName(playerName);
        }
        this.setDirty();
    }
    public void unclaimPermitEntry(String title){
        changePermitEntryPlayer(title, "unclaimed");
    }
}
