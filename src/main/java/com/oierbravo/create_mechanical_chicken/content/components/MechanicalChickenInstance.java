package com.oierbravo.create_mechanical_chicken.content.components;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.mojang.math.Axis;
import com.oierbravo.create_mechanical_chicken.foundation.blockEntity.behaviour.CycleBehavior;
import com.oierbravo.create_mechanical_chicken.registrate.ModPartials;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class MechanicalChickenInstance extends ShaftInstance<MechanicalChickenBlockEntity> implements DynamicInstance {
    private final OrientedData chickenHead;
    private final MechanicalChickenBlockEntity mechanicalChickenBlockEntity;

    public MechanicalChickenInstance(MaterialManager materialManager, MechanicalChickenBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        mechanicalChickenBlockEntity = blockEntity;
        Direction facing = blockState.getValue(HORIZONTAL_FACING);

        BlockState blockState = mechanicalChickenBlockEntity.getBlockState();


        chickenHead = materialManager.defaultSolid()
                .material(Materials.ORIENTED)
                .getModel(ModPartials.MECHANICAL_CHICKEN_HEAD, blockState, facing)
                .createInstance();

    }
    @Override
    public void beginFrame() {

        float progress = getProgress(mechanicalChickenBlockEntity);
        float yPos = 0.0f;
        if(getProgress(mechanicalChickenBlockEntity) > 0)
            yPos = (float) Math.sin(progress) /100;

        chickenHead.setPosition(getInstancePosition())
                .nudge(0, .3f + yPos, 0);

    }
    @Override
    public void updateLight() {

        relight(pos, chickenHead);
        super.updateLight();

    }

    private float getProgress(MechanicalChickenBlockEntity pChickenBlockEntity) {
        CycleBehavior cycleBehavior = pChickenBlockEntity.getCycleBehaviour();
        return cycleBehavior.getProgress(AnimationTickHolder.getPartialTicks());
    }

    @Override
    public void remove() {
        super.remove();
        chickenHead.delete();
    }
    @Override
    protected Instancer<RotatingData> getModel() {
        return getRotatingMaterial().getModel(ModPartials.MECHANICAL_CHICKEN_COG_HORIZONTAL);
    }
}

