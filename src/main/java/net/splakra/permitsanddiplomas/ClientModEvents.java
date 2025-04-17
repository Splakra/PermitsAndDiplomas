package net.splakra.permitsanddiplomas;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.splakra.permitsanddiplomas.ui.PermitInfoMenu;
import net.splakra.permitsanddiplomas.ui.PermitInfoScreen;

public class ClientModEvents {
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen inv) {
            int x = inv.getGuiLeft();
            int y = inv.getGuiTop();

            Button infoBtn = Button.builder(Component.literal("Info"), btn -> {
                        Minecraft mc = Minecraft.getInstance();
                        mc.setScreen(new PermitInfoScreen(
                                new PermitInfoMenu(0, mc.player.getInventory()),
                                mc.player.getInventory(),
                                Component.literal("Permit Info")
                        ));
                    })
                    .bounds(x + 152, y + 6, 40, 20) // Wider for label visibility
                    .build();

            event.addListener(infoBtn);
        }
    }
}