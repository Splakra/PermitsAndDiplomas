package net.splakra.permitsanddiplomas.ui;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.splakra.permitsanddiplomas.PermitMod;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, PermitMod.MOD_ID);

    public static final RegistryObject<MenuType<PermitEditorMenu>> PERMIT_EDITOR_MENU =
            MENUS.register("permit_editor", () ->
                    IForgeMenuType.create((id, inventory, buffer) ->
                            new PermitEditorMenu(id, inventory, new SimpleContainerData(1))
                    )
            );

    // Register the Deferred Register to the event bus
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}