package net.splakra.permitsanddiplomas.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.splakra.permitsanddiplomas.PermitMod;

import java.util.Optional;

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
        INSTANCE.registerMessage(id++, OpenPermitInfoScreenPacket.class, OpenPermitInfoScreenPacket::encode, OpenPermitInfoScreenPacket::decode, OpenPermitInfoScreenPacket::handle);
        INSTANCE.registerMessage(id++, SavePermitDataPacket.class, SavePermitDataPacket::encode, SavePermitDataPacket::decode, SavePermitDataPacket::handle);
        INSTANCE.registerMessage(id++, PermitAccomplishedPacket.class, PermitAccomplishedPacket::encode, PermitAccomplishedPacket::decode, PermitAccomplishedPacket::handle);
    }
}