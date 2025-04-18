package net.splakra.permitsanddiplomas.storage;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import net.splakra.permitsanddiplomas.config.CommonConfig;
import net.splakra.permitsanddiplomas.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

public class PermitEntry {
    private String title;
    private String playerName;
    private List<Item> items;

    public PermitEntry(String title, String playerName, List<Item> items) {
        this.title = title;
        this.playerName = playerName;
        this.items = items;
    }

    public PermitEntry(String title, String playerName, String items) {
        this.title = title;
        this.playerName = playerName;
        this.items = getItemsFromString(items);
    }

    public static List<PermitEntry> getAllPermitEntriesFromConfig(){
        List<PermitEntry> permitEntries = new ArrayList<>();
        for (var entry : CommonConfig.ENTRIES_ITEMS_LIST.get()){
            String title = entry.get(0);
            permitEntries.add(new PermitEntry(title, "unclaimed", ItemUtils.getItemsFromList(entry.subList(1, entry.size()))));
        }
        return permitEntries;
    }

    public String getTitle() {
        return title;
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getItemsAsString(){
        String res = "";
        for (var item: items){
            res += ForgeRegistries.ITEMS.getKey(item);
            res += ", ";
        }
        try {
            res = res.substring(0, res.length() - 2);
        } catch (IndexOutOfBoundsException ignored) {}
        return res;
    }

    public List<Item> getItemsFromString(String itemsAsString){
        List<Item> res = new ArrayList<>();
        List<String> items = List.of(itemsAsString.split(", "));
        for (String item: items){
            if(ItemUtils.modExists(item)) {
                res.add(ItemUtils.getItemByName(item));
            }
        }
        return res;
    }

    public Item getFirstItem(){
        if (items.isEmpty()) return Items.AIR;
        return items.get(0);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName.trim();
    }

    @Override
    public String toString() {
        return title + " (" + playerName + ") -- " + getItemsAsString();
    }
}
