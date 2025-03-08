package com.oierbravo.create_mechanical_chicken.content.components;

import com.oierbravo.create_mechanical_chicken.infrastructure.ModStress;
import com.simibubi.create.infrastructure.config.CKinetics;
import com.simibubi.create.infrastructure.config.CStress;
import net.createmod.catnip.config.ConfigBase;
import net.neoforged.neoforge.common.ModConfigSpec;

public class MechanicalChickenConfigs extends ConfigBase {
    private static final int VERSION = 1;

    public final ConfigInt processingTime = i(500, 0, Integer.MAX_VALUE, "processingTime", MechanicalChickenConfigs.Comments.processingTime);
    public final ConfigInt outputAmount = i(1, 1, "outputAmount", MechanicalChickenConfigs.Comments.outputAmount);
    //public final ConfigFloat stressImpact = f(4f, 0f, 4096f, "stressImpact", MechanicalChickenConfigs.Comments.stressImpact);
    public final ConfigInt fluidCapacity = i(1000, "fluidCapacity", MechanicalChickenConfigs.Comments.fluidCapacity);
    public final ConfigInt requiredFluidAmount = i(100, "requiredFluidAmount", MechanicalChickenConfigs.Comments.requiredFluidAmount);
    public final ConfigString requiredFluid = s("create_mechanical_chicken:chicken_nutrient","requiredFluid",MechanicalChickenConfigs.Comments.requiredFluid);
    public final ConfigFloat soundVolume = f(0.5f, 0, 1f, "soundVolume", MechanicalChickenConfigs.Comments.soundVolume);

    private static class Comments {
        static String processingTime = "Processing time (in ticks).";
        static String outputAmount = "Output amount.";
        static String stressImpact = "Stress impact.";
        static String fluidCapacity = "Fluid capacity.";
        static String requiredFluidAmount = "Required fluid amount.";
        static String requiredFluid = "Required fluid.";
        static String soundVolume = "Sound volume.";

    }

    @Override
    public String getName() {
        return "mechanical_chicken.v" + VERSION;
    }



    public class ConfigString extends CValue<String, ModConfigSpec.ConfigValue<String>> {

        public ConfigString(String name, String current, String... comment) {
            super(name, builder -> builder.define(name, current), comment);
        }
    }
    protected ConfigString s(String current, String name, String... comment) {
        return new ConfigString(name, current, comment);
    }
}
