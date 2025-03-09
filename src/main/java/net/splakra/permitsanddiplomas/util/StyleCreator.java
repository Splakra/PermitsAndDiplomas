package net.splakra.permitsanddiplomas.util;

import net.minecraft.ChatFormatting;
import net.splakra.permitsanddiplomas.config.CommonConfig;

public class StyleCreator {

    public static ChatFormatting TierToColor(int tier) {
        switch (tier) {
            case 1: return ChatFormatting.DARK_AQUA;
            case 2: return ChatFormatting.DARK_RED;
            case 3: return ChatFormatting.GOLD;
            case 4: return ChatFormatting.DARK_PURPLE;
            default: return ChatFormatting.DARK_GRAY;
        }
    }

    public static int RarityToTier(String rarity) {
        if (rarity.equals(CommonConfig.TIER_1_PREFIX.get())) return 1;
        if (rarity.equals(CommonConfig.TIER_2_PREFIX.get())) return 2;
        if (rarity.equals(CommonConfig.TIER_3_PREFIX.get())) return 3;
        if (rarity.equals(CommonConfig.TIER_4_PREFIX.get())) return 4;
        return 0;
    }
}
