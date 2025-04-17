package net.splakra.permitsanddiplomas.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.splakra.permitsanddiplomas.network.OpenPermitInfoScreenPacket;
import net.splakra.permitsanddiplomas.network.PacketHandler;

public class CustomInventoryScreen extends InventoryScreen {
    public CustomInventoryScreen(Inventory playerInventory) {
        super(playerInventory.player);
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(
                Button.builder(Component.literal("Info"), button -> {
                            PacketHandler.INSTANCE.sendToServer(new OpenPermitInfoScreenPacket());
                        })
                        .bounds(this.leftPos + 152, this.topPos + 6, 40, 20) // Feel free to tweak size
                        .build()
        );
    }
}
