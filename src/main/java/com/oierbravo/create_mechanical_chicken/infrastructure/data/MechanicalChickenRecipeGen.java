package com.oierbravo.create_mechanical_chicken.infrastructure.data;

import com.oierbravo.create_mechanical_chicken.ModConstants;
import com.oierbravo.create_mechanical_chicken.registrate.ModFluids;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class MechanicalChickenRecipeGen extends RecipeProvider {
    public MechanicalChickenRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }
    // Get the parameters from GatherDataEvent.

    @Override
    protected void buildRecipes(RecipeOutput output) {
        new ProcessingRecipeBuilder<>(MixingRecipe::new, ModConstants.asResource("mixing_chicken_nutrient"))
                .require(Tags.Items.SEEDS)
                .require(Fluids.WATER, 100)
                .output(ModFluids.CHICKEN_NUTRIENT.get(),100)
                .build(output);
    }

    @Override
    public final String getName() {
        return "Mechanical Chicken's recipes.";
    }

}
