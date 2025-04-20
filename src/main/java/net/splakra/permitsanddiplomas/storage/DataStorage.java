package net.splakra.permitsanddiplomas.storage;


import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.splakra.permitsanddiplomas.network.SavePermitDataPacket;

import java.util.ArrayList;
import java.util.List;


public class DataStorage extends SavedData {

    public DataStorage() {
    }

    private List<String> usedPermits = new ArrayList<>();

    private List<PermitEntry> permitEntries = new ArrayList<>();

    private List<String> overwrittenPermits = new ArrayList<>();

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
            permitEntryTag.putBoolean("accomplished", permitEntries.get(i).getAccomplished());
            permitEntryTag.putString("items", permitEntries.get(i).getItemsAsString());
            pCompoundTag.put("permitEntry_" + i, permitEntryTag);
        }
        pCompoundTag.putInt("overwrittenPermitsSize", overwrittenPermits.size());
        for (int i = 0; i < overwrittenPermits.size(); i++) {
            pCompoundTag.putString("overwrittenPermit_" + i, overwrittenPermits.get(i));
        }
        return pCompoundTag;
    }

    public static DataStorage load(CompoundTag pCompoundTag) {
        DataStorage dataStorage = new DataStorage();
        int size = pCompoundTag.getInt("usedPermitsSize");

        for (int i = 0; i < size; i++) {
            dataStorage.usedPermits.add(pCompoundTag.getString("usedPermit_" + i));
        }

        size = pCompoundTag.getInt("overwrittenPermitsSize");
        for (int i = 0; i < size; i++) {
            dataStorage.overwrittenPermits.add(pCompoundTag.getString("overwrittenPermit_" + i));
        }

        size = pCompoundTag.getInt("permitEntriesSize");
        for (int i = 0; i < size; i++) {
            CompoundTag permitEntryTag = pCompoundTag.getCompound("permitEntry_" + i);
            PermitEntry permitEntry = new PermitEntry(
                    permitEntryTag.getString("title"),
                    permitEntryTag.getString("player"),
                    permitEntryTag.getString("items"),
                    permitEntryTag.getBoolean("accomplished")
                    );
            dataStorage.permitEntries.add(permitEntry);
        }

        dataStorage.loadPermitsFromConfig();

        return dataStorage;
    }

    public void loadPermitsFromConfig(){
        if (this.permitEntries.isEmpty())
            this.permitEntries.addAll(PermitEntry.getAllPermitEntriesFromConfig());
        else {
            PermitEntry.getAllPermitEntriesFromConfig().stream()
                    .forEach(permitEntry -> {
                        if (this.permitEntries.stream()
                                .noneMatch(entry -> permitEntry.getTitle().equalsIgnoreCase(entry.getTitle()) || overwrittenPermits.contains(entry.getTitle())))
                            this.permitEntries.add(permitEntry);
                    });
        }
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

    public PermitEntry getPermitEntry(String permitTitle) {
        return permitEntries.stream().filter(permitEntry -> permitEntry.getTitle().equalsIgnoreCase(permitTitle)).findFirst().orElse(null);
    }

    public void changePermitEntryPlayer(String title, String playerName) {
        PermitEntry permitEntry = getPermitEntries().stream().filter(permitEntry1 -> permitEntry1.getTitle().equalsIgnoreCase(title)).findFirst().orElse(null);
        if (permitEntry != null) {
            permitEntry.setPlayerName(playerName);
        } else {
        }
        this.setDirty();
    }
    public void unclaimPermitEntry(String title){
        changePermitEntryPlayer(title, "unclaimed");
    }

    public boolean permitEntriesContain(String title){
        return permitEntries.stream().anyMatch(permitEntry -> permitEntry.getTitle().equalsIgnoreCase(title));
    }

    public void clearAllPermitEntries() {
        this.permitEntries.clear();
        this.overwrittenPermits.clear();
        this.setDirty();
    }

    public void handlePermitChange(SavePermitDataPacket pkt) {
        CompoundTag tag = pkt.permit.getOrCreateTag();
        String oldTitle = tag.getString("content");
        if (!overwrittenPermits.contains(oldTitle))
            overwrittenPermits.add(oldTitle);

        if (permitEntriesContain(oldTitle)){
            PermitEntry permitEntry = new PermitEntry(pkt.text, tag.getString("owner"), pkt.filterStacks.stream().map(ItemStack::getItem).toList(), getPermitEntry(oldTitle).getAccomplished());
            int idx = permitEntries.indexOf(getPermitEntry(oldTitle));
            permitEntries.remove(idx);
            permitEntries.add(idx, permitEntry);
        }

        this.setDirty();
    }

    public void toggleAccomplished(String title) {
        if (permitEntriesContain(title)){
            PermitEntry permitEntry = getPermitEntry(title);
            permitEntry.setAccomplished(!permitEntry.getAccomplished());

            this.setDirty();
        }
    }
}
