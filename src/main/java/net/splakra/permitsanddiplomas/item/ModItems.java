package net.splakra.permitsanddiplomas.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.splakra.permitsanddiplomas.PermitMod;
import net.splakra.permitsanddiplomas.item.custom.PermitItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PermitMod.MOD_ID);

    public static final RegistryObject<Item> PERMIT_ENVELOPE_1 = ITEMS.register("permit_envelope_1", () -> new PermitItem(new Item.Properties()));
    public static final RegistryObject<Item> PERMIT_ENVELOPE_2 = ITEMS.register("permit_envelope_2", () -> new PermitItem(new Item.Properties()));
    public static final RegistryObject<Item> PERMIT_ENVELOPE_3 = ITEMS.register("permit_envelope_3", () -> new PermitItem(new Item.Properties()));
    public static final RegistryObject<Item> PERMIT_ENVELOPE_4 = ITEMS.register("permit_envelope_4", () -> new PermitItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {

    }
}
