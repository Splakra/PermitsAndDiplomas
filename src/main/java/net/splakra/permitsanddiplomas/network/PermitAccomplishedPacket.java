package net.splakra.permitsanddiplomas.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import net.splakra.permitsanddiplomas.storage.WorldDataManager;
import net.splakra.permitsanddiplomas.util.ServerUtils;

import java.util.function.Supplier;

public class PermitAccomplishedPacket {
    public final String title;

    public PermitAccomplishedPacket(String title) {
        this.title = title;

    }

    // --- ENCODE to buffer ---
    public static void encode(PermitAccomplishedPacket pkt, FriendlyByteBuf buf) {
        buf.writeUtf(pkt.title, 32767);
    }

    // --- DECODE from buffer ---
    public static PermitAccomplishedPacket decode(FriendlyByteBuf buf) {
        String text = buf.readUtf(32767);
        return new PermitAccomplishedPacket(text);
    }

    // --- HANDLE on the server ---
    public static void handle(PermitAccomplishedPacket pkt, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;
            // get your per‐world data
            var data = WorldDataManager.getOverworldData();
            data.toggleAccomplished(pkt.title);

            ServerUtils.spawnRing(player,1,50);
            player.serverLevel().playSound(null, player.blockPosition(), SoundEvents.NOTE_BLOCK_PLING.get(), SoundSource.BLOCKS, 0.4f, 2f);
        });
        ctx.setPacketHandled(true);
    }
}
