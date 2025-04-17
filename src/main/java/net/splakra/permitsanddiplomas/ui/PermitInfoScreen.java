package net.splakra.permitsanddiplomas.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.splakra.permitsanddiplomas.PermitMod;

public class PermitInfoScreen extends AbstractContainerScreen<PermitInfoMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PermitMod.MOD_ID, "textures/gui/custom_info.png");
    private final Component title = Component.literal("Custom Info Screen");

    public PermitInfoScreen(PermitInfoMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        super.init();

        // Exit Button
        this.addRenderableWidget(
                Button.builder(Component.literal("Close"), button -> {
                            this.minecraft.player.closeContainer();
                        })
                        .bounds(this.leftPos + 80, this.topPos + 50, 60, 20)
                        .build()
        );
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        graphics.drawString(this.font, this.title, this.leftPos + 10, this.topPos + 5, 0xFFFFFF);
        graphics.drawString(this.font, "Data: " + this.minecraft.player.getScore(), this.leftPos + 10, this.topPos + 30, 0xFFFFFF);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }
}
