package net.splakra.permitsanddiplomas.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    public static Item getItemByName(String item) {
        String[] split = item.split(":");
        ResourceLocation loc = new ResourceLocation(split[0], split[1]);
        if (ForgeRegistries.ITEMS.containsKey(loc)) {
            return ForgeRegistries.ITEMS.getValue(loc);
        }
        Minecraft.getInstance().player.sendSystemMessage(Component.literal("Error: Item returned null!: " + item));
        return Items.AIR;
    }

    public static List<Item> getItemsFromList(List<String> list) {
        List<Item> items = new ArrayList<>();
        for (String s : list) {
            if (modExists(s)) {
                items.add(getItemByName(s));
            }
        }
        return items;
    }

    public static boolean modExists(String item) {
        String namespace = item.split(":")[0];
        return ForgeRegistries.ITEMS.getKeys().stream().anyMatch(key -> key.getNamespace().equals(namespace));
    }
}
