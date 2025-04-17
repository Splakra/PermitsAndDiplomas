package net.splakra.permitsanddiplomas.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder().push("Permits and Diplomas - Config");

    public static final ForgeConfigSpec.ConfigValue<String> TIER_1_PREFIX = BUILDER
            .comment("The Prefix for all Tier 1 items")
            .define("Tier 1 Prefix", "Cheap");

    public static final ForgeConfigSpec.ConfigValue<List<String>> TIER_1_LIST = BUILDER
            .comment("The List holding all the Items from Tier 1")
            .define("Tier 1 List", List.of("Wool", "Decorative Plants", "Candles", "Clay",
                    "Item Frames", "Sugar Cane", "Glazed Terracotta", "Prismarine", "Lapis",
                    "Amethysts", "Glowstone", "Bricks", "Paintings", "Crops", "Bamboo"));

    public static final ForgeConfigSpec.ConfigValue<String> TIER_2_PREFIX = BUILDER
            .comment("The Prefix for all Tier 2 items")
            .define("Tier 2 Prefix", "Fair");

    public static final ForgeConfigSpec.ConfigValue<List<String>> TIER_2_LIST = BUILDER
            .comment("The List holding all the Items from Tier 2")
            .define("Tier 2 List", List.of("Light Sources", "Glass", "Potion Ingredients",
                    "Paper", "Concrete", "Quartz", "Belts and Chains", "Dyes", "Fluid Stuff", "Gravel",
                    "Cardboard", "Hostile Mob Drops", "Basic Nether Blocks", "Train Components", "Sand"));

    public static final ForgeConfigSpec.ConfigValue<String> TIER_3_PREFIX = BUILDER
            .comment("The Prefix for all Tier 3 items")
            .define("Tier 3 Prefix", "Pricey");

    public static final ForgeConfigSpec.ConfigValue<List<String>> TIER_3_LIST = BUILDER
            .comment("The List holding all the Items from Tier 3")
            .define("Tier 3 List", List.of("Enchanted Books", "Rose Quartz", "Copper", "Zinc", "Precision Mechanisms",
                    "Vaults", "Bottles o' Enchanting", "Equipment", "Honey and Slime", "Gears and Gearboxes"));

    public static final ForgeConfigSpec.ConfigValue<String> TIER_4_PREFIX = BUILDER
            .comment("The Prefix for all Tier 4 items")
            .define("Tier 4 Prefix", "Luxurious");

    public static final ForgeConfigSpec.ConfigValue<List<String>> TIER_4_LIST = BUILDER
            .comment("The List holding all the Items from Tier 4")
            .define("Tier 4 List", List.of("Iron", "Gold", "Rockets", "Andesite Alloy", "Brass",
                    "Shulker Boxes", "All Stone Types", "All Wood Blocks", "Redstone Bits", "Food"));

    public static final ForgeConfigSpec SPEC = BUILDER.build();



}
