package net.splakra.permitsanddiplomas.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.splakra.permitsanddiplomas.PermitMod;
import net.splakra.permitsanddiplomas.util.CustomUtils;

public class PermitEditorScreen extends AbstractContainerScreen<PermitEditorMenu> {
    private final Component title = Component.literal("Permit Editor");
    private EditBox textField;
    private final ResourceLocation TEXTURE = new ResourceLocation(PermitMod.MOD_ID, "textures/gui/permit_editor_ui.png");

    public PermitEditorScreen(PermitEditorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        // Text Field
        this.textField = new EditBox(this.font, this.leftPos + 10, this.topPos + 20, 120, 20, Component.literal(CustomUtils.GetNBTofPermit(this.minecraft.player)));
        this.textField.setMaxLength(50);
        this.addRenderableWidget(this.textField);

        // Confirm Button
        this.addRenderableWidget(
                Button.builder(Component.literal("Confirm"), button -> {
                            menu.saveTextToOffhand(textField.getValue());
                            this.minecraft.player.closeContainer();
                        })
                        .bounds(this.leftPos + 10, this.topPos + 50, 60, 20)
                        .build()
        );

        // Exit Button
        this.addRenderableWidget(
                Button.builder(Component.literal("Exit"), button -> {
                            this.minecraft.player.closeContainer();
                        })
                        .bounds(this.leftPos + 80, this.topPos + 50, 60, 20)
                        .build()
        );
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        graphics.drawString(this.font, this.title, this.leftPos + 10, this.topPos + 5, 0xFFFFFF);
        this.textField.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
    }

    //Disables The window closing on "e" when in the text field
    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (textField.isFocused()) {
            if(pKeyCode == this.minecraft.options.keyInventory.getKey().getValue()){
                return true;
            }
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}