package net.splakra.permitsanddiplomas.ui;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.splakra.permitsanddiplomas.item.custom.PermitItem;
import net.splakra.permitsanddiplomas.storage.WorldDataManager;
import net.splakra.permitsanddiplomas.util.CustomUtils;
import org.jetbrains.annotations.NotNull;


public class PermitEditorMenu extends AbstractContainerMenu {
    private final Player player;
    private final SimpleContainerData data;

    public final ItemStackHandler filterInventory = new ItemStackHandler(54);

    public PermitEditorMenu(int id, Inventory inventory, SimpleContainerData data) {
        super(ModMenus.PERMIT_EDITOR_MENU.get(), id);

        // --- Filter slots (52) in a 4×13 grid ---
        final int cols = 9;
        final int slotSize = 18;
        // offsets are relative to your GUI texture; tweak these to match
        final int startX = 8;
        final int startY = 18;

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < cols; col++) {
                int index = (row * cols) + col;
                int x = startX + col * slotSize;
                int y = startY + row * slotSize;
                this.addSlot(new SlotItemHandler(filterInventory, index, x, y) {
                    @Override
                    public int getMaxStackSize() {
                        return 1;       // only one item per slot
                    }

                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        if (!this.hasItem()) this.set(stack.copyWithCount(1));
                        return false;    // or add your own filter logic here
                    }

                    @Override
                    public void onTake(Player pPlayer, ItemStack pStack) {
                        if (getCarried().getItem().equals(pStack.getItem()))
                        {
                            getCarried().setCount(getCarried().getCount() - 1);
                        }
                        this.set(ItemStack.EMPTY);
                    }

                    @Override
                    public int getMaxStackSize(@NotNull ItemStack stack) {
                        return 1;
                    }

                    @Override
                    public boolean mayPickup(Player playerIn) {
                        return true;
                    }
                });
            }
        }

        player = inventory.player;
        CompoundTag permitTags = CustomUtils.GetPermitInHands(player).getTag();
        int idx = 0;
        for (var permitEntryItem : WorldDataManager.getOverworldData().getPermitEntry(permitTags.getString("content")).getItems()){
            filterInventory.insertItem(idx, permitEntryItem.getDefaultInstance().copyWithCount(1), false);
            idx++;
        }


        // --- Player inventory slots (3 rows of 9) ---
        final int invStartX = startX;               // align with filter grid
        final int invStartY = startY + 6 * slotSize + 13;  // some padding below

        // main inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = col + row * 9 + 9;
                int x = invStartX + col * slotSize;
                int y = invStartY + row * slotSize;
                this.addSlot(new Slot(inventory, slotIndex, x, y));
            }
        }

        // hotbar
        int hotbarY = invStartY + 3 * slotSize + 4;
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inventory, col, invStartX + col * slotSize, hotbarY));
        }

        this.data = data;
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
