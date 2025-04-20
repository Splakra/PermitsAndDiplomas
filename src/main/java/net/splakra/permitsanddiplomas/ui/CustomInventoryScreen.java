package net.splakra.permitsanddiplomas.ui;


import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.splakra.permitsanddiplomas.item.ModItems;
import net.splakra.permitsanddiplomas.network.OpenPermitInfoScreenPacket;
import net.splakra.permitsanddiplomas.network.PacketHandler;

public class CustomInventoryScreen extends InventoryScreen {
    public CustomInventoryScreen(Inventory playerInventory) {
        super(playerInventory.player);
    }

    CustomIconButton infoButton;

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(
                infoButton = CustomIconButton.builder(ModItems.PERMIT_LETTER_2.get(), button -> {
                            PacketHandler.INSTANCE.sendToServer(new OpenPermitInfoScreenPacket());
                        })
                .bounds(this.leftPos + this.imageWidth -42, this.topPos + 61, 20, 18)
                .build()
        );
    }

    @Override
    public void containerTick() {
        infoButton.setX(this.leftPos + this.imageWidth -42);
        super.containerTick();
    }
}
