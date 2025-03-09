package net.splakra.permitsanddiplomas.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.splakra.permitsanddiplomas.PermitMod;
import net.splakra.permitsanddiplomas.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PermitMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.PERMIT_ENVELOPE_1);
        simpleItem(ModItems.PERMIT_ENVELOPE_2);
        simpleItem(ModItems.PERMIT_ENVELOPE_3);
        simpleItem(ModItems.PERMIT_ENVELOPE_4);

        simpleItem(ModItems.PERMIT_LETTER_1);
        simpleItem(ModItems.PERMIT_LETTER_2);
        simpleItem(ModItems.PERMIT_LETTER_3);
        simpleItem(ModItems.PERMIT_LETTER_4);

        simpleItem(ModItems.DATA_DELETE_ITEM);
        simpleItem(ModItems.PERMIT_EDITOR_ITEM);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(PermitMod.MOD_ID, "item/" + item.getId().getPath()));
    }
}
