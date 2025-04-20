package net.splakra.permitsanddiplomas.compat.jei;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;
import net.splakra.permitsanddiplomas.ui.PermitInfoScreen;

import java.util.List;

public class PermitInfoJeiHandler implements IGuiContainerHandler<PermitInfoScreen> {

    @Override
    public List<Rect2i> getGuiExtraAreas(PermitInfoScreen screen) {
        return List.of(new Rect2i(
                screen.getGuiLeft() + 248,
                screen.getGuiTop() + 40,
                134,
                133
        ), new Rect2i(
                screen.getGuiLeft(),
                screen.getGuiTop(),
                248,
                256
                )
        );
    }
}
