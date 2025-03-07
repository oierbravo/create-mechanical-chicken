package com.oierbravo.create_mechanical_chicken.ponders;

import com.oierbravo.create_mechanical_chicken.ModRegistration;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class ModPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(ModRegistration.MECHANICAL_CHICKEN_BLOCK)
                .addStoryBoard("mechanical_chicken", MechanicalChickenScenes::mechanicalChicken);

    }
}
