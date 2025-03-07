package com.oierbravo.create_mechanical_chicken.compat.jei;

import com.oierbravo.create_mechanical_chicken.CreateMechanicalChicken;
import com.oierbravo.create_mechanical_chicken.ModRegistration;
import com.oierbravo.create_mechanical_chicken.content.components.MechanicalChickenConfigs;
import com.oierbravo.create_mechanical_chicken.foundation.utility.ModLang;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(CreateMechanicalChicken.MODID, "jei_plugin");
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

    public class MechanicalChickenCategory implements IRecipeCategory<MechanicalChickenCategory.MechanicalChickenRecipe> {

        public final static RecipeType<MechanicalChickenRecipe> TYPE = RecipeType.create("create_mechanical_chicken", "production", MechanicalChickenRecipe.class);

        private final IDrawable background;
        private final IDrawable icon;
        private final IDrawable slotDrawable;

        private AnimatedChicken chicken = new AnimatedChicken();


        public MechanicalChickenCategory(IGuiHelper guiHelper) {
            this.background = new IDrawable() {
                @Override
                public int getWidth() {
                    return 176;
                }

                @Override
                public int getHeight() {
                    return 40;
                }

                @Override
                public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
                    AllGuiTextures.JEI_ARROW.render(graphics, 30, 16); //Output arrow
                    AllGuiTextures.JEI_ARROW.render(graphics, 110, 16); //Output arrow
                    chicken.draw(graphics, 82, 35);
                }
            };
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
        public IDrawable getBackground() {
            return this.background;
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
                input.addIngredients(ForgeTypes.FLUID_STACK, recipe.fluid.getMatchingFluidStacks())
                        .addRichTooltipCallback(MechanicalChickenCategory::addFluidAmountTooltip);
            }

            var output = builder.addSlot(RecipeIngredientRole.OUTPUT, 155,15);
            if (recipe.itemStack != null) {
                output.addItemStack(recipe.itemStack)
                        .setBackground(slotDrawable, -1, -1);
            }
        }

        private static void addFluidAmountTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip){
            Optional<FluidStack> displayed = recipeSlotView.getDisplayedIngredient(ForgeTypes.FLUID_STACK);
            if (displayed.isEmpty())
                return;

            FluidStack fluidStack = displayed.get();
            tooltip.add(Component.literal(fluidStack.getAmount() + "mB"));
        }
        @Override
        public void draw(MechanicalChickenRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
            Minecraft minecraft = Minecraft.getInstance();
        }
        private static FluidIngredient getFluidIngredientFromConfig(){
            FluidIngredient fluidIngredient = FluidIngredient.EMPTY;
            final String fluidResourceRaw = MechanicalChickenConfigs.REQUIRED_FLUID.get();
            int configuredAmount = MechanicalChickenConfigs.REQUIRED_FLUID_AMOUNT.get();
            if(fluidResourceRaw.startsWith("#")){
                ResourceLocation fluidTag = ResourceLocation.tryParse(fluidResourceRaw.replace("#",""));
                assert fluidTag != null;
                return FluidIngredient.fromTag(FluidTags.create(fluidTag),configuredAmount);
            }
            final ResourceLocation desiredFluid = new ResourceLocation(fluidResourceRaw);

            if (ForgeRegistries.FLUIDS.containsKey(desiredFluid)) {
                fluidIngredient = FluidIngredient.fromFluid(ForgeRegistries.FLUIDS.getValue(desiredFluid), configuredAmount);
            } else {
                CreateMechanicalChicken.LOGGER.error("Unknown fluid '{}' in config, using default '{}' instead", fluidResourceRaw, "minecraft:water");
                fluidIngredient = FluidIngredient.fromFluid(Fluids.WATER, configuredAmount);
            }
            return fluidIngredient;
        }

        public static List<MechanicalChickenRecipe> getRecipes() {
            List<MechanicalChickenRecipe> recipes = new ArrayList<>();
            int configuredOutputAmount = MechanicalChickenConfigs.OUTPUT_AMOUNT.get();

            recipes.add(new MechanicalChickenRecipe(
                    getFluidIngredientFromConfig(),
                    new ItemStack(Items.EGG,configuredOutputAmount)
            ));
            return recipes;
        }

        public record MechanicalChickenRecipe(@Nullable FluidIngredient fluid, ItemStack itemStack) {

        }
    }
}
