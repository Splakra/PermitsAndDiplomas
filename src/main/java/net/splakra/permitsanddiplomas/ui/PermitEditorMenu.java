package net.splakra.permitsanddiplomas.ui;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;


public class PermitEditorMenu extends AbstractContainerMenu {
    private final Player player;
    private final SimpleContainerData data;

    public PermitEditorMenu(int id, Inventory inventory, SimpleContainerData data) {
        super(ModMenus.PERMIT_EDITOR_MENU.get(), id);
        this.player = inventory.player;
        this.data = data;
    }

    // Client-side constructor (called when opening from server)
    public PermitEditorMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, new SimpleContainerData(buf.readVarInt())); // Or hardcode size if known
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
