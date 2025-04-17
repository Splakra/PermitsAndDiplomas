package net.splakra.permitsanddiplomas.ui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class PermitInfoMenu extends AbstractContainerMenu {
    private final Player player;

    // Constructor for use on both sides
    public PermitInfoMenu(int id, Inventory inventory) {
        super(ModMenus.PERMIT_INFO_MENU.get(), id);
        this.player = inventory.player;
    }

    // Constructor Forge uses on client when opening from server
    public PermitInfoMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory); // Add buf.read...() if needed
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
