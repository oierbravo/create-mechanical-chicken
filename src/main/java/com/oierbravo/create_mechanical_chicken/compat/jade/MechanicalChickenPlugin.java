package com.oierbravo.create_mechanical_chicken.compat.jade;

import com.oierbravo.create_mechanical_chicken.content.components.MechanicalChickenBlock;
import com.oierbravo.create_mechanical_chicken.content.components.MechanicalChickenBlockEntity;
import com.oierbravo.mechanicals.jade.MechanicalProgressComponentProvider;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class MechanicalChickenPlugin implements IWailaPlugin {
    public static final ResourceLocation MECHANICAL_CHICKEN_DATA = ResourceLocation.parse("create_mechanical_chicken:data");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new MechanicalProgressComponentProvider(MECHANICAL_CHICKEN_DATA), MechanicalChickenBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new MechanicalProgressComponentProvider(MECHANICAL_CHICKEN_DATA), MechanicalChickenBlock.class);
    }
}
