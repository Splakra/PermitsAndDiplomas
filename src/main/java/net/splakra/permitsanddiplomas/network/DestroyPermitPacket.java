package net.splakra.permitsanddiplomas.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.splakra.permitsanddiplomas.item.custom.PermitItem;
import net.splakra.permitsanddiplomas.storage.DataStorage;
import net.splakra.permitsanddiplomas.storage.WorldDataManager;
import net.splakra.permitsanddiplomas.util.CustomUtils;

import java.util.function.Supplier;

public class DestroyPermitPacket {
    private final String text;

    public DestroyPermitPacket(String text) {
        this.text = text;
    }

    public DestroyPermitPacket(FriendlyByteBuf buffer) {
        this.text = buffer.readUtf(32767);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.text);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) return; // Ensure player is valid

            // Get the item in the offhand
            ItemStack permitItem = CustomUtils.GetItemOfHandByClass(player, PermitItem.class);
            if (!permitItem.isEmpty()) {
                String oldNBT = "";
                CompoundTag tag = permitItem.getOrCreateTag();
                if (tag.contains("content")) {
                    oldNBT = tag.getString("content");
                }
                tag.putString("content", text);
                permitItem.setTag(tag);

                // Modify server-side data
                DataStorage data = WorldDataManager.getOverworldData();
                if (data != null) {
                    data.addUsedPermit(text);
                    data.removeUsedPermit(oldNBT);
                }

                player.playSound(SoundEvents.BOOK_PUT,0.3f,1f);
            }
        });
        context.get().setPacketHandled(true);
    }
}
