package com.oierbravo.create_mechanical_chicken.compat.jei;

import com.oierbravo.create_mechanical_chicken.ModConstants;
import com.oierbravo.create_mechanical_chicken.ModRegistration;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(ModConstants.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MechanicalChickenCategory(registration.getJeiHelpers().getGuiHelper()));
    }


    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModRegistration.MECHANICAL_CHICKEN_BLOCK.get()), MechanicalChickenCategory.TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(MechanicalChickenCategory.TYPE, MechanicalChickenCategory.getRecipes());
    }


}
