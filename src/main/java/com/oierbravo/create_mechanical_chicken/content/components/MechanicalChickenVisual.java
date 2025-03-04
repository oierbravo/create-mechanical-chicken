package com.oierbravo.create_mechanical_chicken.content.components;

import com.oierbravo.create_mechanical_chicken.foundation.blockEntity.behaviour.CycleBehavior;
import com.oierbravo.create_mechanical_chicken.registrate.ModPartials;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class MechanicalChickenVisual extends KineticBlockEntityVisual<MechanicalChickenBlockEntity> implements SimpleDynamicVisual {
    private final OrientedInstance chickenHead;
    private final RotatingInstance cog;
    private final MechanicalChickenBlockEntity mechanicalChickenBlockEntity;
    protected final RotatingInstance shaft;
    final Direction direction;
    private final Direction opposite;

    public MechanicalChickenVisual(VisualizationContext context, MechanicalChickenBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        direction = blockState.getValue(HORIZONTAL_FACING);

        opposite = direction.getOpposite();

        shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                .createInstance();

        shaft.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.UP, opposite)
                .setChanged();

        mechanicalChickenBlockEntity = blockEntity;

        chickenHead = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(ModPartials.MECHANICAL_CHICKEN_HEAD)).createInstance();

        cog = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(ModPartials.MECHANICAL_CHICKEN_COG_HORIZONTAL))
                .createInstance();
        cog.setup(blockEntity)
                .setPosition(getVisualPosition())
                .setChanged();
        transformModels(partialTick);

    }
    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        transformModels(ctx.partialTick());
    }

    private void transformModels(float pt) {
        shaft.setup(blockEntity)
                .setChanged();

        cog.setup(blockEntity)
                .setChanged();

        float progress = getProgress(mechanicalChickenBlockEntity);
        float yPos = 0.0f;
        if(getProgress(mechanicalChickenBlockEntity) > 0)
            yPos = (float) Math.sin(progress) /100;

        chickenHead.position(getVisualPosition())
                .translatePosition(0, .3f + yPos, 0)
                .setChanged();
    }
    @Override
    public void updateLight(float partialTick) {
        relight(shaft);
        relight(cog);
        relight(pos, chickenHead);
    }

    private float getProgress(MechanicalChickenBlockEntity pChickenBlockEntity) {
        CycleBehavior cycleBehavior = pChickenBlockEntity.getCycleBehaviour();
        return cycleBehavior.getProgress(AnimationTickHolder.getPartialTicks());
    }
    @Override
    protected void _delete() {
        shaft.delete();
        cog.delete();
        chickenHead.delete();
    }
    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(shaft);
        consumer.accept(cog);
        consumer.accept(chickenHead);
    }

}

