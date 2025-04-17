package net.splakra.permitsanddiplomas.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagVisitor;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.splakra.permitsanddiplomas.config.CommonConfig;
import net.splakra.permitsanddiplomas.item.ModItems;
import net.splakra.permitsanddiplomas.storage.DataStorage;
import net.splakra.permitsanddiplomas.storage.WorldDataManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class EnvelopeItem extends Item {

    private int tier;

    public EnvelopeItem(Properties pProperties, int tier) {
        super(pProperties);
        this.tier = tier;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide()) {

            Item permit;

            switch (tier) {
                case 1 -> permit = ModItems.PERMIT_LETTER_1.get();
                case 2 -> permit = ModItems.PERMIT_LETTER_2.get();
                case 3 -> permit = ModItems.PERMIT_LETTER_3.get();
                default -> permit = ModItems.PERMIT_LETTER_4.get();
            }

            ItemStack permitStack = new ItemStack(permit);
            if (Randomize(permitStack, pPlayer)){

                // Set the item in hand
                pPlayer.setItemInHand(pUsedHand, permitStack);
                permitStack.use(pLevel, pPlayer, pUsedHand);
                pPlayer.inventoryMenu.broadcastChanges(); // Force sync
            } else {
                pPlayer.setItemInHand(pUsedHand, ItemStack.EMPTY); // Properly remove the envelope
                pPlayer.inventoryMenu.broadcastChanges(); // Force sync with the client
            }
        }
        pLevel.playSound(pPlayer, pPlayer.getOnPos(), SoundEvents.MEDIUM_AMETHYST_BUD_PLACE, SoundSource.BLOCKS, 1f, 1f);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal("Right-click to open!").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public boolean Randomize(ItemStack pStack, Player pPlayer) {
        CompoundTag tag = pStack.getOrCreateTag();
        String rarity;
        List<String> contentList;
        switch (tier) {
            case 1: rarity = CommonConfig.TIER_1_PREFIX.get(); contentList = CommonConfig.TIER_1_LIST.get(); break;
            case 2: rarity = CommonConfig.TIER_2_PREFIX.get(); contentList = CommonConfig.TIER_2_LIST.get(); break;
            case 3: rarity = CommonConfig.TIER_3_PREFIX.get(); contentList = CommonConfig.TIER_3_LIST.get(); break;
            default: rarity = CommonConfig.TIER_4_PREFIX.get(); contentList = CommonConfig.TIER_4_LIST.get(); break;
        }

        String content = GetRandomContent(contentList, pPlayer);
        if (content != null) {
            tag.putString("rarity", rarity);
            tag.putString("content", content);
        } else {
            return false;
        }
        return true;
    }

    public String GetRandomContent(List<String> list, Player pPlayer){
        DataStorage data = WorldDataManager.getOverworldData();
        var usedPermits = data.getUsedPermits();
        String content = null;
        if (!usedPermits.containsAll(list)){
            Random random = new Random();
            int i = 0;
            do {
                content = list.get(random.nextInt(list.size()));
                i++;
            } while (usedPermits.contains(content) || i > 20);
            if (i > 20){
                return null;
            }
            data.addUsedPermit(content);
        }
        return content;
    }
}
