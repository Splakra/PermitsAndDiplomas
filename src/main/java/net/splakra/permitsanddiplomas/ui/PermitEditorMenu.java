package net.splakra.permitsanddiplomas.ui;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import net.splakra.permitsanddiplomas.item.custom.PermitItem;
import net.splakra.permitsanddiplomas.network.PacketHandler;
import net.splakra.permitsanddiplomas.network.SyncPermitDataPacket;
import net.splakra.permitsanddiplomas.storage.DataStorage;
import net.splakra.permitsanddiplomas.storage.WorldDataManager;
import net.splakra.permitsanddiplomas.util.CustomUtils;

public class PermitEditorMenu extends AbstractContainerMenu {
    private final Player player;
    private final SimpleContainerData data;

    public PermitEditorMenu(int id, Inventory inventory, SimpleContainerData data) {
        super(ModMenus.PERMIT_EDITOR_MENU.get(), id);
        this.player = inventory.player;
        this.data = data;
    }

    public void saveTextToOffhand(String text) {
        // Get the item in the offhand
        ItemStack permitItem = CustomUtils.GetItemOfHandByClass(player, PermitItem.class);

        // Ensure the player has an item in the offhand
        if (!permitItem.isEmpty()) {
            String oldNBT = "";
            CompoundTag tag = permitItem.getOrCreateTag();
            if (tag.contains("content")) {
                 oldNBT = tag.getString("content");
            }
            tag.putString("content", text);
            permitItem.setTag(tag);

            // Sync the data to the client
            if (!player.level().isClientSide) {
                PacketHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> (net.minecraft.server.level.ServerPlayer) player),
                    new SyncPermitDataPacket(permitItem)
                );
                DataStorage data = WorldDataManager.getOverworldData();
                data.addUsedPermit(text);
                data.removeUsedPermit(oldNBT);

            }
            player.playSound(SoundEvents.BOOK_PUT,0.3f,1f);
        }
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
