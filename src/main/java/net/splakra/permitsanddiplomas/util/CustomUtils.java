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

    public static ItemStack GetItemOfHandByClass(Player pPlayer, Class<?> pItemClass){
        ItemStack itemToSearch =  pPlayer.getMainHandItem();
        if (itemToSearch.isEmpty() || !itemToSearch.getItem().getClass().equals(pItemClass)){
            itemToSearch = pPlayer.getOffhandItem();
        }
        if (itemToSearch.isEmpty())
            return ItemStack.EMPTY;
        return itemToSearch;
    }

    public static String GetNBTofPermit(Player pPlayer){
        ItemStack permitItem = CustomUtils.GetItemOfHandByClass(pPlayer, PermitItem.class);
        CompoundTag tag = permitItem.getOrCreateTag();
        if (!tag.contains("content")) {
            return "";
        }
        return tag.getString("content");
    }
}
