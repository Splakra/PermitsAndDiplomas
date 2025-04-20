package net.splakra.permitsanddiplomas.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.splakra.permitsanddiplomas.PermitMod;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CustomButton extends AbstractButton {

    public static final int SMALL_WIDTH = 120;
    public static final int DEFAULT_WIDTH = 150;
    public static final int DEFAULT_HEIGHT = 20;

    protected int yTexOffset = 0;
    protected ResourceLocation tex;
    protected boolean clickable = true;

    protected final Item displayItem;

    protected static final CustomButton.CreateNarration DEFAULT_NARRATION = (p_253298_) -> {
        return p_253298_.get();
    };
    protected final CustomButton.OnPress onPress;
    protected final CustomButton.CreateNarration createNarration;

    public static CustomButton.Builder builder(Component pMessage, CustomButton.OnPress pOnPress) {
        return new CustomButton.Builder(pMessage, pOnPress);
    }

    protected CustomButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, CustomButton.OnPress pOnPress, CustomButton.CreateNarration pCreateNarration, ResourceLocation pTex, Item pItem, int pYTexOffset, boolean pClickable) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.onPress = pOnPress;
        this.createNarration = pCreateNarration;
        this.tex = pTex;
        this.displayItem = pItem;
        this.yTexOffset = pYTexOffset;
        this.clickable = pClickable;
    }

    protected CustomButton(CustomButton.Builder builder) {
        this(builder.x, builder.y, builder.width, builder.height, builder.message, builder.onPress, builder.createNarration, builder.tex, builder.item, builder.yOffset, builder.clickable);
        setTooltip(builder.tooltip); // Forge: Make use of the Builder tooltip
    }

    public void onPress() {
        this.onPress.onPress(this);
    }

    protected MutableComponent createNarrationMessage() {
        return this.createNarration.createNarrationMessage(super::createNarrationMessage);
    }

    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        this.defaultButtonNarrationText(pNarrationElementOutput);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        pGuiGraphics.blitNineSliced(tex, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 256, 20, 0, this.getTextureY());
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = getFGColor();
        this.renderString(pGuiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }

//    int i = this.getX() + pWidth;
//    int j = this.getX() + this.getWidth() - pWidth;
//    renderScrollingString(pGuiGraphics, pFont, this.getMessage(), i, this.getY(), j, this.getY() + this.getHeight(), pColor);

    @Override
    protected void renderScrollingString(GuiGraphics pGuiGraphics, Font pFont, int pWidth, int pColor) {
        FormattedText pText = getMessage();
        int i = pFont.width(pText);
        int pMinY = this.getY();
        int pMaxY = this.getY() + this.getHeight();
        int pMinX = this.getX() + pWidth;
        int pMaxX = this.getX() + this.getWidth() - pWidth;

        if (displayItem != null){
            pGuiGraphics.renderItem(displayItem.getDefaultInstance(), this.getX() + 5, pMinY + 1);
            pMinX += 20;
        }

        int j = (pMinY + pMaxY - 9) / 2 + 1;
        int k = pMaxX - pMinX;

        if (i > k) {
            int l = i - k;
            double d0 = (double) Util.getMillis() / 1000.0D;
            double d1 = Math.max((double) l * 0.5D, 3.0D);
            double d2 = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * d0 / d1)) / 2.0D + 0.5D;
            double d3 = Mth.lerp(d2, 0.0D, (double) l);
            pGuiGraphics.enableScissor(pMinX, pMinY, pMaxX, pMaxY);
            pGuiGraphics.drawString(pFont, pText.getString(), pMinX - (int) d3, j, pColor);
            pGuiGraphics.disableScissor();
        } else {
            pGuiGraphics.drawString(pFont, pText.getString(), pMinX, j, pColor);
        }
    }

    @Override
    protected boolean isValidClickButton(int pButton) {
        return clickable && super.isValidClickButton(pButton);
    }

    private int getTextureY() {
        int i = 20;
        int offset = this.yTexOffset;
        if (!this.active) {
            i = 0;
            offset = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 60;
        }
        return i + offset;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Builder {
        private final Component message;
        private final CustomButton.OnPress onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private ResourceLocation tex;
        private Item item = null;
        private int yOffset = 0;
        private boolean clickable = true;

        private CustomButton.CreateNarration createNarration = CustomButton.DEFAULT_NARRATION;

        public Builder(Component pMessage, CustomButton.OnPress pOnPress) {
            this.message = pMessage;
            this.onPress = pOnPress;
        }

        public CustomButton.Builder pos(int pX, int pY) {
            this.x = pX;
            this.y = pY;
            return this;
        }

        public CustomButton.Builder tex(ResourceLocation pTex) {
            this.tex = pTex;
            return this;
        }

        public CustomButton.Builder item(Item pItem) {
            this.item = pItem;
            return this;
        }

        public CustomButton.Builder yOffset(int pYOffset) {
            this.yOffset = pYOffset;
            return this;
        }

        public CustomButton.Builder clickable(boolean pClickable) {
            this.clickable = pClickable;
            return this;
        }

        public CustomButton.Builder width(int pWidth) {
            this.width = pWidth;
            return this;
        }

        public CustomButton.Builder size(int pWidth, int pHeight) {
            this.width = pWidth;
            this.height = pHeight;
            return this;
        }

        public CustomButton.Builder bounds(int pX, int pY, int pWidth, int pHeight) {
            return this.pos(pX, pY).size(pWidth, pHeight);
        }

        public CustomButton.Builder tooltip(@Nullable Tooltip pTooltip) {
            this.tooltip = pTooltip;
            return this;
        }

        public CustomButton.Builder createNarration(CustomButton.CreateNarration pCreateNarration) {
            this.createNarration = pCreateNarration;
            return this;
        }

        public CustomButton build() {
            return build(CustomButton::new);
        }

        public CustomButton build(java.util.function.Function<CustomButton.Builder, CustomButton> builder) {
            return builder.apply(this);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public interface CreateNarration {
        MutableComponent createNarrationMessage(Supplier<MutableComponent> pMessageSupplier);
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(CustomButton pButton);
    }
}
