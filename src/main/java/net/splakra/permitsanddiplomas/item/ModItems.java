package net.splakra.permitsanddiplomas.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.splakra.permitsanddiplomas.PermitMod;
import net.splakra.permitsanddiplomas.item.custom.DataDeleteItem;
import net.splakra.permitsanddiplomas.item.custom.EnvelopeItem;
import net.splakra.permitsanddiplomas.item.custom.PermitEditorItem;
import net.splakra.permitsanddiplomas.item.custom.PermitItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PermitMod.MOD_ID);

    public static final RegistryObject<Item> PERMIT_ENVELOPE_1 = ITEMS.register("permit_envelope_1", () -> new EnvelopeItem(new Item.Properties().stacksTo(1), 1));
    public static final RegistryObject<Item> PERMIT_ENVELOPE_2 = ITEMS.register("permit_envelope_2", () -> new EnvelopeItem(new Item.Properties().stacksTo(1),2));
    public static final RegistryObject<Item> PERMIT_ENVELOPE_3 = ITEMS.register("permit_envelope_3", () -> new EnvelopeItem(new Item.Properties().stacksTo(1),3));
    public static final RegistryObject<Item> PERMIT_ENVELOPE_4 = ITEMS.register("permit_envelope_4", () -> new EnvelopeItem(new Item.Properties().stacksTo(1),4));

    public static final RegistryObject<Item> PERMIT_LETTER_1 = ITEMS.register("permit_letter_1", () -> new PermitItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PERMIT_LETTER_2 = ITEMS.register("permit_letter_2", () -> new PermitItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PERMIT_LETTER_3 = ITEMS.register("permit_letter_3", () -> new PermitItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PERMIT_LETTER_4 = ITEMS.register("permit_letter_4", () -> new PermitItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> DATA_DELETE_ITEM = ITEMS.register("data_delete_item", () -> new DataDeleteItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> PERMIT_EDITOR_ITEM = ITEMS.register("permit_editor_item", () -> new PermitEditorItem(new Item.Properties().stacksTo(1)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {

    }
}
