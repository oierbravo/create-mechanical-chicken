package com.oierbravo.create_mechanical_chicken.compat.jade;

import com.oierbravo.create_mechanical_chicken.content.components.MechanicalChickenBlock;
import com.oierbravo.create_mechanical_chicken.content.components.MechanicalChickenBlockEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class MechanicalChickenPlugin implements IWailaPlugin {
    public static final ResourceLocation MECHANICAL_CHICKEN_DATA = new ResourceLocation("create_mechanical_chicken:data");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new ProgressComponentProvider("chicken"), MechanicalChickenBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new ProgressComponentProvider("chicken"), MechanicalChickenBlock.class);
    }
}
