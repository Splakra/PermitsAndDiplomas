package net.splakra.permitsanddiplomas.ui;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.splakra.permitsanddiplomas.PermitMod;

@Mod.EventBusSubscriber(
        modid = PermitMod.MOD_ID,
        bus   = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
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