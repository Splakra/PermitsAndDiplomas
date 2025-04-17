package net.splakra.permitsanddiplomas.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.splakra.permitsanddiplomas.storage.DataStorage;
import net.splakra.permitsanddiplomas.storage.WorldDataManager;
import net.splakra.permitsanddiplomas.util.CustomUtils;
import net.splakra.permitsanddiplomas.util.StyleCreator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PermitItem extends Item {

    public PermitItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (!pPlayer.getItemInHand(CustomUtils.GetOtherHand(pUsedHand)).getItem().getClass().equals(PermitEditorItem.class)) {
            CompoundTag tag = itemStack.getOrCreateTag();
            boolean isClaimed = tag.getBoolean("claimed");

            if (!pLevel.isClientSide()) {
                if (!isClaimed) {
                    ClaimPermit(itemStack, pPlayer);
                } else if (pPlayer.isShiftKeyDown()) {
                    if (IsClaimedPlayer(itemStack, pPlayer)) {
                        UnclaimPermit(itemStack, pPlayer);
                    } else {
                        pPlayer.sendSystemMessage(Component.literal("This is already owned by ").append(tag.getString("owner")).append("!"));
                    }
                }
            } else {
                if (!isClaimed) {
                    pPlayer.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.3f, 1f);
                }
            }

            return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    public boolean IsClaimedPlayer(ItemStack pStack, Player pPlayer) {
        CompoundTag tag = pStack.getTag();
        return tag.getString("owner").equals(pPlayer.getScoreboardName());
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
            pTooltipComponents.add(Component.translatable("tooltip.permits_and_diplomas.permit.description_prefix").append(owner).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.permits_and_diplomas.permit.description_default"));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Component getName(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        stack.resetHoverName();

        if (tag != null && tag.contains("rarity") && tag.contains("content")) {
            String prefix = tag.getString("rarity");
            String content = tag.getString("content");

            Component title = Component.literal(prefix)
                    .append(" Permit for ")
                    .append(content).withStyle(StyleCreator.TierToColor(StyleCreator.RarityToTier(prefix)));

            stack.setHoverName(title);
            return title;
        }

        // Default name if no custom data exists
        return super.getName(stack);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        CompoundTag tag = pStack.getTag();
        if (tag != null && tag.contains("claimed")) {
            return tag.getBoolean("claimed");
        }

        //Use original function if its not claimed
        return super.isFoil(pStack);
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {
        DataStorage data = WorldDataManager.getOverworldData();
        CompoundTag tag = itemEntity.getPersistentData();
        if (tag.contains("content")) {
            data.removeUsedPermit(tag.getString("content"));
        }
        super.onDestroyed(itemEntity, damageSource);
    }
}
