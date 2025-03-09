package net.splakra.permitsanddiplomas.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.splakra.permitsanddiplomas.PermitMod;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PermitMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> PERMIT_TAB = CREATIVE_MODE_TABS.register("permit_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.PERMIT_ENVELOPE_4.get()))
                    .title(Component.translatable("creativeTab.permit_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept((ModItems.PERMIT_ENVELOPE_1.get()));
                        pOutput.accept((ModItems.PERMIT_ENVELOPE_2.get()));
                        pOutput.accept((ModItems.PERMIT_ENVELOPE_3.get()));
                        pOutput.accept((ModItems.PERMIT_ENVELOPE_4.get()));

                        pOutput.accept((ModItems.PERMIT_LETTER_1.get()));
                        pOutput.accept((ModItems.PERMIT_LETTER_2.get()));
                        pOutput.accept((ModItems.PERMIT_LETTER_3.get()));
                        pOutput.accept((ModItems.PERMIT_LETTER_4.get()));

                        pOutput.accept((ModItems.DATA_DELETE_ITEM).get());

                        pOutput.accept((ModItems.PERMIT_EDITOR_ITEM.get()));

                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
