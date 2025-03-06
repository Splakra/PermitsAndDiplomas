package net.splakra.permitsanddiplomas.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder().push("Permits and Diplomas - Config");

    public static final ForgeConfigSpec.ConfigValue<String> TIER_1_PREFIX = BUILDER
            .comment("The Prefix for all Tier 1 items")
            .define("Tier 1 Prefix", "Cheap");

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> TIER_1_LIST = BUILDER
            .comment("The List holding all the Items from Tier 1")
            .define("Tier 1 List", List.of("Bamboo", "Boats", "Item Frames"));

    public static final ForgeConfigSpec.ConfigValue<String> TIER_2_PREFIX = BUILDER
            .comment("The Prefix for all Tier 1 items")
            .define("Tier 2 Prefix", "Fair");

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> TIER_2_LIST = BUILDER
            .comment("The List holding all the Items from Tier 1")
            .define("Tier 2 List", List.of("Sand", "Gravel", "Dyes"));

    public static final ForgeConfigSpec.ConfigValue<String> TIER_3_PREFIX = BUILDER
            .comment("The Prefix for all Tier 1 items")
            .define("Tier 3 Prefix", "Pricey");

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> TIER_3_LIST = BUILDER
            .comment("The List holding all the Items from Tier 1")
            .define("Tier 3 List", List.of("Meat", "Vegetables", "Stoneworks"));

    public static final ForgeConfigSpec.ConfigValue<String> TIER_4_PREFIX = BUILDER
            .comment("The Prefix for all Tier 1 items")
            .define("Tier 4 Prefix", "Luxurious");

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> TIER_4_LIST = BUILDER
            .comment("The List holding all the Items from Tier 1")
            .define("Tier 4 List", List.of("Redstone Components", "Wood", "Weapons"));

    public static final ForgeConfigSpec SPEC = BUILDER.build();



}
