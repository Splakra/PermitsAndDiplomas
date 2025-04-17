package net.splakra.permitsanddiplomas.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemUtils {
    public static Item getItemByName(String namespace, String name) {
        ResourceLocation loc = new ResourceLocation(namespace, name);
        if (ForgeRegistries.ITEMS.containsKey(loc)) {
            return ForgeRegistries.ITEMS.getValue(loc);
        }
        return null;
    }
}
