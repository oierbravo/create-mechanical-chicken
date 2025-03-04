package com.oierbravo.create_mechanical_chicken.ponders;

import com.oierbravo.create_mechanical_chicken.ModRegistration;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.KINETIC_APPLIANCES;
public class ModPonderTags {
    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?>> TAG_HELPER = helper.withKeyFunction(RegistryEntry::getId);
        TAG_HELPER.addToTag(KINETIC_APPLIANCES).add(ModRegistration.MECHANICAL_CHICKEN_BLOCK);
    }
}
