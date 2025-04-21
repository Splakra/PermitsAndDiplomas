package net.splakra.permitsanddiplomas.ui;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Inventory;
import net.splakra.permitsanddiplomas.PermitMod;
import net.splakra.permitsanddiplomas.network.PacketHandler;
import net.splakra.permitsanddiplomas.network.PermitAccomplishedPacket;
import net.splakra.permitsanddiplomas.storage.PermitEntry;

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
    private int personalVisibleEntryCount = 4;

    private final int windowHeight = 256;
    private final int windowWidth = 381;

    private int entryListX = this.leftPos + 22;
    private int entryListY = this.topPos + 10;
    private int entryListWidth = 200;
    private int entryListHeight = 234;

    private int personalListX = this.leftPos + 251;
    private int personalListY = this.topPos + 57;
    private int personalListWidth = 129;
    private int personalListHeight = 100;

    private int hoverOffset;

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
                            getMinecraft().player.closeContainer();
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
        hoverOffset = 0;
        // Enable scissor box to clip the entries
        graphics.enableScissor(entryListX, entryListY, entryListX + entryListWidth, entryListY + entryListHeight);

        visibleEntryCount = entryListHeight / entryHeight + 2;

        entryListWidth = 200;

        int startIndex = (int) (scrollAmount / entryHeight);
        int yOffset = entryListY - (int) (scrollAmount % entryHeight);

        entries.clear();
        personalEntries.clear();
        entries = new ArrayList<>(menu.allEntries.stream().filter(entry -> !entry.getPlayerName().equalsIgnoreCase(getMinecraft().player.getScoreboardName()) && !entry.getPlayerName().equalsIgnoreCase("unclaimed")).toList());
        entries.addAll(menu.allEntries.stream().filter(entry -> entry.getPlayerName().equalsIgnoreCase("unclaimed")).toList());

        for (int i = startIndex; i < entries.size() && i < startIndex + visibleEntryCount + 1; i++) {
            PermitEntry entry = entries.get(i);

//            if (entry.getPlayerName().equalsIgnoreCase("unclaimed")){
//                renderButtonBackground(graphics, entryListX, yOffset, entryListWidth, -20);
//            } else renderButtonBackground(graphics, entryListX, yOffset, entryListWidth);
//            graphics.renderItem(entry.getFirstItem().getDefaultInstance(), entryListX + 5, yOffset + 2);
//            graphics.drawString(this.font, entry.getTitle() + " - " + entry.getPlayerName(), entryListX + 25, yOffset + 5, 0xFFFFFF);

            renderItemEntry(graphics, entryListX, yOffset, entryListWidth, entry, false);

            yOffset += entryHeight;
        }

        graphics.disableScissor();

        personalEntries = new ArrayList<>(menu.allEntries.stream().filter(entry -> entry.getPlayerName().equalsIgnoreCase(getMinecraft().player.getScoreboardName())).toList());
        personalListX = this.leftPos + 251;
        personalListY = this.topPos + 57;

        personalVisibleEntryCount = personalListHeight / entryHeight + 2;

        int personalStartIndex = (int) (personalScrollAmount / entryHeight);
        int personalYOffset = personalListY - (int) (personalScrollAmount % entryHeight);


        graphics.enableScissor(personalListX, personalListY, personalListX + personalListWidth, personalListY + personalListHeight);

        for (int i = personalStartIndex; i < personalEntries.size() && i < personalStartIndex + personalVisibleEntryCount + 1; i++) {
            PermitEntry entry = personalEntries.get(i);
            if (mouseY >= personalListY && mouseY >= personalYOffset && mouseY <= personalYOffset + entryHeight - 5 && mouseY < personalListY + entryListHeight && mouseX >= personalListX && mouseX <= personalListX + personalListWidth) {
                hoverOffset = 40;
            } else {
                hoverOffset = 0;
            }
            renderItemEntry(graphics, personalListX, personalYOffset, personalListWidth, entry, true);

            personalYOffset += entryHeight;
        }

        graphics.disableScissor();
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

        for (int i = personalStartIndex; i < personalEntries.size() && i < personalStartIndex + personalVisibleEntryCount + 1; i++) {

            if (mouseY >= personalListY && mouseY >= personalYOffset && mouseY <= personalYOffset + entryHeight - 5 && mouseY < personalListY + personalListHeight && mouseX >= personalListX && mouseX <= personalListX + personalListWidth) {
                renderItemsPopup(graphics, personalEntries.get(i).getItems(), mouseX, mouseY);
                break;
            }
            personalYOffset += entryHeight;
        }
    }

    private void renderItemEntry(GuiGraphics graphics, int x, int y, int width, PermitEntry permitEntry, boolean owned) {
        graphics.pose().pushPose();
        graphics.pose().translate(0f,0f,1f);
        int color = 16777215; //White
        if (permitEntry.getPlayerName().equalsIgnoreCase("unclaimed")) {
            color = 10526880; //Light Grey
            renderButtonBackground(graphics, x, y, width, 0);
        } else if (permitEntry.getAccomplished()) {
            renderButtonBackground(graphics, x, y, width, 40);
        } else {
            renderButtonBackground(graphics, x, y, width, 20);
        }

        if (owned) {
            renderScrollingStringWithItem(graphics, permitEntry.getTitle(), permitEntry.getFirstItem(), x, y, width, 20, color);
        } else {
            renderScrollingStringWithItem(graphics, permitEntry.getTitle() + " - " + permitEntry.getPlayerName(), permitEntry.getFirstItem(), x, y, width, 20, color);
        }
        graphics.pose().popPose();
    }


    private void renderButtonBackground(GuiGraphics graphics, int xStart, int yStart, int width, int pVOffset) {
        graphics.blit(BUTTON_TEXTURE, xStart, yStart, 0, pVOffset + hoverOffset, width - 2, 20);
        graphics.blit(BUTTON_TEXTURE, xStart + width - 2, yStart, 254, pVOffset + hoverOffset, 2, 20);
    }

    protected void renderScrollingStringWithItem(GuiGraphics pGuiGraphics, String pText, Item displayItem, int pX, int pY, int pWidth, int pHeight, int pColor) {
        Font pFont = getMinecraft().font;
        int i = pFont.width(pText);
        pWidth -= 6;

        if (displayItem != null) {
            pGuiGraphics.renderItem(displayItem.getDefaultInstance(), pX + 5, pY + 1);
            pX += 25;
            pWidth -= 25;
        }

        int j = (pY + (pY + pHeight) - 9) / 2 + 1;

        if (i > pWidth) {
            int maxScrollAmount = i - pWidth;
            double d0 = (double) Util.getMillis() / 1000.0D;
            double d1 = Math.max((double) maxScrollAmount * 0.5D, 3.0D);
            double d2 = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * d0 / d1)) / 2.0D + 0.5D;
            double d3 = Mth.lerp(d2, 0.0D, (double) maxScrollAmount);
            pGuiGraphics.enableScissor(pX, pY, pX + pWidth, pY + pHeight);
            pGuiGraphics.drawString(pFont, pText, pX - (int) d3, j, pColor);
            pGuiGraphics.disableScissor();
        } else {
            pGuiGraphics.drawString(pFont, pText, pX, j, pColor);
        }
    }

    private void renderItemsPopup(GuiGraphics graphics, List<Item> items, int mouseX, int mouseY) {
        int popupX = mouseX + 5;
        int popupY = mouseY + 5;
        int length = 120;

        for (var item : items) {
            length = Math.max(length, this.font.width(item.getName(item.getDefaultInstance())) + 30);
        }

        // Draw a simple box and the item names
        graphics.pose().pushPose();
        graphics.pose().translate(0f,0f,20f);
        graphics.fill(popupX, popupY, popupX + length, popupY + (15 * items.size()) + 15, 0xF0000000); // Background
        graphics.pose().popPose();

        graphics.pose().pushPose();
        graphics.pose().translate(0f,0f,30f);
        int offset = popupY + 5;
        for (var item : items) {
            graphics.renderItem(item.getDefaultInstance(), popupX + 5, offset);
            graphics.drawString(this.font, item.getName(item.getDefaultInstance()), popupX + 25, offset + 5, 0xFFFFFF);
            offset += 15; // Adjust space for each item
        }
        graphics.pose().popPose();
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        int totalHeight = entries.size() * entryHeight;
        int visibleHeight = (visibleEntryCount - 1 )* entryHeight;
        int scrollRange = Math.max(0, (totalHeight - visibleHeight) + 10);
        int personalScrollRange = Math.max(0, (personalEntries.size() * entryHeight) - ((personalVisibleEntryCount - 1) * entryHeight) + entryHeight);

        if (pMouseX > entryListX && pMouseX < entryListX + entryListWidth && pMouseY > entryListY && pMouseY < entryListY + entryListHeight) {
            scrollAmount -= pDelta * 15;
            scrollAmount = Mth.clamp(scrollAmount, 0, scrollRange);

            return true;
        }

        if (pMouseX > personalListX && pMouseX < personalListX + personalListWidth && pMouseY > personalListY && pMouseY < personalListY + personalListHeight) {
            personalScrollAmount -= pDelta * 15;
            personalScrollAmount = Mth.clamp(personalScrollAmount, 0, personalScrollRange);

            return true;
        }

        return false;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {

        int personalStartIndex = (int) (personalScrollAmount / entryHeight);
        int personalYOffset = personalListY - (int) (personalScrollAmount % entryHeight);

        for (int i = personalStartIndex; i < personalEntries.size() && i < personalStartIndex + personalVisibleEntryCount + 1; i++) {
            if (personalYOffset > personalListY + personalVisibleEntryCount * entryHeight) break;

            if (pMouseY >= personalListY && pMouseY >= personalYOffset && pMouseY <= personalYOffset + entryHeight - 5 && pMouseY < personalListY + entryListHeight && pMouseX >= personalListX && pMouseX <= personalListX + personalListWidth) {
                PacketHandler.INSTANCE.sendToServer(new PermitAccomplishedPacket(personalEntries.get(i).getTitle()));
                personalEntries.get(i).setAccomplished(!personalEntries.get(i).getAccomplished());
                return true;
            }
            personalYOffset += entryHeight;
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }
}