package net.splakra.permitsanddiplomas.ui;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Inventory;
import net.splakra.permitsanddiplomas.PermitMod;
import net.splakra.permitsanddiplomas.storage.PermitEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermitInfoScreen extends AbstractContainerScreen<PermitInfoMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PermitMod.MOD_ID, "textures/gui/custom_info.png");
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation("minecraft", "textures/gui/widgets.png"); // Default button texture

    private final Component title = Component.literal("Permit Informations");

    private List<PermitEntry> entries = new ArrayList<>();

    //Scroll Settings
    private double scrollAmount = 0;
    private final int entryHeight = 25;
    private final int visibleEntryCount = 3;

    private final int windowHeight = 82;
    private final int windowWidth = 176;

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
                        .bounds(this.leftPos + 80, this.topPos + 80, 60, 20)
                        .build()
        );

        // For now, we manually add some test entries.
        // This would typically come from your dynamic data source.
        List<Item> items1 = List.of(Items.ITEM_FRAME, Items.DIAMOND);
        //List<Integer> items2 = List.of(new ItemStack(Items.EMERALD), new ItemStack(Item.byId(1).getDefaultInstance().getItem()));

        // Add PermitEntry dynamically.
        entries.add(new PermitEntry("Title 1", "Player 1", items1));
        entries.add(new PermitEntry("Title 2", "Player 2", items1));
        entries.add(new PermitEntry("Title 3", "Player 3", items1));
        entries.add(new PermitEntry("Title 4", "Player 4", items1));
        entries.add(new PermitEntry("Title 5", "Player 5", items1));
        entries.add(new PermitEntry("Title 6", "Player 6", items1));
        entries.add(new PermitEntry("Title 7", "Player 7", items1));

        // Add more elements if needed
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, windowWidth, windowHeight);

        int entryListX = this.leftPos + 10;
        int entryListY = this.topPos + 10;
        int entryListWidth = this.imageWidth - 20;

        // Enable scissor box to clip the entries
        graphics.enableScissor(entryListX, this.topPos + 2, entryListX + entryListWidth, this.topPos + windowHeight);

        int startIndex = (int) (scrollAmount / entryHeight);
        int yOffset = entryListY - (int) (scrollAmount % entryHeight);

        for (int i = startIndex; i < entries.size() && i < startIndex + visibleEntryCount + 1; i++) {
            PermitEntry entry = entries.get(i);

            renderButtonBackground(graphics, entryListX, yOffset, entryListWidth);
            graphics.drawString(this.font, entry.getTitle() + " - " + entry.getPlayerName(), entryListX + 5, yOffset + 5, 0xFFFFFF);

            yOffset += entryHeight;
        }

        graphics.disableScissor();
    }

    private void renderButtonBackground(GuiGraphics graphics, int xStart, int yStart, int width){
        graphics.blit(BUTTON_TEXTURE, xStart, yStart, 0, 66, width - 2, 20);
        graphics.blit(BUTTON_TEXTURE, xStart + width - 2, yStart, 198, 66, 2, 20);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        graphics.drawString(this.font, this.title, this.leftPos + 10, this.topPos - 10, 0xFFFFFF);

        // Entry list hover check with scroll offset
        int entryListX = this.leftPos + 10;
        int entryListY = this.topPos + 10;
        int entryListWidth = this.imageWidth - 20;

        int startIndex = (int) (scrollAmount / entryHeight);
        int yOffset = entryListY - (int) (scrollAmount % entryHeight);

        for (int i = startIndex; i < entries.size() && i < startIndex + visibleEntryCount + 1; i++) {
            if (yOffset > entryListY + visibleEntryCount * entryHeight) break;

            if (mouseY >= this.topPos && mouseY >= yOffset && mouseY <= yOffset + entryHeight - 5 && mouseY < this.topPos + windowHeight && mouseX >= entryListX && mouseX <= entryListX + entryListWidth) {
                renderItemsPopup(graphics, entries.get(i).getItems(), mouseX, mouseY);
                break;
            }

            yOffset += entryHeight;
        }
    }

    private void renderItemsPopup(GuiGraphics graphics, List<Item> items, int mouseX, int mouseY) {
        // Render the item popup (e.g., draw a box with the list of items)
        int popupX = mouseX + 5;
        int popupY = mouseY + 5;

        // Draw a simple box and the item names
        graphics.fill(popupX, popupY, popupX + 120, popupY + 20 * items.size(), 0xA0000000); // Background
        int offset = popupY + 5;
        for (var item : items) {
            graphics.renderItem(item.getDefaultInstance(), popupX + 5, offset);
            graphics.drawString(this.font, item.getName(item.getDefaultInstance()), popupX + 25, offset + 5, 0xFFFFFF);
            offset += 15; // Adjust space for each item
        }
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        int totalHeight = entries.size() * entryHeight;
        int visibleHeight = visibleEntryCount * entryHeight;
        int scrollRange = Math.max(0, totalHeight - visibleHeight);

        scrollAmount -= pDelta * 15;
        scrollAmount = Mth.clamp(scrollAmount, 0, scrollRange);
        return true;
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }
}