package com.oierbravo.create_mechanical_chicken.registrate;

import com.jozufozu.flywheel.core.PartialModel;
import com.oierbravo.create_mechanical_chicken.CreateMechanicalChicken;

public class ModPartials {
    public static final PartialModel MECHANICAL_CHICKEN_COG_HORIZONTAL = block("mechanical_chicken/cog_horizontal");
    public static final PartialModel MECHANICAL_CHICKEN_HEAD = block("mechanical_chicken/head");

    private static PartialModel block(String path) {
        return new PartialModel(CreateMechanicalChicken.asResource("block/" + path));
    }
    public static void load() {
        // init static fields
    }
}
