package net.splakra.permitsanddiplomas.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent;
import net.splakra.permitsanddiplomas.ui.PermitInfoMenu;

import java.util.function.Supplier;

public class OpenPermitInfoScreenPacket {

    public OpenPermitInfoScreenPacket() {}

    public static void encode(OpenPermitInfoScreenPacket msg, FriendlyByteBuf buf) {
        // Nothing to encode
    }

    public static OpenPermitInfoScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenPermitInfoScreenPacket();
    }

    public static void handle(OpenPermitInfoScreenPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                player.openMenu(new SimpleMenuProvider(
                        (id, inventory, playerEntity) -> new PermitInfoMenu(id, inventory),
                        Component.literal("Permit Info")
                ));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}