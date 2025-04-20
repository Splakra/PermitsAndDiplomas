package net.splakra.permitsanddiplomas.network;

import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.splakra.permitsanddiplomas.storage.WorldDataManager;
import net.splakra.permitsanddiplomas.util.CustomUtils;
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
            player.playSound(SoundEvents.FIREWORK_ROCKET_TWINKLE,0.3f,1f);
        });
        ctx.setPacketHandled(true);
    }
}
