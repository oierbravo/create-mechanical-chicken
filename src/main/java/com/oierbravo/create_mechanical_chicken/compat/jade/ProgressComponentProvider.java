package com.oierbravo.create_mechanical_chicken.compat.jade;

import com.oierbravo.create_mechanical_chicken.content.components.MechanicalChickenBlockEntity;
import com.oierbravo.create_mechanical_chicken.foundation.utility.ModLang;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.util.Color;

public class ProgressComponentProvider extends AbstractProgressComponentProvider {


    public ProgressComponentProvider(String id) {
        super(id);
    }
}
