package com.oierbravo.create_mechanical_chicken.foundation.data.recipe;

import com.oierbravo.create_mechanical_chicken.CreateMechanicalChicken;
import com.oierbravo.create_mechanical_chicken.registrate.ModFluids;
import com.simibubi.create.content.kinetics.mixer.CompactingRecipe;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ItemExistsCondition;
import net.minecraftforge.fluids.FluidStack;


import java.util.function.Consumer;

public class MechanicalChickenRecipeGen extends RecipeProvider {
    public MechanicalChickenRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {

        new ProcessingRecipeBuilder<>(CompactingRecipe::new,CreateMechanicalChicken.asResource("compacting_seed_oil"))
                .require(Tags.Items.SEEDS)
                .output(ModFluids.SEED_OIL.get(),25)
                .withCondition(new FluidExistsCondition(ModFluids.SEED_OIL))
                .build(pWriter);

    }
    @Override
    public final String getName() {
        return "Mechanical Chicken's recipes.";
    }

}
