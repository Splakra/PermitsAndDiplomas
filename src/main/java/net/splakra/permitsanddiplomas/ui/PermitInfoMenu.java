package net.splakra.permitsanddiplomas.ui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.splakra.permitsanddiplomas.storage.PermitEntry;

import java.util.ArrayList;
import java.util.List;

public class PermitInfoMenu extends AbstractContainerMenu {
    protected List<PermitEntry> allEntries;
    private final Player player;

    // Constructor for use on both sides
    public PermitInfoMenu(int id, Inventory inventory) {
        super(ModMenus.PERMIT_INFO_MENU.get(), id);
        this.player = inventory.player;
    }

    public PermitInfoMenu(int id, Inventory inventory, List<PermitEntry> entries) {
        this(id, inventory);
        this.allEntries = entries;
    }

    // Constructor Forge uses on client when opening from server
    public PermitInfoMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory); // Add buf.read...() if needed
        int size = buf.readInt();
        allEntries = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String title = buf.readUtf();
            String playerName = buf.readUtf();
            Boolean accomplished = buf.readBoolean();
            String itemString = buf.readUtf();
            allEntries.add(new PermitEntry(title, playerName, itemString, accomplished));
        }
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
