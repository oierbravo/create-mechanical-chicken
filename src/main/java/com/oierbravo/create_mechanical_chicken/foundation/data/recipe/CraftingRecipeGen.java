package com.oierbravo.create_mechanical_chicken.foundation.data.recipe;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.function.Consumer;

public class CraftingRecipeGen extends RecipeProvider {
    public CraftingRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {


    }
    @Override
    public final String getName() {
        return "Mechanical Template's crafting recipes.";
    }
}
