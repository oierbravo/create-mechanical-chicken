package com.oierbravo.create_mechanical_chicken.compat.jade;

import com.oierbravo.create_mechanical_chicken.content.components.IHavePercent;
import com.oierbravo.create_mechanical_chicken.content.components.MechanicalChickenBlockEntity;
import com.oierbravo.create_mechanical_chicken.foundation.blockEntity.behaviour.CycleBehavior;
import com.oierbravo.create_mechanical_chicken.foundation.utility.ModLang;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.util.Color;

public class AbstractProgressComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    String blockEntityID;

    public AbstractProgressComponentProvider(String id){
        this.blockEntityID = id;
    }

    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getServerData().contains(blockEntityID + ".progress")) {
            int progress = accessor.getServerData().getInt(blockEntityID + ".progress");

            if(progress > 0){
                IElementHelper helper = tooltip.getElementHelper();
                tooltip.add(helper.progress((float)progress / 100, ModLang.translate(blockEntityID + ".tooltip.progress", progress).component(),helper.progressStyle().color(Color.hex("#FFFF00").toInt()), BoxStyle.DEFAULT,true));
            }
        }

    }

    @Override
    public ResourceLocation getUid() {
        return MechanicalChickenPlugin.MECHANICAL_CHICKEN_DATA;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if(blockAccessor.getBlockEntity() instanceof IHavePercent){
            compoundTag.putInt(blockEntityID + ".progress",((IHavePercent) blockAccessor.getBlockEntity()).getProgressPercent());
        }
    }

}
