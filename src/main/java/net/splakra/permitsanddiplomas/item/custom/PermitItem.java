package net.splakra.permitsanddiplomas.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PermitItem extends Item {

    public PermitItem(Properties pProperties) {
        super(pProperties);
        pProperties.stacksTo(1);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide()) {
            CompoundTag tag = itemStack.getOrCreateTag();
            boolean isClaimed = tag.getBoolean("claimed");

            if (!isClaimed) {
                ClaimPermit(itemStack, pPlayer);
            } else if (pPlayer.isCrouching()) {
                UnclaimPermit(itemStack, pPlayer);
            }
        }

        return InteractionResultHolder.success(itemStack);
    }

    public void ClaimPermit(ItemStack pItem, Player pPlayer){
        CompoundTag tag = pItem.getOrCreateTag();
        tag.putBoolean("claimed", true);
        tag.putString("owner", pPlayer.getScoreboardName());
        pPlayer.sendSystemMessage(Component.literal("Claimed!"));
    }

    public void UnclaimPermit(ItemStack pItem, Player pPlayer){
        CompoundTag tag = pItem.getOrCreateTag();
        tag.putBoolean("claimed", false);
        tag.remove("owner");
        pPlayer.sendSystemMessage(Component.literal("Unclaimed!"));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        CompoundTag tag = pStack.getTag();
        if (tag != null && tag.getBoolean("claimed")) {
            String owner = tag.getString("owner");
            pTooltipComponents.add(Component.translatable("tooltip.permits_and_diplomas.permit.description_prefix").append(owner));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.permits_and_diplomas.permit.description_default"));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

}
