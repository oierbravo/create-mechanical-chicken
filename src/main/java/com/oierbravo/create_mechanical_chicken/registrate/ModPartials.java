package com.oierbravo.create_mechanical_chicken.registrate;

import com.oierbravo.create_mechanical_chicken.CreateMechanicalChicken;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class ModPartials {
    public static final PartialModel MECHANICAL_CHICKEN_COG_HORIZONTAL = block("mechanical_chicken/cog_horizontal");
    public static final PartialModel MECHANICAL_CHICKEN_HEAD = block("mechanical_chicken/head");

    private static PartialModel block(String path) {
        return PartialModel.of(CreateMechanicalChicken.asResource("block/" + path));
    }
    public static void load() {
        // init static fields
    }
}
