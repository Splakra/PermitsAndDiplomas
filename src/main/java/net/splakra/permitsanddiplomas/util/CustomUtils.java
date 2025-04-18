package net.splakra.permitsanddiplomas.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.splakra.permitsanddiplomas.item.custom.PermitItem;

public class CustomUtils {

    public static InteractionHand GetOtherHand(InteractionHand pUsedHand){
        switch(pUsedHand){
            case OFF_HAND: return InteractionHand.MAIN_HAND;
            case MAIN_HAND: return InteractionHand.OFF_HAND;
        }
        return null;
    }

    public static ItemStack GetPermitInHands(Player pPlayer){
        for (InteractionHand h : InteractionHand.values()) {
            ItemStack s = pPlayer.getItemInHand(h);
            if (s.getItem() instanceof PermitItem) {
                return s;
            }
        }
        return ItemStack.EMPTY;
    }

    public static String GetContentOfPermitInHand(Player pPlayer){
        ItemStack permitItem = CustomUtils.GetPermitInHands(pPlayer);
        CompoundTag tag = permitItem.getOrCreateTag();
        if (!tag.contains("content")) {
            return "";
        }
        return tag.getString("content");
    }
}
