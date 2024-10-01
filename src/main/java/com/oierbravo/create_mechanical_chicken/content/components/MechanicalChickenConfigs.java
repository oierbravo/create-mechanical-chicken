package com.oierbravo.create_mechanical_chicken.content.components;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.stream.Collectors;

public class MechanicalChickenConfigs {

    public static ForgeConfigSpec.IntValue PROCESSING_TIME;
    public static ForgeConfigSpec.IntValue OUTPUT_AMOUNT;
    public static ForgeConfigSpec.DoubleValue STRESS_IMPACT;
    public static ForgeConfigSpec.IntValue FLUID_CAPACITY;
    public static ForgeConfigSpec.IntValue REQUIRED_FLUID_AMOUNT;
    public static ForgeConfigSpec.ConfigValue<String> REQUIRED_FLUID;
    public static ForgeConfigSpec.BooleanValue SEED_OIL_ENABLED;

    public static void registerCommonConfig(ForgeConfigSpec.Builder COMMON_BUILDER) {
        COMMON_BUILDER.comment("Settings for the mechanical chicken").push("create_mechanical_chicken");

        PROCESSING_TIME = COMMON_BUILDER
                .comment("Processing time (in ticks)")
                .defineInRange("processingTime", 500, 0, Integer.MAX_VALUE);

        STRESS_IMPACT = COMMON_BUILDER
                .comment("Stress impact")
                .defineInRange("stressImpact", 4.0, 0.0, 64.0);
        FLUID_CAPACITY = COMMON_BUILDER
                .comment("Fluid capacity")
                .defineInRange("fluidCapacity", 1000, 1, Integer.MAX_VALUE);

        REQUIRED_FLUID_AMOUNT = COMMON_BUILDER
                .comment("Required fluid amount")
                .defineInRange("requiredFluidAmount", 100, 0, Integer.MAX_VALUE);

        OUTPUT_AMOUNT = COMMON_BUILDER
                .comment("Output amount")
                .defineInRange("outputAmount", 1, 1, Integer.MAX_VALUE);
        REQUIRED_FLUID = COMMON_BUILDER
                .comment("Required fluid")
                        .define("requiredFluid","create_mechanical_chicken:seed_oil", MechanicalChickenConfigs::validateFluidName);

        SEED_OIL_ENABLED = COMMON_BUILDER.define("seedOilEnabled",true);

        COMMON_BUILDER.pop();
    }
    private static boolean validateFluidName(final Object obj)
    {
        return obj instanceof final String fluidName && ForgeRegistries.FLUIDS.containsKey(new ResourceLocation(fluidName));
    }
}
