package net.splakra.permitsanddiplomas.network;

import java.util.function.Supplier;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;
import net.splakra.permitsanddiplomas.storage.DataStorage;
import net.splakra.permitsanddiplomas.storage.WorldDataManager;
import net.splakra.permitsanddiplomas.util.CustomUtils;

public class SavePermitDataPacket {
    public final String text;
    public final NonNullList<ItemStack> filterStacks;
    public final ItemStack permit;

    public SavePermitDataPacket(String text, NonNullList<ItemStack> filterStacks, ItemStack permit) {
        this.text = text;
        this.filterStacks = filterStacks;
        this.permit = permit;
    }

    // --- ENCODE to buffer ---
    public static void encode(SavePermitDataPacket pkt, FriendlyByteBuf buf) {
        buf.writeUtf(pkt.text, 32767);
        buf.writeVarInt(pkt.filterStacks.size());
        for (ItemStack stack : pkt.filterStacks) {
            buf.writeItem(stack);
        }
        buf.writeItemStack(pkt.permit, false);
    }

    // --- DECODE from buffer ---
    public static SavePermitDataPacket decode(FriendlyByteBuf buf) {
        String text = buf.readUtf(32767);
        int size = buf.readVarInt();
        NonNullList<ItemStack> list = NonNullList.withSize(size, ItemStack.EMPTY);
        for (int i = 0; i < size; i++) {
            list.set(i, buf.readItem());
        }
        ItemStack permit = buf.readItem();
        return new SavePermitDataPacket(text, list, permit);
    }

    // --- HANDLE on the server ---
    public static void handle(SavePermitDataPacket pkt, Supplier<Context> ctxSupplier) {
        Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;
            // get your per‐world data
            var data = WorldDataManager.getOverworldData();
            data.handlePermitChange(pkt);


            ItemStack permit = CustomUtils.GetPermitInHands(player);
            if (!permit.isEmpty()) {
                CompoundTag tag = permit.getOrCreateTag();
                String oldNBT = tag.contains("content") ? tag.getString("content") : "";
                tag.putString("content", pkt.text);
                permit.setTag(tag);

                if (data != null) {
                    data.addUsedPermit(pkt.text);
                    data.removeUsedPermit(oldNBT);
                }

                player.inventoryMenu.broadcastChanges();

                player.playSound(SoundEvents.BOOK_PUT,0.3f,1f);
            }
        });
        ctx.setPacketHandled(true);
    }
}