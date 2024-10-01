package com.oierbravo.create_mechanical_chicken.infrastructure.data;

import com.oierbravo.create_mechanical_chicken.foundation.data.recipe.MechanicalChickenRecipeGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;

public class ModDataGen {
    public static void gatherData(GatherDataEvent event) {

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        if (event.includeServer()) {
            generator.addProvider(true, new MechanicalChickenRecipeGen(output));

        }
    }
}
