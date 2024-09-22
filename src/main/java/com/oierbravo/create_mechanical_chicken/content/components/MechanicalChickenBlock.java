package com.oierbravo.create_mechanical_chicken.content.components;

import com.oierbravo.create_mechanical_chicken.ModRegistration;
import com.oierbravo.create_mechanical_chicken.registrate.ModShapes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MechanicalChickenBlock extends HorizontalKineticBlock implements IBE<MechanicalChickenBlockEntity> {

    public MechanicalChickenBlock(Properties properties) {
        super(properties);
    }
    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        return ModShapes.MECHANICAL_CHICKEN;
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction prefferedSide = getPreferredHorizontalFacing(context);
        if (prefferedSide == null)
            prefferedSide = context.getHorizontalDirection();
        return defaultBlockState().setValue(HORIZONTAL_FACING, context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown() ? prefferedSide : prefferedSide.getOpposite());
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    public Class<MechanicalChickenBlockEntity> getBlockEntityClass() {
        return MechanicalChickenBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MechanicalChickenBlockEntity> getBlockEntityType() {
        return ModRegistration.MECHANICAL_CHICKEN_BLOCK_ENTITY.get();
    }
}
