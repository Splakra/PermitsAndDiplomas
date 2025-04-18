package net.splakra.permitsanddiplomas.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Inventory;
import net.splakra.permitsanddiplomas.PermitMod;
import net.splakra.permitsanddiplomas.storage.DataStorage;
import net.splakra.permitsanddiplomas.storage.PermitEntry;
import net.splakra.permitsanddiplomas.storage.WorldDataManager;

import java.util.ArrayList;
import java.util.List;

public class PermitInfoScreen extends AbstractContainerScreen<PermitInfoMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PermitMod.MOD_ID, "textures/gui/custom_info.png");
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(PermitMod.MOD_ID, "textures/gui/extra_widgets.png"); // Default button texture

    private final Component title = Component.literal("Permit Informations");

    private List<PermitEntry> entries = new ArrayList<>();
    private List<PermitEntry> personalEntries = new ArrayList<>();

    //Scroll Settings
    private double scrollAmount = 0;
    private double personalScrollAmount = 0;
    private final int entryHeight = 25;
    private int visibleEntryCount = 10;

    private final int windowHeight = 256;
    private final int windowWidth = 381;

    int entryListX = this.leftPos + 22;
    int entryListY = this.topPos + 10;
    int entryListWidth = 200;
    int entryListHeight = 234;

    int personalListX = this.leftPos + 251;
    int personalListY = this.topPos + 57;
    int personalListWidth = 129;
    int personalListHeight = 100;

    public PermitInfoScreen(PermitInfoMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos -= 30;
        this.topPos -= 40;

        // Exit Button
        this.addRenderableWidget(
                Button.builder(Component.literal("Close"), button -> {
                            this.minecraft.player.closeContainer();
                        })
                        .bounds(this.leftPos + 87, this.topPos + 256, 60, 20)
                        .build()
        );
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, windowWidth, windowHeight, 384, 256);
        entryListX = this.leftPos + 22;
        entryListY = this.topPos + 10;
        // Enable scissor box to clip the entries
        graphics.enableScissor(entryListX, entryListY, entryListX + entryListWidth, entryListY + entryListHeight );

        visibleEntryCount = entryListHeight / entryHeight + 2;

        entryListWidth = 200;

        int startIndex = (int) (scrollAmount / entryHeight);
        int yOffset = entryListY - (int) (scrollAmount % entryHeight);

        DataStorage data = WorldDataManager.getOverworldData();
        entries = new ArrayList<>(data.getClaimedPermitEntries().stream().filter(entry -> !entry.getPlayerName().equalsIgnoreCase(getMinecraft().player.getScoreboardName())).toList());
        entries.addAll(data.getUnclaimedPermitEntries());

        for (int i = startIndex; i < entries.size() && i < startIndex + visibleEntryCount + 1; i++) {
            PermitEntry entry = entries.get(i);

            if (entry.getPlayerName().equalsIgnoreCase("unclaimed")){
                renderButtonBackground(graphics, entryListX, yOffset, entryListWidth, -20);
            } else renderButtonBackground(graphics, entryListX, yOffset, entryListWidth);
            graphics.renderItem(entry.getFirstItem().getDefaultInstance(), entryListX + 5, yOffset + 2);
            graphics.drawString(this.font, entry.getTitle() + " - " + entry.getPlayerName(), entryListX + 25, yOffset + 5, 0xFFFFFF);

            yOffset += entryHeight;
        }

        graphics.disableScissor();

        personalEntries = new ArrayList<>(data.getPermitEntries().stream().filter(entry -> entry.getPlayerName().equalsIgnoreCase(getMinecraft().player.getScoreboardName())).toList());
        personalListX = this.leftPos + 251;
        personalListY = this.topPos + 57;

        int personalStartIndex = (int) (personalScrollAmount / entryHeight);
        int personalYOffset = personalListY - (int) (personalScrollAmount % entryHeight);


        graphics.enableScissor(personalListX, personalListY, personalListX + personalListWidth, personalListY + personalListHeight );

        for (int i = personalStartIndex; i < personalEntries.size() && i < personalStartIndex + 1 + 1; i++) {
            PermitEntry entry = personalEntries.get(i);

            if (entry.getPlayerName().equalsIgnoreCase("unclaimed")){
                renderButtonBackground(graphics, personalListX, personalYOffset, personalListWidth, -20);
            } else renderButtonBackground(graphics, personalListX, personalYOffset, personalListWidth);
            graphics.renderItem(entry.getFirstItem().getDefaultInstance(), personalListX + 5, personalYOffset + 2);
            graphics.drawString(this.font, entry.getTitle(), personalListX + 25, personalYOffset + 5, 0xFFFFFF);

            personalYOffset += entryHeight;
        }

        graphics.disableScissor();


    }

    private void renderButtonBackground(GuiGraphics graphics, int xStart, int yStart, int width){
        renderButtonBackground(graphics, xStart, yStart, width, 0);
    }

    private void renderButtonBackground(GuiGraphics graphics, int xStart, int yStart, int width, int pVOffset){
        graphics.blit(BUTTON_TEXTURE, xStart, yStart, 0, 20 + pVOffset, width - 2, 20);
        graphics.blit(BUTTON_TEXTURE, xStart + width - 2, yStart, 254, 20 + pVOffset, 2, 20);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        graphics.drawString(this.font, this.title, this.leftPos + 10, this.topPos - 10, 0xFFFFFF);
        graphics.drawString(this.font, "Your Permits", this.personalListX + 30, this.personalListY - 10, 0xFFFFFF);

        int startIndex = (int) (scrollAmount / entryHeight);
        int personalStartIndex = (int) (personalScrollAmount / entryHeight);
        int yOffset = entryListY - (int) (scrollAmount % entryHeight);
        int personalYOffset = personalListY - (int) (personalScrollAmount % entryHeight);

        for (int i = startIndex; i < entries.size() && i < startIndex + visibleEntryCount + 1; i++) {
            if (yOffset > entryListY + visibleEntryCount * entryHeight) break;

            if (mouseY >= entryListY && mouseY >= yOffset && mouseY <= yOffset + entryHeight - 5 && mouseY < entryListY + entryListHeight && mouseX >= entryListX && mouseX <= entryListX + entryListWidth) {
                renderItemsPopup(graphics, entries.get(i).getItems(), mouseX, mouseY);
                break;
            }

            yOffset += entryHeight;
        }

        for (int i = personalStartIndex; i < personalEntries.size() && i < personalStartIndex + 1 + 1; i++) {
            if (personalYOffset > personalListY + 4 * entryHeight) break;

            if (mouseY >= personalListY && mouseY >= personalYOffset && mouseY <= personalYOffset + entryHeight - 5 && mouseY < personalListY + entryListHeight && mouseX >= personalListX && mouseX <= personalListX + personalListWidth) {
                renderItemsPopup(graphics, personalEntries.get(i).getItems(), mouseX, mouseY);
                break;
            }

            personalYOffset += entryHeight;
        }
    }

    private void renderItemsPopup(GuiGraphics graphics, List<Item> items, int mouseX, int mouseY) {
        // Render the item popup (e.g., draw a box with the list of items)
        int popupX = mouseX + 5;
        int popupY = mouseY + 5;

        // Draw a simple box and the item names
        graphics.fill(popupX, popupY, popupX + 120, popupY + (15 * items.size()) + 15, 0xA0000000); // Background
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
        int scrollRange = Math.max(0, totalHeight - visibleHeight + 10);
        int personalScrollRange = Math.max(0, (entryHeight * 4) - (entryHeight * 1) + 10);

        if (pMouseX > entryListX && pMouseX < entryListX + entryListWidth && pMouseY > entryListY && pMouseY < entryListY + entryListHeight) {
            scrollAmount -= pDelta * 15;
            scrollAmount = Mth.clamp(scrollAmount, 0, scrollRange);

            return true;
        }

        if (pMouseX > personalListX && pMouseX < personalListX + personalListWidth && pMouseY > personalListY && pMouseY < personalListY + personalListHeight) {
            personalScrollAmount -= pDelta * 15;
            personalScrollAmount = Mth.clamp(personalScrollAmount, 0, entryHeight * 0);

            return true;
        }

        return false;
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }
}