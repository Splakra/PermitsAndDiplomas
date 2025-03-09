package net.splakra.permitsanddiplomas.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.splakra.permitsanddiplomas.storage.DataStorage;
import net.splakra.permitsanddiplomas.storage.WorldDataManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DataDeleteItem extends Item {
    public DataDeleteItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide) {
            DataStorage data = WorldDataManager.getOverworldData();
            data.clearUsedPermits();
        }

        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal("Creative only! Clears the used-permits data in this world.").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
