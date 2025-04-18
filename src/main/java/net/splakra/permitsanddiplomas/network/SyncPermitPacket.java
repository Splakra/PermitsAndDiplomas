package net.splakra.permitsanddiplomas.network;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.splakra.permitsanddiplomas.item.custom.PermitItem;
import net.splakra.permitsanddiplomas.util.CustomUtils;

public class SyncPermitPacket {
    private final CompoundTag tag;

    public SyncPermitPacket(CompoundTag tag) {
        this.tag = tag;
    }

    public static void encode(SyncPermitPacket pkt, FriendlyByteBuf buf) {
        buf.writeNbt(pkt.tag);
    }

    public static SyncPermitPacket decode(FriendlyByteBuf buf) {
        return new SyncPermitPacket(buf.readNbt());
    }

    public static void handle(SyncPermitPacket pkt, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        // run on the client thread
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                ctx.enqueueWork(() -> {
                    Minecraft mc = Minecraft.getInstance();
                    // find the PermitItem in your hand again
                    var player = mc.player;
                    if (player == null) return;
                    var permitStack = CustomUtils.GetPermitInHands(player);
                    if (!permitStack.isEmpty()) {
                        permitStack.setTag(pkt.tag);
                    }
                })
        );
        ctx.setPacketHandled(true);
    }
}
