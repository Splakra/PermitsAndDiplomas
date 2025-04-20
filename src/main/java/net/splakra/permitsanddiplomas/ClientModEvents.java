package net.splakra.permitsanddiplomas;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.splakra.permitsanddiplomas.ui.CustomInventoryScreen;

public class ClientModEvents {
    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event){
        if (event.getScreen() instanceof InventoryScreen) {
            event.setNewScreen(new CustomInventoryScreen(Minecraft.getInstance().player.getInventory()));
        }
    }
}