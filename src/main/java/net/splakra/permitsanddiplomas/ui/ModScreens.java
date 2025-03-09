package net.splakra.permitsanddiplomas.ui;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.splakra.permitsanddiplomas.PermitMod;

@Mod.EventBusSubscriber(modid = PermitMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModScreens {
    // Register the screen for the menu
    @SubscribeEvent
    public static void registerScreens(RegisterGuiOverlaysEvent event) {
        MenuScreens.register(ModMenus.PERMIT_EDITOR_MENU.get(), PermitEditorScreen::new);
    }
}