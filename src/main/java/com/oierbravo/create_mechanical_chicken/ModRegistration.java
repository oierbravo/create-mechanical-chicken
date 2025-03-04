package com.oierbravo.create_mechanical_chicken;

import com.oierbravo.create_mechanical_chicken.content.components.*;
import com.oierbravo.create_mechanical_chicken.registrate.ModFluids;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.Tags;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class ModRegistration {
	public static final CreateRegistrate REGISTRATE = CreateMechanicalChicken.registrate();

	public static final BlockEntry<MechanicalChickenBlock> MECHANICAL_CHICKEN_BLOCK = REGISTRATE.block("mechanical_chicken", MechanicalChickenBlock::new)
			.initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.METAL))
			.transform(pickaxeOnly())
			.blockstate(BlockStateGen.horizontalBlockProvider(true))
			//.transform(BlockStressDefaults.setImpact(MechanicalChickenConfigs.STRESS_IMPACT.get()))
			.recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
					.define('S',AllBlocks.SHAFT)
                    .define('V', AllBlocks.ITEM_VAULT)
                    .define('C', AllBlocks.COGWHEEL)
                    .define('E', Tags.Items.EGGS)
                    .define('B', AllItems.BRASS_SHEET)
                    .pattern(" C ")
                    .pattern("EVE")
                    .pattern("BSB")
                    .unlockedBy("has_brass_sheet",RegistrateRecipeProvider.has(AllTags.AllItemTags.CASING.tag))
			.save(p, CreateMechanicalChicken.asResource("crafting/" + c.getName())))
			.item()
            .transform(customItemModel())
			.register();


	public static final BlockEntityEntry<MechanicalChickenBlockEntity> MECHANICAL_CHICKEN_BLOCK_ENTITY = REGISTRATE
			.blockEntity("mechanical_chicken_block_entity", MechanicalChickenBlockEntity::new)
			.visual(() -> MechanicalChickenVisual::new)
			.renderer(() -> MechanicalChickenRenderer::new)
			.validBlocks(MECHANICAL_CHICKEN_BLOCK)
			.register();



	public static void init() {
		// load the class and register everything
		CreateMechanicalChicken.LOGGER.info("Registering blocks for " + CreateMechanicalChicken.DISPLAY_NAME);
		if(MechanicalChickenConfigs.SEED_OIL_ENABLED.get()){
			ModFluids.register();
		}

	}
}
