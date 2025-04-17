package net.splakra.permitsanddiplomas.ui;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


public class ModScreens {
    // Register the screen for the menu
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.PERMIT_EDITOR_MENU.get(), PermitEditorScreen::new);
            MenuScreens.register(ModMenus.PERMIT_INFO_MENU.get(), PermitInfoScreen::new);
        });
    }
}