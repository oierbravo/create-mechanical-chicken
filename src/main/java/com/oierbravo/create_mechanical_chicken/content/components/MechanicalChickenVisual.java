package com.oierbravo.create_mechanical_chicken.content.components;

import com.mojang.math.Axis;
import com.oierbravo.create_mechanical_chicken.registrate.ModPartials;
import com.oierbravo.mechanical_lemon_lib.foundation.blockEntity.behaviour.CycleBehavior;
import com.oierbravo.mechanical_lemon_lib.foundation.visual.HalfShaftVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlock;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.Direction;
import org.joml.Quaternionf;

import java.util.function.Consumer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class MechanicalChickenVisual extends HalfShaftVisual<MechanicalChickenBlockEntity> implements SimpleDynamicVisual {
    private final OrientedInstance chickenHead;
    private final RotatingInstance cog;
    private final MechanicalChickenBlockEntity mechanicalChickenBlockEntity;
    final Direction horizontalDirection;
    private final Direction opposite;

    public MechanicalChickenVisual(VisualizationContext context, MechanicalChickenBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Direction.NORTH);

        horizontalDirection = blockState.getValue(HORIZONTAL_FACING);
        opposite = horizontalDirection.getOpposite();


        mechanicalChickenBlockEntity = blockEntity;

        chickenHead = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(ModPartials.MECHANICAL_CHICKEN_HEAD)).createInstance();
        Quaternionf q = Axis.YP
                .rotationDegrees(AngleHelper.horizontalAngle(blockState.getValue(MechanicalPressBlock.HORIZONTAL_FACING).getOpposite()));

        chickenHead.rotation(q);
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
        cog.setup(blockEntity)
                .setChanged();

        float progress = getProgress(mechanicalChickenBlockEntity);
        float yPos = 0.0f;
        if(getProgress(mechanicalChickenBlockEntity) > 0)
            yPos = (float) Math.sin(progress) /10;

        chickenHead.position(getVisualPosition())
                .translatePosition(0, .3f + yPos, 0)
                .setChanged();
    }
    @Override
    public void updateLight(float partialTick) {
        super.updateLight(partialTick);
        relight(cog);
        relight(pos, chickenHead);
    }

    private float getProgress(MechanicalChickenBlockEntity pChickenBlockEntity) {
        CycleBehavior cycleBehavior = pChickenBlockEntity.getCycleBehaviour();
        return cycleBehavior.getProgress(AnimationTickHolder.getPartialTicks());
    }
    @Override
    protected void _delete() {
        super._delete();
        cog.delete();
        chickenHead.delete();
    }
    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        super.collectCrumblingInstances(consumer);
        consumer.accept(cog);
        consumer.accept(chickenHead);
    }

}

