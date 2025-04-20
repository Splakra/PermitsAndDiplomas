package net.splakra.permitsanddiplomas.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.splakra.permitsanddiplomas.storage.PermitEntry;
import net.splakra.permitsanddiplomas.storage.WorldDataManager;
import net.splakra.permitsanddiplomas.ui.PermitInfoMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class OpenPermitInfoScreenPacket {

    private List<PermitEntry> entries = new ArrayList<>();
    public OpenPermitInfoScreenPacket() {
    }

    public static void encode(OpenPermitInfoScreenPacket msg, FriendlyByteBuf buf) {
        // Nothing to encode
    }

    public static OpenPermitInfoScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenPermitInfoScreenPacket();
    }

    public static void handle(OpenPermitInfoScreenPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            List<PermitEntry> entries = WorldDataManager.getOverworldData().getPermitEntries();

            NetworkHooks.openScreen(
                    player,
                    new SimpleMenuProvider(
                            (id, inventory, playerEntity) -> new PermitInfoMenu(id, inventory, entries),
                            Component.literal("Permit Info")
                    ),
                    buf -> {
                        buf.writeInt(entries.size());
                        for (PermitEntry entry : entries) {
                            buf.writeUtf(entry.getTitle());
                            buf.writeUtf(entry.getPlayerName());
                            buf.writeBoolean(entry.getAccomplished());
                            buf.writeUtf(entry.getItemsAsString());
                        }
                    }
            );
        });
        ctx.get().setPacketHandled(true);
    }
}