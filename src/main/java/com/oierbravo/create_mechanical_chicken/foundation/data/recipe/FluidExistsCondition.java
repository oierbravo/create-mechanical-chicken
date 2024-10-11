package com.oierbravo.create_mechanical_chicken.foundation.data.recipe;

import com.google.gson.JsonObject;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public class FluidExistsCondition  implements ICondition {
    private static final ResourceLocation NAME = new ResourceLocation("create_mechanical_chicken", "fluid_exists");
    private final ResourceLocation fluid;

    public FluidExistsCondition(String location) {
        this(new ResourceLocation(location));
    }

    public FluidExistsCondition(String namespace, String path) {
        this(new ResourceLocation(namespace, path));
    }

    public FluidExistsCondition(ResourceLocation pFluid) {
        this.fluid = pFluid;
    }

    public FluidExistsCondition(FluidEntry<ForgeFlowingFluid.Flowing> fluid) {
        this(fluid.getId());
    }


    @Override
    public ResourceLocation getID() {
        return NAME;
    }
    public String toString() {
        return "fluid_exists(\"" + this.fluid + "\")";
    }

    public boolean test(ICondition.IContext context) {
        if(fluid.getNamespace().startsWith("#"))
            return true; //Tags always true;
        return ForgeRegistries.FLUIDS.containsKey(this.fluid);
    }
    public static class Serializer implements IConditionSerializer<FluidExistsCondition> {
        public static final FluidExistsCondition.Serializer INSTANCE = new FluidExistsCondition.Serializer();

        public Serializer() {
        }

        public void write(JsonObject json, FluidExistsCondition value) {
            json.addProperty("fluid", value.fluid.toString());
        }

        public FluidExistsCondition read(JsonObject json) {
            return new FluidExistsCondition(new ResourceLocation(GsonHelper.getAsString(json, "fluid")));
        }

        public ResourceLocation getID() {
            return FluidExistsCondition.NAME;
        }

    }
}
