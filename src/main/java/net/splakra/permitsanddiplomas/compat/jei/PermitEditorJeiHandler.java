package net.splakra.permitsanddiplomas.compat.jei;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;
import net.splakra.permitsanddiplomas.ui.PermitEditorScreen;

import java.util.List;

public class PermitEditorJeiHandler implements IGuiContainerHandler<PermitEditorScreen> {

    @Override
    public List<Rect2i> getGuiExtraAreas(PermitEditorScreen screen) {
        return List.of(new Rect2i(
                screen.getGuiLeft() + 176,
                screen.getGuiTop() + 16,
                128,
                56
        ));
    }
}
