package net.splakra.permitsanddiplomas.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.splakra.permitsanddiplomas.PermitMod;


import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CustomIconButton extends AbstractButton {


    protected int yTexOffset = 0;
    protected ResourceLocation tex = new ResourceLocation(PermitMod.MOD_ID, "textures/gui/extra_widgets.png");

    protected final Item displayItem;

    protected static final CustomIconButton.CreateNarration DEFAULT_NARRATION = (p_253298_) -> {
        return p_253298_.get();
    };
    protected final CustomIconButton.OnPress onPress;
    protected final CustomIconButton.CreateNarration createNarration;

    public static CustomIconButton.Builder builder(Item displayItem, CustomIconButton.OnPress pOnPress) {
        return new CustomIconButton.Builder(displayItem, pOnPress);
    }

    protected CustomIconButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, CustomIconButton.OnPress pOnPress, CustomIconButton.CreateNarration pCreateNarration, Item pItem, int pYTexOffset) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.onPress = pOnPress;
        this.createNarration = pCreateNarration;
        this.displayItem = pItem;
        this.yTexOffset = pYTexOffset;
    }

    protected CustomIconButton(CustomIconButton.Builder builder) {
        this(builder.x, builder.y, builder.width, builder.height, builder.message, builder.onPress, builder.createNarration, builder.item, builder.yOffset);
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

    //116 : 61
    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        pGuiGraphics.blit(tex, this.getX(), this.getY(), (isHoveredOrFocused() ? 20 : 0),100,20,18);
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        pGuiGraphics.renderItem(displayItem.getDefaultInstance(), this.getX() + 2, this.getY() + 1);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Builder {
        private final Component message;
        private final CustomIconButton.OnPress onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private ResourceLocation tex;
        private Item item = null;
        private int yOffset = 0;

        private CustomIconButton.CreateNarration createNarration = CustomIconButton.DEFAULT_NARRATION;

        public Builder(Item item, CustomIconButton.OnPress pOnPress) {
            this.item = item;
            this.message = Component.empty();
            this.onPress = pOnPress;
        }

        public CustomIconButton.Builder pos(int pX, int pY) {
            this.x = pX;
            this.y = pY;
            return this;
        }

        public CustomIconButton.Builder yOffset(int pYOffset) {
            this.yOffset = pYOffset;
            return this;
        }

        public CustomIconButton.Builder width(int pWidth) {
            this.width = pWidth;
            return this;
        }

        public CustomIconButton.Builder size(int pWidth, int pHeight) {
            this.width = pWidth;
            this.height = pHeight;
            return this;
        }

        public CustomIconButton.Builder bounds(int pX, int pY, int pWidth, int pHeight) {
            return this.pos(pX, pY).size(pWidth, pHeight);
        }

        public CustomIconButton.Builder tooltip(@Nullable Tooltip pTooltip) {
            this.tooltip = pTooltip;
            return this;
        }

        public CustomIconButton.Builder createNarration(CustomIconButton.CreateNarration pCreateNarration) {
            this.createNarration = pCreateNarration;
            return this;
        }

        public CustomIconButton build() {
            return build(CustomIconButton::new);
        }

        public CustomIconButton build(java.util.function.Function<CustomIconButton.Builder, CustomIconButton> builder) {
            return builder.apply(this);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public interface CreateNarration {
        MutableComponent createNarrationMessage(Supplier<MutableComponent> pMessageSupplier);
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(CustomIconButton pButton);
    }
}
