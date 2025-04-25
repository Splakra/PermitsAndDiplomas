package net.splakra.permitsanddiplomas;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.splakra.permitsanddiplomas.config.CommonConfig;
import net.splakra.permitsanddiplomas.item.ModCreativeModeTabs;
import net.splakra.permitsanddiplomas.item.ModItems;
import net.splakra.permitsanddiplomas.storage.WorldDataManager;
import net.splakra.permitsanddiplomas.ui.ModMenus;
import net.splakra.permitsanddiplomas.network.PacketHandler;
import net.splakra.permitsanddiplomas.ui.ModScreens;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PermitMod.MOD_ID)
public class PermitMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "permits_and_diplomas";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static WorldDataManager worldDataManager;

    public PermitMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        //Register the new creative mode tabs
        ModCreativeModeTabs.register(modEventBus);

        //Register all the new Items
        ModItems.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        //Register the PacketHandler and the Menu which uses it
        PacketHandler.register();
        ModMenus.register(modEventBus);

        //Register the config
        context.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC, "permits_and_diplomas-common-config.toml");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("Server is starting! Setting up WorldDataManager.");
        WorldDataManager.setServer(event.getServer());
    }
}
