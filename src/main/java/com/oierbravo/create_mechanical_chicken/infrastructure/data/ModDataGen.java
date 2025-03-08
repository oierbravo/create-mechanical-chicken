package com.oierbravo.create_mechanical_chicken.infrastructure.data;

import com.oierbravo.create_mechanical_chicken.CreateMechanicalChicken;
import com.oierbravo.create_mechanical_chicken.foundation.data.recipe.MechanicalChickenRecipeGen;
import com.simibubi.create.Create;
import com.tterrag.registrate.providers.RegistrateDataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class ModDataGen {
    public static void gatherData(GatherDataEvent event) {

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        if (event.includeServer()) {
            generator.addProvider(true, new MechanicalChickenRecipeGen(output, event.getLookupProvider()));

        }
        event.getGenerator().addProvider(true, CreateMechanicalChicken.registrate().setDataProvider(new RegistrateDataProvider(CreateMechanicalChicken.registrate(), CreateMechanicalChicken.MODID, event)));
    }
}
