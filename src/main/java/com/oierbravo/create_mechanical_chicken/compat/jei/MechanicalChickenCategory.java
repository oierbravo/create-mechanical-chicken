package com.oierbravo.create_mechanical_chicken.compat.jei;

import com.oierbravo.create_mechanical_chicken.CreateMechanicalChicken;
import com.oierbravo.create_mechanical_chicken.ModRegistration;
import com.oierbravo.create_mechanical_chicken.foundation.utility.ModLang;
import com.oierbravo.create_mechanical_chicken.registrate.ModConfigs;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MechanicalChickenCategory implements IRecipeCategory<MechanicalChickenCategory.MechanicalChickenRecipe> {

    public final static RecipeType<MechanicalChickenRecipe> TYPE = RecipeType.create("create_mechanical_chicken", "production", MechanicalChickenRecipe.class);

    //private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slotDrawable;

    private AnimatedChicken chicken = new AnimatedChicken();

    @Override
    public int getWidth() {
        return 176;
    }
    public int getHeight() {
        return 40;
    }


    public MechanicalChickenCategory(IGuiHelper guiHelper) {

        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModRegistration.MECHANICAL_CHICKEN_BLOCK));
        this.slotDrawable = guiHelper.getSlotDrawable();
    }

    @Override
    public RecipeType<MechanicalChickenRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return ModLang.translate("recipe").component();
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MechanicalChickenRecipe recipe, IFocusGroup iFocusGroup) {
        var input = builder.addSlot(RecipeIngredientRole.INPUT, 4, 15)
                .setBackground(slotDrawable, -1, -1);

        if (recipe.fluid != null) {
            input.addIngredients(NeoForgeTypes.FLUID_STACK, recipe.fluid.getMatchingFluidStacks())
                    .addRichTooltipCallback(MechanicalChickenCategory::addFluidAmountTooltip);
        }

        var output = builder.addSlot(RecipeIngredientRole.OUTPUT, 155,15);
        if (recipe.itemStack != null) {
            output.addItemStack(recipe.itemStack)
                    .setBackground(slotDrawable, -1, -1);
        }
    }

    private static void addFluidAmountTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip){
        Optional<FluidStack> displayed = recipeSlotView.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK);
        if (displayed.isEmpty())
            return;

        FluidStack fluidStack = displayed.get();
        tooltip.add(Component.literal(fluidStack.getAmount() + "mB"));
    }
    @Override
    public void draw(MechanicalChickenRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        AllGuiTextures.JEI_ARROW.render(guiGraphics, 30, 16); //Output arrow
        AllGuiTextures.JEI_ARROW.render(guiGraphics, 110, 16); //Output arrow
        chicken.draw(guiGraphics, 82, 35);
    }
    private static FluidIngredient getFluidIngredientFromConfig(){
        FluidIngredient fluidIngredient = FluidIngredient.EMPTY;
        final String fluidResourceRaw = ModConfigs.server().mechanicalChicken.requiredFluid.get();
        int configuredAmount = ModConfigs.server().mechanicalChicken.requiredFluidAmount.get();
        if(fluidResourceRaw.startsWith("#")){
            ResourceLocation fluidTag = ResourceLocation.tryParse(fluidResourceRaw.replace("#",""));
            assert fluidTag != null;
            return FluidIngredient.fromTag(FluidTags.create(fluidTag),configuredAmount);
        }
        final ResourceLocation desiredFluid = ResourceLocation.parse(fluidResourceRaw);

        if (BuiltInRegistries.FLUID.containsKey(desiredFluid)) {
            fluidIngredient = FluidIngredient.fromFluid(BuiltInRegistries.FLUID.get(desiredFluid), configuredAmount);
        } else {
            CreateMechanicalChicken.LOGGER.error("Unknown fluid '{}' in config, using default '{}' instead", fluidResourceRaw, "minecraft:water");
            fluidIngredient = FluidIngredient.fromFluid(Fluids.WATER, configuredAmount);
        }
        return fluidIngredient;
    }

    public static List<MechanicalChickenRecipe> getRecipes() {
        List<MechanicalChickenRecipe> recipes = new ArrayList<>();
        int configuredOutputAmount = ModConfigs.server().mechanicalChicken.outputAmount.get();

        recipes.add(new MechanicalChickenRecipe(
                getFluidIngredientFromConfig(),
                new ItemStack(Items.EGG,configuredOutputAmount)
        ));
        return recipes;
    }

    public record MechanicalChickenRecipe(@Nullable FluidIngredient fluid, ItemStack itemStack) {

    }
}