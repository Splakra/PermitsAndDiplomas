package net.splakra.permitsanddiplomas.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;
import net.splakra.permitsanddiplomas.PermitMod;
import net.splakra.permitsanddiplomas.ui.PermitEditorScreen;
import net.splakra.permitsanddiplomas.ui.PermitInfoScreen;

@JeiPlugin
public class PermitsJeiPlugin implements IModPlugin {

    private static final ResourceLocation ID = new ResourceLocation(PermitMod.MOD_ID, "jei_plugin");
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(PermitInfoScreen.class, new PermitInfoJeiHandler());
        registration.addGuiContainerHandler(PermitEditorScreen.class, new PermitEditorJeiHandler());
    }
}
