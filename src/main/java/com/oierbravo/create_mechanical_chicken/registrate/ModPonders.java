package com.oierbravo.create_mechanical_chicken.registrate;

import com.oierbravo.create_mechanical_chicken.CreateMechanicalChicken;
import com.oierbravo.create_mechanical_chicken.ModRegistration;
import com.oierbravo.create_mechanical_chicken.ponders.PonderScenes;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.infrastructure.ponder.AllPonderTags;

public class ModPonders {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(CreateMechanicalChicken.MODID);

    public static void register() {

        HELPER.addStoryBoard(ModRegistration.MECHANICAL_CHICKEN_BLOCK, "mechanical_chicken", PonderScenes::mechanicalChicken, AllPonderTags.KINETIC_APPLIANCES);

        PonderRegistry.TAGS.forTag(AllPonderTags.KINETIC_APPLIANCES)
                .add(ModRegistration.MECHANICAL_CHICKEN_BLOCK);
    }
}
