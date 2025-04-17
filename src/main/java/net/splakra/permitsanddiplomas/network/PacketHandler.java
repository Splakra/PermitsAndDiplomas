package net.splakra.permitsanddiplomas.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.splakra.permitsanddiplomas.PermitMod;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PermitMod.MOD_ID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int id = 0;

    public static void register() {
        INSTANCE.registerMessage(id++, SavePermitTextPacket.class, SavePermitTextPacket::encode, SavePermitTextPacket::new, SavePermitTextPacket::handle);
        INSTANCE.registerMessage(id++, OpenPermitInfoScreenPacket.class, OpenPermitInfoScreenPacket::encode, OpenPermitInfoScreenPacket::decode, OpenPermitInfoScreenPacket::handle);
    }
}