package net.splakra.permitsanddiplomas.storage;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.splakra.permitsanddiplomas.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

public class PermitEntry {
    private final String title;
    private final String playerName;
    private final List<Item> items;

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
            res += ForgeRegistries.ITEMS.getKey(item) + ": " + item.toString();
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
            String[] itemSplit = item.split(": ");
            res.add(ItemUtils.getItemByName(itemSplit[0], itemSplit[1]));
        }
        return res;
    }

    public void setPlayerName(String playerName) {
        playerName = playerName.trim();
    }
}
