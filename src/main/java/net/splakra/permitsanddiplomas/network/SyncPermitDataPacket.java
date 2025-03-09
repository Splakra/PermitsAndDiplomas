package net.splakra.permitsanddiplomas.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncPermitDataPacket {
    private final CompoundTag tag;

    public SyncPermitDataPacket(ItemStack itemStack) {
        this.tag = itemStack.getOrCreateTag().copy();
    }

    public SyncPermitDataPacket(FriendlyByteBuf buf) {
        this.tag = buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(tag);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = context.get().getSender();
            if (player != null) {
                ItemStack offhandItem = player.getOffhandItem();
                if (!offhandItem.isEmpty()) {
                    offhandItem.setTag(tag);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}