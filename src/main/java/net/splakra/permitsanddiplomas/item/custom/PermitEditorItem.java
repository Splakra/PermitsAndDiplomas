package net.splakra.permitsanddiplomas.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.splakra.permitsanddiplomas.ui.PermitEditorMenu;
import net.splakra.permitsanddiplomas.util.CustomUtils;

public class PermitEditorItem extends Item {
    public PermitEditorItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player pPlayer, InteractionHand pUsedHand) {
        if (pPlayer.getItemInHand(CustomUtils.GetOtherHand(pUsedHand))
                .getItem() instanceof PermitItem) {
            if (!level.isClientSide) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.literal("Permit Editor");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
                        return new PermitEditorMenu(id, inv, new SimpleContainerData(1));
                    }
                });
            }
            // Return a successful result with an empty item stack (since this item doesn't consume the stack).
            return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
        }
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }
}
