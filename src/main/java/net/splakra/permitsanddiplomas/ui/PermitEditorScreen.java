package net.splakra.permitsanddiplomas.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.splakra.permitsanddiplomas.PermitMod;
import net.splakra.permitsanddiplomas.item.custom.PermitItem;
import net.splakra.permitsanddiplomas.network.PacketHandler;
import net.splakra.permitsanddiplomas.network.SavePermitDataPacket;
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
        this.textField = new EditBox(this.font, this.leftPos + 178, this.topPos + 24, 120, 20, Component.literal(""));
        this.textField.setValue(CustomUtils.GetContentOfPermitInHand(getMinecraft().player));
        this.textField.setMaxLength(50);
        this.addRenderableWidget(this.textField);

        // Confirm Button
        this.addRenderableWidget(
                Button.builder(Component.literal("Confirm"), button -> {
                            NonNullList<ItemStack> stacks = NonNullList.create();
                            for (int i = 0; i < 54; ++i) {
                                ItemStack item = menu.filterInventory.getStackInSlot(i);
                                if (!(item.equals(ItemStack.EMPTY) || stacks.contains(item))) {
                                    stacks.add(item.copyWithCount(1));
                                }
                            }
                            ItemStack permitItem = CustomUtils.GetPermitInHands(getMinecraft().player);

                            PacketHandler.INSTANCE.sendToServer(new SavePermitDataPacket(textField.getValue(), stacks, permitItem)); // Send packet
                            this.minecraft.player.closeContainer();
                        })
                        .bounds(this.leftPos + 176, this.topPos + 53, 60, 20)
                        .build()
        );

        // Exit Button
        this.addRenderableWidget(
                Button.builder(Component.literal("Exit"), button -> {
                            this.minecraft.player.closeContainer();
                        })
                        .bounds(this.leftPos + 241, this.topPos + 53, 60, 20)
                        .build()
        );
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, 320, 220,320, 256);
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