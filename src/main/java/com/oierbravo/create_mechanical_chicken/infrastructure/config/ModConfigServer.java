package com.oierbravo.create_mechanical_chicken.infrastructure.config;

import com.oierbravo.create_mechanical_chicken.content.components.MechanicalChickenConfigs;
import net.createmod.catnip.config.ConfigBase;

public class ModConfigServer extends ConfigBase {
    public final MechanicalChickenConfigs mechanicalChicken = nested(0, MechanicalChickenConfigs::new, "Mechanical Chicken Configs");
    public final ModStress stressValues = nested(1, ModStress::new, "Stress values");

    @Override
    public String getName() {
        return "server";
    }
}
